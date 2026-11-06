package com.tienda.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tienda.demo.entity.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);

    Optional<Users> findByUsernameOrEmail(
            String username,
            String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


}

