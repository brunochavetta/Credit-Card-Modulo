package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.UserNotificationPreferences;

import java.util.UUID;


public interface UserNotificationPreferencesRepository extends Repository<UserNotificationPreferences, UUID> {
    UserNotificationPreferences findPreferencesByUser(Customer customer);
}
