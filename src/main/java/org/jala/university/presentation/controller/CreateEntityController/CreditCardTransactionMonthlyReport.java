package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.designPatterns.facade.TransactionMonthlyReportFacade;
import org.jala.university.application.dto.SendNotificationDto;
import org.jala.university.application.mapper.TransactionMapper;
import org.jala.university.application.service.CreditCardTransactionService;
import org.jala.university.application.serviceImpl.SendEmailService;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardPayment;
import org.jala.university.domain.entity.CreditCardTransaction;
import org.jala.university.domain.entity.Notification;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.controller.MainViewControllerCreditCard.MenuController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

public class CreditCardTransactionMonthlyReport {
    @FXML
    private TableView<CreditCardTransaction> transactionTable;
    @FXML
    private TableColumn<CreditCardTransaction, String> codeColumn;
    @FXML
    private TableColumn<CreditCardTransaction, String> dateColumn;
    @FXML
    private TableColumn<CreditCardTransaction, String> descriptionColumn;
    @FXML
    private TableColumn<CreditCardTransaction, Double> amountColumn;

    @FXML
    private TableView<CreditCardPayment> paymentTable;
    @FXML
    private TableColumn<CreditCardPayment, String> paymentDateColumn;
    @FXML
    private TableColumn<CreditCardPayment, Double> paymentAmountColumn;

    @FXML
    private Label totalAmountLabel;
    @FXML
    private Label remainingDebtLabel;

    @FXML
    private Button redirectToBack;

    private List<CreditCardTransaction> transactions;

    private final CreditCardTransactionService TransactionService;

    private static final String FILE_NAME_DELIMITER = "_";

    private final MenuController menu = new MenuController();

    private CreditCardDebt debt;

    private String filePath;

    public CreditCardTransactionMonthlyReport() {
        this.TransactionService = ServiceFactoryCreditCard.transactionServiceFactory(); 
    }

    public void setDebt(CreditCardDebt debt) {
        this.debt = debt; loadTransactions();
    }

    @FXML
    public void initialize() {
        redirectToBack.setOnMouseClicked(event -> menu.redirectToDebtsView(redirectToBack));
    }

    public void loadTransactions() {
        transactions = TransactionService.findTransactionsByDebt(debt.getId()).stream().map(new TransactionMapper()::mapFrom).toList();
        initializeTable();
        transactionTable.getItems().setAll(transactions);

        double totalAmount = transactions.stream().mapToDouble(CreditCardTransaction::getAmount).sum();
        totalAmountLabel.setText("Total Amount: $" + String.format("%.2f", totalAmount));

        if (!transactions.isEmpty()) {
            var debt = transactions.get(0).getDebt();
            paymentTable.getItems().setAll(debt.getPayments());

            double remainingDebt = debt.getOutstandingAmount();
            remainingDebtLabel.setText("Total Due: $" + String.format("%.2f", remainingDebt));
        }

        generateReportPdf();
    }

    @FXML
    private void onSendEmail() {
        try {
            Notification notification = new Notification();
            notification.setSubject("Credit Card Report " + debt.getMonth());
            notification.setMessage("Below is the report for the month " + debt.getMonth());
            notification.setCustomer(CustomerSession.getInstance().getCurrentCustomer());
            SendNotificationDto notificationDTO = new SendNotificationDto(notification);
            SendEmailService.sendNotificationViaEmail(notificationDTO, new File(filePath));
                    showAlert("Success", "The PDF has been sent successfully.");
        } catch (Exception e) {
            showAlert("Error", "Failed to send the email: " + e.getMessage());
        }
    }

    private void initializeTable() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        paymentAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    @FXML
    private void onDownloadPdf() {
        Stage stage = new Stage();
        try {
            if (filePath == null || filePath.isBlank()) {
                showAlert("Error", "No PDF report found. Please generate the report first.");
                return;
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF Report");
            fileChooser.setInitialFileName(new File(filePath).getName());
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                try {
                    Files.copy(Paths.get(filePath), selectedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    showAlert("Success", "PDF saved successfully at: " + selectedFile.getAbsolutePath());
                } catch (IOException e) {
                    showAlert("Error", "Could not save the file: " + e.getMessage());
                }
            } else {
                showAlert("Info", "Save operation was canceled.");
            }
            showAlert("Success", "The PDF has been downloaded successfully to: " + filePath);
        } catch (Exception e) {
            showAlert("Error", "Failed to download the PDF: " + e.getMessage());
        }
    }

    private void generateReportPdf() {
        if (transactions == null || transactions.isEmpty()) {
            showAlert("Error", "No transactions available to generate the report.");
            return;
        }
            TransactionMonthlyReportFacade reportFacade = new TransactionMonthlyReportFacade();
            String fileName = new StringBuilder("TransactionSummartReport")
                    .append(FILE_NAME_DELIMITER)
                    .append(LocalDate.now().getMonthValue())
                    .append(FILE_NAME_DELIMITER)
                    .append(LocalDate.now().getDayOfMonth())
                    .append(FILE_NAME_DELIMITER)
                    .append(LocalDate.now().getYear())
                    .append(".pdf")
                    .toString();

            filePath = new File(System.getProperty("java.io.tmpdir"), fileName).getAbsolutePath();
            try {
                reportFacade.generatePdfReport(transactions, filePath);
            } catch (FileNotFoundException e) {
                showAlert("Error" , "Could not create the PDF file.");
            } catch (IOException e) {
                showAlert("Error" , "There was a problem opening the PDF file.");
            }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
