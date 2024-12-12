package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends Repository<Notification, UUID> {
    void deleteAllByUser (Customer customer);
    List<Notification> listByUser(Customer customer);
}
