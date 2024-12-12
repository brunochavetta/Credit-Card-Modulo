package org.jala.university.application.service;

import org.jala.university.application.dto.PaymentDto;

import java.util.List;
import java.util.UUID;

public interface CreditCardPaymentService {
    PaymentDto save(PaymentDto payment);
    PaymentDto update(PaymentDto payment);
    void delete(PaymentDto paymentDto);
    void deleteById(UUID id); 
    PaymentDto findById(UUID id);
    List<PaymentDto> findAll();
    void processPayment(PaymentDto payment); 
}

