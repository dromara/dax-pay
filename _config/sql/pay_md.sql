/*
 Navicat Premium Data Transfer

 Source Server         : 229本地服务
 Source Server Type    : PostgreSQL
 Source Server Version : 160014 (160014)
 Source Host           : 192.168.1.229:5432
 Source Catalog        : daxpay-dev
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 160014 (160014)
 File Encoding         : 65001

 Date: 07/07/2026 21:23:36
*/


-- ----------------------------
-- Table structure for pay_md_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_capability";
CREATE TABLE "public"."pay_md_capability" (
  "id" int8 NOT NULL,
  "code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "enabled" bool NOT NULL DEFAULT true,
  "description" varchar(512) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_md_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_capability"."code" IS '支付能力编码（PayCapabilityEnum.code）';
COMMENT ON COLUMN "public"."pay_md_capability"."sort_no" IS '全局排序';
COMMENT ON COLUMN "public"."pay_md_capability"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."pay_md_capability"."description" IS '说明';
COMMENT ON COLUMN "public"."pay_md_capability"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_md_capability"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_md_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_capability"."version" IS '版本号（乐观锁）';
COMMENT ON COLUMN "public"."pay_md_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_capability"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."pay_md_capability" IS '支付能力主数据';

-- ----------------------------
-- Records of pay_md_capability
-- ----------------------------
INSERT INTO "public"."pay_md_capability" VALUES (5001, 'aggregate_pay_qrcode', 0, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5002, 'aggregate_pay_barcode', 1, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5003, 'wechat_cashier', 2, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5004, 'wechat_jsapi', 3, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5005, 'wechat_app', 4, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5006, 'wechat_h5', 5, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5007, 'wechat_qr', 6, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5008, 'wechat_mini', 7, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5009, 'wechat_barcode', 8, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5010, 'alipay_barcode', 9, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5012, 'alipay_app', 11, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5013, 'alipay_h5', 12, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5014, 'alipay_pc', 13, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5015, 'alipay_jsapi', 14, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5016, 'union_pay_qr', 15, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5017, 'union_pay_barcode', 16, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5018, 'union_pay_h5', 17, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5019, 'union_pay_jsapi', 18, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5020, 'visa_card_gateway', 19, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5021, 'visa_card_present', 20, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5022, 'mastercard_card_gateway', 21, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (5023, 'mastercard_card_present', 22, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');
INSERT INTO "public"."pay_md_capability" VALUES (7001, 'douyin_qr', 1, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_capability" VALUES (7002, 'douyin_jsapi', 2, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_capability" VALUES (7003, 'douyin_h5', 3, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_capability" VALUES (7004, 'douyin_app', 4, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_capability" VALUES (5011, 'alipay_qr', 10, 't', NULL, 'f', 1, '2026-05-29 04:18:20.415995', 0, 1, '2026-05-29 04:18:20.415995');

-- ----------------------------
-- Table structure for pay_md_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_channel";
CREATE TABLE "public"."pay_md_channel" (
  "id" int8 NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 DEFAULT 0,
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_md_channel"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_channel"."code" IS '通道编码';
COMMENT ON COLUMN "public"."pay_md_channel"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."pay_md_channel"."description" IS '通道介绍';
COMMENT ON COLUMN "public"."pay_md_channel"."icon" IS '图标';
COMMENT ON COLUMN "public"."pay_md_channel"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_channel"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_channel"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_md_channel"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_channel"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_md_channel"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_md_channel" IS '支付通道';

-- ----------------------------
-- Records of pay_md_channel
-- ----------------------------
INSERT INTO "public"."pay_md_channel" VALUES (1, 'alipay', 1, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (3, 'wechat', 2, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (6, 'leshua_pay', 6, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (7, 'vbill_pay', 7, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (8, 'lakala_pay', 5, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (5, 'ums_pay', 4, NULL, NULL, 1, '2026-05-29 00:00:00', 1, '2026-05-29 00:00:00', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (9, 'douyin_pay', 3, '抖音支付通道', 'douyinPay', 1, '2026-06-15 00:00:00', 1, '2026-06-15 00:00:00', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (13, 'hkrt_pay', 9, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (14, 'fuyou_pay', 13, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (15, 'sheng_pay', 14, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (16, 'ysep_pay', 15, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (17, 'quick_pay', 16, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (18, 'sand_pay', 12, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (19, 'yee_pay', 10, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (20, 'jee_pay', 17, NULL, NULL, 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (11, 'ada_pay', 11, '汇付天下Adapay聚合支付通道', 'ada_pay', 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');
INSERT INTO "public"."pay_md_channel" VALUES (12, 'dougong_pay', 8, '斗拱支付(汇付天下)聚合通道', 'dougong', 1, '2026-07-05 23:19:01.965299', 1, '2026-07-05 23:19:01.965299', 0, 'f');

-- ----------------------------
-- Table structure for pay_md_method
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_method";
CREATE TABLE "public"."pay_md_method" (
  "id" int8 NOT NULL,
  "code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "description" varchar(512) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_md_method"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_method"."code" IS '支付方式编码（全局唯一）';
COMMENT ON COLUMN "public"."pay_md_method"."sort_no" IS '全局排序';
COMMENT ON COLUMN "public"."pay_md_method"."description" IS '说明';
COMMENT ON COLUMN "public"."pay_md_method"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_md_method"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_md_method"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_method"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_md_method"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_method"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."pay_md_method" IS '支付方式';

-- ----------------------------
-- Records of pay_md_method
-- ----------------------------
INSERT INTO "public"."pay_md_method" VALUES (502003001, 'aggregate_pay_qrcode', 1, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003002, 'aggregate_pay_barcode', 2, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003003, 'wechat_cashier', 3, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003004, 'wechat_qr', 4, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003005, 'wechat_jsapi', 5, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003006, 'wechat_mini', 6, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003007, 'wechat_h5', 7, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003008, 'wechat_app', 8, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003009, 'wechat_barcode', 9, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003010, 'alipay_qr', 10, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003012, 'alipay_jsapi', 12, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003013, 'alipay_mini', 13, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003014, 'alipay_pc', 14, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003015, 'alipay_h5', 15, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003016, 'alipay_app', 16, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003017, 'alipay_barcode', 17, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003018, 'union_qr', 18, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003019, 'union_jsapi', 19, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003020, 'union_h5', 20, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003021, 'union_pay_barcode', 21, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003022, 'visa_card_gateway', 22, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003023, 'visa_card_present', 23, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003024, 'mastercard_card_gateway', 24, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003025, 'mastercard_card_present', 25, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003026, 'other', 26, NULL, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977');
INSERT INTO "public"."pay_md_method" VALUES (502003027, 'douyin_qr', 1, NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_method" VALUES (502003028, 'douyin_jsapi', 2, NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_method" VALUES (502003029, 'douyin_h5', 3, NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_method" VALUES (502003030, 'douyin_app', 4, NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');

-- ----------------------------
-- Table structure for pay_md_product
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_product";
CREATE TABLE "public"."pay_md_product" (
  "id" int8 NOT NULL,
  "code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "description" text COLLATE "pg_catalog"."default",
  "sort_no" int4 DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "sandbox" bool DEFAULT false,
  "enabled" bool NOT NULL DEFAULT true
)
;
COMMENT ON COLUMN "public"."pay_md_product"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_product"."code" IS '产品编码';
COMMENT ON COLUMN "public"."pay_md_product"."name" IS '产品名称';
COMMENT ON COLUMN "public"."pay_md_product"."channel" IS '关联通道编码';
COMMENT ON COLUMN "public"."pay_md_product"."description" IS '产品介绍';
COMMENT ON COLUMN "public"."pay_md_product"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."pay_md_product"."creator" IS '创建者';
COMMENT ON COLUMN "public"."pay_md_product"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_product"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."pay_md_product"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_product"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_md_product"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_md_product"."sandbox" IS '是否支持沙箱环境';
COMMENT ON TABLE "public"."pay_md_product" IS '支付产品';

-- ----------------------------
-- Records of pay_md_product
-- ----------------------------
INSERT INTO "public"."pay_md_product" VALUES (10012, 'ada_pay', 'Adapay', 'ada_pay', 'Adapay聚合支付(微信/支付宝/银联)', '["T0", "T1"]', 110, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (1001, 'alipay_isv', '支付宝(服务商)', 'alipay', '支付宝服务商模式，支持多种支付方式、进件申请和分账', '["T+1", "D+1", "T+0", "D+0"]', 11, NULL, NULL, NULL, NULL, 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10009, 'wechat_pay', '微信支付(直连)', 'wechat', '微信支付直连商户模式', '["T0", "T1"]', 20, NULL, NULL, NULL, NULL, 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (1003, 'wechat_isv', '微信支付(服务商)', 'wechat', '微信支付服务商模式，支持多种支付方式、进件申请和分账', '["T+1", "D+1", "T+0", "D+0"]', 21, NULL, NULL, NULL, NULL, 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10001, 'ums_qrcode', '银联商务(C扫B)', 'ums_pay', '银联商务C扫B支付（主扫）', '["T0", "T1"]', 40, NULL, NULL, NULL, NULL, 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10002, 'ums_jsapi', '银联商务(公众号)', 'ums_pay', '银联商务公众号支付', '["T0", "T1"]', 41, NULL, NULL, NULL, NULL, 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10003, 'ums_app', '银联商务(APP)', 'ums_pay', '银联商务APP支付', '["T0", "T1"]', 42, NULL, NULL, NULL, NULL, 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10004, 'ums_mini', '银联商务(小程序)', 'ums_pay', '银联商务小程序支付', '["T0", "T1"]', 43, NULL, NULL, NULL, NULL, 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10005, 'ums_h5', '银联商务(H5)', 'ums_pay', '银联商务H5支付', '["T0", "T1"]', 44, NULL, NULL, NULL, NULL, 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10006, 'ums_barcode', '银联商务(B扫C)', 'ums_pay', '银联商务B扫C支付（被扫）', '["T0", "T1"]', 45, NULL, NULL, NULL, NULL, 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10007, 'lakala_pay', '拉卡拉支付', 'lakala_pay', '拉卡拉支付', '["T0", "T1"]', 50, NULL, NULL, NULL, NULL, 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10010, 'douyin_pay', '抖音支付(直连)', 'douyin_pay', '抖音支付直连商户模式', '["T0", "T1"]', 30, NULL, NULL, NULL, NULL, 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10008, 'alipay', '支付宝(直连)', 'alipay', '支付宝直连商户模式', '["T0", "T1"]', 10, NULL, NULL, 1, '2026-06-17 09:53:02.203443', 4, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10014, 'hkrt_pay', 'hkrt_pay', 'hkrt_pay', NULL, '["T0", "T1"]', 90, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10015, 'fuyou_pay', 'fuyou_pay', 'fuyou_pay', NULL, '["T0", "T1"]', 130, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10016, 'sheng_pay', 'sheng_pay', 'sheng_pay', NULL, '["T0", "T1"]', 140, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10017, 'ysep_pay', 'ysep_pay', 'ysep_pay', NULL, '["T0", "T1"]', 150, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10018, 'quick_pay', 'quick_pay', 'quick_pay', NULL, '["T0", "T1"]', 160, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10020, 'yee_pay', 'yee_pay', 'yee_pay', NULL, '["T0", "T1"]', 100, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10021, 'jee_pay', 'jee_pay', 'jee_pay', NULL, '["T0", "T1"]', 170, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');
INSERT INTO "public"."pay_md_product" VALUES (10022, 'leshua_pay', '乐刷支付', 'leshua_pay', '乐刷聚合支付(微信/支付宝/云闪付)', '["T0", "T1"]', 60, 1, '2026-07-06 00:00:00', 1, '2026-07-06 00:00:00', 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10024, 'hm_pay', '河马付', 'sand_pay', '杉德旗下聚合支付产品(微信/支付宝/扫码/条码)', '["T0", "T1"]', 80, 1, '2026-07-07 00:00:00', 1, '2026-07-07 00:00:00', 0, 'f', 't', 'f');
INSERT INTO "public"."pay_md_product" VALUES (10023, 'vbill_pay', '随行付', 'vbill_pay', '随行付聚合支付(微信/支付宝/云闪付)', '["T0", "T1"]', 70, 1, '2026-07-06 00:00:00', 1, '2026-07-06 00:00:00', 0, 'f', 't', 't');
INSERT INTO "public"."pay_md_product" VALUES (10013, 'dougong_pay', '斗拱', 'dougong_pay', '斗拱支付(汇付天下)聚合支付(微信/支付宝/银联)', '["T0", "T1"]', 80, 1, '2026-07-05 23:19:01.974607', 1, '2026-07-05 23:19:01.974607', 0, 'f', 'f', 't');

-- ----------------------------
-- Table structure for pay_md_product_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_product_capability";
CREATE TABLE "public"."pay_md_product_capability" (
  "id" int8 NOT NULL,
  "product_code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "capability_code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "enabled" bool NOT NULL DEFAULT true,
  "remark" varchar(512) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."pay_md_product_capability"."product_code" IS '支付产品编码';
COMMENT ON COLUMN "public"."pay_md_product_capability"."capability_code" IS '支付能力编码';
COMMENT ON COLUMN "public"."pay_md_product_capability"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."pay_md_product_capability"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."pay_md_product_capability"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_md_product_capability" IS '支付产品与支付能力关联';

-- ----------------------------
-- Records of pay_md_product_capability
-- ----------------------------
INSERT INTO "public"."pay_md_product_capability" VALUES (6001, 'alipay_isv', 'alipay_barcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6003, 'alipay_isv', 'alipay_jsapi', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6004, 'alipay_isv', 'alipay_pc', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6005, 'alipay_isv', 'alipay_h5', 4, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6006, 'alipay_isv', 'alipay_app', 5, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6010, 'wechat_isv', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6011, 'wechat_isv', 'wechat_app', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6012, 'wechat_isv', 'wechat_h5', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6013, 'wechat_isv', 'wechat_barcode', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6014, 'wechat_isv', 'wechat_jsapi', 4, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6015, 'wechat_isv', 'wechat_mini', 5, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6020, 'ums_qrcode', 'aggregate_pay_qrcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6021, 'ums_qrcode', 'union_pay_qr', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6023, 'ums_qrcode', 'wechat_qr', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6030, 'ums_jsapi', 'wechat_jsapi', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6031, 'ums_jsapi', 'alipay_jsapi', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6040, 'ums_app', 'wechat_app', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6041, 'ums_app', 'alipay_app', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6050, 'ums_mini', 'wechat_mini', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6060, 'ums_h5', 'wechat_h5', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6061, 'ums_h5', 'alipay_h5', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6062, 'ums_h5', 'union_pay_h5', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6070, 'ums_barcode', 'union_pay_barcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6080, 'lakala_pay', 'wechat_barcode', 0, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6081, 'lakala_pay', 'alipay_barcode', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6082, 'lakala_pay', 'union_pay_barcode', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6083, 'lakala_pay', 'wechat_jsapi', 3, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6084, 'lakala_pay', 'wechat_app', 4, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6085, 'lakala_pay', 'wechat_mini', 5, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6087, 'lakala_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6088, 'lakala_pay', 'union_pay_qr', 8, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6089, 'lakala_pay', 'union_pay_jsapi', 9, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (20081, 'alipay', 'alipay_barcode', 1, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20083, 'alipay', 'alipay_jsapi', 3, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20084, 'alipay', 'alipay_mini', 4, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20085, 'alipay', 'alipay_pc', 5, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20086, 'alipay', 'alipay_h5', 6, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20087, 'alipay', 'alipay_app', 7, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20091, 'wechat_pay', 'wechat_qr', 1, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20092, 'wechat_pay', 'wechat_app', 2, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20093, 'wechat_pay', 'wechat_h5', 3, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20094, 'wechat_pay', 'wechat_barcode', 4, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20095, 'wechat_pay', 'wechat_jsapi', 5, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20096, 'wechat_pay', 'wechat_mini', 6, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (20101, 'douyin_pay', 'douyin_qr', 1, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (20102, 'douyin_pay', 'douyin_jsapi', 2, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (20103, 'douyin_pay', 'douyin_h5', 3, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (20104, 'douyin_pay', 'douyin_app', 4, 't', NULL, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (21017, 'ada_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21018, 'ada_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21019, 'ada_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21020, 'ada_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21021, 'ada_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21022, 'ada_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (6002, 'alipay_isv', 'alipay_qr', 1, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6022, 'ums_qrcode', 'alipay_qr', 2, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (6086, 'lakala_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-05-29 07:03:18.274881', 0, 1, '2026-05-29 07:03:18.274881');
INSERT INTO "public"."pay_md_product_capability" VALUES (20082, 'alipay', 'alipay_qr', 2, 't', NULL, 'f', NULL, NULL, 0, NULL, NULL);
INSERT INTO "public"."pay_md_product_capability" VALUES (21024, 'ada_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21025, 'ada_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21026, 'ada_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21027, 'ada_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21028, 'ada_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21029, 'ada_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21030, 'ada_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21031, 'ada_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21032, 'ada_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21033, 'dougong_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21034, 'dougong_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21035, 'dougong_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21036, 'dougong_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21037, 'dougong_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21038, 'dougong_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21040, 'dougong_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21041, 'dougong_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21042, 'dougong_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21043, 'dougong_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21044, 'dougong_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21045, 'dougong_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21046, 'dougong_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21047, 'dougong_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21048, 'dougong_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21049, 'hkrt_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21050, 'hkrt_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21051, 'hkrt_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21052, 'hkrt_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21053, 'hkrt_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21054, 'hkrt_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21056, 'hkrt_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21057, 'hkrt_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21058, 'hkrt_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21059, 'hkrt_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21060, 'hkrt_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21061, 'hkrt_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21062, 'hkrt_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21063, 'hkrt_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21064, 'hkrt_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21065, 'fuyou_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21066, 'fuyou_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21067, 'fuyou_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21068, 'fuyou_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21069, 'fuyou_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21070, 'fuyou_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21072, 'fuyou_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21073, 'fuyou_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21074, 'fuyou_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21075, 'fuyou_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21076, 'fuyou_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21077, 'fuyou_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21078, 'fuyou_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21079, 'fuyou_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21080, 'fuyou_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21081, 'sheng_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21082, 'sheng_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21083, 'sheng_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21084, 'sheng_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21085, 'sheng_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21086, 'sheng_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21088, 'sheng_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21089, 'sheng_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21090, 'sheng_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21091, 'sheng_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21092, 'sheng_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21093, 'sheng_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21094, 'sheng_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21095, 'sheng_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21096, 'sheng_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21097, 'ysep_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21098, 'ysep_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21099, 'ysep_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21100, 'ysep_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21101, 'ysep_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21102, 'ysep_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21104, 'ysep_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21105, 'ysep_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21106, 'ysep_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21107, 'ysep_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21108, 'ysep_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21109, 'ysep_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21110, 'ysep_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21111, 'ysep_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21112, 'ysep_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21113, 'quick_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21114, 'quick_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21115, 'quick_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21116, 'quick_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21117, 'quick_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21118, 'quick_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21120, 'quick_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21121, 'quick_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21122, 'quick_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21123, 'quick_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21124, 'quick_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21125, 'quick_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21126, 'quick_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21127, 'quick_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21128, 'quick_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21145, 'yee_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21146, 'yee_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21147, 'yee_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21148, 'yee_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21149, 'yee_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21150, 'yee_pay', 'wechat_barcode', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21152, 'yee_pay', 'alipay_jsapi', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21153, 'yee_pay', 'alipay_app', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21154, 'yee_pay', 'alipay_h5', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21155, 'yee_pay', 'alipay_pc', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21156, 'yee_pay', 'alipay_barcode', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21157, 'yee_pay', 'union_pay_qr', 12, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21158, 'yee_pay', 'union_pay_jsapi', 13, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21159, 'yee_pay', 'union_pay_h5', 14, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21160, 'yee_pay', 'union_pay_barcode', 15, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21161, 'jee_pay', 'wechat_qr', 0, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21162, 'jee_pay', 'wechat_jsapi', 1, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21163, 'jee_pay', 'wechat_app', 2, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21164, 'jee_pay', 'wechat_h5', 3, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21165, 'jee_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21167, 'jee_pay', 'alipay_jsapi', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21168, 'jee_pay', 'alipay_app', 7, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21169, 'jee_pay', 'alipay_h5', 8, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21170, 'jee_pay', 'alipay_pc', 9, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21171, 'jee_pay', 'union_pay_qr', 10, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21172, 'jee_pay', 'union_pay_jsapi', 11, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21023, 'ada_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21039, 'dougong_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21055, 'hkrt_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21071, 'fuyou_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21087, 'sheng_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21103, 'ysep_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21119, 'quick_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21151, 'yee_pay', 'alipay_qr', 6, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
INSERT INTO "public"."pay_md_product_capability" VALUES (21166, 'jee_pay', 'alipay_qr', 5, 't', NULL, 'f', 1, '2026-07-05 23:19:01.978869', 0, 1, '2026-07-05 23:19:01.978869');
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
INSERT INTO "public"."pay_md_product_capability" VALUES (6200, 'hm_pay', 'aggregate_pay_qrcode', 0, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6201, 'hm_pay', 'aggregate_pay_barcode', 1, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6202, 'hm_pay', 'wechat_qr', 2, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6203, 'hm_pay', 'wechat_jsapi', 3, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6204, 'hm_pay', 'wechat_mini', 4, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6205, 'hm_pay', 'alipay_qr', 5, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6206, 'hm_pay', 'alipay_jsapi', 6, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6207, 'hm_pay', 'alipay_mini', 7, 't', NULL, 'f', 1, '2026-07-07 00:00:00', 0, 1, '2026-07-07 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6300, 'vbill_pay', 'wechat_jsapi', 0, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6301, 'vbill_pay', 'wechat_mini', 1, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6302, 'vbill_pay', 'wechat_qr', 2, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6303, 'vbill_pay', 'wechat_barcode', 3, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6304, 'vbill_pay', 'wechat_cashier', 4, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6305, 'vbill_pay', 'alipay_jsapi', 5, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6306, 'vbill_pay', 'alipay_mini', 6, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6307, 'vbill_pay', 'alipay_qr', 7, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6308, 'vbill_pay', 'alipay_barcode', 8, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6309, 'vbill_pay', 'union_pay_jsapi', 9, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6310, 'vbill_pay', 'union_pay_qr', 10, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');
INSERT INTO "public"."pay_md_product_capability" VALUES (6311, 'vbill_pay', 'union_pay_barcode', 11, 't', NULL, 'f', 1, '2026-07-06 00:00:00', 0, 1, '2026-07-06 00:00:00');

-- ----------------------------
-- Table structure for pay_md_product_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_product_config";
CREATE TABLE "public"."pay_md_product_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "active_env" varchar(32) COLLATE "pg_catalog"."default" DEFAULT 'prod'::character varying,
  "configured" bool DEFAULT false,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_md_product_config"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_product_config"."product" IS '产品编码';
COMMENT ON COLUMN "public"."pay_md_product_config"."channel" IS '通道编码';
COMMENT ON COLUMN "public"."pay_md_product_config"."active_env" IS '生效环境: prod/sandbox';
COMMENT ON COLUMN "public"."pay_md_product_config"."configured" IS '是否已配置参数';
COMMENT ON COLUMN "public"."pay_md_product_config"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_md_product_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_product_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_product_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_md_product_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_product_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_md_product_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_md_product_config" IS '支付产品配置';

-- ----------------------------
-- Records of pay_md_product_config
-- ----------------------------
INSERT INTO "public"."pay_md_product_config" VALUES (2067122110487265280, 'wechat_isv', 'wechat', 'prod', 'f', NULL, 1, '2026-06-17 05:48:11.072172+00', 1, '2026-06-17 05:48:11.109699+00', 1, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2067122132809351168, 'douyin_pay', 'douyin_pay', 'prod', 'f', NULL, 1, '2026-06-17 05:48:16.390739+00', 1, '2026-06-17 05:48:16.412758+00', 1, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2067122508711264256, 'alipay_isv', 'alipay', 'prod', 'f', NULL, 1, '2026-06-17 05:49:46.012172+00', 1, '2026-06-17 05:49:46.028695+00', 1, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2066401294799822848, 'alipay', 'alipay', 'sandbox', 'f', NULL, 1, '2026-06-15 06:03:55.216328+00', 1, '2026-07-04 07:28:11.745135+00', 13, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2073794519919456256, 'lakala_pay', 'lakala_pay', 'sandbox', 'f', NULL, 1, '2026-07-05 15:41:57.444509+00', 1, '2026-07-05 15:41:57.469525+00', 1, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2073964390288433152, 'ums_qrcode', 'ums_pay', 'sandbox', 'f', NULL, 1, '2026-07-06 02:56:57.69661+00', 1, '2026-07-06 02:56:57.716639+00', 1, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2074378302137782272, 'hkrt_pay', 'hkrt_pay', 'sandbox', 'f', NULL, 1, '2026-07-07 06:21:41.972669+00', 1, '2026-07-07 06:21:41.994201+00', 1, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2074461988443107328, 'vbill_pay', 'vbill_pay', 'sandbox', 'f', NULL, 1, '2026-07-07 11:54:14.341796+00', 1, '2026-07-07 11:54:14.367832+00', 1, 'f');
INSERT INTO "public"."pay_md_product_config" VALUES (2074475064877887488, 'hm_pay', 'sand_pay', 'sandbox', 'f', NULL, 1, '2026-07-07 12:46:12.006568+00', 1, '2026-07-07 12:46:12.034362+00', 1, 'f');

-- ----------------------------
-- Table structure for pay_md_provider
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_provider";
CREATE TABLE "public"."pay_md_provider" (
  "id" int8 NOT NULL,
  "code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "icon" varchar(255) COLLATE "pg_catalog"."default",
  "sort_no" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6),
  "enabled" bool NOT NULL DEFAULT true,
  "description" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_md_provider"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_provider"."code" IS '支付渠道编码（PayProviderEnum.code：aggregate_pay/wechat/alipay/union_pay/visa/mastercard）';
COMMENT ON COLUMN "public"."pay_md_provider"."icon" IS '图标（可选，覆盖前端默认展示）';
COMMENT ON COLUMN "public"."pay_md_provider"."sort_no" IS '排序（管理端 Tab/列表顺序）';
COMMENT ON COLUMN "public"."pay_md_provider"."deleted" IS '删除标志（逻辑删除）';
COMMENT ON COLUMN "public"."pay_md_provider"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_md_provider"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_provider"."version" IS '版本号（乐观锁）';
COMMENT ON COLUMN "public"."pay_md_provider"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_provider"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."pay_md_provider" IS '支付渠道';

-- ----------------------------
-- Records of pay_md_provider
-- ----------------------------
INSERT INTO "public"."pay_md_provider" VALUES (502001000, 'aggregate_pay', NULL, 0, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', 't', NULL);
INSERT INTO "public"."pay_md_provider" VALUES (502001001, 'wechat', NULL, 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', 't', NULL);
INSERT INTO "public"."pay_md_provider" VALUES (502001002, 'alipay', NULL, 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', 't', NULL);
INSERT INTO "public"."pay_md_provider" VALUES (502001003, 'union_pay', NULL, 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', 't', NULL);
INSERT INTO "public"."pay_md_provider" VALUES (502001004, 'visa', NULL, 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', 't', NULL);
INSERT INTO "public"."pay_md_provider" VALUES (502001005, 'mastercard', NULL, 5, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:55.128441', 't', NULL);
INSERT INTO "public"."pay_md_provider" VALUES (502001006, 'douyin', 'douyinPay', 60, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00', 't', NULL);

-- ----------------------------
-- Table structure for pay_md_provider_method
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_provider_method";
CREATE TABLE "public"."pay_md_provider_method" (
  "id" int8 NOT NULL,
  "provider" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6),
  "description" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_md_provider_method"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_provider_method"."provider" IS '支付渠道编码（对应 PayProviderMethod.provider / PayProviderEnum.code）';
COMMENT ON COLUMN "public"."pay_md_provider_method"."method" IS '支付方式编码（PayMethodEnum.code）';
COMMENT ON COLUMN "public"."pay_md_provider_method"."sort_no" IS '渠道内排序';
COMMENT ON COLUMN "public"."pay_md_provider_method"."deleted" IS '删除标志（逻辑删除）';
COMMENT ON COLUMN "public"."pay_md_provider_method"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_md_provider_method"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_provider_method"."version" IS '版本号（乐观锁）';
COMMENT ON COLUMN "public"."pay_md_provider_method"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_provider_method"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_provider_method"."description" IS '目录项说明';
COMMENT ON TABLE "public"."pay_md_provider_method" IS '支付渠道和方式关联';

-- ----------------------------
-- Records of pay_md_provider_method
-- ----------------------------
INSERT INTO "public"."pay_md_provider_method" VALUES (502001901, 'aggregate_pay', 'aggregate_pay_qrcode', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502001902, 'aggregate_pay', 'aggregate_pay_barcode', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002001, 'wechat', 'wechat_jsapi', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002002, 'wechat', 'wechat_app', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002003, 'wechat', 'wechat_h5', 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002004, 'wechat', 'wechat_qr', 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002005, 'wechat', 'wechat_mini', 5, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002006, 'wechat', 'wechat_barcode', 6, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002007, 'wechat', 'wechat_cashier', 7, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002008, 'alipay', 'alipay_barcode', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002010, 'alipay', 'alipay_app', 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002011, 'alipay', 'alipay_h5', 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002012, 'alipay', 'alipay_pc', 5, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002013, 'alipay', 'alipay_jsapi', 6, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002014, 'union_pay', 'union_qr', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002015, 'union_pay', 'union_pay_barcode', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002016, 'union_pay', 'union_h5', 3, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002017, 'union_pay', 'union_jsapi', 4, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002018, 'visa', 'visa_card_gateway', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002019, 'visa', 'visa_card_present', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002020, 'mastercard', 'mastercard_card_gateway', 1, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002021, 'mastercard', 'mastercard_card_present', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002022, 'douyin', 'douyin_qr', 1, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002023, 'douyin', 'douyin_jsapi', 2, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002024, 'douyin', 'douyin_h5', 3, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002025, 'douyin', 'douyin_app', 4, 'f', NULL, NULL, 0, NULL, '2026-06-15 00:00:00', NULL);
INSERT INTO "public"."pay_md_provider_method" VALUES (502002009, 'alipay', 'alipay_qr', 2, 'f', NULL, NULL, 0, NULL, '2026-05-28 15:26:06.620977', NULL);

-- ----------------------------
-- Indexes structure for table pay_md_capability
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_md_capability_code" ON "public"."pay_md_capability" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_md_capability_code" IS '支付能力编码唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_md_capability
-- ----------------------------
ALTER TABLE "public"."pay_md_capability" ADD CONSTRAINT "pay_md_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_md_channel
-- ----------------------------
ALTER TABLE "public"."pay_md_channel" ADD CONSTRAINT "pay_md_channel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_md_method
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_md_method_code" ON "public"."pay_md_method" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_md_method_code" IS '支付方式编码唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_md_method
-- ----------------------------
ALTER TABLE "public"."pay_md_method" ADD CONSTRAINT "pay_md_method_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_md_product
-- ----------------------------
CREATE UNIQUE INDEX "idx_pay_md_product_code" ON "public"."pay_md_product" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_md_product_code" IS '产品编码唯一索引';

-- ----------------------------
-- Primary Key structure for table pay_md_product
-- ----------------------------
ALTER TABLE "public"."pay_md_product" ADD CONSTRAINT "pay_md_product_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_md_product_capability
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_md_product_capability_pair" ON "public"."pay_md_product_capability" USING btree (
  "product_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "capability_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_md_product_capability_pair" IS '产品+能力唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_md_product_capability
-- ----------------------------
ALTER TABLE "public"."pay_md_product_capability" ADD CONSTRAINT "pay_md_product_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_md_product_config
-- ----------------------------
CREATE UNIQUE INDEX "idx_ppc_md_product" ON "public"."pay_md_product_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;

-- ----------------------------
-- Primary Key structure for table pay_md_product_config
-- ----------------------------
ALTER TABLE "public"."pay_md_product_config" ADD CONSTRAINT "pk_pay_md_product_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_md_provider
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_md_provider_code" ON "public"."pay_md_provider" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_md_provider_code" IS '支付渠道编码唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_md_provider
-- ----------------------------
ALTER TABLE "public"."pay_md_provider" ADD CONSTRAINT "pay_md_provider_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_md_provider_method
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_md_provider_method_pair" ON "public"."pay_md_provider_method" USING btree (
  "provider" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "method" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_md_provider_method_pair" IS '支付渠道+支付方式唯一（未删除）';

-- ----------------------------
-- Primary Key structure for table pay_md_provider_method
-- ----------------------------
ALTER TABLE "public"."pay_md_provider_method" ADD CONSTRAINT "pay_md_provider_method_pkey" PRIMARY KEY ("id");
