/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aiflow.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** 发布工作流前检查节点完整性、工具登记和高风险动作审批。 */
@Service
public class WorkflowGuardService {
    private static final Set<String> REGISTERED_TOOLS = Set.of(
        "knowledge.search", "crm.read", "ticket.create", "message.send", "report.generate"
    );

    public ValidationResult validate(ValidationRequest request) {
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        List<String> uniqueSteps = request.steps().stream().map(String::trim).filter(step -> !step.isEmpty()).distinct().toList();
        List<String> unknownTools = request.tools().stream().map(String::trim).filter(tool -> !REGISTERED_TOOLS.contains(tool)).distinct().toList();

        if (uniqueSteps.size() != request.steps().size()) errors.add("步骤名称不能为空或重复");
        if (!unknownTools.isEmpty()) errors.add("存在未登记工具: " + String.join(", ", unknownTools));
        if (request.hasExternalWrite() && !request.approvalConfigured()) errors.add("外部写操作必须配置人工审批节点");
        if (uniqueSteps.size() > 8) warnings.add("工作流节点较多，建议拆分子流程");
        if (request.tools().size() > 4) warnings.add("单流程工具较多，请复核最小权限范围");

        int riskScore = Math.min(100,
            (request.hasExternalWrite() ? 45 : 10)
                + unknownTools.size() * 20
                + (request.tools().size() > 4 ? 15 : 0)
                + (!request.approvalConfigured() ? 10 : 0));
        return new ValidationResult(errors.isEmpty(), riskScore, List.copyOf(errors), List.copyOf(warnings), uniqueSteps.size(), request.tools().size());
    }

    public record ValidationRequest(
        @NotBlank(message = "请输入工作流名称") String name,
        @NotEmpty(message = "请至少配置一个步骤") List<String> steps,
        @NotEmpty(message = "请至少配置一个工具") List<String> tools,
        boolean hasExternalWrite,
        boolean approvalConfigured
    ) {}

    public record ValidationResult(
        boolean valid,
        int riskScore,
        List<String> errors,
        List<String> warnings,
        int stepCount,
        int toolCount
    ) {}
}
