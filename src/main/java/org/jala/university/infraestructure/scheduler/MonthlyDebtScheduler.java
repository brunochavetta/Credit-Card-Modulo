package org.jala.university.infraestructure.scheduler;

import org.jala.university.ServiceFactoryCreditCard;
import org.jala.university.application.dto.CreditCardDto;
import org.jala.university.application.mapper.CreditCardMapper;
import org.jala.university.application.service.CreditCardDebtService;
import org.jala.university.application.service.CreditCardService;
import org.jala.university.domain.entity.CreditCard;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MonthlyDebtScheduler {
    private final CreditCardDebtService creditCardDebtService;
    private final CreditCardService creditCardService;

    public MonthlyDebtScheduler() {
        this.creditCardDebtService = ServiceFactoryCreditCard.debtServiceFactory(); 
        this.creditCardService = ServiceFactoryCreditCard.creditCardServiceFactory(); 
    }

    public void scheduleMonthlyDebtGeneration() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            List<CreditCard> creditCards = getAllActiveCreditCards();
            creditCards.forEach(creditCardDebtService::generateMonthlyDebt);

            LocalDate now = LocalDate.now();
            LocalDate firstDayOfNextMonth = now.plusMonths(1).withDayOfMonth(1);
            long delayUntilNextMonth = ChronoUnit.DAYS.between(now, firstDayOfNextMonth);

            scheduler.schedule(this::scheduleMonthlyDebtGeneration, delayUntilNextMonth, TimeUnit.DAYS);
        };

        LocalDate now = LocalDate.now();
        LocalDate firstDayOfNextMonth = now.plusMonths(1).withDayOfMonth(1);
        long initialDelay = ChronoUnit.DAYS.between(now, firstDayOfNextMonth);
        scheduler.schedule(task, initialDelay, TimeUnit.DAYS);
    }

    private List<CreditCard> getAllActiveCreditCards() {
        List<CreditCardDto> cardsDto = creditCardService.findAll().stream()
                .filter(creditCard -> creditCard.getStatus() == CreditCard.CreditCardStatus.active)
                .collect(Collectors.toList());
        return cardsDto.stream().map(new CreditCardMapper()::mapFrom).toList(); 
    }
}

