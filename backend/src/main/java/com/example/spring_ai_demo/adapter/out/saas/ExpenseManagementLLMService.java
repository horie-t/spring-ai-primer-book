package com.example.spring_ai_demo.adapter.out.saas;

import com.example.spring_ai_demo.application.domain.model.AccountTitle;
import com.example.spring_ai_demo.application.domain.model.ExpenseAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExpenseManagementLLMService {
    private final ChatClient chatClient;

    @Value("classpath:prompt_templates/ExpenceClassificationSystemPrompt.txt")
    private Resource expenseClassificationSystemPrompt;

    @Value("classpath:prompt_templates/ExpenceClassificationPromptTemplate.txt")
    private Resource expenseClassificationPromptTemplate;


    public ExpenseManagementLLMService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ExpenseAnalysis classifyExpenses(String expense) {
        BeanOutputConverter<ExpenseAnalysis> converter = new BeanOutputConverter<>(ExpenseAnalysis.class);
        log.info(converter.getFormat());

        return chatClient.prompt()
                .system(expenseClassificationSystemPrompt)
                .user(promptUserSpec -> promptUserSpec
                        .text(expenseClassificationPromptTemplate)
                        .param("expense", expense))
                .call().entity(ExpenseAnalysis.class);
    }
}
