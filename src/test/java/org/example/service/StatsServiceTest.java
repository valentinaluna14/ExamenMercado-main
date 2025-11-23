
package org.example.service;

import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRecordRepository repository;

    @InjectMocks
    private StatsService statsService;

    @Test
    void getStats_ShouldCalculateRatioCorrectly() {
        when(repository.countByIsMutant(true)).thenReturn(40L);
        when(repository.countByIsMutant(false)).thenReturn(100L);

        StatsResponse stats = statsService.getStats();

        assertEquals(40, stats.getCount_mutant_dna());
        assertEquals(100, stats.getCount_human_dna());
        assertEquals(0.4, stats.getRatio(), 0.0001);
    }

    @Test
    void getStats_ShouldHandleZeroHumans() {
        when(repository.countByIsMutant(true)).thenReturn(40L);
        when(repository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse stats = statsService.getStats();

        assertEquals(40, stats.getCount_mutant_dna());
        assertEquals(0, stats.getCount_human_dna());
        assertEquals(40.0, stats.getRatio(), 0.0001);
    }

    @Test
    void getStats_ShouldHandleZeroHumansAndZeroMutants() {
        when(repository.countByIsMutant(true)).thenReturn(0L);
        when(repository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse stats = statsService.getStats();

        assertEquals(0, stats.getCount_mutant_dna());
        assertEquals(0, stats.getCount_human_dna());
        assertEquals(0.0, stats.getRatio(), 0.0001);
    }
    @Test
    @DisplayName("Should calculate ratio with decimal precision")
    void testGetStatsWithDecimalRatio() {

        when(repository.countByIsMutant(true)).thenReturn(1L);
        when(repository.countByIsMutant(false)).thenReturn(3L);


        StatsResponse stats = statsService.getStats();


        assertEquals(1, stats.getCount_mutant_dna());
        assertEquals(3, stats.getCount_human_dna());
        assertEquals(0.333, stats.getRatio(), 0.001,
                "Ratio debe ser 0.333... (1/3) con precisión de 3 decimales");
    }

    @Test
    @DisplayName("Should return ratio 1.0 when counts are equal")
    void testGetStatsWithEqualCounts() {

        when(repository.countByIsMutant(true)).thenReturn(50L);
        when(repository.countByIsMutant(false)).thenReturn(50L);


        StatsResponse stats = statsService.getStats();


        assertEquals(50, stats.getCount_mutant_dna());
        assertEquals(50, stats.getCount_human_dna());
        assertEquals(1.0, stats.getRatio(), 0.0001,
                "Ratio debe ser 1.0 cuando hay igual cantidad de mutantes y humanos");
    }

    @Test
    @DisplayName("Should handle large numbers correctly")
    void testGetStatsWithLargeNumbers() {

        when(repository.countByIsMutant(true)).thenReturn(1_000_000L);
        when(repository.countByIsMutant(false)).thenReturn(2_000_000L);


        StatsResponse stats = statsService.getStats();


        assertEquals(1_000_000, stats.getCount_mutant_dna(),
                "Debe manejar 1 millón de mutantes");
        assertEquals(2_000_000, stats.getCount_human_dna(),
                "Debe manejar 2 millones de humanos");
        assertEquals(0.5, stats.getRatio(), 0.0001,
                "Ratio debe ser 0.5 (1M / 2M)");
    }

}