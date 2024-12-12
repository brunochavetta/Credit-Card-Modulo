package org.jala.university.presentation.controller.CreateEntityController;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.mapper.ApplicationMapper;
import org.jala.university.application.service.ApplicationDocumentService;
import org.jala.university.application.service.CreditCardApplicationService;
import org.jala.university.domain.entity.ApplicationDocument;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.controller.MainViewControllerCreditCard.MenuController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CreditCardApplicationFormController {
    @FXML
    private TextField creationDateField, userNameField, companyNameField, businessIdField;
    @FXML
    private ComboBox<String> documentTypeComboBox, countryComboBox;
    @FXML
    private Button addDocumentButton, continueButton, requestPlatinumButton, finishButton, cancelButton, homeButton;
    @FXML
    private TableView<ApplicationDocument> documentsTable;
    @FXML
    private Label messageLabel;

    @FXML
    private Button viewApplicationsButton;

    @FXML
    private Button viewCreditCardByUser;

    private final MenuController menu = new MenuController();

    private final CreditCardApplicationService applicationService;
    private final ApplicationDocumentService documentService;
    private final CreateApplicationDocumentController documentController;
    private final ReviewDocumentController reviewDocumentController;
    private ObservableList<ApplicationDocument> documentList = FXCollections.observableArrayList();
    private CreditCardApplication currentApplication;
    private boolean isPlatinumRequested = false;
    private Customer customer;
    private final ApplicationMapper applicationMapper;

    public CreditCardApplicationFormController() {
        this.applicationService = ServiceFactoryCreditCard.creditCardApplicationService();
        this.documentService = ServiceFactoryCreditCard.documentServiceFactory();
        this.documentController = new CreateApplicationDocumentController();
        this.reviewDocumentController = new ReviewDocumentController(documentController);
        documentController.setReviewDocumentController(reviewDocumentController);
        this.applicationMapper = new ApplicationMapper();
    }

    @FXML
    public void initialize() {
        creationDateField.setText(LocalDateTime.now().toString());
        customer = CustomerSession.getInstance().getCurrentCustomer();
        userNameField.setText(customer.getFullName());
        messageLabel.setText("Credit card application for " + customer.getFullName());

        countryComboBox.setValue("Argentina");

        configureButtonActions();
        configureDocumentsTable();

        homeButton.setOnMouseClicked(event -> {
            redirectHome();
        });

        viewCreditCardByUser.setOnMouseClicked(event -> {
            menu.redirectToCreditCardView(viewCreditCardByUser);
        });

        viewApplicationsButton.setOnMouseClicked(event -> {
            menu.redirectToApplicationView(viewApplicationsButton);
        });
    }

    private void configureDocumentsTable() {
        TableColumn<ApplicationDocument, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDocumentType())
        );

        TableColumn<ApplicationDocument, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFileName())
        );

        TableColumn<ApplicationDocument, String> extensionColumn = new TableColumn<>("Extension");
        extensionColumn.setCellValueFactory(cellData -> {
            String fileName = cellData.getValue().getFileName();
            String fileExtension = documentController.getFileExtension(fileName);
            return new SimpleStringProperty(fileExtension);
        });

        TableColumn<ApplicationDocument, Void> viewColumn = new TableColumn<>("View");
        viewColumn.setCellFactory(col -> new TableCell<>() {
            private final Button viewButton = new Button("View");

            {
                viewButton.setOnAction(event -> {
                    ApplicationDocument document = getTableView().getItems().get(getIndex());
                    documentController.viewDocument(document.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });

        TableColumn<ApplicationDocument, Void> deleteColumn = new TableColumn<>("Delete");
        deleteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    ApplicationDocument document = getTableView().getItems().get(getIndex());
                    try {
                        documentService.deleteById(document.getId());
                        getTableView().getItems().remove(document);
                    } catch (RuntimeException e) {
                        System.err.println("Error deleting the document: " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        documentsTable.getColumns().addAll(typeColumn, nameColumn, extensionColumn, viewColumn, deleteColumn);

        documentsTable.setItems(documentList);
    }

    private void redirectHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/MainView/MainView.fxml"));
            Parent mainView = loader.load();
            Scene currentScene = homeButton.getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configureButtonActions() {
        requestPlatinumButton.setOnAction(event -> {
            isPlatinumRequested = !isPlatinumRequested;
            companyNameField.setVisible(isPlatinumRequested);
            countryComboBox.setVisible(isPlatinumRequested);
            businessIdField.setVisible(isPlatinumRequested);
        });

        continueButton.setOnAction(event -> {
            if (isPlatinumRequested) {
                if (businessIdField.getText().isEmpty() || companyNameField.getText().isEmpty()) {
                    showAlert("Error", "Business ID and Company Name are required.");
                    return;
                }

                String selectedCountry = countryComboBox.getValue();
                String businessId = businessIdField.getText();

                if (!isValidBusinessId(businessId, selectedCountry)) {
                    showAlert("Error", "The format of the Business ID is incorrect.");
                    return;
                }
            }

            currentApplication = new CreditCardApplication();
            currentApplication.setCompanyName(companyNameField.getText());
            currentApplication.setCompanyId(businessIdField.getText());
            currentApplication.setStatus(CreditCardApplication.ApplicationStatus.submitted);
            currentApplication.setApplicationDate(LocalDateTime.now());
            currentApplication.setCustomer(customer);

            try {
                applicationService.save(applicationMapper.mapTo(currentApplication));
                documentTypeComboBox.setVisible(true);
                addDocumentButton.setVisible(true);
                documentsTable.setVisible(true);
                finishButton.setVisible(true);
                cancelButton.setVisible(true);
            } catch (Exception ex) {
                showAlert("Error", "Could not save the application: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        addDocumentButton.setOnAction(e -> {
            String documentType = documentTypeComboBox.getValue();
            ApplicationDocument newDocument = documentController.saveDocument(currentApplication, documentType,
                    customer.getIdNumber());

            if (newDocument != null) {
                documentList.add(newDocument);
                documentsTable.refresh();
            } else {
                showAlert("Error", "Could not add the document.");
            }
        });

        cancelButton.setOnAction(e -> {
            handleCancel();
            redirectHome();
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/CreditCardApplicationForm.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setOnCloseRequest(event -> {
                event.consume();
                handleCancel();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        finishButton.setOnAction(e -> {
            if (documentList.size() < 3) {
                showAlert("Error", "You must add three documents.");
                return;
            }

            List<String> requiredTypes = Arrays.asList("Front ID", "Back ID", "Salary Bonus");
            for (String requiredType : requiredTypes) {
                boolean found = documentList.stream().anyMatch(doc -> doc.getDocumentType().equals(requiredType));
                if (!found) {
                    showAlert("Error", "Missing required document type: " + requiredType);
                    return;
                }
            }

            showAlert("Success", "All documents have been added.");
            redirectHome();
        });
    }

    private void handleCancel() {
        if (currentApplication != null) {
            for (ApplicationDocument document : documentList) {
                try {
                    documentService.deleteById(document.getId());
                } catch (RuntimeException ex) {
                    showAlert("Error", "Error deleting the document: " + ex.getMessage());
                }
            }
            documentList.clear();

            try {
                applicationService.deleteById(currentApplication.getId());
            } catch (RuntimeException ex) {
                showAlert("Error", "Error deleting the application: " + ex.getMessage());
            }
            currentApplication = null;
            showAlert("Success", "Application and documents have been canceled.");
        } else {
            showAlert("Warning", "No application to cancel.");
        }
    }

    private boolean isValidBusinessId(String businessId, String country) {
        switch (country.toLowerCase()) {
            case "argentina":
                return businessId.matches("\\d{2}-\\d{8}-\\d");
            case "colombia":
                return businessId.matches("\\d{8,10}");
            case "bolivia":
                return businessId.matches("\\d{7,10}");
            case "eeuu":
                return businessId.matches("\\d{2}-\\d{7}");
            default:
                return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

