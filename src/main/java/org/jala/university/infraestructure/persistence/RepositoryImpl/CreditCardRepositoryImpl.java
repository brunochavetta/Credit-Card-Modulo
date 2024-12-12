package org.jala.university.infraestructure.persistence.RepositoryImpl;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardRepository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

public class CreditCardRepositoryImpl extends CrudRepository<CreditCard, UUID>  implements CreditCardRepository {

    public CreditCardRepositoryImpl(EntityManager entityManager) {
        super(CreditCard.class, entityManager);
    }

    @Override
    public CreditCard findById(UUID id) {
        String sql = "SELECT * FROM CreditCard WHERE id = UUID_TO_BIN(:id)";
        return (CreditCard) entityManager.createNativeQuery(sql, CreditCard.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

    @Override
    public List<CreditCard> listByUser(Customer customer) {
        List<CreditCard> cards = null;
        try {
            cards = entityManager.createQuery(
                    "SELECT a FROM CreditCard a WHERE a.creditCardApplication.customer = :customer",
                    CreditCard.class)
                    .setParameter("customer", customer)
                    .getResultList();

        } finally {
            entityManager.close();
        }
        return cards;
    }
}
