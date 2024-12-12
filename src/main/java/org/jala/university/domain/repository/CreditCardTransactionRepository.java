package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CreditCardTransactionRepository extends Repository<CreditCardTransaction, UUID> {
    List<CreditCardTransaction> findAllByCreditCard(CreditCard creditCard);
    List<CreditCardTransaction> findTransactionsByCardAndDateRange(CreditCard creditCard, LocalDate startDate, LocalDate endDate);
    List<CreditCardTransaction> findTransactionsByDebt(CreditCardDebt debt);
}
