package com.example.spring_ai_demo.adapter.out.saas;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAIChatService {
    private final ChatClient chatClient;

    public OpenAIChatService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String withUserMessage(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}
