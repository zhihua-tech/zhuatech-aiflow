/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.aiflow;import cn.zhuatech.aiflow.service.WorkflowResilienceService;import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class WorkflowResilienceServiceTests{private final WorkflowResilienceService s=new WorkflowResilienceService();/**
                                                                                                                * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                */
@Test void blocksUncontrolledDestructiveFlow(){var r=s.evaluate(new WorkflowResilienceService.Request(10,3,0,0,false,false,false,2,false));assertEquals("BLOCK",r.status());}/**
                                                                                                                                                                                                                                                                                             * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                                                             */
@Test void approvesResilientFlow(){var r=s.evaluate(new WorkflowResilienceService.Request(10,2,2,3,true,true,true,0,true));assertEquals("RESILIENT",r.status());}}
