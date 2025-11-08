Feature: Trainer workload JMS message consumption

  Scenario: Process ADD workload message
    Given a valid ADD workload request message for trainer "john_doe"
    When the workload message listener processes it
    Then the trainer workload should be updated with 2 hours

  Scenario: Process DELETE workload message without going below zero
    Given a valid DELETE workload request message for trainer "john_doe" with current workload 1 hour
    When the workload message listener processes it
    Then the trainer workload should be updated to 0 hours