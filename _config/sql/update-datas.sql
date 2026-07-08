-- ============================================================
-- 乐刷通道(LESHUA)主数据
-- ============================================================

-- 乐刷支付产品(pay_md_channel 已有 leshua_pay 记录, id=6)
INSERT INTO "public"."pay_md_product" VALUES (10022, 'leshua_pay', '乐刷支付', 'leshua_pay', '乐刷聚合支付(微信/支付宝/云闪付)', '["T0", "T1"]', 60, 1, '2026-07-06 00:00:00', 1, '2026-07-06 00:00:00', 0, 'f', 't', 't');

-- 乐刷支付产品能力(对齐 LeshuaProductStrategy.methodCapabilityMapping)
INSERT INTO "public"."pay_md_product_capability" VALUES (6100, 'leshua_pay', 'wechat_barcode', 0, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6101, 'leshua_pay', 'alipay_barcode', 1, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6102, 'leshua_pay', 'union_pay_barcode', 2, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6103, 'leshua_pay', 'wechat_jsapi', 3, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6104, 'leshua_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6105, 'leshua_pay', 'alipay_qr', 5, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6106, 'leshua_pay', 'alipay_jsapi', 6, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6107, 'leshua_pay', 'alipay_mini', 7, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6108, 'leshua_pay', 'union_pay_qr', 8, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6109, 'leshua_pay', 'union_pay_jsapi', 9, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');

-- 乐刷产品默认配置(沙箱环境)
INSERT INTO "public"."pay_md_product_config" VALUES (2073794519919456257, 'leshua_pay', 'leshua_pay', 'sandbox', 'f', NULL, 1, '2026-07-06 00:00:00+00', 1, '2026-07-06 00:00:00+00', 1, 'f');

-- ============================================================
-- 随行付通道(VBILL)主数据
-- ============================================================

-- 随行付支付产品(pay_md_channel 已有 vbill_pay 记录, id=7)
INSERT INTO "public"."pay_md_product" VALUES (10023, 'vbill_pay', '随行付', 'vbill_pay', '随行付聚合支付(微信/支付宝/云闪付)', '["T0", "T1"]', 70, 1, '2026-07-06 00:00:00', 1, '2026-07-06 00:00:00', 0, 'f', 't', 't');

-- 随行付产品默认配置(生产环境)
INSERT INTO "public"."pay_md_product_config" VALUES (2073794519919456258, 'vbill_pay', 'vbill_pay', 'prod', 'f', NULL, 1, '2026-07-06 00:00:00+00', 1, '2026-07-06 00:00:00+00', 1, 'f');

-- ============================================================
-- 河马付通道(HMPAY/杉德旗下产品)主数据
-- 通道 = sand_pay(杉德, ChannelEnum.SAND_PAY, 已在 pay_md_channel 预置)
-- 产品 = hm_pay(河马付, ProductEnum.HM_PAY, 杉德旗下聚合支付产品)
-- ============================================================

-- 河马付支付产品(channel=sand_pay, 杉德通道; isv=t 服务商模式)
INSERT INTO "public"."pay_md_product" VALUES (10024, 'hm_pay', '河马付', 'sand_pay', '杉德旗下聚合支付产品(微信/支付宝/扫码/条码)', '["T0", "T1"]', 80, 1, '2026-07-07 00:00:00', 1, '2026-07-07 00:00:00', 0, 'f', 't', 'f');

-- 河马付支付产品能力(对齐 HmpayProductStrategy.methodCapabilityMapping)
INSERT INTO "public"."pay_md_product_capability" VALUES (6200, 'hm_pay', 'aggregate_pay_qrcode', 0, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6201, 'hm_pay', 'aggregate_pay_barcode', 1, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6202, 'hm_pay', 'wechat_qr', 2, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6203, 'hm_pay', 'wechat_jsapi', 3, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6204, 'hm_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6205, 'hm_pay', 'alipay_qr', 5, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6206, 'hm_pay', 'alipay_jsapi', 6, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6207, 'hm_pay', 'alipay_mini', 7, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');

-- 河马付产品默认配置(沙箱环境)
INSERT INTO "public"."pay_md_product_config" VALUES (2073794519919456259, 'hm_pay', 'sand_pay', 'sandbox', 't', NULL, 1, '2026-07-07 00:00:00+00', 1, '2026-07-07 00:00:00+00', 1, 'f');

-- ============================================================
-- 汇付天下通道(ADA_PAY)主数据补全 + 斗拱(DOUGONG_PAY)归并
-- 汇付天下(ada_pay, 直连)与斗拱(dougong_pay)同属汇付天下通道,
-- 参照 alipay 通道下 alipay(直连)+alipay_isv(服务商)的多产品模式,
-- 将斗拱作为汇付天下通道下的一个产品, 而非独立通道。
-- ============================================================

-- 汇付天下通道介绍与图标
UPDATE "public"."pay_md_channel" SET description='汇付天下聚合支付通道', icon='ada_pay' WHERE code='ada_pay';

-- Adapay 产品(直连)名称/介绍/图标(name 原为 '汇付天下', 与通道重名, 统一改为 Adapay)
UPDATE "public"."pay_md_product" SET name='Adapay', description='Adapay直连聚合支付(微信/支付宝/银联)' WHERE code='ada_pay';

-- 斗拱产品名称/介绍/图标, 并将 channel 从 'dougong_pay' 归并到 'ada_pay' 通道
UPDATE "public"."pay_md_product" SET name='斗拱', description='斗拱聚合支付(微信/支付宝/银联)', channel='ada_pay' WHERE code='dougong_pay';

-- 删除独立的斗拱通道记录(斗拱已作为产品挂在汇付天下通道下, 不再作为独立通道)
DELETE FROM "public"."pay_md_channel" WHERE code='dougong_pay';

-- ============================================================
-- 防御性数据迁移: 历史订单/交易/退款单的 channel 字段从 'dougong_pay' 刷为 'ada_pay'
-- (ada_pay/dougong_pay 未正式激活, 实际无数据, 此处为防御性兜底)
-- 涉及表: pay_normal_order / pay_trade / pay_refund_order (均含 channel 字段)
-- ============================================================
UPDATE "public"."pay_normal_order" SET channel='ada_pay' WHERE channel='dougong_pay';
UPDATE "public"."pay_trade" SET channel='ada_pay' WHERE channel='dougong_pay';
UPDATE "public"."pay_refund_order" SET channel='ada_pay' WHERE channel='dougong_pay';

-- ============================================================
-- 易宝通道(YEE_PAY)主数据补全
-- channel/product/capability 已在 pay_md.sql 预置(id=19/10020/21145-21160),
-- 此处补充: 聚合扫码能力 + 产品默认配置(沙箱)
-- ============================================================

-- 易宝聚合扫码能力(对齐 YeepayProductStrategy.methodCapabilityMapping, 预置数据缺失此项)
INSERT INTO "public"."pay_md_product_capability" VALUES (21161, 'yee_pay', 'aggregate_pay_qrcode', 16, 't', NULL, 'f', 1, '2026-07-08 00:00:00', 0, 1, '2026-07-08 00:00:00');

-- 易宝产品默认配置(沙箱环境, 对齐 YeepayProductStrategy.isSandbox=true)
INSERT INTO "public"."pay_md_product_config" VALUES (2073794519919456260, 'yee_pay', 'yee_pay', 'sandbox', 'f', NULL, 1, '2026-07-08 00:00:00+00', 1, '2026-07-08 00:00:00+00', 1, 'f');

-- ============================================================
-- 沙箱环境维度数据迁移: 存量密钥记录按产品当前 activeEnv 归类
-- 密钥表新增 sandbox 列后, 将现有唯一一份密钥归入产品当前生效环境
-- ============================================================

-- 服务商密钥(按 product 直接关联 pay_md_product_config)
UPDATE lakala_isv_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM pay_md_product_config p WHERE p.product = k.product;
UPDATE fuyou_isv_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM pay_md_product_config p WHERE p.product = k.product;
UPDATE leshua_isv_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM pay_md_product_config p WHERE p.product = k.product;
UPDATE hkrt_isv_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM pay_md_product_config p WHERE p.product = k.product;
UPDATE vbill_isv_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM pay_md_product_config p WHERE p.product = k.product;
UPDATE hmpay_isv_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM pay_md_product_config p WHERE p.product = k.product;

-- 直连密钥(通过 mch_channel_merchant 中转拿 product)
UPDATE yeepay_direct_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM mch_channel_merchant m JOIN pay_md_product_config p ON p.product = m.product WHERE m.channel_mch_no = k.channel_mch_no;
UPDATE adapay_direct_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM mch_channel_merchant m JOIN pay_md_product_config p ON p.product = m.product WHERE m.channel_mch_no = k.channel_mch_no;
UPDATE ums_direct_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM mch_channel_merchant m JOIN pay_md_product_config p ON p.product = m.product WHERE m.channel_mch_no = k.channel_mch_no;
UPDATE alipay_direct_app_key_config k SET sandbox = COALESCE(p.active_env = 'sandbox', false) FROM mch_channel_merchant m JOIN pay_md_product_config p ON p.product = m.product WHERE m.channel_mch_no = k.channel_mch_no;
