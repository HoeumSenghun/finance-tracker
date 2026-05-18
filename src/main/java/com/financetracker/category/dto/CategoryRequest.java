package com.financetracker.category.dto;

import com.financetracker.common.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private TransactionType type;
}
