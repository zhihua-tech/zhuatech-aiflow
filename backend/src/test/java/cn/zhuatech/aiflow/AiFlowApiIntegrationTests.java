/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aiflow;
import org.junit.jupiter.api.*; import org.springframework.beans.factory.annotation.Autowired; import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc; import org.springframework.boot.test.context.SpringBootTest; import org.springframework.http.MediaType; import org.springframework.test.web.servlet.MockMvc; import java.util.regex.*; import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc class AiFlowApiIntegrationTests {
    @Autowired MockMvc mvc; private String operatorToken; private String plannerToken;
    @BeforeEach void login()throws Exception{operatorToken=token("operator","Demo@2026");plannerToken=token("planner","Demo@2026");}
    private String token(String u,String p)throws Exception{String json=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\""+u+"\",\"password\":\""+p+"\"}")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();Matcher matcher=Pattern.compile("\\\"token\\\":\\\"([^\\\"]+)\\\"").matcher(json);if(!matcher.find())throw new AssertionError("登录响应中缺少 token");return matcher.group(1);}
    @Test void operatorCanReadShopfloorDashboard()throws Exception{mvc.perform(get("/api/shopfloor/dashboard").header("Authorization","Bearer "+operatorToken)).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data.metrics[0].label").value("计划执行节点"));}
    @Test void plannerCanReadWorkRecords()throws Exception{mvc.perform(get("/api/admin/work-orders").header("Authorization","Bearer "+plannerToken)).andExpect(status().isOk()).andExpect(jsonPath("$.data.length()").value(3));}
    @Test void operatorCanSubmitProductionReport()throws Exception{mvc.perform(post("/api/shopfloor/work-orders/1/reports").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"operationName\":\"人工审批\",\"goodQty\":2,\"defectQty\":1,\"remark\":\"数据完整\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("反馈提交成功")).andExpect(jsonPath("$.data.completedQty").value(10));}
    @Test void operatorCanValidateWorkflowBeforePublishing()throws Exception{mvc.perform(post("/api/shopfloor/workflow-validation").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"客户回访流程\",\"steps\":[\"读取客户\",\"生成摘要\",\"发送消息\"],\"tools\":[\"crm.read\",\"message.send\"],\"hasExternalWrite\":true,\"approvalConfigured\":false}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.valid").value(false))
        .andExpect(jsonPath("$.data.riskScore").value(55))
        .andExpect(jsonPath("$.data.errors[0]").value("外部写操作必须配置人工审批节点"));}
    @Test void operatorCanAnalyzeWorkflowImpact()throws Exception{mvc.perform(post("/api/shopfloor/workflow-impact").header("Authorization","Bearer "+operatorToken).contentType(MediaType.APPLICATION_JSON).content("{\"workflowName\":\"客户回访流程\",\"baselineDailyRuns\":100,\"expectedDailyRuns\":180,\"averageTokens\":1200,\"retryRate\":0.2,\"straightThroughRate\":0.7,\"reviewMinutes\":10,\"availableReviewHours\":6}"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.data.readiness").value("BLOCKED"))
        .andExpect(jsonPath("$.data.capacityDeltaPercent").value(80))
        .andExpect(jsonPath("$.data.projectedDailyTokens").value(259200))
        .andExpect(jsonPath("$.data.requiredReviewHours").value(9.0))
        .andExpect(jsonPath("$.data.risks.length()").value(3));}
    @Test void anonymousRequestIsDenied()throws Exception{mvc.perform(get("/api/admin/dashboard")).andExpect(status().isForbidden());}
}
