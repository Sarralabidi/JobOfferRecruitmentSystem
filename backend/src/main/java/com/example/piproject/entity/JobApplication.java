package com.example.piproject.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")  // Foreign key linking to JobOffer
    private JobOffer jobOffer;

    private Long userId;
    private Date applicationDate;
    private String status; // Pending, Accepted, Rejected

    private double matchPercentage; // 🔹 New field

    private String fullName;
    private String email;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String coverLetter;

    @Lob
    @Column(name = "cv_file",columnDefinition = "LONGBLOB")
    private byte[] cvFile; // Store as binary data OR use a file path

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public byte[] getCvFile(byte[] cvData) {
        return cvFile;
    }

    public void setCvFile(byte[] cvFile) {
        this.cvFile = cvFile;
    }

    public void setStatus(String pending) {
    }

    public void setApplicationDate(Date date) {
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }

    public void setJobOffer(JobOffer jobOffer) {
        this.jobOffer = jobOffer;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public String getStatus() {
        return status;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }
}
