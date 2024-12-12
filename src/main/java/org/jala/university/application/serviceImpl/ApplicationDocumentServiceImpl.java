package org.jala.university.application.serviceImpl;

import org.jala.university.application.dto.DocumentDto;
import org.jala.university.application.mapper.DocumentMapper;
import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.repository.ApplicationDocumentRepository;
import org.jala.university.application.service.ApplicationDocumentService;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class ApplicationDocumentServiceImpl implements ApplicationDocumentService {
    private final ApplicationDocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public ApplicationDocumentServiceImpl(ApplicationDocumentRepository documentRepository, DocumentMapper documentMapper) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
    }

    @Override
    public DocumentDto save(DocumentDto documentDto) {
        try {
            ApplicationDocument document = documentRepository.save(documentMapper.mapFrom(documentDto));
            return documentMapper.mapTo(document);
        } catch (Exception e) {
            throw new RuntimeException("Error saving application document: " + e.getMessage());
        }
    }

    @Override
    public DocumentDto update(DocumentDto documentDto) {
        try {
            ApplicationDocument document = documentRepository.save(documentMapper.mapFrom(documentDto));
            return documentMapper.mapTo(document);
        } catch (Exception e) {
            throw new RuntimeException("Error updating application document: " + e.getMessage());
        }
    }

    @Override
    public void delete(DocumentDto documentDto) {
        try {
            documentRepository.delete(documentMapper.mapFrom(documentDto));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting application document: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            documentRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting application document: " + e.getMessage());
        }
    }

    @Override
    public DocumentDto findById(UUID id) {
        try {
            ApplicationDocument document = documentRepository.findById(id);
            return documentMapper.mapTo(document);
        } catch (Exception e) {
            throw new RuntimeException("Error finding application document: " + e.getMessage());
        }
    }

    @Override
    public List<DocumentDto> findAll() {
        try {
            List<ApplicationDocument> documents = documentRepository.findAll();
            return documents.stream().map(documentMapper::mapTo).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all application documents: " + e.getMessage());
        }
    }

    @Override
    public boolean isSupportedMimeType(String mimeType) {
        return "application/pdf".equalsIgnoreCase(mimeType) ||
                "image/jpeg".equalsIgnoreCase(mimeType) ||
                "image/jpg".equalsIgnoreCase(mimeType) ||
                "image/png".equalsIgnoreCase(mimeType);
    }

    @Override
    public String detectMimeType(String fileName) {
        try {
            return Files.probeContentType(Path.of(fileName));
        } catch (Exception e) {
            throw new RuntimeException("Error detecting MIME type for file: " + fileName);
        }
    }
}