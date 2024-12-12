package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.CreditCardPayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreditCardPaymentDataGenerator {

    private static final Random random = new Random();

    public static CreditCardPayment generateCreditCardPayment(CreditCardDebt creditCardDebt) {
        Double amount = random.nextDouble() * 1000;
        CreditCardPayment payment = new CreditCardPayment(amount, creditCardDebt);
        payment.setCreditCardDebt(creditCardDebt); 
        return payment;
    }

    public static List<CreditCardPayment> generateCreditCardPaymentList(CreditCardDebt creditCardDebt, int count) {
        List<CreditCardPayment> payments = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            payments.add(generateCreditCardPayment(creditCardDebt));
        }
        return payments;
    }
}
