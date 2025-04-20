package com.example.piproject.services;

import com.example.piproject.entity.JobApplication;
import com.example.piproject.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
    public class ApplicationService {
        @Autowired
        private ApplicationRepository applicationRepository;


    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // Save a job application
    public JobApplication saveJobApplication(JobApplication jobApplication, MultipartFile cvFile) {
        try {
            jobApplication.setApplicationDate(new Date()); // Set date
            jobApplication.setStatus("Pending"); // Set default status

            if (cvFile != null && !cvFile.isEmpty()) {
                jobApplication.setCvFile(cvFile.getBytes()); // Save CV file as bytes
            }


            return applicationRepository.save(jobApplication);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }


    // Get a job application by ID
    public JobApplication getJobApplicationById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    // Get all job applications
    public List<JobApplication> getAllJobApplications() {
        return applicationRepository.findAll();
    }

    // Delete a job application
    public void deleteJobApplication(Long id) {
        applicationRepository.deleteById(id);
    }

    public byte[] findJobApplicationCV(Long id) {
        return applicationRepository.findCvById(id);
    }

    public void updateStatus(Long id, String status) {
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setStatus(status);
        applicationRepository.save(application);
        JobApplication saved = applicationRepository.findById(id).orElse(null);
        System.out.println("From DB after save: " + (saved != null ? saved.getStatus() : "null"));

    }


    }

