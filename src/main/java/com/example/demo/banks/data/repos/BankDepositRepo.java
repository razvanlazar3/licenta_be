package com.example.demo.banks.data.repos;

import com.example.demo.banks.data.entities.BankDepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankDepositRepo extends JpaRepository<BankDepositEntity, Long> {

    Long deleteAllById(Long id);

    List<BankDepositEntity> findByUserId(Long userId);
}
