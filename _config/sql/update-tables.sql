-- 支付通道路由策略表：移除开源版不需要的 enable/name 字段
ALTER TABLE pay_route_strategy DROP COLUMN IF EXISTS enable;
ALTER TABLE pay_route_strategy DROP COLUMN IF EXISTS name;
