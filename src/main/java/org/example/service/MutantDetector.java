package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_THRESHOLD = 2;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    public boolean isMutant(String[] dna) {
        validateDna(dna);
        int n = dna.length;
        char[][] matrix = convertToMatrix(dna);

        int sequenceCount = 0;


        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {


                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount >= MUTANT_THRESHOLD) return true;
                    }
                }


                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount >= MUTANT_THRESHOLD) return true;
                    }
                }


                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount >= MUTANT_THRESHOLD) return true;
                    }
                }

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
            throw new IllegalArgumentException("ADN no puede ser nulo o vacío");
        }

        int n = dna.length;


        if (n < SEQUENCE_LENGTH) {
            throw new IllegalArgumentException("La matriz de ADN debe ser de al menos " + SEQUENCE_LENGTH + "x" + SEQUENCE_LENGTH);
        }

        for (String row : dna) {
            if (row == null || row.length() != n) {
                throw new IllegalArgumentException("La matriz de ADN debe ser cuadrada (NxN)");
            }
            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(c)) {
                    throw new IllegalArgumentException("Caracter de ADN inválido: " + c);
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