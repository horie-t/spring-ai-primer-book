package com.example.spring_ai_demo.application.domain.model;

public record ReviewAnalysisResult(
        String extractedIssues,
        String categorizedIssues,
        String improvementSuggestions
) {}
