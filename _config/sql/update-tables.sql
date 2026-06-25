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
