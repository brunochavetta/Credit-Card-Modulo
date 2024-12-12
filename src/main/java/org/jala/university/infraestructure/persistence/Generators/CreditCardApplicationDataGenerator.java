package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreditCardApplicationDataGenerator {

    private static final Random random = new Random();

    public static CreditCardApplication generateCreditCardApplication(Customer customer) {
        LocalDateTime applicationDate = LocalDateTime.now();
        CreditCardApplication.ApplicationStatus status = CreditCardApplication.ApplicationStatus.submitted; 
        List<ApplicationDocument> documents = generateApplicationDocuments(customer); 

        return new CreditCardApplication(customer, applicationDate, status, documents);
    }

    public static List<CreditCardApplication> generateCreditCardApplicationList(Customer customer, int count) {
        List<CreditCardApplication> applications = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            applications.add(generateCreditCardApplication(customer));
        }
        return applications;
    }

    private static List<ApplicationDocument> generateApplicationDocuments(Customer customer) {
        List<ApplicationDocument> documents = new ArrayList<>();

        for (int i = 0; i < random.nextInt(5) + 1; i++) { 
            ApplicationDocument document = ApplicationDocumentDataGenerator.generateApplicationDocument(generateCreditCardApplication(customer)); 
            if (document != null) { 
                documents.add(document);
            }
        }
        return documents;
    }
}
