-- IAM 安全配置表
CREATE TABLE IF NOT EXISTS pay_platform_security_config (
    id BIGSERIAL PRIMARY KEY,
    config_group VARCHAR(100) NOT NULL,
    config_key VARCHAR(100) NOT NULL,
    config_value TEXT,
    value_type VARCHAR(50) NOT NULL,
    description VARCHAR(500),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    UNIQUE (config_group, config_key)
);

-- 添加注释
COMMENT ON TABLE pay_platform_security_config IS 'IAM 安全配置表';
COMMENT ON COLUMN pay_platform_security_config.id IS '主键ID';
COMMENT ON COLUMN pay_platform_security_config.config_group IS '配置分组（如：password_policy, session_management）';
COMMENT ON COLUMN pay_platform_security_config.config_key IS '配置项键名';
COMMENT ON COLUMN pay_platform_security_config.config_value IS '配置项值（根据 value_type 存储不同格式）';
COMMENT ON COLUMN pay_platform_security_config.value_type IS '配置值类型（STRING, INTEGER, BOOLEAN, JSON_OBJECT, JSON_ARRAY）';
COMMENT ON COLUMN pay_platform_security_config.description IS '配置描述';
COMMENT ON COLUMN pay_platform_security_config.enabled IS '是否启用';
COMMENT ON COLUMN pay_platform_security_config.created_at IS '创建时间';
COMMENT ON COLUMN pay_platform_security_config.updated_at IS '更新时间';
COMMENT ON COLUMN pay_platform_security_config.updated_by IS '更新人ID';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_security_config_group ON pay_platform_security_config(config_group);
CREATE INDEX IF NOT EXISTS idx_security_config_key ON pay_platform_security_config(config_key);
CREATE INDEX IF NOT EXISTS idx_security_config_enabled ON pay_platform_security_config(enabled);

-- IAM 安全配置变更历史表
CREATE TABLE IF NOT EXISTS pay_platform_security_config_history (
    id BIGSERIAL PRIMARY KEY,
    config_group VARCHAR(100) NOT NULL,
    config_key VARCHAR(100) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    modified_by BIGINT,
    modified_by_username VARCHAR(100),
    modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remark VARCHAR(500)
);

-- 添加注释
COMMENT ON TABLE pay_platform_security_config_history IS 'IAM 安全配置变更历史表';
COMMENT ON COLUMN pay_platform_security_config_history.id IS '主键ID';
COMMENT ON COLUMN pay_platform_security_config_history.config_group IS '配置分组';
COMMENT ON COLUMN pay_platform_security_config_history.config_key IS '配置项键名';
COMMENT ON COLUMN pay_platform_security_config_history.old_value IS '旧值';
COMMENT ON COLUMN pay_platform_security_config_history.new_value IS '新值';
COMMENT ON COLUMN pay_platform_security_config_history.modified_by IS '修改人ID';
COMMENT ON COLUMN pay_platform_security_config_history.modified_by_username IS '修改人用户名';
COMMENT ON COLUMN pay_platform_security_config_history.modified_at IS '修改时间';
COMMENT ON COLUMN pay_platform_security_config_history.remark IS '修改原因/备注';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_security_config_history_group ON pay_platform_security_config_history(config_group);
CREATE INDEX IF NOT EXISTS idx_security_config_history_key ON pay_platform_security_config_history(config_key);
CREATE INDEX IF NOT EXISTS idx_security_config_history_modified_at ON pay_platform_security_config_history(modified_at DESC);
CREATE INDEX IF NOT EXISTS idx_security_config_history_modified_by ON pay_platform_security_config_history(modified_by);
