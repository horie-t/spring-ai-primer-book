package com.example.spring_ai_demo.adapter.in.web.dto;

import java.util.Map;

public record PortfolioOptimizationResponse(
        Map<String, Double> optimizedAllocation,
        double projectedRiskScore,
        double projectedReturn,
        String rationale
) {
}
