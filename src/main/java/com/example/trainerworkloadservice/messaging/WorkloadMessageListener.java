package com.example.trainerworkloadservice.messaging;

import com.example.trainerworkloadservice.dto.MonthlyWorkloadRequest;
import com.example.trainerworkloadservice.dto.WorkloadRequest;
import com.example.trainerworkloadservice.dto.WorkloadResponse;
import com.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
public class WorkloadMessageListener {

    private final TrainerWorkloadService workloadService;
    private final DeadLetterPublisher deadLetterPublisher;

    public WorkloadMessageListener(TrainerWorkloadService workloadService, DeadLetterPublisher deadLetterPublisher) {
        this.workloadService = workloadService;
        this.deadLetterPublisher = deadLetterPublisher;
    }

    @JmsListener(destination = "trainer.workload.queue")
    @SendTo("trainer.workload.return.queue")
    public WorkloadResponse receiveWorkloadEvent(WorkloadRequest request) {
        try {
            System.out.println("Received JMS workload for trainer: " + request.getTrainerUsername());
            return workloadService.processWorkload(request);
        } catch (Exception e) {
            deadLetterPublisher.sendToDlq(request,e);
        }
        return null;
    }

    @JmsListener(destination = "trainer.monthly-workload.queue")
    @SendTo("trainer.monthly-workload.return.queue")
    public Integer receiveMonthlyWorkloadRequest(MonthlyWorkloadRequest request) {
        try {
            System.out.println("Received JMS monthly workload request for trainer: " + request.getTrainerUsername());
            return workloadService.getMonthlyWorkload(request.getTrainerUsername(), request.getYear(), request.getMonth());
        } catch (Exception e) {
            deadLetterPublisher.sendToDlq(request,e);
        }
        return 0;
    }

}