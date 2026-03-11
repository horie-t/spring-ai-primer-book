package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public enum TaskType {
    @JsonPropertyDescription("画像診断（X線、CT、MRI等）の分析")
    IMAGING_ANALYSIS,
    
    @JsonPropertyDescription("血液検査・尿検査等の結果解釈")
    LAB_RESULTS,
    
    @JsonPropertyDescription("薬物相互作用チェック")
    DRUG_INTERACTION,
    
    @JsonPropertyDescription("鑑別診断の提案")
    DIFFERENTIAL_DIAGNOSIS,
    
    @JsonPropertyDescription("治療計画の立案")
    TREATMENT_PLAN
}
