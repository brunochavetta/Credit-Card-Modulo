package org.jala.university.infraestructure.persistence.RepositoryImpl;

import jakarta.persistence.EntityManager;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.repository.ApplicationDocumentRepository;

import java.util.UUID;

public class ApplicationDocumentRepositoryImpl extends CrudRepository<ApplicationDocument, UUID>  implements ApplicationDocumentRepository {

    public ApplicationDocumentRepositoryImpl(EntityManager entityManager) {
        super(ApplicationDocument.class, entityManager);
    }

    @Override
    public ApplicationDocument findById(UUID id) {
        String sql = "SELECT * FROM ApplicationDocument WHERE id = UUID_TO_BIN(:id)";
        return (ApplicationDocument) entityManager.createNativeQuery(sql, ApplicationDocument.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

}
