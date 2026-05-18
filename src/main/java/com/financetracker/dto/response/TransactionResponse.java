package com.financetracker.dto.response;

import com.financetracker.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private UUID id;
    private BigDecimal amount;
    private String note;
    private TransactionType type;
    private LocalDate date;
    private Long categoryId;
    private String categoryName;
    private String receiptPath;
}
