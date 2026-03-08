package com.example.spring_ai_demo.adapter.in.web;

import com.example.spring_ai_demo.adapter.out.saas.ReviewAnalysisLLMService;
import com.example.spring_ai_demo.application.domain.model.ReviewAnalysisResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewAnalysisController {
    private final ReviewAnalysisLLMService reviewAnalysisLLMService;

    public ReviewAnalysisController(ReviewAnalysisLLMService reviewAnalysisLLMService) {
        this.reviewAnalysisLLMService = reviewAnalysisLLMService;
    }

    @PostMapping("/api/review:analyze")
    public ReviewAnalysisResult analyzeReview(@RequestBody String reviewText) {
        return reviewAnalysisLLMService.analyzeReview(reviewText);
    }
}
