package org.jala.university.presentation.EntityView;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.presentation.controller.CreateEntityController.CreditCardApplicationController;

public class UserApplicationsView {
    private JFXSnackbar snackbar;
    private CreditCardApplicationController applicationController;

    public UserApplicationsView(Stage stage) {
        applicationController = new CreditCardApplicationController();
    }

    @FXML
    private BorderPane mainLayout;

    @FXML
    public void initialize() {
        if (snackbar == null) {
            snackbar = new JFXSnackbar(mainLayout);
        }
    }

    public void viewApplications(Stage primaryStage) {
        primaryStage.setTitle("User Applications");

        BorderPane mainLayout = new BorderPane();
        VBox header = new VBox();
        header.setPadding(new Insets(10));
        header.setSpacing(10);

        Label title = new Label("Your Applications");
        title.setStyle("-fx-font-size: 24px;");

        JFXListView<VBox> applicationsList = new JFXListView<>();
        applicationsList.getItems().addAll(getUserApplications());

        JFXButton refreshButton = new JFXButton("Refresh Applications");
        refreshButton.setOnAction(event -> {
            applicationsList.getItems().clear();
            applicationsList.getItems().addAll(getUserApplications());
            showSimpleSnackbar("Applications refreshed.");
        });

        header.getChildren().addAll(title, refreshButton);
        mainLayout.setTop(header);
        mainLayout.setCenter(applicationsList);

        Scene scene = new Scene(mainLayout, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/styles/jfoenix.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private List<VBox> getUserApplications() {
        List<CreditCardApplication> applications = applicationController.listApplications();
        List<VBox> displayItems = new ArrayList<>();
    
        for (CreditCardApplication application : applications) {
            VBox applicationBox = new VBox();
            StringBuilder displayText = new StringBuilder();
            displayText.append("ID: ").append(application.getId()).append(", ");
            displayText.append("Date: ")
                    .append(application.getApplicationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                    .append(", ");
            displayText.append("Status: ").append(application.getStatus()).append(", ");
            displayText.append("Documents: ");
    
            if (application.getDocuments() != null && !application.getDocuments().isEmpty()) {
                displayText.append(application.getDocuments().stream()
                        .map(ApplicationDocument::getDocumentType)
                        .collect(Collectors.joining(", ")));
            } else {
                displayText.append("None");
            }
    
            Label applicationLabel = new Label(displayText.toString());
            JFXButton verifySalaryButton = new JFXButton("Verify Salary");
            verifySalaryButton.setOnAction(event -> {
                ApplicationDocument salaryBonusDocument = findSalaryBonusDocument(application);
                if (salaryBonusDocument != null) {
                    verifySalary(salaryBonusDocument, application); 
                } else {
                    showSimpleSnackbar("Salary Bonus document not found for Application ID: " + application.getId());
                }
            });
    
            applicationBox.getChildren().addAll(applicationLabel, verifySalaryButton);
            displayItems.add(applicationBox);
        }
    
        return displayItems;
    }
    
    private ApplicationDocument findSalaryBonusDocument(CreditCardApplication application) {
        return application.getDocuments().stream()
                .filter(doc -> "Salary Bonus".equals(doc.getDocumentType()))
                .findFirst()
                .orElse(null);
    }
    
    private void verifySalary(ApplicationDocument salaryBonusDocument, CreditCardApplication application) {
        applicationController.verifySalary(salaryBonusDocument.getId(), application);
        showSimpleSnackbar("Verifying salary for document: " + salaryBonusDocument.getDocumentType());
    }

    private void showSimpleSnackbar(String text) {
        Label label = new Label(text);
        showSnackbar(label);
    }

    private void showSnackbar(Node message) {
        if (snackbar == null) {
            initialize();
        } 
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(message));
    }
}
