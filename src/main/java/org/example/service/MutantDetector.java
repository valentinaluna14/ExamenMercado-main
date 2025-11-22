package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_THRESHOLD = 2;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    /**
     * Detects if the given DNA sequence belongs to a mutant.
     * A mutant is defined as having more than one sequence of four identical letters
     * obliquely, horizontally, or vertically.
     *
     * @param dna Array of strings representing the DNA matrix.
     * @return true if mutant, false otherwise.
     * @throws IllegalArgumentException if the DNA is invalid (null, empty, not square, invalid characters).
     */
    public boolean isMutant(String[] dna) {
        validateDna(dna);
        int n = dna.length;
        char[][] matrix = convertToMatrix(dna);

        int sequenceCount = 0;

        // Single pass iteration
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Only check directions if we haven't exceeded the count needed

                // Horizontal
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount >= MUTANT_THRESHOLD) return true;
                    }
                }

                // Vertical
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount >= MUTANT_THRESHOLD) return true;
                    }
                }

                // Diagonal Descending (\)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount >= MUTANT_THRESHOLD) return true;
                    }
                }

                // Diagonal Ascending (/)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount >= MUTANT_THRESHOLD) return true;
                    }
                }
            }
        }

        return false;
    }

    private void validateDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            throw new IllegalArgumentException("DNA cannot be null or empty");
        }

        int n = dna.length;
        for (String row : dna) {
            if (row == null || row.length() != n) {
                throw new IllegalArgumentException("DNA must be a square NxN matrix");
            }
            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(c)) {
                    throw new IllegalArgumentException("Invalid DNA character: " + c);
                }
            }
        }
    }

    private char[][] convertToMatrix(String[] dna) {
        int n = dna.length;
        char[][] matrix = new char[n][n];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }
        return matrix;
    }

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row][col+1] &&
                base == matrix[row][col+2] &&
                base == matrix[row][col+3];
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row+1][col] &&
                base == matrix[row+2][col] &&
                base == matrix[row+3][col];
    }

    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row+1][col+1] &&
                base == matrix[row+2][col+2] &&
                base == matrix[row+3][col+3];
    }

    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return base == matrix[row-1][col+1] &&
                base == matrix[row-2][col+2] &&
                base == matrix[row-3][col+3];
    }
}
