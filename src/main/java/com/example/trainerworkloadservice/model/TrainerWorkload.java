package com.example.trainerworkloadservice.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Entity
public class TrainerWorkload {

    @Id
    private String trainerUsername;

    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;

    /**
     * Stored as JSON string in DB.
     * Structure: { year -> { month -> totalHours } }
     */
    @Lob
    private String workloadSummaryJson;

    @Transient
    private Map<Integer, Map<Integer, Integer>> workloadSummary = new HashMap<>();

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

    public String getWorkloadSummaryJson() {
        return workloadSummaryJson;
    }

    public void setWorkloadSummaryJson(String workloadSummaryJson) {
        this.workloadSummaryJson = workloadSummaryJson;
    }

    public Map<Integer, Map<Integer, Integer>> getWorkloadSummary() {
        return workloadSummary;
    }

    public void setWorkloadSummary(Map<Integer, Map<Integer, Integer>> workloadSummary) {
        this.workloadSummary = workloadSummary;
    }
}