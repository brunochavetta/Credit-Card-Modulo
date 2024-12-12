package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.CreditCardTypeDTO;
import org.jala.university.application.mapper.ApplicationMapper;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.mapper.CreditCardTypeMapper;
import org.jala.university.application.service.CreditCardApplicationService;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.application.service.CreditCardTypeService;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.CreditCardType;
import org.jala.university.domain.entity.CreditCardApplication.ApplicationStatus;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.jala.university.presentation.controller.MainViewControllerCreditCard.MenuController;

public class CreditCardController {

    @FXML
    private ComboBox<String> creditCardTypeCombo;

    @FXML
    private TextField creditLimitField;

    @FXML
    private Button modifyLimitButton;

    @FXML
    private Button submitButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button requestCardButton;

    @FXML
    private Button viewCreditCardByUser;

    @FXML
    private Button viewApplicationsButton;

    private final MenuController menu = new MenuController();

    private final CreditCardTypeService creditCardTypeService = ServiceFactoryCreditCard.typeServiceFactory();
    private final CreditCardService creditCardService; 
    private final ApplicationMapper applicationMapper; 
    private final CreditCardApplicationService applicationService;
    private final CreditCardMapper creditCardMapper; 
    private final CreditCardTypeMapper typeMapper; 

    private Double salary;
    private boolean hasCompanyNote;
    private CreditCardApplication application;
    private List<CreditCardTypeDTO> availableTypes;

    public CreditCardController() {
        this.creditCardService = ServiceFactoryCreditCard.creditCardServiceFactory(); 
        this.applicationService = ServiceFactoryCreditCard.creditCardApplicationService(); 
        this.applicationMapper = new ApplicationMapper(); 
        this.creditCardMapper = new CreditCardMapper(); 
        this.typeMapper = new CreditCardTypeMapper(); 
    }

    public void setParameters(Double salary, boolean hasCompanyNote, CreditCardApplication application) {
        this.salary = salary;
        this.hasCompanyNote = hasCompanyNote;
        this.application = application;
        System.out.println("Application id desde Credit Card Controller: " + application.getId());
        initializeAfterParameters();
    }

    public void initializeAfterParameters() {
        if (application == null) {
            showAlert("Error", "application is null in in card creation");
            return;
        }

        getAvailableCreditCardTypes();

        if (availableTypes.isEmpty()) {
            return;
        }

        creditCardTypeCombo.getItems().clear();
        availableTypes.forEach(type -> creditCardTypeCombo.getItems().add(
                type.getTypeName() + " (max credit: " + type.getMaxCreditAmount() + ", fee: " + type.getMonthlyFee() + ")"
        ));

        creditCardTypeCombo.getSelectionModel().selectFirst();

        homeButton.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/MainView/MainView.fxml"));
                Parent mainView = loader.load();
                Scene currentScene = homeButton.getScene();
                currentScene.setRoot(mainView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        requestCardButton.setOnMouseClicked(event -> {
            menu.redirectToFormView(requestCardButton);
        });

        viewCreditCardByUser.setOnMouseClicked(event -> {
            menu.redirectToCreditCardView(viewCreditCardByUser);
        });

        viewApplicationsButton.setOnMouseClicked(event -> {
            menu.redirectToApplicationView(viewApplicationsButton);
        });
    }

    @FXML
    public void handleModifyLimitButton() {
        creditLimitField.setVisible(!creditLimitField.isVisible());
    }

    public void getAvailableCreditCardTypes() {
        availableTypes = new ArrayList<>();

        if (application.getStatus() == ApplicationStatus.accepted) {
            if (hasCompanyNote) {
                availableTypes.add(creditCardService.returnCreditCardType(1));
            } else if (salary >= 1000 && salary <= 2000) {
                availableTypes.add(creditCardService.returnCreditCardType(2));
            } else if (salary > 2000) {
                availableTypes.add(creditCardService.returnCreditCardType(2));
                if (salary <= 4500) availableTypes.add(creditCardService.returnCreditCardType(3));
                if (salary > 4500) availableTypes.add(creditCardService.returnCreditCardType(4));
            }
        }
    }

    public void modifyStatus() {
        if (salary < 1000) {
            application.setStatus(ApplicationStatus.rejected);
            showAlert("Sorry", "Your salary is not sufficient for a credit card.");
        } else if (application.getStatus() == ApplicationStatus.in_review) {
            application.setStatus(ApplicationStatus.accepted);
            showAlert("Congratulations", "You can now apply for your credit card.");
        } else {
            showAlert("Notice", "Your application is under review.");
        }

        applicationService.update(applicationMapper.mapTo(application));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleSubmitButton() {
        if (creditCardTypeCombo.getSelectionModel().isEmpty()) {
            showAlert("Error", "Select a credit card type.");
            return;
        }

        Double limit = creditLimitField.isVisible() && !creditLimitField.getText().isEmpty()
                ? Double.parseDouble(creditLimitField.getText())
                : availableTypes.get(creditCardTypeCombo.getSelectionModel().getSelectedIndex()).getMaxCreditAmount();

        if (!validateCreditLimit(limit, availableTypes.get(creditCardTypeCombo.getSelectionModel().getSelectedIndex()))) {
            showAlert("Error", "Selected limit exceeds maximum allowed.");
            return;
        }

        createCreditCard(availableTypes.get(creditCardTypeCombo.getSelectionModel().getSelectedIndex()), limit, application);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/ListEntityView/CreditCardList.fxml"));
            Parent mainView = loader.load();
            Scene currentScene = homeButton.getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateCreditLimit(Double selectedCreditLimit, CreditCardTypeDTO creditCardType) {
        return selectedCreditLimit <= creditCardType.getMaxCreditAmount();
    }

    public CreditCard createCreditCard(CreditCardTypeDTO selectedType, Double selectedCreditLimit,
                                       CreditCardApplication application) {
        String cardNumber = creditCardService.generateRandomCardNumber();
        int securityCode = creditCardService.generateSecurityCode();

        CreditCardType type = new CreditCardType();
        type.setTypeName(selectedType.getTypeName());
        type.setDescription(selectedType.getDescription());
        type.setMaxCreditAmount(selectedType.getMaxCreditAmount());
        type.setMonthlyFee(selectedType.getMonthlyFee());

        CreditCard newCard = new CreditCard(
                cardNumber, LocalDateTime.now(), LocalDateTime.now().plusYears(3),
                selectedCreditLimit, application, CreditCard.CreditCardStatus.active,
                type, securityCode
        );

        creditCardTypeService.save(typeMapper.mapTo(type));

        creditCardService.save(creditCardMapper.mapTo(newCard));
        application.setStatus(ApplicationStatus.completed);
        applicationService.update(applicationMapper.mapTo(application));
        showAlert("Success", "Credit card created successfully.");
        return newCard;
    }
}

