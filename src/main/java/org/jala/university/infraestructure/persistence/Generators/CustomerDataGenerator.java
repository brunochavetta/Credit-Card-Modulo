package org.jala.university.infraestructure.persistence.Generators;

import org.jala.university.domain.entity.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerDataGenerator {

    private static final String[] FIRST_NAMES = {"Bruno", "Agustín", "Douglas", "Noelia", "Marcelo", "David"};
    private static final String[] LAST_NAMES = {"Chavetta", "De Luca", "Mendez", "Santos", "Choque", "Vida"};
    private static final String[] EMAIL_DOMAINS = {"example.com", "mail.com", "test.com"};

    private static final Random random = new Random();

    public static Customer generateCustomer() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@" + EMAIL_DOMAINS[random.nextInt(EMAIL_DOMAINS.length)];
        String phoneNumber = "+54-261-" + (random.nextInt(9000) + 1000);
        String fullName = firstName + " " + lastName; 

        return new Customer(fullName, email, phoneNumber);
    }

    public static List<Customer> generateCustomerList(int count) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            customers.add(generateCustomer());
        }
        return customers;
    }
}
