package org.jala.university.application.serviceImpl;

import org.jala.university.application.dto.CustomerDto;
import org.jala.university.application.mapper.CustomerMapper;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CustomerRepository;
import org.jala.university.application.service.CustomerService;

import java.util.List;
import java.util.UUID;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDto save(CustomerDto customerDto) {
        try {
            Customer customer = customerMapper.mapFrom(customerDto);
            Customer saved = customerRepository.save(customer);
            return customerMapper.mapTo(saved);
        } catch (Exception e) {
            throw new RuntimeException("Error saving customer: " + e.getMessage());
        }
    }

    @Override
    public CustomerDto update(CustomerDto customerDto) {
        try {
            Customer customer = customerMapper.mapFrom(customerDto);
            Customer updated = customerRepository.save(customer);
            return customerMapper.mapTo(updated);
        } catch (Exception e) {
            throw new RuntimeException("Error updating customer: " + e.getMessage());
        }
    }

    public void delete(UUID id) {
        try {
            customerRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting customer: " + e.getMessage());
        }
    }

    public CustomerDto findById(UUID id) {
        try {
            Customer customer = customerRepository.findById(id);
            return customerMapper.mapTo(customer);
        } catch (Exception e) {
            throw new RuntimeException("Error finding customer: " + e.getMessage());
        }
    }

    public List<CustomerDto> findAll() {
        try {
            List<Customer> customers = customerRepository.findAll();
            return customers.stream().map(customerMapper::mapTo).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all customers: " + e.getMessage());
        }
    }
}
