package com.example.spring_ai_demo.application.domain.model;

import java.util.List;

public record CodeReviewResult(
        boolean hasVulnerabilities,
        List<VulnerabilityCheck> reviews,
        String summary
) {}
