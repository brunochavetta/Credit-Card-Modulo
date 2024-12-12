package org.jala.university.presentation.controller.CreateEntityController;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.DocumentDto;
import org.jala.university.application.mapper.DocumentMapper;
import org.jala.university.application.service.ApplicationDocumentService;
import org.jala.university.domain.entity.ApplicationDocument;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ReviewDocumentController {

    private ApplicationDocumentService documentService;
    private CreateApplicationDocumentController documentController;

    private static final String TEMP_DIR = "temp";

    static {
        System.loadLibrary("opencv_java460");
    }

    public ReviewDocumentController(CreateApplicationDocumentController documentController) {
        this.documentService = ServiceFactoryCreditCard.documentServiceFactory();
        this.documentController = documentController;
    }

    public boolean validateDocumentContent(String extractedText, String expectedID) {
        return extractedText.contains(expectedID);
    }

    public Double verifySalary(String extractedText, String expectedID) {

        // Primer intento: buscar salario neto
        Pattern pattern = Pattern.compile(
                "\\b(?:neto a cobrar|neto|net|salario neto|wage net|salary net):?\\s*\\$?\\s*(\\d+(?:[.,]\\d+)?)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(extractedText);

        if (matcher.find()) {
            Double salary = Double.parseDouble(matcher.group(1).replace(".", ""));
            return salary;
        }

        // Segundo intento: buscar coincidencia con solo $
        pattern = Pattern.compile("\\$\\s*(\\d+(?:[.,]\\d+)?)(?:\\s+(\\d+(?:[.,]\\d+)?))?");
        matcher = pattern.matcher(extractedText);

        if (matcher.find()) {
            StringBuilder salaryBuilder = new StringBuilder(matcher.group(1));
            // Si hay un segundo grupo, se agrega
            if (matcher.group(2) != null) {
                salaryBuilder.append(matcher.group(2));
            }
            Double salary = Double.parseDouble(salaryBuilder.toString().replace(",", "."));
            return salary;
        }

        // Tercer intento: buscar formato separado (por espacio)
        pattern = Pattern.compile("(\\d+)\\s+(\\d+(?:[.,]\\d+)?)");
        matcher = pattern.matcher(extractedText);

        if (matcher.find()) {
            String combinedSalary = matcher.group(1) + matcher.group(2);
            Double salary = Double.parseDouble(combinedSalary.replace(",", "."));
            return salary;
        }

        return Double.valueOf(0);
    }

    public String returnTextByDocument (ApplicationDocument applicationDocument) {
        if (applicationDocument == null) {
            System.out.println("Document is empty");
            return null;
        }

        String extension = documentController.getFileExtension(applicationDocument.getFileName());
        String path = TEMP_DIR + extension;

        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(applicationDocument.getContent());
        } catch (IOException e) {
            System.err.println("Error writing to temporary file: " + e.getMessage());
            return null;
        }

        String result = "";

        if (extension.equalsIgnoreCase(".pdf")) {
            result = processPdfFile(file);
        } else if (isImageExtension(extension)) {
            result = processImageFile(file);
        } else {
            System.err.println("Unsupported file type: " + extension);
        }

        if (file.exists()) {
            file.delete();
        }

        return result;
    }

    public String reviewDocument(UUID id) {
        DocumentMapper documentMapper = new DocumentMapper();
        DocumentDto documentDto = documentService.findById(id);
        ApplicationDocument applicationDocument = documentMapper.mapFrom(documentDto);

        if (applicationDocument == null) {
            System.out.println("Document not found with ID: " + id);
            return null;
        }

        return returnTextByDocument(applicationDocument); 
    }

    private String processPdfFile(File file) {
        StringBuilder allText = new StringBuilder();

        try (PDDocument documentPDF = PDDocument.load(file)) {
            PDFRenderer pdfRenderer = new PDFRenderer(documentPDF);
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata/");
            tesseract.setLanguage("spa");

            for (int page = 0; page < documentPDF.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                String result = tesseract.doOCR(bim);
                allText.append("Text extracted from page ").append(page + 1).append(": ").append(result).append("\n");
                System.out.println("Text extracted from page " + (page + 1) + result);
            }
        } catch (IOException | TesseractException e) {
            System.err.println("Error processing PDF: " + e.getMessage());
        }

        return allText.toString();
    }

    private String processImageFile(File file) {
        StringBuilder allText = new StringBuilder();

        try {
            BufferedImage bufferedImage = ImageIO.read(file);

            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("/usr/share/tesseract-ocr/5/tessdata/");
            tesseract.setLanguage("spa");

            String result = tesseract.doOCR(bufferedImage);
            allText.append(result).append("\n");
            System.out.println("Text extracted:  " + result);
        } catch (IOException | TesseractException e) {
            System.err.println("Error processing image: " + e.getMessage());
        }

        return allText.toString();
    }

    private boolean isImageExtension(String extension) {
        return extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")
                || extension.equalsIgnoreCase(".png");
    }
}