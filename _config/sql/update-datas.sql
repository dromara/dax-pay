-- 升级数据脚本（按顺序执行）
-- 说明: 已部署环境增量更新，执行前请确认版本与备份

-- ----------------------------
-- 2026-07-10 移动端应用配置菜单从 payment 迁到 system 配置
-- 后端包: payment.app.mobile → platform.system.mobile
-- API: /admin/mobile-app → /platform/config/mobile-app
-- 前端: payment/config/mobileApp → system/config/mobileApp
-- ----------------------------
UPDATE "public"."iam_perm_menu"
SET "pid" = 304,
    "menu_code" = 'system:config:mobile_app',
    "i18n_key" = 'menu.system.config.mobileApp',
    "component" = '/system/config/mobileApp/MobileAppConfig',
    "path" = '/system/config/mobile-app',
    "sort_no" = 6,
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 40504;

UPDATE "public"."iam_perm_menu"
SET "menu_code" = 'system:config:mobile_app_detail',
    "i18n_key" = 'menu.system.config.mobileAppDetail',
    "component" = '/system/config/mobileApp/detail/MobileAppDetail',
    "path" = '/system/config/mobile-app/detail/:appType',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 40505;

-- ----------------------------
-- 2026-07-11 开源版取消硬件对接：移除云音箱/云打印/厂商配置菜单
-- 码牌(device:qrcode)、辅助支付终端(device:assistant)、目录 device 保留
-- 辅助终端不属于 IoT 硬件对接，勿与音箱/打印一并删除
-- ----------------------------
DELETE FROM "public"."iam_perm_menu"
WHERE "menu_code" IN (
    'device:vendor_config',
    'device:speaker',
    'device:printer'
);

-- 若误删过辅助支付终端菜单，补回（已存在则跳过）
INSERT INTO "public"."iam_perm_menu"
SELECT 904, 9, 'device:assistant', 'admin', 'DeviceAssistant', '辅助支付终端', 'Payment Assistant Terminal',
       'menu.device.assistant', 'lucide:monitor-smartphone', 'f', 'f',
       '/_core/fallback/coming-soon', '/device/assistant', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu',
       NULL, NULL, NULL, NULL, NULL, NULL,
       '2026-06-25 12:51:25.252086+00', '2026-06-25 12:51:25.252086+00'
WHERE NOT EXISTS (
    SELECT 1 FROM "public"."iam_perm_menu" WHERE "id" = 904 OR "menu_code" = 'device:assistant'
);

-- 目录图标由音箱改为码牌语义（可选，已有库同步）
UPDATE "public"."iam_perm_menu"
SET "icon" = 'lucide:qr-code',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 9 AND "menu_code" = 'device';

-- ----------------------------
-- 聚合扫码配置菜单（应用级子页面）
-- 对接后端 GatewayAggregateConfigAdminController, 三级配置深度(AUTO/METHOD/DIRECT)
-- ----------------------------
INSERT INTO "public"."iam_perm_menu"
SELECT 4040120, 40401, 'merchant:gateway-aggregate', 'admin', 'AggregateScanConfig', '聚合扫码', 'Aggregate QR Pay',
       'menu.payment.merchant.aggregateScan', NULL, 't', 'f',
       '/payment/merchant/aggregate/AggregateScanConfig', '/payment/merchant/aggregate', NULL, 3, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage',
       NULL, NULL, NULL, NULL, NULL, NULL,
       CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM "public"."iam_perm_menu" WHERE "id" = 4040120 OR "menu_code" = 'merchant:gateway-aggregate'
);
