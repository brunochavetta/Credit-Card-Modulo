package org.jala.university.infraestructure.persistence.RepositoryImpl;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CustomerRepository;

import jakarta.persistence.EntityManager;

import java.util.UUID;

public class CustomerRepositoryImpl extends CrudRepository<Customer, UUID> implements CustomerRepository {
    public CustomerRepositoryImpl(EntityManager entityManager) {
        super(Customer.class, entityManager);
    }

    @Override
    public Customer findById(UUID id) {
        String sql = "SELECT * FROM customers WHERE id = UUID_TO_BIN(:id)";
        return (Customer) entityManager.createNativeQuery(sql, Customer.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }
}
