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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CVParser {


    private static final List<String> STOPWORDS = Arrays.asList("a", "an", "the", "with", "in", "on", "at", "for", "to", "and", "or", "of", "is", "are", "this", "that", "by");

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
    public static String extractText(File file) {
        String fileName = file.getName().toLowerCase();

        try {
            if (fileName.endsWith(".pdf")) {
                String pdfText = extractTextFromPDF(file);
                if (pdfText.trim().isEmpty()) {
                    return extractTextFromScannedPDF(file);  // OCR if text is empty
                }
                return pdfText;
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

    // 📌 Extract text from PDF using PDFBox
    private static String extractTextFromPDF(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);
            System.out.println("✅ Extracted PDF Text: " + extractedText);
            return extractedText.trim();
        }
    }

    // 📌 Extract text from Scanned PDF using OCR (Tesseract)
    private static String extractTextFromScannedPDF(File file) {
        try {
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:/Tesseract-OCR/tessdata"); // Adjust the path
            String extractedText = tesseract.doOCR(file);
            System.out.println("✅ Extracted OCR Text: " + extractedText);
            return extractedText.trim();
        } catch (TesseractException e) {
            System.out.println("❌ OCR failed: " + e.getMessage());
            return "";
        }
    }

    // 📌 Extract text from DOCX using Apache POI
    private static String extractTextFromDocx(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            List<String> paragraphs = document.getParagraphs()
                    .stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.toList());

            String extractedText = String.join("\n", paragraphs);
            System.out.println("✅ Extracted DOCX Text: " + extractedText);
            return extractedText.trim();
        }
    }

    // 📌 New: Extract Keywords from CV
    public static List<String> extractKeywords(String cvText) {
        return Arrays.stream(cvText.toLowerCase().split("\\W+"))
                .filter(word -> !STOPWORDS.contains(word))
                .map(CVParser::replaceSynonyms)
                .distinct()
                .collect(Collectors.toList());
    }

    private static String replaceSynonyms(String word) {
        return SYNONYM_MAP.getOrDefault(word, word);
    }


}


