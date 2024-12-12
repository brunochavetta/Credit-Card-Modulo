package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Customer;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.EntityView.CreditCardGeneratorView;

public class CreditCardDetailsController {
    @FXML
    private AnchorPane cardDetailsContainer;

    @FXML
    private Pane paneCreditCard;

    @FXML
    private Label creditLimitLabel, cardTypeLabel, statusLabel;

    private Customer customer;

    private CreditCard creditCard;

    public CreditCardDetailsController() {
    }

    @FXML
    public void initialize() {
        this.customer = CustomerSession.getInstance().getCurrentCustomer();
    }

    public void setCardDetails(CreditCard creditCard) {
        this.creditCard = creditCard;
        updateView();
    }

    private void updateView() {
        cardDetailsContainer.setVisible(true);
        CreditCardGeneratorView viewCreditCard = new CreditCardGeneratorView(customer.getFullName(), creditCard);
        Pane generatedPane = viewCreditCard.generateCard();
        paneCreditCard.getChildren().setAll(generatedPane);
        creditLimitLabel.setText("Credit Limit: " + creditCard.getSelectCreditLimit());
        cardTypeLabel.setText("Card Type: " + creditCard.getCreditCardType().getTypeName());
        statusLabel.setText("Status: " + creditCard.getStatus());
    }

    @FXML
    private void updateLimit() {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Update Credit Card Limit");

        CreditCardService creditCardService = ServiceFactoryCreditCard.creditCardServiceFactory();
        CreditCardMapper creditCardMapper = new CreditCardMapper();
        double currentLimit = creditCard.getSelectCreditLimit(); 
        double maxLimit = creditCard.getCreditCardType().getMaxCreditAmount();  

        Label currentLimitLabel = new Label("Current Limit: " + currentLimit);

        TextField newLimitField = new TextField();
        newLimitField.setPromptText("Enter new limit");

        Button changeLimitButton = new Button("Change Limit");
        changeLimitButton.setOnAction(event -> {
            try {
                double newLimit = Double.parseDouble(newLimitField.getText());
                if (newLimit > maxLimit) {
                    showAlert("Error", "The new limit cannot exceed " + maxLimit + ".");
                } else {
                    creditCard.setSelectCreditLimit(newLimit);
                    creditCardService.update(creditCardMapper.mapTo(creditCard));
                    currentLimitLabel.setText("Current Limit: " + newLimit);
                    showAlert("Success", "The limit has been successfully updated.");
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Please enter a valid number.");
            }
        });

        VBox layout = new VBox(10, currentLimitLabel, newLimitField, changeLimitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        Scene scene = new Scene(layout, 300, 200);
        modalStage.setScene(scene);

        modalStage.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
