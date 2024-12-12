package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.Customer;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PreferencesDto {
    UUID id;
    boolean notifications;
    Customer customer;
    boolean transactionMail;
    boolean transactionApp;
    boolean paymentMail;
    boolean paymentApp;
    boolean limitExceededMail;
    boolean limitExceededApp;
}
