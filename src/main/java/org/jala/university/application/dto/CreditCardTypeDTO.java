package org.jala.university.application.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreditCardTypeDTO {
    UUID id;
    String typeName;
    String description;
    Double maxCreditAmount;
    Double monthlyFee;

    public CreditCardTypeDTO() { }

    @Override
    public String toString() {
        return typeName + " (Max Credit: " + maxCreditAmount + ")";
    }

}
