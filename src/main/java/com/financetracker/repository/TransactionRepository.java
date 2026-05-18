package com.financetracker.repository;

import com.financetracker.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>,
        JpaSpecificationExecutor<Transaction> {

    Optional<Transaction> findByIdAndUserId(UUID id, UUID userId);

    @Query("""
            SELECT t.type, SUM(t.amount)
            FROM Transaction t
            WHERE t.user.id = :userId
              AND FUNCTION('TO_CHAR', t.date, 'YYYY-MM') = :month
            GROUP BY t.type
            """)
    List<Object[]> sumByTypeForMonth(@Param("userId") UUID userId, @Param("month") String month);

    @Query("""
            SELECT t.category.id, t.category.name, t.category.type, SUM(t.amount)
            FROM Transaction t
            WHERE t.user.id = :userId
              AND FUNCTION('TO_CHAR', t.date, 'YYYY-MM') = :month
            GROUP BY t.category.id, t.category.name, t.category.type
            """)
    List<Object[]> sumByCategoryForMonth(@Param("userId") UUID userId, @Param("month") String month);
}
