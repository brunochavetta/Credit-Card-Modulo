package org.jala.university.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.jala.university.domain.entity.Notification;
import org.jala.university.infraestructure.session.CustomerSession;

@Setter
@Getter
public class SendNotificationDto {

    private String recipient;
    private Notification notification;
    private String sender;

    public SendNotificationDto(Notification notification){
        this.sender = "coffeandbugssd2@gmail.com";
        this.recipient = CustomerSession.getInstance().getCurrentCustomer().getEmail();
        this.notification = notification;
    }
}
