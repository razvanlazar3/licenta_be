package com.example.demo.crypto.data.repos;

import com.example.demo.crypto.data.model.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoRepo extends JpaRepository<Crypto, Integer> {

}
