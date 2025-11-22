package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final Pattern VALID_PATTERN = Pattern.compile("^[ATCG]+$");

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) {
            // Let @NotNull or @NotEmpty handle null/empty if used, but validation logic usually requires checking here too if we want robust custom validation.
            // However, for this specific validator, false is appropriate if it's null/empty as it's not a valid sequence.
            return false;
        }

        int n = dna.length;
        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false; // Not square
            }
            if (!VALID_PATTERN.matcher(row).matches()) {
                return false; // Invalid characters
            }
        }
        return true;
    }
}
