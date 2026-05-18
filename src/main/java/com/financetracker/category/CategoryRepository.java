// src/main/java/com/financetracker/category/CategoryRepository.java
package com.financetracker.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByUserId(UUID userId);

    Optional<Category> findByIdAndUserId(Long id, UUID userId);
}
