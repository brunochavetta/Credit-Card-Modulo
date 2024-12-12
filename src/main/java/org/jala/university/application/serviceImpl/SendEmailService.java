package org.jala.university.application.serviceImpl;

import org.jala.university.application.dto.SendNotificationDto;

import java.io.File;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.DataSource;


public class SendEmailService {
    public static void sendNotificationViaEmail(SendNotificationDto notificationDTO, File document) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(notificationDTO.getSender(), "mkrwinloxzcdtvtp");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(notificationDTO.getSender()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notificationDTO.getRecipient()));
            message.setSubject(notificationDTO.getNotification().getSubject());
            message.setText(notificationDTO.getNotification().getMessage(), "utf-8");

            Multipart multipart = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(notificationDTO.getNotification().getMessage(), "utf-8");
            multipart.addBodyPart(textPart);

            if (document != null && document.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                DataSource source = new FileDataSource(document);
                attachmentPart.setDataHandler(new DataHandler(source));
                attachmentPart.setFileName(document.getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Email sent successfully");

        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }
}
