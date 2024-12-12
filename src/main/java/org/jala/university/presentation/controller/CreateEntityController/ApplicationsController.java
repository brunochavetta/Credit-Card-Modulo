package org.jala.university.presentation.controller.CreateEntityController;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.ApplicationDTO;
import org.jala.university.application.service.ApplicationDocumentService;
import org.jala.university.application.service.CreditCardApplicationService;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.entity.Customer;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.controller.MainViewControllerCreditCard.MenuController;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ApplicationsController {
    @FXML
    private AnchorPane mainLayout;
    @FXML
    private JFXListView<VBox> applicationsList;
    @FXML
    private JFXButton refreshButton;
    @FXML
    private Button homeButton;

    @FXML
    private Button requestCardButton;

    @FXML
    private Button viewCreditCardByUser;

    private final MenuController menu = new MenuController();

    private JFXSnackbar snackbar;
    private CreditCardApplicationController applicationController;
    private final CreditCardApplicationService applicationService;
    private final ApplicationDocumentService documentService;
    private Customer customer;

    public ApplicationsController() {
        this.applicationService = ServiceFactoryCreditCard.creditCardApplicationService();
        this.applicationController = new CreditCardApplicationController();
        this.documentService = ServiceFactoryCreditCard.documentServiceFactory();
    }

    public void initialize() {
        if (snackbar == null) {
            snackbar = new JFXSnackbar(mainLayout);
        }

        refreshButton.setOnAction(event -> refreshApplications());

        this.applicationController = new CreditCardApplicationController();
        this.customer = CustomerSession.getInstance().getCurrentCustomer();
        refreshApplications();

        homeButton.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/MainView/MainView.fxml"));
                Parent mainView = loader.load();
                Scene currentScene = homeButton.getScene();
                currentScene.setRoot(mainView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        requestCardButton.setOnMouseClicked(event -> {
            menu.redirectToFormView(requestCardButton);
        });

        viewCreditCardByUser.setOnMouseClicked(event -> {
            menu.redirectToCreditCardView(viewCreditCardByUser);
        });
    }

    private void refreshApplications() {
        applicationsList.getItems().clear();
        applicationsList.getItems().addAll(getUserApplications());
        showSimpleSnackbar("Applications refreshed.");
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
            applicationBox.getChildren().add(applicationLabel);

            if (application.getStatus() != CreditCardApplication.ApplicationStatus.rejected && application.getStatus() != CreditCardApplication.ApplicationStatus.completed){
                JFXButton actionButton;

                if (application.getStatus() == CreditCardApplication.ApplicationStatus.accepted) {
                    actionButton = new JFXButton("Generate Card");
                    actionButton.setOnAction(event -> {
                        try {
                            Customer customerAux = CustomerSession.getInstance().getCurrentCustomer();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/CreditCardView.fxml"));
                            Parent mainView = loader.load();
                            CreditCardController creditCardController = loader.getController();

                            creditCardController.setParameters(customerAux.getSalary(), false, application);

                            Scene currentScene = actionButton.getScene();
                            currentScene.setRoot(mainView);
                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert("Error", "Failed to load the CreditCardView.");
                        }
                    });
                } else {
                    actionButton = new JFXButton("Verify Salary");
                    actionButton.setOnAction(event -> {
                        ApplicationDocument salaryBonusDocument = findSalaryBonusDocument(application);
                        if (salaryBonusDocument != null) {
                            verifySalary(salaryBonusDocument, application);
                        } else {
                            showSimpleSnackbar("Salary Bonus document not found for Application ID: " + application.getId());
                        }
                    });
                }

                    JFXButton deleteApplication = new JFXButton("Delete");
                    deleteApplication.setOnAction(event -> {
                        try {
                            ApplicationDTO existingApplication = applicationService.findById(application.getId());
                             if (existingApplication != null) {
                                applicationService.delete(existingApplication);

                                if (application.getDocuments() != null) {
                                    for (ApplicationDocument document : application.getDocuments()) {
                                        if (document != null) {
                                            documentService.deleteById(document.getId());
                                        }
                                    }
                                }

                                refreshApplications();
                                showAlert("Success", "Application and its documents deleted successfully.");
                            } else {
                                showAlert("Error", "Application not found.");
                            }
                        } catch (Exception e) {
                            showAlert("Error", "Sorry, the app cannot be deleted at this time.");
                        }
                    });


                    applicationBox.getChildren().addAll(actionButton, deleteApplication);
            }

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

    private void showSnackbar(javafx.scene.Node message) {
        if (snackbar == null) {
            snackbar = new JFXSnackbar(mainLayout);
        }
        snackbar.enqueue(new JFXSnackbar.SnackbarEvent(message));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

