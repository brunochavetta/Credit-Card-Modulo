package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jala.university.commons.domain.BaseEntity;

import java.util.UUID;

@Getter
@Setter
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "user_notification_preferences")
public class UserNotificationPreferences implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private boolean notifications = false;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "transaction_mail")
    private boolean transactionMail;

    @Column(name = "transaction_app")
    private boolean transactionApp;

    @Column(name = "payment_mail")
    private boolean paymentMail;

    @Column(name = "payment_app")
    private boolean paymentApp;

    @Column(name = "limit_exceeded_mail")
    private boolean limitExceededMail;

    @Column(name = "limit_exceeded_app")
    private boolean limitExceededApp;

    public UserNotificationPreferences() {
        super();
    }

    public UserNotificationPreferences(boolean notifications, Customer customer, boolean transactionMail, boolean transactionApp, boolean paymentMail, boolean paymentApp, boolean limitExceededMail, boolean limitExceededApp) {
        this.notifications = notifications;
        this.customer = customer;
        this.transactionMail = transactionMail;
        this.transactionApp = transactionApp;
        this.paymentMail = paymentMail;
        this.paymentApp = paymentApp;
        this.limitExceededMail = limitExceededMail;
        this.limitExceededApp = limitExceededApp;
    }

    @Override
    public UUID getId() {
        return id;
    }
}
