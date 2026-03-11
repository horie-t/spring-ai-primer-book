package com.example.spring_ai_demo.adapter.in.web;

import com.example.spring_ai_demo.adapter.in.web.dto.DiagnosisRequest;
import com.example.spring_ai_demo.adapter.in.web.dto.DiagnosisResponse;
import com.example.spring_ai_demo.adapter.out.saas.MedicalDiagnosisService;
import com.example.spring_ai_demo.application.domain.model.medical.MedicalReport;
import com.example.spring_ai_demo.application.domain.model.medical.PatientInfo;
import org.springframework.web.bind.annotation.*;

@RestController
public class MedicalDiagnosisController {
    private final MedicalDiagnosisService medicalDiagnosisService;

    public MedicalDiagnosisController(MedicalDiagnosisService medicalDiagnosisService) {
        this.medicalDiagnosisService = medicalDiagnosisService;
    }

    @PostMapping("/api/medical:diagnose")
    public DiagnosisResponse diagnose(@RequestBody DiagnosisRequest request) {
        PatientInfo patientInfo = new PatientInfo(
                request.age(),
                request.gender(),
                request.chiefComplaint(),
                request.medicalHistory(),
                request.currentSymptoms(),
                request.imagingData(),
                request.labResults(),
                request.currentMedications(),
                request.vitalSigns(),
                request.physicalExam()
        );

        MedicalReport report = medicalDiagnosisService.diagnose(patientInfo);

        return new DiagnosisResponse(report);
    }

}
