package org.jala.university.infraestructure.persistence.RepositoryImpl;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCardPayment;
import org.jala.university.domain.repository.CreditCardPaymentRepository;

import jakarta.persistence.EntityManager;
import java.util.UUID;

public class CreditCardPaymentRepositoryImpl extends CrudRepository<CreditCardPayment, UUID> implements CreditCardPaymentRepository {
    public CreditCardPaymentRepositoryImpl(EntityManager entityManager) {
        super(CreditCardPayment.class, entityManager); 
    }

    @Override
    public CreditCardPayment findById(UUID id) {
        String sql = "SELECT * FROM CreditCardPayment WHERE id = UUID_TO_BIN(:id)";
        return (CreditCardPayment) entityManager.createNativeQuery(sql, CreditCardPayment.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }
}
