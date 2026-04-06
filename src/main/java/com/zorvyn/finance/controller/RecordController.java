package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.request.CreateRecordRequest;
import com.zorvyn.finance.dto.request.UpdateRecordRequest;
import com.zorvyn.finance.dto.response.ApiResponse;
import com.zorvyn.finance.dto.response.RecordResponse;
import com.zorvyn.finance.entity.RecordType;
import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Financial Records", description = "Income/expense record management")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new financial record (Admin only)")
    public ResponseEntity<ApiResponse<RecordResponse>> createRecord(
            @Valid @RequestBody CreateRecordRequest request,
            @AuthenticationPrincipal User currentUser) {
        RecordResponse data = recordService.createRecord(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Record created successfully.", data));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "List records with filtering, sorting, and pagination")
    public ResponseEntity<ApiResponse<Page<RecordResponse>>> listRecords(
            @RequestParam(required = false) RecordType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, sort = "date") Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                recordService.listRecords(type, category, startDate, endDate, pageable)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get a single record by ID")
    public ResponseEntity<ApiResponse<RecordResponse>> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(recordService.getRecordById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a financial record (Admin only)")
    public ResponseEntity<ApiResponse<RecordResponse>> updateRecord(
            @PathVariable Long id, @Valid @RequestBody UpdateRecordRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Record updated successfully.",
                recordService.updateRecord(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a financial record (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return ResponseEntity.ok(ApiResponse.ok("Record deleted successfully.", null));
    }
}
