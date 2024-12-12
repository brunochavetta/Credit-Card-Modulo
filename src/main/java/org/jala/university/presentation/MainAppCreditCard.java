package org.jala.university.presentation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.CustomerDto;
import org.jala.university.application.mapper.CustomerMapper;
import org.jala.university.application.service.CustomerService;
import org.jala.university.domain.entity.Customer;
import org.jala.university.infraestructure.session.CustomerSession;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class MainAppCreditCard extends Application {
    private static final Logger log = LoggerFactory.getLogger(MainAppCreditCard.class);
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        login();

        Parent root = FXMLLoader.load(getClass().getResource("/org/jala/university/presentation/MainView/MainView.fxml"));
        primaryStage.setTitle("Jala University Bank");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void login() {
        CustomerMapper customerMapper = new CustomerMapper();
        CustomerService customerService = ServiceFactoryCreditCard.customerServiceFactory();

        UUID customerId = UUID.fromString("E28A1DD3-A9E0-11EF-9D84-00410E4723A3");
        CustomerDto customerDtoNew = customerService.findById(customerId);

        Customer customer = customerMapper.mapFrom(customerDtoNew);
        CustomerSession.getInstance().setCurrentCustomer(customer);
    }

    public static void setRoot(String fxml) throws Exception {
        Parent root = FXMLLoader.load(MainAppCreditCard.class.getResource("/org/jala/university/presentation/" + fxml + ".fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
