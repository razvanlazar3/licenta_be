package com.example.demo.banks.jobs;

import com.example.demo.banks.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddBanksAndOffersJob {

    private final BankService bankService;

    @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 1000)
    public void createAdmin() {
        log.info("Banks addition started");
        bankService.addPredefinedBanksWithOffers();
        log.info("Banks addition finished successfully");
    }
}