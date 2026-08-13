package com.siddharth.mcporchestrator.service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@Service
public class AuditService { private final JdbcTemplate jdbc; public AuditService(JdbcTemplate jdbc){this.jdbc=jdbc;} public void record(String tenantId,String clientId,String tool,long latencyMs,String outcome){jdbc.update("INSERT INTO tool_audit(tenant_id,client_id,tool_name,latency_ms,outcome) VALUES (?,?,?,?,?)",tenantId,clientId,tool,latencyMs,outcome);} }
