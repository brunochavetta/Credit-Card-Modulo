package org.jala.university.application.mapper;

import org.jala.university.application.dto.CreditCardTypeDTO;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCardType;

public class CreditCardTypeMapper implements Mapper<CreditCardType, CreditCardTypeDTO>{

    @Override
    public CreditCardType mapFrom(CreditCardTypeDTO typeDto) {
        return CreditCardType.builder()
            .id(typeDto.getId())
            .typeName(typeDto.getTypeName())
            .description(typeDto.getDescription())
            .maxCreditAmount(typeDto.getMaxCreditAmount())
            .monthlyFee(typeDto.getMonthlyFee())
            .build(); 

    }

    @Override
    public CreditCardTypeDTO mapTo(CreditCardType type) {
        return CreditCardTypeDTO.builder()
            .id(type.getId())
            .typeName(type.getTypeName())
            .description(type.getDescription())
            .maxCreditAmount(type.getMaxCreditAmount())
            .monthlyFee(type.getMonthlyFee())
            .build(); 
    }
    
}
