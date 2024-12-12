package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.CreditCardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreditCardTypeDataGenerator {

    private static final String[] CARD_TYPES = {"Gold", "Black", "Platinum", "Classic", "Diamond"};
    private static final Random random = new Random();

    public static CreditCardType generateCreditCardType() {
        String name = CARD_TYPES[random.nextInt(CARD_TYPES.length)];
        CreditCardType creditCardType = new CreditCardType(); 
        creditCardType.setTypeName(name);
        return creditCardType;
    }

    public static List<CreditCardType> generateCreditCardTypeList(int count) {
        List<CreditCardType> creditCardTypes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            creditCardTypes.add(generateCreditCardType());
        }
        return creditCardTypes;
    }
}
