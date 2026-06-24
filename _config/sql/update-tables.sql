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
