package org.jala.university.application.service;

public interface NotificationConfig {
    void activateMailNotifications();
    void activateAppNotifications();
    boolean getActivateMailNotifications();
    boolean getActivateAppNotifications();
    boolean DesactivateMailNotification();
    boolean DesactivateAppNotification();
}
