package com.edelweiss.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.edelweiss.security.domain.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
}
