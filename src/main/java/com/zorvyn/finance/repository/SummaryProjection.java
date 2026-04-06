package com.zorvyn.finance.repository;

import java.math.BigDecimal;

/**
 * Projection for dashboard financial summary.
 */
public interface SummaryProjection {
    BigDecimal getTotalIncome();
    BigDecimal getTotalExpenses();
    Long getTotalRecords();
}
