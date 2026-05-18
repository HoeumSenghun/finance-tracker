// src/main/java/com/financetracker/transaction/TransactionService.java
package com.financetracker.transaction;

import com.financetracker.category.Category;
import com.financetracker.category.CategoryService;
import com.financetracker.common.enums.TransactionType;
import com.financetracker.common.exception.ForbiddenException;
import com.financetracker.transaction.dto.TransactionRequest;
import com.financetracker.transaction.dto.TransactionResponse;
import com.financetracker.upload.FileStorageService;
import com.financetracker.user.User;
import com.financetracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Transactional(readOnly = true)
    public List<TransactionResponse> findAll(TransactionType type, String month, Long categoryId) {
        User user = userService.getCurrentUser();
        Specification<Transaction> spec = TransactionSpecification.belongsToUser(user.getId());

        if (type != null) {
            spec = spec.and(TransactionSpecification.hasType(type));
        }
        if (month != null && !month.isBlank()) {
            LocalDate[] range = parseMonthRange(month);
            spec = spec.and(TransactionSpecification.inDateRange(range[0], range[1]));
        }
        if (categoryId != null) {
            spec = spec.and(TransactionSpecification.hasCategoryId(categoryId));
        }

        return transactionRepository.findAll(spec).stream()
                .map(transactionMapper::toResponse)
                .toList();
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        User user = userService.getCurrentUser();
        Category category = categoryService.findOwnedCategoryEntity(request.getCategoryId(), user.getId());

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setUser(user);
        transaction.setCategory(category);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponse update(UUID id, TransactionRequest request) {
        Transaction transaction = getOwnedTransaction(id);
        User user = userService.getCurrentUser();
        Category category = categoryService.findOwnedCategoryEntity(request.getCategoryId(), user.getId());

        transactionMapper.updateEntity(request, transaction);
        transaction.setCategory(category);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Transactional
    public Map<String, String> delete(UUID id) {
        transactionRepository.delete(getOwnedTransaction(id));
        return Map.of("message", "Transaction deleted");
    }

    @Transactional
    public Map<String, String> uploadReceipt(UUID id, MultipartFile file) {
        Transaction transaction = getOwnedTransaction(id);
        User user = userService.getCurrentUser();

        String relativePath = fileStorageService.store(file, user.getId(), id);
        transaction.setReceiptPath(relativePath);
        transactionRepository.save(transaction);

        return Map.of("message", "Receipt uploaded", "path", relativePath);
    }

    private Transaction getOwnedTransaction(UUID id) {
        User user = userService.getCurrentUser();
        return transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ForbiddenException("Transaction does not belong to you"));
    }

    private LocalDate[] parseMonthRange(String month) {
        try {
            YearMonth yearMonth = YearMonth.parse(month);
            return new LocalDate[]{yearMonth.atDay(1), yearMonth.atEndOfMonth()};
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid month format, expected yyyy-MM");
        }
    }
}
