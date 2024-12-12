package org.jala.university.application.service;

import org.jala.university.application.dto.TransactionDto;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CreditCardTransactionService {
    TransactionDto save(TransactionDto transaction);
    void deleteById(UUID id);
    TransactionDto findById(UUID id);
    List<TransactionDto> findAllByCreditCard(UUID idCreditCard);
    List<TransactionDto> findTransactionsByCardAndDateRange(UUID idCreditCard, LocalDate startDate, LocalDate endDate);
    CreditCardDebt debtCreate (CreditCard creditCard, LocalDateTime localDate);
    List<TransactionDto> findTransactionsByDebt(UUID debtId);
}

