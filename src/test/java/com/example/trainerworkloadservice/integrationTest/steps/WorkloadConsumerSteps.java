package com.example.trainerworkloadservice.integrationTest.steps;

import com.example.trainerworkloadservice.dto.ActionType;
import com.example.trainerworkloadservice.dto.WorkloadRequest;
import com.example.trainerworkloadservice.messaging.DeadLetterPublisher;
import com.example.trainerworkloadservice.messaging.WorkloadMessageListener;
import com.example.trainerworkloadservice.service.TrainerWorkloadService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class WorkloadConsumerSteps {

    private TrainerWorkloadService mockService;
    private WorkloadMessageListener listener;
    private WorkloadRequest request;
    private DeadLetterPublisher deadLetterPublisher;

    @Given("a valid ADD workload request message for trainer {string}")
    public void givenValidAddMessage(String username) {
        mockService = mock(TrainerWorkloadService.class);
        deadLetterPublisher = mock(DeadLetterPublisher.class);
        listener = new WorkloadMessageListener(mockService,deadLetterPublisher);

        request = new WorkloadRequest();
        request.setTrainerUsername(username);
        request.setTrainerFirstName("John");
        request.setTrainerLastName("Doe");
        request.setIsActive(true);
        request.setTrainingDate(LocalDate.of(2024, 6, 15));
        request.setTrainingDuration(2);
        request.setActionType(ActionType.ADD);
    }

    @When("the workload message listener processes it")
    public void whenMessageProcessed() {
        listener.receiveWorkloadEvent(request);
    }

    @Then("the trainer workload should be updated with {int} hours")
    public void thenWorkloadUpdated(int expectedHours) {
        ArgumentCaptor<WorkloadRequest> captor = ArgumentCaptor.forClass(WorkloadRequest.class);
        verify(mockService, times(1)).processWorkload(captor.capture());

        WorkloadRequest processed = captor.getValue();
        assert processed.getTrainingDuration() == expectedHours;
    }

    @Given("a valid DELETE workload request message for trainer {string} with current workload {int} hour")
    public void givenValidDeleteMessage(String username, int currentHours) {
        mockService = mock(TrainerWorkloadService.class);

        // Simulate current workload
        Map<Integer, Map<Integer, Integer>> summary = new HashMap<>();
        summary.put(2024, new HashMap<>(Map.of(6, currentHours)));

        // Mock service behavior
        when(mockService.getMonthlyWorkload(username, 2024, 6)).thenReturn(currentHours);

        listener = new WorkloadMessageListener(mockService,deadLetterPublisher);

        request = new WorkloadRequest(username, "John", "Doe", true,
                LocalDate.of(2024, 6, 15), 5, ActionType.DELETE);
    }

    @Then("the trainer workload should be updated to {int} hours")
    public void thenWorkloadUpdatedTo(int expectedHours) {
        verify(mockService).processWorkload(request);
    }
}