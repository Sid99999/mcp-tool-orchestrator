package com.siddharth.mcporchestrator.domain;
public record ToolExecutionResult(String tool,String tenantId,String status,long latencyMs,Object result) {}
