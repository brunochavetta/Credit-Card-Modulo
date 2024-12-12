package org.jala.university.infraestructure.persistence.RepositoryMock;

import org.jala.university.domain.entity.CreditCardType;
import org.jala.university.domain.repository.CreditCardTypeRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreditCardTypeRepositoryMock implements CreditCardTypeRepository {
    private final Map<UUID, CreditCardType> creditCardTypeStorage = new HashMap<>();
    private Integer currentId = null;

    @Override
    public CreditCardType save(CreditCardType creditCardType) {
        if (creditCardType.getId() == null) {
            creditCardType.setId(UUID.randomUUID());
        }
        return creditCardTypeStorage.put(creditCardType.getId(), creditCardType);
    }

    @Override
    public void deleteById(UUID id) {
        if (!creditCardTypeStorage.containsKey(id)) {
            throw new IllegalArgumentException("CreditCardType not found");
        }
        creditCardTypeStorage.remove(id);
    }

    @Override
    public void delete(CreditCardType type) {
        creditCardTypeStorage.remove(type);
    }

    @Override
    public CreditCardType findById(UUID id) {
        return creditCardTypeStorage.get(id);
    }

    @Override
    public List<CreditCardType> findAll() {
        return new ArrayList<>(creditCardTypeStorage.values());
    }

    @Override
    public CreditCardType findLast() {
        if (currentId != null) {
            return creditCardTypeStorage.get(currentId); 
        }
        return null;    
    }
}

