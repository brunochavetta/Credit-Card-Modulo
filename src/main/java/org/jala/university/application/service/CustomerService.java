package org.jala.university.application.service;

import org.jala.university.application.dto.CustomerDto;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    CustomerDto save(CustomerDto customer);
    CustomerDto update(CustomerDto customer);
    void delete(UUID id);
    CustomerDto findById(UUID id);
    List<CustomerDto> findAll();
}

