package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record DrugInteractionCheck(
        @JsonPropertyDescription("検出された薬物相互作用のリスト")
        List<Interaction> interactions,
        @JsonPropertyDescription("全体的なリスクレベル（低/中/高）")
        String overallRiskLevel,
        @JsonPropertyDescription("推奨される対応事項")
        List<String> recommendations
) {
    public record Interaction(
            @JsonPropertyDescription("薬剤1の名称")
            String drug1,
            @JsonPropertyDescription("薬剤2の名称")
            String drug2,
            @JsonPropertyDescription("相互作用の重症度（軽度/中等度/重度）")
            String severity,
            @JsonPropertyDescription("相互作用の詳細説明")
            String description,
            @JsonPropertyDescription("推奨される対応")
            String action
    ) {
    }
}
