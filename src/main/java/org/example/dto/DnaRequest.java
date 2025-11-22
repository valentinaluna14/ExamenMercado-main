package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.validation.ValidDnaSequence;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object containing the DNA sequence to verify")
public class DnaRequest {

    @NotNull(message = "DNA cannot be null")
    @NotEmpty(message = "DNA cannot be empty")
    @ValidDnaSequence
    @Schema(description = "NxN DNA matrix", example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private String[] dna;
}
