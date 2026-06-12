-- 用户表
CREATE TABLE IF NOT EXISTS iam_user (
    id BIGINT NOT NULL,
    creator BIGINT,
    create_time TIMESTAMP,
    last_modifier BIGINT,
    last_modified_time TIMESTAMP,
    version INT4 DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE,
    tenant_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(200) NOT NULL,
    role_id BIGINT,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    PRIMARY KEY (id)
);

COMMENT ON TABLE iam_user IS '用户表';
COMMENT ON COLUMN iam_user.tenant_id IS '租户ID';
COMMENT ON COLUMN iam_user.username IS '用户名';
COMMENT ON COLUMN iam_user.password IS '密码';
COMMENT ON COLUMN iam_user.role_id IS '角色ID(单角色模式)';
COMMENT ON COLUMN iam_user.status IS '状态';

CREATE UNIQUE INDEX IF NOT EXISTS uk_user_tenant_username ON iam_user(tenant_id, username) WHERE deleted = FALSE;
CREATE INDEX IF NOT EXISTS idx_user_tenant_id ON iam_user(tenant_id);
CREATE INDEX IF NOT EXISTS idx_user_role_id ON iam_user(role_id);
