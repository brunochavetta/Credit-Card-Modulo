package org.jala.university.application.service;

import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.CreditCardTypeDTO;
import org.jala.university.domain.entity.CreditCardType;

import java.util.List;
import java.util.UUID;

public interface CreditCardService {
    CreditCardDto save(CreditCardDto creditCard);
    CreditCardDto update(CreditCardDto creditCard);
    void deleteById(UUID id);
    void delete(CreditCardDto creditCardDto); 
    CreditCardDto findById(UUID id);
    List<CreditCardDto> findAll();
    String generateRandomCardNumber(); 
    CreditCardTypeDTO returnCreditCardType(int option); 
    boolean validateCreditLimit(Double selectedCreditLimit, CreditCardType creditCardType); 
    int generateSecurityCode(); 
    boolean isUniqueNumber(int number); 
    boolean isUniqueCardNumber(String number); 
    List<CreditCardDto> findByUser();
    CreditCardDto updateTotalSpend (CreditCardDto creditCard, double amount);
}