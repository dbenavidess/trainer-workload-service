package com.example.trainerworkloadservice.dto;

public class MonthlyWorkloadRequest {

    String trainerUsername;
    int year;
    int month;

    public MonthlyWorkloadRequest() {
    }

    public MonthlyWorkloadRequest(String trainerUsername, int year, int month) {
        this.trainerUsername = trainerUsername;
        this.year = year;
        this.month = month;
    }

    public String getTrainerUsername() {
        return trainerUsername;
    }

    public void setTrainerUsername(String username) {
        this.trainerUsername = username;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
