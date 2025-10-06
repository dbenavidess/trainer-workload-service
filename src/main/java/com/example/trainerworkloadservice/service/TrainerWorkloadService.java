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
    private final ObjectMapper objectMapper = new ObjectMapper();

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

            // Deserialize existing workloadSummaryJson to Map
            Map<Integer, Map<Integer, Integer>> summaryMap = fromJson(workload.getWorkloadSummaryJson());

            int year = request.getTrainingDate().getYear();
            int month = request.getTrainingDate().getMonthValue();
            int duration = request.getTrainingDuration();

            summaryMap.putIfAbsent(year, new HashMap<>());
            summaryMap.get(year).putIfAbsent(month, 0);

            if (request.getActionType() == ActionType.ADD) {
                summaryMap.get(year).put(month, summaryMap.get(year).get(month) + duration);
            } else if (request.getActionType() == ActionType.DELETE) {
                int current = summaryMap.get(year).get(month);
                summaryMap.get(year).put(month, Math.max(0, current - duration)); // avoid negative
            }

            // Serialize back to JSON and save
            workload.setWorkloadSummaryJson(toJson(summaryMap));
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

        Map<Integer, Map<Integer, Integer>> summaryMap = fromJson(optWorkload.get().getWorkloadSummaryJson());

        return summaryMap.getOrDefault(year, new HashMap<>())
                .getOrDefault(month, 0);
    }

    /**
     * JSON Serialization Helper
     */
    private String toJson(Map<Integer, Map<Integer, Integer>> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing workload summary", e);
        }
    }

    /**
     * JSON Deserialization Helper
     */
    private Map<Integer, Map<Integer, Integer>> fromJson(String json) {
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }
        try {
            var typeFactory = objectMapper.getTypeFactory();
            var innerMapType = typeFactory.constructMapType(Map.class, Integer.class, Integer.class);
            var outerMapType = typeFactory.constructMapType(Map.class, Integer.class, innerMapType.getRawClass());
            return objectMapper.readValue(json, outerMapType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing workload summary", e);
        }
    }
}