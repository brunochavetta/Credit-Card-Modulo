package org.jala.university.application.serviceImpl;

import org.jala.university.application.dto.ApplicationDTO;
import org.jala.university.application.mapper.ApplicationMapper;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardApplicationRepository;
import org.jala.university.application.service.CreditCardApplicationService;
import org.jala.university.infraestructure.session.CustomerSession;

import java.util.List;
import java.util.UUID;

public class CreditCardApplicationServiceImpl implements CreditCardApplicationService {
    private final CreditCardApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;

    public CreditCardApplicationServiceImpl(CreditCardApplicationRepository applicationRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public ApplicationDTO save(ApplicationDTO applicationDto) {
        try {
            CreditCardApplication application = applicationMapper.mapFrom(applicationDto);
            CreditCardApplication saved = applicationRepository.save(application);
            return applicationMapper.mapTo(saved);
        } catch (Exception e) {
            throw new RuntimeException("Error saving credit card application: " + e.getMessage());
        }
    }

    public ApplicationDTO update(ApplicationDTO applicationDto) {
        try {
            CreditCardApplication application = applicationMapper.mapFrom(applicationDto);
            CreditCardApplication updated = applicationRepository.save(application);
            return applicationMapper.mapTo(updated);
        } catch (Exception e) {
            throw new RuntimeException("Error updating credit card application: " + e.getMessage());
        }
    }

    public void deleteById(UUID id) {
        try {
            applicationRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card application: " + e.getMessage());
        }
    }

    @Override
    public void delete(ApplicationDTO applicationDTO) {
        try {
            CreditCardApplication application = applicationMapper.mapFrom(applicationDTO);
            applicationRepository.delete(application);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card application: " + e.getMessage());
        }
    }

    public ApplicationDTO findById(UUID id) {
        try {
            CreditCardApplication application = applicationRepository.findById(id);
            return applicationMapper.mapTo(application);
        } catch (Exception e) {
            throw new RuntimeException("Error finding credit card application: " + e.getMessage());
        }
    }

    public List<ApplicationDTO> findAll() {
        try {
            List<CreditCardApplication> applications = applicationRepository.findAll();
            return applications.stream().map(applicationMapper::mapTo).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit card applications: " + e.getMessage());
        }
    }

    @Override
    public List<ApplicationDTO> listByUser() {
        try {
            Customer customer = CustomerSession.getInstance().getCurrentCustomer();
            List<CreditCardApplication> applications =  applicationRepository.listByUser(customer);
            return applications.stream().map(applicationMapper::mapTo).toList();
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit card applications by user: " + e.getMessage());
        }
    }

    
}
