package com.example.demo.crypto.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalCryptoResponseDTO {
    private Integer coinId;
    private String name;
    private String slug;
    private Double initialSum;
    private Double currentSum;
    private Integer amount;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
