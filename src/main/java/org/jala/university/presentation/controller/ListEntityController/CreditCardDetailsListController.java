package org.jala.university.presentation.controller.ListEntityController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Customer;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.controller.CreateEntityController.CreditCardDetailsController;
import org.jala.university.presentation.controller.MainViewControllerCreditCard.MenuController;

import java.io.IOException;
import java.util.List;

public class CreditCardDetailsListController {
    private Customer customer;

    @FXML
    private VBox cardDetailsContainer;

    @FXML
    private Button homeButton;

    @FXML
    private Button requestCardButton;

    @FXML
    private Button viewApplicationsButton;

    @FXML
    private Button requestTransactionButton;

    private final MenuController menu = new MenuController();

    private final CreditCardService creditCardService; 
    private final CreditCardMapper creditCardMapper; 

    public CreditCardDetailsListController() {
        this.creditCardService = ServiceFactoryCreditCard.creditCardServiceFactory(); 
        this.creditCardMapper = new CreditCardMapper(); 
    }

    @FXML
    public void initialize() {
        this.customer = CustomerSession.getInstance().getCurrentCustomer();
        loadCreditCards();

        homeButton.setOnMouseClicked(event -> menu.redirectToHome(homeButton));

        requestCardButton.setOnMouseClicked(event -> menu.redirectToFormView(requestCardButton));

        viewApplicationsButton.setOnMouseClicked(event -> menu.redirectToApplicationView(viewApplicationsButton));



        requestTransactionButton.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/TransactionReportView.fxml"));
                Parent mainView = loader.load();
                Scene currentScene = requestTransactionButton.getScene();
                currentScene.setRoot(mainView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadCreditCards() {
        if (customer != null) {
            List<CreditCardDto> cardsDto = creditCardService.findByUser();
            List<CreditCard> cards = cardsDto.stream().map(creditCardMapper::mapFrom).toList(); 
            for (CreditCard card : cards) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/CreditCardDetail.fxml"));
                    AnchorPane cardPane = loader.load();
                    CreditCardDetailsController controller = loader.getController();
                    controller.setCardDetails(card);
                    cardDetailsContainer.getChildren().add(cardPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}