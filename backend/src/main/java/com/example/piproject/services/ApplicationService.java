package com.example.piproject.services;

import com.example.piproject.entity.JobApplication;
import com.example.piproject.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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


    public Object countByStatus(String pending) {
        return applicationRepository.countAllByStatusIsLike(pending);

    }

    public Object countAll() {
        return (int) applicationRepository.count();
    }
    public String getMostAppliedJobOfferTitle() {
        return applicationRepository.findTitleOfMostAppliedJobOffer()
                ;
    }

    public List<Map<String, Object>> countApplicationsPerDayLastMonth() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(29); // include today
        java.sql.Date startDate = java.sql.Date.valueOf(thirtyDaysAgo);

        List<Object[]> rawResults = applicationRepository.countApplicationsGroupedByDay(startDate);

        Map<String, Long> dateToCountMap = new HashMap<>();
        for (Object[] row : rawResults) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            Long count = (Long) row[1];
            dateToCountMap.put(date.toString(), count);
        }

        List<Map<String, Object>> results = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 29; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Map<String, Object> entry = new HashMap<>();
            entry.put("date", date.toString());
            entry.put("count", dateToCountMap.getOrDefault(date.toString(), 0L));
            results.add(entry);
        }

        return results;
    }




    public Map<String, Long> countApplicationsByJobTitle() {
        List<Object[]> results = applicationRepository.countApplicationsGroupedByJobTitle();
        Map<String, Long> jobTitleCounts = new HashMap<>();
        for (Object[] result : results) {
            String jobTitle = (String) result[0];
            Long count = (Long) result[1];
            jobTitleCounts.put(jobTitle, count);
        }
        return jobTitleCounts;
    }

}

