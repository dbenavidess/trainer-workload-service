package com.example.trainerworkloadservice.messaging;

import com.example.trainerworkloadservice.dto.WorkloadRequest;
import com.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class WorkloadMessageListener {

    private final TrainerWorkloadService workloadService;

    public WorkloadMessageListener(TrainerWorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @JmsListener(destination = "trainer.workload.queue")
    public void receiveWorkloadEvent(WorkloadRequest request) {
        System.out.println("Received JMS workload for trainer: " + request.getTrainerUsername());
        workloadService.processWorkload(request);
    }
}