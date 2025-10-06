package com.example.trainerworkloadservice.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WorkloadResponse {
    private String message;
    private int statusCode;

    public WorkloadResponse(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}