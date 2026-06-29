-- 支付通道路由策略表：移除开源版不需要的 enable/name 字段
ALTER TABLE pay_route_strategy DROP COLUMN IF EXISTS enable;
ALTER TABLE pay_route_strategy DROP COLUMN IF EXISTS name;

-- 支付宝直连商户应用支付能力关联表
CREATE TABLE IF NOT EXISTS alipay_direct_app_capability (
    id                   BIGSERIAL PRIMARY KEY,
    mch_no               VARCHAR(32)  NOT NULL,
    channel_mch_no       VARCHAR(64)  NOT NULL,
    capability           VARCHAR(32)  NOT NULL,
    alipay_direct_app_id BIGINT       NOT NULL,
    create_time          TIMESTAMPTZ(6) DEFAULT NOW(),
    update_time          TIMESTAMPTZ(6) DEFAULT NOW(),
    deleted              BOOLEAN      DEFAULT FALSE
);

COMMENT ON TABLE  alipay_direct_app_capability IS '支付宝直连商户应用支付能力关联';
COMMENT ON COLUMN alipay_direct_app_capability.id IS '主键';
COMMENT ON COLUMN alipay_direct_app_capability.mch_no IS '商户号';
COMMENT ON COLUMN alipay_direct_app_capability.channel_mch_no IS '通道商户号';
COMMENT ON COLUMN alipay_direct_app_capability.capability IS '支付能力编码';
COMMENT ON COLUMN alipay_direct_app_capability.alipay_direct_app_id IS '关联支付宝直连应用ID';
COMMENT ON COLUMN alipay_direct_app_capability.create_time IS '创建时间';
COMMENT ON COLUMN alipay_direct_app_capability.update_time IS '更新时间';
COMMENT ON COLUMN alipay_direct_app_capability.deleted IS '删除标志';

CREATE UNIQUE INDEX IF NOT EXISTS uk_alipay_direct_app_cap ON alipay_direct_app_capability (channel_mch_no, capability) WHERE deleted = FALSE;

