package com.example.spring_ai_demo.adapter.in.web.dto;

import java.util.Map;

public record PortfolioOptimizationRequest(
        Map<String, Double> currentPortfolio,
        String riskTolerance,
        String investmentGoal,
        String marketCondition,
        Integer maxIterations
) {
}
