package org.jala.university.application.serviceImpl;

import org.jala.university.application.service.NotificationService;
import org.jala.university.application.service.UserNotificationPreferencesService;
import org.jala.university.domain.entity.*;
import org.jala.university.domain.repository.CreditCardDebtRepository;

import org.jala.university.domain.repository.CustomerRepository;
import org.jala.university.domain.repository.NotificationRepository;
import org.jala.university.domain.repository.UserNotificationPreferencesRepository;
import org.jala.university.infraestructure.persistence.RepositoryImpl.CustomerRepositoryImpl;
import org.jala.university.infraestructure.persistence.RepositoryImpl.NotificationRepositoryImpl;
import org.jala.university.infraestructure.persistence.RepositoryImpl.UserNotificationPreferencesRepositoryImpl;
import org.jala.university.infraestructure.session.CustomerSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CreditCardPaymentServiceImplTest {
 /*   @Mock
    private CreditCardDebtRepository debtRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserNotificationPreferencesService preferencesService;

    @Mock
    private Customer mockCustomer;

    private static MockedStatic<CustomerSession> mockedSession;

    @Mock
    private CreditCardDebt mockDebt;

    @Mock
    private CreditCard mockCreditCard;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CreditCardApplication mockApplication;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserNotificationPreferencesRepository preferencesRepository;

    @InjectMocks
    private CreditCardPaymentServiceImpl paymentService;

    private CreditCardPayment mockPayment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        preferencesRepository = mock(UserNotificationPreferencesRepositoryImpl.class);
        notificationRepository = mock(NotificationRepositoryImpl.class);

        mockCustomer = new Customer();
        UUID customerId = UUID.fromString("ddbc2ee1-a0be-11ef-89ae-00410e4723a3");
        mockCustomer.setId(customerId);
        mockCustomer.setEmail("customer@example.com");

        customerRepository = mock(CustomerRepositoryImpl.class);
        when(customerRepository.findById(customerId)).thenReturn(mockCustomer);

        mockedSession = mockStatic(CustomerSession.class);
        mockedSession.when(CustomerSession::getInstance).thenReturn(mock(CustomerSession.class));

        CustomerSession mockSession = CustomerSession.getInstance();
        when(mockSession.getCurrentCustomer()).thenReturn(mockCustomer);  

        preferencesService = mock(UserNotificationPreferencesServiceImpl.class);
        UserNotificationPreferences mockPreferences = mock(UserNotificationPreferences.class);
        when(preferencesService.findPreferencesByUser()).thenReturn(mockPreferences);
        when(mockPreferences.isNotifications()).thenReturn(true);
        when(mockPreferences.isTransactionApp()).thenReturn(true);
        when(mockPreferences.isTransactionMail()).thenReturn(false);
        when(mockPreferences.isPaymentApp()).thenReturn(true);
        when(mockPreferences.isPaymentMail()).thenReturn(true);

        when(mockDebt.getOutstandingAmount()).thenReturn(400.0);
        when(mockDebt.getId()).thenReturn(1);
        when(mockDebt.getCreditCard()).thenReturn(mockCreditCard);
        when(mockCreditCard.getCreditCardApplication()).thenReturn(mockApplication);
        when(mockApplication.getCustomer()).thenReturn(mockCustomer);

        List<CreditCardDebt> debts = new ArrayList<>();
        debts.add(mockDebt);

        when(debtRepository.findByCustomer(mockCustomer)).thenReturn(debts);
        doNothing().when(debtRepository).save(any());

        mockPayment = new CreditCardPayment();
        mockPayment.setAmount(200.0);
        mockPayment.setCreditCardDebt(mockDebt);
        mockPayment.setPaymentDate(LocalDateTime.of(2024, 11, 10, 10, 30));

        notificationService = mock(NotificationServiceImpl.class);
        doNothing().when(notificationService).sendNotification(any());
    }


    @AfterEach
    void tearDown() {
        Mockito.clearAllCaches();
    }

    @Test
   public void save_SuccessfulPayment_SendsNotification() {
        doAnswer(invocation -> {
            return null;
        }).when(notificationService).sendNotification(any());

        try {
            paymentService.save(mockPayment);

            ArgumentCaptor<NotificationDTO> notificationCaptor = ArgumentCaptor.forClass(NotificationDTO.class);
            verify(notificationService).sendNotification(notificationCaptor.capture());

            NotificationDTO notificationDTO = notificationCaptor.getValue();
            assertEquals("Successful payment", notificationDTO.getNotification().getSubject());
            assertTrue(notificationDTO.getNotification().getMessage().contains("was successfully made"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error durante la ejecución del test: " + e.getMessage());
        }
    }

    @Test
    public void save_PaymentGreaterThanDebt_ShouldNotProcessPayment() {
        doAnswer(invocation -> {
            return null;
        }).when(notificationService).sendNotification(any());

        mockPayment.setAmount(500.0);

        try {
            paymentService.save(mockPayment);

            ArgumentCaptor<NotificationDTO> notificationCaptor = ArgumentCaptor.forClass(NotificationDTO.class);
            verify(notificationService).sendNotification(notificationCaptor.capture());

            NotificationDTO notificationDTO = notificationCaptor.getValue();
            assertEquals("Payment error: The payment is greater than the amount to be paid", notificationDTO.getNotification().getSubject());
            assertTrue(notificationDTO.getNotification().getMessage().contains("greater than the debt"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error durante la ejecución del test: " + e.getMessage());
        }
    }*/
}
