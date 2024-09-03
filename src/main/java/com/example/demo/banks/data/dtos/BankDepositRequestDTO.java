package com.example.demo.banks.data.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BankDepositRequestDTO {

    private Long offerId;
    private Integer amount;
}
