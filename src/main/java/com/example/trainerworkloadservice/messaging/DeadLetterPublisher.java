package com.example.trainerworkloadservice.messaging;

import com.example.trainerworkloadservice.dto.MonthlyWorkloadRequest;
import com.example.trainerworkloadservice.dto.WorkloadRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterPublisher {

    private final JmsTemplate jmsTemplate;

    public DeadLetterPublisher(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendToDlq(WorkloadRequest request, Exception e) {
        jmsTemplate.convertAndSend("ActiveMQ.DLQ", request, message -> {
            message.setStringProperty("_typeId", WorkloadRequest.class.getName());
            message.setStringProperty("error", e.getMessage());
            return message;
        });
        System.out.println("Sent failed message to DLQ: " + request.getTrainerUsername());
    }

    public void sendToDlq(MonthlyWorkloadRequest request, Exception e) {
        jmsTemplate.convertAndSend("ActiveMQ.DLQ", request, message -> {
            message.setStringProperty("_typeId", WorkloadRequest.class.getName());
            message.setStringProperty("error", e.getMessage());
            return message;
        });
        System.out.println("Sent failed message to DLQ: " + request.getTrainerUsername());
    }
}