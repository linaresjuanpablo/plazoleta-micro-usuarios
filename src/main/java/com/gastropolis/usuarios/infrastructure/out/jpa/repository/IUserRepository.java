package com.gastropolis.usuarios.infrastructure.out.jpa.repository;

import com.gastropolis.usuarios.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByDni(String dni);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);
}
