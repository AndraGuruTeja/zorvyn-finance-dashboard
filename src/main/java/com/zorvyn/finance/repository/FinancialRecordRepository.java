package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.FinancialRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialRecordRepository
        extends JpaRepository<FinancialRecord, Long>, JpaSpecificationExecutor<FinancialRecord> {

    // ── Dashboard: overall summary ──
    @Query(value = "SELECT " +
            "COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE 0 END), 0) AS totalIncome, " +
            "COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount ELSE 0 END), 0) AS totalExpenses, " +
            "COUNT(*) AS totalRecords " +
            "FROM financial_records", nativeQuery = true)
    SummaryProjection getDashboardSummary();

    // ── Dashboard: category breakdown (optional type filter) ──
    @Query(value = "SELECT category, type, " +
            "ROUND(CAST(SUM(amount) AS NUMERIC), 2) AS total, " +
            "COUNT(*) AS count " +
            "FROM financial_records " +
            "WHERE (:type IS NULL OR type = :type) " +
            "GROUP BY category, type " +
            "ORDER BY total DESC", nativeQuery = true)
    List<CategorySummaryProjection> getCategorySummary(@Param("type") String type);

    // ── Dashboard: monthly trends ──
    @Query("SELECT new com.zorvyn.finance.dto.response.TrendDto(" +
            "  CAST(YEAR(r.date) AS integer), " +
            "  CAST(MONTH(r.date) AS integer), " +
            "  COALESCE(SUM(CASE WHEN r.type = com.zorvyn.finance.entity.RecordType.INCOME THEN r.amount ELSE 0 END), 0), " +
            "  COALESCE(SUM(CASE WHEN r.type = com.zorvyn.finance.entity.RecordType.EXPENSE THEN r.amount ELSE 0 END), 0), " +
            "  COUNT(r) " +
            ") " +
            "FROM FinancialRecord r " +
            "WHERE r.date >= :startDate " +
            "GROUP BY YEAR(r.date), MONTH(r.date) " +
            "ORDER BY YEAR(r.date) ASC, MONTH(r.date) ASC")
    List<com.zorvyn.finance.dto.response.TrendDto> getMonthlyTrends(@Param("startDate") LocalDate startDate);

    List<FinancialRecord> findTop5ByOrderByDateDesc();
}
