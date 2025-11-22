package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        long mutantCount = repository.countByIsMutant(true);
        long humanCount = repository.countByIsMutant(false);

        double ratio = 0.0;
        if (humanCount > 0) {
            ratio = (double) mutantCount / humanCount;
        } else if (mutantCount > 0) {
            // If there are no humans but there are mutants, ratio is technically infinite or undefined,
            // but logically for this challenge, if we have mutants and 0 humans, the ratio is the mutant count
            // according to the example in README?
            // Example: "0 mutantes, 100 humanos -> ratio = 0.0"
            // "40 mutantes, 0 humanos -> ratio = 40.0 (caso especial)"
            ratio = (double) mutantCount;
        }

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
