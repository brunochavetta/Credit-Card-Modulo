package org.jala.university.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.*;
import org.jala.university.commons.domain.BaseEntity; 

@Getter
@Setter
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "credit_card_applications")
public class CreditCardApplication implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ApplicationDocument> documents;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_id")
    private String companyId;

    public CreditCardApplication() { super(); }

    public CreditCardApplication(Customer customer, LocalDateTime applicationDate, ApplicationStatus status,
            List<ApplicationDocument> documents) {
        this.customer = customer;
        this.applicationDate = applicationDate;
        this.status = status;
        this.documents = documents;
    }

    public enum ApplicationStatus {
        submitted,
        in_review,
        accepted,
        rejected,
        completed;
    }

    @Override
    public UUID getId() {
        return id;
    }
}

