package com.example.spring_ai_demo.application.domain.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public enum SupportCategory {
    @JsonPropertyDescription("請求、支払い、インボイス、二重請求などの請求関連の問題。")
    BILLING("billing"),

    @JsonPropertyDescription("ログインできない、機能が動作しない、エラーメッセージなどの技術的な問題。")
    TECHNICAL("technical"),

    @JsonPropertyDescription("製品に関する質問、サービスの使い方、その他の一般的な問い合わせ。")
    GENERAL("general");

    private final String value;

    SupportCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SupportCategory fromString(String value) {
        for (SupportCategory category : SupportCategory.values()) {
            if (category.getValue().equalsIgnoreCase(value.trim())) {
                return category;
            }
        }
        return GENERAL; // デフォルト
    }
}
