package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.mapper.PaymentMapper;
import org.jala.university.application.service.CreditCardPaymentService;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardPayment;
import org.jala.university.domain.entity.Customer;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.controller.MainViewControllerCreditCard.MenuController;

public class CreditCardPaymentController {

    @FXML
    private ComboBox<String> paymentOption;

    @FXML
    private Label monthAndYear;

    @FXML
    private Text totalDebt;

    @FXML
    private TextField amountField;

    @FXML
    private Button sendPayment;

    @FXML
    private Button requestCardButton;

    @FXML
    private Button viewApplicationsButton;

    @FXML
    private Button viewCreditCardByUser;

    @FXML
    private Text menuText;

    @FXML
    private Button viewDebtsByUser;

    @FXML
    private Button homeButton;

    private final MenuController menu = new MenuController();

    private final CreditCardPaymentService creditCardPaymentService;

    private CreditCardDebt debt;

    private Customer customer;

    public CreditCardPaymentController() {
        this.creditCardPaymentService = ServiceFactoryCreditCard.paymentServiceFactory(); 
    }

    public void setDebt(CreditCardDebt debt) {
        this.debt = debt; updateView();

        if (menuText != null) {
            menuText.setText("Menú");
        } else {
            System.err.println("Error: menuText is null!");
        }

        homeButton.setOnMouseClicked(event -> menu.redirectToHome(homeButton));

        requestCardButton.setOnMouseClicked(event -> menu.redirectToFormView(requestCardButton));

        viewCreditCardByUser.setOnMouseClicked(event -> menu.redirectToCreditCardView(viewCreditCardByUser));

        viewApplicationsButton.setOnMouseClicked(event -> menu.redirectToApplicationView(viewApplicationsButton));

        viewDebtsByUser.setOnMouseClicked(event -> menu.redirectToDebtsView(viewDebtsByUser));
    }

    private void updateView() {
        int year = debt.getDueDate().getYear();
        String newDebtName = debt.getMonth() + " " + year;
        monthAndYear.setText(newDebtName);
        totalDebt.setText("$" + debt.getOutstandingAmount());
        sendPayment.setOnMouseClicked(event -> handlePayment());
    }

    @FXML
    public void initialize() {
        customer = CustomerSession.getInstance().getCurrentCustomer();
        paymentOption.valueProperty().addListener((observable, oldValue, newValue) -> setVisibleAmount());
    }

    private void setVisibleAmount() {
        String selectedOption = paymentOption.getValue();
        if ("Custom Payment".equals(selectedOption)) {
            amountField.setVisible(true);
        } else {
            amountField.setVisible(false);
        }
    }

    @FXML
    private void handlePayment() {
        try {
            String selectedOption = paymentOption.getValue();
            double totalDebt = debt.getOutstandingAmount();
            double minPayment = totalDebt * 0.15;
            double amount = 0;

            if ("Custom Payment".equals(selectedOption)) {
                if (amount < minPayment) {
                    showAlert("Error", "The amount must be greater than or equal to the minimum payment (" + minPayment + ")");
                    return;
                } else if (amount > totalDebt) {
                    showAlert("Error", "The amount cannot exceed the total debt.");
                    return;
                }
                amount = Double.parseDouble(amountField.getText());
            } else if ("Minimum Payment".equals(selectedOption)) {
                amount = minPayment;
            } else if ("Total Payment".equals(selectedOption)) {
                amount = totalDebt;
            }

            if (customer.getAccount().getBalance() < amount) {
                showAlert("Insufficient Balance", "Your account balance is not enough to process the payment.");
                return;
            }

            processPayment(amount);

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount.");
        }
    }

    private void processPayment(double amount) {
        CreditCardPayment payment = new CreditCardPayment(amount, debt);
        creditCardPaymentService.save(new PaymentMapper().mapTo(payment));
        showAlert("Successful payment", ("Payment of $" + amount + " was made"));
        viewDebtsByUser.setOnMouseClicked(event -> menu.redirectToDebtsView(viewDebtsByUser));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
