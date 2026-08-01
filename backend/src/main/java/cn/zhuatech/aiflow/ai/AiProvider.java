/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.aiflow.ai;
import org.springframework.stereotype.Component; import java.util.Map;
public interface AiProvider { AiResult execute(String prompt,Map<String,String> context); record AiResult(String provider,String answer,Map<String,Object> evidence){} }
@Component class DemoAiProvider implements AiProvider { public AiResult execute(String prompt,Map<String,String> context){return new AiResult("demo-agent-provider","已生成演示执行计划，生产环境须在每个工具节点实施权限与人工确认。",Map.of("steps",5,"tools",3,"requiresApproval",true));} }
