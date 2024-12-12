package org.jala.university.application.mapper;

import org.jala.university.application.dto.DebtDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCardDebt;

public class DebtMapper implements Mapper<CreditCardDebt, DebtDto>{

    @Override
    public CreditCardDebt mapFrom(DebtDto debtDto) {
        return CreditCardDebt.builder()
            .id(debtDto.getId())
            .month(debtDto.getMonth())
            .outstandingAmount(debtDto.getOutstandingAmount())
            .status(debtDto.getStatus())
            .build();
    }

    @Override
    public DebtDto mapTo(CreditCardDebt debt) {
        return DebtDto.builder()
            .id(debt.getId())
            .month(debt.getMonth())
            .outstandingAmount(debt.getOutstandingAmount())
            .status(debt.getStatus())
            .build();
    }
    
}
