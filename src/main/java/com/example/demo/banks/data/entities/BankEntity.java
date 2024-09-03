package com.example.demo.banks.data.entities;

import com.example.demo.banks.data.constants.BankConstants;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Table(name = BankConstants.BANKS_TABLE)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String location;

    @OneToMany(mappedBy = "bank")
    private List<BankOfferEntity> bankOffers;
}
