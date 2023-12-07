package com.example.schedulewozniak;

public class EmployeeAvailability {
    private String day;
    private boolean morningAvailable;
    private boolean eveningAvailable;

    public EmployeeAvailability(String day, boolean morningAvailable, boolean eveningAvailable) {
        this.day = day;
        this.morningAvailable = morningAvailable;
        this.eveningAvailable = eveningAvailable;
    }


    public EmployeeAvailability() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isMorningAvailable() {
        return morningAvailable;
    }

    public void setMorningAvailable(boolean morningAvailable) {
        this.morningAvailable = morningAvailable;
    }

    public boolean isEveningAvailable() {
        return eveningAvailable;
    }

    public void setEveningAvailable(boolean eveningAvailable) {
        this.eveningAvailable = eveningAvailable;
    }
}
