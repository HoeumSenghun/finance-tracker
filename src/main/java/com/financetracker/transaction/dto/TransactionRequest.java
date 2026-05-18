package com.financetracker.transaction.dto;

import com.financetracker.common.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private BigDecimal amount;

    private String note;

    @NotNull
    private TransactionType type;

    @NotNull
    @PastOrPresent
    private LocalDate date;

    @NotNull
    private Long categoryId;
}
