package com.example.demo.banks.data.repos;

import com.example.demo.banks.data.entities.BankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepo extends JpaRepository<BankEntity, Long> {

    Optional<BankEntity> findFirstByName(String name);
}
