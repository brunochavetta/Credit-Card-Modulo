package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import lombok.Setter;
import org.jala.university.domain.entity.CreditCard;

import java.awt.*;

public class SecurityCodeModalController {

    @FXML
    private PasswordField securityCodeField;

    private boolean isValid;

    @Setter
    private CreditCard creditCard;

    @FXML
    private void validateSecurityCode() {
        try {
            String securityCodeText = securityCodeField.getText();
            if (securityCodeText.matches("\\d{3}")) {
                int securityCode = Integer.parseInt(securityCodeText);
                if (creditCard.getSecurityCode() == securityCode) {
                    isValid = true;
                    closeWindow();
                } else {
                    securityCodeField.clear();
                    securityCodeField.setPromptText("Incorrect Code");
                }
            } else {
                securityCodeField.clear();
                securityCodeField.setPromptText("Invalid Code");
            }
        } catch (NumberFormatException e) {
            securityCodeField.clear();
            securityCodeField.setPromptText("Invalid Code");
        }
    }


    public boolean isValid() {
        return isValid;
    }

    private void closeWindow() {
        Stage stage = (Stage) securityCodeField.getScene().getWindow();
        stage.close();
    }
}

