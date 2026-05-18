// src/main/java/com/financetracker/report/ReportController.java
package com.financetracker.report;

import com.financetracker.common.ApiResponse;
import com.financetracker.report.dto.CategoryBreakdownResponse;
import com.financetracker.report.dto.MonthlySummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlySummaryResponse>> monthlySummary(
            @RequestParam String month) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getMonthlySummary(month)));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryBreakdownResponse>>> categoryBreakdown(
            @RequestParam String month) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getCategoryBreakdown(month)));
    }
}
