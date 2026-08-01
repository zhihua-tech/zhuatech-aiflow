/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aiflow.controller;

import cn.zhuatech.aiflow.ai.AiProvider;
import cn.zhuatech.aiflow.common.ApiResponse;
import cn.zhuatech.aiflow.dto.AiFlowDto.*;
import cn.zhuatech.aiflow.service.AiFlowService;
import cn.zhuatech.aiflow.service.WorkflowGuardService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/shopfloor")
@PreAuthorize("hasAnyRole('DOMAIN_USER','ADMIN')")
public class WorkspaceController {
    private final AiFlowService service;
    private final AiProvider ai;
    private final WorkflowGuardService workflowGuard;

    public WorkspaceController(AiFlowService service, AiProvider ai, WorkflowGuardService workflowGuard) {
        this.service = service;
        this.ai = ai;
        this.workflowGuard = workflowGuard;
    }

    @GetMapping("/dashboard")
    public ApiResponse<Dashboard> dashboard() { return ApiResponse.ok(service.shopfloorDashboard()); }

    @PostMapping("/work-orders/{id}/reports")
    public ApiResponse<ReportResult> report(@PathVariable Long id, @Valid @RequestBody ReportRequest request) {
        return ApiResponse.ok("反馈提交成功", service.report(id, request));
    }

    @PostMapping("/ai-preview")
    public ApiResponse<AiProvider.AiResult> preview(@RequestBody Map<String, String> body) {
        return ApiResponse.ok(ai.execute(body.getOrDefault("prompt", ""), Map.of("mode", "demo")));
    }

    @PostMapping("/workflow-validation")
    public ApiResponse<WorkflowGuardService.ValidationResult> validateWorkflow(@Valid @RequestBody WorkflowGuardService.ValidationRequest request) {
        return ApiResponse.ok("工作流校验完成", workflowGuard.validate(request));
    }
}
