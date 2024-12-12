package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.Account;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class CustomerDto {
    UUID id;
    String fullName;
    String email;
    String phone;
    String idNumber;
    String address;
    LocalDateTime createdAt;
    Double salary;
    Account account;

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                ", salary=" + salary +
                ", account=" + account +
                '}';
    }
}
