package org.jala.university.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.jala.university.domain.entity.CreditCardDebt;

@Data
@Builder
@AllArgsConstructor
public class PaymentDto {
    UUID id; 
    LocalDateTime paymentDate; 
    Double amount; 
    CreditCardDebt debt; 
}
