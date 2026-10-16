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

-- 2026-10-15 通道产品拆分 B 批: 新增 9 个占位通道 (连连支付/汇聚支付/威富通/嘉联支付/合利宝/付呗/易票联/PayPal/Airwallex, 零策略实现零创建入口, 仅目录层登记) + 9 个占位产品与支付能力登记 (paypal/airwallex 为国际通道, 不种能力行)
INSERT INTO public.pay_md_channel VALUES (26, 'llianpay', 24, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (27, 'joinpay', 25, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (28, 'swiftpass', 26, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (29, 'jlpay', 27, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (30, 'helipay', 28, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (31, 'fubei', 29, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (32, 'epaylinks', 30, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (33, 'paypal', 31, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (34, 'airwallex', 32, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_product VALUES (10041, 'llian_pay', '连连聚合支付', 'llianpay', 147, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10042, 'join_pay', '汇聚聚合支付', 'joinpay', 148, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10043, 'swift_pass', '威富通支付', 'swiftpass', 149, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10044, 'jl_pay', '嘉联聚合支付', 'jlpay', 150, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10045, 'heli_pay', '合利宝支付', 'helipay', 151, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10046, 'fubei_pay', '付呗聚合支付', 'fubei', 152, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10047, 'epay_links', '易票联支付', 'epaylinks', 153, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10048, 'paypal', 'PayPal支付', 'paypal', 154, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10049, 'airwallex', 'Airwallex支付', 'airwallex', 155, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product_capability VALUES (21275, 'llian_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21276, 'llian_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21277, 'llian_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21278, 'llian_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21279, 'llian_pay', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21280, 'llian_pay', 'union_jsapi', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21281, 'llian_pay', 'union_h5', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21282, 'join_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21283, 'join_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21284, 'join_pay', 'alipay_pc', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21285, 'join_pay', 'wechat_qr', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21286, 'join_pay', 'wechat_jsapi', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21287, 'join_pay', 'wechat_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21288, 'join_pay', 'wechat_app', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21289, 'swift_pass', 'wechat_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21290, 'swift_pass', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21291, 'swift_pass', 'alipay_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21292, 'jl_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21293, 'jl_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21294, 'jl_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21295, 'jl_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21296, 'jl_pay', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21297, 'jl_pay', 'union_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21298, 'heli_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21299, 'heli_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21300, 'heli_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21301, 'heli_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21302, 'heli_pay', 'wechat_h5', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21303, 'fubei_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21304, 'fubei_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21305, 'fubei_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21306, 'epay_links', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21307, 'epay_links', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21308, 'epay_links', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21309, 'epay_links', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21310, 'epay_links', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21311, 'epay_links', 'union_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');

-- 2026-10-15 合规通道占位 C 批: 新增 10 个占位通道 (中金支付/宝付支付/首信易支付/哆啦宝/火脸支付/有赞支付/银盈通/慧收钱/收银呗/Payssion, 零策略实现零创建入口, 仅目录层登记) + 10 个占位产品与支付能力登记
INSERT INTO public.pay_md_channel VALUES (35, 'cpcn', 33, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (36, 'baofu', 34, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (38, 'yizhifubj', 36, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (40, 'duolabao', 38, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (44, 'huolian', 42, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (45, 'youzan', 43, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (54, 'yinyingtong', 52, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (57, 'huishouqian', 55, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (58, 'shouyinbei', 56, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (61, 'payssion', 59, NULL, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false);
INSERT INTO public.pay_md_product VALUES (10050, 'cpcn_pay', '中金聚合支付', 'cpcn', 156, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10051, 'baofu_pay', '宝付支付', 'baofu', 157, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10054, 'yizhifubj_pay', '首信易支付', 'yizhifubj', 160, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10057, 'duolabao_pay', '哆啦宝支付', 'duolabao', 163, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10061, 'huolian_pay', '火脸支付', 'huolian', 167, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10062, 'youzan_pay', '有赞支付', 'youzan', 168, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10071, 'yinyingtong_pay', '银盈通支付', 'yinyingtong', 177, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10074, 'huishouqian_pay', '慧收钱', 'huishouqian', 180, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10075, 'shouyinbei_pay', '收银呗支付', 'shouyinbei', 181, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product VALUES (10078, 'payssion', 'Payssion', 'payssion', 184, 1, '2026-10-15 00:00:00+00', 1, '2026-10-15 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product_capability VALUES (21312, 'cpcn_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21313, 'cpcn_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21314, 'cpcn_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21315, 'cpcn_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21316, 'cpcn_pay', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21317, 'cpcn_pay', 'union_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21318, 'cpcn_pay', 'wechat_h5', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21319, 'baofu_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21320, 'baofu_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21321, 'baofu_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21322, 'baofu_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21323, 'baofu_pay', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21324, 'baofu_pay', 'union_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21333, 'yizhifubj_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21334, 'yizhifubj_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21335, 'yizhifubj_pay', 'alipay_pc', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21336, 'yizhifubj_pay', 'wechat_qr', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21337, 'yizhifubj_pay', 'wechat_jsapi', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21338, 'yizhifubj_pay', 'wechat_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21339, 'yizhifubj_pay', 'union_qr', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21340, 'yizhifubj_pay', 'union_h5', 7, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21349, 'duolabao_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21350, 'duolabao_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21351, 'duolabao_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21352, 'duolabao_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21353, 'duolabao_pay', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21354, 'duolabao_pay', 'union_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21376, 'huolian_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21377, 'huolian_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21378, 'huolian_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21379, 'huolian_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21380, 'huolian_pay', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21381, 'huolian_pay', 'union_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21382, 'youzan_pay', 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21420, 'yinyingtong_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21421, 'yinyingtong_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21422, 'yinyingtong_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21423, 'yinyingtong_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21424, 'yinyingtong_pay', 'wechat_h5', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21425, 'yinyingtong_pay', 'union_qr', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21426, 'yinyingtong_pay', 'union_h5', 6, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21429, 'huishouqian_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21430, 'huishouqian_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21431, 'huishouqian_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21432, 'huishouqian_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21433, 'huishouqian_pay', 'union_qr', 4, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21434, 'huishouqian_pay', 'union_h5', 5, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21435, 'shouyinbei_pay', 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21449, 'payssion', 'aggregate_pay_qrcode', 0, true, NULL, false, 1, '2026-10-15 00:00:00+00', 0, 1, '2026-10-15 00:00:00+00');

-- 2026-10-16 通道产品补充 D 批: 现有通道新增 3 个占位产品 (拉卡拉MOSS/富友合作方/海科聚合支付, 无策略实现仅目录层) 与支付能力登记
INSERT INTO public.pay_md_product VALUES (10082, 'lakala_moss', '拉卡拉MOSS', 'lakala_pay', 185, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false, false, false);
INSERT INTO public.pay_md_product VALUES (10083, 'fuyou_partner', '富友(合作方)', 'fuyou_pay', 186, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false, false, false);
INSERT INTO public.pay_md_product VALUES (10084, 'hkrt_saas', '海科聚合支付', 'hkrt_pay', 187, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false, false, false);
INSERT INTO public.pay_md_product_capability VALUES (21459, 'lakala_moss', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21460, 'lakala_moss', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21461, 'lakala_moss', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21462, 'lakala_moss', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21463, 'fuyou_partner', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21464, 'fuyou_partner', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21465, 'fuyou_partner', 'alipay_barcode', 2, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21466, 'fuyou_partner', 'wechat_qr', 3, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21467, 'fuyou_partner', 'wechat_jsapi', 4, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21468, 'fuyou_partner', 'wechat_barcode', 5, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21469, 'hkrt_saas', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21470, 'hkrt_saas', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21471, 'hkrt_saas', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21472, 'hkrt_saas', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21473, 'hkrt_saas', 'union_qr', 4, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21474, 'hkrt_saas', 'union_h5', 5, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');

-- 2026-10-16 通道产品补充 E 批: 新增 3 个持牌占位通道 (新生支付/联动优势/易生支付) 与 4 个产品及支付能力登记
INSERT INTO public.pay_md_channel VALUES (65, 'hnapay', 60, NULL, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (66, 'umfpay', 61, NULL, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false);
INSERT INTO public.pay_md_channel VALUES (67, 'yisheng_pay', 62, NULL, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false);
INSERT INTO public.pay_md_product VALUES (10085, 'hnapay_pay', '新生支付', 'hnapay', 188, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false, false, false);
INSERT INTO public.pay_md_product VALUES (10086, 'xsy', '新生易', 'hnapay', 189, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false, false, false);
INSERT INTO public.pay_md_product VALUES (10087, 'umf_pay', '联动优势', 'umfpay', 190, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false, false, false);
INSERT INTO public.pay_md_product VALUES (10088, 'yisheng_pay', '易生支付', 'yisheng_pay', 191, 1, '2026-10-16 00:00:00+00', 1, '2026-10-16 00:00:00+00', 0, false, false, false);
INSERT INTO public.pay_md_product_capability VALUES (21475, 'hnapay_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21476, 'hnapay_pay', 'alipay_h5', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21477, 'hnapay_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21478, 'hnapay_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21479, 'xsy', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21480, 'xsy', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21481, 'xsy', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21482, 'xsy', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21483, 'umf_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21484, 'umf_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21485, 'umf_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21486, 'umf_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21487, 'yisheng_pay', 'alipay_qr', 0, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21488, 'yisheng_pay', 'alipay_jsapi', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21489, 'yisheng_pay', 'wechat_qr', 2, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21490, 'yisheng_pay', 'wechat_jsapi', 3, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');

-- 2026-10-16 「规划中」产品对齐: 41 个无策略实现/预留产品(合规占位 + 发版前拆分占位 + 规划中通道产品 + 银盛/快钱/Jeepay 等) enabled 置为停用,
-- 配合产品配置卡片页 online 派生字段渲染为「规划中」灰卡; 产品下拉与运行时路由不再出现未实连产品
UPDATE public.pay_md_product SET enabled = false WHERE code IN (
  'fuyou_internet', 'fuyou_h5', 'sand_pay', 'yseqt_pay', 'huifu_ecny',
  'llian_pay', 'join_pay', 'swift_pass', 'jl_pay', 'heli_pay', 'fubei_pay', 'epay_links',
  'paypal', 'airwallex', 'cpcn_pay', 'baofu_pay', 'yizhifubj_pay', 'duolabao_pay',
  'huolian_pay', 'youzan_pay', 'yinyingtong_pay', 'huishouqian_pay', 'shouyinbei_pay', 'payssion',
  'ysep_pay', 'quick_pay', 'jee_pay',
  'xingyi_pay', 'long_pay', 'allin_cloud', 'allin_cashier', 'allin_payfund', 'allin_direct', 'shouqianba',
  'lakala_moss', 'fuyou_partner', 'hkrt_saas', 'hnapay_pay', 'xsy', 'umf_pay', 'yisheng_pay'
);

-- 2026-10-16 数字人民币能力码贯通: 新增支付渠道 ecny / 支付方式与能力 ecny_qr, 并挂到汇付数字人民币产品(huifu_ecny 为追加, 保留原聚合扫码能力)
INSERT INTO public.pay_md_provider VALUES (502001007, 'ecny', NULL, 61, false, NULL, NULL, 0, NULL, '2026-10-16 00:00:00+00', true, NULL);
INSERT INTO public.pay_md_method VALUES (502003031, 'ecny_qr', 1, NULL, false, NULL, NULL, 0, NULL, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_capability VALUES (7005, 'ecny_qr', 1, true, NULL, false, NULL, NULL, 0, NULL, '2026-10-16 00:00:00+00');
INSERT INTO public.pay_md_provider_method VALUES (502002026, 'ecny', 'ecny_qr', 1, false, NULL, NULL, 0, NULL, '2026-10-16 00:00:00+00', NULL);
INSERT INTO public.pay_md_product_capability VALUES (21491, 'huifu_ecny', 'ecny_qr', 1, true, NULL, false, 1, '2026-10-16 00:00:00+00', 0, 1, '2026-10-16 00:00:00+00');

-- 2026-10-16 移除占位主数据: 「海科聚合支付」(产品 hkrt_saas, 与海科融通 hkrt_pay 重复)、
-- 「收银呗支付」(产品 shouyinbei_pay 及其占位通道 shouyinbei)、「威富通」(产品 swift_pass 及其占位通道 swiftpass)、
-- 「火脸支付」(产品 huolian_pay 及其占位通道 huolian)、「慧收钱」(产品 huishouqian_pay 及其占位通道 huishouqian)、
-- 「汇聚支付」(产品 join_pay 及其占位通道 joinpay)、「银盈通」(产品 yinyingtong_pay 及其占位通道 yinyingtong)、
-- 「新生支付」(产品 hnapay_pay 与新生易 xsy 及其占位通道 hnapay), 均按用户拍板整体下线
DELETE FROM public.pay_md_product_capability WHERE product_code IN ('shouyinbei_pay', 'hkrt_saas', 'swift_pass', 'huolian_pay', 'huishouqian_pay', 'join_pay', 'yinyingtong_pay', 'hnapay_pay', 'xsy');
DELETE FROM public.pay_md_product WHERE code IN ('shouyinbei_pay', 'hkrt_saas', 'swift_pass', 'huolian_pay', 'huishouqian_pay', 'join_pay', 'yinyingtong_pay', 'hnapay_pay', 'xsy');
DELETE FROM public.pay_md_channel WHERE code IN ('shouyinbei', 'swiftpass', 'huolian', 'huishouqian', 'joinpay', 'yinyingtong', 'hnapay');
