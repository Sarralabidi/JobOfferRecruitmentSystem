package com.example.piproject.controller;

import com.example.piproject.DTO.InterviewAvailabilityDTO;
import com.example.piproject.DataExtraction.CVParser;
import com.example.piproject.DataExtraction.JobMatchingService;
import com.example.piproject.entity.JobApplication;
import com.example.piproject.entity.JobOffer;
import com.example.piproject.repository.JobOfferRepository;
import com.example.piproject.services.ApplicationService;
import com.example.piproject.services.EmailService;
import com.example.piproject.services.MailerSendService;
import com.example.piproject.services.OfferService;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class JobApplicationController {
    @Autowired
    private ApplicationService jobApplicationService;
    @Autowired  // ✅ This ensures Spring injects the service automatically

    private OfferService jobService;
    @Autowired
    JobOfferRepository jobRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    private MailerSendService mailerSendService;
    public JobApplicationController(ApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    // Create a new job application
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JobApplication> createJobApplication(
            @RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("coverLetter") String coverLetter,
            @RequestParam("jobOfferId") Long jobOfferId,
            @RequestParam("userId") Long userId,
            @RequestPart("cvFile") MultipartFile cvFile) throws IOException, TikaException {

        // Create the JobApplication object manually
        JobApplication jobApplication = new JobApplication();

        jobApplication.setFullName(fullName);
        jobApplication.setEmail(email);
        jobApplication.setCoverLetter(coverLetter);
        jobApplication.setJobOffer(new JobOffer(jobOfferId)); // Assuming JobOffer has a constructor with ID
        jobApplication.setUserId(userId);
        jobApplication.setApplicationDate(new Date());
        jobApplication.setStatus("Pending");
        // Extract text from CV
        //String cvText = CVParser.extractText(convertToFile(cvFile));
        //Extract sections from cv
        //String cvText= CVParser.extractSections(convertToFile( cvFile)).toString();

        Map<String, List<String>> extractedSections = CVParser.extractSections(convertToFile(cvFile));

// Retrieve Job Description from Database
        String jobDescription = jobService.getJobOfferDescription(jobOfferId);
        System.out.println("the selected job desc is " + jobDescription);
        if (jobDescription == null || jobDescription.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
// Convert List<String> sections into a single string per section
        Map<String, String> processedSections = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : extractedSections.entrySet()) {
            processedSections.put(entry.getKey(), String.join(" ", entry.getValue())); // Convert list to single string
        }

// Send cleaned data to AI Matching Service
        // Call AI Matching Service and get the response
        Map<String, Object> matchResults = JobMatchingService.matchCVWithJob(jobDescription, processedSections);

// Extract global match score from the response
        if (matchResults.containsKey("global_match_score")) {
            double matchScore = (double) matchResults.get("global_match_score");  // Convert to double
            jobApplication.setMatchPercentage(matchScore);  // Save it to the database
            System.out.println("match score: " + matchScore);
        } else {
            System.out.println("Error: global_match_score not found in the response.");
        }

// Send to AI Matching Service didnt work because its list
        //Map<String, Object> matchResults = JobMatchingService.matchCVWithJob(jobDescription, extractedSections);

        System.out.println("🔍 AI Match Results: " + matchResults);


// 🔍 Debugging Output
        //System.out.println("📄 Extracted CV Text: [" + extractedSections + "]");

        if (extractedSections.isEmpty()) {
            //System.out.println("❌ Extraction failed! CV text is empty.");
        }
        // Get job requirements
        List<String> jobKeywords = jobService.getJobKeywords(jobOfferId);


        // Calculate match percentage
        //double matchScore = CVMatcher.calculateMatch(extractedSections.toString(), jobKeywords);//cvText houni taawadhha b jdid

        // Call the service method with jobApplication and cvFile
        JobApplication savedApplication = jobApplicationService.saveJobApplication(jobApplication, cvFile);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
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

    // Get a job application by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getJobApplicationById(@PathVariable Long id) {
        JobApplication jobApplication = jobApplicationService.getJobApplicationById(id);
        return (jobApplication != null) ? ResponseEntity.ok(jobApplication) : ResponseEntity.notFound().build();
    }




    // Get all job applications
    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllJobApplications() {
        List<JobApplication> applications = jobApplicationService.getAllJobApplications();
        return ResponseEntity.ok(applications);
    }

    // Delete a job application
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJobApplication(@PathVariable Long id) {
        jobApplicationService.deleteJobApplication(id);
        return ResponseEntity.ok("Job application deleted successfully!");
    }


    @GetMapping("/cv/{id}")
    public ResponseEntity<byte[]> getJobApplicationCV(@PathVariable("id")  Long id) {
        byte[] cv = jobApplicationService.findJobApplicationCV(id);

        if (cv != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(ContentDisposition.attachment().filename("cv.pdf").build());
            return new ResponseEntity<>(cv, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        jobApplicationService.updateStatus(id, status);
        System.out.println("job app"+id+"status updated to "+status);
        return ResponseEntity.ok().build();
    }




        @PostMapping("/confirm-slot")
        public ResponseEntity<Map<String, String>> confirmSlot(@RequestBody InterviewAvailabilityDTO request) {
            String email = request.getCandidateEmail();
            List<String> selectedSlot = request.getSlots();
            System.out.println("Payload received -> email: " + email + " slots: " + selectedSlot);
            System.out.println("DTO raw -> " + request);
            System.out.println("Saving confirmed slot for " + email + " at " + selectedSlot);
            String subject = "Interview Slot Confirmed";
            String body = "Hello,\n\nYour interview slot is confirmed for: " + selectedSlot + "\n\nBest of luck!";

            // ✅ Convert String dates to ZonedDateTime
            List<ZonedDateTime> convertedSlots = selectedSlot.stream()
                    .map(ZonedDateTime::parse)
                    .collect(Collectors.toList());

            System.out.println("✅ Converted slots: " + convertedSlots);

            // ✉️ Send email with .ics attached
            try {
                emailService.sendInterviewInviteWithICS(email, convertedSlots);
            } catch (Exception e) {
                System.out.println("❌ Failed to send email: " + e.getMessage());
                return ResponseEntity.status(500)
                        .body(Collections.singletonMap("message", "Failed to send email with calendar invite."));
            }

            return ResponseEntity.ok(Collections.singletonMap("message", "Interview slot confirmed and email sent!"));
        }


}





