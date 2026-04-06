package com.zorvyn.finance.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TrendDto {
    private Integer year;
    private Integer month;
    private BigDecimal income;
    private BigDecimal expenses;
    private Long transactionCount;
}
