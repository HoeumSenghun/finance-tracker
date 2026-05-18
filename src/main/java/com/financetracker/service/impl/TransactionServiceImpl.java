package com.financetracker.service.impl;

import com.financetracker.domain.entity.Category;
import com.financetracker.domain.entity.Transaction;
import com.financetracker.domain.entity.User;
import com.financetracker.domain.enums.TransactionType;
import com.financetracker.dto.request.TransactionRequest;
import com.financetracker.dto.response.TransactionResponse;
import com.financetracker.exception.ForbiddenException;
import com.financetracker.mapper.TransactionMapper;
import com.financetracker.repository.TransactionRepository;
import com.financetracker.service.CategoryService;
import com.financetracker.service.FileStorageService;
import com.financetracker.service.TransactionService;
import com.financetracker.service.UserService;
import com.financetracker.specification.TransactionSpecification;
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
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final CategoryService categoryService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Override
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

    @Override
    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        User user = userService.getCurrentUser();
        Category category = categoryService.findOwnedCategoryEntity(request.getCategoryId(), user.getId());

        Transaction transaction = transactionMapper.toEntity(request);
        transaction.setUser(user);
        transaction.setCategory(category);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public TransactionResponse update(UUID id, TransactionRequest request) {
        Transaction transaction = getOwnedTransaction(id);
        User user = userService.getCurrentUser();
        Category category = categoryService.findOwnedCategoryEntity(request.getCategoryId(), user.getId());

        transactionMapper.updateEntity(request, transaction);
        transaction.setCategory(category);

        return transactionMapper.toResponse(transactionRepository.save(transaction));
    }

    @Override
    @Transactional
    public Map<String, String> delete(UUID id) {
        transactionRepository.delete(getOwnedTransaction(id));
        return Map.of("message", "Transaction deleted");
    }

    @Override
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
