package org.jala.university.domain.entity;

import java.util.List;
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
@Data
@Builder
@AllArgsConstructor
@Table(name = "credit_card_type")
public class CreditCardType implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(length = 200)
    private String description;

    @Column(name = "max_credit_amount", nullable = false)
    private Double maxCreditAmount;

    @Column(name = "monthly_fee", nullable = false)
    private Double monthlyFee;

    @OneToMany(mappedBy = "creditCardType", cascade = CascadeType.ALL)
    private List<CreditCard> creditCards; 

    public CreditCardType() { super(); }

    public CreditCardType(String typeName, String description, Double maxCreditAmount, Double monthlyFee) {
        this.typeName = typeName;
        this.description = description;
        this.maxCreditAmount = maxCreditAmount;
        this.monthlyFee = monthlyFee;
    }

    @Override
    public UUID getId() {
        return id;
    }

}

