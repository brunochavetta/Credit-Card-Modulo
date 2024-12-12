package org.jala.university.application.mapper;

import org.jala.university.application.dto.NotificationDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Notification;

public class NotificationMapper implements Mapper<Notification, NotificationDto>{
    @Override
    public Notification mapFrom(NotificationDto notificationDto) {
        return Notification.builder()
            .id(notificationDto.getId())
            .customer(notificationDto.getCustomer())
            .type(notificationDto.getType())
            .channel(notificationDto.getChannel())
            .subject(notificationDto.getSubject())
            .message(notificationDto.getMessage())
            .timestamp(notificationDto.getTimestamp())
            .seen(notificationDto.isSeen())
            .build(); 
    }

    @Override
    public NotificationDto mapTo(Notification notification) {
        return NotificationDto.builder()
            .id(notification.getId())
            .customer(notification.getCustomer())
            .type(notification.getType())
            .channel(notification.getChannel())
            .subject(notification.getSubject())
            .message(notification.getMessage())
            .timestamp(notification.getTimestamp())
            .seen(notification.isSeen())
            .build(); 
    }
}
