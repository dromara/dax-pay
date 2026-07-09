-- ============================================================
-- 升级 SQL - 建表语句增量
-- 数据库: PostgreSQL 14+
-- 注意: 所有表必须有 COMMENT ON 注释
-- ============================================================

-- ------------------------------------------------------------
-- 商户微信域名验证文件表
-- 商户将公众号/小程序的 MP_verify_xxx.txt 上传至平台, 由平台网关统一响应
-- 微信域名校验请求, 便于商户将网关域名绑定到自己的公众号/小程序
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS mch_wx_domain_verify (
    id                   int8          NOT NULL,
    mch_no               varchar(32)   NOT NULL,
    platform             bool          NOT NULL DEFAULT false,
    file_name            varchar(100)  NOT NULL,
    verify_code          varchar(64)   NOT NULL,
    file_content         varchar(200)  NOT NULL,
    remark               varchar(200),
    creator              int8,
    create_time          timestamptz(6),
    last_modifier        int8,
    last_modified_time   timestamptz(6),
    version              int4          NOT NULL DEFAULT 0,
    deleted              bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_mch_wx_domain_verify PRIMARY KEY (id)
);

COMMENT ON TABLE  mch_wx_domain_verify IS '商户微信域名验证文件';
COMMENT ON COLUMN mch_wx_domain_verify.id IS '主键';
COMMENT ON COLUMN mch_wx_domain_verify.mch_no IS '商户号（平台级填 0）';
COMMENT ON COLUMN mch_wx_domain_verify.platform IS '是否平台级：false-商户级 true-平台级';
COMMENT ON COLUMN mch_wx_domain_verify.file_name IS '完整文件名（如 MP_verify_PjhdRxpB8FhG06Fr.txt）';
COMMENT ON COLUMN mch_wx_domain_verify.verify_code IS '验证码（文件名提取，全局唯一）';
COMMENT ON COLUMN mch_wx_domain_verify.file_content IS '文件内容（微信生成的随机字符串）';
COMMENT ON COLUMN mch_wx_domain_verify.remark IS '备注';
COMMENT ON COLUMN mch_wx_domain_verify.creator IS '创建者ID';
COMMENT ON COLUMN mch_wx_domain_verify.create_time IS '创建时间';
COMMENT ON COLUMN mch_wx_domain_verify.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN mch_wx_domain_verify.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN mch_wx_domain_verify.version IS '版本号（乐观锁）';
COMMENT ON COLUMN mch_wx_domain_verify.deleted IS '逻辑删除标记';

CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_wx_domain_verify_code ON mch_wx_domain_verify (verify_code) WHERE deleted = false;
CREATE INDEX IF NOT EXISTS idx_mch_wx_domain_verify_mch_no ON mch_wx_domain_verify (mch_no);
CREATE INDEX IF NOT EXISTS idx_mch_wx_domain_verify_platform ON mch_wx_domain_verify (platform);

-- 删除已废弃的归属类型/微信原始ID列（旧版建表时存在，现已从实体移除）
ALTER TABLE mch_wx_domain_verify DROP COLUMN IF EXISTS owner_type;
ALTER TABLE mch_wx_domain_verify DROP COLUMN IF EXISTS owner_id;

-- ------------------------------------------------------------
-- 微信服务商通道商户绑定表新增认证应用类型字段
-- 控制服务商模式下授权获取openId时, 使用服务商应用(sp_appid)还是子商户应用(sub_appid)
-- ------------------------------------------------------------
ALTER TABLE wechat_isv_channel_merchant ADD COLUMN IF NOT EXISTS auth_app_type varchar(32);
COMMENT ON COLUMN wechat_isv_channel_merchant.auth_app_type IS '认证应用类型: SP_APP=服务商应用(sp_appid), SUB_APP=子商户应用(sub_appid), 默认 SP_APP';

-- ------------------------------------------------------------
-- 微信服务商通道商户应用授权认证配置表
-- 配置服务商通道商户应用(子商户应用)的应用密钥和授权回调地址, 用于微信OAuth授权流程中的身份验证与回调跳转
-- 一个应用对应一份授权配置(由唯一约束 channel_mch_no + wechat_isv_mch_app_id 保证)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS wechat_isv_mch_app_auth_config (
    id                        int8          NOT NULL,
    channel_mch_no            varchar(32)   NOT NULL,
    wechat_isv_mch_app_id     int8          NOT NULL,
    app_secret                varchar(255),
    auth_callback_url         varchar(255),
    mch_no                    varchar(32),
    creator                   int8,
    create_time               timestamptz(6),
    last_modifier             int8,
    last_modified_time        timestamptz(6),
    version                   int4          NOT NULL DEFAULT 0,
    deleted                   bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_wechat_isv_mch_app_auth_config PRIMARY KEY (id)
);

COMMENT ON TABLE  wechat_isv_mch_app_auth_config IS '微信服务商通道商户应用授权认证配置';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.id IS '主键';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.channel_mch_no IS '通道商户号(服务商特约商户)';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.wechat_isv_mch_app_id IS '关联服务商通道商户应用ID';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.app_secret IS '应用密钥(加密存储)';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.auth_callback_url IS '授权回调地址';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.mch_no IS '商户号';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.creator IS '创建者ID';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.create_time IS '创建时间';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN wechat_isv_mch_app_auth_config.deleted IS '逻辑删除标记';

CREATE UNIQUE INDEX IF NOT EXISTS uk_wechat_isv_mch_app_auth_config ON wechat_isv_mch_app_auth_config (channel_mch_no, wechat_isv_mch_app_id) WHERE deleted = false;
CREATE INDEX IF NOT EXISTS idx_wechat_isv_mch_app_auth_config_mch_app_id ON wechat_isv_mch_app_auth_config (wechat_isv_mch_app_id);

-- ------------------------------------------------------------
-- 微信消息通知模板配置已迁入 system_platform_config
-- (config_type=wechat_notify, 非加密 JSON: tradeTemplateId/operateTemplateId).
-- 公众号凭据仍在 system_platform_encrypt_config (wechat_mp_auth).
-- 兼容已建独立表环境: 直接丢弃废弃表.
-- ------------------------------------------------------------
DROP TABLE IF EXISTS pay_platform_wechat_config;

-- ------------------------------------------------------------
-- 微信消息发送记录表
-- 每次发送(含重发)产生一条, 记录发送参数与结果, 供管理端查询与失败重发.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_platform_wechat_message_record (
    id                    int8          NOT NULL,
    user_id               int8,
    message_type          varchar(32),
    open_id               varchar(64),
    template_id           varchar(64),
    template_data         text,
    url                   varchar(500),
    status                varchar(32),
    msg_id                varchar(64),
    error_code            varchar(32),
    error_msg             varchar(500),
    send_time             timestamptz(6),
    scene                 varchar(32),
    wx_app_id             varchar(64),
    creator               int8,
    create_time           timestamptz(6),
    last_modifier         int8,
    last_modified_time    timestamptz(6),
    version               int4          NOT NULL DEFAULT 0,
    deleted               bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_platform_wechat_message_record PRIMARY KEY (id)
);

COMMENT ON TABLE  pay_platform_wechat_message_record IS '微信消息发送记录';
COMMENT ON COLUMN pay_platform_wechat_message_record.id IS '主键';
COMMENT ON COLUMN pay_platform_wechat_message_record.user_id IS '接收平台用户ID(发送目标, 未绑定时也能追溯)';
COMMENT ON COLUMN pay_platform_wechat_message_record.message_type IS '消息类型: template-公众号模板消息, uniform-小程序统一服务消息';
COMMENT ON COLUMN pay_platform_wechat_message_record.open_id IS '接收者 OpenId';
COMMENT ON COLUMN pay_platform_wechat_message_record.template_id IS '模板ID';
COMMENT ON COLUMN pay_platform_wechat_message_record.template_data IS '模板数据(JSON)';
COMMENT ON COLUMN pay_platform_wechat_message_record.url IS '跳转链接或小程序页面路径';
COMMENT ON COLUMN pay_platform_wechat_message_record.status IS '发送状态: sending-发送中, success-成功, failed-失败';
COMMENT ON COLUMN pay_platform_wechat_message_record.msg_id IS '微信消息ID(成功时返回)';
COMMENT ON COLUMN pay_platform_wechat_message_record.error_code IS '错误码(微信错误码)';
COMMENT ON COLUMN pay_platform_wechat_message_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_platform_wechat_message_record.send_time IS '发送时间';
COMMENT ON COLUMN pay_platform_wechat_message_record.scene IS '业务场景标识(trade/operate)';
COMMENT ON COLUMN pay_platform_wechat_message_record.wx_app_id IS '使用的公众号 AppId';
COMMENT ON COLUMN pay_platform_wechat_message_record.creator IS '创建者ID';
COMMENT ON COLUMN pay_platform_wechat_message_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_platform_wechat_message_record.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_platform_wechat_message_record.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_platform_wechat_message_record.version IS '版本号(乐观锁)';
COMMENT ON COLUMN pay_platform_wechat_message_record.deleted IS '逻辑删除标记';

CREATE INDEX IF NOT EXISTS idx_pay_platform_wechat_message_record_user_id ON pay_platform_wechat_message_record (user_id);
CREATE INDEX IF NOT EXISTS idx_pay_platform_wechat_message_record_status ON pay_platform_wechat_message_record (status);
CREATE INDEX IF NOT EXISTS idx_pay_platform_wechat_message_record_send_time ON pay_platform_wechat_message_record (send_time);

-- rename iam_social_config -> iam_social_login_config (avoid naming clash with upcoming third-party platform management)
ALTER TABLE IF EXISTS iam_social_config RENAME TO iam_social_login_config;

-- ----------------------------
-- 平台级移动端应用配置(按 app_type + platform 维度, 每组合一条)
-- app_config/notify_config 使用 text + DataEncryptTypeHandler 加密存储(密文非合法JSON, 故不用 jsonb)
-- ----------------------------
CREATE TABLE IF NOT EXISTS pay_platform_mobile_app (
    id                    int8          NOT NULL,
    app_type              varchar(32)   NOT NULL,
    platform              varchar(32)   NOT NULL,
    app_name              varchar(64),
    app_config            text,
    notify_config         text,
    binding_enabled       bool          NOT NULL DEFAULT false,
    enabled               bool          NOT NULL DEFAULT true,
    remark                varchar(500),
    creator               int8,
    create_time           timestamptz(6),
    last_modifier         int8,
    last_modified_time    timestamptz(6),
    version               int4          NOT NULL DEFAULT 0,
    deleted               bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_platform_mobile_app PRIMARY KEY (id)
);

COMMENT ON TABLE  pay_platform_mobile_app IS '平台级移动端应用配置';
COMMENT ON COLUMN pay_platform_mobile_app.id IS '主键';
COMMENT ON COLUMN pay_platform_mobile_app.app_type IS '端类型: merchant-商户端 / admin-管理端 / cashier-收银台';
COMMENT ON COLUMN pay_platform_mobile_app.platform IS '移动平台: wx_h5/wx_mini/alipay_mini/dy_mini/android/ios';
COMMENT ON COLUMN pay_platform_mobile_app.app_name IS '应用名称(展示用)';
COMMENT ON COLUMN pay_platform_mobile_app.app_config IS '平台特有密钥配置(JSON文本, AES-256-GCM加密存储)';
COMMENT ON COLUMN pay_platform_mobile_app.notify_config IS '消息通知配置(JSON文本, AES-256-GCM加密存储)';
COMMENT ON COLUMN pay_platform_mobile_app.binding_enabled IS '是否启用第三方账号用户绑定';
COMMENT ON COLUMN pay_platform_mobile_app.enabled IS '是否启用';
COMMENT ON COLUMN pay_platform_mobile_app.remark IS '备注';
COMMENT ON COLUMN pay_platform_mobile_app.creator IS '创建者ID';
COMMENT ON COLUMN pay_platform_mobile_app.create_time IS '创建时间';
COMMENT ON COLUMN pay_platform_mobile_app.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_platform_mobile_app.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_platform_mobile_app.version IS '版本号(乐观锁)';
COMMENT ON COLUMN pay_platform_mobile_app.deleted IS '逻辑删除标记';

CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_platform_mobile_app_type_platform
    ON pay_platform_mobile_app (app_type, platform) WHERE deleted = false;
