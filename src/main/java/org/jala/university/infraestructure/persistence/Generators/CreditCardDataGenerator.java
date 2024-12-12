package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.CreditCardType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreditCardDataGenerator {

    private static final Random random = new Random();

    public static CreditCard generateCreditCard(CreditCardApplication application, CreditCardType type) {
        String cardNumber = generateRandomCardNumber();
        LocalDateTime issueDate = LocalDateTime.now();
        LocalDateTime expirationDate = issueDate.plusYears(3); 
        Double selectCreditLimit = random.nextDouble() * 5000 + 1000; 

        return new CreditCard(cardNumber, issueDate, expirationDate, selectCreditLimit, application, 
                CreditCard.CreditCardStatus.active, type, generateSecurityCode());
    }

    public static List<CreditCard> generateCreditCardList(CreditCardApplication application, CreditCardType type, int count) {
        List<CreditCard> creditCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            creditCards.add(generateCreditCard(application, type));
        }
        return creditCards;
    }

    private static String generateRandomCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                cardNumber.append("-"); 
            }
            for (int j = 0; j < 4; j++) {
                cardNumber.append(random.nextInt(10)); 
            }
        }
        return cardNumber.toString();
    }

    public static int generateSecurityCode() {
        int number; 
        Random random = new Random();
        number = 100 + random.nextInt(900);

        return number; 
    }
}
