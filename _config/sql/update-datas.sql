-- 增量数据脚本（PostgreSQL）
-- 交易记录：提升为一级菜单（pid=NULL，紧挨交易管理之后）
INSERT INTO "public"."iam_perm_menu" VALUES (607, NULL, 'trade:record', 'admin', 'TradeRecordCatalog', 'menu.trade.record', 'lucide:scroll-text', 'f', 'f', NULL, '/trade/record', '/trade/record/mch-notice', 4.5, 'f', 't', 'f', 1, 1, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-21 06:30:00+00', '2026-07-21 06:45:00+00')
ON CONFLICT ("id") DO UPDATE SET
  "pid" = EXCLUDED."pid",
  "menu_code" = EXCLUDED."menu_code",
  "name" = EXCLUDED."name",
  "i18n_key" = EXCLUDED."i18n_key",
  "icon" = EXCLUDED."icon",
  "path" = EXCLUDED."path",
  "redirect" = EXCLUDED."redirect",
  "sort_no" = EXCLUDED."sort_no",
  "menu_type" = EXCLUDED."menu_type",
  "deleted" = false,
  "last_modified_time" = EXCLUDED."last_modified_time";

-- 商户出站通知菜单（挂到交易记录目录）
INSERT INTO "public"."iam_perm_menu" VALUES (605, 607, 'trade:mch-notice', 'admin', 'MchNoticeTaskList', 'menu.trade.mchNotice', 'lucide:bell-ring', 'f', 'f', '/payment/notice/MchNoticeTaskList', '/trade/record/mch-notice', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-21 03:00:00+00', '2026-07-21 06:30:00+00')
ON CONFLICT ("id") DO UPDATE SET
  "pid" = EXCLUDED."pid",
  "path" = EXCLUDED."path",
  "sort_no" = EXCLUDED."sort_no",
  "deleted" = false,
  "last_modified_time" = EXCLUDED."last_modified_time";

-- 通道入站回调记录菜单（挂到交易记录目录）
INSERT INTO "public"."iam_perm_menu" VALUES (606, 607, 'trade:callback-record', 'admin', 'PayCallbackRecordList', 'menu.trade.callbackRecord', 'lucide:inbox', 'f', 'f', '/payment/record/PayCallbackRecordList', '/trade/record/callback-record', NULL, 2, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-21 06:00:00+00', '2026-07-21 06:30:00+00')
ON CONFLICT ("id") DO UPDATE SET
  "pid" = EXCLUDED."pid",
  "path" = EXCLUDED."path",
  "sort_no" = EXCLUDED."sort_no",
  "deleted" = false,
  "last_modified_time" = EXCLUDED."last_modified_time";

-- 码牌管理迁入支付管理
UPDATE "public"."iam_perm_menu"
SET "pid" = 4,
    "sort_no" = 3,
    "last_modified_time" = '2026-07-21 06:30:00+00'
WHERE "id" = 901;

-- 软删空壳设备管理目录，并清理角色菜单关联
UPDATE "public"."iam_perm_menu"
SET "deleted" = true,
    "last_modified_time" = '2026-07-21 06:30:00+00'
WHERE "id" = 9;

DELETE FROM "public"."iam_role_menu" WHERE "menu_id" = 9;

-- ============================================================
-- Admin 前端路径结构治理：同步 iam_perm_menu.component / path
-- （views 迁至 device/terminal/system、payment/route、payment/global/*、
--  system/config/mobile-app；已装库执行本段即可对齐菜单）
-- ============================================================

-- 系统终端
UPDATE "public"."iam_perm_menu"
SET "component" = '/payment/device/terminal/system/SystemTerminalList',
    "path" = '/payment/device/terminal/system',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 905;

-- 支付路由
UPDATE "public"."iam_perm_menu"
SET "component" = '/payment/route/PayRouteConfig',
    "path" = '/payment/route',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040111;

-- 通道商户列表（商户入口）
UPDATE "public"."iam_perm_menu"
SET "component" = '/payment/global/channel-merchant/ChannelMerchantList',
    "path" = '/payment/global/channel-merchant',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040106;

-- 通道商户创建
UPDATE "public"."iam_perm_menu"
SET "component" = '/payment/global/channel-merchant/ChannelMerchantCreate',
    "path" = '/payment/global/channel-merchant/create',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040109;

-- 通道商户详情分发
UPDATE "public"."iam_perm_menu"
SET "component" = '/payment/global/channel-merchant/detail/ChannelMerchantDetailDispatch',
    "path" = '/payment/global/channel-merchant/detail',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040112;

-- 通道商户全局列表
UPDATE "public"."iam_perm_menu"
SET "component" = '/payment/global/channel-merchant-global/ChannelMerchantGlobalList',
    "path" = '/payment/global/channel-merchants',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 40402;

-- 通道应用子页：仅统一浏览器 path 前缀（component 仍指向 channel/{provider}）
UPDATE "public"."iam_perm_menu"
SET "path" = '/payment/global/channel-merchant/alipay-app-manage',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040114;

UPDATE "public"."iam_perm_menu"
SET "path" = '/payment/global/channel-merchant/wechat-app-manage',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040113;

UPDATE "public"."iam_perm_menu"
SET "path" = '/payment/global/channel-merchant/douyin-app-manage',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040115;

UPDATE "public"."iam_perm_menu"
SET "path" = '/payment/global/channel-merchant/wechat-isv-mch-app-manage',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 4040119;

-- 移动端应用配置（目录 camelCase → kebab-case）
UPDATE "public"."iam_perm_menu"
SET "component" = '/system/config/mobile-app/MobileAppConfig',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 40504;

UPDATE "public"."iam_perm_menu"
SET "component" = '/system/config/mobile-app/detail/MobileAppDetail',
    "last_modified_time" = CURRENT_TIMESTAMP
WHERE "id" = 40505;

-- ========== 商户端仪表板菜单（client_code=merchant） ==========
INSERT INTO "public"."iam_perm_menu" VALUES (91001, NULL, NULL, 'merchant', 'Dashboard', 'menu.dashboard', 'lucide:layout-dashboard', 'f', 'f', NULL, '/dashboard', '/workspace', -1, 'f', 'f', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."iam_perm_menu" VALUES (91002, 91001, 'dashboard:workspace', 'merchant', 'Workspace', 'menu.dashboard.workspace', 'lucide:panels-top-left', 'f', 'f', '/dashboard/workspace/index', '/workspace', NULL, 1, 'f', 'f', 't', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;
INSERT INTO "public"."iam_perm_menu" VALUES (91003, 91001, 'dashboard:analytics', 'merchant', 'Analytics', 'menu.dashboard.analytics', 'lucide:area-chart', 'f', 'f', '/dashboard/analytics/index', '/analytics', NULL, 2, 'f', 'f', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- 挂到所有 client_code=merchant 的角色（若尚无商户角色则无行；新建商户管理员角色时需勾选仪表板）
INSERT INTO "public"."iam_role_menu" ("id", "role_id", "menu_id")
SELECT ((EXTRACT(EPOCH FROM clock_timestamp()) * 1000)::bigint * 1000 + (row_number() OVER ())::bigint),
       r."id",
       m.menu_id
FROM "public"."iam_role" r
CROSS JOIN (VALUES (91001::bigint), (91002::bigint), (91003::bigint)) AS m(menu_id)
WHERE r."client_code" = 'merchant'
  AND NOT EXISTS (
    SELECT 1 FROM "public"."iam_role_menu" rm
    WHERE rm."role_id" = r."id" AND rm."menu_id" = m.menu_id
  );
