package com.example.trainerworkloadservice.controller;

import com.example.trainerworkloadservice.dto.WorkloadRequest;
import com.example.trainerworkloadservice.dto.WorkloadResponse;
import com.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/workload")
public class TrainerWorkloadController {

    private final TrainerWorkloadService workloadService;

    public TrainerWorkloadController(TrainerWorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    /**
     * Accept workload updates from main microservice
     */
    @PostMapping
    public ResponseEntity<WorkloadResponse> processWorkload(@RequestBody WorkloadRequest request) {
        WorkloadResponse response = workloadService.processWorkload(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    /**
     * Get monthly total hours for a specific trainer
     */
    @GetMapping("/{username}/{year}/{month}")
    public ResponseEntity<Integer> getMonthlyWorkload(
            @PathVariable String username,
            @PathVariable int year,
            @PathVariable int month) {
        Integer totalHours = workloadService.getMonthlyWorkload(username, year, month);
        return ResponseEntity.ok(totalHours);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}