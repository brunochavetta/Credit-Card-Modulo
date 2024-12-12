package org.jala.university.infraestructure.persistence.RepositoryImpl;

import jakarta.persistence.*;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardTransaction;
import org.jala.university.domain.repository.CreditCardTransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CreditCardTransactionRepositoryImpl extends CrudRepository<CreditCardTransaction, UUID> implements CreditCardTransactionRepository {
    public CreditCardTransactionRepositoryImpl(EntityManager entityManager) {
        super(CreditCardTransaction.class, entityManager);
    }

    @Override
    public CreditCardTransaction findById(UUID id) {
        String sql = "SELECT * FROM CreditCardTransaction WHERE id = UUID_TO_BIN(:id)";
        return (CreditCardTransaction) entityManager.createNativeQuery(sql, CreditCardTransaction.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

    @Override
    public List<CreditCardTransaction> findAllByCreditCard(CreditCard creditCard) {
        List<CreditCardTransaction> transactions;
        try {
            transactions = entityManager.createQuery("SELECT t FROM CreditCardTransaction t WHERE n.creditCard = :creditCard",
                    CreditCardTransaction.class)
                    .setParameter("creditCard", creditCard)
                    .getResultList();
        } finally {
            entityManager.close();
        }
        return transactions;
    }

    public List<CreditCardTransaction> findTransactionsByCardAndDateRange(CreditCard creditCard, LocalDate startDate, LocalDate endDate) {
        List<CreditCardTransaction> transactions;
        try {
            transactions = entityManager.createQuery(
                            "SELECT t FROM CreditCardTransaction t WHERE t.creditCard = :creditCard AND t.transactionDate BETWEEN :startDate AND :endDate",
                            CreditCardTransaction.class)
                    .setParameter("creditCard", creditCard)
                    .setParameter("startDate", startDate.atStartOfDay())
                    .setParameter("endDate", endDate.atTime(23, 59, 59))
                    .getResultList();
        } catch (PersistenceException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error retrieving credit card transactions", e);
        } finally {
            entityManager.close();
        }
        return transactions;
    }


    @Override
    public List<CreditCardTransaction> findTransactionsByDebt(CreditCardDebt debt) {
        List<CreditCardTransaction> transactions;
        try {
            transactions = entityManager.createQuery(
                            "SELECT t FROM CreditCardTransaction t WHERE t.debt = :debt",
                            CreditCardTransaction.class)
                    .setParameter("debt", debt)
                    .getResultList();
        } catch (PersistenceException e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error retrieving credit card transactions", e);
        } finally {
            entityManager.close();
        }
        return transactions;
    }
}
