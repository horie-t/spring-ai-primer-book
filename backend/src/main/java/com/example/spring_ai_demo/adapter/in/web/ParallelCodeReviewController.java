package com.example.spring_ai_demo.adapter.in.web;

import com.example.spring_ai_demo.adapter.in.web.dto.CodeRequest;
import com.example.spring_ai_demo.adapter.out.saas.ParallelCodeReviewService;
import com.example.spring_ai_demo.application.domain.model.CodeReviewResult;
import org.springframework.web.bind.annotation.*;

@RestController
public class ParallelCodeReviewController {
    private final ParallelCodeReviewService votingService;

    public ParallelCodeReviewController(ParallelCodeReviewService votingService) {
        this.votingService = votingService;
    }

    @PostMapping("/api/code:review")
    public CodeReviewResult review(@RequestBody CodeRequest request) {
        return votingService.reviewCodeWithVoting(request.code());
    }
}
