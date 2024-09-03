package com.example.demo.banks.data.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class OfferDTO {

    private Long id;

    @NotBlank(message = "A period is required")
    private String period;

    @NotBlank(message = "A discount is required")
    private Integer discount;
}