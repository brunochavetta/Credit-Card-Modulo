package org.jala.university.infraestructure.session;

import org.jala.university.domain.entity.Customer;

public class CustomerSession {
    private static CustomerSession instance;
    private Customer currentCustomer;

    private CustomerSession() {
    }

    public static CustomerSession getInstance() {
        if (instance == null) {
            instance = new CustomerSession();
        }
        return instance;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }
}

