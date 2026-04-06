package com.zorvyn.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TrendResponse {
    private String month;       // "2025-06"
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal net;
    private Long transactionCount;
}
