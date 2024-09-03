package com.example.demo.crypto.data.mappers;

import com.example.demo.crypto.data.dtos.CryptocurrencyDTO;
import com.example.demo.crypto.data.model.Crypto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CryptoMapper {

    CryptocurrencyDTO toDto(Crypto crypto);
}
