-- =====================================================================
-- 支付宝通道相关表 (DROP + CREATE 重建)
-- 对应实体:
--   org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvApp
--   org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvAppKeyConfig
--   org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvAppAuthConfig
--   org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvChannelMerchant
--   org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectApp
--   org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectAppKeyConfig
--   org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectAppAuthConfig
--   org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectChannelMerchant
-- 执行方式: 手动执行(业务表 DDL 不入库, 由开发者按需在数据库执行)
-- 注意: 加密字段(DataEncryptTypeHandler)在数据库层均为普通 text, 加解密在应用层完成
-- =====================================================================

-- =========================
-- 1. 支付宝服务商应用 alipay_isv_app
-- =========================
DROP TABLE IF EXISTS "public"."alipay_isv_app";

CREATE TABLE "public"."alipay_isv_app" (
  "id" int8 NOT NULL,
  "app_name" varchar(64),
  "ali_app_id" varchar(64),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_isv_app" IS '支付宝服务商应用';
COMMENT ON COLUMN "public"."alipay_isv_app"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."alipay_isv_app"."ali_app_id" IS '支付宝应用ID';
COMMENT ON COLUMN "public"."alipay_isv_app"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_app"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_app"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_isv_app"
  ADD CONSTRAINT "pk_alipay_isv_app" PRIMARY KEY ("id");


-- =========================
-- 2. 支付宝服务商应用密钥配置 alipay_isv_app_key_config
-- =========================
DROP TABLE IF EXISTS "public"."alipay_isv_app_key_config";

CREATE TABLE "public"."alipay_isv_app_key_config" (
  "id" int8 NOT NULL,
  "alipay_isv_app_id" int8,
  "auth_type" varchar(32),
  "alipay_public_key" text,
  "private_key" text,
  "app_cert" text,
  "alipay_cert" text,
  "alipay_root_cert" text,
  "secret_key" text,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_isv_app_key_config" IS '支付宝服务商应用密钥配置';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."alipay_isv_app_id" IS '支付宝服务商应用ID(指向 alipay_isv_app.id)';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."auth_type" IS '认证类型';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."alipay_public_key" IS '支付宝公钥';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."private_key" IS '应用私钥(加密存储)';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."app_cert" IS '应用公钥证书(加密存储)';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."alipay_cert" IS '支付宝公钥证书(加密存储)';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."alipay_root_cert" IS '支付宝CA根证书(加密存储)';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."secret_key" IS 'AES通信密钥(加密存储)';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_isv_app_key_config"
  ADD CONSTRAINT "pk_alipay_isv_app_key_config" PRIMARY KEY ("id");


-- =========================
-- 3. 支付宝服务商应用授权配置 alipay_isv_app_auth_config
-- =========================
DROP TABLE IF EXISTS "public"."alipay_isv_app_auth_config";

CREATE TABLE "public"."alipay_isv_app_auth_config" (
  "id" int8 NOT NULL,
  "alipay_isv_app_id" int8,
  "user_id_type" varchar(32),
  "auth_callback_url" varchar(255),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_isv_app_auth_config" IS '支付宝服务商应用授权配置';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."alipay_isv_app_id" IS '支付宝服务商应用ID(指向 alipay_isv_app.id)';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."user_id_type" IS '用户标识类型';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."auth_callback_url" IS '授权回调地址';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_isv_app_auth_config"
  ADD CONSTRAINT "pk_alipay_isv_app_auth_config" PRIMARY KEY ("id");


-- =========================
-- 4. 支付宝服务商通道商户绑定 alipay_isv_channel_merchant
-- 一条记录代表子商户挂靠在某个服务商应用下的授权关系
-- 同一子商户挂不同应用 = 不同行(不同 channel_mch_no)
-- =========================
DROP TABLE IF EXISTS "public"."mch_alipay_isv_channel_merchant";
DROP TABLE IF EXISTS "public"."alipay_isv_channel_merchant";

CREATE TABLE "public"."alipay_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "product" varchar(32) NOT NULL,
  "isv_app_id" int8,
  "alipay_user_id" varchar(64),
  "app_auth_token" varchar(128),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_isv_channel_merchant" IS '支付宝服务商通道商户绑定';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."channel_mch_no" IS '通道商户号(AISV+雪花)';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."isv_app_id" IS '关联服务商应用ID(指向 alipay_isv_app.id)';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."alipay_user_id" IS '子商户支付宝识别码(2088开头的16位数字)';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."app_auth_token" IS '应用授权令牌(服务商代子商户调用接口的凭据, 会过期/刷新)';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_isv_channel_merchant"
  ADD CONSTRAINT "pk_alipay_isv_channel_merchant" PRIMARY KEY ("id");


-- =========================
-- 5. 支付宝直连商户应用 alipay_direct_app
-- 每个应用关联一个通道商户号, 拥有独立的支付宝应用ID
-- =========================
DROP TABLE IF EXISTS "public"."alipay_direct_app";

CREATE TABLE "public"."alipay_direct_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "app_name" varchar(64),
  "ali_app_id" varchar(64),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_direct_app" IS '支付宝直连商户应用';
COMMENT ON COLUMN "public"."alipay_direct_app"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_app"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."alipay_direct_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."alipay_direct_app"."ali_app_id" IS '支付宝应用ID';
COMMENT ON COLUMN "public"."alipay_direct_app"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_direct_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_app"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_direct_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_direct_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_direct_app"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_direct_app"
  ADD CONSTRAINT "pk_alipay_direct_app" PRIMARY KEY ("id");


-- =========================
-- 6. 支付宝直连商户应用密钥配置 alipay_direct_app_key_config
-- 支持公钥模式和证书模式两种认证方式, 敏感字段加密存储
-- =========================
DROP TABLE IF EXISTS "public"."alipay_direct_app_key_config";

CREATE TABLE "public"."alipay_direct_app_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "alipay_direct_app_id" int8,
  "auth_type" varchar(32),
  "alipay_public_key" text,
  "private_key" text,
  "app_cert" text,
  "alipay_cert" text,
  "alipay_root_cert" text,
  "secret_key" text,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_direct_app_key_config" IS '支付宝直连商户应用密钥配置';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."alipay_direct_app_id" IS '关联应用ID(指向 alipay_direct_app.id)';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."auth_type" IS '认证类型';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."alipay_public_key" IS '支付宝公钥';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."private_key" IS '应用私钥(加密存储)';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."app_cert" IS '应用公钥证书(加密存储)';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."alipay_cert" IS '支付宝公钥证书(加密存储)';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."alipay_root_cert" IS '支付宝CA根证书(加密存储)';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."secret_key" IS 'AES通信密钥(加密存储)';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_direct_app_key_config"
  ADD CONSTRAINT "pk_alipay_direct_app_key_config" PRIMARY KEY ("id");


-- =========================
-- 7. 支付宝直连商户应用授权配置 alipay_direct_app_auth_config
-- =========================
DROP TABLE IF EXISTS "public"."alipay_direct_app_auth_config";

CREATE TABLE "public"."alipay_direct_app_auth_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "alipay_direct_app_id" int8,
  "user_id_type" varchar(32),
  "auth_callback_url" varchar(255),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_direct_app_auth_config" IS '支付宝直连商户应用授权配置';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."alipay_direct_app_id" IS '关联应用ID(指向 alipay_direct_app.id)';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."user_id_type" IS '用户标识类型';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."auth_callback_url" IS '授权回调地址';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_direct_app_auth_config"
  ADD CONSTRAINT "pk_alipay_direct_app_auth_config" PRIMARY KEY ("id");


-- =========================
-- 8. 支付宝直连通道商户绑定 alipay_direct_channel_merchant
-- 一个商户PID对应一个channelMchNo, 商户的多个应用共享此绑定
-- =========================
DROP TABLE IF EXISTS "public"."mch_alipay_direct_channel_merchant";
DROP TABLE IF EXISTS "public"."alipay_direct_channel_merchant";

CREATE TABLE "public"."alipay_direct_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) NOT NULL,
  "channel_mch_no" varchar(64) NOT NULL,
  "product" varchar(32) NOT NULL,
  "alipay_user_id" varchar(64),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0 NOT NULL,
  "deleted" bool DEFAULT false NOT NULL
);

COMMENT ON TABLE  "public"."alipay_direct_channel_merchant" IS '支付宝直连通道商户绑定';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."channel_mch_no" IS '通道商户号(系统生成雪花号)';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."alipay_user_id" IS '支付宝商家唯一识别码(2088开头的16位数字)';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."deleted" IS '逻辑删除标志';

ALTER TABLE "public"."alipay_direct_channel_merchant"
  ADD CONSTRAINT "pk_alipay_direct_channel_merchant" PRIMARY KEY ("id");
