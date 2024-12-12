package org.jala.university.application.service;

import java.util.UUID;

import org.jala.university.application.dto.PreferencesDto;

public interface UserNotificationPreferencesService {
    PreferencesDto save(PreferencesDto notification);
    void deleteById(UUID id);
    PreferencesDto update(PreferencesDto notification);
    PreferencesDto findById(UUID id);
    PreferencesDto findPreferencesByUser();
}
