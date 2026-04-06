package com.zorvyn.finance.controller;

import com.zorvyn.finance.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "API health check")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Check if the API is running")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        Map<String, String> info = Map.of(
                "status", "UP",
                "service", "Zorvyn Finance API",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.ok("Zorvyn Finance API is running.", info));
    }
}
