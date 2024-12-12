package org.jala.university.infraestructure.persistence.RepositoryMock;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardDebtRepository;

import java.util.*;
import java.util.stream.Collectors;

public class CreditCardDebtRepositoryMock implements CreditCardDebtRepository {

    private final Map<UUID, CreditCardDebt> database = new HashMap<>();

    @Override
    public CreditCardDebt save(CreditCardDebt debt) {
        debt.setId(UUID.randomUUID());
        return database.put(debt.getId(), debt);
    }

    @Override
    public void deleteById(UUID id) {
        database.remove(id);
    }

    @Override
    public CreditCardDebt findById(UUID id) {
        return database.get(id);
    }

    @Override
    public List<CreditCardDebt> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public CreditCardDebt existsByCreditCardIdAndMonth(CreditCard creditCard, String month) {
        return database.values().stream()
                .filter(debt -> Objects.equals(debt.getCreditCard(), creditCard) && debt.getMonth().equals(month))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<CreditCardDebt> findByCustomer(Customer customer) {
        return database.values().stream()
                .filter(debt -> Objects.equals(debt.getCreditCard().getCreditCardApplication().getCustomer(), customer))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(CreditCardDebt debt) {
        database.remove(debt); 
    }

}
