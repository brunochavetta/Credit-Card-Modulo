package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.mapper.TransactionMapper;
import org.jala.university.application.service.CreditCardTransactionService;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardTransaction;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CreditCardTransactionController {

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField transactionAmountField;

    @FXML
    private TextArea transactionDescriptionField;

    @FXML
    private Label resultLabel;

    private List<CreditCard> creditCards;
    private CreditCard creditCard;

    private final CreditCardTransactionService transactionService;

    public CreditCardTransactionController() {
        this.transactionService = ServiceFactoryCreditCard.transactionServiceFactory();
    }

    @FXML
    public void initialize() {
        creditCards = ServiceFactoryCreditCard.creditCardServiceFactory()
                .findByUser()
                .stream()
                .map(new CreditCardMapper()::mapFrom)
                .toList();
    }

    private boolean openSecurityCodeModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/SecurityCodeModal.fxml"));
            Parent modalRoot = loader.load();

            SecurityCodeModalController modalController = loader.getController();
            modalController.setCreditCard(creditCard);

            Stage modalStage = new Stage();
            modalStage.setTitle("Security Code");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

            return modalController.isValid();
        } catch (IOException e) {
            resultLabel.setText("Error opening security code modal.");
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void processTransaction() {
        String cardNumber = cardNumberField.getText().trim();
        String transactionAmountText = transactionAmountField.getText().trim();
        String transactionDescription = transactionDescriptionField.getText().trim();

        try {
            if (cardNumber.isEmpty() || transactionAmountText.isEmpty() || transactionDescription.isEmpty()) {
                resultLabel.setText("All fields are required.");
                return;
            }

            if (!isValidCardNumber(cardNumber)) {
                resultLabel.setText("Card number does not belong to the user.");
                return;
            }

            double transactionAmount = Double.parseDouble(transactionAmountText);
            if (transactionAmount <= 0) {
                resultLabel.setText("Amount must be greater than zero.");
                return;
            }

            if (!openSecurityCodeModal()) {
                resultLabel.setText("Transaction canceled. Invalid security code.");
                return;
            }

            CreditCardTransaction transaction = new CreditCardTransaction(creditCard, transactionAmount, transactionDescription);
            transactionService.save(new TransactionMapper().mapTo(transaction));
            resultLabel.setText("Transaction successful for $" + transactionAmount);
        } catch (NumberFormatException e) {
            resultLabel.setText("Invalid amount. Use a numeric value.");
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        String sanitizedCardNumber = cardNumber.replaceAll("\\s", "");

        StringBuilder formattedCardNumber = new StringBuilder();
        for (int i = 0; i < sanitizedCardNumber.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formattedCardNumber.append(" ");
            }
            formattedCardNumber.append(sanitizedCardNumber.charAt(i));
        }

        Optional<CreditCard> optionalCard = creditCards.stream()
                .filter(card -> card.getCardNumber().equals(formattedCardNumber.toString()))
                .findFirst();

        if (optionalCard.isPresent()) {
            creditCard = optionalCard.get();
            return true;
        }

        return false;
    }

}
