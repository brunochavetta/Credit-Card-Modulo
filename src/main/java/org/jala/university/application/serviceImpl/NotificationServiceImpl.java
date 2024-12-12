package org.jala.university.application.serviceImpl;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.NotificationDto;
import org.jala.university.application.dto.SendNotificationDto;
import org.jala.university.application.mapper.NotificationMapper;
import org.jala.university.application.mapper.PreferencesMapper;
import org.jala.university.application.service.NotificationService;
import org.jala.university.application.service.UserNotificationPreferencesService;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.Notification;
import org.jala.university.domain.entity.UserNotificationPreferences;
import org.jala.university.domain.repository.NotificationRepository;
import org.jala.university.infraestructure.session.CustomerSession;

import java.util.List;
import java.util.UUID;

public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository; 
    private final NotificationMapper notificationMapper; 
    private SendNotificationDto notificationDTO;
    private Customer customer;
    private UserNotificationPreferencesService preferencesService; 

    public NotificationServiceImpl (NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.customer = CustomerSession.getInstance().getCurrentCustomer();
        this.notificationRepository = notificationRepository; 
        this.notificationMapper = notificationMapper; 
        this.preferencesService = ServiceFactoryCreditCard.preferencesServiceFactory(); 
    }

    @Override
    public NotificationDto save(NotificationDto notificationDto) {
        try {
            Notification notification = notificationRepository.save(notificationMapper.mapFrom(notificationDto));
            return notificationMapper.mapTo(notification); 
        } catch (Exception e) {
            throw new RuntimeException("Error saving notification: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            notificationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card application: " + e.getMessage());
        }
    }

    @Override
    public void delete(NotificationDto notificationDto) {
        try {
            notificationRepository.delete(notificationMapper.mapFrom(notificationDto));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card application: " + e.getMessage());
        }
    }

    @Override
    public NotificationDto update(NotificationDto notificationDto) {
        try {
            Notification notification = notificationRepository.save(notificationMapper.mapFrom(notificationDto));
            return notificationMapper.mapTo(notification); 
        } catch (Exception e) {
            throw new RuntimeException("Error updating notification: " + e.getMessage());
        }
    }

    @Override
    public void deleteAllByUser() {
        try {
            notificationRepository.deleteAllByUser(customer);
        } catch (Exception e) {
            throw new RuntimeException("Error finding all notifications by user: " + e.getMessage());
        }
    }

    @Override
    public NotificationDto findById(UUID id) {
        try {
            return notificationMapper.mapTo(notificationRepository.findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error finding notification: " + e.getMessage());
        }
    }

    @Override
    public List<NotificationDto> listByUser() {
        try {
            return notificationRepository.listByUser(customer).stream().map(notificationMapper::mapTo).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding all notifications by user: " + e.getMessage());
        }
    }

    @Override
    public void sendNotification(SendNotificationDto notificationDTO) {
        this.notificationDTO = notificationDTO;
       try {
           notificationDTO.getNotification().setCustomer(customer);
           UserNotificationPreferences preferences = new PreferencesMapper().mapFrom(preferencesService.findPreferencesByUser());
           if (preferences.isNotifications()) {
               switch (notificationDTO.getNotification().getType()) {
                   case "Transaction notification":
                       if (preferences.isTransactionApp()) {
                           sendNotificationViaApp();
                       }
                       if (preferences.isTransactionMail()) {
                           sendNotificationViaEmail();
                       }
                       break;
                   case "Payment notification":
                       if (preferences.isPaymentMail()) {
                           sendNotificationViaEmail();
                       }
                       if (preferences.isPaymentApp()) {
                           sendNotificationViaApp();
                       }
                       break;
                   case "Limit Exceeded notification":
                       if (preferences.isLimitExceededMail()) {
                           sendNotificationViaEmail();
                       }
                       if (preferences.isLimitExceededApp()) {
                           sendNotificationViaApp();
                       }
                       break;
                   default:
                       throw new RuntimeException("The notification type is incorrect, it cannot be created");
               }
           }
       } catch (RuntimeException e) {
           throw new RuntimeException("An error occurred while creating the notification " + e.getMessage());
       }
    }

    private void sendNotificationViaEmail() {
        notificationDTO.getNotification().setChannel(Notification.Channel.EMAIL);
        SendEmailService.sendNotificationViaEmail(notificationDTO, null);
        try {
            notificationRepository.save(notificationDTO.getNotification());
        } catch (Exception e) {
            throw new RuntimeException("Error saving notification: " + e.getMessage());
        }
    }

    private void sendNotificationViaApp() {
        try {
            Notification oldNotification = notificationDTO.getNotification();
            Notification newNotification = new Notification(oldNotification.getCustomer(), Notification.Channel.APP,
                    oldNotification.getType(), oldNotification.getSubject(), oldNotification.getMessage());

            notificationRepository.save(newNotification);
        } catch (Exception e) {
            throw new RuntimeException("Error saving notification: " + e.getMessage());
        }
    }
}
