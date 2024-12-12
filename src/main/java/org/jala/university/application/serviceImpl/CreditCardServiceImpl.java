package org.jala.university.application.serviceImpl;

import org.jala.university.domain.entity.CreditCard;
import org.jala.university.domain.entity.CreditCardType;
import org.jala.university.domain.entity.Customer;
import org.jala.university.domain.repository.CreditCardRepository;

import java.util.Random;

import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.dto.CreditCardTypeDTO;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.infraestructure.session.CustomerSession;

import java.util.List;

public class CreditCardServiceImpl implements CreditCardService {
    private final CreditCardRepository creditCardRepository; 
    private final CreditCardMapper creditCardMapper; 
    private Random random;
    private Set<Integer> existingSecurityCodes = new HashSet<>();
    private Set<String> existingCardNumber = new HashSet<>();

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper) {
        this.creditCardRepository = creditCardRepository; 
        this.creditCardMapper = creditCardMapper; 
    }

    @Override
    public CreditCardDto save(CreditCardDto creditCardDto) {
        try {
            CreditCard creditCard = creditCardRepository.save(creditCardMapper.mapFrom(creditCardDto));
            return creditCardMapper.mapTo(creditCard); 
        } catch (Exception e) {
            throw new RuntimeException("Error saving credit card: " + e.getMessage());
        }
    }

    @Override
    public CreditCardDto update(CreditCardDto creditCardDto) {
        try {
            CreditCard creditCard = creditCardRepository.save(creditCardMapper.mapFrom(creditCardDto));
            return creditCardMapper.mapTo(creditCard); 
        } catch (Exception e) {
            throw new RuntimeException("Error updating credit card: " + e.getMessage());
        }
    }

    @Override
    public boolean isUniqueCardNumber(String number) {
        return !existingCardNumber.contains(number);
    }

    @Override
    public String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber;

        int maxAttempts = 1000;
        int attempts = 0;

        do {
            if (attempts >= maxAttempts) {
                throw new RuntimeException("A unique number could not be generated after several attempts.");
            }

            cardNumber = new StringBuilder();
            cardNumber.append("5135 ")
                    .append(String.format("%04d", random.nextInt(10000))).append(" ")
                    .append(String.format("%04d", random.nextInt(10000))).append(" ")
                    .append(String.format("%04d", random.nextInt(10000)));

            attempts++;
        } while (!isUniqueCardNumber(cardNumber.toString()));

        existingCardNumber.add(cardNumber.toString());
        return cardNumber.toString();
    }

    @Override
    public void deleteById(UUID id) {
        try {
            creditCardRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card: " + e.getMessage());
        }
    }

    @Override
    public void delete(CreditCardDto creditCardDto) {
        try {
            creditCardRepository.delete(creditCardMapper.mapFrom(creditCardDto));
        } catch (Exception e) {
            throw new RuntimeException("Error deleting credit card: " + e.getMessage());
        }
    }

    @Override
    public CreditCardDto findById(UUID id) {
        try {
            return creditCardMapper.mapTo(creditCardRepository.findById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error finding credit card: " + e.getMessage());
        }
    }

    @Override
    public List<CreditCardDto> findAll() {
        try {
            List<CreditCard> creditCards = creditCardRepository.findAll();
            return creditCards.stream().map(creditCardMapper::mapTo).toList(); 
        } catch (Exception e) {
            throw new RuntimeException("Error finding all credit cards: " + e.getMessage());
        }
    }

    @Override
    public CreditCardTypeDTO returnCreditCardType(int option) {
        CreditCardTypeDTO type = null;
        switch (option) {
            case 1:
                type = new CreditCardTypeDTO(UUID.randomUUID(), "Platinum (Business)", "Platinum credit card for companies", 100000.0,
                        40.0);
                break;
            case 2:
                type = new CreditCardTypeDTO(UUID.randomUUID(), "Basic", "Basic credit card", 5000.0, 10.0);
                break;
            case 3:
                type = new CreditCardTypeDTO(UUID.randomUUID(), "Gold", "Gold credit card", 10000.0, 20.0);
                break;
            case 4:
                type = new CreditCardTypeDTO(UUID.randomUUID(), "Black", "Black credit card", 15000.0, 30.0);
                break;
            case 5:
                type = new CreditCardTypeDTO(UUID.randomUUID(), "Platinum (Personal)", "Platinum credit card for individuals", 20000.0,
                        40.0);
                break;
            default:
                throw new IllegalArgumentException("The chosen type does not exist or is incorrect.");
        }

        return type;
    }

    @Override
    public CreditCardDto updateTotalSpend (CreditCardDto creditCardDto, double amount) {
        try {
            double actualTotalSpend = creditCardDto.getTotalSpent();
            creditCardDto.setTotalSpent(actualTotalSpend - amount);
            CreditCard creditCard = creditCardRepository.save(creditCardMapper.mapFrom(creditCardDto));
            return creditCardMapper.mapTo(creditCard); 
        } catch (Exception e) {
            throw new RuntimeException("Error: Could not update total spent " + e.getMessage());
        }
    }

    @Override
    public boolean validateCreditLimit(Double selectedCreditLimit, CreditCardType creditCardType) {
        return selectedCreditLimit <= creditCardType.getMaxCreditAmount();
    }

    @Override
    public boolean isUniqueNumber(int number) {
        return !existingSecurityCodes.contains(number);
    }

    @Override
    public int generateSecurityCode() {
        int number;
        do {
            random = new Random();
            number = 100 + random.nextInt(900);
        } while (!isUniqueNumber(number));

        existingSecurityCodes.add(number);
        return number;
    }

    @Override
    public List<CreditCardDto> findByUser() {
        Customer customer = CustomerSession.getInstance().getCurrentCustomer();
        List<CreditCard> creditCards = creditCardRepository.listByUser(customer); 
        return creditCards.stream().map(creditCardMapper::mapTo).toList();  
    }

}
