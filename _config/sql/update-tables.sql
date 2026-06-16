-- 订单商品明细列表
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS goods_detail jsonb;
