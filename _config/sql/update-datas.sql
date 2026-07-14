-- 升级数据脚本（幂等）
-- 执行顺序: update-tables.sql → update-datas.sql
-- 2026-07-14: 删除聚合付款码 aggregate_pay_barcode；付款码改由平台按 authCode 前缀识别

-- 路由场景误配清理
DELETE FROM pay_route_scene_config WHERE method = 'aggregate_pay_barcode';

-- 产品能力 / 目录 / 主数据（按依赖顺序）
DELETE FROM pay_md_product_capability WHERE capability_code = 'aggregate_pay_barcode';
DELETE FROM pay_md_provider_method WHERE method = 'aggregate_pay_barcode';
DELETE FROM pay_md_method WHERE code = 'aggregate_pay_barcode';
DELETE FROM pay_md_capability WHERE code = 'aggregate_pay_barcode';

-- 河马付：分钱包付款码能力（替代原聚合付款码；id 与 pay_md.sql 对齐）
-- 列序: id, product_code, capability_code, sort_no, enabled, remark, deleted, last_modifier, last_modified_time, version, creator, create_time
INSERT INTO pay_md_product_capability (
    id, product_code, capability_code, sort_no, enabled, remark, deleted,
    last_modifier, last_modified_time, version, creator, create_time
)
SELECT 6201, 'hm_pay', 'wechat_barcode', 1, true, NULL, false, 1, NOW(), 0, 1, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM pay_md_product_capability
    WHERE product_code = 'hm_pay' AND capability_code = 'wechat_barcode' AND deleted = false
);

INSERT INTO pay_md_product_capability (
    id, product_code, capability_code, sort_no, enabled, remark, deleted,
    last_modifier, last_modified_time, version, creator, create_time
)
SELECT 6208, 'hm_pay', 'alipay_barcode', 8, true, NULL, false, 1, NOW(), 0, 1, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM pay_md_product_capability
    WHERE product_code = 'hm_pay' AND capability_code = 'alipay_barcode' AND deleted = false
);

-- 易宝：挂载聚合扫码能力
INSERT INTO pay_md_product_capability (
    id, product_code, capability_code, sort_no, enabled, remark, deleted,
    last_modifier, last_modified_time, version, creator, create_time
)
SELECT 21161, 'yee_pay', 'aggregate_pay_qrcode', 16, true, NULL, false, 1, NOW(), 0, 1, NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM pay_md_product_capability
    WHERE product_code = 'yee_pay' AND capability_code = 'aggregate_pay_qrcode' AND deleted = false
);
