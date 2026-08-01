/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aiflow.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** 在工作流版本发布前估算吞吐、令牌消耗和人工复核负荷。 */
@Service
public class WorkflowImpactService {
    public ImpactResult analyze(ImpactRequest request) {
        int capacityDelta = (int) Math.round((request.expectedDailyRuns() - request.baselineDailyRuns()) * 100.0 / request.baselineDailyRuns());
        long projectedTokens = Math.round(request.expectedDailyRuns() * request.averageTokens() * (1 + request.retryRate()));
        double reviewHours = Math.round(request.expectedDailyRuns() * (1 - request.straightThroughRate())
            * request.reviewMinutes() / 60.0 * 10.0) / 10.0;
        List<String> risks = new ArrayList<>();
        if (capacityDelta > 50) risks.add("运行量较基线增长超过 50%");
        if (request.retryRate() > 0.15) risks.add("重试率偏高，可能放大模型和接口负载");
        if (reviewHours > request.availableReviewHours()) risks.add("人工复核工时不足");
        String readiness = risks.isEmpty() ? "READY" : reviewHours > request.availableReviewHours() ? "BLOCKED" : "REVIEW";
        return new ImpactResult(readiness, capacityDelta, projectedTokens, reviewHours,
            Math.max(0, Math.round((request.availableReviewHours() - reviewHours) * 10.0) / 10.0), List.copyOf(risks));
    }

    public record ImpactRequest(
        @NotBlank(message = "请输入工作流名称") String workflowName,
        @Positive int baselineDailyRuns,
        @Positive int expectedDailyRuns,
        @Positive int averageTokens,
        @DecimalMin("0.0") @DecimalMax("1.0") double retryRate,
        @DecimalMin("0.0") @DecimalMax("1.0") double straightThroughRate,
        @Positive int reviewMinutes,
        @PositiveOrZero double availableReviewHours
    ) {}

    public record ImpactResult(
        String readiness,
        int capacityDeltaPercent,
        long projectedDailyTokens,
        double requiredReviewHours,
        double reviewHourHeadroom,
        List<String> risks
    ) {}
}
