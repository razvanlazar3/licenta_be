package com.example.demo.crypto.data.mapper;

import com.example.demo.crypto.data.dtos.CryptocurrencyDTO;
import com.example.demo.crypto.data.dtos.CryptocurrencyDTO.CryptocurrencyDTOBuilder;
import com.example.demo.crypto.data.model.Cryptocurrency;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-01T00:06:50+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class CryptocurrencyMapperImpl implements CryptocurrencyMapper {

    @Override
    public CryptocurrencyDTO toDTO(Cryptocurrency cryptocurrency) {
        if ( cryptocurrency == null ) {
            return null;
        }

        CryptocurrencyDTOBuilder cryptocurrencyDTO = CryptocurrencyDTO.builder();

        cryptocurrencyDTO.name( cryptocurrency.getName() );
        cryptocurrencyDTO.symbol( cryptocurrency.getSymbol() );
        cryptocurrencyDTO.price( cryptocurrency.getPrice() );
        cryptocurrencyDTO.percentChange24h( cryptocurrency.getPercentChange24h() );
        cryptocurrencyDTO.marketCap( cryptocurrency.getMarketCap() );
        cryptocurrencyDTO.volume24h( cryptocurrency.getVolume24h() );
        cryptocurrencyDTO.rank( cryptocurrency.getRank() );
        cryptocurrencyDTO.circulatingSupply( cryptocurrency.getCirculatingSupply() );
        cryptocurrencyDTO.trendingStatus( cryptocurrency.getTrendingStatus() );

        return cryptocurrencyDTO.build();
    }
}
