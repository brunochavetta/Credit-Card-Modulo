package org.jala.university.infraestructure.persistence.RepositoryImpl;

import jakarta.persistence.*;
import org.jala.university.commons.infrastructure.persistance.CrudRepository;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.UserNotificationPreferences;
import org.jala.university.domain.repository.UserNotificationPreferencesRepository;

import java.util.UUID;

public class UserNotificationPreferencesRepositoryImpl extends CrudRepository<UserNotificationPreferences, UUID> implements UserNotificationPreferencesRepository {

    public UserNotificationPreferencesRepositoryImpl(EntityManager entityManager) {
        super(UserNotificationPreferences.class, entityManager);
    }

    @Override
    public UserNotificationPreferences findById(UUID id) {
        String sql = "SELECT * FROM UserNotificationPreferences WHERE id = :id";
        return (UserNotificationPreferences) entityManager.createNativeQuery(sql, UserNotificationPreferences.class)
                .setParameter("id", id.toString())
                .getSingleResult();
    }

    @Override
    public UserNotificationPreferences findPreferencesByUser(Customer customer) {
        UserNotificationPreferences notification;
        try {
            notification = entityManager.createQuery(
                            "SELECT n FROM UserNotificationPreferences n WHERE n.customer = :customer",
                            UserNotificationPreferences.class)
                    .setParameter("customer", customer)
                    .getSingleResult();

            if (notification == null) {
                notification = new UserNotificationPreferences(); 
            }

        } catch (NoResultException e) {
            notification= new UserNotificationPreferences(); 
        } 
        return notification;
    }
}
