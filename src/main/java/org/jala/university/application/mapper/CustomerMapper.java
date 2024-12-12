package org.jala.university.application.mapper;

import org.jala.university.application.dto.CustomerDto;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Customer;

public class CustomerMapper implements Mapper<Customer, CustomerDto> {
    @Override
    public CustomerDto mapTo(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .idNumber(customer.getIdNumber())
                .address(customer.getAddress())
                .createdAt(customer.getCreatedAt())
                .salary(customer.getSalary())
                .account(customer.getAccount())
                .build();
    }

    @Override
    public Customer mapFrom(CustomerDto customerDto) {
        return Customer.builder()
                .id(customerDto.getId())
                .fullName(customerDto.getFullName())
                .email(customerDto.getEmail())
                .phone(customerDto.getPhone())
                .idNumber(customerDto.getIdNumber())
                .address(customerDto.getAddress())
                .createdAt(customerDto.getCreatedAt())
                .salary(customerDto.getSalary())
                .account(customerDto.getAccount())
                .build();
    }
}
