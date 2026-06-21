-- 第三方平台登录配置表
-- 配置页采用"枚举驱动 + 读时初始化"模式: 首次访问时为每个 SocialSource 枚举平台
-- 插入占位记录(configured=false, 业务字段留空), 用户保存配置后才置 configured=true.
-- 因此 client_id/client_secret/redirect_uri 等业务字段允许为空.
DROP TABLE IF EXISTS iam_social_config;
CREATE TABLE IF NOT EXISTS iam_social_config (
    id BIGINT NOT NULL,
    creator BIGINT,
    create_time TIMESTAMPTZ(6),
    last_modifier BIGINT,
    last_modified_time TIMESTAMPTZ(6),
    version INT4 DEFAULT 0,
    deleted BOOLEAN DEFAULT FALSE,
    source VARCHAR(32) NOT NULL,
    client_id VARCHAR(128),
    client_secret VARCHAR(256),
    redirect_uri VARCHAR(256),
    extra JSONB DEFAULT '{}'::jsonb,
    configured BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT uk_iam_social_config_source UNIQUE (source)
);

COMMENT ON TABLE iam_social_config IS '第三方平台登录配置表';
COMMENT ON COLUMN iam_social_config.id IS '主键';
COMMENT ON COLUMN iam_social_config.creator IS '创建人';
COMMENT ON COLUMN iam_social_config.create_time IS '创建时间';
COMMENT ON COLUMN iam_social_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN iam_social_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_social_config.version IS '乐观锁';
COMMENT ON COLUMN iam_social_config.deleted IS '逻辑删除';
COMMENT ON COLUMN iam_social_config.source IS '平台编码';
COMMENT ON COLUMN iam_social_config.client_id IS '客户端ID';
COMMENT ON COLUMN iam_social_config.client_secret IS '客户端密钥';
COMMENT ON COLUMN iam_social_config.redirect_uri IS '回调地址';
COMMENT ON COLUMN iam_social_config.extra IS '平台特有配置';
COMMENT ON COLUMN iam_social_config.configured IS '是否已完成配置';
COMMENT ON COLUMN iam_social_config.enabled IS '是否启用';
