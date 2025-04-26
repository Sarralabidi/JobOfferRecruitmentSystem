package com.example.piproject.controller;

import com.example.piproject.DataExtraction.JobMatchingService;
import com.example.piproject.services.ApplicationService;
import com.example.piproject.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin-stat")
@CrossOrigin("http://localhost:4200")

public class StatController
{

    @Autowired
    private OfferService jobOfferService;
    @Autowired
    private ApplicationService applicationService;



    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAdminStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobOffers", jobOfferService.countAll());
        stats.put("openOffers", jobOfferService.countByStatus("Open"));
        System.out.println(stats.get("openOffers"));
        stats.put("closedOffers", jobOfferService.countByStatus("Closed"));

        stats.put("totalApplications", applicationService.countAll());
        stats.put("pendingApplications", applicationService.countByStatus("Pending"));
        stats.put("reviewedApplications", applicationService.countByStatus("Rejected"));
        stats.put("acceptedApplications", applicationService.countByStatus("Accepted"));

        stats.put("mostPopularJobTitle", applicationService.getMostAppliedJobOfferTitle());



        return ResponseEntity.ok(stats);
    }
    @GetMapping("/application-trend")
    public List<Map<String, Object>> getApplicationTrend() {
        return applicationService.countApplicationsPerDayLastMonth();
    }
    @GetMapping("/applications-by-job-title")
    public Map<String, Long> getApplicationsByJobTitle() {
        return applicationService.countApplicationsByJobTitle();
    }
    @GetMapping("/application-trend-prediction")
    public Map<String, Object> getApplicationTrendPrediction() {
        List<Map<String, Object>> applications = applicationService.countApplicationsPerDayLastMonth();

        // Now call Flask API through your service
        Map<String, Object> predictionResult = JobMatchingService.predictGrowth(applications);

        return predictionResult; // Return { future_predictions: [...] }
    }

}
