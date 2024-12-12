package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreditCardDebtDataGenerator {

    private static final Random random = new Random();
    private static final String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};

    public static CreditCardDebt generateCreditCardDebt(CreditCard creditCard) {
        Double outstandingAmount = random.nextDouble() * 1000 + 100; 
        LocalDate dueDate = LocalDate.now().plusDays(random.nextInt(30)); 
        CreditCardDebt.DebtStatus status = CreditCardDebt.DebtStatus.pending;
        String month = months[random.nextInt(months.length)];
        return new CreditCardDebt(creditCard, outstandingAmount, month, dueDate, status);
    }

    public static List<CreditCardDebt> generateCreditCardDebtList(CreditCard creditCard, int count) {
        List<CreditCardDebt> debts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            debts.add(generateCreditCardDebt(creditCard));
        }
        return debts;
    }
}
