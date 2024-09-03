package com.example.demo.banks.data.repos;

import com.example.demo.banks.data.entities.BankOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankOfferRepo extends JpaRepository<BankOfferEntity, Long> {

    Optional<BankOfferEntity> findFirstById(Long id);

}
