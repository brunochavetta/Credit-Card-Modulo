package org.jala.university.application.service;

import org.jala.university.application.dto.NotificationDto;
import org.jala.university.application.dto.SendNotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    NotificationDto save(NotificationDto notificationDto);
    void deleteById(UUID id);
    void delete(NotificationDto notificationDto);
    NotificationDto update(NotificationDto notificationDto);
    void deleteAllByUser();
    NotificationDto findById(UUID id);
    List<NotificationDto> listByUser();
    void sendNotification(SendNotificationDto notificationDTO);
}
