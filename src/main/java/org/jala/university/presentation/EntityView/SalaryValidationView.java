package org.jala.university.presentation.EntityView;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.ApplicationDTO;
import org.jala.university.application.mapper.ApplicationMapper;
import org.jala.university.application.service.ApplicationDocumentService;
import org.jala.university.application.service.CreditCardApplicationService;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.entity.CreditCardApplication.ApplicationStatus;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.controller.CreateEntityController.CreateApplicationDocumentController;
import org.jala.university.presentation.controller.CreateEntityController.ReviewDocumentController;

import javafx.scene.control.*;

import java.util.UUID;

public class SalaryValidationView {

    private Label messageLabel;
    private TextField salaryInputField;
    private boolean isCorrect;
    private Double correctSalary;
    private Double salary; 
    private UUID idDocument;

    private ApplicationDocumentService documentService;
    private CreateApplicationDocumentController documentController;
    private CreditCardApplicationService applicationService; 
    private CreditCardApplication application; 
    private Customer customer;

    private ReviewDocumentController reviewDocument;

    public SalaryValidationView(UUID idDocument, CreditCardApplication application) {
        documentService = ServiceFactoryCreditCard.documentServiceFactory();
        documentController = new CreateApplicationDocumentController();
        applicationService = ServiceFactoryCreditCard.creditCardApplicationService();
        this.customer = CustomerSession.getInstance().getCurrentCustomer();
        this.idDocument = idDocument;
        reviewDocument = new ReviewDocumentController(documentController);
        documentController.setReviewDocumentController(reviewDocument);
        this.application = application; 
    }

    public Double startValidate() {
        ApplicationMapper applicationMapper = new ApplicationMapper();
        messageLabel = new Label();
        salaryInputField = new TextField();
        salaryInputField.setPromptText("Enter the correct salary");

        reviewSalary();

        Double returnSalary;

        if (isCorrect) {
            returnSalary = salary;
            application.setStatus(ApplicationStatus.in_review);
            ApplicationDTO applicationDTO = applicationService.update(applicationMapper.mapTo(application));
        } else {
            returnSalary = correctSalary;
        }

        return returnSalary;
    }


    public void reviewSalary() {
        String result = reviewDocument.reviewDocument(idDocument); 

        boolean validateDocument = reviewDocument.validateDocumentContent(result, customer.getIdNumber()); 

        if (validateDocument) {
            salary = reviewDocument.verifySalary(result, customer.getIdNumber());

            showConfirmationAlert(salary); 
    
            if (!isCorrect) {
                requestCorrectSalary(); 
            } 
        } else {
            String messageError = "No match was found in the document that meets the identification number " + customer.getIdNumber();  
            showAlert("Error performing validation", messageError);
        }
    }

    private void showConfirmationAlert(Double salary) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Salary Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("The salary determined by our system is: $" + salary + "\nIs this correct?");

        ButtonType correctButton = new ButtonType("Correct");
        ButtonType incorrectButton = new ButtonType("Incorrect");

        alert.getButtonTypes().setAll(correctButton, incorrectButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == correctButton) {
                isCorrect = true;
            } else if (response == incorrectButton) {
                isCorrect = false;
                requestCorrectSalary();
            }
        });
    }

    private void requestCorrectSalary() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Input Correct Salary");
        alert.setHeaderText("Please enter the correct salary:");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(salaryInputField);
        salaryInputField.clear();

        alert.showAndWait().ifPresent(response -> {
            String inputSalary = salaryInputField.getText();
            try {
                correctSalary = Double.parseDouble(inputSalary);
            } catch (NumberFormatException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Please enter a valid salary.");
                errorAlert.showAndWait();
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}