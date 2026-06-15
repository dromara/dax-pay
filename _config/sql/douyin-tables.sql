-- =====================================================================
-- 抖音支付直连通道相关表
-- 对应实体:
--   org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectChannelMerchant
--   org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectKeyConfig
--   org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectApp
--   org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectAppAuthConfig
-- 执行方式: 手动执行(业务表 DDL 不入库, 由开发者按需在数据库执行)
-- 注意: 加密字段(DataEncryptTypeHandler)在数据库层均为普通 text, 加解密在应用层完成
-- =====================================================================

-- =========================
-- 1. 抖音直连通道商户绑定 douyin_direct_channel_merchant
-- =========================
DROP TABLE IF EXISTS "public"."douyin_direct_channel_merchant";

CREATE TABLE "public"."douyin_direct_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "product" varchar(32) NOT NULL,
  "dy_mch_id" varchar(64),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."douyin_direct_channel_merchant" IS '抖音直连通道商户绑定';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."channel_mch_no" IS '通道商户号(系统生成雪花号)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."dy_mch_id" IS '抖音商户号(MCHID)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."douyin_direct_channel_merchant"
  ADD CONSTRAINT "pk_douyin_direct_channel_merchant" PRIMARY KEY ("id");


-- =========================
-- 2. 抖音直连密钥配置 douyin_direct_key_config
-- 商户维度的密钥和证书配置, 按通道商户号关联, 敏感字段加密存储
-- =========================
DROP TABLE IF EXISTS "public"."douyin_direct_key_config";

CREATE TABLE "public"."douyin_direct_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "merchant_private_key" text,
  "merchant_serial_number" varchar(64),
  "encrypt_key" text,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."douyin_direct_key_config" IS '抖音直连密钥配置';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."merchant_private_key" IS '商户私钥(加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."merchant_serial_number" IS '商家公钥证书序列号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."encrypt_key" IS '接口加密密钥(加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."douyin_direct_key_config"
  ADD CONSTRAINT "pk_douyin_direct_key_config" PRIMARY KEY ("id");


-- =========================
-- 3. 抖音直连商户应用 douyin_direct_app
-- 每个应用关联一个通道商户号, 拥有独立的抖音应用ID
-- =========================
DROP TABLE IF EXISTS "public"."douyin_direct_app";

CREATE TABLE "public"."douyin_direct_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "app_name" varchar(64),
  "douyin_app_id" varchar(64),
  "app_type" varchar(32),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."douyin_direct_app" IS '抖音直连商户应用';
COMMENT ON COLUMN "public"."douyin_direct_app"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_direct_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_app"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."douyin_direct_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."douyin_direct_app"."douyin_app_id" IS '抖音应用AppId(APPID)';
COMMENT ON COLUMN "public"."douyin_direct_app"."app_type" IS '应用类型: mini_program-小程序, mobile_app-移动应用, web_app-网站应用';
COMMENT ON COLUMN "public"."douyin_direct_app"."creator" IS '创建人';
COMMENT ON COLUMN "public"."douyin_direct_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_direct_app"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."douyin_direct_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_direct_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_app"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."douyin_direct_app"
  ADD CONSTRAINT "pk_douyin_direct_app" PRIMARY KEY ("id");


-- =========================
-- 4. 抖音直连商户应用授权认证配置 douyin_direct_app_auth_config
-- =========================
DROP TABLE IF EXISTS "public"."douyin_direct_app_auth_config";

CREATE TABLE "public"."douyin_direct_app_auth_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "douyin_direct_app_id" int8,
  "auth_callback_url" varchar(512),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."douyin_direct_app_auth_config" IS '抖音直连商户应用授权认证配置';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."douyin_direct_app_id" IS '关联应用ID(指向 douyin_direct_app.id)';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."auth_callback_url" IS '授权回调地址';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."douyin_direct_app_auth_config"
  ADD CONSTRAINT "pk_douyin_direct_app_auth_config" PRIMARY KEY ("id");
