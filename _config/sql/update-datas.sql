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
-- 注意: id 不能用 4040120, 该 ID 已被"微信域名验证"(merchant:wx_verify)占用
-- ----------------------------
INSERT INTO "public"."iam_perm_menu" VALUES (
    4040121, 40401, 'merchant:gateway-aggregate', 'admin', 'AggregateScanConfig',
    '聚合扫码', 'Aggregate QR Pay', 'menu.payment.merchant.aggregateScan',
    NULL, 't', 'f',
    '/payment/merchant/aggregate/AggregateScanConfig', '/payment/merchant/aggregate',
    NULL, 3, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage',
    NULL, NULL, NULL, NULL, NULL, NULL,
    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
);

-- ----------------------------
-- 2026-07-11 菜单子页面分组(subpage_group)升级
-- 新增 subpage_group 类型用于组织 menu 下大量平铺的 subpage, 不在侧边栏显示
-- 详见方案: catalog > menu > subpage_group(隐藏) > subpage, 面包屑显示完整四级
-- ----------------------------

-- 更新 menu_type 字段注释
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_type" IS '菜单类型: catalog-目录, menu-菜单, subpage-子页面, subpage_group-子页面分组, embedded-内嵌, link-外链';

-- 新建 4 个子页面分组节点(纯容器: 无 path/component/menu_code, 强制隐藏)
-- 40401 商户列表下 3 个分组
INSERT INTO "public"."iam_perm_menu"
    (id, pid, menu_code, client_code, name, title_cn, title_en, i18n_key, icon,
     hidden, hide_children_menu, component, path, redirect, sort_no, root,
     keep_alive, affix_tab, creator, last_modifier, version, deleted, menu_type,
     active_icon, badge, badge_type, badge_variants, iframe_src, link,
     create_time, last_modified_time)
VALUES
    (4040130, 40401, NULL, 'admin', 'MchManageGroup', '商户管理', 'Merchant Management',
     'menu.payment.merchant.group.manage', 'lucide:settings-2',
     true, false, NULL, NULL, NULL, 1, false,
     false, false, 1, 1, 0, false, 'subpage_group',
     NULL, NULL, NULL, NULL, NULL, NULL,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4040131, 40401, NULL, 'admin', 'ChannelMerchantGroup', '通道商户', 'Channel Merchant',
     'menu.payment.merchant.group.channelMerchant', 'lucide:repeat',
     true, false, NULL, NULL, NULL, 2, false,
     false, false, 1, 1, 0, false, 'subpage_group',
     NULL, NULL, NULL, NULL, NULL, NULL,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4040132, 40401, NULL, 'admin', 'ChannelAppGroup', '渠道应用', 'Channel App',
     'menu.payment.merchant.group.channelApp', 'lucide:layout-grid',
     true, false, NULL, NULL, NULL, 3, false,
     false, false, 1, 1, 0, false, 'subpage_group',
     NULL, NULL, NULL, NULL, NULL, NULL,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- 40105 支付产品管理下 1 个分组
    (40508, 40105, NULL, 'admin', 'ChannelIsvConfigGroup', '渠道服务商配置', 'Channel ISV Config',
     'menu.payment.config.group.channelIsv', 'lucide:server',
     true, false, NULL, NULL, NULL, 1, false,
     false, false, 1, 1, 0, false, 'subpage_group',
     NULL, NULL, NULL, NULL, NULL, NULL,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- 将子页面 pid 迁移到对应分组
-- 4040130 商户管理组(9个)
UPDATE "public"."iam_perm_menu"
SET "pid" = 4040130, "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" IN (4040101, 4040103, 4040102, 4040110, 4040111, 4040108, 4040117, 4040121, 4040120);

-- 4040131 通道商户组(3个)
UPDATE "public"."iam_perm_menu"
SET "pid" = 4040131, "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" IN (4040106, 4040109, 4040112);

-- 4040132 渠道应用组(4个)
UPDATE "public"."iam_perm_menu"
SET "pid" = 4040132, "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" IN (4040113, 4040114, 4040115, 4040119);

-- 40508 渠道服务商配置组(3个)
UPDATE "public"."iam_perm_menu"
SET "pid" = 40508, "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" IN (40502, 40503, 40506);

-- 注: 40501(产品配置详情)、40505(移动端应用详情) 仅1个子页面, 保留直挂 menu 不强制分组

-- ----------------------------
-- 收银台配置菜单(应用 subpage, 从应用管理/应用工作台进入)
-- menu_code = merchant:gateway-cashier
-- 权限码: merchant:gateway-cashier:view / merchant:gateway-cashier:update
-- ----------------------------
INSERT INTO public.iam_perm_menu (
    id, pid, menu_code, client_code, name,
    title_cn, title_en, i18n_key, icon,
    hidden, hide_children_menu, component, path, redirect, sort_no,
    root, keep_alive, affix_tab, creator, last_modifier, version, deleted,
    menu_type, active_icon, badge, badge_type, badge_variants, iframe_src, link,
    create_time, last_modified_time
)
SELECT
    4040122,
    4040130,
    'merchant:gateway-cashier',
    'admin',
    'CashierConfig',
    '收银台配置',
    'Cashier Config',
    'menu.payment.merchant.cashierConfig',
    NULL,
    true,
    false,
    '/payment/merchant/cashier/CashierConfig',
    '/payment/merchant/cashier',
    NULL,
    4,
    false,
    true,
    false,
    1,
    1,
    1,
    false,
    'subpage',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    '2026-07-11 03:00:13.374496+00',
    '2026-07-11 03:37:15.424776+00'
WHERE NOT EXISTS (
    SELECT 1 FROM public.iam_perm_menu WHERE id = 4040122 OR menu_code = 'merchant:gateway-cashier'
);

-- ----------------------------
-- 应用工作台菜单(应用 subpage, 从应用列表进入)
-- menu_code = merchant:app (与应用管理同权)
-- ----------------------------
INSERT INTO public.iam_perm_menu (
    id, pid, menu_code, client_code, name,
    title_cn, title_en, i18n_key, icon,
    hidden, hide_children_menu, component, path, redirect, sort_no,
    root, keep_alive, affix_tab, creator, last_modifier, version, deleted,
    menu_type, active_icon, badge, badge_type, badge_variants, iframe_src, link,
    create_time, last_modified_time
)
SELECT
    4040123,
    4040130,
    'merchant:app',
    'admin',
    'MchAppWorkbench',
    '应用配置',
    'App Configuration',
    'menu.payment.merchant.appWorkbench',
    NULL,
    true,
    false,
    '/payment/merchant/app/MchAppWorkbench',
    '/payment/merchant/app/manage',
    NULL,
    11,
    false,
    true,
    false,
    1,
    1,
    1,
    false,
    'subpage',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    '2026-07-11 08:00:00+00',
    '2026-07-11 08:00:00+00'
WHERE NOT EXISTS (
    SELECT 1 FROM public.iam_perm_menu WHERE id = 4040123 OR name = 'MchAppWorkbench'
);
