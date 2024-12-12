package org.jala.university.application.service;

import org.jala.university.application.dto.ApplicationDTO;
  
import java.util.List;
import java.util.UUID;

public interface CreditCardApplicationService {
    ApplicationDTO save(ApplicationDTO application);
    ApplicationDTO update(ApplicationDTO application);
    void deleteById(UUID id);
    void delete(ApplicationDTO applicationDTO);
    ApplicationDTO findById(UUID id);
    List<ApplicationDTO> findAll();
    List<ApplicationDTO> listByUser();
}

