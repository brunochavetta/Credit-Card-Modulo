package org.jala.university.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.jala.university.commons.domain.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "credit_card_payments")
public class CreditCardPayment implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "debt_id")  
    private CreditCardDebt creditCardDebt;

    public CreditCardPayment () { super(); }

    public CreditCardPayment(Double amount, CreditCardDebt debt) {
        this.amount = amount;
        this.creditCardDebt = debt;
    }

    @PrePersist
    public void onCreate() {
        this.paymentDate = LocalDateTime.now(); 
    }

    @Override
    public UUID getId() {
        return id;
    }

}

