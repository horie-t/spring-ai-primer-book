package com.example.spring_ai_demo.application.domain.model.medical;

public record PatientInfo(
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
