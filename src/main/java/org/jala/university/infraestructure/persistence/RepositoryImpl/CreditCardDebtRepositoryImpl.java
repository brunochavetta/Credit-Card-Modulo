package org.jala.university.infraestructure.persistence.RepositoryImpl;

import jakarta.persistence.*;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardDebtRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreditCardDebtRepositoryImpl extends CrudRepository<CreditCardDebt, UUID> implements CreditCardDebtRepository {
 
    public CreditCardDebtRepositoryImpl(EntityManager entityManager) {
        super(CreditCardDebt.class, entityManager); 
    }

    @Override
    public CreditCardDebt findById(UUID id) {
        String sql = "SELECT * FROM CreditCardDebt WHERE id = UUID_TO_BIN(:id)";
        return (CreditCardDebt) entityManager.createNativeQuery(sql, CreditCardDebt.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

    @Override
    public CreditCardDebt existsByCreditCardIdAndMonth(CreditCard creditCard, String month) {
        CreditCardDebt debt = null;
        try {
            String sql = "SELECT d FROM CreditCardDebt d WHERE d.creditCard = :creditCard AND d.month = :month";
            debt = entityManager.createQuery(sql, CreditCardDebt.class)
                    .setParameter("creditCard", creditCard)
                    .setParameter("month", month)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            debt = null;
        } finally {
            entityManager.close();
        }
        return debt;
    }

    @Override
    public List<CreditCardDebt> findByCustomer(Customer customer) {
        List<CreditCardDebt> debts = new ArrayList<>();
        try {
            String sql = "SELECT d FROM CreditCardDebt d " +
                    "JOIN d.creditCard c " +
                    "JOIN c.creditCardApplication a " +
                    "JOIN a.customer cust " +
                    "WHERE cust = :customer";
            debts = entityManager.createQuery(sql, CreditCardDebt.class)
                    .setParameter("customer", customer)
                    .getResultList();
        } catch (NoResultException e) {
            throw new NoResultException("Could not access debts for " + customer.getFullName());
        } finally {
            if (entityManager.isOpen()) {
                entityManager.close();
            }
        }
        return debts;
    }
}
