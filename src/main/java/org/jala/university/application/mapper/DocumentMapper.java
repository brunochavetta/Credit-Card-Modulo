package org.jala.university.application.mapper;

import org.jala.university.application.dto.DocumentDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.ApplicationDocument;

public class DocumentMapper implements Mapper<ApplicationDocument, DocumentDto> {

    @Override
    public DocumentDto mapTo(ApplicationDocument document) {
        return DocumentDto.builder()
                .id(document.getId())
                .application(document.getApplication())
                .fileName(document.getFileName())
                .mimeType(document.getMimeType())
                .content(document.getContent())
                .uploadedAt(document.getUploadedAt())
                .build();
    }

    @Override
    public ApplicationDocument mapFrom(DocumentDto documentDto) {
        return ApplicationDocument.builder()
                .id(documentDto.getId())
                .application(documentDto.getApplication())
                .fileName(documentDto.getFileName())
                .mimeType(documentDto.getMimeType())
                .content(documentDto.getContent())
                .uploadedAt(documentDto.getUploadedAt())
                .build();
    }
}
