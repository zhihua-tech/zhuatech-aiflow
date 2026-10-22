/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aiflow.workbench;
import org.junit.jupiter.api.Test;
import java.math.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.authentication.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;
/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
class AiWorkbenchServiceTest {
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void coversAdditionalBoundaryOne(){var r=new HashMap<>(sample());r.put("retryable",false);assertEquals("NON_RETRYABLE",service.execute(r).get("decision"));assertEquals(List.of(),service.execute(r).get("schedule"));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void coversAdditionalBoundaryTwo(){var r=new HashMap<>(sample());r.put("totalBudgetMs",80);assertEquals("BUDGET_EXHAUSTED",service.execute(r).get("decision"));}
 private final AiWorkbenchService service=new AiWorkbenchService();
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 private Map<String,Object> sample(){return Map.<String,Object>ofEntries(Map.entry("failures",1),Map.entry("maxRetries",4),Map.entry("initialDelayMs",100),Map.entry("maxDelayMs",1000),Map.entry("elapsedMs",50),Map.entry("totalBudgetMs",1000),Map.entry("retryable",true));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void computesBusinessResult(){var result=service.execute(sample());assertEquals("RETRY_ELIGIBLE",result.get("decision"));assertEquals(100L,result.get("nextDelayMs"));assertEquals(3,((List<?>)result.get("schedule")).size());}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void coversFeatureBoundary(){var bad=new HashMap<>(sample());bad.put("failures",5);assertEquals("RETRIES_EXHAUSTED",service.execute(bad).get("decision"));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void deterministicAndDoesNotMutateInput(){var input=sample();String before=input.toString();assertEquals(service.execute(input),service.execute(input));assertEquals(before,input.toString());}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsNull(){assertThrows(IllegalArgumentException.class,()->service.execute(null));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsMissingFields(){assertThrows(IllegalArgumentException.class,()->service.execute(Map.of()));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsUnknownFields(){var input=new HashMap<>(sample());input.put("__unknown",true);assertThrows(IllegalArgumentException.class,()->service.execute(input));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsOversizedText(){assertThrows(IllegalArgumentException.class,()->service.execute(Map.of("payload","x".repeat(64001))));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsDeepInput(){Map<String,Object> v=Map.of("x",1);for(int i=0;i<14;i++)v=Map.of("x",v);final var input=v;assertThrows(IllegalArgumentException.class,()->service.execute(input));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void httpRequiresAuthentication()throws Exception{var mvc=MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();mvc.perform(post("/api/workbench/retry-schedule").contentType(MediaType.APPLICATION_JSON).content("{\"failures\":1,\"maxRetries\":4,\"initialDelayMs\":100,\"maxDelayMs\":1000,\"elapsedMs\":50,\"totalBudgetMs\":1000,\"retryable\":true}")).andExpect(status().isUnauthorized());}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void httpAllowsAuthenticatedComputation()throws Exception{var mvc=MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();mvc.perform(post("/api/workbench/retry-schedule").principal(new TestingAuthenticationToken("test-user","unused","ROLE_OPERATOR")).contentType(MediaType.APPLICATION_JSON).content("{\"failures\":1,\"maxRetries\":4,\"initialDelayMs\":100,\"maxDelayMs\":1000,\"elapsedMs\":50,\"totalBudgetMs\":1000,\"retryable\":true}")).andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void httpRejectsInvalidInput()throws Exception{var mvc=MockMvcBuilders.standaloneSetup(new AiWorkbenchController(service)).build();mvc.perform(post("/api/workbench/retry-schedule").principal(new TestingAuthenticationToken("test-user","unused","ROLE_OPERATOR")).contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.success").value(false));}
 /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
 @Test void rejectsAnonymousAuthenticationObject(){var actor=new AnonymousAuthenticationToken("test-key","anonymous",List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ANONYMOUS")));assertEquals(401,new AiWorkbenchController(service).run(sample(),actor).getStatusCode().value());}
}
