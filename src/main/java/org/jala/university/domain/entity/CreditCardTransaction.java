package org.jala.university.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.jala.university.commons.domain.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@Data
@AllArgsConstructor
@Table(name = "credit_card_transactions")
public class CreditCardTransaction implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private CreditCard creditCard;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "debt_id")
    private CreditCardDebt debt;

    public CreditCardTransaction() { super(); }

    public CreditCardTransaction(CreditCard creditCard, Double amount,
            String description) {
        this.creditCard = creditCard;
        this.amount = amount;
        this.description = description;
    }

    public CreditCardTransaction( Double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    @PrePersist
    public void onCreate() {
        this.transactionDate = LocalDateTime.now(); 
    }


    @Override
    public UUID getId() {
        return id;
    }
}

