package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.CreditCardApplication;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Data
@Builder
public class DocumentDto {
    UUID id;
    private CreditCardApplication application;
    private String documentType;
    private String fileName;
    private String mimeType;
    private byte[] content;
    private LocalDateTime uploadedAt;

    @Override
    public String toString() {
        return "DocumentDto{" +
                "id=" + id +
                ", application=" + application +
                ", documentType='" + documentType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", content=" + Arrays.toString(content) +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
