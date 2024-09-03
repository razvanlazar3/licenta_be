package com.example.demo.crypto.data.model;

import com.example.demo.crypto.data.constants.CryptoConstants;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Table(name = CryptoConstants.CRYPTO_TABLE)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crypto {
    @Id
    private Integer id;
    private String name;
    private String symbol;
    private String slug;
    private Integer score;
    private String grade;
    private String lastUpdated;
    private Double price;
    private Double volume_24h;
    private Double volume_change_24h;
    private Double percent_change_1h;
    private Double percent_change_24h;
    private Double percent_change_7d;
}
