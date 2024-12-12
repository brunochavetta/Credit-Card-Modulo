package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.CreditCardType;

import java.util.List;
import java.util.UUID;

public interface CreditCardTypeRepository extends Repository<CreditCardType, UUID> {
    CreditCardType findLast(); 
}
