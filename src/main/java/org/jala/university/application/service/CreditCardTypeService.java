package org.jala.university.application.service;

import org.jala.university.application.dto.CreditCardTypeDTO;

import java.util.List;
import java.util.UUID;

public interface CreditCardTypeService {
    CreditCardTypeDTO save(CreditCardTypeDTO typeDto);
    CreditCardTypeDTO update(CreditCardTypeDTO typeDto);
    void delete(CreditCardTypeDTO typeDto);
    void deleteById(UUID id); 
    CreditCardTypeDTO findById(UUID id);
    List<CreditCardTypeDTO> findAll();
    CreditCardTypeDTO findLast(); 
}

