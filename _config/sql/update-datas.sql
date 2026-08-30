-- 2026-08-29 商户端菜单: 异常订单 + 资金流水 (随运营端同功能批次补商户端镜像)
INSERT INTO "public"."iam_perm_menu" VALUES (91128, 91100, 'trade:abnormal-order', 'merchant', 'AbnormalOrderList', 'menu.trade.abnormalOrder', 'lucide:triangle-alert', 'f', 'f', '/payment/order/AbnormalOrderList', '/trade/abnormal-order', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-29 16:00:00+00', '2026-08-29 16:00:00+00');
INSERT INTO "public"."iam_perm_menu" VALUES (91203, 91200, 'trade:fund-flow', 'merchant', 'FundFlowList', 'menu.trade.fundFlow', 'lucide:coins', 'f', 'f', '/payment/record/FundFlowList', '/trade/record/fund-flow', NULL, 3, 'f', 'f', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-08-29 16:00:00+00', '2026-08-29 16:00:00+00');

-- 商户管理员角色(role 2)菜单授权(引用上方同脚本菜单ID, 自洽)
INSERT INTO "public"."iam_role_menu" VALUES (1000000091128, 2, NULL, 91128);
INSERT INTO "public"."iam_role_menu" VALUES (1000000091203, 2, NULL, 91203);

-- 商户管理员角色(role 2)权限码授权: 按code字符串解析code_id
-- (iam_perm_code 由 @PermCode 启动扫描同步生成, 升级库中ID为运行时雪花, 不可硬编码引用)
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2079866296000000311, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:abnormal-order:view'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2079866296000000312, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:fund-flow:view'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
