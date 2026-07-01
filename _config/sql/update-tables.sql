-- 支付同步: pay_normal_order 冗余通道路由参数, 供同步时凭证解析使用

ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS channel_mch_no varchar(64);
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS capability varchar(64);

COMMENT ON COLUMN pay_normal_order.channel_mch_no IS '通道商户号(路由回填, 同步时用于解析通道应用凭证)';
COMMENT ON COLUMN pay_normal_order.capability IS '支付能力编码(路由回填, 同步时用于解析通道应用凭证)';
