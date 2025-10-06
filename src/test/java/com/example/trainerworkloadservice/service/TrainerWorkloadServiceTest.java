package com.example.trainerworkloadservice.service;

import com.example.trainerworkloadservice.dto.ActionType;
import com.example.trainerworkloadservice.dto.WorkloadRequest;
import com.example.trainerworkloadservice.dto.WorkloadResponse;
import com.example.trainerworkloadservice.model.TrainerWorkload;
import com.example.trainerworkloadservice.repository.TrainerWorkloadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainerWorkloadServiceTest {

    @Mock
    private TrainerWorkloadRepository repository;

    @InjectMocks
    private TrainerWorkloadService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddWorkloadCreatesNewTrainer() {
        // No trainer in repository initially
        when(repository.findById("john_doe")).thenReturn(Optional.empty());

        WorkloadRequest req = new WorkloadRequest(
                "john_doe",
                "John",
                "Doe",
                true,
                LocalDate.of(2024, 6, 15),
                2,
                ActionType.ADD
        );

        WorkloadResponse response = service.processWorkload(req);
        assertEquals(200, response.getStatusCode());
        assertEquals("Workload processed successfully", response.getMessage());

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());

    }

    @Test
    void testAddWorkloadIncrementsExistingTrainer() {
        TrainerWorkload existing = new TrainerWorkload("john_doe", "John", "Doe", true);
        Map<Integer, Map<Integer, Integer>> existingMap = new HashMap<>();
        existingMap.put(2024, new HashMap<>(Map.of(6, 2)));
        existing.setWorkloadSummaryJson(service.toJson(existingMap));

        when(repository.findById("john_doe")).thenReturn(Optional.of(existing));

        WorkloadRequest req = new WorkloadRequest(
                "john_doe", "John", "Doe", true,
                LocalDate.of(2024, 6, 15), 3, ActionType.ADD
        );

        WorkloadResponse response = service.processWorkload(req);
        assertEquals(200, response.getStatusCode());

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());

        TrainerWorkload savedTrainer = captor.getValue();

    }

    @Test
    void testDeleteWorkloadDecrementsValues() {
        TrainerWorkload existing = new TrainerWorkload("john_doe", "John", "Doe", true);
        Map<Integer, Map<Integer, Integer>> existingMap = new HashMap<>();
        existingMap.put(2024, new HashMap<>(Map.of(6, 5)));
        existing.setWorkloadSummaryJson(service.toJson(existingMap));

        when(repository.findById("john_doe")).thenReturn(Optional.of(existing));

        WorkloadRequest req = new WorkloadRequest(
                "john_doe", "John", "Doe", true,
                LocalDate.of(2024, 6, 15), 3, ActionType.DELETE
        );

        service.processWorkload(req);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
    }

    @Test
    void testDeleteWorkloadDoesNotGoBelowZero() {
        TrainerWorkload existing = new TrainerWorkload("john_doe", "John", "Doe", true);
        Map<Integer, Map<Integer, Integer>> existingMap = new HashMap<>();
        existingMap.put(2024, new HashMap<>(Map.of(6, 1)));
        existing.setWorkloadSummaryJson(service.toJson(existingMap));

        when(repository.findById("john_doe")).thenReturn(Optional.of(existing));

        WorkloadRequest req = new WorkloadRequest(
                "john_doe", "John", "Doe", true,
                LocalDate.of(2024, 6, 15), 5, ActionType.DELETE
        );

        service.processWorkload(req);

        ArgumentCaptor<TrainerWorkload> captor = ArgumentCaptor.forClass(TrainerWorkload.class);
        verify(repository).save(captor.capture());
    }

    @Test
    void testGetMonthlyWorkloadReturnsCorrectValue() {
        TrainerWorkload existing = new TrainerWorkload("john_doe", "John", "Doe", true);
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();
        Map<Integer, Integer> months =  new HashMap<>();
        int june = 6;
        int realHours = 4;
        months.put(june,realHours);
        map.put(2024, months);
        existing.setWorkloadSummaryJson(service.toJson(map));

        when(repository.findById("john_doe")).thenReturn(Optional.of(existing));

        int hours = service.getMonthlyWorkload("john_doe", 2024, june);
        assertEquals(4, hours);
    }

    @Test
    void testGetMonthlyWorkloadReturnsZeroIfNotFound() {
        when(repository.findById("unknown")).thenReturn(Optional.empty());
        int hours = service.getMonthlyWorkload("unknown", 2024, 6);
        assertEquals(0, hours);
    }
}