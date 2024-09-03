package com.example.demo.banks.data.mappers;

import com.example.demo.banks.data.dtos.BankDepositRequestDTO;
import com.example.demo.banks.data.entities.BankDepositEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankDepositEntityMapper {

    BankDepositEntity toEntity(BankDepositRequestDTO dto);
}
