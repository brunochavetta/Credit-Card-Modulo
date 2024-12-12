package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.NotificationDto;
import org.jala.university.application.mapper.NotificationMapper;
import org.jala.university.application.mapper.PreferencesMapper;
import org.jala.university.application.service.NotificationService;
import org.jala.university.application.service.UserNotificationPreferencesService;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.Notification;
import org.jala.university.domain.entity.UserNotificationPreferences;
import org.jala.university.infraestructure.session.CustomerSession;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationController {

    @FXML
    private Text numberOfUnreadNotifications;

    @FXML
    private ToggleButton notificationsToggleButton;

    @FXML
    private MenuButton notificationMenuButton;

    @FXML
    private VBox notificationsVBox;

    @FXML
    private ScrollPane notificationsScrollPane;

    @FXML
    private CustomMenuItem notificationsScrollItem;

    private final NotificationService notificationService; 
    private final UserNotificationPreferencesService preferencesService; 
    private final NotificationMapper notificationMapper; 
    private final PreferencesMapper preferencesMapper; 

    private UserNotificationPreferences userPreferences;
    private List<Notification> notifications;

    public NotificationController() {
        this.notificationService = ServiceFactoryCreditCard.notificationServiceFactory(); 
        this.preferencesService = ServiceFactoryCreditCard.preferencesServiceFactory(); 
        this.notificationMapper = new NotificationMapper(); 
        this.preferencesMapper = new PreferencesMapper();
    }

    @FXML
    private void initialize() {
        loadUserPreferences();
        updateToggleButtonState();

        notificationsToggleButton.setOnMouseClicked(event -> {
            toggleNotifications();
            updateToggleButtonState();
        });

        notificationMenuButton.setOnMouseClicked(event -> markNotificationsAsRead());
    }

    private void loadUserPreferences() {
        userPreferences = preferencesMapper.mapFrom(preferencesService.findPreferencesByUser());
        Customer customer = CustomerSession.getInstance().getCurrentCustomer();
        if (userPreferences.getCustomer() == null) {
            userPreferences = new UserNotificationPreferences(false, customer, false,
                    false, false, false, false, false);
            preferencesService.save(preferencesMapper.mapTo(userPreferences));
        }
    }

    private void updateToggleButtonState() {
        if (userPreferences.isNotifications()) {
            notificationsToggleButton.setSelected(true);
            notificationsToggleButton.setText("✔");
            notificationsToggleButton.setStyle("-fx-text-fill: green;");
            notificationMenuButton.setVisible(true);
            loadNotifications();
            addNotificationPreferencesItem();
        } else {
            notificationsToggleButton.setSelected(false);
            notificationsToggleButton.setText("🛇");
            notificationsToggleButton.setStyle("-fx-text-fill: red;");
            notificationMenuButton.setVisible(false);
            numberOfUnreadNotifications.setVisible(false);
        }
    }

    private void markNotificationsAsRead() {
        notifications.stream()
                .filter(notification -> !notification.isSeen())
                .forEach(notification -> {
                    notification.setSeen(true);
                    notificationService.update(notificationMapper.mapTo(notification));
                });

        loadNotifications();
    }

    private void updateUnreadNotificationsCount() {
        int countNotification = (int) notifications.stream()
                .filter(notification -> !notification.isSeen())
                .count();

        numberOfUnreadNotifications.setText(String.valueOf(countNotification));
        numberOfUnreadNotifications.setVisible(countNotification > 0);
    }

    public void loadNotifications() {
        List<NotificationDto> notificationDtos = notificationService.listByUser()
                .stream()
                .filter(notification -> notification.getChannel() == Notification.Channel.APP)
                .collect(Collectors.toList());

        notifications = notificationDtos.stream().map(notificationMapper::mapFrom).toList(); 

        updateUnreadNotificationsCount();
        addNotificationsInMenu();
    }


    private void addNotificationsInMenu () {
        notificationsVBox.getChildren().clear();

        notifications.forEach(notification -> {
            VBox vbox = new VBox(5);

            Label subjectLabel = new Label(notification.getSubject());
            subjectLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-color: transparent;");

            Label messageLabel = new Label(notification.getMessage());
            messageLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: gray;" +
                    "-fx-background-color: transparent;");

            vbox.getChildren().addAll(subjectLabel, messageLabel);

            notificationsVBox.getChildren().add(vbox);
        });

        notificationsScrollItem.setContent(notificationsScrollPane);

        notificationsScrollPane.setContent(notificationsVBox);
    }

    public void addNotificationPreferencesItem() {
        boolean preferencesAdded = false;

        for (MenuItem item : notificationMenuButton.getItems()) {
            if (item instanceof CustomMenuItem customItem) {
                if (customItem.getContent() instanceof Hyperlink link) {
                    if (link.getText().equals("Set notification preferences")) {
                        preferencesAdded = true;
                        break;
                    }
                }
            }
        }

        if (!preferencesAdded) {
            CustomMenuItem preferencesItem = new CustomMenuItem();
            Hyperlink preferencesLink = new Hyperlink("Set notification preferences");
            preferencesLink.setOnAction(event -> openNotificationConfig());
            preferencesLink.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
            preferencesItem.setStyle("-fx-background-color: transparent;");
            preferencesItem.setContent(preferencesLink);
            notificationMenuButton.getItems().add(preferencesItem);
        }
    }

    @FXML
    public void toggleNotifications() {
        boolean notificationsEnabled = notificationsToggleButton.isSelected();
        userPreferences.setNotifications(notificationsEnabled);
        preferencesService.update(preferencesMapper.mapTo(userPreferences));
    }

    private void openNotificationConfig() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/NotificationConfigView.fxml"));

            Stage modalStage = new Stage();

            modalStage.setScene(new Scene(loader.load()));

            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Notification Preferences");
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
