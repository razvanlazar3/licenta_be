package com.example.demo.banks.data.mappers;

import com.example.demo.banks.data.dtos.BankDTO;
import com.example.demo.banks.data.dtos.OfferDTO;
import com.example.demo.banks.data.entities.BankEntity;
import com.example.demo.banks.data.entities.BankOfferEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BankOfferEntityMapper.class})
public interface BankEntityMapper {

    @Mapping(target = "offers", expression = "java(mapOffers(bank.getBankOffers()))")
    BankDTO toDTO(BankEntity bank);

    @Named("offerListToOfferDtoList")
    List<OfferDTO> mapOffers(List<BankOfferEntity> bankOffers);
}
