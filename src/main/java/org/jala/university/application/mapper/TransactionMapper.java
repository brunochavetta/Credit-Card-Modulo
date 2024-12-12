package org.jala.university.application.mapper;

import org.jala.university.application.dto.TransactionDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCardTransaction;

public class TransactionMapper implements Mapper<CreditCardTransaction, TransactionDto> {
    @Override
    public TransactionDto mapTo(CreditCardTransaction creditCardTransaction) {
        return TransactionDto.builder()
                .id(creditCardTransaction.getId())
                .creditCard(creditCardTransaction.getCreditCard())
                .transactionDate(creditCardTransaction.getTransactionDate())
                .amount(creditCardTransaction.getAmount())
                .description(creditCardTransaction.getDescription())
                .debt(creditCardTransaction.getDebt())
                .build();
    }

    @Override
    public CreditCardTransaction mapFrom(TransactionDto transactionDto) {
        return CreditCardTransaction.builder()
                .id(transactionDto.getId())
                .creditCard(transactionDto.getCreditCard())
                .transactionDate(transactionDto.getTransactionDate())
                .amount(transactionDto.getAmount())
                .description(transactionDto.getDescription())
                .debt(transactionDto.getDebt())
                .build();
    }
}
