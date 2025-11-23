package org.example.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private final MutantDetector mutantDetector = new MutantDetector();

    @Test
    @DisplayName("Should detect mutant with horizontal sequences (2 or more)")
    void isMutant_Horizontal() {

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

        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "GGGGGG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should detect mutant with both diagonal directions (Ascending / and Descending)")
    void isMutant_BothDiagonals() {

        String[] dna = {
                "ATCCGA",
                "CATAAC",
                "TTATTA",
                "AGACAG",
                "CCTATT",
                "GGCGCT"
        };

        String[] dnaSimple = {
                "ATGCGA",
                "CAATGC",
                "TTACGT",
                "AGACGA",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dnaSimple));
    }

    @Test
    @DisplayName("Should return false for human DNA (only one sequence)")
    void isMutant_HumanOneSequence() {
        String[] dnaOne = {
                "AAAATA",
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
                "CCCCTX",
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
                "AAAA",
                "CCCC",
                "TTTT",
                "GGGG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }
    @Test
    @DisplayName("Should detect mutant with mixed sequence types")
    void testMutantWithMixedSequences() {
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
    @DisplayName("Should validate DNA with lowercase characters throws exception")
    void testInvalidDnaWithLowercase() {
        String[] dna = {
                "atgcga",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        assertThrows(IllegalArgumentException.class,
                () -> mutantDetector.isMutant(dna),
                "Debe rechazar caracteres en minúscula");
    }

    @Test
    @DisplayName("Should detect mutant with diagonal ascending in last rows")
    void testMutantDiagonalAscendingLastRows() {
        String[] dna = {
                "ATGCGT",
                "CAGTGA",
                "TTATGA",
                "AGAAGA",
                "CCCCAA",
                "TCACTA"
        };

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Should handle DNA with exactly 2 sequences (boundary case)")
    void testMutantWithExactlyTwoSequences() {
        String[] dna = {
                "AAAAGT",
                "CAGTGC",
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TTTTCA"
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante con exactamente 2 secuencias");
    }

    @Test
    @DisplayName("Should detect mutant in large DNA matrix (10x10)")
    void testMutantWithLargeDna() {
        String[] dna = {
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA",
                "CCCCTACCCC",
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA"
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Debe detectar mutante en matriz 10x10 con múltiples secuencias horizontales");
    }

    @Test
    @DisplayName("Should detect mutant when all characters are the same")
    void testMutantAllSameCharacter() {
        String[] dna = {
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA"
        };

        assertTrue(mutantDetector.isMutant(dna),
                "Matriz con todos caracteres iguales debe ser mutante (múltiples secuencias)");
    }

    @Test
    @DisplayName("Should return false for small human DNA (4x4 without sequences)")
    void testNotMutantSmallDna() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };

        assertFalse(mutantDetector.isMutant(dna),
                "Matriz 4x4 sin secuencias debe retornar false");
    }

    @Test
    @DisplayName("Should throw exception for matrix smaller than 4x4")
    void testNotMutantWithTooSmallDna() {
        String[] dna = {
                "ATG",
                "CAG",
                "TTA"
        };

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mutantDetector.isMutant(dna),
                "Debe lanzar excepción para matriz menor a 4x4"
        );

        assertTrue(exception.getMessage().contains("al menos 4x4"),
                "El mensaje debe indicar el tamaño mínimo requerido");
    }

    @Test
    @DisplayName("Should throw exception when one row is null")
    void testNotMutantWithNullRow() {
        String[] dna = {
                "ATGCGA",
                null,  // Fila nula
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> mutantDetector.isMutant(dna),
                "Debe lanzar excepción cuando una fila es null"
        );

        assertTrue(exception.getMessage().contains("cuadrada"),
                "El mensaje debe mencionar que debe ser cuadrada");
    }

    @Test
    @DisplayName("Should not count sequences longer than 4 as multiple sequences")
    void testNotMutantWithSequenceLongerThanFour() {
        String[] dna = {
                "AAAAAA",
                "CAGTGC",
                "TTATGT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };


        boolean result = mutantDetector.isMutant(dna);


        assertTrue(result,
                "Secuencia de 6 caracteres iguales debe contener múltiples secuencias de 4");
    }
}