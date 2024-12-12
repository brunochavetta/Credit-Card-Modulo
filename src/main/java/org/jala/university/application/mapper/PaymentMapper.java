package org.jala.university.application.mapper;

import org.jala.university.application.dto.PaymentDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCardPayment;

public class PaymentMapper implements Mapper<CreditCardPayment, PaymentDto>{

    @Override
    public CreditCardPayment mapFrom(PaymentDto paymentDto) {
        return CreditCardPayment.builder()
            .id(paymentDto.getId())
            .paymentDate(paymentDto.getPaymentDate())
            .amount(paymentDto.getAmount())
            .creditCardDebt(paymentDto.getDebt())
            .build(); 
    }

    @Override
    public PaymentDto mapTo(CreditCardPayment payment) {
        return PaymentDto.builder()
            .id(payment.getId())
            .paymentDate(payment.getPaymentDate())
            .amount(payment.getAmount())
            .debt(payment.getCreditCardDebt())
            .build(); 
    }
    
}
