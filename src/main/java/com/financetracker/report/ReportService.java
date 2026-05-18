package com.financetracker.report;

import com.financetracker.common.enums.TransactionType;
import com.financetracker.report.dto.CategoryBreakdownResponse;
import com.financetracker.report.dto.MonthlySummaryResponse;
import com.financetracker.transaction.TransactionRepository;
import com.financetracker.user.User;
import com.financetracker.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public MonthlySummaryResponse getMonthlySummary(String month) {
        User user = userService.getCurrentUser();
        List<Object[]> rows = transactionRepository.sumByTypeForMonth(user.getId(), month);

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Object[] row : rows) {
            TransactionType type = (TransactionType) row[0];
            BigDecimal sum = (BigDecimal) row[1];
            if (type == TransactionType.INCOME) {
                totalIncome = sum;
            } else if (type == TransactionType.EXPENSE) {
                totalExpense = sum;
            }
        }

        return MonthlySummaryResponse.builder()
                .month(month)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(totalIncome.subtract(totalExpense))
                .build();
    }

    @Transactional(readOnly = true)
    public List<CategoryBreakdownResponse> getCategoryBreakdown(String month) {
        User user = userService.getCurrentUser();
        List<Object[]> rows = transactionRepository.sumByCategoryForMonth(user.getId(), month);

        return rows.stream()
                .map(row -> CategoryBreakdownResponse.builder()
                        .categoryId((Long) row[0])
                        .categoryName((String) row[1])
                        .type((TransactionType) row[2])
                        .total((BigDecimal) row[3])
                        .build())
                .toList();
    }
}
