package com.example.spring_ai_demo.adapter.out.saas;

import com.example.spring_ai_demo.application.domain.model.medical.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class MedicalDiagnosisService {
    private final ChatClient chatClient;

    @Value("classpath:prompt_templates/medical/OrchestratorPrompt.txt")
    private Resource orchestratorPrompt;

    @Value("classpath:prompt_templates/medical/ImagingAnalysisPrompt.txt")
    private Resource imagingAnalysisPrompt;

    @Value("classpath:prompt_templates/medical/LabResultsPrompt.txt")
    private Resource labResultsPrompt;

    @Value("classpath:prompt_templates/medical/DrugInteractionPrompt.txt")
    private Resource drugInteractionPrompt;

    @Value("classpath:prompt_templates/medical/DifferentialDiagnosisPrompt.txt")
    private Resource differentialDiagnosisPrompt;

    @Value("classpath:prompt_templates/medical/TreatmentPlanPrompt.txt")
    private Resource treatmentPlanPrompt;

    @Value("classpath:prompt_templates/medical/SynthesizerPrompt.txt")
    private Resource synthesizerPrompt;

    public MedicalDiagnosisService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public MedicalReport diagnose(PatientInfo patientInfo) {
        // ステップ1: オーケストレーター - 必要な分析タスクを特定
        AnalysisTasks tasks = orchestrate(patientInfo);

        // ステップ2: 各ワーカーを並列実行
        CompletableFuture<ImagingAnalysisResult> imagingFuture = tasks.tasks().contains(TaskType.IMAGING_ANALYSIS)
                ? CompletableFuture.supplyAsync(() -> analyzeImaging(patientInfo))
                : CompletableFuture.completedFuture(null);

        CompletableFuture<LabResultsInterpretation> labFuture = tasks.tasks().contains(TaskType.LAB_RESULTS)
                ? CompletableFuture.supplyAsync(() -> interpretLabResults(patientInfo))
                : CompletableFuture.completedFuture(null);

        CompletableFuture<DrugInteractionCheck> drugFuture = tasks.tasks().contains(TaskType.DRUG_INTERACTION)
                ? CompletableFuture.supplyAsync(() -> checkDrugInteraction(patientInfo))
                : CompletableFuture.completedFuture(null);

        CompletableFuture<DifferentialDiagnosisResult> diagnosisFuture = tasks.tasks().contains(TaskType.DIFFERENTIAL_DIAGNOSIS)
                ? CompletableFuture.supplyAsync(() -> performDifferentialDiagnosis(patientInfo))
                : CompletableFuture.completedFuture(null);

        // 鑑別診断が完了してから治療計画を作成（依存関係あり）
        CompletableFuture<TreatmentPlan> treatmentFuture = tasks.tasks().contains(TaskType.TREATMENT_PLAN)
                ? diagnosisFuture.thenApplyAsync(diagnosis -> createTreatmentPlan(patientInfo, diagnosis))
                : CompletableFuture.completedFuture(null);

        // すべてのワーカーの完了を待つ
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(
                imagingFuture, labFuture, drugFuture, diagnosisFuture, treatmentFuture
        );

        // ステップ3: シンセサイザー - 結果を統合
        return allTasks.thenApply(v -> synthesize(
                patientInfo,
                imagingFuture.join(),
                labFuture.join(),
                drugFuture.join(),
                diagnosisFuture.join(),
                treatmentFuture.join()
        )).join();
    }

    private AnalysisTasks orchestrate(PatientInfo patientInfo) {
        return chatClient.prompt()
                .user(u -> u.text(orchestratorPrompt)
                        .param("age", patientInfo.age())
                        .param("gender", patientInfo.gender())
                        .param("chiefComplaint", patientInfo.chiefComplaint())
                        .param("medicalHistory", patientInfo.medicalHistory())
                        .param("currentSymptoms", patientInfo.currentSymptoms()))
                .call()
                .entity(AnalysisTasks.class);
    }

    private ImagingAnalysisResult analyzeImaging(PatientInfo patientInfo) {
        return chatClient.prompt()
                .user(u -> u.text(imagingAnalysisPrompt)
                        .param("imagingData", patientInfo.imagingData())
                        .param("chiefComplaint", patientInfo.chiefComplaint())
                        .param("clinicalContext", patientInfo.currentSymptoms()))
                .call()
                .entity(ImagingAnalysisResult.class);
    }

    private LabResultsInterpretation interpretLabResults(PatientInfo patientInfo) {
        return chatClient.prompt()
                .user(u -> u.text(labResultsPrompt)
                        .param("labResults", patientInfo.labResults())
                        .param("age", patientInfo.age())
                        .param("gender", patientInfo.gender())
                        .param("symptoms", patientInfo.currentSymptoms()))
                .call()
                .entity(LabResultsInterpretation.class);
    }

    private DrugInteractionCheck checkDrugInteraction(PatientInfo patientInfo) {
        return chatClient.prompt()
                .user(u -> u.text(drugInteractionPrompt)
                        .param("currentMedications", patientInfo.currentMedications())
                        .param("medicalHistory", patientInfo.medicalHistory())
                        .param("age", patientInfo.age()))
                .call()
                .entity(DrugInteractionCheck.class);
    }

    private DifferentialDiagnosisResult performDifferentialDiagnosis(PatientInfo patientInfo) {
        return chatClient.prompt()
                .user(u -> u.text(differentialDiagnosisPrompt)
                        .param("chiefComplaint", patientInfo.chiefComplaint())
                        .param("symptoms", patientInfo.currentSymptoms())
                        .param("vitalSigns", patientInfo.vitalSigns())
                        .param("physicalExam", patientInfo.physicalExam()))
                .call()
                .entity(DifferentialDiagnosisResult.class);
    }

    private TreatmentPlan createTreatmentPlan(PatientInfo patientInfo, DifferentialDiagnosisResult diagnosis) {
        return chatClient.prompt()
                .user(u -> u.text(treatmentPlanPrompt)
                        .param("diagnosis", diagnosis != null ? diagnosis.diagnoses() : "")
                        .param("patientAge", patientInfo.age())
                        .param("medicalHistory", patientInfo.medicalHistory())
                        .param("currentMedications", patientInfo.currentMedications()))
                .call()
                .entity(TreatmentPlan.class);
    }

    private MedicalReport synthesize(PatientInfo patientInfo,
                                      ImagingAnalysisResult imagingResult,
                                      LabResultsInterpretation labResult,
                                      DrugInteractionCheck drugCheck,
                                      DifferentialDiagnosisResult diagnosisResult,
                                      TreatmentPlan treatmentPlan) {
        return chatClient.prompt()
                .user(u -> u.text(synthesizerPrompt)
                        .param("chiefComplaint", patientInfo.chiefComplaint())
                        .param("imagingResults", imagingResult != null ? imagingResult : "N/A")
                        .param("labInterpretation", labResult != null ? labResult : "N/A")
                        .param("drugCheck", drugCheck != null ? drugCheck : "N/A")
                        .param("differentialDiagnosis", diagnosisResult != null ? diagnosisResult : "N/A")
                        .param("treatmentPlan", treatmentPlan != null ? treatmentPlan : "N/A"))
                .call()
                .entity(MedicalReport.class);
    }
}
