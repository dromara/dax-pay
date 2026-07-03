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


-- ------------------------------------------------------------
-- 退款订单
-- 记录每笔退款交易, 与原支付订单通过 order_no 关联, 支持多次部分退款
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_refund_order (
    id                      bigserial       PRIMARY KEY,
    mch_no                  varchar(32)     NOT NULL,
    app_id                  varchar(32),
    refund_no               varchar(64)     NOT NULL,
    biz_refund_no           varchar(64),
    title                   varchar(128),
    order_no                varchar(64)     NOT NULL,
    biz_order_no            varchar(64),
    out_order_no            varchar(64),
    out_refund_no           varchar(64),
    amount                  bigint          NOT NULL,
    order_amount            bigint,
    currency                varchar(8)      DEFAULT 'CNY',
    reason                  varchar(256),
    status                  varchar(32)     NOT NULL,
    finish_time             timestamptz(6),
    channel                 varchar(32),
    product                 varchar(32),
    method                  varchar(32),
    channel_mch_no          varchar(32),
    capability              varchar(64),
    notify_url              varchar(256),
    attach                  varchar(256),
    client_ip               varchar(64),
    error_msg               varchar(512),
    creator                 bigint,
    create_time             timestamptz(6)  DEFAULT now(),
    last_modifier           bigint,
    last_modified_time      timestamptz(6),
    version                 integer         NOT NULL DEFAULT 0,
    deleted                 boolean         NOT NULL DEFAULT false,
    CONSTRAINT uk_pay_refund_order_refund_no UNIQUE (refund_no)
);

COMMENT ON TABLE pay_refund_order IS '退款订单';

COMMENT ON COLUMN pay_refund_order.id IS '主键';
COMMENT ON COLUMN pay_refund_order.mch_no IS '商户号';
COMMENT ON COLUMN pay_refund_order.app_id IS '应用号';
COMMENT ON COLUMN pay_refund_order.refund_no IS '退款号(平台生成, 全局唯一, 对应通道 out_request_no)';
COMMENT ON COLUMN pay_refund_order.biz_refund_no IS '商户退款号(商户传入)';
COMMENT ON COLUMN pay_refund_order.title IS '标题';
COMMENT ON COLUMN pay_refund_order.order_no IS '原支付订单号(平台支付交易号 tradeNo)';
COMMENT ON COLUMN pay_refund_order.biz_order_no IS '商户业务订单号';
COMMENT ON COLUMN pay_refund_order.out_order_no IS '通道支付订单号(三方通道返回的 trade_no)';
COMMENT ON COLUMN pay_refund_order.out_refund_no IS '通道退款流水号(退款成功后由通道返回)';
COMMENT ON COLUMN pay_refund_order.amount IS '退款金额(最小货币单位, 分)';
COMMENT ON COLUMN pay_refund_order.order_amount IS '订单总金额(分, 冗余自原支付订单)';
COMMENT ON COLUMN pay_refund_order.currency IS '币种 ISO 4217';
COMMENT ON COLUMN pay_refund_order.reason IS '退款原因';
COMMENT ON COLUMN pay_refund_order.status IS '退款状态(init/progress/success/fail/close)';
COMMENT ON COLUMN pay_refund_order.finish_time IS '退款完成时间';
COMMENT ON COLUMN pay_refund_order.channel IS '支付通道';
COMMENT ON COLUMN pay_refund_order.product IS '支付产品编码';
COMMENT ON COLUMN pay_refund_order.method IS '支付方式';
COMMENT ON COLUMN pay_refund_order.channel_mch_no IS '通道商户号(路由回填)';
COMMENT ON COLUMN pay_refund_order.capability IS '支付能力编码(路由回填)';
COMMENT ON COLUMN pay_refund_order.notify_url IS '异步通知地址(出站商户通知用)';
COMMENT ON COLUMN pay_refund_order.attach IS '商户附加参数(回调原样返回)';
COMMENT ON COLUMN pay_refund_order.client_ip IS '客户端 IP';
COMMENT ON COLUMN pay_refund_order.error_msg IS '错误信息';
COMMENT ON COLUMN pay_refund_order.creator IS '创建人';
COMMENT ON COLUMN pay_refund_order.create_time IS '创建时间';
COMMENT ON COLUMN pay_refund_order.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_refund_order.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_refund_order.version IS '乐观锁版本号';
COMMENT ON COLUMN pay_refund_order.deleted IS '逻辑删除标志';


-- ------------------------------------------------------------
-- 支付同步记录
-- 记录每次资金交易状态同步的结果, 含通道返回原始报文与是否触发本地状态调整
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_sync_record (
    id                      bigserial       PRIMARY KEY,
    mch_no                  varchar(32)     NOT NULL,
    app_id                  varchar(32),
    trade_no                varchar(64)     NOT NULL,
    biz_trade_no            varchar(64),
    out_trade_no            varchar(64),
    out_trade_status        varchar(32),
    trade_type              varchar(32),
    product                 varchar(32),
    channel                 varchar(32),
    sync_info               text,
    adjust                  boolean         NOT NULL DEFAULT false,
    error_code              varchar(128),
    error_msg               varchar(300),
    client_ip               varchar(64),
    creator                 bigint,
    create_time             timestamptz(6)  DEFAULT now(),
    last_modifier           bigint,
    last_modified_time      timestamptz(6),
    version                 integer         NOT NULL DEFAULT 0,
    deleted                 boolean         NOT NULL DEFAULT false
);

COMMENT ON TABLE pay_sync_record IS '支付同步记录';

COMMENT ON COLUMN pay_sync_record.id IS '主键';
COMMENT ON COLUMN pay_sync_record.mch_no IS '商户号';
COMMENT ON COLUMN pay_sync_record.app_id IS '应用号';
COMMENT ON COLUMN pay_sync_record.trade_no IS '平台交易号(对应 pay_trade.trade_no)';
COMMENT ON COLUMN pay_sync_record.biz_trade_no IS '商户业务单号(对应 pay_normal_order.biz_order_no)';
COMMENT ON COLUMN pay_sync_record.out_trade_no IS '通道交易号(三方通道返回的订单号)';
COMMENT ON COLUMN pay_sync_record.out_trade_status IS '通道返回的资金状态';
COMMENT ON COLUMN pay_sync_record.trade_type IS '交易类型';
COMMENT ON COLUMN pay_sync_record.product IS '支付产品编码';
COMMENT ON COLUMN pay_sync_record.channel IS '支付通道';
COMMENT ON COLUMN pay_sync_record.sync_info IS '网关返回的同步原始报文(json)';
COMMENT ON COLUMN pay_sync_record.adjust IS '本地与通道状态不一致时是否进行了调整';
COMMENT ON COLUMN pay_sync_record.error_code IS '错误码';
COMMENT ON COLUMN pay_sync_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_sync_record.client_ip IS '客户端 IP';
COMMENT ON COLUMN pay_sync_record.creator IS '创建人';
COMMENT ON COLUMN pay_sync_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_sync_record.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_sync_record.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_sync_record.version IS '乐观锁版本号';
COMMENT ON COLUMN pay_sync_record.deleted IS '逻辑删除标志';


-- ------------------------------------------------------------
-- 支付关闭记录
-- 记录每次支付关闭/撤销操作的结果, 通过 close_type 区分关闭(close)与撤销(cancel)
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_close_record (
    id                      bigserial       PRIMARY KEY,
    mch_no                  varchar(32)     NOT NULL,
    app_id                  varchar(32),
    trade_no                varchar(64)     NOT NULL,
    biz_trade_no            varchar(64),
    product                 varchar(32),
    channel                 varchar(32),
    closed                  boolean         NOT NULL DEFAULT false,
    close_type              varchar(32),
    error_code              varchar(128),
    error_msg               varchar(300),
    client_ip               varchar(64),
    creator                 bigint,
    create_time             timestamptz(6)  DEFAULT now(),
    last_modifier           bigint,
    last_modified_time      timestamptz(6),
    version                 integer         NOT NULL DEFAULT 0,
    deleted                 boolean         NOT NULL DEFAULT false
);

COMMENT ON TABLE pay_close_record IS '支付关闭记录';

COMMENT ON COLUMN pay_close_record.id IS '主键';
COMMENT ON COLUMN pay_close_record.mch_no IS '商户号';
COMMENT ON COLUMN pay_close_record.app_id IS '应用号';
COMMENT ON COLUMN pay_close_record.trade_no IS '平台交易号(对应 pay_trade.trade_no)';
COMMENT ON COLUMN pay_close_record.biz_trade_no IS '商户业务单号(对应 pay_normal_order.biz_order_no)';
COMMENT ON COLUMN pay_close_record.product IS '支付产品编码';
COMMENT ON COLUMN pay_close_record.channel IS '支付通道';
COMMENT ON COLUMN pay_close_record.closed IS '是否关闭成功';
COMMENT ON COLUMN pay_close_record.close_type IS '关闭类型(close/cancel)';
COMMENT ON COLUMN pay_close_record.error_code IS '错误码';
COMMENT ON COLUMN pay_close_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_close_record.client_ip IS '客户端 IP';
COMMENT ON COLUMN pay_close_record.creator IS '创建人';
COMMENT ON COLUMN pay_close_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_close_record.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_close_record.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_close_record.version IS '乐观锁版本号';
COMMENT ON COLUMN pay_close_record.deleted IS '逻辑删除标志';
