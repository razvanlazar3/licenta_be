package com.example.demo.crypto.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptocurrencyDTO {
    private Integer id;
    private String name;
    private String symbol;
    private String slug;
    private Integer score;
    private String grade;
    private String lastUpdated;
    private Double price;
    private Integer volume_24h;
    private Integer volume_change_24h;
    private Integer percent_change_1h;
    private Integer percent_change_24h;
    private Integer percent_change_7d;
}

