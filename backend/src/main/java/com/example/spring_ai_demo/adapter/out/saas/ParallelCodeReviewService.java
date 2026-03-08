package com.example.spring_ai_demo.adapter.out.saas;

import com.example.spring_ai_demo.application.domain.model.CodeReviewResult;
import com.example.spring_ai_demo.application.domain.model.VulnerabilityCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ParallelCodeReviewService {
    private final ChatClient chatClient;

    @Value("classpath:prompt_templates/CodeReviewSqlInjectionPrompt.txt")
    private Resource sqlInjectionPrompt;

    @Value("classpath:prompt_templates/CodeReviewAuthPrompt.txt")
    private Resource authPrompt;

    @Value("classpath:prompt_templates/CodeReviewXssPrompt.txt")
    private Resource xssPrompt;

    @Value("classpath:prompt_templates/CodeReviewInputValidationPrompt.txt")
    private Resource inputValidationPrompt;

    public ParallelCodeReviewService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * 複数の専門家の視点で並列にコードレビューを実行
     */
    public CodeReviewResult reviewCodeWithVoting(String code) {
        List<Resource> prompts = getReviewPrompts();

        // 各観点で並列にレビューを実行
        List<CompletableFuture<VulnerabilityCheck>> reviewFutures = prompts.stream()
                .map(prompt -> CompletableFuture.supplyAsync(() ->
                        performReview(code, prompt)
                ))
                .toList();

        // すべてのレビューが完了するまで待機
        CompletableFuture.allOf(reviewFutures.toArray(new CompletableFuture[0])).join();

        // 結果を収集
        List<VulnerabilityCheck> reviews = reviewFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 投票結果を集計
        boolean hasVulnerabilities = reviews.stream()
                .anyMatch(VulnerabilityCheck::hasVulnerability);

        String summary = generateSummary(reviews);

        return new CodeReviewResult(hasVulnerabilities, reviews, summary);
    }

    private List<Resource> getReviewPrompts() {
        return List.of(sqlInjectionPrompt, authPrompt, xssPrompt, inputValidationPrompt);
    }

    private VulnerabilityCheck performReview(String code, Resource prompt) {
        VulnerabilityCheck result = chatClient.prompt()
                .user(u -> u.text(prompt)
                        .param("code", code))
                .call()
                .entity(VulnerabilityCheck.class);

        return result;
    }

    private String generateSummary(List<VulnerabilityCheck> reviews) {
        long vulnerabilityCount = reviews.stream()
                .filter(VulnerabilityCheck::hasVulnerability)
                .count();

        if (vulnerabilityCount == 0) {
            return "すべてのレビュアーが問題を検出しませんでした。";
        }

        StringBuilder summary = new StringBuilder();
        summary.append(String.format("%d人中%d人のレビュアーが問題を検出しました。\n\n",
                reviews.size(), vulnerabilityCount));

        reviews.stream()
                .filter(VulnerabilityCheck::hasVulnerability)
                .forEach(review -> {
                    summary.append(String.format("【%s】\n", review.reviewerName()));
                    review.findings().forEach(finding ->
                            summary.append(String.format("- %s\n", finding))
                    );
                    summary.append("\n");
                });

        return summary.toString();
    }
}
