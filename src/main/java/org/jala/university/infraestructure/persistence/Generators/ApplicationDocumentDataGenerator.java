package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.entity.CreditCardApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ApplicationDocumentDataGenerator {

    private static final Random random = new Random();
    private static final String[] DOCUMENT_TYPES = {"ID front", "ID back", "Salary bonus"};
    private static final String[] MIME_TYPES = {"application/pdf", "image/jpeg", "image/png"};

    public static ApplicationDocument generateApplicationDocument(CreditCardApplication application) {
        String documentType = DOCUMENT_TYPES[random.nextInt(DOCUMENT_TYPES.length)];
        String mimeType = MIME_TYPES[random.nextInt(MIME_TYPES.length)];
        String extension = getExtension(mimeType); 
        String fileName = ""; 

        if (extension.equals("error")) {
            System.out.println("Error: Invalid mime type");
            return null; 
        } else {
            fileName = "document_" + System.currentTimeMillis() + "." + extension; 
        }
        
        byte[] content = generateRandomContent(1024); 

        return new ApplicationDocument(application, documentType, fileName, mimeType, content);
    }

    public static List<ApplicationDocument> generateApplicationDocumentList(CreditCardApplication application, int count) {
        List<ApplicationDocument> documents = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            documents.add(generateApplicationDocument(application));
        }
        return documents;
    }

    private static byte[] generateRandomContent(int length) {
        byte[] content = new byte[length];
        random.nextBytes(content);
        return content;
    }

    public static String getExtension(String mimeType) {
        switch (mimeType) {
            case "application/pdf":
                return "pdf"; 
            case "image/jpeg":
                return "jpg"; 
            case "image/png":
                return "png"; 
            default:
                return "error"; 
        }
    }
}
