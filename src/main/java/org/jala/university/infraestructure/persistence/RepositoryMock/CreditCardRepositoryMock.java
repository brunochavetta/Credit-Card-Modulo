package org.jala.university.infraestructure.persistence.RepositoryMock;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreditCardRepositoryMock implements CreditCardRepository {

    private Map<UUID, CreditCard> creditCards = new HashMap<>();
   
    @Override
    public CreditCard save(CreditCard creditCard) {
        creditCard.setId(UUID.randomUUID()); 
        return creditCards.put(creditCard.getId(), creditCard);
    }

    @Override
    public void deleteById(UUID id) {
        creditCards.remove(id);
    }

    @Override
    public CreditCard findById(UUID id) {
        return creditCards.get(id); 
    }

    @Override
    public List<CreditCard> findAll() {
        return new ArrayList<>(creditCards.values());
    }

    @Override
    public List<CreditCard> listByUser(Customer customer) {
        return creditCards.values().stream()
                .filter(creditCard -> creditCard.getCreditCardApplication().getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(CreditCard id) {
        creditCards.remove(id); 
    }
}
