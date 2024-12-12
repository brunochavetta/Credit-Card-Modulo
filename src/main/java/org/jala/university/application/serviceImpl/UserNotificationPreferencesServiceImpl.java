package org.jala.university.application.serviceImpl;

import java.util.UUID;

import org.jala.university.application.dto.PreferencesDto;
import org.jala.university.application.mapper.PreferencesMapper;
import org.jala.university.application.service.UserNotificationPreferencesService;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.UserNotificationPreferences;
import org.jala.university.domain.repository.UserNotificationPreferencesRepository;
import org.jala.university.infraestructure.session.CustomerSession;

public class UserNotificationPreferencesServiceImpl implements UserNotificationPreferencesService{
    private final UserNotificationPreferencesRepository notificationRepository;
    private final PreferencesMapper preferencesMapper; 
    private Customer customer;

    public UserNotificationPreferencesServiceImpl(UserNotificationPreferencesRepository notificationRepository, PreferencesMapper preferencesMapper) {
        this.notificationRepository = notificationRepository; 
        this.preferencesMapper = preferencesMapper;
    }

    @Override
    public PreferencesDto save(PreferencesDto preferencesDto) {
        try {
            UserNotificationPreferences preferences = notificationRepository.save(preferencesMapper.mapFrom(preferencesDto));
            return preferencesMapper.mapTo(preferences); 
        } catch (Exception e) {
            throw new RuntimeException("Error saving notification preferences: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            notificationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting notification preferences: " + e.getMessage());
        }
    }

    @Override
    public PreferencesDto update(PreferencesDto preferencesDto) {
        try {
            UserNotificationPreferences preferences = notificationRepository.save(preferencesMapper.mapFrom(preferencesDto));
            return preferencesMapper.mapTo(preferences); 
        } catch (Exception e) {
            throw new RuntimeException("Error updating notification preferences: " + e.getMessage());
        }
    }

    @Override
    public PreferencesDto findById(UUID id) {
        try {
            return preferencesMapper.mapTo(notificationRepository.findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error finding notification preferences: " + e.getMessage());
        }
    }

    @Override
    public PreferencesDto findPreferencesByUser() {
        try {
            customer = CustomerSession.getInstance().getCurrentCustomer();
            return preferencesMapper.mapTo(notificationRepository.findPreferencesByUser(customer));
        } catch (Exception e) {
            throw new RuntimeException("Error finding all notifications preferences by user: " + e.getMessage());
        }
    }
}
