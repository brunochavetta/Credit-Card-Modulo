package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CreditCardApplicationRepository extends Repository<CreditCardApplication, UUID> {
    CreditCardApplication findLast(); 
    List<CreditCardApplication> listByUser(Customer customer); 
}
