package com.example.demo.banks.data.dtos;

import lombok.*;

import java.time.LocalDateTime;
import java.time.Period;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BankDepositResponseDTO {

    private Long depositId;
    private Period period;
    private Integer discount;
    private Integer amount;
    private LocalDateTime createdDate;
    private String bankName;
}
