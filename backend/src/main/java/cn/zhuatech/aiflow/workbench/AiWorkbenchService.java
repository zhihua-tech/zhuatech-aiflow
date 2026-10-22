/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aiflow.workbench;
import org.springframework.stereotype.Service;
import java.math.*;
import java.util.*;
import static cn.zhuatech.aiflow.workbench.WorkbenchInput.*;
/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
    /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
    public Map<String,Object> execute(Map<String,Object> r){
        validate(r);

        keys(r,"failures","maxRetries","initialDelayMs","maxDelayMs","elapsedMs","totalBudgetMs","retryable");int failures=integer(r,"failures",1,100),maximum=integer(r,"maxRetries",0,30),initial=integer(r,"initialDelayMs",1,3600000),max=integer(r,"maxDelayMs",1,3600000),elapsed=integer(r,"elapsedMs",0,86400000),budget=integer(r,"totalBudgetMs",1,86400000);boolean retryable=flag(r,"retryable");if(initial>max)throw new IllegalArgumentException("初始延迟不能超过最大延迟");
        List<Map<String,Object>> schedule=new ArrayList<>();long due=elapsed,delay=initial;for(int i=1;i<failures;i++)delay=Math.min(max,delay*2);
        String decision=!retryable?"NON_RETRYABLE":failures>maximum?"RETRIES_EXHAUSTED":elapsed>=budget?"BUDGET_EXHAUSTED":"RETRY_ELIGIBLE";
        if(decision.equals("RETRY_ELIGIBLE"))for(int attempt=failures;attempt<=maximum;attempt++){if(due+delay>budget)break;due+=delay;schedule.add(Map.of("retryNumber",attempt,"delayMs",delay,"dueAtElapsedMs",due));delay=Math.min(max,delay*2);}
        if(decision.equals("RETRY_ELIGIBLE")&&schedule.isEmpty())decision="BUDGET_EXHAUSTED";long next=schedule.isEmpty()?0L:(Long)schedule.get(0).get("delayMs");
        return Map.of("decision",decision,"nextDelayMs",next,"schedule",List.copyOf(schedule),"remainingBudgetMs",Math.max(0,budget-elapsed),"createsJobs",false,"executionDurationExcluded",true);

    }
}
