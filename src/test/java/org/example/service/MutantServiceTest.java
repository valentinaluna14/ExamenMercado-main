package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    @Test
    @DisplayName("Should analyze and save human DNA (returns false)")
    void testAnalyzeHumanDnaAndSave() {
        // ARRANGE
        String[] humanDna = {
                "ATGCGA",
                "CAGTGC",
                "TTATTT",
                "AGACGG",
                "GCGTCA",
                "TCACTG"
        };

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna)).thenReturn(false);


        boolean result = mutantService.analyzeDna(humanDna);


        assertFalse(result, "Debe retornar false para humano");
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(repository, times(1)).save(any(DnaRecord.class));


        ArgumentCaptor<DnaRecord> captor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(repository).save(captor.capture());
        assertFalse(captor.getValue().isMutant(), "El registro guardado debe tener isMutant=false");
    }

    @Test
    @DisplayName("Should generate consistent hash for same DNA sequence")
    void testConsistentHashGeneration() {

        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);


        mutantService.analyzeDna(dna);
        mutantService.analyzeDna(dna);


        ArgumentCaptor<String> hashCaptor = ArgumentCaptor.forClass(String.class);
        verify(repository, times(2)).findByDnaHash(hashCaptor.capture());


        String firstHash = hashCaptor.getAllValues().get(0);
        String secondHash = hashCaptor.getAllValues().get(1);

        assertEquals(firstHash, secondHash,
                "El mismo DNA debe generar el mismo hash en múltiples llamadas");
        assertEquals(64, firstHash.length(),
                "Hash SHA-256 debe tener 64 caracteres hexadecimales");
    }

    @Test
    @DisplayName("Should save record with correct hash format (SHA-256)")
    void testSavesRecordWithCorrectHash() {

        String[] mutantDna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna)).thenReturn(true);


        mutantService.analyzeDna(mutantDna);


        ArgumentCaptor<DnaRecord> recordCaptor = ArgumentCaptor.forClass(DnaRecord.class);
        verify(repository).save(recordCaptor.capture());

        DnaRecord savedRecord = recordCaptor.getValue();


        assertNotNull(savedRecord.getDnaHash(), "Hash no debe ser null");
        assertEquals(64, savedRecord.getDnaHash().length(),
                "Hash SHA-256 debe tener exactamente 64 caracteres");
        assertTrue(savedRecord.getDnaHash().matches("^[a-f0-9]{64}$"),
                "Hash debe ser hexadecimal (solo caracteres a-f y 0-9)");


        assertTrue(savedRecord.isMutant(), "isMutant debe ser true");


        assertNotNull(savedRecord.getCreatedAt(), "createdAt no debe ser null");
    }



    @Test
    @DisplayName("Should handle different DNA sequences producing different hashes")
    void testDifferentDnaProducesDifferentHash() {

        String[] dna1 = {"AAAA", "CCCC", "TTTT", "GGGG"};
        String[] dna2 = {"ATGC", "CGTA", "TACG", "GCAT"};

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any())).thenReturn(true);


        mutantService.analyzeDna(dna1);
        mutantService.analyzeDna(dna2);


        ArgumentCaptor<String> hashCaptor = ArgumentCaptor.forClass(String.class);
        verify(repository, times(2)).findByDnaHash(hashCaptor.capture());

        String hash1 = hashCaptor.getAllValues().get(0);
        String hash2 = hashCaptor.getAllValues().get(1);

        assertNotEquals(hash1, hash2,
                "DNAs diferentes deben producir hashes diferentes");
    }

    @Test
    @DisplayName("Should not call detector when result is cached (performance optimization)")
    void testCacheOptimization() {

        String[] dna = {"AAAA", "CCCC", "TTTT", "GGGG"};
        DnaRecord cachedRecord = new DnaRecord();
        cachedRecord.setDnaHash("somehash123");
        cachedRecord.setMutant(false); // Humano cacheado

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord));


        boolean result = mutantService.analyzeDna(dna);


        assertFalse(result, "Debe retornar false desde caché");


        verify(repository, times(1)).findByDnaHash(anyString());
        verify(mutantDetector, never()).isMutant(any()); // NO debe llamar al detector
        verify(repository, never()).save(any()); // NO debe guardar
    }

    @Test
    @DisplayName("Should handle repository save operation")
    void testRepositorySaveOperation() {

        String[] dna = {"ATGC", "CGTA", "TACG", "GCAT"};

        when(repository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        when(mutantDetector.isMutant(dna)).thenReturn(false);

        DnaRecord savedRecord = new DnaRecord();
        savedRecord.setId(1L);
        when(repository.save(any(DnaRecord.class))).thenReturn(savedRecord);


        mutantService.analyzeDna(dna);


        verify(repository, times(1)).save(any(DnaRecord.class));
    }
}