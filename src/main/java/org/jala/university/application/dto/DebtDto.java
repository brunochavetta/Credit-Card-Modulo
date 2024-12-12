package org.jala.university.application.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.jala.university.domain.entity.CreditCardDebt.DebtStatus;

@Data
@Builder
@AllArgsConstructor
public class DebtDto {
    UUID id; 
    String month; 
    Double outstandingAmount;
    DebtStatus status;
}
