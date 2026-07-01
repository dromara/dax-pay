-- ============================================================
-- 升级建表脚本
-- ============================================================


-- ------------------------------------------------------------
-- 微信服务商应用支付能力关联（全局维度，一个能力绑定一个服务商应用）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS wechat_isv_app_capability (
    id                      bigserial       PRIMARY KEY,
    capability              varchar(64)     NOT NULL,
    wechat_isv_app_id       bigint          NOT NULL,
    creator                 bigint,
    create_time             timestamptz(6)  DEFAULT now(),
    last_modifier           bigint,
    last_modified_time      timestamptz(6),
    version                 integer         NOT NULL DEFAULT 0,
    deleted                 boolean         NOT NULL DEFAULT false,
    CONSTRAINT uk_wechat_isv_app_capability UNIQUE (capability)
);

COMMENT ON TABLE wechat_isv_app_capability IS '微信服务商应用支付能力关联';

COMMENT ON COLUMN wechat_isv_app_capability.id IS '主键';
COMMENT ON COLUMN wechat_isv_app_capability.capability IS '支付能力编码';
COMMENT ON COLUMN wechat_isv_app_capability.wechat_isv_app_id IS '关联微信服务商应用ID';
COMMENT ON COLUMN wechat_isv_app_capability.creator IS '创建人';
COMMENT ON COLUMN wechat_isv_app_capability.create_time IS '创建时间';
COMMENT ON COLUMN wechat_isv_app_capability.last_modifier IS '最后修改人';
COMMENT ON COLUMN wechat_isv_app_capability.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN wechat_isv_app_capability.version IS '乐观锁版本号';
COMMENT ON COLUMN wechat_isv_app_capability.deleted IS '逻辑删除标志';


-- ------------------------------------------------------------
-- 微信直连商户应用支付能力关联（商户维度，同一通道商户下一个能力绑定一个应用）
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS wechat_direct_app_capability (
    id                      bigserial       PRIMARY KEY,
    mch_no                  varchar(32)     NOT NULL,
    channel_mch_no          varchar(32)     NOT NULL,
    capability              varchar(64)     NOT NULL,
    wechat_direct_app_id    bigint          NOT NULL,
    creator                 bigint,
    create_time             timestamptz(6)  DEFAULT now(),
    last_modifier           bigint,
    last_modified_time      timestamptz(6),
    version                 integer         NOT NULL DEFAULT 0,
    deleted                 boolean         NOT NULL DEFAULT false,
    CONSTRAINT uk_wechat_direct_app_capability UNIQUE (channel_mch_no, capability)
);

COMMENT ON TABLE wechat_direct_app_capability IS '微信直连商户应用支付能力关联';

COMMENT ON COLUMN wechat_direct_app_capability.id IS '主键';
COMMENT ON COLUMN wechat_direct_app_capability.mch_no IS '商户号';
COMMENT ON COLUMN wechat_direct_app_capability.channel_mch_no IS '通道商户号';
COMMENT ON COLUMN wechat_direct_app_capability.capability IS '支付能力编码';
COMMENT ON COLUMN wechat_direct_app_capability.wechat_direct_app_id IS '关联微信直连应用ID';
COMMENT ON COLUMN wechat_direct_app_capability.creator IS '创建人';
COMMENT ON COLUMN wechat_direct_app_capability.create_time IS '创建时间';
COMMENT ON COLUMN wechat_direct_app_capability.last_modifier IS '最后修改人';
COMMENT ON COLUMN wechat_direct_app_capability.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN wechat_direct_app_capability.version IS '乐观锁版本号';
COMMENT ON COLUMN wechat_direct_app_capability.deleted IS '逻辑删除标志';


-- ------------------------------------------------------------
-- 抖音直连商户应用支付能力关联
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS douyin_direct_app_capability (
    id                      bigserial       PRIMARY KEY,
    mch_no                  varchar(32)     NOT NULL,
    channel_mch_no          varchar(32)     NOT NULL,
    capability              varchar(64)     NOT NULL,
    douyin_direct_app_id    bigint          NOT NULL,
    creator                 bigint,
    create_time             timestamptz(6)  DEFAULT now(),
    last_modifier           bigint,
    last_modified_time      timestamptz(6),
    version                 integer         NOT NULL DEFAULT 0,
    deleted                 boolean         NOT NULL DEFAULT false,
    CONSTRAINT uk_douyin_direct_app_capability UNIQUE (channel_mch_no, capability)
);

COMMENT ON TABLE douyin_direct_app_capability IS '抖音直连商户应用支付能力关联';

COMMENT ON COLUMN douyin_direct_app_capability.id IS '主键';
COMMENT ON COLUMN douyin_direct_app_capability.mch_no IS '商户号';
COMMENT ON COLUMN douyin_direct_app_capability.channel_mch_no IS '通道商户号';
COMMENT ON COLUMN douyin_direct_app_capability.capability IS '支付能力编码';
COMMENT ON COLUMN douyin_direct_app_capability.douyin_direct_app_id IS '关联抖音直连应用ID';
COMMENT ON COLUMN douyin_direct_app_capability.creator IS '创建人';
COMMENT ON COLUMN douyin_direct_app_capability.create_time IS '创建时间';
COMMENT ON COLUMN douyin_direct_app_capability.last_modifier IS '最后修改人';
COMMENT ON COLUMN douyin_direct_app_capability.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN douyin_direct_app_capability.version IS '乐观锁版本号';
COMMENT ON COLUMN douyin_direct_app_capability.deleted IS '逻辑删除标志';
