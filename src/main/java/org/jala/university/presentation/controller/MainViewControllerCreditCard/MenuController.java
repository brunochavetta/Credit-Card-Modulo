package org.jala.university.presentation.controller.MainViewControllerCreditCard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.jala.university.presentation.controller.CreateEntityController.CreditCardTransactionController;

import java.io.IOException;

public class MenuController {

    public void redirectToHome(Button homeButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/MainView/MainView.fxml"));
            Parent mainView = loader.load();
            Scene currentScene = homeButton.getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToFormView (Button requestCardButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/CreditCardApplicationForm.fxml"));
            Parent mainView = loader.load();
            Scene currentScene = requestCardButton.getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToCreditCardView (Button viewCreditCardByUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/ListEntityView/CreditCardList.fxml"));
            Parent mainView = loader.load();
            Scene currentScene = viewCreditCardByUser.getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToApplicationView(Button viewApplicationsButton) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/ApplicationsView.fxml"));
            Parent root = loader.load();
            Scene currentScene = viewApplicationsButton.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToDebtsView (Button viewDebtsByUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/ListEntityView/CreditCardDebtsList.fxml"));
            Parent root = loader.load();
            Scene currentScene = viewDebtsByUser.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void redirectToTransactionView (Button viewTransactionSimulation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/CreditCardTransactionForm.fxml"));
            Parent root = loader.load();
            CreditCardTransactionController controller = loader.getController();
            Scene currentScene = viewTransactionSimulation.getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
