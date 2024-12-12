package org.jala.university.application.mapper;

import org.jala.university.application.dto.PreferencesDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.UserNotificationPreferences;


public class PreferencesMapper implements Mapper<UserNotificationPreferences, PreferencesDto>{

    @Override
    public UserNotificationPreferences mapFrom(PreferencesDto preferencesDto) {
        return UserNotificationPreferences.builder()
            .id(preferencesDto.getId())
            .notifications(preferencesDto.isNotifications())
            .customer(preferencesDto.getCustomer())
            .transactionMail(preferencesDto.isTransactionMail())
            .transactionApp(preferencesDto.isTransactionApp())
            .paymentMail(preferencesDto.isPaymentMail())
            .paymentApp(preferencesDto.isPaymentApp())
            .limitExceededMail(preferencesDto.isLimitExceededMail())
            .limitExceededApp(preferencesDto.isLimitExceededApp())
            .build(); 
    }

    @Override
    public PreferencesDto mapTo(UserNotificationPreferences preferences) {
        return PreferencesDto.builder()
        .id(preferences.getId())
        .notifications(preferences.isNotifications())
        .customer(preferences.getCustomer())
        .transactionMail(preferences.isTransactionMail())
        .transactionApp(preferences.isTransactionApp())
        .paymentMail(preferences.isPaymentMail())
        .paymentApp(preferences.isPaymentApp())
        .limitExceededMail(preferences.isLimitExceededMail())
        .limitExceededApp(preferences.isLimitExceededApp())
        .build(); 
    }
    
}
