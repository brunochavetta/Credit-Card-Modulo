package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.jala.university.commons.domain.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "notifications")
public class Notification implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false, length = 50)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Channel channel;

    @Column(nullable = false, length = 50)
    private String subject;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(nullable = false)
    private boolean seen = false;

    @Override
    public UUID getId() {
        return id;
    }

    public enum Channel {
        APP,
        EMAIL
    }

    public Notification() { super(); }

    public Notification(Customer customer, Channel channel, String type, String subject, String message) {
        this.customer = customer;
        this.channel = channel;
        this.type = type;
        this.subject = subject;
        this.message = message;
    }
}
