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
-- 配置服务商通道商户应用(子商户应用)的应用密钥, 用于微信OAuth授权流程中的身份验证
-- 一个应用对应一份授权配置(由唯一约束 channel_mch_no + wechat_isv_mch_app_id 保证)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS wechat_isv_mch_app_auth_config (
    id                        int8          NOT NULL,
    channel_mch_no            varchar(32)   NOT NULL,
    wechat_isv_mch_app_id     int8          NOT NULL,
    app_secret                varchar(255),
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
-- app_config: text + DataEncryptTypeHandler 加密(密文非合法JSON, 不用 jsonb)
-- notify_config: jsonb 明文(模板/开关等非敏感配置, JsonbStringTypeHandler)
-- ----------------------------
CREATE TABLE IF NOT EXISTS pay_platform_mobile_app (
    id                    int8          NOT NULL,
    app_type              varchar(32)   NOT NULL,
    platform              varchar(32)   NOT NULL,
    app_config            text,
    notify_config         jsonb,
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
COMMENT ON COLUMN pay_platform_mobile_app.app_config IS '平台特有密钥配置(JSON文本, AES-256-GCM加密存储)';
COMMENT ON COLUMN pay_platform_mobile_app.notify_config IS '消息通知配置(jsonb, 明文, 非敏感)';

-- 移除无用的展示字段 app_name(以端类型/平台区分即可)
ALTER TABLE pay_platform_mobile_app DROP COLUMN IF EXISTS app_name;
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

-- 兼容历史列类型:
-- app_config: 若曾为 jsonb 需改为 text(加密密文)
-- notify_config: 若为 text 需改为 jsonb(明文配置); 空串按 NULL
-- 目标类型已正确时再执行无副作用(jsonb::text::jsonb 可逆)
ALTER TABLE pay_platform_mobile_app
    ALTER COLUMN app_config TYPE text USING app_config::text;
ALTER TABLE pay_platform_mobile_app
    ALTER COLUMN notify_config TYPE jsonb USING (
        CASE
            WHEN notify_config IS NULL THEN NULL
            WHEN btrim(notify_config::text) = '' THEN NULL
            ELSE notify_config::text::jsonb
        END
    );
COMMENT ON COLUMN pay_platform_mobile_app.app_config IS '平台特有密钥配置(JSON文本, AES-256-GCM加密存储)';
COMMENT ON COLUMN pay_platform_mobile_app.notify_config IS '消息通知配置(jsonb, 明文, 非敏感)';

-- ------------------------------------------------------------
-- 开源版已取消硬件对接(云音箱/云打印/厂商配置), 相关表 device_speaker /
-- device_printer / device_vendor_config 不再由开源维护, 可手工 DROP。
-- 码牌 device_qr_code 仍为开源能力(收款入口)。
-- ------------------------------------------------------------

-- ------------------------------------------------------------
-- 码牌: 支持空白库存与划拨(批次号 + 商户号可空)
-- ------------------------------------------------------------
ALTER TABLE device_qr_code ADD COLUMN IF NOT EXISTS batch_no varchar(64);
ALTER TABLE device_qr_code ALTER COLUMN mch_no DROP NOT NULL;

COMMENT ON COLUMN device_qr_code.batch_no IS '批次号(批量创建空白码时写入)';
COMMENT ON COLUMN device_qr_code.mch_no IS '所属商户号(空白码为空, 划拨后写入)';

-- ------------------------------------------------------------
-- 网关支付业务单(容器): 预下单时创建, 支付时再挂 pay_trade
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_gateway_order (
    id                   int8          NOT NULL,
    mch_no               varchar(32)   NOT NULL,
    app_id               varchar(32)   NOT NULL,
    order_no             varchar(64)   NOT NULL,
    biz_order_no         varchar(64)   NOT NULL,
    gateway_type         varchar(32)   NOT NULL,
    title                varchar(128)  NOT NULL,
    description          varchar(512),
    amount               int8          NOT NULL,
    currency             varchar(8)    NOT NULL DEFAULT 'CNY',
    status               varchar(32)   NOT NULL,
    notify_url           varchar(256),
    return_url           varchar(256),
    attach               varchar(512),
    expired_time         timestamptz(6),
    channel              varchar(32),
    method               varchar(32),
    product              varchar(32),
    capability           varchar(64),
    channel_mch_no       varchar(64),
    scene                varchar(32),
    device               varchar(16),
    pay_time             timestamptz(6),
    close_time           timestamptz(6),
    client_ip            varchar(64),
    terminal_no          varchar(64),
    goods_detail         jsonb,
    creator              int8,
    create_time          timestamptz(6),
    last_modifier        int8,
    last_modified_time   timestamptz(6),
    version              int4          NOT NULL DEFAULT 0,
    deleted              bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_gateway_order PRIMARY KEY (id)
);

COMMENT ON TABLE  pay_gateway_order IS '网关支付业务单容器(聚合扫码/收银台预下单)';
COMMENT ON COLUMN pay_gateway_order.id IS '主键';
COMMENT ON COLUMN pay_gateway_order.mch_no IS '商户号';
COMMENT ON COLUMN pay_gateway_order.app_id IS '应用号';
COMMENT ON COLUMN pay_gateway_order.order_no IS '平台网关单号(URL用)';
COMMENT ON COLUMN pay_gateway_order.biz_order_no IS '商户业务单号';
COMMENT ON COLUMN pay_gateway_order.gateway_type IS '网关类型: cashier/aggregate';
COMMENT ON COLUMN pay_gateway_order.title IS '标题';
COMMENT ON COLUMN pay_gateway_order.description IS '描述';
COMMENT ON COLUMN pay_gateway_order.amount IS '金额(最小货币单位)';
COMMENT ON COLUMN pay_gateway_order.currency IS '币种';
COMMENT ON COLUMN pay_gateway_order.status IS '业务状态: wait_pay/paying/paid/closed/expired';
COMMENT ON COLUMN pay_gateway_order.notify_url IS '异步通知地址';
COMMENT ON COLUMN pay_gateway_order.return_url IS '同步跳转地址';
COMMENT ON COLUMN pay_gateway_order.attach IS '商户附加参数';
COMMENT ON COLUMN pay_gateway_order.expired_time IS '过期时间';
COMMENT ON COLUMN pay_gateway_order.channel IS '支付通道(支付后冗余)';
COMMENT ON COLUMN pay_gateway_order.method IS '支付方式(支付后冗余)';
COMMENT ON COLUMN pay_gateway_order.product IS '支付产品(支付后冗余)';
COMMENT ON COLUMN pay_gateway_order.capability IS '支付能力(路由回填)';
COMMENT ON COLUMN pay_gateway_order.channel_mch_no IS '通道商户号(路由回填)';
COMMENT ON COLUMN pay_gateway_order.scene IS '收银场景 wechat_pay/alipay/union_pay';
COMMENT ON COLUMN pay_gateway_order.device IS '最后发起设备 mobile/pc';
COMMENT ON COLUMN pay_gateway_order.pay_time IS '支付成功时间';
COMMENT ON COLUMN pay_gateway_order.close_time IS '关闭时间';
COMMENT ON COLUMN pay_gateway_order.client_ip IS '客户端IP';
COMMENT ON COLUMN pay_gateway_order.terminal_no IS '终端设备编码';
COMMENT ON COLUMN pay_gateway_order.goods_detail IS '商品明细(jsonb)';
COMMENT ON COLUMN pay_gateway_order.creator IS '创建者ID';
COMMENT ON COLUMN pay_gateway_order.create_time IS '创建时间';
COMMENT ON COLUMN pay_gateway_order.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_gateway_order.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_gateway_order.version IS '版本号(乐观锁)';
COMMENT ON COLUMN pay_gateway_order.deleted IS '逻辑删除标记';

CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_gateway_order_no ON pay_gateway_order (order_no) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_gateway_biz ON pay_gateway_order (app_id, biz_order_no) WHERE deleted = false;
CREATE INDEX IF NOT EXISTS idx_pay_gateway_status_expired ON pay_gateway_order (status, expired_time);
CREATE INDEX IF NOT EXISTS idx_pay_gateway_mch_time ON pay_gateway_order (mch_no, create_time);

-- ------------------------------------------------------------
-- 网关聚合扫码配置(应用级一行)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_gateway_aggregate_config (
    id                   int8          NOT NULL,
    mch_no               varchar(32)   NOT NULL,
    app_id               varchar(32)   NOT NULL,
    wx_product           varchar(32),
    wx_method            varchar(32),
    alipay_product       varchar(32),
    alipay_method        varchar(32),
    union_product        varchar(32),
    union_method         varchar(32),
    auto_launch          bool          NOT NULL DEFAULT false,
    creator              int8,
    create_time          timestamptz(6),
    last_modifier        int8,
    last_modified_time   timestamptz(6),
    version              int4          NOT NULL DEFAULT 0,
    deleted              bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_gateway_aggregate_config PRIMARY KEY (id)
);

COMMENT ON TABLE  pay_gateway_aggregate_config IS '网关聚合扫码支付配置(应用级)';
COMMENT ON COLUMN pay_gateway_aggregate_config.id IS '主键';
COMMENT ON COLUMN pay_gateway_aggregate_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_gateway_aggregate_config.app_id IS '应用号';
COMMENT ON COLUMN pay_gateway_aggregate_config.wx_product IS '微信场景支付产品';
COMMENT ON COLUMN pay_gateway_aggregate_config.wx_method IS '微信场景支付方式';
COMMENT ON COLUMN pay_gateway_aggregate_config.alipay_product IS '支付宝场景支付产品';
COMMENT ON COLUMN pay_gateway_aggregate_config.alipay_method IS '支付宝场景支付方式';
COMMENT ON COLUMN pay_gateway_aggregate_config.union_product IS '云闪付场景支付产品';
COMMENT ON COLUMN pay_gateway_aggregate_config.union_method IS '云闪付场景支付方式';
COMMENT ON COLUMN pay_gateway_aggregate_config.auto_launch IS '是否自动拉起支付';
COMMENT ON COLUMN pay_gateway_aggregate_config.creator IS '创建者ID';
COMMENT ON COLUMN pay_gateway_aggregate_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_gateway_aggregate_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_gateway_aggregate_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_gateway_aggregate_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN pay_gateway_aggregate_config.deleted IS '逻辑删除标记';

CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_gateway_aggregate_app ON pay_gateway_aggregate_config (app_id) WHERE deleted = false;

-- ------------------------------------------------------------
-- pay_trade: 冗余路由参数 + 业务单号 + 客户端IP, 消除 sync/close/callback 对容器的依赖
-- ------------------------------------------------------------
ALTER TABLE pay_trade ADD COLUMN IF NOT EXISTS biz_order_no varchar(64);
ALTER TABLE pay_trade ADD COLUMN IF NOT EXISTS channel_mch_no varchar(64);
ALTER TABLE pay_trade ADD COLUMN IF NOT EXISTS capability varchar(64);
ALTER TABLE pay_trade ADD COLUMN IF NOT EXISTS client_ip varchar(64);

COMMENT ON COLUMN pay_trade.biz_order_no IS '商户业务单号(冗余自容器, 供同步/关闭/回调等流程直接读取)';
COMMENT ON COLUMN pay_trade.channel_mch_no IS '通道商户号(路由回填, 冗余自容器, 供策略层直接读取)';
COMMENT ON COLUMN pay_trade.capability IS '支付能力编码(路由回填, 冗余自容器, 供策略层直接读取)';
COMMENT ON COLUMN pay_trade.client_ip IS '客户端IP(冗余自容器, 供关闭/同步等策略读取)';

-- ------------------------------------------------------------
-- 网关聚合扫码配置重构: 三级配置深度(AUTO/METHOD/DIRECT) + 场景子表
-- ------------------------------------------------------------
ALTER TABLE pay_gateway_aggregate_config DROP COLUMN IF EXISTS wx_product;
ALTER TABLE pay_gateway_aggregate_config DROP COLUMN IF EXISTS wx_method;
ALTER TABLE pay_gateway_aggregate_config DROP COLUMN IF EXISTS alipay_product;
ALTER TABLE pay_gateway_aggregate_config DROP COLUMN IF EXISTS alipay_method;
ALTER TABLE pay_gateway_aggregate_config DROP COLUMN IF EXISTS union_product;
ALTER TABLE pay_gateway_aggregate_config DROP COLUMN IF EXISTS union_method;
ALTER TABLE pay_gateway_aggregate_config ADD COLUMN IF NOT EXISTS level varchar(16) NOT NULL DEFAULT 'auto';

COMMENT ON COLUMN pay_gateway_aggregate_config.level IS '配置深度: auto-自动/method-方式/direct-精确';

CREATE TABLE IF NOT EXISTS pay_gateway_aggregate_scene (
    id                   int8          NOT NULL,
    config_id            int8          NOT NULL,
    scene                varchar(32)   NOT NULL,
    method               varchar(32),
    channel_mch_no       varchar(64),
    capability           varchar(64),
    creator              int8,
    create_time          timestamptz(6),
    last_modifier        int8,
    last_modified_time   timestamptz(6),
    version              int4          NOT NULL DEFAULT 0,
    deleted              bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_gateway_aggregate_scene PRIMARY KEY (id)
);

COMMENT ON TABLE  pay_gateway_aggregate_scene IS '网关聚合扫码场景配置(子表, 每场景一行)';
COMMENT ON COLUMN pay_gateway_aggregate_scene.id IS '主键';
COMMENT ON COLUMN pay_gateway_aggregate_scene.config_id IS '聚合配置主表ID';
COMMENT ON COLUMN pay_gateway_aggregate_scene.scene IS '场景编码: wechat_pay/alipay/union_pay/douyin';
COMMENT ON COLUMN pay_gateway_aggregate_scene.method IS '支付方式(METHOD模式填)';
COMMENT ON COLUMN pay_gateway_aggregate_scene.channel_mch_no IS '通道商户号(DIRECT模式填)';
COMMENT ON COLUMN pay_gateway_aggregate_scene.capability IS '支付能力(DIRECT模式填)';
COMMENT ON COLUMN pay_gateway_aggregate_scene.creator IS '创建者ID';
COMMENT ON COLUMN pay_gateway_aggregate_scene.create_time IS '创建时间';
COMMENT ON COLUMN pay_gateway_aggregate_scene.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_gateway_aggregate_scene.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_gateway_aggregate_scene.version IS '版本号(乐观锁)';
COMMENT ON COLUMN pay_gateway_aggregate_scene.deleted IS '逻辑删除标记';

CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_gateway_aggregate_scene ON pay_gateway_aggregate_scene (config_id, scene) WHERE deleted = false;

-- ----------------------------
-- 网关收银台支付项配置(应用级, H5 按终端分桶 / WEB 扁平列表)
-- ----------------------------
CREATE TABLE IF NOT EXISTS pay_gateway_cashier_item (
    id                   int8          NOT NULL,
    mch_no               varchar(32)   NOT NULL,
    app_id               varchar(32)   NOT NULL,
    cashier_type         varchar(16)   NOT NULL,
    scene                varchar(32),
    name                 varchar(64)   NOT NULL,
    icon                 varchar(32),
    recommend            bool          NOT NULL DEFAULT false,
    sort_no              int4          NOT NULL DEFAULT 0,
    resolve_mode         varchar(16)   NOT NULL,
    method               varchar(32),
    channel_mch_no       varchar(64),
    capability           varchar(64),
    creator              int8,
    create_time          timestamptz(6),
    last_modifier        int8,
    last_modified_time   timestamptz(6),
    version              int4          NOT NULL DEFAULT 0,
    deleted              bool          NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_gateway_cashier_item PRIMARY KEY (id)
);

COMMENT ON TABLE  pay_gateway_cashier_item IS '网关收银台支付项配置(应用级)';
COMMENT ON COLUMN pay_gateway_cashier_item.id IS '主键';
COMMENT ON COLUMN pay_gateway_cashier_item.mch_no IS '商户号';
COMMENT ON COLUMN pay_gateway_cashier_item.app_id IS '应用号';
COMMENT ON COLUMN pay_gateway_cashier_item.cashier_type IS '收银台类型: h5/web';
COMMENT ON COLUMN pay_gateway_cashier_item.scene IS 'H5终端场景: browser/wechat_pay/alipay/union_pay/douyin; WEB为空';
COMMENT ON COLUMN pay_gateway_cashier_item.name IS '前台展示名称';
COMMENT ON COLUMN pay_gateway_cashier_item.icon IS '图标编码: wechat/alipay/union/douyin/aggregate';
COMMENT ON COLUMN pay_gateway_cashier_item.recommend IS '是否推荐';
COMMENT ON COLUMN pay_gateway_cashier_item.sort_no IS '排序号, 越小越前';
COMMENT ON COLUMN pay_gateway_cashier_item.resolve_mode IS '解析模式: method/direct';
COMMENT ON COLUMN pay_gateway_cashier_item.method IS '支付方式(METHOD模式填)';
COMMENT ON COLUMN pay_gateway_cashier_item.channel_mch_no IS '通道商户号(DIRECT模式填)';
COMMENT ON COLUMN pay_gateway_cashier_item.capability IS '支付能力(DIRECT模式填)';
COMMENT ON COLUMN pay_gateway_cashier_item.creator IS '创建者ID';
COMMENT ON COLUMN pay_gateway_cashier_item.create_time IS '创建时间';
COMMENT ON COLUMN pay_gateway_cashier_item.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN pay_gateway_cashier_item.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_gateway_cashier_item.version IS '版本号(乐观锁)';
COMMENT ON COLUMN pay_gateway_cashier_item.deleted IS '逻辑删除标记';

CREATE INDEX IF NOT EXISTS idx_pay_gateway_cashier_item_list
    ON pay_gateway_cashier_item (app_id, cashier_type, scene)
    WHERE deleted = false;

-- ============================================================
-- PayTrade 瘦身: 业务/路由/回执字段归容器, trade 仅留资金动作固有属性
-- ============================================================

-- pay_normal_order 补充 14 字段(请求参数 + 回执 + 通道关联 + 错误)
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS other_method varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS limit_pay varchar(128);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS openid varchar(128);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS bar_code varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS error_msg varchar(500);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS buyer_id varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS buyer_logon_id varchar(128);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS trade_product varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS trade_way varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS bank_type varchar(32);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS promotion_type varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS provider varchar(32);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS trans_order_no varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS relation_order_no varchar(64);

COMMENT ON COLUMN pay_normal_order.other_method IS '其他支付方式, method=other 时生效';
COMMENT ON COLUMN pay_normal_order.limit_pay IS '限制支付类型(如限制信用卡)';
COMMENT ON COLUMN pay_normal_order.openid IS '微信 openid(jsapi/app/miniapp)';
COMMENT ON COLUMN pay_normal_order.bar_code IS '付款码(被扫支付)';
COMMENT ON COLUMN pay_normal_order.error_msg IS '错误信息';
COMMENT ON COLUMN pay_normal_order.buyer_id IS '付款用户 ID(支付宝 buyer_user_id 等)';
COMMENT ON COLUMN pay_normal_order.buyer_logon_id IS '买家登录账号(支付宝手机号/邮箱)';
COMMENT ON COLUMN pay_normal_order.trade_product IS '通道方记录的支付产品';
COMMENT ON COLUMN pay_normal_order.trade_way IS '通道方记录的交易方式';
COMMENT ON COLUMN pay_normal_order.bank_type IS '银行卡类型(借记卡/贷记卡)';
COMMENT ON COLUMN pay_normal_order.promotion_type IS '活动类型';
COMMENT ON COLUMN pay_normal_order.provider IS '支付渠道(微信/支付宝/银联等)';
COMMENT ON COLUMN pay_normal_order.trans_order_no IS '透传订单号(三方通道产生的透传订单号)';
COMMENT ON COLUMN pay_normal_order.relation_order_no IS '特殊通道关联订单号(部分通道订单号有前缀/长度限制时使用)';

-- pay_gateway_order 补充 14 字段
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS other_method varchar(64);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS limit_pay varchar(128);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS openid varchar(128);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS bar_code varchar(64);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS error_msg varchar(500);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS buyer_id varchar(64);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS buyer_logon_id varchar(128);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS trade_product varchar(64);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS trade_way varchar(64);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS bank_type varchar(32);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS promotion_type varchar(64);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS provider varchar(32);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS trans_order_no varchar(64);
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS relation_order_no varchar(64);

COMMENT ON COLUMN pay_gateway_order.other_method IS '其他支付方式, method=other 时生效';
COMMENT ON COLUMN pay_gateway_order.limit_pay IS '限制支付类型(如限制信用卡)';
COMMENT ON COLUMN pay_gateway_order.openid IS '微信 openid(jsapi/app/miniapp)';
COMMENT ON COLUMN pay_gateway_order.bar_code IS '付款码(被扫支付)';
COMMENT ON COLUMN pay_gateway_order.error_msg IS '错误信息';
COMMENT ON COLUMN pay_gateway_order.buyer_id IS '付款用户 ID(支付宝 buyer_user_id 等)';
COMMENT ON COLUMN pay_gateway_order.buyer_logon_id IS '买家登录账号(支付宝手机号/邮箱)';
COMMENT ON COLUMN pay_gateway_order.trade_product IS '通道方记录的支付产品';
COMMENT ON COLUMN pay_gateway_order.trade_way IS '通道方记录的交易方式';
COMMENT ON COLUMN pay_gateway_order.bank_type IS '银行卡类型(借记卡/贷记卡)';
COMMENT ON COLUMN pay_gateway_order.promotion_type IS '活动类型';
COMMENT ON COLUMN pay_gateway_order.provider IS '支付渠道(微信/支付宝/银联等)';
COMMENT ON COLUMN pay_gateway_order.trans_order_no IS '透传订单号(三方通道产生的透传订单号)';
COMMENT ON COLUMN pay_gateway_order.relation_order_no IS '特殊通道关联订单号(部分通道订单号有前缀/长度限制时使用)';

-- pay_trade 删除 22 字段(已归容器)
ALTER TABLE pay_trade DROP COLUMN IF EXISTS biz_order_no;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS product;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS channel;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS method;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS other_method;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS limit_pay;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS provider;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS expired_time;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS trans_order_no;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS relation_order_no;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS buyer_id;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS buyer_logon_id;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS openid;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS trade_product;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS trade_way;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS bank_type;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS bar_code;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS promotion_type;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS channel_mch_no;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS capability;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS client_ip;
ALTER TABLE pay_trade DROP COLUMN IF EXISTS error_msg;