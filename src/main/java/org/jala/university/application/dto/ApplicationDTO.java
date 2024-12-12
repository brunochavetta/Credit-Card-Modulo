package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class ApplicationDTO {
    UUID id;
    Customer customer;
    LocalDateTime applicationDate;
    CreditCardApplication.ApplicationStatus status;
    String companyName;
    String companyId;

    @Override
    public String toString() {
        return "ApplicationDTO{" +
                "id=" + id +
                ", customer=" + customer +
                ", applicationDate=" + applicationDate +
                ", status=" + status +
                ", companyName='" + companyName + '\'' +
                ", companyId='" + companyId + '\'' +
                '}';
    }
}
