package org.jala.university.infraestructure.persistence.RepositoryMock;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardTransaction;
import org.jala.university.domain.repository.CreditCardTransactionRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CreditCardTransactionRepositoryMock implements CreditCardTransactionRepository {
    private final Map<UUID, CreditCardTransaction> transactionStorage = new HashMap<>();

    @Override
    public CreditCardTransaction save(CreditCardTransaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(UUID.randomUUID());
        }
        return transactionStorage.put(transaction.getId(), transaction);
    }

    @Override
    public void deleteById(UUID id) {
        if (!transactionStorage.containsKey(id)) {
            throw new IllegalArgumentException("CreditCardTransaction not found");
        }
        transactionStorage.remove(id);
    }

    @Override
    public CreditCardTransaction findById(UUID id) {
        return transactionStorage.get(id);
    }

    @Override
    public List<CreditCardTransaction> findAllByCreditCard(CreditCard creditCard) {
        List<CreditCardTransaction> transactions = transactionStorage.values().stream()
                .filter(creditCardTransaction -> creditCardTransaction.getCreditCard().getId() == creditCard.getId())
                .collect(Collectors.toList());
        return transactions;
    }

    @Override
    public List<CreditCardTransaction> findTransactionsByCardAndDateRange(CreditCard creditCard, LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public List<CreditCardTransaction> findTransactionsByDebt(CreditCardDebt debt) {
        return List.of();
    }

    @Override
    public void delete(CreditCardTransaction transaction) {
        transactionStorage.remove(transaction); 
    }

    @Override
    public List<CreditCardTransaction> findAll() {
        return new ArrayList<>(transactionStorage.values());
    }
}
