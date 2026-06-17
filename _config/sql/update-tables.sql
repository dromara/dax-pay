-- 订单商品明细列表
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS goods_detail jsonb;

-- 支付产品配置增加启用/停用开关
ALTER TABLE pay_product_config ADD COLUMN IF NOT EXISTS enabled boolean NOT NULL DEFAULT TRUE;
