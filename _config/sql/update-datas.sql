-- ============================================================
-- 升级数据脚本 (上一个版本 -> 当前版本)
-- 执行顺序: update-tables.sql -> update-datas.sql
-- 注意: 不可跨版本升级, 仅限相邻版本间执行
-- ============================================================

-- ----------------------------
-- 商户端 开发调试(develop) 菜单
-- 顶级目录 + 4 子菜单(交易/网关/签名/认证调试)
-- 对应前端 apps/daxpay-merchant, 后端 /mch/develop/* 接口
-- 排序: 顶级目录 sort=5, 位于「回调通知/交易记录」(sort=4.5)之后
-- ----------------------------
INSERT INTO "public"."iam_perm_menu" VALUES (91600, NULL, NULL, 'merchant', 'Develop', 'menu.develop', 'lucide:wrench', 'f', 'f', NULL, '/develop', '/develop/trade', 5, 'f', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (91601, 91600, 'develop:trade', 'merchant', 'DevelopTrade', 'menu.develop.trade', 'lucide:credit-card', 'f', 'f', '/payment/develop/trade/DevelopTrade', '/develop/trade', NULL, 1, 'f', 'f', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (91602, 91600, 'develop:gateway', 'merchant', 'DevelopGateway', 'menu.develop.gateway', 'lucide:globe', 'f', 'f', '/payment/develop/gateway/DevelopGateway', '/develop/gateway', NULL, 2, 'f', 'f', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (91603, 91600, 'develop:sign', 'merchant', 'DevelopSign', 'menu.develop.sign', 'lucide:pen-tool', 'f', 'f', '/payment/develop/sign/DevelopSign', '/develop/sign', NULL, 3, 'f', 'f', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (91604, 91600, 'develop:auth', 'merchant', 'ChannelAuth', 'menu.develop.auth', 'lucide:shield-check', 'f', 'f', '/payment/develop/auth/ChannelAuth', '/develop/auth', NULL, 4, 'f', 'f', 'f', 0, NULL, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-25 15:10:00+00', '2026-07-25 15:10:00+00');
