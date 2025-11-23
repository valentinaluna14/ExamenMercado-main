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
}