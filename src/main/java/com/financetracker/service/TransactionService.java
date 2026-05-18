package com.financetracker.service;

import com.financetracker.domain.enums.TransactionType;
import com.financetracker.dto.request.TransactionRequest;
import com.financetracker.dto.response.TransactionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TransactionService {

    List<TransactionResponse> findAll(TransactionType type, String month, Long categoryId);

    TransactionResponse create(TransactionRequest request);

    TransactionResponse update(UUID id, TransactionRequest request);

    Map<String, String> delete(UUID id);

    Map<String, String> uploadReceipt(UUID id, MultipartFile file);
}
