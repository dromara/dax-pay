-- ----------------------------
-- 补分账菜单与缓存演示菜单种子(2026-08-23)
-- 背景: 6d5395758(分账)与 8bdeb6e9d(缓存演示)提交只更新了 iam_perm_menu.sql(229 库单表文件),
--       未同步 data.sql 种子, 已装环境与全量种子均缺以下菜单; 幂等写入需先确认不存在
-- ----------------------------
INSERT INTO public.iam_perm_menu VALUES (208, 2, 'demos:cache', 'admin', 'CacheDemo', 'menu.demos.cache', 'lucide:database-backup', false, false, '/demos/cache/CacheDemo', '/demos/cache', NULL, 7, false, true, false, 0, NULL, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-15 00:00:00+08', '2026-08-15 00:00:00+08');
INSERT INTO public.iam_perm_menu VALUES (608, 6, 'trade:alloc', 'admin', 'AllocOrderList', 'menu.trade.allocOrder', 'lucide:split', false, false, '/payment/order/AllocOrderList', '/trade/alloc-order', NULL, 2.5, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.iam_perm_menu VALUES (91127, 91100, 'trade:alloc', 'merchant', 'AllocOrderList', 'menu.trade.allocOrder', 'lucide:split', false, false, '/payment/order/AllocOrderList', '/trade/alloc-order', NULL, 2.5, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.iam_role_menu VALUES (1000000000208, 1, NULL, 208);
INSERT INTO public.iam_role_menu VALUES (1000000000608, 1, NULL, 608);
INSERT INTO public.iam_role_menu VALUES (920000091127, 2, NULL, 91127);

-- ----------------------------
-- 分账按钮权限码(229 库 8/19 扫描同步生成, data.sql 2026-08-23 重导时纳入)
-- ----------------------------
INSERT INTO public.iam_perm_code VALUES (2089898876290093056, 'trade:alloc:manage', 'trade:alloc', true, NULL, 1, 1, 0, false, '2026-08-19 02:14:55.208677+00', '2026-08-19 02:14:55.208677+00', 'perm.trade:alloc:manage');
INSERT INTO public.iam_perm_code VALUES (2089898876302675968, 'trade:alloc:view', 'trade:alloc', true, NULL, 1, 1, 0, false, '2026-08-19 02:14:55.211906+00', '2026-08-19 02:14:55.211906+00', 'perm.trade:alloc:view');
