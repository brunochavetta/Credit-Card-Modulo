package org.jala.university.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.jala.university.commons.domain.BaseEntity;

@Entity
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@Table(name = "application_documents")
public class ApplicationDocument implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private CreditCardApplication application;

    @Column(name = "document_type", nullable = false)
    private String documentType;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "mime_type", nullable = false)
    private String mimeType;

    @Lob
    @Column(name = "content", nullable = false)
    private byte[] content;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    public ApplicationDocument() { super(); }

    public ApplicationDocument(CreditCardApplication application, String documentType, String fileName, String mimeType,
            byte[] content) {
        this.application = application;
        this.documentType = documentType;
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.content = content;
    }

    public ApplicationDocument(CreditCardApplication application, String documentType, String fileName, String mimeType) {
        this.application = application;
        this.documentType = documentType;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    @PrePersist
    public void onCreate() {
        this.uploadedAt = LocalDateTime.now(); 
    }

    @Override
    public UUID getId() {
        return id;
    }
}

