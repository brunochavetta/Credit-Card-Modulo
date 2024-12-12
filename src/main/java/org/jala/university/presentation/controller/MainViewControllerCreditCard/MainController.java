package org.jala.university.presentation.controller.MainViewControllerCreditCard;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class MainController {

    public MainController() {
    }

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

    private final MenuController menu = new MenuController();

    @FXML
    public void initialize() {
        if (menuText != null) {
            menuText.setText("Menú");
        } else {
            System.err.println("Error: menuText is null!");
        }

        requestCardButton.setOnMouseClicked(event -> menu.redirectToFormView(requestCardButton));

        viewCreditCardByUser.setOnMouseClicked(event -> menu.redirectToCreditCardView(viewCreditCardByUser));

        viewApplicationsButton.setOnMouseClicked(event -> menu.redirectToApplicationView(viewApplicationsButton));

        viewDebtsByUser.setOnMouseClicked(event -> menu.redirectToDebtsView(viewDebtsByUser));
    }
}
