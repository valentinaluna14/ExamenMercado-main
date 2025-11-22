package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository repository;

    /**
     * Analyzes the DNA sequence to determine if it's mutant.
     * Stores the result in the database if it's a new sequence.
     *
     * @param dna DNA sequence matrix
     * @return true if mutant, false otherwise
     */
    @Transactional
    public boolean analyzeDna(String[] dna) {
        String hash = calculateDnaHash(dna);

        // Check if already exists
        Optional<DnaRecord> existing = repository.findByDnaHash(hash);
        if (existing.isPresent()) {
            return existing.get().isMutant();
        }

        // Determine if mutant
        boolean isMutant = mutantDetector.isMutant(dna);

        // Save result
        DnaRecord record = new DnaRecord();
        record.setDnaHash(hash);
        record.setMutant(isMutant);
        record.setCreatedAt(LocalDateTime.now());
        repository.save(record);

        return isMutant;
    }

    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Join all strings to form a single sequence for hashing
            StringBuilder sb = new StringBuilder();
            for (String s : dna) {
                sb.append(s);
            }
            byte[] hashBytes = digest.digest(sb.toString().getBytes(StandardCharsets.UTF_8));

            // Convert to hex
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating DNA hash", e);
        }
    }
}
