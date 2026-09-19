/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aiflow.service;import jakarta.validation.constraints.*;import org.springframework.stereotype.Service;import java.util.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service public class WorkflowResilienceService{/**
                                                 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                 */
public Result evaluate(Request r){int score=100;List<String> gaps=new ArrayList<>();if(r.fallbackNodes()<r.criticalNodes()){score-=25;gaps.add("为关键节点补充降级或替代路径");}if(!r.idempotent()){score-=20;gaps.add("确保重试操作幂等");}if(r.retryLimit()==0){score-=15;gaps.add("配置受控重试策略");}if(!r.circuitBreakerEnabled()){score-=15;gaps.add("增加外部依赖熔断");}if(!r.rollbackAvailable()){score-=25;gaps.add("准备工作流版本回滚");}if(r.destructiveActions()>0&&!r.humanApproval()){score-=40;gaps.add("破坏性操作前增加人工确认");}score=Math.max(0,score);String status=score<60?"BLOCK":score<90?"IMPROVE":"RESILIENT";if(gaps.isEmpty())gaps.add("工作流具备恢复、降级和回滚能力");return new Result(score,status,gaps);}
 /**
  * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
  */
 public record Request(@Min(1) int totalNodes,@Min(0) int criticalNodes,@Min(0) int fallbackNodes,@Min(0) int retryLimit,@NotNull Boolean idempotent,@NotNull Boolean circuitBreakerEnabled,@NotNull Boolean rollbackAvailable,@Min(0) int destructiveActions,@NotNull Boolean humanApproval){}/**
                                                                                                                                                                                                                                                                                                * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                                */
public record Result(int resilienceScore,String status,List<String> gaps){} }
