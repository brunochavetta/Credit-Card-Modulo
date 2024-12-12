package org.jala.university.infraestructure.persistence.RepositoryMock;


import org.jala.university.domain.entity.CreditCardPayment;
import org.jala.university.domain.repository.CreditCardPaymentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreditCardPaymentRepositoryMock implements CreditCardPaymentRepository {
    private Map<UUID, CreditCardPayment> database = new HashMap<>();

    @Override
    public CreditCardPayment save(CreditCardPayment payment) {
        payment.setId(UUID.randomUUID());
        return database.put(payment.getId(), payment);
    }

    @Override
    public void delete(CreditCardPayment payment) {
        database.remove(payment);
    }

    @Override
    public CreditCardPayment findById(UUID id) {
        return database.get(id);
    }

    @Override
    public List<CreditCardPayment> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(UUID id) {
        database.remove(id); 
    }
}

