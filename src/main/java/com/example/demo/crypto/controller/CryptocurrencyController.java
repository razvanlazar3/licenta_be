package com.example.demo.crypto.controller;

import com.example.demo.config.JwtService;
import com.example.demo.crypto.data.dtos.CryptocurrencyDTO;
import com.example.demo.crypto.data.dtos.PersonalCryptoCoinRequestDTO;
import com.example.demo.crypto.data.dtos.PersonalCryptoResponseDTO;
import com.example.demo.crypto.service.CryptocurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cryptocurrency")
@CrossOrigin(origins = "http://localhost:4200")
public class CryptocurrencyController {
    private final CryptocurrencyService cryptocurrencyService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<Page<CryptocurrencyDTO>> getCurrencyInfo(Pageable pageable) {
        Page<CryptocurrencyDTO> cryptos = cryptocurrencyService.updateCryptos(pageable);
        return ResponseEntity.ok(cryptos);
    }

    @GetMapping(value = "/personal")
    public ResponseEntity<List<PersonalCryptoResponseDTO>> getPersonalCrypto(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractId(token);
        List<PersonalCryptoResponseDTO> cryptos = cryptocurrencyService.getPersonalDeposits(userId);
        return ResponseEntity.ok(cryptos);
    }

    @PutMapping
    public ResponseEntity<Long> addOrUpdatePersonalCoin(@RequestBody PersonalCryptoCoinRequestDTO cryptoReq, @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractId(token);
        return ResponseEntity.ok(cryptocurrencyService.registerCryptoPurchase(cryptoReq, userId));
    }

    @PutMapping(value = "/remove")
    public ResponseEntity<Long> removePersonalCoin(@RequestBody PersonalCryptoCoinRequestDTO cryptoReq, @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractId(token);
        return ResponseEntity.ok(cryptocurrencyService.removeCryptoPurchase(cryptoReq, userId));
    }
}