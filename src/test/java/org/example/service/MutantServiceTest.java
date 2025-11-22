package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    void analyzeDna_ShouldReturnCachedResult_WhenDnaExists() {
        // Assuming a hash is calculated, but since it's private, we can't mock calculation easily.
        // But calculateDnaHash is deterministic.
        // We mock repository to return a record.

        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};
        DnaRecord record = new DnaRecord();
        record.setMutant(true);

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(record));

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        verify(mutantDetector, never()).isMutant(any());
        verify(repository, never()).save(any());
    }

    @Test
    void analyzeDna_ShouldAnalyzeAndSave_WhenDnaDoesNotExist() {
        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(true);

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        verify(mutantDetector).isMutant(dna);
        verify(repository).save(any(DnaRecord.class));
    }
}
