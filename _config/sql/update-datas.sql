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

-- 2026-10-15 通道产品拆分 A 批: 新增 5 个占位产品 (富友互联网直连/富友H5聚合收银台/杉德支付/银盛e企通/汇付数字人民币, 无策略实现仅目录层) 与支付能力登记
INSERT INTO public.pay_md_product VALUES (10036, 'fuyou_internet', '富友(互联网直连)', 'fuyou_pay', 142, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10037, 'fuyou_h5', '富友(H5聚合收银台)', 'fuyou_pay', 143, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10038, 'sand_pay', '杉德支付', 'sand_pay', 144, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10039, 'yseqt_pay', '银盛e企通', 'ysep_pay', 145, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10040, 'huifu_ecny', '汇付数字人民币', 'huifu', 146, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product_capability VALUES (21236, 'fuyou_internet', 'wechat_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21237, 'fuyou_internet', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21238, 'fuyou_internet', 'wechat_mini', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21239, 'fuyou_internet', 'wechat_barcode', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21240, 'fuyou_internet', 'alipay_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21241, 'fuyou_internet', 'alipay_jsapi', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21242, 'fuyou_internet', 'alipay_barcode', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21243, 'fuyou_internet', 'union_qr', 7, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21244, 'fuyou_h5', 'wechat_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21245, 'fuyou_h5', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21246, 'fuyou_h5', 'wechat_mini', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21247, 'fuyou_h5', 'wechat_barcode', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21248, 'fuyou_h5', 'alipay_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21249, 'fuyou_h5', 'alipay_jsapi', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21250, 'fuyou_h5', 'alipay_barcode', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21251, 'fuyou_h5', 'union_qr', 7, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21252, 'sand_pay', 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21253, 'sand_pay', 'wechat_qr', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21254, 'sand_pay', 'wechat_jsapi', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21255, 'sand_pay', 'wechat_mini', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21256, 'sand_pay', 'alipay_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21257, 'sand_pay', 'alipay_jsapi', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21258, 'yseqt_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21259, 'yseqt_pay', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21260, 'yseqt_pay', 'wechat_app', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21261, 'yseqt_pay', 'wechat_h5', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21262, 'yseqt_pay', 'wechat_mini', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21263, 'yseqt_pay', 'wechat_barcode', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21264, 'yseqt_pay', 'alipay_qr', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21265, 'yseqt_pay', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21266, 'yseqt_pay', 'alipay_app', 8, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21267, 'yseqt_pay', 'alipay_h5', 9, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21268, 'yseqt_pay', 'alipay_pc', 10, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21269, 'yseqt_pay', 'alipay_barcode', 11, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21270, 'yseqt_pay', 'union_qr', 12, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21271, 'yseqt_pay', 'union_jsapi', 13, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21272, 'yseqt_pay', 'union_h5', 14, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21273, 'yseqt_pay', 'union_barcode', 15, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21274, 'huifu_ecny', 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
