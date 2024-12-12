package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class TransactionDto {
    UUID id;
    CreditCard creditCard;
    LocalDateTime transactionDate;
    Double amount;
    String description;
    CreditCardDebt debt;
}
