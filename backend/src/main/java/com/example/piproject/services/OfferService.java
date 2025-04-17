package com.example.piproject.services;

import com.example.piproject.entity.JobOffer;
import com.example.piproject.repository.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service

public class OfferService {
    @Autowired
    private JobOfferRepository jobOfferRepository;

    public List<JobOffer> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public JobOffer createJobOffer(JobOffer jobOffer) {
        return jobOfferRepository.save(jobOffer);
    }

    public Optional<JobOffer> getJobOfferById(Long id) {
        return jobOfferRepository.findById(id);
    }
    public String getJobOfferDescriptionById(JobOffer jobOffer) {
        return jobOffer.getDescription();
    }

    public void deleteJobOffer(Long id) {
        jobOfferRepository.deleteById(id);
    }

    public List<String> getJobKeywords(Long jobOfferId) {
        // Fetch job offer from the DB
        JobOffer jobOffer = jobOfferRepository.findById(jobOfferId).orElse(null);

        if (jobOffer != null && jobOffer.getKeywords() != null) {
            // Convert comma-separated keywords into a list
            return Arrays.asList(jobOffer.getKeywords().toLowerCase().split(","));
        }

        return List.of(); // Return empty list if no keywords found
    }

    public String getJobOfferDescription(Long jobOfferId) {
        return jobOfferRepository.findJobOfferDescriptionById(jobOfferId);
    }


}
