package com.ups.portafolio.portafolio_backend.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ups.portafolio.portafolio_backend.users.entities.UserEntity;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserEntity, UUID>{
    Optional<UserEntity> findByEmail(String email);
}
