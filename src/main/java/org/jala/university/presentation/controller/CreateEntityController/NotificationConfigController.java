package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.mapper.PreferencesMapper;
import org.jala.university.application.service.UserNotificationPreferencesService;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.UserNotificationPreferences;
import org.jala.university.infraestructure.session.CustomerSession;

public class NotificationConfigController {
    @FXML
    private CheckBox transactionMailCheckBox;
    @FXML
    private CheckBox transactionAppCheckBox;

    @FXML
    private CheckBox successfulPaymentMailCheckBox;
    @FXML
    private CheckBox successfulPaymentAppCheckBox;

    @FXML
    private CheckBox LimitExceedMailCheckBox;

    @FXML
    private CheckBox LimitExceedAppCheckBox;

    private final UserNotificationPreferencesService preferencesService; 
    private final PreferencesMapper preferencesMapper; 

    private Customer customer;
    private UserNotificationPreferences verifyPreferences;

    public NotificationConfigController() {
        this.preferencesService = ServiceFactoryCreditCard.preferencesServiceFactory(); 
        this.preferencesMapper = new PreferencesMapper(); 
    }

    @FXML
    public void initialize() {
        customer = CustomerSession.getInstance().getCurrentCustomer();
        updatePreferencesView();
    }

    public void updatePreferencesView() {
        verifyPreferences = preferencesMapper.mapFrom(preferencesService.findPreferencesByUser());
        if (verifyPreferences != null) {
            transactionMailCheckBox.setSelected(verifyPreferences.isTransactionMail());
            transactionAppCheckBox.setSelected(verifyPreferences.isTransactionApp());

            successfulPaymentMailCheckBox.setSelected(verifyPreferences.isPaymentMail());
            successfulPaymentAppCheckBox.setSelected(verifyPreferences.isPaymentApp());

            LimitExceedMailCheckBox.setSelected(verifyPreferences.isLimitExceededMail());
            LimitExceedAppCheckBox.setSelected(verifyPreferences.isLimitExceededApp());
        }
    }

    @FXML
    private void saveNotificationSettings() {
        boolean isTransactionMailEnabled = transactionMailCheckBox.isSelected();
        boolean isTransactionAppEnabled = transactionAppCheckBox.isSelected();

        boolean isPaymentMailEnabled = successfulPaymentMailCheckBox.isSelected();
        boolean isPaymentAppEnabled = successfulPaymentAppCheckBox.isSelected();

        boolean isLimitExceedMailEnabled = LimitExceedMailCheckBox.isSelected();
        boolean isLimitExceedAppEnabled = LimitExceedAppCheckBox.isSelected();

        if (verifyPreferences == null) {
            UserNotificationPreferences preferences = new UserNotificationPreferences(
                    true,
                    customer,
                    isTransactionMailEnabled,
                    isTransactionAppEnabled,
                    isPaymentMailEnabled,
                    isPaymentAppEnabled,
                    isLimitExceedMailEnabled,
                    isLimitExceedAppEnabled
            );
            preferencesService.save(preferencesMapper.mapTo(preferences));
        } else {
            verifyPreferences.setTransactionMail(isTransactionMailEnabled);
            verifyPreferences.setTransactionApp(isTransactionAppEnabled);
            verifyPreferences.setPaymentMail(isPaymentMailEnabled);
            verifyPreferences.setPaymentApp(isPaymentAppEnabled);
            verifyPreferences.setLimitExceededMail(isLimitExceedMailEnabled);
            verifyPreferences.setLimitExceededApp(isLimitExceedAppEnabled);

            preferencesService.update(preferencesMapper.mapTo(verifyPreferences));
        }

        updatePreferencesView();

        Stage stage = (Stage) transactionMailCheckBox.getScene().getWindow();
        stage.close();
    }

}
