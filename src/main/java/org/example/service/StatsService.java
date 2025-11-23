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

            ratio = (double) mutantCount;
        }

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
