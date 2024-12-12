package org.jala.university.domain.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.jala.university.commons.domain.BaseEntity;

@Getter
@Setter
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "credit_cards")
public class CreditCard implements BaseEntity<UUID>  {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "card_number", nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "select_credit_limit")
    private Double selectCreditLimit;

    @Column(name = "total_spend")
    private Double totalSpent;

    @Column(name = "security_code", nullable = false)
    private int securityCode; 

    @ManyToOne
    @JoinColumn(name = "credit_card_applications_id", nullable = false)
    private CreditCardApplication creditCardApplication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditCardStatus status;

    @ManyToOne
    @JoinColumn(name = "credit_card_type_id", nullable = false)
    private CreditCardType creditCardType;

    @OneToMany(mappedBy = "creditCard", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CreditCardDebt> debts;

    public CreditCard() { super(); }

    public CreditCard(String cardNumber, LocalDateTime issueDate, LocalDateTime expirationDate,
            Double selectCreditLimit, CreditCardApplication creditCardApplication, CreditCardStatus status,
            CreditCardType creditCardType, int securityCode) {
        this.cardNumber = cardNumber;
        this.issueDate = issueDate;
        this.expirationDate = expirationDate;
        this.selectCreditLimit = selectCreditLimit;
        this.creditCardApplication = creditCardApplication;
        this.status = status;
        this.creditCardType = creditCardType;
        this.securityCode = securityCode; 
    }

    public enum CreditCardStatus {
        active, inactive, blocked
    }

    @PrePersist
    public void onCreate() {
        this.status = CreditCardStatus.active;
        this.totalSpent = 0.0;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", selectCreditLimit=" + selectCreditLimit +
                ", securityCode=" + securityCode +
                ", creditCardApplication=" + creditCardApplication +
                ", status=" + status +
                ", creditCardType=" + creditCardType +
                '}';
    }

    @Override
    public UUID getId() {
        return id;
    }
}


