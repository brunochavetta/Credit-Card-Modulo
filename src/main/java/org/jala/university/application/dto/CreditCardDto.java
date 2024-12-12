package org.jala.university.application.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.CreditCardType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CreditCardDto {
    UUID id;
    String cardNumber;
    LocalDateTime issueDate;
    LocalDateTime expirationDate;
    Double selectCreditLimit;
    Double totalSpent;
    int securityCode;
    CreditCardApplication creditCardApplication;
    CreditCard.CreditCardStatus status;
    CreditCardType creditCardType;

    @Override
    public String toString() {
        return "CreditCardDto{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", issueDate=" + issueDate +
                ", expirationDate=" + expirationDate +
                ", selectCreditLimit=" + selectCreditLimit +
                ", totalSpent=" + totalSpent +
                ", securityCode=" + securityCode +
                ", creditCardApplication=" + creditCardApplication +
                ", status=" + status +
                ", creditCardType=" + creditCardType +
                '}';
    }
}
