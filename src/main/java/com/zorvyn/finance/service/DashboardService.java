package com.zorvyn.finance.service;

import com.zorvyn.finance.dto.response.CategorySummaryResponse;
import com.zorvyn.finance.dto.response.DashboardSummaryResponse;
import com.zorvyn.finance.dto.response.TrendResponse;
import com.zorvyn.finance.repository.CategorySummaryProjection;
import com.zorvyn.finance.repository.FinancialRecordRepository;
import com.zorvyn.finance.repository.SummaryProjection;
import com.zorvyn.finance.repository.TrendProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final FinancialRecordRepository recordRepository;

    /**
     * Overall financial summary: total income, expenses, net balance, record count.
     * All aggregation is done in SQL (SUM, COUNT) — NOT in Java loops.
     */
    public DashboardSummaryResponse getSummary() {
        SummaryProjection summary = recordRepository.getDashboardSummary();

        BigDecimal income = summary.getTotalIncome() != null ? summary.getTotalIncome() : BigDecimal.ZERO;
        BigDecimal expenses = summary.getTotalExpenses() != null ? summary.getTotalExpenses() : BigDecimal.ZERO;

        return DashboardSummaryResponse.builder()
                .totalIncome(income)
                .totalExpenses(expenses)
                .netBalance(income.subtract(expenses))
                .totalRecords(summary.getTotalRecords() != null ? summary.getTotalRecords() : 0L)
                .build();
    }

    /**
     * Category-wise breakdown with optional type filter.
     * Uses SQL GROUP BY — NOT Java loops.
     */
    public List<CategorySummaryResponse> getCategorySummary(String type) {
        List<CategorySummaryProjection> projections = recordRepository.getCategorySummary(type);

        return projections.stream()
                .map(p -> CategorySummaryResponse.builder()
                        .category(p.getCategory())
                        .type(p.getType())
                        .total(p.getTotal())
                        .count(p.getCount())
                        .build())
                .toList();
    }

    /**
     * Monthly income/expense trends for the last N months.
     * Uses SQL EXTRACT + GROUP BY — NOT Java loops.
     */
    public List<TrendResponse> getMonthlyTrends(int months) {
        LocalDate startDate = LocalDate.now().minusMonths(months);
        List<com.zorvyn.finance.dto.response.TrendDto> projections = recordRepository.getMonthlyTrends(startDate);

        return projections.stream()
                .map(p -> {
                    BigDecimal income = p.getIncome() != null ? p.getIncome() : BigDecimal.ZERO;
                    BigDecimal expenses = p.getExpenses() != null ? p.getExpenses() : BigDecimal.ZERO;

                    return TrendResponse.builder()
                            .month(String.format("%d-%02d", p.getYear(), p.getMonth()))
                            .income(income)
                            .expenses(expenses)
                            .net(income.subtract(expenses))
                            .transactionCount(p.getTransactionCount())
                            .build();
                })
                .toList();
    }

    /**
     * Get the 5 most recent records.
     */
    public List<com.zorvyn.finance.dto.response.RecordResponse> getRecentRecords() {
        return recordRepository.findTop5ByOrderByDateDesc()
                .stream()
                .map(com.zorvyn.finance.dto.response.RecordResponse::from)
                .toList();
    }
}
