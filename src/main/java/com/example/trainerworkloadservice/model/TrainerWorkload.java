package com.example.trainerworkloadservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document("workload-summary")
@CompoundIndex(name = "first_last_name_idx", def = "{'trainerFirstName': 1, 'trainerLastName': 1}")
public class TrainerWorkload {

    @Id()
    private String trainerUsername;

    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;
    private Map<Integer, Map<Integer, Integer>> workloadSummary = new HashMap<>();

    public TrainerWorkload() {
    }

    public TrainerWorkload(String trainerUsername, String trainerFirstName, String trainerLastName, boolean isActive) {
        this.trainerUsername = trainerUsername;
        this.trainerFirstName = trainerFirstName;
        this.trainerLastName = trainerLastName;
        this.isActive = isActive;
        this.workloadSummary = new HashMap<>();
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String trainerUsername) {
        this.trainerUsername = trainerUsername;
    }

    public String getTrainerFirstName() {
        return trainerFirstName;
    }

    public void setTrainerFirstName(String trainerFirstName) {
        this.trainerFirstName = trainerFirstName;
    }

    public String getTrainerLastName() {
        return trainerLastName;
    }

    public void setTrainerLastName(String trainerLastName) {
        this.trainerLastName = trainerLastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public Map<Integer, Map<Integer, Integer>> getWorkloadSummary() {
        return workloadSummary;
    }

    public void setWorkloadSummary(Map<Integer, Map<Integer, Integer>> workloadSummary) {
        this.workloadSummary = workloadSummary;
    }
}