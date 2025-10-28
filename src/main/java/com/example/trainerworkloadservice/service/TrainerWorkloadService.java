package com.example.trainerworkloadservice.service;

import com.example.trainerworkloadservice.dto.ActionType;
import com.example.trainerworkloadservice.dto.WorkloadRequest;
import com.example.trainerworkloadservice.dto.WorkloadResponse;
import com.example.trainerworkloadservice.model.TrainerWorkload;
import com.example.trainerworkloadservice.repository.TrainerWorkloadRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TrainerWorkloadService {

    private final TrainerWorkloadRepository repository;

    public TrainerWorkloadService(TrainerWorkloadRepository repository) {
        this.repository = repository;
    }

    /**
     * Process incoming workload request from the main microservice.
     */
    public WorkloadResponse processWorkload(WorkloadRequest request) {
        try {
            TrainerWorkload workload = repository.findById(request.getTrainerUsername())
                    .orElse(new TrainerWorkload(
                            request.getTrainerUsername(),
                            request.getTrainerFirstName(),
                            request.getTrainerLastName(),
                            request.getIsActive()
                    ));

            Map<Integer, Map<Integer, Integer>> summaryMap = workload.getWorkloadSummary();

            int year = request.getTrainingDate().getYear();
            int month = request.getTrainingDate().getMonthValue();
            int duration = request.getTrainingDuration();

            summaryMap.putIfAbsent(year, new HashMap<>());
            summaryMap.get(year).putIfAbsent(month, 0);

            if (request.getActionType() == ActionType.ADD) {
                summaryMap.get(year).put(month, summaryMap.get(year).get(month) + duration);
            } else if (request.getActionType() == ActionType.DELETE) {
                summaryMap.get(year).put(month, summaryMap.get(year).get(month) - duration);
            }

            repository.save(workload);

            return new WorkloadResponse("Workload processed successfully", 200);

        } catch (Exception e) {
            return new WorkloadResponse("Error processing workload: " + e.getMessage(), 500);
        }
    }

    /**
     * Retrieve monthly workload total for a trainer.
     */
    public Integer getMonthlyWorkload(String username, int year, int month) {
        Optional<TrainerWorkload> optWorkload = repository.findById(username);
        if (optWorkload.isEmpty()) {
            return 0;
        }

        Map<Integer, Map<Integer, Integer>> summaryMap = optWorkload.get().getWorkloadSummary();
        Map<Integer,Integer> yearly = summaryMap.getOrDefault(year, new HashMap<>());

        return yearly.getOrDefault(month, 0);
    }

}