package com.ashish.farm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIService {

    // Inject API key from environment variable
    @Value("${gemini.api.key}")
    private String apiKey;

    // Base URL without the key
    private static final String BASE_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public String getFarmingAdvice(String userQuestion) {
        try {
            System.out.println("AI Service: Sending request to Gemini 2.0 Flash...");

            // --- 1. Build final URL (key added here) ---
            String finalUrl = BASE_URL + "?key=" + apiKey;

            // --- 2. Prepare AI Prompt ---
            String prompt = "You are 'Kisan Sahayak', an expert AI assistant for Indian farmers. " +
                    "Follow these strict rules:\n" +
                    "1. ANSWER ONLY IN HINDI (Devanagari Script). Do not use English characters.\n" +
                    "2. SCOPE: Answer ONLY questions related to Agriculture, Crops, Fertilizers, Pests, Weather, and Farming Schemes.\n" +
                    "3. RESTRICTION: If the user asks about anything else, politely refuse in Hindi.\n" +
                    "4. TONE: Be respectful, simple, and practical.\n" +
                    "5. FORMAT: Keep the answer short (under 60 words).\n\n" +
                    "User Question: " + userQuestion;

            Map<String, Object> part = new HashMap<>();
            part.put("text", prompt);

            Map<String, Object> content = new HashMap<>();
            content.put("parts", List.of(part));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", List.of(content));

            // --- 3. Send Request ---
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(finalUrl, entity, Map.class);

            // --- 4. Parse Response ---
            Map<String, Object> body = response.getBody();

            if (body == null || !body.containsKey("candidates")) {
                return "Google sent an empty response.";
            }

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) body.get("candidates");
            if (candidates.isEmpty()) {
                return "I couldn't generate an answer. (Safety Filter triggered?)";
            }

            Map<String, Object> firstCandidate = candidates.get(0);
            Map<String, Object> contentResponse = (Map<String, Object>) firstCandidate.get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentResponse.get("parts");

            String answer = (String) parts.get(0).get("text");

            System.out.println("AI Service: Success! Answer: " + answer);

            return answer;

        } catch (HttpClientErrorException e) {
            System.err.println("GOOGLE API ERROR: " + e.getResponseBodyAsString());
            return "API Error. Check Java Console.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Internal Server Error.";
        }
    }
}
