-- 命名治理迁移 (2026-07-15)
-- 目标: 汇付通道 huifu / 抖音通道 douyin / clientEnv wechat / 银联 capability·method 与 wechat_* 形态一致
-- 全量种子以 pay_md.sql 为准；本文件用于已部署库升级

-- ========== 1. 主数据: 支付通道 ==========
-- 抖音通道 code: douyin_pay → douyin
UPDATE pay_md_channel SET code = 'douyin' WHERE code = 'douyin_pay';

-- 汇付: 合并 ada_pay / dougong_pay 为 huifu（保留 id=11 行，删除独立 dougong 通道行）
UPDATE pay_md_channel
SET code = 'huifu',
    description = '汇付天下(Adapay/斗拱)',
    icon = 'huifu'
WHERE code = 'ada_pay';

DELETE FROM pay_md_channel WHERE code = 'dougong_pay';

-- 产品归属通道
UPDATE pay_md_product SET channel = 'huifu' WHERE code IN ('ada_pay', 'dougong_pay') AND channel IN ('ada_pay', 'dougong_pay');
UPDATE pay_md_product SET channel = 'douyin' WHERE code = 'douyin_pay' AND channel = 'douyin_pay';

-- 产品配置表 channel 列
UPDATE pay_md_product_config SET channel = 'douyin' WHERE product = 'douyin_pay' AND channel = 'douyin_pay';
UPDATE pay_md_product_config SET channel = 'huifu' WHERE product IN ('ada_pay', 'dougong_pay') AND channel IN ('ada_pay', 'dougong_pay');

-- ========== 2. 主数据: 银联 capability / method ==========
UPDATE pay_md_capability SET code = 'union_qr' WHERE code = 'union_pay_qr';
UPDATE pay_md_capability SET code = 'union_jsapi' WHERE code = 'union_pay_jsapi';
UPDATE pay_md_capability SET code = 'union_h5' WHERE code = 'union_pay_h5';
UPDATE pay_md_capability SET code = 'union_barcode' WHERE code = 'union_pay_barcode';

UPDATE pay_md_method SET code = 'union_barcode' WHERE code = 'union_pay_barcode';
UPDATE pay_md_provider_method SET method = 'union_barcode' WHERE method = 'union_pay_barcode';

UPDATE pay_md_product_capability SET capability = 'union_qr' WHERE capability = 'union_pay_qr';
UPDATE pay_md_product_capability SET capability = 'union_jsapi' WHERE capability = 'union_pay_jsapi';
UPDATE pay_md_product_capability SET capability = 'union_h5' WHERE capability = 'union_pay_h5';
UPDATE pay_md_product_capability SET capability = 'union_barcode' WHERE capability = 'union_pay_barcode';

-- ========== 3. 业务表: channel 冗余字段（有则更新，无表则跳过由执行者按需裁剪） ==========
-- 订单/交易上的通道编码
UPDATE pay_trade SET channel = 'huifu' WHERE channel IN ('ada_pay', 'dougong_pay');
UPDATE pay_trade SET channel = 'douyin' WHERE channel = 'douyin_pay';

-- 通道商户表仅存 product, 无独立 channel 列; 通过 product 反查通道, 无需 UPDATE

-- ========== 4. 网关配置: clientEnv wechat_pay → wechat ==========
UPDATE pay_gateway_aggregate_client_env SET client_env = 'wechat' WHERE client_env = 'wechat_pay';
UPDATE pay_gateway_code_client_env SET client_env = 'wechat' WHERE client_env = 'wechat_pay';
UPDATE pay_gateway_cashier_item SET client_env = 'wechat' WHERE client_env = 'wechat_pay';

-- ========== 5. 路由/聚合 DIRECT 等落库的 capability ==========
UPDATE pay_route_scene_config SET capability = 'union_qr' WHERE capability = 'union_pay_qr';
UPDATE pay_route_scene_config SET capability = 'union_jsapi' WHERE capability = 'union_pay_jsapi';
UPDATE pay_route_scene_config SET capability = 'union_h5' WHERE capability = 'union_pay_h5';
UPDATE pay_route_scene_config SET capability = 'union_barcode' WHERE capability = 'union_pay_barcode';

UPDATE pay_gateway_aggregate_client_env SET capability = 'union_qr' WHERE capability = 'union_pay_qr';
UPDATE pay_gateway_aggregate_client_env SET capability = 'union_jsapi' WHERE capability = 'union_pay_jsapi';
UPDATE pay_gateway_aggregate_client_env SET capability = 'union_h5' WHERE capability = 'union_pay_h5';
UPDATE pay_gateway_aggregate_client_env SET capability = 'union_barcode' WHERE capability = 'union_pay_barcode';

UPDATE pay_gateway_code_client_env SET capability = 'union_qr' WHERE capability = 'union_pay_qr';
UPDATE pay_gateway_code_client_env SET capability = 'union_jsapi' WHERE capability = 'union_pay_jsapi';
UPDATE pay_gateway_code_client_env SET capability = 'union_h5' WHERE capability = 'union_pay_h5';
UPDATE pay_gateway_code_client_env SET capability = 'union_barcode' WHERE capability = 'union_pay_barcode';

UPDATE pay_gateway_cashier_item SET capability = 'union_qr' WHERE capability = 'union_pay_qr';
UPDATE pay_gateway_cashier_item SET capability = 'union_jsapi' WHERE capability = 'union_pay_jsapi';
UPDATE pay_gateway_cashier_item SET capability = 'union_h5' WHERE capability = 'union_pay_h5';
UPDATE pay_gateway_cashier_item SET capability = 'union_barcode' WHERE capability = 'union_pay_barcode';

-- 支付方式 method 付款码（若业务表存 method）
UPDATE pay_route_scene_config SET method = 'union_barcode' WHERE method = 'union_pay_barcode';
UPDATE pay_gateway_aggregate_client_env SET method = 'union_barcode' WHERE method = 'union_pay_barcode';
UPDATE pay_gateway_code_client_env SET method = 'union_barcode' WHERE method = 'union_pay_barcode';
UPDATE pay_gateway_cashier_item SET method = 'union_barcode' WHERE method = 'union_pay_barcode';
UPDATE pay_trade SET method = 'union_barcode' WHERE method = 'union_pay_barcode';
