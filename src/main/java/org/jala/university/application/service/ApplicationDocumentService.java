package org.jala.university.application.service;

import org.jala.university.application.dto.DocumentDto;

import java.util.List;
import java.util.UUID;

public interface ApplicationDocumentService {
    DocumentDto save(DocumentDto document);
    DocumentDto update(DocumentDto document);
    void delete(DocumentDto documentDto);
    void deleteById(UUID id);
    DocumentDto findById(UUID id);
    List<DocumentDto> findAll();
    boolean isSupportedMimeType(String fileType);
    String detectMimeType(String fileName);
}
