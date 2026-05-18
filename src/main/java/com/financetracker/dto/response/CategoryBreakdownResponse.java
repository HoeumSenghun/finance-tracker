package com.financetracker.dto.response;

import com.financetracker.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBreakdownResponse {

    private Long categoryId;
    private String categoryName;
    private TransactionType type;
    private BigDecimal total;
}
