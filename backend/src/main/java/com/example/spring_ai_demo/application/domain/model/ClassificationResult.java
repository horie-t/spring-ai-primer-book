package com.example.spring_ai_demo.application.domain.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record ClassificationResult(
        @JsonPropertyDescription("サポート問い合わせのカテゴリー")
        SupportCategory category,

        @JsonPropertyDescription("このカテゴリーに分類した理由の説明")
        String reasoning,

        @JsonPropertyDescription("分類の信頼度（0.0から1.0）")
        double confidence
) {}
