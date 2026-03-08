package com.example.spring_ai_demo.adapter.out.saas;

import com.example.spring_ai_demo.application.domain.model.ClassificationResult;
import com.example.spring_ai_demo.application.domain.model.SupportCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class CustomerSupportRoutingService {
    private final ChatClient chatClient;

    @Value("classpath:prompt_templates/CustomerSupportClassificationPrompt.txt")
    private Resource classificationPrompt;

    @Value("classpath:prompt_templates/CustomerSupportBillingSystemPrompt.txt")
    private Resource billingSystemPrompt;

    @Value("classpath:prompt_templates/CustomerSupportTechnicalSystemPrompt.txt")
    private Resource technicalSystemPrompt;

    @Value("classpath:prompt_templates/CustomerSupportGeneralSystemPrompt.txt")
    private Resource generalSystemPrompt;

    public CustomerSupportRoutingService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String route(String userInput) {
        // ステップ1: 入力を分類
        ClassificationResult classification = classify(userInput);
        // ステップ2: 分類に応じた専門プロンプトを取得
        Resource specializedPrompt = getSpecializedPrompt(classification.category());
        // ステップ3: 専門プロンプトで処理
        String response = process(userInput, specializedPrompt);

        return response;
    }

    private ClassificationResult classify(String input) {
        return chatClient.prompt()
                .user(u -> u.text(classificationPrompt)
                        .param("input", input))
                .call()
                .entity(ClassificationResult.class);
    }

    private Resource getSpecializedPrompt(SupportCategory category) {
        return switch (category) {
            case BILLING -> billingSystemPrompt;
            case TECHNICAL -> technicalSystemPrompt;
            case GENERAL -> generalSystemPrompt;
        };
    }

    private String process(String input, Resource systemPrompt) {
        return chatClient.prompt()
                .system(systemPrompt)
                .user(input)
                .call()
                .content();
    }

}
