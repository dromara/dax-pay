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
