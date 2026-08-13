CREATE TABLE IF NOT EXISTS tool_audit (
  id BIGSERIAL PRIMARY KEY,
  tenant_id VARCHAR(100) NOT NULL,
  client_id VARCHAR(100) NOT NULL,
  tool_name VARCHAR(150) NOT NULL,
  latency_ms BIGINT NOT NULL,
  outcome VARCHAR(30) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_tool_audit_tenant_created ON tool_audit (tenant_id,created_at DESC);
