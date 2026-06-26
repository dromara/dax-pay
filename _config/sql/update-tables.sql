-- ===================================
-- 云音箱设备域: 厂商配置表 + 设备台账表
-- ===================================

-- 设备厂商配置
CREATE TABLE IF NOT EXISTS device_vendor_config (
    id                  bigint          NOT NULL,
    device_type         varchar(32)     NOT NULL,
    vendor_code         varchar(64)     NOT NULL,
    config_name         varchar(128)    NOT NULL,
    app_id              varchar(128),
    app_secret          varchar(512),
    enable              boolean         NOT NULL DEFAULT true,
    ext_param           jsonb,
    remark              varchar(512),
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int             NOT NULL DEFAULT 0,
    deleted             boolean         NOT NULL DEFAULT false,
    CONSTRAINT device_vendor_config_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE device_vendor_config IS '设备厂商配置';
COMMENT ON COLUMN device_vendor_config.id IS '主键';
COMMENT ON COLUMN device_vendor_config.device_type IS '设备类型';
COMMENT ON COLUMN device_vendor_config.vendor_code IS '厂商代码';
COMMENT ON COLUMN device_vendor_config.config_name IS '配置名称';
COMMENT ON COLUMN device_vendor_config.app_id IS '应用ID';
COMMENT ON COLUMN device_vendor_config.app_secret IS '应用密钥';
COMMENT ON COLUMN device_vendor_config.enable IS '是否启用';
COMMENT ON COLUMN device_vendor_config.ext_param IS '扩展参数';
COMMENT ON COLUMN device_vendor_config.remark IS '备注';
COMMENT ON COLUMN device_vendor_config.creator IS '创建人ID';
COMMENT ON COLUMN device_vendor_config.create_time IS '创建时间';
COMMENT ON COLUMN device_vendor_config.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN device_vendor_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN device_vendor_config.version IS '版本号';
COMMENT ON COLUMN device_vendor_config.deleted IS '逻辑删除';

-- 设备类型+厂商+配置名称唯一索引(未删除范围内)
CREATE UNIQUE INDEX IF NOT EXISTS uk_device_vendor_config_name
    ON device_vendor_config (device_type, vendor_code, config_name) WHERE deleted = false;
-- 厂商查询索引
CREATE INDEX IF NOT EXISTS idx_device_vendor_config_vendor
    ON device_vendor_config (device_type, vendor_code, deleted);

-- 云音箱设备
CREATE TABLE IF NOT EXISTS device_speaker (
    id                  bigint          NOT NULL,
    mch_no              varchar(32),
    vendor_code         varchar(64),
    vendor_config_id    bigint,
    device_sn           varchar(64)     NOT NULL,
    imei                varchar(32),
    shop_id             varchar(64),
    device_name         varchar(128),
    status              varchar(16)     NOT NULL DEFAULT 'unbound',
    bind_time           timestamptz(6),
    last_online_time    timestamptz(6),
    remark              varchar(512),
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int             NOT NULL DEFAULT 0,
    deleted             boolean         NOT NULL DEFAULT false,
    CONSTRAINT device_speaker_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE device_speaker IS '云音箱设备';
COMMENT ON COLUMN device_speaker.id IS '主键';
COMMENT ON COLUMN device_speaker.mch_no IS '商户号';
COMMENT ON COLUMN device_speaker.device_sn IS '设备序列号';
COMMENT ON COLUMN device_speaker.imei IS 'IMEI';
COMMENT ON COLUMN device_speaker.shop_id IS '厂商门店ID';
COMMENT ON COLUMN device_speaker.device_name IS '设备名称';
COMMENT ON COLUMN device_speaker.status IS '设备状态';
COMMENT ON COLUMN device_speaker.bind_time IS '绑定时间';
COMMENT ON COLUMN device_speaker.last_online_time IS '最后在线时间';
COMMENT ON COLUMN device_speaker.remark IS '备注';
COMMENT ON COLUMN device_speaker.creator IS '创建人ID';
COMMENT ON COLUMN device_speaker.create_time IS '创建时间';
COMMENT ON COLUMN device_speaker.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN device_speaker.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN device_speaker.version IS '版本号';
COMMENT ON COLUMN device_speaker.deleted IS '逻辑删除';
COMMENT ON COLUMN device_speaker.vendor_code IS '厂商代码';
COMMENT ON COLUMN device_speaker.vendor_config_id IS '厂商配置ID';

-- 设备序列号唯一索引(未删除范围内)
CREATE UNIQUE INDEX IF NOT EXISTS uk_device_speaker_sn ON device_speaker (device_sn) WHERE deleted = false;
-- 商户号查询索引
CREATE INDEX IF NOT EXISTS idx_device_speaker_mch_no ON device_speaker (mch_no, deleted);

-- 云打印设备
CREATE TABLE IF NOT EXISTS device_printer (
    id                  bigint          NOT NULL,
    mch_no              varchar(32),
    vendor_code         varchar(64),
    vendor_config_id    bigint,
    device_sn           varchar(64)     NOT NULL,
    imei                varchar(32),
    shop_id             varchar(64),
    device_name         varchar(128),
    status              varchar(16)     NOT NULL DEFAULT 'unbound',
    bind_time           timestamptz(6),
    last_online_time    timestamptz(6),
    remark              varchar(512),
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int             NOT NULL DEFAULT 0,
    deleted             boolean         NOT NULL DEFAULT false,
    CONSTRAINT device_printer_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE device_printer IS '云打印设备';
COMMENT ON COLUMN device_printer.id IS '主键';
COMMENT ON COLUMN device_printer.mch_no IS '商户号';
COMMENT ON COLUMN device_printer.device_sn IS '设备序列号';
COMMENT ON COLUMN device_printer.imei IS 'IMEI';
COMMENT ON COLUMN device_printer.shop_id IS '厂商门店ID';
COMMENT ON COLUMN device_printer.device_name IS '设备名称';
COMMENT ON COLUMN device_printer.status IS '设备状态';
COMMENT ON COLUMN device_printer.bind_time IS '绑定时间';
COMMENT ON COLUMN device_printer.last_online_time IS '最后在线时间';
COMMENT ON COLUMN device_printer.remark IS '备注';
COMMENT ON COLUMN device_printer.creator IS '创建人ID';
COMMENT ON COLUMN device_printer.create_time IS '创建时间';
COMMENT ON COLUMN device_printer.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN device_printer.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN device_printer.version IS '版本号';
COMMENT ON COLUMN device_printer.deleted IS '逻辑删除';
COMMENT ON COLUMN device_printer.vendor_code IS '厂商代码';
COMMENT ON COLUMN device_printer.vendor_config_id IS '厂商配置ID';

-- 设备序列号唯一索引(未删除范围内)
CREATE UNIQUE INDEX IF NOT EXISTS uk_device_printer_sn ON device_printer (device_sn) WHERE deleted = false;
-- 商户号查询索引
CREATE INDEX IF NOT EXISTS idx_device_printer_mch_no ON device_printer (mch_no, deleted);

-- ===================================
-- 双因素认证: 用户 TOTP 绑定表
-- ===================================

-- 用户双因素认证绑定记录(一对一, 记录存在即代表该用户已启用 TOTP 双因素认证)
-- 移除最后验证时间字段(仅展示用途, 已废弃)
ALTER TABLE iam_user_two_factor DROP COLUMN IF EXISTS last_verify_time;

CREATE TABLE IF NOT EXISTS iam_user_two_factor (
    id                      bigint          NOT NULL,
    user_id                 bigint          NOT NULL,
    secret                  varchar(512)    NOT NULL,
    backup_codes            jsonb,
    backup_codes_remaining  int             NOT NULL DEFAULT 0,
    creator                 bigint,
    create_time             timestamptz(6),
    last_modifier           bigint,
    last_modified_time      timestamptz(6),
    version                 int             NOT NULL DEFAULT 0,
    deleted                 boolean         NOT NULL DEFAULT false,
    CONSTRAINT iam_user_two_factor_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE iam_user_two_factor IS '用户双因素认证绑定记录';
COMMENT ON COLUMN iam_user_two_factor.id IS '主键';
COMMENT ON COLUMN iam_user_two_factor.user_id IS '用户ID';
COMMENT ON COLUMN iam_user_two_factor.secret IS 'TOTP 密钥';
COMMENT ON COLUMN iam_user_two_factor.backup_codes IS '备用验证码';
COMMENT ON COLUMN iam_user_two_factor.backup_codes_remaining IS '剩余可用备用验证码数量';
COMMENT ON COLUMN iam_user_two_factor.creator IS '创建人ID';
COMMENT ON COLUMN iam_user_two_factor.create_time IS '创建时间';
COMMENT ON COLUMN iam_user_two_factor.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN iam_user_two_factor.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iam_user_two_factor.version IS '版本号(乐观锁)';
COMMENT ON COLUMN iam_user_two_factor.deleted IS '逻辑删除标志';

-- 用户ID唯一索引(未删除范围内, 一个用户最多一条绑定记录)
CREATE UNIQUE INDEX IF NOT EXISTS uk_iam_user_two_factor_user_id
    ON iam_user_two_factor (user_id) WHERE deleted = false;

-- ===================================
-- 支付通道路由: 场景模式配置表 + 基础模式配置表
-- 重构: 以通道商户号(channel_mch_no)替代支付产品(product)定位，场景模式新增支付能力(capability)
-- ===================================

DROP TABLE IF EXISTS pay_route_scene_config;
DROP TABLE IF EXISTS pay_route_basic_config;

-- 支付通道路由场景模式配置(每支付方式一条: 通道商户 + 支付能力)
CREATE TABLE IF NOT EXISTS pay_route_scene_config (
    id                  bigint          NOT NULL,
    strategy_id         bigint          NOT NULL,
    provider            varchar(32),
    channel             varchar(32),
    method              varchar(32)     NOT NULL,
    channel_mch_no      varchar(32)     NOT NULL,
    capability          varchar(32)     NOT NULL,
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int             NOT NULL DEFAULT 0,
    deleted             boolean         NOT NULL DEFAULT false,
    CONSTRAINT pay_route_scene_config_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE pay_route_scene_config IS '支付通道路由场景模式配置';
COMMENT ON COLUMN pay_route_scene_config.id IS '主键';
COMMENT ON COLUMN pay_route_scene_config.strategy_id IS '路由策略ID';
COMMENT ON COLUMN pay_route_scene_config.provider IS '支付渠道(派生自支付方式)';
COMMENT ON COLUMN pay_route_scene_config.channel IS '通道编码(派生自通道商户绑定的产品)';
COMMENT ON COLUMN pay_route_scene_config.method IS '支付方式编码';
COMMENT ON COLUMN pay_route_scene_config.channel_mch_no IS '通道商户号(唯一绑定支付产品)';
COMMENT ON COLUMN pay_route_scene_config.capability IS '支付能力编码';
COMMENT ON COLUMN pay_route_scene_config.creator IS '创建人ID';
COMMENT ON COLUMN pay_route_scene_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_route_scene_config.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN pay_route_scene_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_route_scene_config.version IS '版本号';
COMMENT ON COLUMN pay_route_scene_config.deleted IS '逻辑删除';

-- 策略 + 支付方式 唯一索引(未删除范围内)
CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_route_scene_config_method
    ON pay_route_scene_config (strategy_id, method) WHERE deleted = false;

-- 支付通道路由基础模式配置(每支付渠道一条: 通道商户)
CREATE TABLE IF NOT EXISTS pay_route_basic_config (
    id                  bigint          NOT NULL,
    strategy_id         bigint          NOT NULL,
    provider            varchar(32)     NOT NULL,
    channel_mch_no      varchar(32)     NOT NULL,
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int             NOT NULL DEFAULT 0,
    deleted             boolean         NOT NULL DEFAULT false,
    CONSTRAINT pay_route_basic_config_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE pay_route_basic_config IS '支付通道路由基础模式配置';
COMMENT ON COLUMN pay_route_basic_config.id IS '主键';
COMMENT ON COLUMN pay_route_basic_config.strategy_id IS '路由策略ID';
COMMENT ON COLUMN pay_route_basic_config.provider IS '支付渠道';
COMMENT ON COLUMN pay_route_basic_config.channel_mch_no IS '通道商户号(唯一绑定支付产品)';
COMMENT ON COLUMN pay_route_basic_config.creator IS '创建人ID';
COMMENT ON COLUMN pay_route_basic_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_route_basic_config.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN pay_route_basic_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_route_basic_config.version IS '版本号';
COMMENT ON COLUMN pay_route_basic_config.deleted IS '逻辑删除';

-- 策略 + 支付渠道 唯一索引(未删除范围内)
CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_route_basic_config_provider
    ON pay_route_basic_config (strategy_id, provider) WHERE deleted = false;
