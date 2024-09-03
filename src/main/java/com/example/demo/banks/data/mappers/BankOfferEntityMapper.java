package com.example.demo.banks.data.mappers;

import com.example.demo.banks.data.dtos.OfferDTO;
import com.example.demo.banks.data.entities.BankOfferEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankOfferEntityMapper {

    OfferDTO toDTO(BankOfferEntity offer);
}
