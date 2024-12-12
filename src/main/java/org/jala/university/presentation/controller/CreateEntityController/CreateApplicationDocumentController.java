package org.jala.university.presentation.controller.CreateEntityController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.DocumentDto;
import org.jala.university.application.mapper.DocumentMapper;
import org.jala.university.application.service.ApplicationDocumentService;
import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.entity.CreditCardApplication;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static org.jala.university.presentation.MainAppCreditCard.primaryStage;

public class CreateApplicationDocumentController {
    private final ApplicationDocumentService documentService;
    private Stage stage;
    private ReviewDocumentController reviewDocumentController;
    private final DocumentMapper documentMapper;

    public CreateApplicationDocumentController() {
        this.documentService = ServiceFactoryCreditCard.documentServiceFactory();
        this.stage = primaryStage;
        this.documentMapper = new DocumentMapper();
    }

    public void setReviewDocumentController(ReviewDocumentController reviewDocumentController) {
        this.reviewDocumentController = reviewDocumentController;
    }

    public ApplicationDocument saveDocument(CreditCardApplication application, String documentType, String idCustomer) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a document to save");
    
        File fileToSave = fileChooser.showOpenDialog(stage);
    
        if (fileToSave != null) {
            String fileName = fileToSave.getName();
            String fileExtension = getFileExtension(fileName).toLowerCase(); 
    
            if (!isValidFileExtension(fileExtension)) {
                showAlert("Error: The file extension is not allowed. Only .png, .jpg, and .pdf are permitted.");
                return null; 
            }
    
            try (InputStream inputStream = Files.newInputStream(fileToSave.toPath())) {
                byte[] content = inputStream.readAllBytes();
                String mimeType = Files.probeContentType(fileToSave.toPath());
                
                ApplicationDocument document = new ApplicationDocument(application, documentType, fileName, mimeType, content);

                String result = reviewDocumentController.returnTextByDocument(document); 

                if (!reviewDocumentController.validateDocumentContent(result, idCustomer) && documentType.equals("Salary Bonus")) {
                    showAlert("Error: The ID expressed in the " + documentType + " is not valid");
                    return null; 
                } else {
                    showAlert("Document saved successfully.");
                    DocumentDto documentDto = documentService.save(documentMapper.mapTo(document));
                    return documentMapper.mapFrom(documentDto);
                }
                
            } catch (IOException e) {
                showAlert("Error saving document: " + e.getMessage());
            }
        }
        return null; 
    }
    
    private boolean isValidFileExtension(String fileExtension) {
        return fileExtension.equals(".png") || fileExtension.equals(".jpg") || fileExtension.equals(".pdf");
    }
    

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public ApplicationDocument viewDocument(UUID id) {
        try {
            DocumentDto documentDto = documentService.findById(id);
            ApplicationDocument document = documentMapper.mapFrom(documentDto);
            if (document.getContent() == null || document.getContent().length == 0) {
                showAlert("Error: Document content is empty.");
                return null;
            }

            String fileName = sanitizeFileName(document.getFileName());
            String fileExtension = getFileExtension(fileName);
            File tempFile = File.createTempFile("tempDocument_", fileExtension);
            tempFile.deleteOnExit();

            System.out.println("File Name: " + fileName);
            System.out.println("File Extension: " + fileExtension);

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(document.getContent());
            }

            if (tempFile.exists() && tempFile.length() > 0) {
                String fileUrl = tempFile.toURI().toString();
                System.out.println("File URL: " + fileUrl);
                
                openDefaultBrowser(fileUrl);

                return document; 
            } else {
                showAlert("Error: Temporary file was not created or is empty.");
            }

        } catch (IOException e) {
            showAlert("Error viewing document: " + e.getMessage());
        }
        return null;
    }

    private String sanitizeFileName(String fileName) {
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static void openDefaultBrowser(String url) throws IOException {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows"))
            openDefaultBrowserWindows(url);
        else if (osName.contains("Linux"))
            openDefaultBrowserLinux(url);
        else if (osName.contains("Mac OS X"))
            openDefaultBrowserMacOsx(url);
    }

    public static void openDefaultBrowserWindows(String url) throws IOException {
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
    }
    
    public static void openDefaultBrowserLinux(String url) throws IOException {
        Runtime.getRuntime().exec("xdg-open " + url);
    }
    
    public static void openDefaultBrowserMacOsx(String url) throws IOException {
        Runtime.getRuntime().exec("open " + url);
    }

    public String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < fileName.length() - 1) {
            return fileName.substring(lastIndexOfDot);
        }
        return "";
    }
}