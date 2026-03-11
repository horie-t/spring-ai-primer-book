package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record TreatmentPlan(
        @JsonPropertyDescription("推奨される薬物療法（薬剤名と用法）")
        List<String> medications,
        @JsonPropertyDescription("必要な処置・手技")
        List<String> procedures,
        @JsonPropertyDescription("生活習慣の改善項目")
        List<String> lifestyleModifications,
        @JsonPropertyDescription("フォローアップのスケジュール")
        String followUpSchedule,
        @JsonPropertyDescription("患者への説明・教育事項")
        List<String> patientEducation
) {
}
