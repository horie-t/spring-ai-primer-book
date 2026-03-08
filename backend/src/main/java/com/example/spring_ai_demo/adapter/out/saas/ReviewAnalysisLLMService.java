package com.example.spring_ai_demo.adapter.out.saas;

import com.example.spring_ai_demo.application.domain.model.ReviewAnalysisResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ReviewAnalysisLLMService {
    private final ChatClient chatClient;

    @Value("classpath:prompt_templates/ReviewIssueExtractionPrompt.txt")
    private Resource issueExtractionPrompt;

    @Value("classpath:prompt_templates/ReviewIssueCategorizePrompt.txt")
    private Resource issueCategorizePrompt;

    @Value("classpath:prompt_templates/ReviewImprovementSuggestionPrompt.txt")
    private Resource improvementSuggestionPrompt;

    public ReviewAnalysisLLMService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ReviewAnalysisResult analyzeReview(String reviewText) {
        // ステップ1: レビューから重要な問題点を抽出
        String extractedIssues = chatClient.prompt()
                .user(u -> u.text(issueExtractionPrompt)
                        .param("reviewText", reviewText))
                .call()
                .content();

        // ステップ2: 抽出された問題点をカテゴリー分類
        String categorizedIssues = chatClient.prompt()
                .user(u -> u.text(issueCategorizePrompt)
                        .param("issues", extractedIssues))
                .call()
                .content();

        // ステップ3: 分類された問題点に基づいて改善提案を生成
        String improvementSuggestions = chatClient.prompt()
                .user(u -> u.text(improvementSuggestionPrompt)
                        .param("categorizedIssues", categorizedIssues))
                .call()
                .content();

        return new ReviewAnalysisResult(extractedIssues, categorizedIssues, improvementSuggestions);
    }
}
