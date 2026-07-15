-- 付款码列：bar_code → auth_code（与 unipay 入参 authCode 对齐）
ALTER TABLE pay_normal_order RENAME COLUMN bar_code TO auth_code;
COMMENT ON COLUMN pay_normal_order.auth_code IS '付款码（被扫支付，审计保留）';

-- 订单来源: 容器权威 + pay_trade 冗余(方案 C)
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS source varchar(32);
COMMENT ON COLUMN pay_normal_order.source IS '订单来源(业务入口权威, TradeSourceEnum)';

ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS source varchar(32);
COMMENT ON COLUMN pay_gateway_order.source IS '订单来源(业务入口权威, TradeSourceEnum; 预下单写入)';

-- 历史数据: 从 pay_trade 回填容器(仅空值)
UPDATE pay_normal_order o
SET source = t.source
FROM pay_trade t
WHERE t.container_id = o.id
  AND t.trade_type = 'normal'
  AND t.deleted = false
  AND o.source IS NULL
  AND t.source IS NOT NULL;

UPDATE pay_gateway_order o
SET source = t.source
FROM pay_trade t
WHERE t.container_id = o.id
  AND t.trade_type = 'gateway'
  AND t.deleted = false
  AND o.source IS NULL
  AND t.source IS NOT NULL;

-- 网关预下单尚未建 trade 的: 按 gateway_type 派生
UPDATE pay_gateway_order
SET source = CASE gateway_type
    WHEN 'cashier' THEN 'cashier'
    ELSE 'aggress_pay'
END
WHERE source IS NULL
  AND gateway_type IS NOT NULL;

-- ============================================================
-- 易支付协议插件表
-- ============================================================

-- 易支付凭证（应用级）
CREATE TABLE IF NOT EXISTS pay_easy_pay_credential (
    id                  int8            NOT NULL,
    pid                 int4            NOT NULL,
    app_id              varchar(32)     NOT NULL,
    enable              bool            NOT NULL DEFAULT false,
    enable_v1           bool            NOT NULL DEFAULT false,
    enable_v2           bool            NOT NULL DEFAULT true,
    md5_key             varchar(128),
    use_system_key      bool            NOT NULL DEFAULT true,
    public_key          text,
    mch_no              varchar(32)     NOT NULL,
    creator             int8,
    create_time         timestamptz(6),
    last_modifier       int8,
    last_modified_time  timestamptz(6),
    version             int4            NOT NULL DEFAULT 0,
    deleted             bool            NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_easy_pay_credential PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_easy_pay_credential_app_id ON pay_easy_pay_credential (app_id) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_easy_pay_credential_pid ON pay_easy_pay_credential (pid) WHERE deleted = false;

COMMENT ON TABLE pay_easy_pay_credential IS '易支付凭证配置表（应用级）';
COMMENT ON COLUMN pay_easy_pay_credential.id IS '主键';
COMMENT ON COLUMN pay_easy_pay_credential.pid IS '易支付商户号';
COMMENT ON COLUMN pay_easy_pay_credential.app_id IS '应用号（支付出口）';
COMMENT ON COLUMN pay_easy_pay_credential.enable IS '是否启用';
COMMENT ON COLUMN pay_easy_pay_credential.enable_v1 IS '是否开启V1接口';
COMMENT ON COLUMN pay_easy_pay_credential.enable_v2 IS '是否开启V2接口';
COMMENT ON COLUMN pay_easy_pay_credential.md5_key IS 'V1 MD5密钥';
COMMENT ON COLUMN pay_easy_pay_credential.use_system_key IS 'V2是否使用系统公私钥';
COMMENT ON COLUMN pay_easy_pay_credential.public_key IS '商户RSA公钥';
COMMENT ON COLUMN pay_easy_pay_credential.mch_no IS '商户号';
COMMENT ON COLUMN pay_easy_pay_credential.creator IS '创建人';
COMMENT ON COLUMN pay_easy_pay_credential.create_time IS '创建时间';
COMMENT ON COLUMN pay_easy_pay_credential.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_easy_pay_credential.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_easy_pay_credential.version IS '乐观锁版本';
COMMENT ON COLUMN pay_easy_pay_credential.deleted IS '逻辑删除';

-- 易支付场景配置
CREATE TABLE IF NOT EXISTS pay_easy_pay_config (
    id                  int8            NOT NULL,
    pid                 int4            NOT NULL,
    app_id              varchar(32)     NOT NULL,
    limit_pay           varchar(64),
    mch_no              varchar(32)     NOT NULL,
    creator             int8,
    create_time         timestamptz(6),
    last_modifier       int8,
    last_modified_time  timestamptz(6),
    version             int4            NOT NULL DEFAULT 0,
    deleted             bool            NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_easy_pay_config PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_easy_pay_config_app_id ON pay_easy_pay_config (app_id) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_easy_pay_config_pid ON pay_easy_pay_config (pid) WHERE deleted = false;

COMMENT ON TABLE pay_easy_pay_config IS '易支付场景配置表';
COMMENT ON COLUMN pay_easy_pay_config.id IS '主键';
COMMENT ON COLUMN pay_easy_pay_config.pid IS '易支付商户号';
COMMENT ON COLUMN pay_easy_pay_config.app_id IS '应用号';
COMMENT ON COLUMN pay_easy_pay_config.limit_pay IS '限制支付方式';
COMMENT ON COLUMN pay_easy_pay_config.mch_no IS '商户号';
COMMENT ON COLUMN pay_easy_pay_config.creator IS '创建人';
COMMENT ON COLUMN pay_easy_pay_config.create_time IS '创建时间';
COMMENT ON COLUMN pay_easy_pay_config.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_easy_pay_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_easy_pay_config.version IS '乐观锁版本';
COMMENT ON COLUMN pay_easy_pay_config.deleted IS '逻辑删除';

-- 易支付协议订单
CREATE TABLE IF NOT EXISTS pay_easy_pay_order (
    id                  int8            NOT NULL,
    order_id            int8,
    pid                 int4            NOT NULL,
    app_id              varchar(32)     NOT NULL,
    trade_no            varchar(64),
    out_trade_no        varchar(100)    NOT NULL,
    api_trade_no        varchar(64),
    type                varchar(32),
    status              int4            NOT NULL DEFAULT 0,
    add_time            timestamptz(6),
    end_time            timestamptz(6),
    name                varchar(200),
    money               numeric(16, 2)  NOT NULL,
    refund_money        numeric(16, 2)  DEFAULT 0,
    notify_url          varchar(500),
    return_url          varchar(500),
    param               varchar(500),
    buyer               varchar(128),
    client_ip           varchar(64),
    api_version         varchar(8),
    pc_call_type        varchar(32),
    pay_url             varchar(1000),
    pay_body            text,
    mch_no              varchar(32)     NOT NULL,
    creator             int8,
    create_time         timestamptz(6),
    last_modifier       int8,
    last_modified_time  timestamptz(6),
    version             int4            NOT NULL DEFAULT 0,
    deleted             bool            NOT NULL DEFAULT false,
    CONSTRAINT pk_pay_easy_pay_order PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_easy_pay_order_app_out ON pay_easy_pay_order (app_id, out_trade_no) WHERE deleted = false;
CREATE INDEX IF NOT EXISTS idx_easy_pay_order_pid_out ON pay_easy_pay_order (pid, out_trade_no);
CREATE INDEX IF NOT EXISTS idx_easy_pay_order_trade_no ON pay_easy_pay_order (trade_no);
CREATE INDEX IF NOT EXISTS idx_easy_pay_order_order_id ON pay_easy_pay_order (order_id);

COMMENT ON TABLE pay_easy_pay_order IS '易支付协议订单表';
COMMENT ON COLUMN pay_easy_pay_order.id IS '主键（收银台路径参数）';
COMMENT ON COLUMN pay_easy_pay_order.order_id IS '关联内核容器ID';
COMMENT ON COLUMN pay_easy_pay_order.pid IS '易支付商户号';
COMMENT ON COLUMN pay_easy_pay_order.app_id IS '应用号';
COMMENT ON COLUMN pay_easy_pay_order.trade_no IS '平台业务单号（容器orderNo）';
COMMENT ON COLUMN pay_easy_pay_order.out_trade_no IS '商户订单号';
COMMENT ON COLUMN pay_easy_pay_order.api_trade_no IS '通道订单号';
COMMENT ON COLUMN pay_easy_pay_order.type IS '协议支付方式 alipay/wxpay/aggregate';
COMMENT ON COLUMN pay_easy_pay_order.status IS '协议状态 0待付 1成功';
COMMENT ON COLUMN pay_easy_pay_order.add_time IS '创建时间';
COMMENT ON COLUMN pay_easy_pay_order.end_time IS '完成时间';
COMMENT ON COLUMN pay_easy_pay_order.name IS '商品名称';
COMMENT ON COLUMN pay_easy_pay_order.money IS '订单金额（元）';
COMMENT ON COLUMN pay_easy_pay_order.refund_money IS '已退款金额（元）';
COMMENT ON COLUMN pay_easy_pay_order.notify_url IS '异步通知地址（本期仅落库）';
COMMENT ON COLUMN pay_easy_pay_order.return_url IS '同步跳转地址';
COMMENT ON COLUMN pay_easy_pay_order.param IS '业务扩展参数';
COMMENT ON COLUMN pay_easy_pay_order.buyer IS '支付用户标识';
COMMENT ON COLUMN pay_easy_pay_order.client_ip IS '客户端IP';
COMMENT ON COLUMN pay_easy_pay_order.api_version IS 'API版本 v1/v2';
COMMENT ON COLUMN pay_easy_pay_order.pc_call_type IS '支付调用方式';
COMMENT ON COLUMN pay_easy_pay_order.pay_url IS '支付链接';
COMMENT ON COLUMN pay_easy_pay_order.pay_body IS '支付参数体';
COMMENT ON COLUMN pay_easy_pay_order.mch_no IS '商户号';
COMMENT ON COLUMN pay_easy_pay_order.creator IS '创建人';
COMMENT ON COLUMN pay_easy_pay_order.create_time IS '创建时间';
COMMENT ON COLUMN pay_easy_pay_order.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_easy_pay_order.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_easy_pay_order.version IS '乐观锁版本';
COMMENT ON COLUMN pay_easy_pay_order.deleted IS '逻辑删除';

-- ============================================================
-- 订单门店维度: store_no（线下经营归属，可空）

-- ============================================================
-- 订单门店维度: store_no（线下经营归属，可空）
-- ============================================================
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS store_no varchar(32);
COMMENT ON COLUMN pay_normal_order.store_no IS '门店号(线下经营归属, 可空; 对应 mch_store_info.store_no)';

ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS store_no varchar(32);
COMMENT ON COLUMN pay_gateway_order.store_no IS '门店号(线下经营归属, 可空; 对应 mch_store_info.store_no)';

ALTER TABLE pay_refund_order ADD COLUMN IF NOT EXISTS store_no varchar(32);
COMMENT ON COLUMN pay_refund_order.store_no IS '门店号(继承自原支付容器, 可空)';

CREATE INDEX IF NOT EXISTS idx_normal_order_mch_store ON pay_normal_order (mch_no, store_no);
CREATE INDEX IF NOT EXISTS idx_gateway_order_mch_store ON pay_gateway_order (mch_no, store_no);
CREATE INDEX IF NOT EXISTS idx_refund_order_mch_store ON pay_refund_order (mch_no, store_no);

