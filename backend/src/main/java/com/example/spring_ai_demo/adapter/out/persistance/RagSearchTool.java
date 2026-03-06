package com.example.spring_ai_demo.adapter.out.persistance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class RagSearchTool {
    private final VectorStore vectorStore;

    public RagSearchTool(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Tool(description = "社内の最新ドキュメントやマニュアルから情報を検索します。")
    public String searchInternalDocuments(@ToolParam(description = "検索クエリ。例：'出張旅費の精算方法について'") String query, ToolContext context) {
        String username = context.getContext().get("username").toString();

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(3)
                .filterExpression("owner == '" + username + "'")
                .build();

        return vectorStore.similaritySearch(searchRequest).stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));
    }
}
