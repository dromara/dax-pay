-- =====================================================================
-- 微信通道相关表 (DROP + CREATE 重建)
-- 对应实体:
--   org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvApp
--   org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvAppAuthConfig
--   org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvKeyConfig
--   org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvChannelMerchant
--   org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectApp
--   org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectKeyConfig
--   org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectAppAuthConfig
--   org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectChannelMerchant
-- 执行方式: 手动执行(业务表 DDL 不入库, 由开发者按需在数据库执行)
-- 注意: 加密字段(DataEncryptTypeHandler)在数据库层均为普通 text, 加解密在应用层完成
-- =====================================================================

-- =========================
-- 1. 微信服务商应用 wechat_isv_app
-- =========================
DROP TABLE IF EXISTS "public"."wechat_isv_app";

CREATE TABLE "public"."wechat_isv_app" (
  "id" int8 NOT NULL,
  "app_name" varchar(64),
  "app_type" varchar(32),
  "wx_app_id" varchar(64),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."wechat_isv_app" IS '微信服务商应用';
COMMENT ON COLUMN "public"."wechat_isv_app"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."wechat_isv_app"."app_type" IS '应用类型';
COMMENT ON COLUMN "public"."wechat_isv_app"."wx_app_id" IS '微信应用AppId';
COMMENT ON COLUMN "public"."wechat_isv_app"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_isv_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_isv_app"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_isv_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_isv_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_app"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."wechat_isv_app"
  ADD CONSTRAINT "pk_wechat_isv_app" PRIMARY KEY ("id");


-- =========================
-- 2. 微信服务商应用授权配置 wechat_isv_app_auth_config
-- =========================
DROP TABLE IF EXISTS "public"."wechat_isv_app_auth_config";

CREATE TABLE "public"."wechat_isv_app_auth_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32),
  "channel_mch_no" varchar(64),
  "wechat_isv_app_id" int8,
  "app_secret" text,
  "auth_callback_url" varchar(255),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."wechat_isv_app_auth_config" IS '微信服务商应用授权配置';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."wechat_isv_app_id" IS '关联服务商应用ID(指向 wechat_isv_app.id)';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."auth_callback_url" IS '授权回调地址（仅公众号）';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_app_auth_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."wechat_isv_app_auth_config"
  ADD CONSTRAINT "pk_wechat_isv_app_auth_config" PRIMARY KEY ("id");


-- =========================
-- 3. 微信服务商密钥配置 wechat_isv_key_config
-- =========================
DROP TABLE IF EXISTS "public"."wechat_isv_key_config";

CREATE TABLE "public"."wechat_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(32),
  "wx_mch_id" varchar(32),
  "api_key_v3" text,
  "public_key" text,
  "public_key_id" varchar(128),
  "private_key" text,
  "private_cert" text,
  "cert_serial_no" varchar(128),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."wechat_isv_key_config" IS '微信服务商密钥配置';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."product" IS '产品编码';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."wx_mch_id" IS '微信服务商商户号';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."api_key_v3" IS 'APIv3密钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."public_key" IS '支付公钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."public_key_id" IS '支付公钥ID';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."private_key" IS '商户私钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."private_cert" IS '商户证书(加密存储)';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."cert_serial_no" IS '证书序列号';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_key_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."wechat_isv_key_config"
  ADD CONSTRAINT "pk_wechat_isv_key_config" PRIMARY KEY ("id");


-- =========================
-- 4. 微信服务商通道商户绑定 mch_wechat_isv_channel_merchant
-- 微信特约商户关联到服务商本身, 不挂靠具体服务商应用
-- =========================
DROP TABLE IF EXISTS "public"."mch_wechat_isv_channel_merchant";

CREATE TABLE "public"."mch_wechat_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "product" varchar(32) NOT NULL,
  "sub_mch_id" varchar(32),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."mch_wechat_isv_channel_merchant" IS '微信服务商通道商户绑定';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."channel_mch_no" IS '通道商户号(WISV+雪花)';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."sub_mch_id" IS '微信特约商户号/二级商户号(V3服务商支付 sub_mchid)';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."mch_wechat_isv_channel_merchant"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."mch_wechat_isv_channel_merchant"
  ADD CONSTRAINT "pk_mch_wechat_isv_channel_merchant" PRIMARY KEY ("id");


-- =========================
-- 5. 微信直连通道商户绑定 mch_wechat_direct_channel_merchant
-- 一个微信商户号(wx_mch_id)对应一个channelMchNo, 商户的多个应用共享此绑定
-- =========================
DROP TABLE IF EXISTS "public"."mch_wechat_direct_channel_merchant";

CREATE TABLE "public"."mch_wechat_direct_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "product" varchar(32) NOT NULL,
  "wx_mch_id" varchar(32),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."mch_wechat_direct_channel_merchant" IS '微信直连通道商户绑定';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."channel_mch_no" IS '通道商户号(系统生成雪花号)';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."wx_mch_id" IS '微信直连商户号';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."mch_wechat_direct_channel_merchant"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."mch_wechat_direct_channel_merchant"
  ADD CONSTRAINT "pk_mch_wechat_direct_channel_merchant" PRIMARY KEY ("id");


-- =========================
-- 6. 微信直连商户应用 wechat_direct_app
-- 每个应用关联一个通道商户号, 拥有独立的微信应用ID
-- =========================
DROP TABLE IF EXISTS "public"."wechat_direct_app";

CREATE TABLE "public"."wechat_direct_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "app_name" varchar(64),
  "wx_app_id" varchar(64),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."wechat_direct_app" IS '微信直连商户应用';
COMMENT ON COLUMN "public"."wechat_direct_app"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_direct_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_direct_app"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."wechat_direct_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."wechat_direct_app"."wx_app_id" IS '微信应用AppId';
COMMENT ON COLUMN "public"."wechat_direct_app"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_direct_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_direct_app"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_direct_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_direct_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_direct_app"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."wechat_direct_app"
  ADD CONSTRAINT "pk_wechat_direct_app" PRIMARY KEY ("id");


-- =========================
-- 7. 微信直连密钥配置 wechat_direct_key_config
-- 直连商户维度的APIv3密钥和证书, 按通道商户号关联, 与具体应用无关, 敏感字段加密存储
-- =========================
DROP TABLE IF EXISTS "public"."wechat_direct_key_config";

CREATE TABLE "public"."wechat_direct_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "api_key_v3" text,
  "public_key" text,
  "public_key_id" varchar(128),
  "private_key" text,
  "private_cert" text,
  "cert_serial_no" varchar(128),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."wechat_direct_key_config" IS '微信直连密钥配置';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."api_key_v3" IS 'APIv3密钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."public_key" IS '支付公钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."public_key_id" IS '支付公钥ID';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."private_key" IS '商户私钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."private_cert" IS '商户证书(加密存储)';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."cert_serial_no" IS '证书序列号';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_direct_key_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."wechat_direct_key_config"
  ADD CONSTRAINT "pk_wechat_direct_key_config" PRIMARY KEY ("id");


-- =========================
-- 8. 微信直连商户应用授权配置 wechat_direct_app_auth_config
-- =========================
DROP TABLE IF EXISTS "public"."wechat_direct_app_auth_config";

CREATE TABLE "public"."wechat_direct_app_auth_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "wechat_direct_app_id" int8,
  "app_secret" text,
  "auth_callback_url" varchar(255),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."wechat_direct_app_auth_config" IS '微信直连商户应用授权配置';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."wechat_direct_app_id" IS '关联应用ID(指向 wechat_direct_app.id)';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."auth_callback_url" IS '授权回调地址';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_direct_app_auth_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."wechat_direct_app_auth_config"
  ADD CONSTRAINT "pk_wechat_direct_app_auth_config" PRIMARY KEY ("id");
