package org.jala.university.application.mapper;

import org.jala.university.application.dto.ApplicationDTO;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.CreditCardApplication;

public class ApplicationMapper implements Mapper<CreditCardApplication, ApplicationDTO> {

    @Override
    public ApplicationDTO mapTo(CreditCardApplication creditCardApplication) {
        return ApplicationDTO.builder()
                .id(creditCardApplication.getId())
                .customer(creditCardApplication.getCustomer())
                .applicationDate(creditCardApplication.getApplicationDate())
                .status(creditCardApplication.getStatus())
                .companyName(creditCardApplication.getCompanyName())
                .companyId(creditCardApplication.getCompanyId())
                .build();
    }

    @Override
    public CreditCardApplication mapFrom(ApplicationDTO applicationDTO) {
        return CreditCardApplication.builder()
                .id(applicationDTO.getId())
                .customer(applicationDTO.getCustomer())
                .applicationDate(applicationDTO.getApplicationDate())
                .status(applicationDTO.getStatus())
                .companyName(applicationDTO.getCompanyName())
                .companyId(applicationDTO.getCompanyId())
                .build();
    }
}
