package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.jala.university.domain.entity.CreditCardDebt;

public class CreditCardDebtController {
    @FXML
    AnchorPane cardDebtContainer;

    @FXML
    private Label debtName;

    @FXML
    private Button paymentButton;

    @FXML
    private Button generateReportDebtButton;

    @FXML
    private Text totalAmount;

    private CreditCardDebt debt;

    public void setDebt(CreditCardDebt debt) {
        this.debt = debt; updateView();
    }

    private void updateView() {
        cardDebtContainer.setVisible(true);
        int year = debt.getDueDate().getYear();
        String newDebtName = debt.getMonth() + " " + year;
        debtName.setText(newDebtName);
        totalAmount.setText("$" + debt.getOutstandingAmount());
        paymentButton.setOnMouseClicked(event -> loadPaymentForm());
        generateReportDebtButton.setOnMouseClicked(event -> loadReportView());
    }

    private void loadPaymentForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/PaymentForm.fxml"));
            Parent formPaymentView = loader.load();
            CreditCardPaymentController controller = loader.getController();
            controller.setDebt(debt);
            Scene currentScene = paymentButton.getScene();
            currentScene.setRoot(formPaymentView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadReportView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/CreditCardReportView.fxml"));
            Parent reportView = loader.load();
            CreditCardTransactionMonthlyReport controller = loader.getController();
            controller.setDebt(debt);
            Scene currentScene = generateReportDebtButton.getScene();
            currentScene.setRoot(reportView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
