package com.example.spring_ai_demo.adapter.in.web;

import com.example.spring_ai_demo.adapter.in.web.dto.PortfolioOptimizationRequest;
import com.example.spring_ai_demo.adapter.in.web.dto.PortfolioOptimizationResponse;
import com.example.spring_ai_demo.adapter.out.saas.PortfolioManagementService;
import com.example.spring_ai_demo.application.domain.model.OptimizedPortfolio;
import org.springframework.web.bind.annotation.*;

@RestController
public class PortfolioManagementController {
    private final PortfolioManagementService portfolioManagementService;

    public PortfolioManagementController(PortfolioManagementService portfolioManagementService) {
        this.portfolioManagementService = portfolioManagementService;
    }

    @PostMapping("/api/portfolio:optimize")
    public PortfolioOptimizationResponse optimizePortfolio(@RequestBody PortfolioOptimizationRequest request) {
        OptimizedPortfolio optimized = portfolioManagementService.optimizePortfolio(
                request.currentPortfolio(),
                request.riskTolerance(),
                request.investmentGoal(),
                request.marketCondition(),
                request.maxIterations() != null ? request.maxIterations() : 3
        );

        return new PortfolioOptimizationResponse(
                optimized.assetAllocation(),
                optimized.projectedRiskScore(),
                optimized.projectedReturn(),
                optimized.rationale()
        );
    }

}
