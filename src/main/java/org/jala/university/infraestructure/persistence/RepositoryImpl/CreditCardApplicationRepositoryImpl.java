package org.jala.university.infraestructure.persistence.RepositoryImpl;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardApplicationRepository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

public class CreditCardApplicationRepositoryImpl extends CrudRepository<CreditCardApplication, UUID>  implements CreditCardApplicationRepository {

    public CreditCardApplicationRepositoryImpl (EntityManager entityManager) {
        super(CreditCardApplication.class, entityManager);
    }

    @Override
    public CreditCardApplication findById(UUID id) {
        String sql = "SELECT * FROM CreditCardApplication WHERE id = UUID_TO_BIN(:id)";
        return (CreditCardApplication) entityManager.createNativeQuery(sql, CreditCardApplication.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

    @Override
    public CreditCardApplication findLast() {
        return entityManager
                .createQuery("SELECT a FROM CreditCardApplication a ORDER BY a.id DESC", CreditCardApplication.class)
                .setMaxResults(1)
                .getSingleResult();
    }

    @Override
    public List<CreditCardApplication> listByUser(Customer customer) {
        List<CreditCardApplication> applications = null;

        try {
            applications = entityManager.createQuery(
                    "SELECT a FROM CreditCardApplication a WHERE a.customer = :customer",
                    CreditCardApplication.class)
                    .setParameter("customer", customer)
                    .getResultList();
        } finally {
            entityManager.close();
        }

        return applications;
    }

}