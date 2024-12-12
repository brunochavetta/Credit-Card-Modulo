package org.jala.university.domain.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.jala.university.commons.domain.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "credit_card_debts")
public class CreditCardDebt implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private CreditCard creditCard;

    private String month;

    @Column(name = "outstanding_amount", nullable = false)
    private Double outstandingAmount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebtStatus status;

    @OneToMany(mappedBy = "creditCardDebt", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CreditCardPayment> payments;

    @OneToMany(mappedBy = "debt", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CreditCardTransaction> transactions;

    public CreditCardDebt () { super(); }

    public CreditCardDebt(CreditCard creditCard, Double outstandingAmount, String month, LocalDate dueDate, DebtStatus status) {
        this.creditCard = creditCard;
        this.outstandingAmount = outstandingAmount;
        this.month = month;
        this.dueDate = dueDate;
        this.status = status;
    }

    public enum DebtStatus {
        pending, partial_paid, full_paid
    }

    @Override
    public String toString() {
        return "CreditCardDebt{" +
                "id=" + id +
                ", creditCard number=" + creditCard.getCardNumber() +
                ", month='" + month + '\'' +
                ", outstandingAmount=" + outstandingAmount +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", payments=" + payments.size() +
                ", transactions=" + transactions.size() +
                '}';
    }

    @Override
    public UUID getId() {
        return id;
    }
}

