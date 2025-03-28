package com.example.piproject.DataExtraction;

import java.util.HashMap;
import java.util.Map;

public class SynonymMapper {
    private static final Map<String, String> SYNONYM_MAP = new HashMap<>();

    static {
        // Fitness-related synonyms
        SYNONYM_MAP.put("trainer", "coach");
        SYNONYM_MAP.put("fitness", "exercise");
        SYNONYM_MAP.put("strength training", "weightlifting");
        SYNONYM_MAP.put("cardio", "aerobics");
        SYNONYM_MAP.put("yoga", "flexibility training");
        SYNONYM_MAP.put("dietitian", "nutritionist");
        SYNONYM_MAP.put("health expert", "wellness coach");
        SYNONYM_MAP.put("gym instructor", "personal trainer");
        SYNONYM_MAP.put("athletics", "sports");
    }

    public static String getSynonym(String word) {
        return SYNONYM_MAP.getOrDefault(word, word);
    }
}

