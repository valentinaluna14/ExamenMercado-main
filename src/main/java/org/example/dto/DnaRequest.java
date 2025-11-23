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

    @NotNull(message = "ADN no puede ser nulo")
    @NotEmpty(message = "ADN no puede estar vacío")
    @ValidDnaSequence
    @Schema(description = "NxN DNA matrix", example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]", requiredMode = Schema.RequiredMode.REQUIRED)
    private String[] dna;
}
