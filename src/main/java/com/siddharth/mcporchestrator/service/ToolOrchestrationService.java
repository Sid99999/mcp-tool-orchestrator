package com.siddharth.mcporchestrator.service;
import com.siddharth.mcporchestrator.domain.ToolExecutionResult;
import org.springframework.stereotype.Service;
import java.util.function.Supplier;
@Service
public class ToolOrchestrationService {
  private final RateLimitService rateLimitService; private final AuditService auditService;
  public ToolOrchestrationService(RateLimitService rateLimitService,AuditService auditService){this.rateLimitService=rateLimitService;this.auditService=auditService;}
  public ToolExecutionResult execute(String tenantId,String clientId,String tool,Supplier<Object> action){long started=System.nanoTime(); try{rateLimitService.check(tenantId,tool); Object result=action.get(); long latency=elapsed(started); auditService.record(tenantId,clientId,tool,latency,"SUCCESS"); return new ToolExecutionResult(tool,tenantId,"SUCCESS",latency,result);}catch(RuntimeException ex){long latency=elapsed(started); auditService.record(tenantId,clientId,tool,latency,"ERROR"); throw ex;}}
  private long elapsed(long started){return (System.nanoTime()-started)/1_000_000;}
}
