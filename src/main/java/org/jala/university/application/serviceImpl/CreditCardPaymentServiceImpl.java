package org.jala.university.application.serviceImpl;

import org.jala.university.ServiceFactory;
import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.PaymentDto;
import org.jala.university.application.dto.SendNotificationDto;
import org.jala.university.application.mapper.AccountMapper;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.mapper.DebtMapper;
import org.jala.university.application.mapper.PaymentMapper;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.application.service.NotificationService;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardPayment;
import org.jala.university.domain.entity.Notification;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.repository.CreditCardPaymentRepository;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.application.service.CreditCardDebtService;
import org.jala.university.application.service.CreditCardPaymentService;

import java.util.List;
import java.util.UUID;

public class CreditCardPaymentServiceImpl implements CreditCardPaymentService {
    private final CreditCardPaymentRepository paymentRepository; 
    private final PaymentMapper paymentMapper; 
    private final CreditCardDebtService debtService; 
    private final NotificationService notificationService; 
    private final CreditCardService creditCardService;

    public CreditCardPaymentServiceImpl(CreditCardPaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository; 
        this.paymentMapper = paymentMapper; 
        this.creditCardService = ServiceFactoryCreditCard.creditCardServiceFactory();
        this.debtService = ServiceFactoryCreditCard.debtServiceFactory(); 
        this.notificationService = ServiceFactoryCreditCard.notificationServiceFactory(); 
    }

    @Override
    public PaymentDto save(PaymentDto paymentDto) {
        Notification notification = new Notification();
        notification.setType("Payment notification");
        CreditCardPayment payment = null; 
        try {
            if (paymentDto.getDebt().getOutstandingAmount() <= 0 || paymentDto.getDebt().getOutstandingAmount() < paymentDto.getAmount()){
                notification.setSubject("Payment error: The payment is greater than the amount to be paid");
                notification.setMessage("We are sorry, the payment of $" + paymentDto.getAmount() + " of the debt corresponding to the month of " + payment.getCreditCardDebt().getDueDate().getMonthValue() + ", is greater than the debt. Check the amount to pay and try again.");
            } else {
                payment = paymentRepository.save(paymentMapper.mapFrom(paymentDto));
                notification.setSubject("Successful payment");
                notification.setMessage("The payment of $" + paymentDto.getAmount() + " of the debt corresponding to the month of "  + payment.getCreditCardDebt().getDueDate().getMonthValue() + " was successfully made. Hour " + payment.getPaymentDate().getHour() + ", on date " + payment.getPaymentDate().getDayOfMonth() + "/" + payment.getPaymentDate().getMonth());
                processPayment(paymentDto);
            }
            System.out.println("Sending notification with subject: " + notification.getSubject());
            System.out.println("Sending notification with message: " + notification.getMessage());
        } catch (Exception e) {
            notification.setSubject("An error occurred while processing the payment");
            notification.setMessage("We are sorry, the payment of $" + paymentDto.getAmount() + " of the debt corresponding to the month of " + payment.getCreditCardDebt().getDueDate().getMonthValue() + ", could not be made. Try again later");
            throw new RuntimeException("Error saving credit card payment: " + e.getMessage());
        } finally {
            SendNotificationDto notificationDTO = new SendNotificationDto(notification);
            notificationService.sendNotification(notificationDTO);
        }
        return paymentMapper.mapTo(payment); 
    }

    @Override
    public PaymentDto update(PaymentDto paymentDto) {
        try {
            CreditCardPayment payment = paymentRepository.save(paymentMapper.mapFrom(paymentDto));
            return paymentMapper.mapTo(payment); 
        } catch (Exception e) {
            throw new RuntimeException("Error updating credit card payment: " + e.getMessage());
        }
    }

    @Override
    public void delete(PaymentDto payment) {
        try {
            paymentRepository.delete(paymentMapper.mapFrom(payment));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card payment: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            paymentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card payment: " + e.getMessage());
        }
    }

    @Override
    public PaymentDto findById(UUID id) {
        try {
            return paymentMapper.mapTo(paymentRepository.findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error finding credit card payment: " + e.getMessage());
        }
    }

    @Override
    public List<PaymentDto> findAll() {
        try {
            List<CreditCardPayment> payments = paymentRepository.findAll();
            return payments.stream().map(paymentMapper::mapTo).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit card payments: " + e.getMessage());
        }
    }

    @Override
    public void processPayment(PaymentDto payment) {
        CreditCardMapper creditCardMapper = new CreditCardMapper(); 
        AccountMapper accountMapper = new AccountMapper(); 
        DebtMapper debtMapper = new DebtMapper(); 
        CreditCardDebt debt = debtMapper.mapFrom(debtService.findById(payment.getDebt().getId())); 
        
        if (debt == null || debt.getOutstandingAmount() <= 0) {
            throw new IllegalStateException("No outstanding debt to apply the payment.");
        }

        creditCardService.updateTotalSpend(creditCardMapper.mapTo(debt.getCreditCard()), payment.getAmount());
    
        Double newOutstandingAmount = debt.getOutstandingAmount() - payment.getAmount();
        debt.setOutstandingAmount(newOutstandingAmount);

        Account account = CustomerSession.getInstance().getCurrentCustomer().getAccount(); 
        account.setBalance(account.getBalance() - payment.getAmount());
        ServiceFactory.accountService().updateAccount(accountMapper.mapTo(account)); 
    
        if (newOutstandingAmount <= 0) {
            debt.setStatus(CreditCardDebt.DebtStatus.full_paid);
        } else {
            debt.setStatus(CreditCardDebt.DebtStatus.partial_paid);
        }
    
        debt.getPayments().add(paymentMapper.mapFrom(payment));

        debtService.update(debtMapper.mapTo(debt));
    }
}
