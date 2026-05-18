package com.financetracker.repository;

import com.financetracker.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(UUID userId);

    Optional<Category> findByIdAndUserId(Long id, UUID userId);
}
