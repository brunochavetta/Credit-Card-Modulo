package org.jala.university.application.service;

import java.util.List;
import java.util.UUID;

import org.jala.university.application.dto.DebtDto;
import org.jala.university.domain.entity.CreditCard;

public interface CreditCardDebtService {
    DebtDto generateMonthlyDebt(CreditCard creditCard);
    DebtDto update(DebtDto debt);
    void deleteById(UUID id);
    DebtDto findById(UUID id);
    List<DebtDto> findAll();
    List<DebtDto> findByCustomer();
    DebtDto IsExistsByCreditCardIdAndMonth(CreditCard creditCard, String month);
}