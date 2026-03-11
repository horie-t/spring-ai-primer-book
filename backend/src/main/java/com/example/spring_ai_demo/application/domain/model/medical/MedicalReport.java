package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record MedicalReport(
        @JsonPropertyDescription("診断の要約")
        String diagnosisSummary,
        @JsonPropertyDescription("重要な所見のリスト")
        List<String> keyFindings,
        @JsonPropertyDescription("推奨される治療方針の詳細")
        String recommendedTreatment,
        @JsonPropertyDescription("注意事項とフォローアップ計画")
        String precautionsAndFollowUp
) {
}
