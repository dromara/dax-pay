-- 表结构更新(2026-08-30): notify_mail_record 两列注释精简为紧凑枚举值风格
COMMENT ON COLUMN public.notify_mail_record.business_type IS '业务场景(test/manual等)';
COMMENT ON COLUMN public.notify_mail_record.status IS '发送状态(sending/success/fail)';

-- 表结构更新(2026-09-06): 风控精简, 移除门店地理围栏(移交商业版)
-- 命中记录去掉围栏快照列(client_city 保留, 省市地区黑名单命中仍记录 IP 归属城市)
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS store_city;
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS store_no;
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS geo_fence_strategy;
-- 商户级围栏 opt-in 配置表随围栏功能整体移除
DROP TABLE IF EXISTS mch_risk_config;


-- 风控精简: 移除城市接壤关系表(围栏 balanced 邻市策略专用, 随门店地理围栏一并移交商业版)
DROP TABLE IF EXISTS base_city_adjacent;
DROP SEQUENCE IF EXISTS base_city_adjacent_id_seq;
