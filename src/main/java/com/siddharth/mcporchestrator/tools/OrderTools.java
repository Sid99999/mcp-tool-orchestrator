package com.siddharth.mcporchestrator.tools;
import com.siddharth.mcporchestrator.domain.ToolExecutionResult;
import com.siddharth.mcporchestrator.service.DownstreamOrderService;
import com.siddharth.mcporchestrator.service.ToolOrchestrationService;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
@Component
public class OrderTools {
  private final DownstreamOrderService downstream; private final ToolOrchestrationService orchestration;
  public OrderTools(DownstreamOrderService downstream,ToolOrchestrationService orchestration){this.downstream=downstream;this.orchestration=orchestration;}
  @McpTool(name="get_order_status",description="Return the current order status for a tenant. Use this when a user asks about an order.")
  public ToolExecutionResult getOrderStatus(@McpToolParam(description="Tenant identifier",required=true) String tenantId,@McpToolParam(description="Client identifier for audit and isolation",required=true) String clientId,@McpToolParam(description="Order identifier",required=true) String orderId){return orchestration.execute(tenantId,clientId,"get_order_status",()->downstream.getOrder(orderId));}
  @McpTool(name="search_orders",description="Search sample orders by tenant and status. Demonstrates a read-only MCP tool with tenant isolation.")
  public ToolExecutionResult searchOrders(@McpToolParam(description="Tenant identifier",required=true) String tenantId,@McpToolParam(description="Client identifier for audit and isolation",required=true) String clientId,@McpToolParam(description="Order status filter, for example PROCESSING",required=false) String status){return orchestration.execute(tenantId,clientId,"search_orders",()->List.of(Map.of("orderId","ORD-1001","status",status==null?"PROCESSING":status),Map.of("orderId","ORD-1002","status","SHIPPED")));}
}
