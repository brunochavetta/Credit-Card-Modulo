package org.jala.university.application.serviceImpl;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardTransaction;
import org.jala.university.domain.repository.CreditCardDebtRepository;
import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.DebtDto;
import org.jala.university.application.dto.TransactionDto;
import org.jala.university.application.mapper.DebtMapper;
import org.jala.university.application.mapper.TransactionMapper;
import org.jala.university.application.service.CreditCardDebtService;
import org.jala.university.application.service.CreditCardTransactionService;
import org.jala.university.infraestructure.session.CustomerSession;

import java.util.List;
import java.util.UUID;
import java.time.LocalDate;
import java.time.YearMonth;

public class CreditCardDebtServiceImpl implements CreditCardDebtService {
    private final CreditCardDebtRepository debtRepository; 
    private final DebtMapper debtMapper; 
    private final CreditCardTransactionService transactionService; 

    public CreditCardDebtServiceImpl(CreditCardDebtRepository debtRepository, DebtMapper debtMapper) {
        this.debtRepository = debtRepository; 
        this.debtMapper = debtMapper; 
        this.transactionService = ServiceFactoryCreditCard.transactionServiceFactory(); 
    }

    @Override
    public DebtDto generateMonthlyDebt(CreditCard creditCard) {
        try {
            YearMonth lastMonth = YearMonth.now().minusMonths(1);
            LocalDate startDate = lastMonth.atDay(1);
            LocalDate endDate = lastMonth.atEndOfMonth();

            String month = lastMonth.getMonth().toString();

            List<TransactionDto> transactionsDto = transactionService
                    .findTransactionsByCardAndDateRange(creditCard.getId(), startDate, endDate);

            List<CreditCardTransaction> transactions = transactionsDto.stream().map(new TransactionMapper()::mapFrom).toList(); 

            if (transactions.isEmpty()) {
                return null;
            }

            double totalAmount = transactions.stream()
                    .mapToDouble(CreditCardTransaction::getAmount)
                    .sum();

            CreditCardDebt newDebt = new CreditCardDebt();
            newDebt.setCreditCard(creditCard);
            newDebt.setOutstandingAmount(totalAmount);
            newDebt.setMonth(month);
            newDebt.setDueDate(LocalDate.now().withDayOfMonth(15));
            newDebt.setStatus(CreditCardDebt.DebtStatus.pending);

            return debtMapper.mapTo(debtRepository.save(newDebt));
        } catch (Exception e) {
            throw new RuntimeException("Error saving credit card debt: " + e.getMessage());
        }
    }

    @Override
    public DebtDto update(DebtDto debtDto) {
        try {
            CreditCardDebt debt = debtRepository.save(debtMapper.mapFrom(debtDto)); 
            return debtMapper.mapTo(debt); 
        } catch (Exception e) {
            throw new RuntimeException("Error updating credit card debt: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            debtRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card debt: " + e.getMessage());
        }
    }

    @Override
    public DebtDto findById(UUID id) {
        try {
            return debtMapper.mapTo(debtRepository.findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error finding credit card debt: " + e.getMessage());
        }
    }

    @Override
    public List<DebtDto> findAll() {
        try {
            List<CreditCardDebt> debts = debtRepository.findAll();
            return debts.stream().map(debtMapper::mapTo).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit card debts: " + e.getMessage());
        }
    }

    @Override
    public List<DebtDto> findByCustomer() {
        try {
            List<CreditCardDebt> debts = debtRepository.findByCustomer(CustomerSession.getInstance().getCurrentCustomer());
            return debts.stream().map(debtMapper::mapTo).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit card debts by customer: " + e.getMessage());
        }
    }

    @Override
    public DebtDto IsExistsByCreditCardIdAndMonth(CreditCard creditCard, String month){

        CreditCardDebt debt = debtRepository.existsByCreditCardIdAndMonth(creditCard,month);

        return debtMapper.mapTo(debt);
    }
}
