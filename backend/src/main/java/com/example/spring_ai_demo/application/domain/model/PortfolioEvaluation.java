package com.example.spring_ai_demo.application.domain.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record PortfolioEvaluation(
        @JsonPropertyDescription("ポートフォリオのリスクレベル（0=低リスク, 10=高リスク）")
        double riskScore,

        @JsonPropertyDescription("年間期待リターン率（パーセンテージ）")
        double expectedReturn,

        @JsonPropertyDescription("資産分散の適切性スコア（0=不適切, 10=非常に良好）")
        double diversificationScore,

        @JsonPropertyDescription("現在の市場環境との整合性（良好/普通/不良）")
        String marketAlignment,

        @JsonPropertyDescription("ポートフォリオ改善のための具体的な推奨事項")
        String recommendation
) {
}
