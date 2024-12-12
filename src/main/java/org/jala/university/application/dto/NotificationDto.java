package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.Notification;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class NotificationDto {
    UUID id;
    Customer customer;
    String type;
    Notification.Channel channel;
    String subject;
    String message;
    LocalDateTime timestamp = LocalDateTime.now();
    boolean seen = false;
}
