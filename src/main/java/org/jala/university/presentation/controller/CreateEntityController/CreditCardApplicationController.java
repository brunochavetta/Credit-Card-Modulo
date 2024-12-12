package org.jala.university.presentation.controller.CreateEntityController;

import java.util.List;
import java.util.UUID;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.ApplicationDTO;
import org.jala.university.application.dto.CustomerDto;
import org.jala.university.application.mapper.ApplicationMapper;
import org.jala.university.application.mapper.CustomerMapper;
import org.jala.university.application.service.CreditCardApplicationService;
import org.jala.university.application.service.CustomerService;
import org.jala.university.domain.entity.CreditCardApplication;
import org.jala.university.domain.entity.Customer;
import org.jala.university.infraestructure.session.CustomerSession;
import org.jala.university.presentation.EntityView.SalaryValidationView;

public class CreditCardApplicationController {
    private final CreditCardApplicationService creditCardApplicationService;
    private final CustomerService customerService;
    private final CustomerMapper customerMapper = new CustomerMapper();

    public CreditCardApplicationController() {
        this.customerService = ServiceFactoryCreditCard.customerServiceFactory();
        this.creditCardApplicationService = ServiceFactoryCreditCard.creditCardApplicationService();
    }

    public List<CreditCardApplication> listApplications() {
        ApplicationMapper applicationMapper = new ApplicationMapper();
        List<ApplicationDTO> applications = creditCardApplicationService.listByUser();
        return applications.stream().map(applicationMapper::mapFrom).toList();
    }

    public void verifySalary(UUID idDocument, CreditCardApplication application) {
            SalaryValidationView salaryValidation = new SalaryValidationView(idDocument, application);
            Double salary = salaryValidation.startValidate();
    
            if (salary != null) {
                Customer customer = CustomerSession.getInstance().getCurrentCustomer();
                customer.setSalary(salary);
                CustomerDto customerDto = customerMapper.mapTo(customer);
                customerService.update(customerDto);

                CreditCardController creditCardController = new CreditCardController();

                creditCardController.setParameters(salary, false, application);

                creditCardController.modifyStatus();

            } else {
                System.out.println("Salary validation failed.");
            }
    }    

}
