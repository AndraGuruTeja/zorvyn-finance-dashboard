package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.response.ApiResponse;
import com.zorvyn.finance.dto.response.CategorySummaryResponse;
import com.zorvyn.finance.dto.response.DashboardSummaryResponse;
import com.zorvyn.finance.dto.response.TrendResponse;
import com.zorvyn.finance.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
@Tag(name = "Dashboard", description = "Financial analytics and aggregation endpoints")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @Operation(summary = "Total income, expenses, net balance, and record count")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getSummary()));
    }

    @GetMapping("/category-summary")
    @Operation(summary = "Category-wise totals (optional ?type=INCOME or EXPENSE)")
    public ResponseEntity<ApiResponse<List<CategorySummaryResponse>>> getCategorySummary(
            @RequestParam(required = false) String type) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getCategorySummary(type)));
    }

    @GetMapping("/trends")
    @Operation(summary = "Monthly income/expense trends (optional ?months=12)")
    public ResponseEntity<ApiResponse<List<TrendResponse>>> getTrends(
            @RequestParam(defaultValue = "12") int months) {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getMonthlyTrends(months)));
    }

    @GetMapping("/recent")
    @Operation(summary = "Get the 5 most recent financial records")
    public ResponseEntity<ApiResponse<List<com.zorvyn.finance.dto.response.RecordResponse>>> getRecent() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getRecentRecords()));
    }
}
