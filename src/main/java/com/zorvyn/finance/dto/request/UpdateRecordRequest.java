package com.zorvyn.finance.dto.request;

import com.zorvyn.finance.entity.RecordType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UpdateRecordRequest {

    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private RecordType type;

    private String category;

    private LocalDate date;

    @Size(max = 500, message = "Notes must be at most 500 characters")
    private String notes;
}
