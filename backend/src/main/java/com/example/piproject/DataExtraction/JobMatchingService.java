package com.example.piproject.DataExtraction;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class JobMatchingService {
    private static final String FLASK_API_URL = "http://127.0.0.1:5000/match"; // Adjust if needed

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
}
