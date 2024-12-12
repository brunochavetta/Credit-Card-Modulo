package org.jala.university.application.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.DebtDto;
import org.jala.university.application.dto.SendNotificationDto;
import org.jala.university.application.dto.TransactionDto;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.mapper.DebtMapper;
import org.jala.university.application.mapper.TransactionMapper;
import org.jala.university.application.service.CreditCardDebtService;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.application.service.CreditCardTransactionService;
import org.jala.university.application.service.NotificationService;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardTransaction;
import org.jala.university.domain.entity.Notification;
import org.jala.university.domain.repository.CreditCardTransactionRepository;

public class CreditCardTransactionServiceImpl implements CreditCardTransactionService {
    private final CreditCardTransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper; 
    private final CreditCardService creditCardService; 
    private final NotificationService notificationService; 
    private final CreditCardDebtService debtService; 

    public CreditCardTransactionServiceImpl(CreditCardTransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository; 
        this.transactionMapper = transactionMapper; 
        this.creditCardService = ServiceFactoryCreditCard.creditCardServiceFactory(); 
        this.notificationService = ServiceFactoryCreditCard.notificationServiceFactory(); 
        this.debtService = ServiceFactoryCreditCard.debtServiceFactory(); 
    }

    @Override
    public TransactionDto save(TransactionDto transaction) {
        Notification notification = new Notification();
        notification.setType("Transaction notification");
        CreditCardTransaction newTransaction = null; 
        try {
            if (transaction.getCreditCard().getSelectCreditLimit() >= (transaction.getAmount() + transaction.getCreditCard().getTotalSpent())) {
                transaction.setDebt(debtCreate(transaction.getCreditCard(),transaction.getTransactionDate()));
                transactionRepository.save(transactionMapper.mapFrom(transaction));
                notification.setSubject("Successful Transaction");
                notification.setMessage("The transaction " + transaction.getDescription() + " for $" + transaction.getAmount() + " was successfully completed. Date: " + transaction.getTransactionDate().getDayOfMonth() + "/" + transaction.getTransactionDate().getDayOfMonth() + ", time: " + transaction.getTransactionDate().getHour());
                double actualTotalSpend = transaction.getCreditCard().getTotalSpent() + transaction.getAmount();
                transaction.getCreditCard().setTotalSpent(actualTotalSpend);
                creditCardService.update(new CreditCardMapper().mapTo(transaction.getCreditCard()));
                verifyLimit(actualTotalSpend, transaction.getCreditCard().getSelectCreditLimit());
            } else {
                notification.setSubject("Failed transaction. Insufficient funds");
                notification.setMessage("Sorry, you have insufficient funds. The transaction " + transaction.getDescription() + " for $" + transaction.getAmount() + " could not be completed. Date: " + transaction.getTransactionDate().getDayOfMonth() + "/" + transaction.getTransactionDate().getDayOfMonth() + ", time: " + transaction.getTransactionDate().getHour());
            }
        } catch (Exception e) {
            notification.setSubject("Sorry, the transaction could not be processed");
            notification.setMessage("Sorry. Transaction " + transaction.getDescription() + " for $" + transaction.getAmount() + " could not be processed successfully. Date: " + transaction.getTransactionDate().getDayOfMonth() + "/" + transaction.getTransactionDate().getDayOfMonth() + ", time: " + transaction.getTransactionDate().getHour());
            throw new RuntimeException("Error saving credit card transaction: " + e.getMessage());
        } finally {
            SendNotificationDto notificationDTO = new SendNotificationDto(notification);
            notificationService.sendNotification(notificationDTO);
        }

        return transactionMapper.mapTo(newTransaction); 
    }

    private void verifyLimit(double actualTotalSpend, double selectCreditLimit) {
        if (selectCreditLimit == 0) {
            throw new IllegalArgumentException("Credit limit cannot be zero.");
        }

        double percentage = (actualTotalSpend / selectCreditLimit) * 100;

        if (percentage >= 80) {
            Notification notification = new Notification();
            notification.setType("Limit Exceeded notification");
            notification.setSubject("Warning: Credit limit nearly reached");
            notification.setMessage("You have reached " + percentage + " of your credit limit.");

            SendNotificationDto notificationDTO = new SendNotificationDto(notification);
            notificationService.sendNotification(notificationDTO);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            transactionRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card transaction: " + e.getMessage());
        }
    }

    @Override
    public TransactionDto findById(UUID id) {
        try {
            return transactionMapper.mapTo(transactionRepository.findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error finding credit card transaction: " + e.getMessage());
        }
    }

    @Override
    public List<TransactionDto> findAllByCreditCard(UUID idCreditCard) {
        try {
            CreditCard creditCard = new CreditCardMapper().mapFrom(creditCardService.findById(idCreditCard));
            List<CreditCardTransaction> transactions =  transactionRepository.findAllByCreditCard(creditCard);
            return transactions.stream().map(transactionMapper::mapTo).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit card transactions: " + e.getMessage());
        }
    }

    @Override
    public List<TransactionDto> findTransactionsByCardAndDateRange(UUID idCreditCard, LocalDate startDate, LocalDate endDate) {
        CreditCard creditCard = new CreditCardMapper().mapFrom(creditCardService.findById(idCreditCard));
        List<CreditCardTransaction> creditCardTransactions;
        if (creditCard != null) {
            creditCardTransactions = transactionRepository.findTransactionsByCardAndDateRange(creditCard, startDate, endDate);
        } else {
            creditCardTransactions = null;
        }

        return creditCardTransactions.stream().map(transactionMapper::mapTo).toList();
    }

    @Override
    public CreditCardDebt debtCreate(CreditCard creditCard, LocalDateTime localDate) {
        DebtMapper debtMapper = new DebtMapper(); 
        DebtDto debtDto = debtService.IsExistsByCreditCardIdAndMonth(creditCard, localDate.getMonth().toString());
        CreditCardDebt debt = debtMapper.mapFrom(debtDto); 
        
        if (debt == null && debt.getDueDate().getYear() != LocalDate.now().getYear()) {
            return debtMapper.mapFrom(debtService.generateMonthlyDebt(creditCard));
        }
        return debt;
    }

    @Override
    public List<TransactionDto> findTransactionsByDebt(UUID debtId) {
        CreditCardDebt debt = new DebtMapper().mapFrom(debtService.findById(debtId));
        List<CreditCardTransaction> transactions =  transactionRepository.findTransactionsByDebt(debt);
        return transactions.stream().map(transactionMapper::mapTo).toList(); 
    }

}
