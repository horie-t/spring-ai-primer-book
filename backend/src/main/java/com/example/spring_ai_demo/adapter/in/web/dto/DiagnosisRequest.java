package com.example.spring_ai_demo.adapter.in.web.dto;

public record DiagnosisRequest(
        Integer age,
        String gender,
        String chiefComplaint,
        String medicalHistory,
        String currentSymptoms,
        String imagingData,
        String labResults,
        String currentMedications,
        String vitalSigns,
        String physicalExam
) {
}
