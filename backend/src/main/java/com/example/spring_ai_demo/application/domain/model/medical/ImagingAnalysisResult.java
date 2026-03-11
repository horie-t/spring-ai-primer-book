package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record ImagingAnalysisResult(
        @JsonPropertyDescription("画像モダリティ（X線、CT、MRI等）")
        String modality,
        @JsonPropertyDescription("画像上の主要な所見のリスト")
        List<String> findings,
        @JsonPropertyDescription("画像診断の結論")
        String impression,
        @JsonPropertyDescription("臨床的相関性・意義")
        String clinicalCorrelation
) {
}
