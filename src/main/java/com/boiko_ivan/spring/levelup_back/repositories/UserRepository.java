package com.boiko_ivan.spring.levelup_back.repositories;

import com.boiko_ivan.spring.levelup_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
