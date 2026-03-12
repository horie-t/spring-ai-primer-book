package com.example.spring_ai_demo.adapter.out.saas;

import com.example.spring_ai_demo.application.domain.model.OptimizedPortfolio;
import com.example.spring_ai_demo.application.domain.model.PortfolioEvaluation;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PortfolioManagementService {
    private final ChatClient chatClient;

    @Value("classpath:prompt_templates/PortfolioEvaluationPrompt.txt")
    private Resource evaluationPrompt;

    @Value("classpath:prompt_templates/PortfolioOptimizationPrompt.txt")
    private Resource optimizationPrompt;

    public PortfolioManagementService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Evaluator-Optimizerワークフローを実行し、最適化されたポートフォリオを返す
     *
     * @param currentPortfolio 現在の資産配分（資産名 -> 割合%）
     * @param riskTolerance    リスク許容度（low, medium, high）
     * @param investmentGoal   投資目標
     * @param marketCondition  現在の市場環境
     * @param maxIterations    最大反復回数
     * @return 最適化されたポートフォリオ
     */
    public OptimizedPortfolio optimizePortfolio(
            Map<String, Double> currentPortfolio,
            String riskTolerance,
            String investmentGoal,
            String marketCondition,
            int maxIterations) {

        Map<String, Double> workingPortfolio = currentPortfolio;
        PortfolioEvaluation evaluation = null;

        for (int i = 0; i < maxIterations; i++) {
            // Evaluator: 現在のポートフォリオを評価
            evaluation = evaluate(workingPortfolio, riskTolerance, investmentGoal, marketCondition);

            // 評価が十分良好であれば終了
            if (isAcceptable(evaluation)) {
                break;
            }

            // Optimizer: 評価結果に基づいてポートフォリオを最適化
            OptimizedPortfolio optimized = optimize(
                    workingPortfolio,
                    evaluation,
                    riskTolerance,
                    investmentGoal,
                    marketCondition
            );

            workingPortfolio = optimized.assetAllocation();
        }

        // 最終的な最適化結果を返す
        return new OptimizedPortfolio(
                workingPortfolio,
                evaluation.riskScore(),
                evaluation.expectedReturn(),
                evaluation.recommendation()
        );
    }

    /**
     * Evaluator: ポートフォリオを評価
     */
    private PortfolioEvaluation evaluate(
            Map<String, Double> portfolio,
            String riskTolerance,
            String investmentGoal,
            String marketCondition) {

        return chatClient.prompt()
                .user(u -> u.text(evaluationPrompt)
                        .param("portfolio", formatPortfolio(portfolio))
                        .param("riskTolerance", riskTolerance)
                        .param("investmentGoal", investmentGoal)
                        .param("marketCondition", marketCondition))
                .call()
                .entity(PortfolioEvaluation.class);
    }

    /**
     * Optimizer: 評価結果に基づいてポートフォリオを最適化
     */
    private OptimizedPortfolio optimize(
            Map<String, Double> currentPortfolio,
            PortfolioEvaluation evaluation,
            String riskTolerance,
            String investmentGoal,
            String marketCondition) {

        return chatClient.prompt()
                .user(u -> u.text(optimizationPrompt)
                        .param("currentPortfolio", formatPortfolio(currentPortfolio))
                        .param("evaluation", formatEvaluation(evaluation))
                        .param("riskTolerance", riskTolerance)
                        .param("investmentGoal", investmentGoal)
                        .param("marketCondition", marketCondition))
                .call()
                .entity(OptimizedPortfolio.class);
    }

    /**
     * 評価結果が許容範囲かどうかを判定
     */
    private boolean isAcceptable(PortfolioEvaluation evaluation) {
        // リスクスコアが適切で、期待リターンが十分で、分散が良好な場合に許容
        return evaluation.riskScore() <= 7.0
                && evaluation.expectedReturn() >= 5.0
                && evaluation.diversificationScore() >= 7.0;
    }

    private String formatPortfolio(Map<String, Double> portfolio) {
        StringBuilder sb = new StringBuilder();
        portfolio.forEach((asset, allocation) ->
                sb.append(asset).append(": ").append(allocation).append("%\n"));
        return sb.toString();
    }

    private String formatEvaluation(PortfolioEvaluation evaluation) {
        return String.format(
                "Risk Score: %.2f\nExpected Return: %.2f%%\nDiversification Score: %.2f\n" +
                        "Market Alignment: %s\nRecommendation: %s",
                evaluation.riskScore(),
                evaluation.expectedReturn(),
                evaluation.diversificationScore(),
                evaluation.marketAlignment(),
                evaluation.recommendation()
        );
    }
}
