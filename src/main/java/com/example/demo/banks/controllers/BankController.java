package com.example.demo.banks.controllers;

import com.example.demo.banks.data.dtos.BankDTO;
import com.example.demo.banks.data.dtos.BankDepositRequestDTO;
import com.example.demo.banks.service.BankService;
import com.example.demo.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/banks")
public class BankController {

    private final BankService bankService;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<BankDTO>> getBanks() {
        return ResponseEntity.ok(bankService.getAll());
    }

    @PostMapping("/offer")
    public ResponseEntity<Long> registerOffer(@RequestBody BankDepositRequestDTO offer, @RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractId(token);
        return ResponseEntity.ok(bankService.registerOffer(offer, userId));
    }

    @GetMapping("/download-report")
    public ResponseEntity<byte[]> downloadReport(@RequestHeader("Authorization") String token) {
        Long userId = jwtService.extractId(token);
        String reportContent = bankService.generateReport(userId);

        byte[] content = reportContent.getBytes(StandardCharsets.UTF_8);
        String fileName = "bank_report.txt";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.setContentType(MediaType.TEXT_PLAIN);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(content.length)
                .body(content);
    }
}
