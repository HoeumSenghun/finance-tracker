package com.financetracker.specification;

import com.financetracker.domain.entity.Transaction;
import com.financetracker.domain.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public final class TransactionSpecification {

    private TransactionSpecification() {
    }

    public static Specification<Transaction> belongsToUser(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Transaction> hasType(TransactionType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> inDateRange(LocalDate start, LocalDate end) {
        return (root, query, cb) -> cb.between(root.get("date"), start, end);
    }

    public static Specification<Transaction> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }
}
