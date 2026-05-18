package com.financetracker.mapper;

import com.financetracker.domain.entity.Transaction;
import com.financetracker.dto.request.TransactionRequest;
import com.financetracker.dto.response.TransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        Long categoryId = null;
        String categoryName = null;
        if (transaction.getCategory() != null) {
            categoryId = transaction.getCategory().getId();
            categoryName = transaction.getCategory().getName();
        }
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .note(transaction.getNote())
                .type(transaction.getType())
                .date(transaction.getDate())
                .categoryId(categoryId)
                .categoryName(categoryName)
                .receiptPath(transaction.getReceiptPath())
                .build();
    }

    public Transaction toEntity(TransactionRequest request) {
        if (request == null) {
            return null;
        }
        return Transaction.builder()
                .amount(request.getAmount())
                .note(request.getNote())
                .type(request.getType())
                .date(request.getDate())
                .build();
    }

    public void updateEntity(TransactionRequest request, Transaction transaction) {
        if (request == null || transaction == null) {
            return;
        }
        transaction.setAmount(request.getAmount());
        transaction.setNote(request.getNote());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
    }
}
