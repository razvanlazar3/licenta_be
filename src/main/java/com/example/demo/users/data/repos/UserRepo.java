package com.example.demo.users.data.repos;

import com.example.demo.users.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findFirstByEmail(String email);

    boolean existsByEmail(String email);
}