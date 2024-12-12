package org.jala.university;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.jala.university.application.mapper.*; 
import org.jala.university.application.service.*; 
import org.jala.university.application.serviceImpl.*; 
import org.jala.university.domain.repository.*; 
import org.jala.university.infraestructure.persistence.RepositoryImpl.*; 

public class ServiceFactoryCreditCard {
    private static CreditCardApplicationService applicationService;
    private static CustomerService customerService;
    private static ApplicationDocumentService documentService;
    private static CreditCardService creditCardService; 
    private static CreditCardTypeService typeService; 
    private static CreditCardPaymentService paymentService; 
    private static CreditCardTransactionService transactionService; 
    private static CreditCardDebtService debtService; 
    private static NotificationService notificationService; 
    private static UserNotificationPreferencesService preferencesService; 

    private ServiceFactoryCreditCard() {
    }

    private static EntityManager createEntityManager() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("creditCardPU");
        return entityManagerFactory.createEntityManager();
    }


    public static CreditCardApplicationService creditCardApplicationService() {
        if (applicationService != null) {
            return applicationService;
        }
        CreditCardApplicationRepository applicationRepository = new CreditCardApplicationRepositoryImpl(createEntityManager());
        ApplicationMapper applicationMapper = new ApplicationMapper();
        applicationService = new CreditCardApplicationServiceImpl(applicationRepository, applicationMapper);
        return applicationService;
    }

    public static CustomerService customerServiceFactory() {
        if (customerService != null) {
            return customerService;
        }

        CustomerRepository customerRepository = new CustomerRepositoryImpl(createEntityManager());
        CustomerMapper customerMapper = new CustomerMapper();
        customerService = new CustomerServiceImpl(customerRepository, customerMapper);
        return customerService;
    }

    public static ApplicationDocumentService documentServiceFactory() {
        if (documentService != null) {
            return documentService;
        }

        ApplicationDocumentRepository documentRepository = new ApplicationDocumentRepositoryImpl(createEntityManager());
        DocumentMapper documentMapper = new DocumentMapper();
        documentService = new ApplicationDocumentServiceImpl(documentRepository, documentMapper);
        return documentService;
    }

    public static CreditCardService creditCardServiceFactory() {
        if (creditCardService != null) {
            return creditCardService;
        }

        CreditCardRepository creditCardRepository = new CreditCardRepositoryImpl(createEntityManager());
        CreditCardMapper creditCardMapper = new CreditCardMapper();
        creditCardService = new CreditCardServiceImpl(creditCardRepository, creditCardMapper);
        return creditCardService;
    }

    public static CreditCardTypeService typeServiceFactory() {
        if (typeService != null) {
            return typeService;
        }

        CreditCardTypeRepository typeRepository = new CreditCardTypeRepositoryImpl(createEntityManager());
        CreditCardTypeMapper typeMapper = new CreditCardTypeMapper();
        typeService = new CreditCardTypeServiceImpl(typeRepository, typeMapper);
        return typeService;
    }

    public static CreditCardPaymentService paymentServiceFactory() {
        if (paymentService != null) {
            return paymentService;
        }

        CreditCardPaymentRepository paymentRepository = new CreditCardPaymentRepositoryImpl(createEntityManager());
        PaymentMapper paymentMapper = new PaymentMapper();
        paymentService = new CreditCardPaymentServiceImpl(paymentRepository, paymentMapper);
        return paymentService;
    }

    public static CreditCardTransactionService transactionServiceFactory() {
        if (transactionService != null) {
            return transactionService;
        }

        CreditCardTransactionRepository transactionRepository = new CreditCardTransactionRepositoryImpl(createEntityManager());
        TransactionMapper transactionMapper = new TransactionMapper();
        transactionService = new CreditCardTransactionServiceImpl(transactionRepository, transactionMapper);
        return transactionService;
    }

    public static CreditCardDebtService debtServiceFactory() {
        if (debtService != null) {
            return debtService;
        }

        CreditCardDebtRepository debtRepository = new CreditCardDebtRepositoryImpl(createEntityManager());
        DebtMapper debtMapper = new DebtMapper();
        debtService = new CreditCardDebtServiceImpl(debtRepository, debtMapper);
        return debtService;
    }

    public static NotificationService notificationServiceFactory() {
        if (notificationService != null) {
            return notificationService;
        }

        NotificationRepository notificationRepository = new NotificationRepositoryImpl(createEntityManager());
        NotificationMapper notificationMapper = new NotificationMapper();
        notificationService = new NotificationServiceImpl(notificationRepository, notificationMapper);
        return notificationService;
    }

    public static UserNotificationPreferencesService preferencesServiceFactory() {
        if (preferencesService != null) {
            return preferencesService;
        }

        UserNotificationPreferencesRepository preferencesRepository = new UserNotificationPreferencesRepositoryImpl(createEntityManager());
        PreferencesMapper preferencesMapper = new PreferencesMapper();
        preferencesService = new UserNotificationPreferencesServiceImpl(preferencesRepository, preferencesMapper);
        return preferencesService;
    }

}
