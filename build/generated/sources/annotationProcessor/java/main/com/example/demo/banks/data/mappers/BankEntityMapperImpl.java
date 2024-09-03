package com.example.demo.banks.data.mappers;

import com.example.demo.banks.data.dtos.BankDTO;
import com.example.demo.banks.data.dtos.BankDTO.BankDTOBuilder;
import com.example.demo.banks.data.dtos.OfferDTO;
import com.example.demo.banks.data.entities.BankEntity;
import com.example.demo.banks.data.entities.BankOfferEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-28T21:56:59+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class BankEntityMapperImpl implements BankEntityMapper {

    @Autowired
    private BankOfferEntityMapper bankOfferEntityMapper;

    @Override
    public BankDTO toDTO(BankEntity bank) {
        if ( bank == null ) {
            return null;
        }

        BankDTOBuilder bankDTO = BankDTO.builder();

        bankDTO.id( bank.getId() );
        bankDTO.name( bank.getName() );
        bankDTO.location( bank.getLocation() );

        bankDTO.offers( mapOffers(bank.getBankOffers()) );

        return bankDTO.build();
    }

    @Override
    public List<OfferDTO> mapOffers(List<BankOfferEntity> bankOffers) {
        if ( bankOffers == null ) {
            return null;
        }

        List<OfferDTO> list = new ArrayList<OfferDTO>( bankOffers.size() );
        for ( BankOfferEntity bankOfferEntity : bankOffers ) {
            list.add( bankOfferEntityMapper.toDTO( bankOfferEntity ) );
        }

        return list;
    }
}
