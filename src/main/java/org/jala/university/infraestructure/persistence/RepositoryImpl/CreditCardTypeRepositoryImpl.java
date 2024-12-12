package org.jala.university.infraestructure.persistence.RepositoryImpl;

import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.CreditCardType;
import org.jala.university.domain.repository.CreditCardTypeRepository;

import jakarta.persistence.EntityManager;
import java.util.UUID;

public class CreditCardTypeRepositoryImpl extends CrudRepository<CreditCardType, UUID> implements CreditCardTypeRepository {

    public CreditCardTypeRepositoryImpl(EntityManager entityManager) {
        super(CreditCardType.class, entityManager);
    }

    @Override
    public CreditCardType findById(UUID id) {
        String sql = "SELECT * FROM CreditCardType WHERE id = UUID_TO_BIN(:id)";
        return (CreditCardType) entityManager.createNativeQuery(sql, CreditCardType.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

    @Override
    public CreditCardType findLast() {
        return entityManager.createQuery("SELECT a FROM CreditCardType a ORDER BY a.id DESC", CreditCardType.class)
            .setMaxResults(1)
            .getSingleResult();
    } 
}
