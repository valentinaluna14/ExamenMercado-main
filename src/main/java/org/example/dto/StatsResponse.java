package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Statistics of DNA verifications")
public class StatsResponse {

    @Schema(description = "Cantidad de secuencias de ADN mutante encontradas")
    private long count_mutant_dna;

    @Schema(description = "Cantidad de secuencias de ADN humano encontradas")
    private long count_human_dna;

    @Schema(description = "Ratio entre ADN mutante y humano")
    private double ratio;
}
