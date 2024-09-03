package com.example.demo.crypto.data.mapper;

import com.example.demo.crypto.data.dtos.CryptocurrencyDTO;
import com.example.demo.crypto.data.model.Cryptocurrency;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CryptocurrencyMapper {
    CryptocurrencyDTO toDTO(Cryptocurrency cryptocurrency);
}
