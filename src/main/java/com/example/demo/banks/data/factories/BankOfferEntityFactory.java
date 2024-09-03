package com.example.demo.banks.data.factories;

import com.example.demo.banks.data.entities.BankEntity;
import com.example.demo.banks.data.entities.BankOfferEntity;
import org.springframework.stereotype.Component;

import java.time.Period;

@Component
public class BankOfferEntityFactory {

    public static BankOfferEntity buildBankOfferEntity(BankEntity bank, Integer discount, Period period) {
        return BankOfferEntity
                .builder()
                .bank(bank)
                .discount(discount)
                .period(period)
                .build();
    }
}
