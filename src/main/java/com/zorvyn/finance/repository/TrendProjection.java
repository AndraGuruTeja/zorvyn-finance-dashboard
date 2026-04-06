package com.zorvyn.finance.repository;

import java.math.BigDecimal;

/**
 * Projection for monthly trend data.
 */
public interface TrendProjection {
    Integer getYear();
    Integer getMonth();
    BigDecimal getIncome();
    BigDecimal getExpenses();
    Long getTransactionCount();
}
