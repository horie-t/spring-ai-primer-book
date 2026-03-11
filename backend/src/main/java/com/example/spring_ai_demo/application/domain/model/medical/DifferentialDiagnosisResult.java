package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record DifferentialDiagnosisResult(
        @JsonPropertyDescription("鑑別診断のリスト（確率の高い順）")
        List<Diagnosis> diagnoses,
        @JsonPropertyDescription("除外すべき重篤な疾患")
        List<String> redFlags
) {
    public record Diagnosis(
            @JsonPropertyDescription("疾患名")
            String condition,
            @JsonPropertyDescription("可能性の高さ（高/中/低）")
            String probability,
            @JsonPropertyDescription("診断を支持する根拠となる症状・所見")
            List<String> supportingEvidence,
            @JsonPropertyDescription("確定診断に必要な追加検査")
            List<String> additionalTests
    ) {
    }
}
