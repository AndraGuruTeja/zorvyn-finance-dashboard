package com.zorvyn.finance.repository;

import java.math.BigDecimal;

/**
 * Projection for category-level aggregation.
 */
public interface CategorySummaryProjection {
    String getCategory();
    String getType();
    BigDecimal getTotal();
    Long getCount();
}
