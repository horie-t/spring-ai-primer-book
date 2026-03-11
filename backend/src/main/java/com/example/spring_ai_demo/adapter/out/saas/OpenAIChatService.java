package com.example.spring_ai_demo.adapter.out.saas;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

@Service
public class OpenAIChatService {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;
    private final VectorStore vectorStore;

    private final SyncMcpToolCallbackProvider syncMcpToolCallbackProvider;

    public OpenAIChatService(ChatClient.Builder builder, ChatMemory chatMemory, VectorStore vectorStore,
                             SyncMcpToolCallbackProvider syncMcpToolCallbackProvider) {
        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
        this.vectorStore = vectorStore;
        this.syncMcpToolCallbackProvider = syncMcpToolCallbackProvider;
    }

    public String withUserMessage(String userMessage, boolean ragEnabled) {
        return chatClient.prompt()
                .advisors(advisorSpec -> {
                    if (ragEnabled) {
                        advisorSpec.advisors(
                                new SimpleLoggerAdvisor(),
                                MessageChatMemoryAdvisor.builder(chatMemory).build(),
                                QuestionAnswerAdvisor.builder(vectorStore)
                                        .searchRequest(SearchRequest.builder().build())
                                        .build());
                        advisorSpec.params(Map.of(
                                ChatMemory.CONVERSATION_ID, getCurrentUsername() + "-" + getCurrentSessionId(),
                                QuestionAnswerAdvisor.FILTER_EXPRESSION, "owner == '" + getCurrentUsername() + "'"));
                    } else {
                        advisorSpec.advisors(
                                new SimpleLoggerAdvisor(),
                                MessageChatMemoryAdvisor.builder(chatMemory).build());
                        advisorSpec.param(ChatMemory.CONVERSATION_ID, getCurrentUsername() + "-" + getCurrentSessionId());
                    }
                })
                .user(userMessage)
                .tools(new PetStoreTools())
                .toolContext(Map.of("JSESSIONID", getCurrentSessionId(), "username", getCurrentUsername()))
                .toolCallbacks(syncMcpToolCallbackProvider.getToolCallbacks())
                .call()
                .content();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String username) {
            return username;
        } else {
            return authentication.getName();
        }
    }

    private String getCurrentSessionId() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return "Context outside HTTP Request";
        }

        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        if (session != null) {
            return session.getId();
        } else {
            return "No active session in service layer";
        }
    }
}
