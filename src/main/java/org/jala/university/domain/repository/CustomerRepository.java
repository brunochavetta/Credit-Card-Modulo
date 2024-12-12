package org.jala.university.domain.repository;

import org.jala.university.domain.entity.Customer;
import org.jala.university.commons.domain.Repository;

import java.util.UUID;

public interface CustomerRepository extends Repository<Customer, UUID> {
}
