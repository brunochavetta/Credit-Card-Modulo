package org.jala.university.application.serviceImpl;


import org.jala.university.application.service.NotificationConfig;


public class NotificationConfigImpl implements NotificationConfig {

    private boolean mailNotification;
    private boolean appNotification;


    @Override
    public void activateMailNotifications() {
        this.mailNotification = true;
    }

    @Override
    public void activateAppNotifications() {
        this.appNotification = true;
    }

    @Override
    public boolean getActivateMailNotifications() {
        return mailNotification;
    }

    @Override
    public boolean getActivateAppNotifications() {
        return appNotification;
    }

    @Override
    public boolean DesactivateMailNotification() {
        mailNotification = false;
        return mailNotification;
    }

    @Override
    public boolean DesactivateAppNotification() {
        appNotification = false;
        return appNotification;
    }

    public void activateNotifications(){
        this.mailNotification = true;
        this.appNotification = true;

    }
}
