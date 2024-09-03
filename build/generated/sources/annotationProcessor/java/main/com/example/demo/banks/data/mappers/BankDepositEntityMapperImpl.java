package com.example.demo.banks.data.mappers;

import com.example.demo.banks.data.dtos.BankDepositRequestDTO;
import com.example.demo.banks.data.entities.BankDepositEntity;
import com.example.demo.banks.data.entities.BankDepositEntity.BankDepositEntityBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T00:06:50+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class BankDepositEntityMapperImpl implements BankDepositEntityMapper {

    @Override
    public BankDepositEntity toEntity(BankDepositRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        BankDepositEntityBuilder bankDepositEntity = BankDepositEntity.builder();

        bankDepositEntity.offerId( dto.getOfferId() );
        bankDepositEntity.amount( dto.getAmount() );

        return bankDepositEntity.build();
    }
}
