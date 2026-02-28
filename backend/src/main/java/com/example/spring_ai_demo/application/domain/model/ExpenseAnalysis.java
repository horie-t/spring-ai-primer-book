package com.example.spring_ai_demo.application.domain.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record ExpenseAnalysis(
        @JsonPropertyDescription("支出の内容から推論された最も適切な勘定科目")
        AccountTitle accountTitle,

        @JsonPropertyDescription("その勘定科目を選んだ理由の簡潔な説明")
        String reasoning
) {}