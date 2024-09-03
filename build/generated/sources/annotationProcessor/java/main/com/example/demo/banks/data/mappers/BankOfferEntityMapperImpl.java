package com.example.demo.banks.data.mappers;

import com.example.demo.banks.data.dtos.OfferDTO;
import com.example.demo.banks.data.dtos.OfferDTO.OfferDTOBuilder;
import com.example.demo.banks.data.entities.BankOfferEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-28T21:56:59+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class BankOfferEntityMapperImpl implements BankOfferEntityMapper {

    @Override
    public OfferDTO toDTO(BankOfferEntity offer) {
        if ( offer == null ) {
            return null;
        }

        OfferDTOBuilder offerDTO = OfferDTO.builder();

        offerDTO.id( offer.getId() );
        if ( offer.getPeriod() != null ) {
            offerDTO.period( offer.getPeriod().toString() );
        }
        offerDTO.discount( offer.getDiscount() );

        return offerDTO.build();
    }
}
