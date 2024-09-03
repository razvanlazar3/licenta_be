package com.example.demo.crypto.data.model;

import com.example.demo.crypto.data.constants.CryptoConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = CryptoConstants.PERSONAL_CRYPTO_TABLE)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalCrypto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer coinId;
    private Long userId;
    private Double initialSum;
    private Double currentSum;
    private Integer amount;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;
}
