package com.example.piproject.DataExtraction;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobMatchingService {
    private static final String FLASK_API_URL = "http://127.0.0.1:5000/match"; // Adjust if needed
    private static final String sFLASK_API_URL = "http://127.0.0.1:5000/recommend_jobs";
    private static final String PREDICT_GROWTH_URL = "http://127.0.0.1:5000/predict_growth"; // New


    public static Map<String, Object> matchCVWithJob(String jobDescription, Map<String, String> extractedCVSections) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("job_description", jobDescription);
        requestBody.put("cv_sections", extractedCVSections);

        // Create request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Send request to Flask API
        ResponseEntity<Map> response = restTemplate.exchange(FLASK_API_URL, HttpMethod.POST, request, Map.class);

        return response.getBody();
    }



    public static Map<String, Object> getRecommendedJobs(Map<String, String> extractedCVSections) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cv_sections", extractedCVSections);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                sFLASK_API_URL, HttpMethod.POST, request, Map.class
        );

        return response.getBody(); // ✅ return the whole map like: { recommended_jobs: [...] }
    }


    public static Map<String, Object> predictGrowth(List<Map<String, Object>> applications) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare request payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("applications", applications);

        // Create request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Send request to Flask API
        ResponseEntity<Map> response = restTemplate.exchange(
                PREDICT_GROWTH_URL, HttpMethod.POST, request, Map.class
        );

        return response.getBody(); // Should return { future_predictions: [...] }
    }

}
