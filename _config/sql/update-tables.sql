-- 第三方平台登录配置表: 合并前端地址字段, 移除 state 超时与作用域字段
-- 将 frontend_base_url + callback_path 合并为 frontend_callback_url,
-- state_timeout 不再由平台配置维护, 改用系统默认常量(300秒),
-- scopes 字段从未参与授权请求, 各平台 scope 已硬编码, 移除该死字段
ALTER TABLE iam_social_config ADD COLUMN IF NOT EXISTS frontend_callback_url VARCHAR(384);
-- 迁移已有数据(拼接基础地址与回调路径)
UPDATE iam_social_config
    SET frontend_callback_url = CONCAT(COALESCE(frontend_base_url, ''), COALESCE(callback_path, ''))
    WHERE frontend_callback_url IS NULL AND (frontend_base_url IS NOT NULL OR callback_path IS NOT NULL);
ALTER TABLE iam_social_config DROP COLUMN IF EXISTS frontend_base_url;
ALTER TABLE iam_social_config DROP COLUMN IF EXISTS callback_path;
ALTER TABLE iam_social_config DROP COLUMN IF EXISTS state_timeout;
ALTER TABLE iam_social_config DROP COLUMN IF EXISTS scopes;
ALTER TABLE iam_social_config DROP COLUMN IF EXISTS name;
ALTER TABLE iam_social_config DROP COLUMN IF EXISTS frontend_callback_url;

-- client_secret 改为加密存储(AES-256-GCM, 由 DataEncryptTypeHandler 透明加解密)
-- 字段长度 VARCHAR(256) 可容纳密文(appSecret 多为 32~64 字符, 密文 < 200 字符), 无需调整
-- 开发阶段无历史明文数据, 跳过迁移; 生产环境启用前需写一次性逻辑加密历史明文
COMMENT ON COLUMN iam_social_config.client_secret IS '客户端密钥(加密存储)';

-- 回调地址不再由社交配置维护, 改由端点配置(PlatformUrlConfig)的 baseUrl 自动生成
-- 实际回调地址为 {adminBaseUrl|merchantBaseUrl}/auth/oauth-callback/{source}
ALTER TABLE iam_social_config DROP COLUMN IF EXISTS redirect_uri;
