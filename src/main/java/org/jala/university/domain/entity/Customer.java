package org.jala.university.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.*;
import org.jala.university.commons.domain.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@Data
@AllArgsConstructor
@Table(name = "customers")
public class Customer implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(length = 80)
    private String phone;

    @Column(name = "id_number", nullable = false)
    private String idNumber; 

    @Column(length = 80)
    private String address;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "salary")
    private Double salary; 

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CreditCardApplication> creditCardApplications;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserNotificationPreferences> preferences;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Customer(String fullName, String email, String phone, String address, LocalDateTime createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
    }

    public Customer(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public Customer() {
        super();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                ", salary=" + salary +
                ", account number=" + account.getAccountNumber() +
                ", creditCardApplications=" + creditCardApplications +
                '}';
    }

    @Override
    public UUID getId() {
        return id;
    }
}

