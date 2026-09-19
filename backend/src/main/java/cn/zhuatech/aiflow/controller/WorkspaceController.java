/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aiflow.controller;

import cn.zhuatech.aiflow.ai.AiProvider;
import cn.zhuatech.aiflow.common.ApiResponse;
import cn.zhuatech.aiflow.dto.AiFlowDto.*;
import cn.zhuatech.aiflow.service.AiFlowService;
import cn.zhuatech.aiflow.service.WorkflowGuardService;
import cn.zhuatech.aiflow.service.WorkflowImpactService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/shopfloor")
@PreAuthorize("hasAnyRole('DOMAIN_USER','ADMIN')")
public class WorkspaceController {
    private final AiFlowService service;
    private final AiProvider ai;
    private final WorkflowGuardService workflowGuard;
    private final WorkflowImpactService workflowImpact;

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public WorkspaceController(AiFlowService service, AiProvider ai, WorkflowGuardService workflowGuard, WorkflowImpactService workflowImpact) {
        this.service = service;
        this.ai = ai;
        this.workflowGuard = workflowGuard;
        this.workflowImpact = workflowImpact;
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @GetMapping("/dashboard")
    public ApiResponse<Dashboard> dashboard() { return ApiResponse.ok(service.shopfloorDashboard()); }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/work-orders/{id}/reports")
    public ApiResponse<ReportResult> report(@PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        return ApiResponse.ok("反馈提交成功", service.report(id, request));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/ai-preview")
    public ApiResponse<AiProvider.AiResult> preview(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(ai.execute(body.getOrDefault("prompt", ""), Map.of("mode", "demo")));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/workflow-validation")
    public ApiResponse<WorkflowGuardService.ValidationResult> validateWorkflow(@Valid @RequestBody WorkflowGuardService.ValidationRequest request) {
        return ApiResponse.ok("工作流校验完成", workflowGuard.validate(request));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping("/workflow-impact")
    public ApiResponse<WorkflowImpactService.ImpactResult> analyzeImpact(@Valid @RequestBody WorkflowImpactService.ImpactRequest request) {
        return ApiResponse.ok("工作流影响分析完成", workflowImpact.analyze(request));
    }
}
