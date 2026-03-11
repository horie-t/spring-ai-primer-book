package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record LabResultsInterpretation(
        @JsonPropertyDescription("異常値のリスト")
        List<AbnormalFinding> abnormalFindings,
        @JsonPropertyDescription("検査結果の臨床的意義")
        String clinicalSignificance,
        @JsonPropertyDescription("推奨される追加検査やフォローアップ")
        List<String> recommendedFollowUp
) {
    public record AbnormalFinding(
            @JsonPropertyDescription("検査項目名")
            String testName,
            @JsonPropertyDescription("測定値")
            String value,
            @JsonPropertyDescription("基準値範囲")
            String referenceRange,
            @JsonPropertyDescription("異常の重症度（軽度/中等度/重度）")
            String severity
    ) {
    }
}
