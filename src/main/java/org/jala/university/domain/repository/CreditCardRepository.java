package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.Customer;

import java.util.List;
import java.util.UUID;

public interface CreditCardRepository  extends Repository<CreditCard, UUID> {
    List<CreditCard> listByUser(Customer customer); 
}
