package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de error de la API")
public class ErrorResponse {

    @Schema(description = "Timestamp del error", example = "2025-01-07T15:30:45.123")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int status;

    @Schema(description = "Tipo de error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "Secuencia de ADN inválida")
    private String message;

    @Schema(description = "Ruta del endpoint", example = "/mutant")
    private String path;
}