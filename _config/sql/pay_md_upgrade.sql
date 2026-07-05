-- ============================================================
-- 支付主数据升级：补全第三方聚合通道 + 统一重排 sort_no
-- 依据：开源版 ChannelEnum 19 项、商业版各通道 RATE_PRODUCT_MAP / 策略类
-- 说明：union_pay(云闪付直连通道) 不实例化主数据，保留 provider/能力层
-- 幂等：ON CONFLICT (id) DO NOTHING + UPDATE 可重复执行
-- ============================================================

SET TIME ZONE 'Asia/Shanghai';

-- ------------------------------------------------------------
-- 1. 新增支付通道 pay_md_channel（10 条，不含 union_pay 直连）
-- ------------------------------------------------------------
INSERT INTO "public"."pay_md_channel"
  (id, code, sort_no, description, icon, creator, create_time, last_modifier, last_modified_time, version, deleted)
VALUES
  (12, 'dougong_pay', 8, NULL, NULL, 1, now(), 1, now(), 0, false),
  (13, 'hkrt_pay', 9, NULL, NULL, 1, now(), 1, now(), 0, false),
  (19, 'yee_pay', 10, NULL, NULL, 1, now(), 1, now(), 0, false),
  (11, 'ada_pay', 11, NULL, NULL, 1, now(), 1, now(), 0, false),
  (18, 'sand_pay', 12, NULL, NULL, 1, now(), 1, now(), 0, false),
  (14, 'fuyou_pay', 13, NULL, NULL, 1, now(), 1, now(), 0, false),
  (15, 'sheng_pay', 14, NULL, NULL, 1, now(), 1, now(), 0, false),
  (16, 'ysep_pay', 15, NULL, NULL, 1, now(), 1, now(), 0, false),
  (17, 'quick_pay', 16, NULL, NULL, 1, now(), 1, now(), 0, false),
  (20, 'jee_pay', 17, NULL, NULL, 1, now(), 1, now(), 0, false)
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------
-- 2. 新增支付产品 pay_md_product（10 条，一通道一产品，settle_periods=[T0,T1]）
-- ------------------------------------------------------------
INSERT INTO "public"."pay_md_product"
  (id, code, name, channel, description, icon, settle_periods, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted, sandbox, enabled)
VALUES
  (10013, 'dougong_pay', 'dougong_pay', 'dougong_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 80, 1, now(), 1, now(), 0, false, false, true),
  (10014, 'hkrt_pay', 'hkrt_pay', 'hkrt_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 90, 1, now(), 1, now(), 0, false, false, true),
  (10020, 'yee_pay', 'yee_pay', 'yee_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 100, 1, now(), 1, now(), 0, false, false, true),
  (10012, 'ada_pay', 'ada_pay', 'ada_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 110, 1, now(), 1, now(), 0, false, false, true),
  (10019, 'sand_pay', 'sand_pay', 'sand_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 120, 1, now(), 1, now(), 0, false, false, true),
  (10015, 'fuyou_pay', 'fuyou_pay', 'fuyou_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 130, 1, now(), 1, now(), 0, false, false, true),
  (10016, 'sheng_pay', 'sheng_pay', 'sheng_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 140, 1, now(), 1, now(), 0, false, false, true),
  (10017, 'ysep_pay', 'ysep_pay', 'ysep_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 150, 1, now(), 1, now(), 0, false, false, true),
  (10018, 'quick_pay', 'quick_pay', 'quick_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 160, 1, now(), 1, now(), 0, false, false, true),
  (10021, 'jee_pay', 'jee_pay', 'jee_pay', NULL, NULL, '["T0", "T1"]'::jsonb, 170, 1, now(), 1, now(), 0, false, false, true)
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------
-- 3. 产品能力映射 pay_md_product_capability（156 条）
--   - 三件套通道(9个)：微信6 + 支付宝6 + 银联4 = 16 项
--   - jeepay：微信5 + 支付宝5 + 银联2 = 12 项（无被扫 barcode）
-- ------------------------------------------------------------
INSERT INTO "public"."pay_md_product_capability"
  (id, product_code, capability_code, sort_no, enabled, remark, deleted, last_modifier, last_modified_time, version, creator, create_time)
VALUES
  (21001, 'dougong_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21002, 'dougong_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21003, 'dougong_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21004, 'dougong_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21005, 'dougong_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21006, 'dougong_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21007, 'dougong_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21008, 'dougong_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21009, 'dougong_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21010, 'dougong_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21011, 'dougong_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21012, 'dougong_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21013, 'dougong_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21014, 'dougong_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21015, 'dougong_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21016, 'dougong_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21017, 'hkrt_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21018, 'hkrt_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21019, 'hkrt_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21020, 'hkrt_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21021, 'hkrt_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21022, 'hkrt_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21023, 'hkrt_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21024, 'hkrt_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21025, 'hkrt_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21026, 'hkrt_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21027, 'hkrt_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21028, 'hkrt_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21029, 'hkrt_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21030, 'hkrt_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21031, 'hkrt_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21032, 'hkrt_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21033, 'yee_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21034, 'yee_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21035, 'yee_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21036, 'yee_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21037, 'yee_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21038, 'yee_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21039, 'yee_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21040, 'yee_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21041, 'yee_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21042, 'yee_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21043, 'yee_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21044, 'yee_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21045, 'yee_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21046, 'yee_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21047, 'yee_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21048, 'yee_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21049, 'ada_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21050, 'ada_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21051, 'ada_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21052, 'ada_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21053, 'ada_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21054, 'ada_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21055, 'ada_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21056, 'ada_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21057, 'ada_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21058, 'ada_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21059, 'ada_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21060, 'ada_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21061, 'ada_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21062, 'ada_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21063, 'ada_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21064, 'ada_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21065, 'sand_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21066, 'sand_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21067, 'sand_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21068, 'sand_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21069, 'sand_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21070, 'sand_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21071, 'sand_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21072, 'sand_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21073, 'sand_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21074, 'sand_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21075, 'sand_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21076, 'sand_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21077, 'sand_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21078, 'sand_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21079, 'sand_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21080, 'sand_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21081, 'fuyou_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21082, 'fuyou_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21083, 'fuyou_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21084, 'fuyou_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21085, 'fuyou_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21086, 'fuyou_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21087, 'fuyou_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21088, 'fuyou_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21089, 'fuyou_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21090, 'fuyou_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21091, 'fuyou_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21092, 'fuyou_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21093, 'fuyou_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21094, 'fuyou_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21095, 'fuyou_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21096, 'fuyou_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21097, 'sheng_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21098, 'sheng_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21099, 'sheng_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21100, 'sheng_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21101, 'sheng_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21102, 'sheng_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21103, 'sheng_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21104, 'sheng_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21105, 'sheng_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21106, 'sheng_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21107, 'sheng_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21108, 'sheng_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21109, 'sheng_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21110, 'sheng_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21111, 'sheng_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21112, 'sheng_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21113, 'ysep_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21114, 'ysep_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21115, 'ysep_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21116, 'ysep_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21117, 'ysep_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21118, 'ysep_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21119, 'ysep_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21120, 'ysep_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21121, 'ysep_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21122, 'ysep_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21123, 'ysep_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21124, 'ysep_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21125, 'ysep_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21126, 'ysep_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21127, 'ysep_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21128, 'ysep_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21129, 'quick_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21130, 'quick_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21131, 'quick_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21132, 'quick_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21133, 'quick_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21134, 'quick_pay', 'wechat_barcode', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21135, 'quick_pay', 'alipay_order_qr', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21136, 'quick_pay', 'alipay_jsapi', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21137, 'quick_pay', 'alipay_app', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21138, 'quick_pay', 'alipay_h5', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21139, 'quick_pay', 'alipay_pc', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21140, 'quick_pay', 'alipay_barcode', 11, true, NULL, false, 1, now(), 0, 1, now()),
  (21141, 'quick_pay', 'union_pay_qr', 12, true, NULL, false, 1, now(), 0, 1, now()),
  (21142, 'quick_pay', 'union_pay_jsapi', 13, true, NULL, false, 1, now(), 0, 1, now()),
  (21143, 'quick_pay', 'union_pay_h5', 14, true, NULL, false, 1, now(), 0, 1, now()),
  (21144, 'quick_pay', 'union_pay_barcode', 15, true, NULL, false, 1, now(), 0, 1, now()),
  (21145, 'jee_pay', 'wechat_qr', 0, true, NULL, false, 1, now(), 0, 1, now()),
  (21146, 'jee_pay', 'wechat_jsapi', 1, true, NULL, false, 1, now(), 0, 1, now()),
  (21147, 'jee_pay', 'wechat_app', 2, true, NULL, false, 1, now(), 0, 1, now()),
  (21148, 'jee_pay', 'wechat_h5', 3, true, NULL, false, 1, now(), 0, 1, now()),
  (21149, 'jee_pay', 'wechat_mini', 4, true, NULL, false, 1, now(), 0, 1, now()),
  (21150, 'jee_pay', 'alipay_order_qr', 5, true, NULL, false, 1, now(), 0, 1, now()),
  (21151, 'jee_pay', 'alipay_jsapi', 6, true, NULL, false, 1, now(), 0, 1, now()),
  (21152, 'jee_pay', 'alipay_app', 7, true, NULL, false, 1, now(), 0, 1, now()),
  (21153, 'jee_pay', 'alipay_h5', 8, true, NULL, false, 1, now(), 0, 1, now()),
  (21154, 'jee_pay', 'alipay_pc', 9, true, NULL, false, 1, now(), 0, 1, now()),
  (21155, 'jee_pay', 'union_pay_qr', 10, true, NULL, false, 1, now(), 0, 1, now()),
  (21156, 'jee_pay', 'union_pay_jsapi', 11, true, NULL, false, 1, now(), 0, 1, now())
ON CONFLICT (id) DO NOTHING;

-- ------------------------------------------------------------
-- 4. 统一重排通道 sort_no（覆盖全量 17 通道）
--    顺序：支付宝/微信/抖音/银联商务/拉卡拉/乐刷/随行付/斗拱/海科/易宝/汇付/杉德/富友/盛付通/银盛/快钱/jeepay
-- ------------------------------------------------------------
UPDATE "public"."pay_md_channel" SET sort_no = CASE code
  WHEN 'alipay' THEN 1
  WHEN 'wechat' THEN 2
  WHEN 'douyin_pay' THEN 3
  WHEN 'ums_pay' THEN 4
  WHEN 'lakala_pay' THEN 5
  WHEN 'leshua_pay' THEN 6
  WHEN 'vbill_pay' THEN 7
  WHEN 'dougong_pay' THEN 8
  WHEN 'hkrt_pay' THEN 9
  WHEN 'yee_pay' THEN 10
  WHEN 'ada_pay' THEN 11
  WHEN 'sand_pay' THEN 12
  WHEN 'fuyou_pay' THEN 13
  WHEN 'sheng_pay' THEN 14
  WHEN 'ysep_pay' THEN 15
  WHEN 'quick_pay' THEN 16
  WHEN 'jee_pay' THEN 17
END
WHERE code IN ('alipay', 'wechat', 'douyin_pay', 'ums_pay', 'lakala_pay', 'leshua_pay', 'vbill_pay', 'dougong_pay', 'hkrt_pay', 'yee_pay', 'ada_pay', 'sand_pay', 'fuyou_pay', 'sheng_pay', 'ysep_pay', 'quick_pay', 'jee_pay');

-- ------------------------------------------------------------
-- 5. 统一重排产品 sort_no（覆盖全量 22 产品，同通道聚集）
-- ------------------------------------------------------------
UPDATE "public"."pay_md_product" SET sort_no = CASE code
  WHEN 'alipay' THEN 10
  WHEN 'alipay_isv' THEN 11
  WHEN 'wechat_pay' THEN 20
  WHEN 'wechat_isv' THEN 21
  WHEN 'douyin_pay' THEN 30
  WHEN 'ums_qrcode' THEN 40
  WHEN 'ums_jsapi' THEN 41
  WHEN 'ums_app' THEN 42
  WHEN 'ums_mini' THEN 43
  WHEN 'ums_h5' THEN 44
  WHEN 'ums_barcode' THEN 45
  WHEN 'lakala_pay' THEN 50
  WHEN 'dougong_pay' THEN 80
  WHEN 'hkrt_pay' THEN 90
  WHEN 'yee_pay' THEN 100
  WHEN 'ada_pay' THEN 110
  WHEN 'sand_pay' THEN 120
  WHEN 'fuyou_pay' THEN 130
  WHEN 'sheng_pay' THEN 140
  WHEN 'ysep_pay' THEN 150
  WHEN 'quick_pay' THEN 160
  WHEN 'jee_pay' THEN 170
END
WHERE code IN ('alipay', 'alipay_isv', 'wechat_pay', 'wechat_isv', 'douyin_pay', 'ums_qrcode', 'ums_jsapi', 'ums_app', 'ums_mini', 'ums_h5', 'ums_barcode', 'lakala_pay', 'dougong_pay', 'hkrt_pay', 'yee_pay', 'ada_pay', 'sand_pay', 'fuyou_pay', 'sheng_pay', 'ysep_pay', 'quick_pay', 'jee_pay');
