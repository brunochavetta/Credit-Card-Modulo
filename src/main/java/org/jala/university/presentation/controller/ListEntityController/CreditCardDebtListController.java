package org.jala.university.presentation.controller.ListEntityController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.DebtDto;
import org.jala.university.application.mapper.DebtMapper;
import org.jala.university.application.service.CreditCardDebtService;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.presentation.controller.CreateEntityController.CreditCardDebtController;

import java.util.List;

public class CreditCardDebtListController {
    @FXML
    private VBox cardDebtContainer;

    private List<CreditCardDebt> debts;

    private final CreditCardDebtService debtService; 

    public CreditCardDebtListController() {
        this.debtService = ServiceFactoryCreditCard.debtServiceFactory(); 
    }

    @FXML
    public void initialize() {
        loadCreditCardDebts();
    }

    private void loadCreditCardDebts() {
        List<DebtDto> debtsDto = debtService.findByCustomer().stream()
                .filter(debt -> debt.getOutstandingAmount() > 0)
                .toList();
        debts = debtsDto.stream().map(new DebtMapper()::mapFrom).toList(); 
        if (!debts.isEmpty()) {
            for (CreditCardDebt debt : debts) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/jala/university/presentation/EntityView/DebtView.fxml"));
                    AnchorPane debtPane = loader.load();
                    CreditCardDebtController controller = loader.getController();
                    controller.setDebt(debt);
                    cardDebtContainer.getChildren().add(debtPane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
