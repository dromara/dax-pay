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
-- 2026-07-11 开源版取消硬件对接：移除云音箱/云打印/厂商配置/辅助终端菜单
-- 码牌(device:qrcode)与目录 device 保留；硬件能力归商业版
-- ----------------------------
DELETE FROM "public"."iam_perm_menu"
WHERE "menu_code" IN (
    'device:vendor_config',
    'device:speaker',
    'device:printer',
    'device:assistant'
);

-- 目录图标由音箱改为码牌语义（可选，已有库同步）
UPDATE "public"."iam_perm_menu"
SET "icon" = 'lucide:qr-code',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 9 AND "menu_code" = 'device';
