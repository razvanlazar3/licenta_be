package com.example.demo.crypto.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalCryptoCoinRequestDTO {
    private Integer coinId;
    private Double price;
    private Integer amount;
}
