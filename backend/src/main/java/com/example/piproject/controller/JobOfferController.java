package com.example.piproject.controller;


import com.example.piproject.entity.JobOffer;
import com.example.piproject.repository.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
    @RequestMapping("/api/job-offers")
@CrossOrigin("http://localhost:4200")
    public class JobOfferController {
        @Autowired
        private JobOfferRepository jobOfferRepository;

        @GetMapping
        public List<JobOffer> getAllJobOffers() {
            return jobOfferRepository.findAll();
        }

    @PostMapping

    public JobOffer createJobOffer(@RequestBody JobOffer jobOffer) {
            return jobOfferRepository.save(jobOffer);
        }
    @GetMapping("/{id}")
    public Optional<JobOffer> getJobOfferById(@PathVariable Long id) {
        return jobOfferRepository.findById(id);
    }

    @PutMapping("/{id}")
    public JobOffer updateJobOffer(@PathVariable Long id, @RequestBody JobOffer jobOfferDetails) {
        JobOffer jobOffer = jobOfferRepository.findById(id).orElseThrow();
        jobOffer.setTitle(jobOfferDetails.getTitle());
        jobOffer.setDescription(jobOfferDetails.getDescription());
        jobOffer.setCompany(jobOfferDetails.getCompany());
        jobOffer.setLocation(jobOfferDetails.getLocation());
        jobOffer.setStatus(jobOfferDetails.getStatus());

        return jobOfferRepository.save(jobOffer);
    }

    @DeleteMapping("/{id}")
    public void deleteJobOffer(@PathVariable Long id) {
        jobOfferRepository.deleteById(id);
    }


    }

