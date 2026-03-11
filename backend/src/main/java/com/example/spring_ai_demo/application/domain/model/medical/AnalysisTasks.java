package com.example.spring_ai_demo.application.domain.model.medical;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record AnalysisTasks(
        @JsonPropertyDescription("必要な分析タスクのリスト")
        List<TaskType> tasks,
        @JsonPropertyDescription("これらのタスクが必要な理由")
        String reasoning
) {
}
