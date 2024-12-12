package org.jala.university.infraestructure.persistence.RepositoryMock;

import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.repository.ApplicationDocumentRepository;

import java.util.*;

public class ApplicationDocumentRepositoryMock implements ApplicationDocumentRepository {

    private final Map<UUID, ApplicationDocument> database = new HashMap<>();

    @Override
    public ApplicationDocument save(ApplicationDocument document) {
        document.setId(UUID.randomUUID());
        database.put(document.getId(), document);
        return document;
    }

    @Override
    public void delete(ApplicationDocument document) {
        database.remove(document);
    }

    @Override
    public void deleteById(UUID id) {
        database.remove(id);
    }

    @Override
    public ApplicationDocument findById(UUID id) {
        return database.get(id);
    }

    @Override
    public List<ApplicationDocument> findAll() {
        return new ArrayList<>(database.values());
    }
}
