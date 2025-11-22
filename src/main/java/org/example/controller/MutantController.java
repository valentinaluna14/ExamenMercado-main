package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API for mutant detection")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    @Operation(summary = "Check if a human is a mutant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Is a Mutant"),
            @ApiResponse(responseCode = "403", description = "Is a Human (Forbidden)"),
            @ApiResponse(responseCode = "400", description = "Invalid DNA sequence")
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyzeDna(request.getDna());
        return isMutant ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Get verification statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}
