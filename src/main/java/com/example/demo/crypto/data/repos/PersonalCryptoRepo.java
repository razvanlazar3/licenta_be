package com.example.demo.crypto.data.repos;

import com.example.demo.crypto.data.model.PersonalCrypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalCryptoRepo extends JpaRepository<PersonalCrypto, Long> {

    Optional<PersonalCrypto> findFirstByCoinIdAndUserId(Integer coinId, Long userId);

    List<PersonalCrypto> findAllByUserId(Long userId);
}
