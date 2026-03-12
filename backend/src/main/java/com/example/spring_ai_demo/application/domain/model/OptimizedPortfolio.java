package com.example.spring_ai_demo.application.domain.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.Map;

public record OptimizedPortfolio(
        @JsonPropertyDescription("最適化された資産配分。資産名をキー、配分割合（%）を値とするマップ。合計は100%")
        Map<String, Double> assetAllocation,

        @JsonPropertyDescription("最適化後の予想リスクスコア（0=低リスク, 10=高リスク）")
        double projectedRiskScore,

        @JsonPropertyDescription("最適化後の予想年間リターン率（パーセンテージ）")
        double projectedReturn,

        @JsonPropertyDescription("最適化を行った根拠と期待される効果の説明")
        String rationale
) {
}
