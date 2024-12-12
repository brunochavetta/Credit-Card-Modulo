package org.jala.university.application.mapper;

import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCard;

public class CreditCardMapper implements Mapper<CreditCard, CreditCardDto>{

    @Override
    public CreditCard mapFrom(CreditCardDto creditCardDto) {
        return CreditCard.builder()
        .id(creditCardDto.getId())
        .cardNumber(creditCardDto.getCardNumber())
        .issueDate(creditCardDto.getIssueDate())
        .expirationDate(creditCardDto.getExpirationDate())
        .selectCreditLimit(creditCardDto.getSelectCreditLimit())
        .totalSpent(creditCardDto.getTotalSpent())
        .securityCode(creditCardDto.getSecurityCode())
        .creditCardApplication(creditCardDto.getCreditCardApplication())
        .status(creditCardDto.getStatus())
        .creditCardType(creditCardDto.getCreditCardType())
        .build(); 
    }

    @Override
    public CreditCardDto mapTo(CreditCard creditCard) {
        return CreditCardDto.builder()
        .id(creditCard.getId())
        .cardNumber(creditCard.getCardNumber())
        .issueDate(creditCard.getIssueDate())
        .expirationDate(creditCard.getExpirationDate())
        .selectCreditLimit(creditCard.getSelectCreditLimit())
        .totalSpent(creditCard.getTotalSpent())
        .securityCode(creditCard.getSecurityCode())
        .creditCardApplication(creditCard.getCreditCardApplication())
        .status(creditCard.getStatus())
        .creditCardType(creditCard.getCreditCardType())
        .build(); 
    }
    
}
