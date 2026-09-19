/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aiflow.controller;import cn.zhuatech.aiflow.common.ApiResponse;import cn.zhuatech.aiflow.service.WorkflowResilienceService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/aiflow/insights/workflow-resilience") public class WorkflowResilienceController{private final WorkflowResilienceService service;/**
                                                                                                                                                                       * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                       */
public WorkflowResilienceController(WorkflowResilienceService service){this.service=service;}/**
                                                                                                                                                                                                                                                                    * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                    */
@PostMapping ApiResponse<WorkflowResilienceService.Result> evaluate(@Valid @RequestBody WorkflowResilienceService.Request r){return ApiResponse.ok(service.evaluate(r));}}
