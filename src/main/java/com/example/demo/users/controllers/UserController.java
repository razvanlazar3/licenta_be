package com.example.demo.users.controllers;

import com.example.demo.banks.data.dtos.BankDepositResponseDTO;
import com.example.demo.banks.service.BankService;
import com.example.demo.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class UserController {

    private final BankService bankService;
    private final JwtService jwtService;

    @GetMapping(value = "/deposits")
    public ResponseEntity<List<BankDepositResponseDTO>> getBanks(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractId(token);
        return ResponseEntity.ok(bankService.getDeposits(userId));
    }

    @DeleteMapping(value = "/deposit/{depositId}")
    public ResponseEntity<Long> unsubscribeFromHospital(@PathVariable Long depositId,
                                                        @RequestHeader("Authorization") String token) {

        Long userId = jwtService.extractId(token);
        return ResponseEntity.ok(bankService.removeDeposit(depositId, userId));
    }
}
