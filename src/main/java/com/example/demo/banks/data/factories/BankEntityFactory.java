package com.example.demo.banks.data.factories;

import com.example.demo.banks.data.dtos.BankDTO;
import com.example.demo.banks.data.entities.BankEntity;
import org.springframework.stereotype.Component;

@Component
public class BankEntityFactory {

    public static BankEntity buildBankEntity(String name, String location) {
        return BankEntity
                .builder()
                .name(name)
                .location(location)
                .build();
    }
}
