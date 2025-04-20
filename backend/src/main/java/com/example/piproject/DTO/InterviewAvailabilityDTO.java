package com.example.piproject.DTO;

import java.util.List;

public class InterviewAvailabilityDTO {
    private String candidateEmail;
    private List<String> slots; // List of slot dates
    public InterviewAvailabilityDTO() {} // ✅ No-args constructor


    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
    }

    public List<String> getSlots() { return slots; }
    public void setSlots(List<String> slots) { this.slots = slots; }}

