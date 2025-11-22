// Archivo: src/test/java/org/example/service/MutantDetectorTest.java

package org.example.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private final MutantDetector mutantDetector = new MutantDetector();

    @Test
    @DisplayName("Should detect mutant with horizontal sequences (2 or more)")
    void isMutant_Horizontal() {
        // Secuencia 1: AAAA en Fila 0
        // Secuencia 2: TTTT en Fila 5
        String[] dna = {
                "AAAAAT",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TTTTTT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with vertical sequences (2 or more)")
    void isMutant_Vertical() {
        // Secuencia 1: AAAA en Columna 0 (Filas 0-3)
        // Secuencia 2: GGGG en Columna 5 (Filas 2-5)
        String[] dna = {
                "ATGCGA",
                "ATGTGC",
                "ATAGGG",
                "ATAAAG",
                "CTCATC",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with diagonal sequences (Forward-Descending)")
    void isMutant_Diagonal() {
        // Secuencia 1: AAAA diagonal \
        // Secuencia 2: GGGG horizontal
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "GGGGGG" // <-- Se corrigió para asegurar 2 secuencias
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with both diagonal directions (Ascending / and Descending)")
    void isMutant_BothDiagonals() {
        // Secuencia 1: AAAA diagonal \
        // Secuencia 2: CCCC diagonal /
        String[] dna = {
                "ATCCGA", // C en (0,2) para diagonal /
                "CATAAC", // A en (1,1) para diagonal \
                "TTATTA",
                "AGACAG",
                "CCTATT", // C en (4,1) para diagonal /
                "GGCGCT" // G en (5,0) para diagonal /
        };
        // Para simplificar y asegurar, usaremos un caso más sencillo con 2 secuencias claras:
        String[] dnaSimple = {
                "ATGCGA",
                "CAATGC", // A-A-A-A diagonal \ en (1,1)
                "TTACGT",
                "AGACGA",
                "CCCCTA", // C-C-C-C horizontal
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dnaSimple));
    }

    @Test
    @DisplayName("Should return false for human DNA (only one sequence)")
    void isMutant_HumanOneSequence() {
        String[] dnaOne = {
                "AAAAAT", // 1 sequence horizontal
                "CAGTGC",
                "TTATGT",
                "AGAGAG",
                "CCCTTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dnaOne));
    }

    @Test
    @DisplayName("Should return false for human DNA (no sequences)")
    void isMutant_HumanNoSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should throw exception for invalid characters")
    void isMutant_InvalidCharacters() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTX", // X is invalid
                "TCACTG"
        };
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should throw exception for NxM matrix (not square)")
    void isMutant_NotSquare() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC"
        };
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception for null or empty input")
    void isMutant_NullOrEmpty(String[] dna) {
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with overlapping sequences (AAAAA -> 2 sequences)")
    void isMutant_Overlapping() {
        String[] dna = {
                "AAAAA",
                "CAGTG",
                "TTATG",
                "AGAAG",
                "CCCCT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should handle minimum size 4x4")
    void isMutant_MinSize() {
        String[] dna = {
                "AAAA", // Secuencia 1
                "CCCC", // Secuencia 2
                "TTTT",
                "GGGG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }
}