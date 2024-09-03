package com.example.demo.banks.data.entities;

import com.example.demo.banks.data.constants.BankConstants;
import com.example.demo.users.data.entities.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = BankConstants.BANK_DEPOSITS_TABLE)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long offerId;
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
