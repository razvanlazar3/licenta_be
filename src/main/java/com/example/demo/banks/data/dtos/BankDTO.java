package com.example.demo.banks.data.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BankDTO {

    private Long id;

    @NotBlank(message = "A name is required")
    private String name;

    @NotBlank(message = "A location is required")
    private String location;

    private List<OfferDTO> offers;
}
