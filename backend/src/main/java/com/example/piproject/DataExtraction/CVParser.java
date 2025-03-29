package com.example.piproject.DataExtraction;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class CVParser {

    private static final List<String> STOPWORDS = Arrays.asList("a", "an", "the", "with", "in", "on", "at", "for", "to", "and", "or", "of", "is", "are", "this", "that", "by", "com", "phone", "example", "email");

    private static final Map<String, String> SYNONYM_MAP = new HashMap<>();
    static {
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

    public static Map<String, List<String>> extractSections(File file) {
        String extractedText = extractText(file);
        return parseSections(extractedText);
    }

    // 📌 Extract text from a CV file
    private static String extractText(File file) {
        String fileName = file.getName().toLowerCase();

        try {
            if (fileName.endsWith(".pdf")) {
                String pdfText = extractTextFromPDF(file);
                return pdfText.trim().isEmpty() ? extractTextFromScannedPDF(file) : pdfText;
            } else if (fileName.endsWith(".docx")) {
                return extractTextFromDocx(file);
            } else {
                System.out.println("❌ Unsupported file format: " + fileName);
                return "";
            }
        } catch (Exception e) {
            System.out.println("❌ Error extracting text: " + e.getMessage());
            return "";
        }
    }

    // 📌 Extract text from PDF
    private static String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        }
    }

    // 📌 Extract text from Scanned PDF using OCR
    private static String extractTextFromScannedPDF(File file) {
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Tesseract-OCR/tessdata"); // Adjust the path
            return tesseract.doOCR(file).trim();
        } catch (TesseractException e) {
            System.out.println("❌ OCR failed: " + e.getMessage());
            return "";
        }
    }

    // 📌 Extract text from DOCX
    private static String extractTextFromDocx(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            return document.getParagraphs()
                    .stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.joining("\n")).trim();
        }
    }

    // 📌 Extract Sections from the CV
    private static Map<String, List<String>> parseSections(String cvText) {
        Map<String, List<String>> sectionKeywords = new HashMap<>();

        Pattern sectionPattern = Pattern.compile("(?i)(Skills|Experience|Certifications|Education)[:\\n]");
        Matcher matcher = sectionPattern.matcher(cvText);

        int lastIndex = 0;
        String lastSection = "Other"; // Default section

        while (matcher.find()) {
            String section = matcher.group(1);
            int sectionStart = matcher.start();

            // Old : Extract text as keywords of the previous section
            //String sectionText = cvText.substring(lastIndex, sectionStart);
            //sectionKeywords.put(lastSection, extractKeywords(sectionText));

            // New : Extract meaningful section text instead of just keywords
            String sectionText = cvText.substring(lastIndex, sectionStart).trim();
            if (!sectionText.isEmpty()) {
                sectionKeywords.put(lastSection, Arrays.asList(extractSectionText(sectionText)));
            } else {
                sectionKeywords.put(lastSection, Arrays.asList("No details provided.")); // Avoid empty sections
            }


            lastSection = section;
            lastIndex = sectionStart;
        }

        // Extract last section
        String remainingText = cvText.substring(lastIndex);
        sectionKeywords.put(lastSection, extractKeywords(remainingText));

        return sectionKeywords;
    }

    // 📌 Extract Keywords
    static List<String> extractKeywords(String text) {
        return Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(word -> !STOPWORDS.contains(word))
                .map(CVParser::replaceSynonyms)
                .distinct()
                .collect(Collectors.toList());
    }
    static String extractSectionText(String text) {
        return text.replaceAll("[\\n\\r]+", " ").trim();  // ✅ Keep full sentences, remove extra newlines
    }

    private static String replaceSynonyms(String word) {
        return SYNONYM_MAP.getOrDefault(word, word);
    }


}
