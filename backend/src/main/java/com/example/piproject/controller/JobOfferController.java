package com.example.piproject.controller;

import com.example.piproject.DataExtraction.JobMatchingService;

import com.example.piproject.DataExtraction.CVParser;
import com.example.piproject.entity.JobOffer;
import com.example.piproject.repository.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        jobOffer.setRemuneration(jobOfferDetails.getRemuneration());
        jobOffer.setType(jobOfferDetails.getType());

        return jobOfferRepository.save(jobOffer);
    }

    @DeleteMapping("/{id}")
    public void deleteJobOffer(@PathVariable Long id) {
        jobOfferRepository.deleteById(id);
    }


    private File convertToFile(MultipartFile multipartFile) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + multipartFile.getOriginalFilename());

        System.out.println("📂 Saving file to: " + convFile.getAbsolutePath());

        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }

        if (!convFile.exists() || convFile.length() == 0) {
            System.out.println("❌ File conversion failed! File does not exist or is empty.");
        } else {
            System.out.println("✅ File saved successfully! Size: " + convFile.length() + " bytes.");
        }

        return convFile;
    }

    @PostMapping(value = "/recommend", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> recommendJobsFromCv(@RequestPart("cvFile") MultipartFile cvFile) throws IOException {

        Map<String, List<String>> extractedSections = CVParser.extractSections(convertToFile(cvFile));

        if (extractedSections.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "CV parsing failed"));
        }

        // ✅ Convert each section into a single string
        Map<String, String> processedSections = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : extractedSections.entrySet()) {
            processedSections.put(entry.getKey(), String.join(" ", entry.getValue()));
        }

        // ✅ Call AI matching
        Map<String, Object> recommendations = (Map<String, Object>) JobMatchingService.getRecommendedJobs(processedSections);

        return ResponseEntity.ok(recommendations);
    }





}

