package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardDebt;
import org.jala.university.domain.entity.Customer;

import java.util.List;
import java.util.UUID; 

public interface CreditCardDebtRepository extends Repository<CreditCardDebt, UUID>{
    CreditCardDebt existsByCreditCardIdAndMonth(CreditCard creditCard, String month);
    List<CreditCardDebt> findByCustomer(Customer customer);
}
