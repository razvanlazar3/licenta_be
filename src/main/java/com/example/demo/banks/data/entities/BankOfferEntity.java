package com.example.demo.banks.data.entities;

import com.example.demo.banks.data.constants.BankConstants;
import jakarta.persistence.*;
import lombok.*;

import java.time.Period;

@Getter
@Setter
@Table(name = BankConstants.BANK_OFFERS_TABLE)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Period period;
    private Integer discount;

    @ManyToOne
    @JoinColumn(name = "bankId")
    private BankEntity bank;
}
