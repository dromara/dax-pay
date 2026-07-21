-- 增量数据脚本（PostgreSQL）
-- 商户出站通知菜单
INSERT INTO "public"."iam_perm_menu" VALUES (605, 6, 'trade:mch-notice', 'admin', 'MchNoticeTaskList', 'menu.trade.mchNotice', 'lucide:bell-ring', 'f', 'f', '/payment/notice/MchNoticeTaskList', '/trade/mch-notice', NULL, 4, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-21 03:00:00+00', '2026-07-21 03:00:00+00')
ON CONFLICT ("id") DO NOTHING;
