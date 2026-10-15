-- =============================================================
-- 增量数据脚本(上一版本 -> 当前版本)
-- 清空重置基线。
-- 发版时本文件无需改动, 保持空白即可; 下一版本周期内的种子数据变化再追加于此,
-- =============================================================

-- 2026-10-13 移除「行政区划选择器」演示菜单 (demo 页面下线, RegionCascader 组件与 /china/region 接口为业务依赖保留)
DELETE FROM "public"."iam_role_menu" WHERE menu_id = 203;
DELETE FROM "public"."iam_perm_menu" WHERE id = 203;

-- 2026-10-15 新增富友商户前置(fuyou_direct)产品与支付能力登记 (直连商户模式 aggregatePay, 一期对齐现有扫码/JSAPI/小程序/付款码 8 种支付方式)
INSERT INTO public.pay_md_product VALUES (10035, 'fuyou_direct', '富友商户前置', 'fuyou_pay', 141, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product_capability VALUES (21228, 'fuyou_direct', 'wechat_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21229, 'fuyou_direct', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21230, 'fuyou_direct', 'wechat_mini', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21231, 'fuyou_direct', 'wechat_barcode', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21232, 'fuyou_direct', 'alipay_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21233, 'fuyou_direct', 'alipay_jsapi', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21234, 'fuyou_direct', 'alipay_barcode', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21235, 'fuyou_direct', 'union_qr', 7, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
