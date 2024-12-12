package org.jala.university.presentation.controller.CreateEntityController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.designPatterns.facade.TransactionReportFacade;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.domain.entity.*;
import org.jala.university.presentation.controller.MainViewControllerCreditCard.MenuController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class TransactionReportController {
    @FXML
    private ComboBox<String> creditCardComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private Button generatedReport;

    @FXML
    private Button homeButton;

    @FXML
    private Button requestCardButton;

    @FXML
    private Button viewCreditCardByUser;

    @FXML
    private Button viewApplicationsButton;

    private final MenuController menu = new MenuController();
    private static final String FILE_NAME_DELIMITER = "_";
    private final CreditCardService creditCardService;
    private List<CreditCard> creditCards;
    private File tempPdfFile;

    public TransactionReportController() {
        this.creditCardService = ServiceFactoryCreditCard.creditCardServiceFactory();
    }

    @FXML
    public void initialize() {
        creditCards = creditCardService.findByUser().stream().map(new CreditCardMapper()::mapFrom).toList();
        setupCreditCardComboBox();
        setupButtonHandlers();
    }

    private void setupCreditCardComboBox() {
        creditCardComboBox.getItems().clear();
        creditCards.forEach(card -> {
            String cardNumber = card.getCardNumber();
            String maskedCardNumber = "XXXX XXXX XXXX " + cardNumber.substring(cardNumber.length() - 4);
            creditCardComboBox.getItems().add(
                    maskedCardNumber + " (type: " + card.getCreditCardType().getTypeName() + ")"
            );
        });
        creditCardComboBox.getSelectionModel().selectFirst();
    }

    private void setupButtonHandlers() {
        generatedReport.setOnMouseClicked(event -> generateReport());
        homeButton.setOnMouseClicked(event -> navigateToHome());
        requestCardButton.setOnMouseClicked(event -> menu.redirectToFormView(requestCardButton));
        viewCreditCardByUser.setOnMouseClicked(event -> menu.redirectToCreditCardView(viewCreditCardByUser));
        viewApplicationsButton.setOnMouseClicked(event -> menu.redirectToApplicationView(viewApplicationsButton));
    }

    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/MainView/MainView.fxml"));
            Parent mainView = loader.load();
            Scene currentScene = homeButton.getScene();
            currentScene.setRoot(mainView);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error: Could not navigate to home view", Alert.AlertType.ERROR);
        }
    }

    private void generateReport() {
        TransactionReportFacade reportFacade = new TransactionReportFacade();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (!validateInputs(startDate, endDate)) {
            return;
        }

        int selectedIndex = creditCardComboBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= creditCards.size()) {
            showAlert("Error: Please select a valid credit card", Alert.AlertType.ERROR);
            return;
        }

        CreditCard selectedCard = creditCards.get(selectedIndex);
        List<CreditCardTransaction> transactions = reportFacade.fetchTransactions(selectedCard.getId(), startDate, endDate);

        if (transactions.isEmpty()) {
            showAlert("The selected card does not have recorded transactions", Alert.AlertType.INFORMATION);
            return;
        }

        showPreviewWindow(transactions);
    }

    private boolean validateInputs(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            showAlert("Error: Please select both start and end dates", Alert.AlertType.ERROR);
            return false;
        }
        if (endDate.isBefore(startDate)) {
            showAlert("Error: End date cannot be before start date", Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void showPreviewWindow(List<CreditCardTransaction> transactions) {
        try {
            tempPdfFile = createTempPdfFile();
            TransactionReportFacade reportFacade = new TransactionReportFacade();
            reportFacade.generatePdfReport(transactions, tempPdfFile.getAbsolutePath());

            Stage previewStage = new Stage();
            previewStage.initModality(Modality.APPLICATION_MODAL);
            previewStage.setTitle("Transaction Report Preview");

            ScrollPane scrollPane = new ScrollPane();
            VBox pdfPagesContainer = new VBox(10);
            pdfPagesContainer.setPadding(new Insets(10));
            pdfPagesContainer.setAlignment(Pos.CENTER);

            PDDocument document = PDDocument.load(tempPdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 100);
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", output);
                ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
                Image fxImage = new Image(input);

                ImageView imageView = new ImageView(fxImage);
                imageView.setFitWidth(595);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                pdfPagesContainer.getChildren().add(imageView);

                output.close();
                input.close();
            }

            document.close();

            scrollPane.setContent(pdfPagesContainer);
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefViewportWidth(615);
            scrollPane.setPrefViewportHeight(800);

            Button downloadButton = new Button("Download PDF");
            Button closeButton = new Button("Close");

            downloadButton.setOnAction(e -> downloadPdf());
            closeButton.setOnAction(e -> {
                previewStage.close();
                pdfPagesContainer.getChildren().clear();
            });

            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER);
            buttonBox.getChildren().addAll(downloadButton, closeButton);
            buttonBox.setPadding(new Insets(10));

            VBox mainLayout = new VBox(10);
            mainLayout.getChildren().addAll(scrollPane, buttonBox);
            VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

            Scene scene = new Scene(mainLayout);
            previewStage.setScene(scene);
            previewStage.setWidth(650);
            previewStage.setHeight(900);
            previewStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error: Could not create preview: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private File createTempPdfFile() throws IOException {
        String fileName = "TransactionReport" + FILE_NAME_DELIMITER +
                LocalDate.now().getMonthValue() + FILE_NAME_DELIMITER +
                LocalDate.now().getDayOfMonth() + FILE_NAME_DELIMITER +
                LocalDate.now().getYear() + "_" +
                new Random().nextInt(100) + ".pdf";
        File tempFile = File.createTempFile("preview_", ".pdf");
        tempFile.deleteOnExit();
        return tempFile;
    }

    private void downloadPdf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Report");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );
        fileChooser.setInitialFileName("transaction_report.pdf");

        File destinationFile = fileChooser.showSaveDialog(null);
        if (destinationFile != null) {
            try {
                Files.copy(tempPdfFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                showAlert("Report saved successfully!", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Error: Could not save file: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}