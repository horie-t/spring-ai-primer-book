package com.example.spring_ai_demo.adapter.out.saas;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    public OpenAIChatService(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
    }

    public String withUserMessage(String userMessage) {
        return chatClient.prompt()
                .advisors(new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build())
                .user(userMessage)
                .call()
                .content();
    }
}
