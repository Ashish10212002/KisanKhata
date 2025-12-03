package com.ashish.farm.controller;

import com.ashish.farm.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIChatController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> payload) {
        // 1. Get the message from React
        String userMessage = payload.get("message");
        
        // 2. Send it to your Gemini Service
        String aiResponse = aiService.getFarmingAdvice(userMessage);
        
        // 3. Return the answer to React
        return Map.of("response", aiResponse);
    }
}