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
    @DisplayName("Should detect mutant with horizontal sequences")
    void isMutant_Horizontal() {
        String[] dna = {
                "AAAAAA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with vertical sequences")
    void isMutant_Vertical() {
        String[] dna = {
                "ATGCGA",
                "AAGTGC",
                "ATATGT",
                "AGAAGG",
                "CTCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with diagonal sequences")
    void isMutant_Diagonal() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with both diagonal directions (forward and backward)")
    void isMutant_BothDiagonals() {
        String[] dna = {
                "ATGCGA",
                "CAGTAC",
                "TTAAGT",
                "AGAAGG",
                "CCCTTA",
                "TCACTG"
        };
        // Checks if it finds sequences in different diagonal directions
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should return false for human DNA (only one sequence)")
    void isMutant_HumanOneSequence() {
        String[] dnaOne = {
                "AAAAGA", // 1 sequence horizontal
                "CAGTGC",
                "TTATGT",
                "AGAGAG", // Changed GG -> AG to break vertical sequence in Col 4
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
    @DisplayName("Should return false for invalid characters")
    void isMutant_InvalidCharacters() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTX", // X is invalid
                "TCACTG"
        };
        // Depending on implementation, this might throw exception or return false.
        // The guide says "lanzar una excepción apropiada o devolver false".
        // Given the robustness requirement, throwing exception is better, but let's see.
        // The controller validation will catch this usually, but the core logic should probably return false or throw.
        // Let's assume it should validation fails and return false or throw.
        // I will implement it to throw IllegalArgumentException for invalid chars as per robust code.
        assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should return false/throw for NxM matrix (not square)")
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
    @DisplayName("Should detect mutant with overlapping sequences")
    void isMutant_Overlapping() {
        // AAAAA -> 2 sequences of 4?
        // AAAA (0-3) and AAAA (1-4).
        // If implementation counts strictly distinct sequences or just occurrences.
        // "Sabrás si un humano es mutante, si encuentras más de una secuencia de cuatro letras iguales"
        // Usually overlaps count.
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
                "AAAA",
                "CCCC",
                "TTTT",
                "GGGG"
        };
        // 4 sequences found
        assertTrue(mutantDetector.isMutant(dna));
    }
}
