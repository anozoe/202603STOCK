package com.example.stock.repository;

import com.example.stock.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndIdNot(String email, Long id);

    Page<User> findByDeletedAtIsNullOrderByIdAsc(Pageable pageable);

    long countByDeletedAtIsNull();
}