package com.financetracker.service;

import com.financetracker.dto.response.CategoryBreakdownResponse;
import com.financetracker.dto.response.MonthlySummaryResponse;

import java.util.List;

public interface ReportService {

    MonthlySummaryResponse getMonthlySummary(String month);

    List<CategoryBreakdownResponse> getCategoryBreakdown(String month);
}
