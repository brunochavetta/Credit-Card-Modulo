package org.jala.university.infraestructure.persistence.RepositoryMock;

import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CustomerRepository;

import java.util.*;

public class CustomerRepositoryMock implements CustomerRepository {
    private final Map<UUID, Customer> customerStorage = new HashMap<>();

    @Override
    public void delete(Customer customer) {
        customerStorage.remove(customer); 
    }

    @Override
    public void deleteById(UUID uuid) {
        customerStorage.remove(uuid); 
    }

    @Override
    public Customer findById(UUID id) {
        return customerStorage.get(id);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customerStorage.values());
    }

    @Override
    public Customer save(Customer customer) {
        return null;
    }
}

