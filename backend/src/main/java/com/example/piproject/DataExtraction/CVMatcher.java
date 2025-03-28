package com.example.piproject.DataExtraction;

import org.apache.commons.text.similarity.CosineSimilarity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CVMatcher {
    public static double calculateMatch(String cvText, List<String> jobKeywords) {
        List<String> cvKeywords = CVParser.extractKeywords(cvText);
        System.out.println("those are cv key words "+cvKeywords);
        Map<CharSequence, Integer> cvVector = getFrequencyVector(String.join(" ", cvKeywords));
        System.out.println("those are cv vector "+cvVector);
        Map<CharSequence, Integer> jobVector = getFrequencyVector(String.join(" ", jobKeywords));
        System.out.println("those are job vector "+jobVector);

        CosineSimilarity cosine = new CosineSimilarity();
        return cosine.cosineSimilarity(cvVector, jobVector) * 100; // Convert to percentage
    }

    private static Map<CharSequence, Integer> getFrequencyVector(String text) {
        Map<CharSequence, Integer> freqMap = new HashMap<>();
        for (String word : text.toLowerCase().split(" ")) {
            freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
        }
        return freqMap;
    }
}
