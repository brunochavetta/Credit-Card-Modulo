package org.jala.university.infraestructure.persistence.RepositoryMock;

import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardApplicationRepository;

import java.util.*;
import java.util.stream.Collectors;

public class CreditCardApplicationRepositoryMock implements CreditCardApplicationRepository {

    private final Map<UUID, CreditCardApplication> database = new HashMap<>();

    @Override
    public CreditCardApplication save(CreditCardApplication application) {
        return database.put(application.getId(), application);
    }

    @Override
    public void delete(CreditCardApplication creditCardApplication) {
        database.remove(creditCardApplication);
    }

    @Override
    public void deleteById(UUID id) {
        database.remove(id);
    }

    @Override
    public CreditCardApplication findById(UUID id) {
        return database.get(id);
    }

    @Override
    public List<CreditCardApplication> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public CreditCardApplication findLast() {
        return database.get(database.size());
    }

    @Override
    public List<CreditCardApplication> listByUser(Customer customer) {
        return database.values().stream()
                .filter(creditCardApplication -> creditCardApplication.getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

}

