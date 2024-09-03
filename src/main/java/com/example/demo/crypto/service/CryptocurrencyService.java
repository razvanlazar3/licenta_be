package com.example.demo.crypto.service;

import com.example.demo.crypto.data.dtos.CryptocurrencyDTO;
import com.example.demo.crypto.data.dtos.PersonalCryptoCoinRequestDTO;
import com.example.demo.crypto.data.dtos.PersonalCryptoResponseDTO;
import com.example.demo.crypto.data.mappers.CryptoMapper;
import com.example.demo.crypto.data.model.Crypto;
import com.example.demo.crypto.data.model.PersonalCrypto;
import com.example.demo.crypto.data.repos.CryptoRepo;
import com.example.demo.crypto.data.repos.PersonalCryptoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CryptocurrencyService {

    @Value("52131a46-4d1a-4a90-98ec-8d520f2bc2da")
    private String apiKey;

    private static final String CRYPTO_GENERAL_INFO_ENDPOINT = "https://pro-api.coinmarketcap.com/v1/partners" +
            "/flipside-crypto" + "/fcas/listings/latest";
    private static final String CRYPTO_EXTRA_INFO_ENDPOINT = "https://pro-api.coinmarketcap.com/v1/cryptocurrency" +
            "/listings/latest";

    private final CryptoMapper cryptoMapper;
    private final CryptoRepo cryptoRepo;
    private final PersonalCryptoRepo personalCryptoRepo;

    public Page<CryptocurrencyDTO> updateCryptos(Pageable pageable) {
        List<Map<String, Object>> cryptoCurrenciesGeneralInfo = (List<Map<String, Object>>)
                (getCryptoCoinDetails(CRYPTO_GENERAL_INFO_ENDPOINT).getBody()).get("data");

        List<Map<String, Object>> cryptoCurrenciesData = (List<Map<String, Object>>)
                getCryptoCoinDetails(CRYPTO_EXTRA_INFO_ENDPOINT).getBody().get("data");

        List<Map<String, Object>> cryptoCurrenciesExtra = cryptoCurrenciesData.stream()
                .map(cryptoData -> {
                    Map<String, Object> quote = (Map<String, Object>) cryptoData.get("quote");
                    return (Map<String, Object>) quote.get("USD");
                }).toList();

        List<Crypto> cryptoCurrencies = obtainCryptoEntities(cryptoCurrenciesGeneralInfo, cryptoCurrenciesExtra);
        cryptoRepo.saveAll(cryptoCurrencies);
        updatePersonalCryptos();

        var cryptocurrencyDTOs = cryptoCurrencies.stream().map(cryptoMapper::toDto).toList();
        int start = Math.min((int) pageable.getOffset(), cryptocurrencyDTOs.size());
        int end = Math.min((start + pageable.getPageSize()), cryptocurrencyDTOs.size());

        return new PageImpl<>(cryptocurrencyDTOs.subList(start, end), pageable, cryptocurrencyDTOs.size());
    }

    public List<CryptocurrencyDTO> updateCryptos() {
        List<Map<String, Object>> cryptoCurrenciesGeneralInfo = (List<Map<String, Object>>)
                (getCryptoCoinDetails(CRYPTO_GENERAL_INFO_ENDPOINT).getBody()).get("data");

        List<Map<String, Object>> cryptoCurrenciesData = (List<Map<String, Object>>)
                getCryptoCoinDetails(CRYPTO_EXTRA_INFO_ENDPOINT).getBody().get("data");

        List<Map<String, Object>> cryptoCurrenciesExtra = cryptoCurrenciesData.stream()
                .map(cryptoData -> {
                    Map<String, Object> quote = (Map<String, Object>) cryptoData.get("quote");
                    return (Map<String, Object>) quote.get("USD");
                }).toList();

        List<Crypto> cryptoCurrencies = obtainCryptoEntities(cryptoCurrenciesGeneralInfo, cryptoCurrenciesExtra);
        cryptoRepo.saveAll(cryptoCurrencies);
        updatePersonalCryptos();

        return cryptoCurrencies.stream().map(cryptoMapper::toDto).toList();
    }

    private static List<Crypto> obtainCryptoEntities(List<Map<String, Object>> cryptoCurrenciesGeneralInfo, List<Map<String, Object>> cryptoCurrenciesExtraInfo) {
        return cryptoCurrenciesGeneralInfo.stream()
                .map(generalInfo -> {
                    var generalInfoIndex = cryptoCurrenciesGeneralInfo.indexOf(generalInfo);
                    Map<String, Object> extraInfo = cryptoCurrenciesExtraInfo.stream().toList()
                            .get(generalInfoIndex);

                    return Crypto.builder()
                            .id((Integer) generalInfo.get("id"))
                            .name((String) generalInfo.get("name"))
                            .grade((String) generalInfo.get("grade"))
                            .symbol((String) generalInfo.get("symbol"))
                            .slug((String) generalInfo.get("slug"))
                            .score((Integer) generalInfo.get("score"))
                            .lastUpdated((String) generalInfo.get("last_updated"))
                            .price((Double) extraInfo.get("price"))
                            .volume_24h((Double) extraInfo.get("volume_24h"))
                            .volume_change_24h((Double) extraInfo.get("volume_change_24h"))
                            .percent_change_1h((Double) extraInfo.get("percent_change_1h"))
                            .percent_change_24h((Double) extraInfo.get("percent_change_24h"))
                            .percent_change_7d((Double) extraInfo.get("percent_change_7d"))
                            .build();
                })
                .toList();
    }

    private ResponseEntity<Map> getCryptoCoinDetails(String url) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_PRO_API_KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
    }

    public Long registerCryptoPurchase(PersonalCryptoCoinRequestDTO cryptoReq, Long userId) {
        var personalCryptoForCoin = personalCryptoRepo
                .findFirstByCoinIdAndUserId(cryptoReq.getCoinId(), userId);

        PersonalCrypto newPersonalCryptoForCoin;
        var totalReqSum = cryptoReq.getAmount() * cryptoReq.getPrice();
        if (personalCryptoForCoin.isPresent()) {
            newPersonalCryptoForCoin = personalCryptoForCoin.get();
            var oldAmount = newPersonalCryptoForCoin.getAmount();
            var oldInitialSum = newPersonalCryptoForCoin.getInitialSum();
            newPersonalCryptoForCoin.setAmount(oldAmount + cryptoReq.getAmount());
            newPersonalCryptoForCoin.setCurrentSum(newPersonalCryptoForCoin.getAmount() * cryptoReq.getPrice());
            newPersonalCryptoForCoin.setInitialSum(oldInitialSum + totalReqSum);
            newPersonalCryptoForCoin.setCoinId(cryptoReq.getCoinId());
        } else {
            newPersonalCryptoForCoin = PersonalCrypto
                    .builder()
                    .amount(cryptoReq.getAmount())
                    .coinId(cryptoReq.getCoinId())
                    .userId(userId)
                    .initialSum(totalReqSum)
                    .currentSum(totalReqSum)
                    .build();
        }
        return personalCryptoRepo.save(newPersonalCryptoForCoin).getId().longValue();
    }

    private void updatePersonalCryptos() {
        var updatedPersonalCryptos = personalCryptoRepo.findAll()
                .stream()
                .peek(personalCrypto -> {
                    var coinCurrentPrice = cryptoRepo.findById(personalCrypto.getCoinId());
                    if (coinCurrentPrice.isPresent()) {
                        var currentSum = personalCrypto.getAmount() * coinCurrentPrice.get().getPrice();
                        personalCrypto.setCurrentSum(currentSum);
                    }
                }).
                toList();
        personalCryptoRepo.saveAll(updatedPersonalCryptos);
    }

    public List<PersonalCryptoResponseDTO> getPersonalDeposits(Long userId) {
        updateCryptos();
        return personalCryptoRepo.findAllByUserId(userId)
                .stream()
                .map((personalCrypto) -> {
                            var coin = cryptoRepo.findById(personalCrypto.getCoinId());
                            if (coin.isPresent()) {
                                return PersonalCryptoResponseDTO
                                        .builder()
                                        .initialSum(personalCrypto.getInitialSum())
                                        .currentSum(personalCrypto.getCurrentSum())
                                        .createdDate(personalCrypto.getCreatedDate())
                                        .lastModifiedDate(personalCrypto.getLastModifiedDate())
                                        .slug(coin.get().getSlug())
                                        .amount(personalCrypto.getAmount())
                                        .coinId(personalCrypto.getCoinId())
                                        .name(coin.get().getName())
                                        .build();
                            }
                            return PersonalCryptoResponseDTO.builder().build();
                        }
                )
                .toList();
    }

    public Long removeCryptoPurchase(PersonalCryptoCoinRequestDTO cryptoReq, Long userId) {
        var personalCryptoForCoin = personalCryptoRepo
                .findFirstByCoinIdAndUserId(cryptoReq.getCoinId(), userId)
                .orElseThrow();

        var totalReqSum = cryptoReq.getAmount() * cryptoReq.getPrice();
        var newPersonalCryptoForCoin = personalCryptoForCoin;
        var oldAmount = personalCryptoForCoin.getAmount();
        var oldInitialSum = personalCryptoForCoin.getInitialSum();
        var oldCurrentSum = personalCryptoForCoin.getCurrentSum();


        newPersonalCryptoForCoin.setAmount(oldAmount - cryptoReq.getAmount());
        newPersonalCryptoForCoin.setCurrentSum(newPersonalCryptoForCoin.getAmount() * cryptoReq.getPrice());
        newPersonalCryptoForCoin.setInitialSum(oldInitialSum - totalReqSum);
        newPersonalCryptoForCoin.setCoinId(cryptoReq.getCoinId());

        return personalCryptoRepo.save(newPersonalCryptoForCoin).getId().longValue();
    }
}
