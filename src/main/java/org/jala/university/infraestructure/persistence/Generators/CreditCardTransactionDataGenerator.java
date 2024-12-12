package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.CreditCardTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreditCardTransactionDataGenerator {

    private static final String[] DESCRIPTIONS = {
            "Buy at store A",
            "Buy at store B",
            "Buy at the XFarma pharmacy",
            "Buy online on website C",
            "Netflix monthly subscription",
            "PrimeVideo monthly subscription"
    };
    
    private static final Random random = new Random();

    public static CreditCardTransaction generateCreditCardTransaction() {
        String description = DESCRIPTIONS[random.nextInt(DESCRIPTIONS.length)];
        Double amount = 10 + (1000 - 10) * random.nextDouble(); 
        
        return new CreditCardTransaction(amount, description);
    }

    public static List<CreditCardTransaction> generateCreditCardTransactionList(int count) {
        List<CreditCardTransaction> transactions = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            transactions.add(generateCreditCardTransaction());
        }
        return transactions;
    }
}
