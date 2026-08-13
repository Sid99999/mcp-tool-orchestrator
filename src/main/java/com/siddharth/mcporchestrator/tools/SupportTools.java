package com.siddharth.mcporchestrator.tools;
import com.siddharth.mcporchestrator.domain.ToolExecutionResult;
import com.siddharth.mcporchestrator.service.ToolOrchestrationService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import java.util.UUID;
@Component
public class SupportTools {
  private final ToolOrchestrationService orchestration;
  public SupportTools(ToolOrchestrationService orchestration){this.orchestration=orchestration;}
  @McpTool(name="create_support_ticket",description="Create a support ticket for a tenant. Use only when the user explicitly requests support follow-up.")
  public ToolExecutionResult createSupportTicket(@McpToolParam(description="Tenant identifier",required=true) String tenantId,@McpToolParam(description="Client identifier for audit and isolation",required=true) String clientId,@McpToolParam(description="Short issue description",required=true) String issue){return orchestration.execute(tenantId,clientId,"create_support_ticket",()->new Ticket(UUID.randomUUID().toString(),issue,"OPEN"));}
  public record Ticket(String ticketId,String issue,String status){}
}
