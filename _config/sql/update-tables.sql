-- ===================================
-- 通知模块: 公告(广播) + 已读记录 + 个人消息(预留)
-- ===================================

-- 公告主体表(广播型通知, 1条公告 N人可见)
CREATE TABLE IF NOT EXISTS notify_notice (
    id                  bigint       NOT NULL,
    title               varchar(128) NOT NULL,
    content             text         NOT NULL,
    severity            varchar(16)  NOT NULL DEFAULT 'normal',
    is_top              boolean      NOT NULL DEFAULT false,
    effective_time      timestamptz(6),
    expire_time         timestamptz(6),
    status              varchar(16)  NOT NULL DEFAULT 'draft',
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int          NOT NULL DEFAULT 0,
    deleted             boolean      NOT NULL DEFAULT false,
    CONSTRAINT notify_notice_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE notify_notice IS '公告通知(广播型, 1条对多用户可见)';
COMMENT ON COLUMN notify_notice.id IS '主键';
COMMENT ON COLUMN notify_notice.title IS '标题';
COMMENT ON COLUMN notify_notice.content IS '正文(Markdown原文)';
COMMENT ON COLUMN notify_notice.severity IS '重要程度(normal普通/important重要)';
COMMENT ON COLUMN notify_notice.is_top IS '是否置顶';
COMMENT ON COLUMN notify_notice.effective_time IS '生效时间(为空则立即生效)';
COMMENT ON COLUMN notify_notice.expire_time IS '过期时间(为空则永久有效)';
COMMENT ON COLUMN notify_notice.status IS '状态(draft草稿/published发布/offline下线)';
COMMENT ON COLUMN notify_notice.creator IS '创建人ID';
COMMENT ON COLUMN notify_notice.create_time IS '创建时间';
COMMENT ON COLUMN notify_notice.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN notify_notice.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN notify_notice.version IS '版本号(乐观锁)';
COMMENT ON COLUMN notify_notice.deleted IS '逻辑删除标志';

CREATE INDEX IF NOT EXISTS idx_notify_notice_status ON notify_notice (status, deleted, effective_time, expire_time);

-- 公告已读记录(用户 x 公告, 记录已读/忽略状态)
CREATE TABLE IF NOT EXISTS notify_notice_read (
    id                  bigint       NOT NULL,
    user_id             bigint       NOT NULL,
    notice_id           bigint       NOT NULL,
    read_time           timestamptz(6),
    is_ignored          boolean      NOT NULL DEFAULT false,
    creator             bigint,
    create_time         timestamptz(6),
    CONSTRAINT notify_notice_read_pkey PRIMARY KEY (id),
    CONSTRAINT uk_notify_notice_read UNIQUE (user_id, notice_id)
);

COMMENT ON TABLE notify_notice_read IS '公告已读记录(用户x公告)';
COMMENT ON COLUMN notify_notice_read.id IS '主键';
COMMENT ON COLUMN notify_notice_read.user_id IS '用户ID';
COMMENT ON COLUMN notify_notice_read.notice_id IS '公告ID';
COMMENT ON COLUMN notify_notice_read.read_time IS '阅读时间';
COMMENT ON COLUMN notify_notice_read.is_ignored IS '是否忽略(用户主动隐藏该公告)';
COMMENT ON COLUMN notify_notice_read.creator IS '创建人ID';
COMMENT ON COLUMN notify_notice_read.create_time IS '创建时间';

CREATE INDEX IF NOT EXISTS idx_notify_notice_read_user ON notify_notice_read (user_id);

-- 个人消息表(定向通知, 本次预留建表, 暂不接入业务)
CREATE TABLE IF NOT EXISTS notify_message (
    id                  bigint       NOT NULL,
    user_id             bigint       NOT NULL,
    title               varchar(128) NOT NULL,
    content             varchar(1024),
    source              varchar(32),
    link                varchar(255),
    extra               text,
    is_read             boolean      NOT NULL DEFAULT false,
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int          NOT NULL DEFAULT 0,
    deleted             boolean      NOT NULL DEFAULT false,
    CONSTRAINT notify_message_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE notify_message IS '个人消息(定向通知, 1条对1用户)';
COMMENT ON COLUMN notify_message.id IS '主键';
COMMENT ON COLUMN notify_message.user_id IS '接收用户ID';
COMMENT ON COLUMN notify_message.title IS '标题';
COMMENT ON COLUMN notify_message.content IS '正文内容';
COMMENT ON COLUMN notify_message.source IS '业务来源(预留, 如TRADE/REFUND等)';
COMMENT ON COLUMN notify_message.link IS '跳转链接(内部路由或完整http外链)';
COMMENT ON COLUMN notify_message.extra IS '跳转附加参数(JSON字符串)';
COMMENT ON COLUMN notify_message.is_read IS '是否已读';
COMMENT ON COLUMN notify_message.creator IS '创建人ID';
COMMENT ON COLUMN notify_message.create_time IS '创建时间';
COMMENT ON COLUMN notify_message.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN notify_message.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN notify_message.version IS '版本号(乐观锁)';
COMMENT ON COLUMN notify_message.deleted IS '逻辑删除标志';

CREATE INDEX IF NOT EXISTS idx_notify_message_user ON notify_message (user_id, deleted, is_read);

-- ===================================
-- 商户门店(商户物理经营场所)
-- ===================================
CREATE TABLE IF NOT EXISTS mch_store_info (
    id                  bigint          NOT NULL,
    mch_no              varchar(32)     NOT NULL,
    store_no            varchar(32)     NOT NULL,
    store_name          varchar(128)    NOT NULL,
    contact_phone       varchar(32),
    logo_url            varchar(512),
    facade_url          varchar(512),
    interior_url        varchar(512),
    region_code         varchar(12),
    address             varchar(256),
    longitude           numeric(10,7),
    latitude            numeric(10,7),
    status              varchar(16)     NOT NULL DEFAULT 'enable',
    remark              varchar(512),
    creator             bigint,
    create_time         timestamptz(6),
    last_modifier       bigint,
    last_modified_time  timestamptz(6),
    version             int             NOT NULL DEFAULT 0,
    deleted             boolean         NOT NULL DEFAULT false,
    CONSTRAINT mch_store_info_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE mch_store_info IS '商户门店(商户物理经营场所)';
COMMENT ON COLUMN mch_store_info.id IS '主键';
COMMENT ON COLUMN mch_store_info.mch_no IS '商户号';
COMMENT ON COLUMN mch_store_info.store_no IS '门店号(系统生成, 唯一)';
COMMENT ON COLUMN mch_store_info.store_name IS '门店名称';
COMMENT ON COLUMN mch_store_info.contact_phone IS '联系人电话';
COMMENT ON COLUMN mch_store_info.logo_url IS '门店LOGO';
COMMENT ON COLUMN mch_store_info.facade_url IS '门头照';
COMMENT ON COLUMN mch_store_info.interior_url IS '门店内景照';
COMMENT ON COLUMN mch_store_info.region_code IS '行政区划代码(区县级)';
COMMENT ON COLUMN mch_store_info.address IS '详细地址';
COMMENT ON COLUMN mch_store_info.longitude IS '经度';
COMMENT ON COLUMN mch_store_info.latitude IS '纬度';
COMMENT ON COLUMN mch_store_info.status IS '状态(enable启用/disabled停用)';
COMMENT ON COLUMN mch_store_info.remark IS '备注';
COMMENT ON COLUMN mch_store_info.creator IS '创建人ID';
COMMENT ON COLUMN mch_store_info.create_time IS '创建时间';
COMMENT ON COLUMN mch_store_info.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN mch_store_info.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN mch_store_info.version IS '版本号(乐观锁)';
COMMENT ON COLUMN mch_store_info.deleted IS '逻辑删除标志';

-- 门店号唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_store_info_store_no ON mch_store_info (store_no);
-- 商户号查询索引
CREATE INDEX IF NOT EXISTS idx_mch_store_info_mch_no ON mch_store_info (mch_no, deleted);

-- ===================================
-- 云音响设备(商米云音响, 设备台账与商户/门店绑定关系)
-- ===================================
CREATE TABLE IF NOT EXISTS iot_speaker_device (
    id                  bigint          NOT NULL,
    mch_no              varchar(32)     NOT NULL,
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
    CONSTRAINT iot_speaker_device_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE iot_speaker_device IS '云音响设备(商米云音响, 设备台账与商户/门店绑定关系)';
COMMENT ON COLUMN iot_speaker_device.id IS '主键';
COMMENT ON COLUMN iot_speaker_device.mch_no IS '所属商户号';
COMMENT ON COLUMN iot_speaker_device.device_sn IS '商米设备序列号(SN)';
COMMENT ON COLUMN iot_speaker_device.imei IS '设备IMEI';
COMMENT ON COLUMN iot_speaker_device.shop_id IS '商米门店ID';
COMMENT ON COLUMN iot_speaker_device.device_name IS '设备名称';
COMMENT ON COLUMN iot_speaker_device.status IS '设备状态(unbound未绑定/online在线/offline离线/fault故障)';
COMMENT ON COLUMN iot_speaker_device.bind_time IS '绑定时间';
COMMENT ON COLUMN iot_speaker_device.last_online_time IS '最后在线时间';
COMMENT ON COLUMN iot_speaker_device.remark IS '备注';
COMMENT ON COLUMN iot_speaker_device.creator IS '创建人ID';
COMMENT ON COLUMN iot_speaker_device.create_time IS '创建时间';
COMMENT ON COLUMN iot_speaker_device.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN iot_speaker_device.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN iot_speaker_device.version IS '版本号(乐观锁)';
COMMENT ON COLUMN iot_speaker_device.deleted IS '逻辑删除标志';

-- 设备序列号唯一索引(未删除范围内)
CREATE UNIQUE INDEX IF NOT EXISTS uk_iot_speaker_device_sn ON iot_speaker_device (device_sn) WHERE deleted = false;
-- 商户号查询索引
CREATE INDEX IF NOT EXISTS idx_iot_speaker_device_mch_no ON iot_speaker_device (mch_no, deleted);
