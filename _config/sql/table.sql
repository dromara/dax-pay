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

 Date: 05/08/2026 13:48:33
*/


-- ----------------------------
-- Sequence structure for adapay_direct_key_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."adapay_direct_key_config_id_seq";
CREATE SEQUENCE "public"."adapay_direct_key_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for alipay_direct_app_capability_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."alipay_direct_app_capability_id_seq";
CREATE SEQUENCE "public"."alipay_direct_app_capability_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for hmpay_isv_channel_merchant_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."hmpay_isv_channel_merchant_id_seq";
CREATE SEQUENCE "public"."hmpay_isv_channel_merchant_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for hmpay_isv_key_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."hmpay_isv_key_config_id_seq";
CREATE SEQUENCE "public"."hmpay_isv_key_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for mch_app_notify_config_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."mch_app_notify_config_id_seq";
CREATE SEQUENCE "public"."mch_app_notify_config_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for mch_user_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."mch_user_id_seq";
CREATE SEQUENCE "public"."mch_user_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for pay_close_record_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."pay_close_record_id_seq";
CREATE SEQUENCE "public"."pay_close_record_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Sequence structure for pay_sync_record_id_seq
-- ----------------------------
DROP SEQUENCE IF EXISTS "public"."pay_sync_record_id_seq";
CREATE SEQUENCE "public"."pay_sync_record_id_seq" 
INCREMENT 1
MINVALUE  1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

-- ----------------------------
-- Table structure for adapay_direct_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."adapay_direct_key_config";
CREATE TABLE "public"."adapay_direct_key_config" (
  "id" int8 NOT NULL DEFAULT nextval('adapay_direct_key_config_id_seq'::regclass),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "adapay_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "api_key" text COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."adapay_direct_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."channel_mch_no" IS '通道商户号(创建时录入不可修改)';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."adapay_app_id" IS 'Adapay 应用ID(app_id)';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."api_key" IS 'Adapay API Key(请求头Authorization, 加密存储)';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."private_key" IS '商户RSA私钥(PKCS#8 Base64, 请求签名, 加密存储)';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."public_key" IS 'Adapay 平台公钥(X509 Base64, 响应验签, 加密存储; 为空使用全局默认)';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."adapay_direct_key_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."adapay_direct_key_config" IS 'Adapay 直连密钥配置';

-- ----------------------------
-- Table structure for alipay_direct_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_direct_app";
CREATE TABLE "public"."alipay_direct_app" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(64) COLLATE "pg_catalog"."default",
  "ali_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "app_type" varchar(32) COLLATE "pg_catalog"."default"
)
;
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
COMMENT ON TABLE "public"."alipay_direct_app" IS '支付宝直连商户应用';

-- ----------------------------
-- Table structure for alipay_direct_app_auth_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_direct_app_auth_config";
CREATE TABLE "public"."alipay_direct_app_auth_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "alipay_direct_app_id" int8,
  "user_id_type" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."alipay_direct_app_id" IS '关联应用 ID';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."user_id_type" IS '用户标识类型';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_direct_app_auth_config"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."alipay_direct_app_auth_config" IS '支付宝直连商户应用授权配置';

-- ----------------------------
-- Table structure for alipay_direct_app_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_direct_app_capability";
CREATE TABLE "public"."alipay_direct_app_capability" (
  "id" int8 NOT NULL DEFAULT nextval('alipay_direct_app_capability_id_seq'::regclass),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "capability" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "alipay_direct_app_id" int8 NOT NULL,
  "create_time" timestamptz(6) DEFAULT now(),
  "update_time" timestamptz(6) DEFAULT now(),
  "deleted" bool DEFAULT false,
  "creator" int8,
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0
)
;
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."alipay_direct_app_id" IS '关联支付宝直连应用ID';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."alipay_direct_app_capability" IS '支付宝直连商户应用支付能力关联';

-- ----------------------------
-- Table structure for alipay_direct_app_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_direct_app_key_config";
CREATE TABLE "public"."alipay_direct_app_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "alipay_direct_app_id" int8,
  "auth_type" varchar(32) COLLATE "pg_catalog"."default",
  "alipay_public_key" text COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "app_cert" text COLLATE "pg_catalog"."default",
  "alipay_cert" text COLLATE "pg_catalog"."default",
  "alipay_root_cert" text COLLATE "pg_catalog"."default",
  "secret_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."alipay_direct_app_id" IS '关联应用 ID';
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
COMMENT ON COLUMN "public"."alipay_direct_app_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON TABLE "public"."alipay_direct_app_key_config" IS '支付宝直连商户应用密钥配置';

-- ----------------------------
-- Table structure for alipay_direct_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_direct_channel_merchant";
CREATE TABLE "public"."alipay_direct_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "alipay_user_id" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
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
COMMENT ON COLUMN "public"."alipay_direct_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON TABLE "public"."alipay_direct_channel_merchant" IS '支付宝直连通道商户绑定';

-- ----------------------------
-- Table structure for alipay_isv_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_isv_app";
CREATE TABLE "public"."alipay_isv_app" (
  "id" int8 NOT NULL,
  "app_name" varchar(64) COLLATE "pg_catalog"."default",
  "ali_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_isv_app"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."alipay_isv_app"."ali_app_id" IS '支付宝应用ID';
COMMENT ON COLUMN "public"."alipay_isv_app"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_app"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_app"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."alipay_isv_app" IS '支付宝服务商应用';

-- ----------------------------
-- Table structure for alipay_isv_app_auth_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_isv_app_auth_config";
CREATE TABLE "public"."alipay_isv_app_auth_config" (
  "id" int8 NOT NULL,
  "alipay_isv_app_id" int8,
  "user_id_type" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."alipay_isv_app_id" IS '支付宝服务商应用 ID';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."user_id_type" IS '用户标识类型';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_app_auth_config"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."alipay_isv_app_auth_config" IS '支付宝服务商应用授权配置';

-- ----------------------------
-- Table structure for alipay_isv_app_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_isv_app_key_config";
CREATE TABLE "public"."alipay_isv_app_key_config" (
  "id" int8 NOT NULL,
  "alipay_isv_app_id" int8,
  "auth_type" varchar(32) COLLATE "pg_catalog"."default",
  "alipay_public_key" text COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "app_cert" text COLLATE "pg_catalog"."default",
  "alipay_cert" text COLLATE "pg_catalog"."default",
  "alipay_root_cert" text COLLATE "pg_catalog"."default",
  "secret_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_app_key_config"."alipay_isv_app_id" IS '支付宝服务商应用 ID';
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
COMMENT ON TABLE "public"."alipay_isv_app_key_config" IS '支付宝服务商应用密钥配置';

-- ----------------------------
-- Table structure for alipay_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_isv_channel_merchant";
CREATE TABLE "public"."alipay_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "isv_app_id" int8,
  "alipay_user_id" varchar(64) COLLATE "pg_catalog"."default",
  "app_auth_token" varchar(128) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."channel_mch_no" IS '通道商户号(AISV+雪花)';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."isv_app_id" IS '关联服务商应用 ID';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."alipay_user_id" IS '子商户支付宝识别码(2088开头的16位数字)';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."app_auth_token" IS '应用授权令牌(服务商代子商户调用接口的凭据, 会过期/刷新)';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_channel_merchant"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."alipay_isv_channel_merchant" IS '支付宝服务商通道商户绑定';

-- ----------------------------
-- Table structure for base_area
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_area";
CREATE TABLE "public"."base_area" (
  "code" varchar(6) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
  "city_code" varchar(4) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_area"."name" IS '区域名称';
COMMENT ON COLUMN "public"."base_area"."city_code" IS '城市编码';
COMMENT ON TABLE "public"."base_area" IS '县区表';

-- ----------------------------
-- Table structure for base_city
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_city";
CREATE TABLE "public"."base_city" (
  "code" varchar(4) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
  "province_code" varchar(2) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_city"."code" IS '城市编码';
COMMENT ON COLUMN "public"."base_city"."name" IS '城市名称';
COMMENT ON COLUMN "public"."base_city"."province_code" IS '省份编码';
COMMENT ON TABLE "public"."base_city" IS '城市表';

-- ----------------------------
-- Table structure for base_province
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_province";
CREATE TABLE "public"."base_province" (
  "code" varchar(2) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_province"."code" IS '省份编码';
COMMENT ON COLUMN "public"."base_province"."name" IS '省份名称';
COMMENT ON TABLE "public"."base_province" IS '省份表';

-- ----------------------------
-- Table structure for base_street
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_street";
CREATE TABLE "public"."base_street" (
  "code" varchar(9) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
  "area_code" varchar(6) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_street"."code" IS '编码';
COMMENT ON COLUMN "public"."base_street"."name" IS '街道名称';
COMMENT ON COLUMN "public"."base_street"."area_code" IS '县区编码';
COMMENT ON TABLE "public"."base_street" IS '街道表';

-- ----------------------------
-- Table structure for base_user_protocol
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_user_protocol";
CREATE TABLE "public"."base_user_protocol" (
  "id" int8 NOT NULL,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "show_name" varchar(100) COLLATE "pg_catalog"."default",
  "type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "client_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "default_protocol" bool NOT NULL DEFAULT false,
  "default_language" varchar(10) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'zh-CN'::character varying,
  "creator" int8,
  "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "last_modifier" int8,
  "last_modified_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."base_user_protocol"."id" IS '主键';
COMMENT ON COLUMN "public"."base_user_protocol"."name" IS '名称';
COMMENT ON COLUMN "public"."base_user_protocol"."show_name" IS '显示名称';
COMMENT ON COLUMN "public"."base_user_protocol"."type" IS '协议类型(USER_AGREEMENT/PRIVACY_POLICY/THIRD_PARTY_INFO/CHILDREN_POLICY)';
COMMENT ON COLUMN "public"."base_user_protocol"."client_type" IS '端类型(WEB/APP/MINIAPP)';
COMMENT ON COLUMN "public"."base_user_protocol"."default_protocol" IS '是否默认协议(同类型同端唯一)';
COMMENT ON COLUMN "public"."base_user_protocol"."default_language" IS '默认语言(对外拉取时回退使用)';
COMMENT ON COLUMN "public"."base_user_protocol"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."base_user_protocol"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."base_user_protocol"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."base_user_protocol"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."base_user_protocol"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."base_user_protocol"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."base_user_protocol" IS '用户协议';

-- ----------------------------
-- Table structure for base_user_protocol_version
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_user_protocol_version";
CREATE TABLE "public"."base_user_protocol_version" (
  "id" int8 NOT NULL,
  "protocol_id" int8 NOT NULL,
  "language" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "version_no" int4,
  "version_label" varchar(32) COLLATE "pg_catalog"."default",
  "title" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "content_html" text COLLATE "pg_catalog"."default",
  "content_format" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'MARKDOWN'::character varying,
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'DRAFT'::character varying,
  "effective_time" timestamptz(6),
  "summary" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "last_modifier" int8,
  "last_modified_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."base_user_protocol_version"."id" IS '主键';
COMMENT ON COLUMN "public"."base_user_protocol_version"."protocol_id" IS '协议ID';
COMMENT ON COLUMN "public"."base_user_protocol_version"."language" IS '语言';
COMMENT ON COLUMN "public"."base_user_protocol_version"."version_no" IS '版本号(同协议同语言自增)';
COMMENT ON COLUMN "public"."base_user_protocol_version"."version_label" IS '版本标签, 如 v1.0.0';
COMMENT ON COLUMN "public"."base_user_protocol_version"."title" IS '标题';
COMMENT ON COLUMN "public"."base_user_protocol_version"."content" IS '协议内容(Markdown)';
COMMENT ON COLUMN "public"."base_user_protocol_version"."content_html" IS '渲染后的HTML';
COMMENT ON COLUMN "public"."base_user_protocol_version"."content_format" IS '内容格式';
COMMENT ON COLUMN "public"."base_user_protocol_version"."status" IS '状态(DRAFT草稿/PUBLISHED已发布/ARCHIVED已归档)';
COMMENT ON COLUMN "public"."base_user_protocol_version"."effective_time" IS '生效时间';
COMMENT ON COLUMN "public"."base_user_protocol_version"."summary" IS '变更说明';
COMMENT ON COLUMN "public"."base_user_protocol_version"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."base_user_protocol_version"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."base_user_protocol_version"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."base_user_protocol_version"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."base_user_protocol_version"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."base_user_protocol_version"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."base_user_protocol_version" IS '用户协议版本';

-- ----------------------------
-- Table structure for device_qr_code
-- ----------------------------
DROP TABLE IF EXISTS "public"."device_qr_code";
CREATE TABLE "public"."device_qr_code" (
  "id" int8 NOT NULL,
  "code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(100) COLLATE "pg_catalog"."default",
  "batch_no" varchar(64) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(64) COLLATE "pg_catalog"."default",
  "program_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'h5'::character varying,
  "amount_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "fixed_amount" int8,
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "store_no" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."device_qr_code"."id" IS '主键';
COMMENT ON COLUMN "public"."device_qr_code"."code" IS '码牌编码(唯一, 二维码参数)';
COMMENT ON COLUMN "public"."device_qr_code"."name" IS '码牌名称';
COMMENT ON COLUMN "public"."device_qr_code"."batch_no" IS '批次号';
COMMENT ON COLUMN "public"."device_qr_code"."mch_no" IS '所属商户号(空=空白库存)';
COMMENT ON COLUMN "public"."device_qr_code"."app_id" IS '关联应用号(空=商户默认应用)';
COMMENT ON COLUMN "public"."device_qr_code"."program_type" IS '落地程序类型 h5/小程序(mini_app)';
COMMENT ON COLUMN "public"."device_qr_code"."amount_type" IS '金额类型 random/fixed';
COMMENT ON COLUMN "public"."device_qr_code"."fixed_amount" IS '固定金额(分)';
COMMENT ON COLUMN "public"."device_qr_code"."status" IS '状态 enabled/disabled';
COMMENT ON COLUMN "public"."device_qr_code"."remark" IS '备注';
COMMENT ON COLUMN "public"."device_qr_code"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."device_qr_code"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."device_qr_code"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."device_qr_code"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."device_qr_code"."version" IS '版本号';
COMMENT ON COLUMN "public"."device_qr_code"."deleted" IS '逻辑删除标识';
COMMENT ON COLUMN "public"."device_qr_code"."store_no" IS '绑定门店号(可空; 对应 mch_store_info.store_no)';
COMMENT ON TABLE "public"."device_qr_code" IS '支付码牌';

-- ----------------------------
-- Table structure for douyin_direct_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."douyin_direct_channel_merchant";
CREATE TABLE "public"."douyin_direct_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "dy_mch_id" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "transfer_scene" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."channel_mch_no" IS '通道商户号(系统生成雪花号)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."product" IS '所属支付产品(如 douyin_pay)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."dy_mch_id" IS '抖音商户号(MCHID)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."deleted" IS '逻辑删除标志';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."transfer_scene" IS '转账场景ID(商家转账, 未配置时发起转账报错)';
COMMENT ON TABLE "public"."douyin_direct_channel_merchant" IS '抖音直连通道商户绑定';

-- ----------------------------
-- Table structure for douyin_direct_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."douyin_direct_key_config";
CREATE TABLE "public"."douyin_direct_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "merchant_private_key" text COLLATE "pg_catalog"."default",
  "merchant_serial_number" varchar(64) COLLATE "pg_catalog"."default",
  "encrypt_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."douyin_direct_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."channel_mch_no" IS '通道商户号(唯一关联)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."merchant_private_key" IS '商户私钥(MERCHANT_PRIVATE_KEY, 加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."merchant_serial_number" IS '商家公钥证书序列号(MERCHANT_SERIAL_NO)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."encrypt_key" IS '接口加密密钥(ENCRYPT_KEY, 加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."douyin_direct_key_config" IS '抖音直连密钥配置';

-- ----------------------------
-- Table structure for dy_channel_app_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."dy_channel_app_capability";
CREATE TABLE "public"."dy_channel_app_capability" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "capability" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_scope" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "dy_app_ref_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."dy_channel_app_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."version" IS '版本号';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."app_scope" IS '应用档位：platform/merchant';
COMMENT ON COLUMN "public"."dy_channel_app_capability"."dy_app_ref_id" IS '抖音应用主数据主键（由 app_scope 决定指向平台或商户表）';
COMMENT ON TABLE "public"."dy_channel_app_capability" IS '通道商户抖音应用能力绑定（同能力可按档位双绑 platform+merchant）';

-- ----------------------------
-- Table structure for dy_mch_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."dy_mch_app";
CREATE TABLE "public"."dy_mch_app" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "douyin_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_secret" varchar(512) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."dy_mch_app"."id" IS '主键';
COMMENT ON COLUMN "public"."dy_mch_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."dy_mch_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dy_mch_app"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."dy_mch_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."dy_mch_app"."version" IS '版本号';
COMMENT ON COLUMN "public"."dy_mch_app"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."dy_mch_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."dy_mch_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."dy_mch_app"."app_type" IS '应用类型：mini_program/mobile_app/web_app';
COMMENT ON COLUMN "public"."dy_mch_app"."douyin_app_id" IS '抖音应用AppId';
COMMENT ON COLUMN "public"."dy_mch_app"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON TABLE "public"."dy_mch_app" IS '商户抖音应用（商户域开放平台身份，跨通道可引用）';

-- ----------------------------
-- Table structure for dy_platform_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."dy_platform_app";
CREATE TABLE "public"."dy_platform_app" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "app_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "douyin_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_secret" varchar(512) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."dy_platform_app"."id" IS '主键';
COMMENT ON COLUMN "public"."dy_platform_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."dy_platform_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dy_platform_app"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."dy_platform_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."dy_platform_app"."version" IS '版本号';
COMMENT ON COLUMN "public"."dy_platform_app"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."dy_platform_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."dy_platform_app"."app_type" IS '应用类型：mini_program/mobile_app/web_app';
COMMENT ON COLUMN "public"."dy_platform_app"."douyin_app_id" IS '抖音应用AppId';
COMMENT ON COLUMN "public"."dy_platform_app"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON TABLE "public"."dy_platform_app" IS '平台抖音应用（开放平台身份，跨通道可引用）';

-- ----------------------------
-- Table structure for dy_platform_app_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."dy_platform_app_capability";
CREATE TABLE "public"."dy_platform_app_capability" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "capability" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "dy_platform_app_id" int8 NOT NULL,
  "product" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."dy_platform_app_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."version" IS '版本号';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."dy_platform_app_id" IS '平台抖音应用ID';
COMMENT ON COLUMN "public"."dy_platform_app_capability"."product" IS '产品编码';
COMMENT ON TABLE "public"."dy_platform_app_capability" IS '平台抖音应用默认能力绑定（全局一能力一应用）';

-- ----------------------------
-- Table structure for fuyou_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."fuyou_isv_channel_merchant";
CREATE TABLE "public"."fuyou_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "fuyou_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "term_no" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."channel_mch_no" IS '通道商户号(FUYOU+雪花)';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."product" IS '所属支付产品(对应 ProductEnum.code)';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."fuyou_mch_no" IS '富友商户号(mchnt_cd)';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."term_no" IS '终端号(term_id)';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."fuyou_isv_channel_merchant"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."fuyou_isv_channel_merchant" IS '富友通道商户绑定';

-- ----------------------------
-- Table structure for fuyou_isv_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."fuyou_isv_key_config";
CREATE TABLE "public"."fuyou_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "fy_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "order_prefix" varchar(64) COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."product" IS '产品编码(对应 ProductEnum.code, 如 fuyou_pay)';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."fy_app_id" IS '富友应用编号(机构号 ins_cd)';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."order_prefix" IS '富友订单前缀(关联订单号前缀, 富友回调凭 mchnt_order_no 反查平台订单)';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."private_key" IS '商户RSA私钥(PKCS8 Base64, MD5withRSA 签名, 加密存储)';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."public_key" IS '富友RSA公钥(X509 Base64, 响应/回调验签, 加密存储)';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."fuyou_isv_key_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."fuyou_isv_key_config" IS '富友服务商密钥配置';

-- ----------------------------
-- Table structure for hkrt_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."hkrt_isv_channel_merchant";
CREATE TABLE "public"."hkrt_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "merch_no" varchar(64) COLLATE "pg_catalog"."default",
  "pn" varchar(64) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6),
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."mch_no" IS '平台商户号';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."channel_mch_no" IS '通道商户号(平台生成的唯一标识, HKRT+雪花)';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."product" IS '所属支付产品(对应 ProductEnum.code, 如 hkrt_pay)';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."merch_no" IS '海科商户号(merch_no)';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."pn" IS 'SAAS 终端号(pn)';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."creator" IS '创建者';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."hkrt_isv_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON TABLE "public"."hkrt_isv_channel_merchant" IS '海科融通通道商户绑定';

-- ----------------------------
-- Table structure for hkrt_isv_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."hkrt_isv_key_config";
CREATE TABLE "public"."hkrt_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "agent_no" varchar(64) COLLATE "pg_catalog"."default",
  "access_id" varchar(128) COLLATE "pg_catalog"."default",
  "access_key" text COLLATE "pg_catalog"."default",
  "wx_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6),
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."product" IS '产品编码(对应 ProductEnum.code, 如 hkrt_pay)';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."agent_no" IS '服务商编号(agent_no)';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."access_id" IS '接入机构标识(access_id)';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."access_key" IS '签名密钥(access_key, MD5 大写签名, 加密存储)';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."wx_app_id" IS '微信公众号 AppId(JSAPI 场景透传, 可选)';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."creator" IS '创建者';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."hkrt_isv_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON TABLE "public"."hkrt_isv_key_config" IS '海科融通服务商密钥配置';

-- ----------------------------
-- Table structure for hmpay_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."hmpay_isv_channel_merchant";
CREATE TABLE "public"."hmpay_isv_channel_merchant" (
  "id" int8 NOT NULL DEFAULT nextval('hmpay_isv_channel_merchant_id_seq'::regclass),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "merchant_no" varchar(64) COLLATE "pg_catalog"."default",
  "store_id" varchar(64) COLLATE "pg_catalog"."default",
  "wx_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "wx_channel_auth" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."channel_mch_no" IS '通道商户号(HMPAY+雪花)';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."product" IS '所属支付产品(对应 ProductEnum.code, 如 hm_pay)';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."merchant_no" IS '杉德商户编号(merchantNo / sub_app_id)';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."store_id" IS '门店号(storeId)';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."wx_app_id" IS '微信应用ID(公众号/小程序 appId, 用于微信 JSAPI/小程序支付)';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."wx_channel_auth" IS '是否使用通道渠道认证(微信服务商授权模式)';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON TABLE "public"."hmpay_isv_channel_merchant" IS '河马付通道商户绑定';

-- ----------------------------
-- Table structure for hmpay_isv_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."hmpay_isv_key_config";
CREATE TABLE "public"."hmpay_isv_key_config" (
  "id" int8 NOT NULL DEFAULT nextval('hmpay_isv_key_config_id_seq'::regclass),
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "sand_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."product" IS '产品编码(对应 ProductEnum.code, 如 hm_pay)';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."sand_app_id" IS '杉德代理号(sandAppId / app_id)';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."private_key" IS '商户RSA私钥(PKCS#8 Base64, 加密存储, 签名用)';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."public_key" IS '杉德RSA公钥(X509 Base64, 加密存储, 回调/响应验签用)';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON TABLE "public"."hmpay_isv_key_config" IS '河马付服务商密钥配置';

-- ----------------------------
-- Table structure for iam_perm_code
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_code";
CREATE TABLE "public"."iam_perm_code" (
  "id" int8 NOT NULL,
  "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_code" varchar(100) COLLATE "pg_catalog"."default",
  "internal" bool DEFAULT false,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "last_modifier" int8,
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "create_time" timestamptz(6),
  "last_modified_time" timestamptz(6),
  "i18n_key" varchar(200) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_perm_code"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_code"."code" IS '权限码编码';
COMMENT ON COLUMN "public"."iam_perm_code"."menu_code" IS '菜单编码';
COMMENT ON COLUMN "public"."iam_perm_code"."internal" IS '是否系统内置';
COMMENT ON COLUMN "public"."iam_perm_code"."remark" IS '备注';
COMMENT ON COLUMN "public"."iam_perm_code"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_code"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_perm_code"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_perm_code"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_perm_code"."i18n_key" IS '国际化key（由 code 推导: perm.{code}）';
COMMENT ON TABLE "public"."iam_perm_code" IS '权限码';

-- ----------------------------
-- Table structure for iam_perm_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_perm_menu";
CREATE TABLE "public"."iam_perm_menu" (
  "id" int8 NOT NULL,
  "pid" int8,
  "menu_code" varchar(100) COLLATE "pg_catalog"."default",
  "client_code" varchar(100) COLLATE "pg_catalog"."default",
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "i18n_key" varchar(200) COLLATE "pg_catalog"."default",
  "icon" varchar(200) COLLATE "pg_catalog"."default",
  "hidden" bool DEFAULT false,
  "hide_children_menu" bool DEFAULT false,
  "component" varchar(500) COLLATE "pg_catalog"."default",
  "path" varchar(500) COLLATE "pg_catalog"."default",
  "redirect" varchar(500) COLLATE "pg_catalog"."default",
  "sort_no" float8,
  "root" bool DEFAULT false,
  "keep_alive" bool DEFAULT false,
  "affix_tab" bool DEFAULT false,
  "creator" int8,
  "last_modifier" int8,
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "menu_type" varchar(20) COLLATE "pg_catalog"."default",
  "active_icon" varchar(100) COLLATE "pg_catalog"."default",
  "badge" varchar(50) COLLATE "pg_catalog"."default",
  "badge_type" varchar(20) COLLATE "pg_catalog"."default",
  "badge_variants" varchar(50) COLLATE "pg_catalog"."default",
  "iframe_src" varchar(500) COLLATE "pg_catalog"."default",
  "link" varchar(500) COLLATE "pg_catalog"."default",
  "create_time" timestamptz(6),
  "last_modified_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."iam_perm_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_perm_menu"."pid" IS '父菜单ID,0表示根菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_code" IS '菜单编码';
COMMENT ON COLUMN "public"."iam_perm_menu"."client_code" IS '关联终端code';
COMMENT ON COLUMN "public"."iam_perm_menu"."name" IS '路由名称，建议唯一';
COMMENT ON COLUMN "public"."iam_perm_menu"."i18n_key" IS '国际化key';
COMMENT ON COLUMN "public"."iam_perm_menu"."icon" IS '菜单图标';
COMMENT ON COLUMN "public"."iam_perm_menu"."hidden" IS '是否隐藏';
COMMENT ON COLUMN "public"."iam_perm_menu"."hide_children_menu" IS '是否隐藏子菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."component" IS '组件';
COMMENT ON COLUMN "public"."iam_perm_menu"."path" IS '访问路径';
COMMENT ON COLUMN "public"."iam_perm_menu"."redirect" IS '菜单跳转地址(重定向)';
COMMENT ON COLUMN "public"."iam_perm_menu"."sort_no" IS '菜单排序';
COMMENT ON COLUMN "public"."iam_perm_menu"."root" IS '是否是一级菜单';
COMMENT ON COLUMN "public"."iam_perm_menu"."keep_alive" IS '是否开启页面缓存';
COMMENT ON COLUMN "public"."iam_perm_menu"."affix_tab" IS '是否固定标签页';
COMMENT ON COLUMN "public"."iam_perm_menu"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_perm_menu"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_perm_menu"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_type" IS '菜单类型: catalog-目录, menu-菜单, subpage-子页面, subpage_group-子页面分组, embedded-内嵌, link-外链';
COMMENT ON COLUMN "public"."iam_perm_menu"."active_icon" IS '激活状态图标';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge" IS '徽章显示文本';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge_type" IS '徽章类型: dot-圆点, normal-文本';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge_variants" IS '徽章样式变体';
COMMENT ON COLUMN "public"."iam_perm_menu"."iframe_src" IS '内嵌页面URL地址';
COMMENT ON COLUMN "public"."iam_perm_menu"."link" IS '外部链接URL地址';
COMMENT ON TABLE "public"."iam_perm_menu" IS '菜单权限配置';

-- ----------------------------
-- Table structure for iam_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role";
CREATE TABLE "public"."iam_role" (
  "id" int8 NOT NULL,
  "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "client_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "data_scope" varchar(50) COLLATE "pg_catalog"."default",
  "internal" bool DEFAULT false,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "last_modifier" int8,
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "create_time" timestamptz(6),
  "last_modified_time" timestamptz(6),
  "i18n_key" varchar(200) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."iam_role"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role"."code" IS '角色编码';
COMMENT ON COLUMN "public"."iam_role"."client_code" IS '终端编码';
COMMENT ON COLUMN "public"."iam_role"."data_scope" IS '数据权限范围';
COMMENT ON COLUMN "public"."iam_role"."internal" IS '是否系统内置';
COMMENT ON COLUMN "public"."iam_role"."remark" IS '备注';
COMMENT ON COLUMN "public"."iam_role"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_role"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_role"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_role"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."iam_role"."i18n_key" IS '国际化key（有值时走语言包翻译）';
COMMENT ON TABLE "public"."iam_role" IS '角色';

-- ----------------------------
-- Table structure for iam_role_code
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role_code";
CREATE TABLE "public"."iam_role_code" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "code_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_role_code"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role_code"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."iam_role_code"."code_id" IS '权限码ID';
COMMENT ON TABLE "public"."iam_role_code" IS '角色权限码关系';

-- ----------------------------
-- Table structure for iam_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_role_menu";
CREATE TABLE "public"."iam_role_menu" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "client_code" varchar(50) COLLATE "pg_catalog"."default",
  "menu_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_role_menu"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_role_menu"."role_id" IS '角色ID';
COMMENT ON COLUMN "public"."iam_role_menu"."client_code" IS '终端编码: ADMIN/ISV/AGENT/MCH';
COMMENT ON COLUMN "public"."iam_role_menu"."menu_id" IS '菜单ID';
COMMENT ON TABLE "public"."iam_role_menu" IS '角色-菜单关联表';

-- ----------------------------
-- Table structure for iam_social_login_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_social_login_config";
CREATE TABLE "public"."iam_social_login_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "source" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "client_id" varchar(128) COLLATE "pg_catalog"."default",
  "client_secret" varchar(256) COLLATE "pg_catalog"."default",
  "extra" jsonb DEFAULT '{}'::jsonb,
  "configured" bool DEFAULT false,
  "enabled" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_social_login_config"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_social_login_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."iam_social_login_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_social_login_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."iam_social_login_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_social_login_config"."version" IS '乐观锁';
COMMENT ON COLUMN "public"."iam_social_login_config"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."iam_social_login_config"."source" IS '平台编码';
COMMENT ON COLUMN "public"."iam_social_login_config"."client_id" IS '客户端ID';
COMMENT ON COLUMN "public"."iam_social_login_config"."client_secret" IS '客户端密钥';
COMMENT ON COLUMN "public"."iam_social_login_config"."extra" IS '平台特有配置';
COMMENT ON COLUMN "public"."iam_social_login_config"."configured" IS '是否已完成配置';
COMMENT ON COLUMN "public"."iam_social_login_config"."enabled" IS '是否启用';
COMMENT ON TABLE "public"."iam_social_login_config" IS '第三方平台登录配置表';

-- ----------------------------
-- Table structure for iam_user_dashboard_preference
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_dashboard_preference";
CREATE TABLE "public"."iam_user_dashboard_preference" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "client_code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "entries" jsonb NOT NULL DEFAULT '[]'::jsonb,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."client_code" IS '终端编码(WEB/MOBILE), PC与移动分开管理';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."entries" IS '已选快捷入口有序序列(纯key数组), 如 ["merchant","notify"]';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."iam_user_dashboard_preference" IS '用户工作台快捷入口偏好';

-- ----------------------------
-- Table structure for iam_user_expand_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_expand_info";
CREATE TABLE "public"."iam_user_expand_info" (
  "id" int8 NOT NULL,
  "sex" varchar(10) COLLATE "pg_catalog"."default",
  "avatar" varchar(500) COLLATE "pg_catalog"."default",
  "birthday" date,
  "last_login_ip" varchar(100) COLLATE "pg_catalog"."default",
  "login_count" int4,
  "register_source" varchar(100) COLLATE "pg_catalog"."default",
  "register_channel" varchar(100) COLLATE "pg_catalog"."default",
  "creator" int8,
  "last_modifier" int8,
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "create_time" timestamptz(6),
  "last_modified_time" timestamptz(6),
  "last_login_time" timestamptz(6),
  "register_time" timestamptz(6),
  "current_login_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."iam_user_expand_info"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_expand_info"."sex" IS '性别';
COMMENT ON COLUMN "public"."iam_user_expand_info"."avatar" IS '头像图片ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."birthday" IS '生日';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_login_ip" IS '最后登录IP';
COMMENT ON COLUMN "public"."iam_user_expand_info"."login_count" IS '登录次数';
COMMENT ON COLUMN "public"."iam_user_expand_info"."register_source" IS '注册来源';
COMMENT ON COLUMN "public"."iam_user_expand_info"."register_channel" IS '注册渠道';
COMMENT ON COLUMN "public"."iam_user_expand_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_expand_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_expand_info"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."iam_user_expand_info" IS '用户扩展信息';

-- ----------------------------
-- Table structure for iam_user_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_info";
CREATE TABLE "public"."iam_user_info" (
  "id" int8 NOT NULL,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "client_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "account" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "password" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "phone" varchar(50) COLLATE "pg_catalog"."default",
  "email" varchar(100) COLLATE "pg_catalog"."default",
  "administrator" bool DEFAULT false,
  "status" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'normal'::character varying,
  "creator" int8,
  "last_modifier" int8,
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "create_time" timestamptz(6),
  "last_modified_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."iam_user_info"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_info"."name" IS '名称';
COMMENT ON COLUMN "public"."iam_user_info"."client_code" IS '终端编码';
COMMENT ON COLUMN "public"."iam_user_info"."account" IS '账号';
COMMENT ON COLUMN "public"."iam_user_info"."password" IS '密码';
COMMENT ON COLUMN "public"."iam_user_info"."phone" IS '手机号';
COMMENT ON COLUMN "public"."iam_user_info"."email" IS '邮箱';
COMMENT ON COLUMN "public"."iam_user_info"."administrator" IS '是否管理员';
COMMENT ON COLUMN "public"."iam_user_info"."status" IS '账号状态';
COMMENT ON COLUMN "public"."iam_user_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_info"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_info"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."iam_user_info" IS '用户核心信息';

-- ----------------------------
-- Table structure for iam_user_password_history
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_password_history";
CREATE TABLE "public"."iam_user_password_history" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "password" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."iam_user_password_history"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_password_history"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_password_history"."password" IS '密码';
COMMENT ON COLUMN "public"."iam_user_password_history"."creator" IS '创建者ID';
COMMENT ON TABLE "public"."iam_user_password_history" IS '用户密码历史表';

-- ----------------------------
-- Table structure for iam_user_password_security
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_password_security";
CREATE TABLE "public"."iam_user_password_security" (
  "id" int8 NOT NULL,
  "password_error_count" int4,
  "lock_time" timestamp(6),
  "password_expire_time" timestamp(6),
  "last_change_password_time" timestamp(6),
  "initial_password" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "last_failure_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."iam_user_password_security"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_password_security"."password_error_count" IS '密码错误次数';
COMMENT ON COLUMN "public"."iam_user_password_security"."lock_time" IS '锁定结束时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."password_expire_time" IS '密码过期时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_change_password_time" IS '上次修改密码时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."initial_password" IS '是否初始密码';
COMMENT ON COLUMN "public"."iam_user_password_security"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_password_security"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."version" IS '版本号';
COMMENT ON COLUMN "public"."iam_user_password_security"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."iam_user_password_security" IS '用户密码安全信息';

-- ----------------------------
-- Table structure for iam_user_role
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_role";
CREATE TABLE "public"."iam_user_role" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."iam_user_role"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_role"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_role"."role_id" IS '角色ID';
COMMENT ON TABLE "public"."iam_user_role" IS '用户角色关系';

-- ----------------------------
-- Table structure for iam_user_social
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_social";
CREATE TABLE "public"."iam_user_social" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "client_code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'admin'::character varying,
  "source" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "open_id" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "username" varchar(128) COLLATE "pg_catalog"."default",
  "avatar" varchar(512) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_user_social"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_social"."user_id" IS '本地用户ID(关联 iam_user_info.id)';
COMMENT ON COLUMN "public"."iam_user_social"."client_code" IS '终端编码(admin/merchant)';
COMMENT ON COLUMN "public"."iam_user_social"."source" IS '平台编码(weChat/weCom/qq/github/gitee/feishu/dingTalk)';
COMMENT ON COLUMN "public"."iam_user_social"."open_id" IS '平台用户唯一标识(openid/uuid)';
COMMENT ON COLUMN "public"."iam_user_social"."username" IS '平台昵称';
COMMENT ON COLUMN "public"."iam_user_social"."avatar" IS '平台头像';
COMMENT ON COLUMN "public"."iam_user_social"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_social"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_social"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."iam_user_social"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_social"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."iam_user_social"."deleted" IS '删除标志(逻辑删除)';
COMMENT ON TABLE "public"."iam_user_social" IS '用户第三方账号绑定';

-- ----------------------------
-- Table structure for iam_user_two_factor
-- ----------------------------
DROP TABLE IF EXISTS "public"."iam_user_two_factor";
CREATE TABLE "public"."iam_user_two_factor" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "secret" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "backup_codes" jsonb,
  "backup_codes_remaining" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."iam_user_two_factor"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_two_factor"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_two_factor"."secret" IS 'TOTP 密钥';
COMMENT ON COLUMN "public"."iam_user_two_factor"."backup_codes" IS '备用验证码';
COMMENT ON COLUMN "public"."iam_user_two_factor"."backup_codes_remaining" IS '剩余可用备用验证码数量';
COMMENT ON COLUMN "public"."iam_user_two_factor"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."iam_user_two_factor"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_two_factor"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."iam_user_two_factor"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_two_factor"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."iam_user_two_factor"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."iam_user_two_factor" IS '用户双因素认证绑定记录';

-- ----------------------------
-- Table structure for lakala_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."lakala_isv_channel_merchant";
CREATE TABLE "public"."lakala_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "lakala_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "term_no" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."mch_no" IS '平台商户号';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."channel_mch_no" IS '通道商户号(LAKALA+雪花)';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."lakala_mch_no" IS '拉卡拉商户编号(merchantNo)';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."term_no" IS '终端号';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."deleted" IS '逻辑删除标志';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON TABLE "public"."lakala_isv_channel_merchant" IS '拉卡拉通道商户绑定';

-- ----------------------------
-- Table structure for lakala_isv_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."lakala_isv_key_config";
CREATE TABLE "public"."lakala_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "lkl_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "mch_serial_no" varchar(128) COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "sm4_key" varchar(128) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "org_code" varchar(64) COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."lakala_isv_key_config"."product" IS '产品编码 @see ProductEnum';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."lkl_app_id" IS '拉卡拉应用编号';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."mch_serial_no" IS '商户证书序列号';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."private_key" IS '商户RSA私钥PEM(加密存储)';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."public_key" IS '拉卡拉RSA公钥PEM(加密存储)';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."sm4_key" IS 'SM4对称密钥(加密存储, 进件敏感字段加密用)';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."deleted" IS '逻辑删除标志';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."org_code" IS '机构代码';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON TABLE "public"."lakala_isv_key_config" IS '拉卡拉服务商密钥配置(全局唯一)';

-- ----------------------------
-- Table structure for leshua_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."leshua_isv_channel_merchant";
CREATE TABLE "public"."leshua_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "ls_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."channel_mch_no" IS '通道商户号(LESHUA+雪花)';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."product" IS '所属支付产品(对应 ProductEnum.code)';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."ls_mch_no" IS '乐刷商户编号(merchant_id)';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."leshua_isv_channel_merchant" IS '乐刷通道商户绑定';

-- ----------------------------
-- Table structure for leshua_isv_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."leshua_isv_key_config";
CREATE TABLE "public"."leshua_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "ls_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "trade_key" text COLLATE "pg_catalog"."default",
  "notify_key" text COLLATE "pg_catalog"."default",
  "sign_type" varchar(16) COLLATE "pg_catalog"."default",
  "ls_isv_no" varchar(64) COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."leshua_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."product" IS '产品编码(对应 ProductEnum.code, 如 leshua_pay)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."ls_mch_no" IS '乐刷商户号(merchant_id, 服务商级或商户级, 全局唯一)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."trade_key" IS '交易密钥(tradeKey, 请求签名与响应/回调验签, 加密存储)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."notify_key" IS '异步通知密钥(notifyKey, 部分场景回调验签, 加密存储)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."sign_type" IS '签名类型(MD5 / SM3)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."ls_isv_no" IS '乐刷服务商号(lsIsvNo, 进件场景使用, 可选)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."leshua_isv_key_config" IS '乐刷服务商密钥配置';

-- ----------------------------
-- Table structure for mch_app_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_app_info";
CREATE TABLE "public"."mch_app_info" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "default_app" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_app_info"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_app_info"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_app_info"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."mch_app_info"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."mch_app_info"."status" IS '应用状态，字典 mch_app_status';
COMMENT ON COLUMN "public"."mch_app_info"."default_app" IS '是否默认应用';
COMMENT ON COLUMN "public"."mch_app_info"."creator" IS '创建者';
COMMENT ON COLUMN "public"."mch_app_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_app_info"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."mch_app_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_app_info"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_app_info"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."mch_app_info" IS '商户应用信息';

-- ----------------------------
-- Table structure for mch_app_notify_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_app_notify_config";
CREATE TABLE "public"."mch_app_notify_config" (
  "id" int8 NOT NULL DEFAULT nextval('mch_app_notify_config_id_seq'::regclass),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_url" varchar(255) COLLATE "pg_catalog"."default",
  "notify_way" varchar(20) COLLATE "pg_catalog"."default" DEFAULT 'http'::character varying,
  "subscribed_events" varchar(100) COLLATE "pg_catalog"."default",
  "status" bool DEFAULT false,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_app_notify_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_app_notify_config"."app_id" IS '应用ID';
COMMENT ON COLUMN "public"."mch_app_notify_config"."notify_url" IS '回调地址(https, notifyWay=http 时生效)';
COMMENT ON COLUMN "public"."mch_app_notify_config"."notify_way" IS '传输通道: http-HTTP异步回调 / mq-MQ推送(订阅 daxpay.notice.<appId> Topic)';
COMMENT ON COLUMN "public"."mch_app_notify_config"."subscribed_events" IS '订阅事件类型(逗号分隔, 支持前缀匹配: pay 匹配 pay.*, refund 匹配 refund.*)';
COMMENT ON COLUMN "public"."mch_app_notify_config"."status" IS '启用状态(true-启用 false-禁用)';
COMMENT ON COLUMN "public"."mch_app_notify_config"."remark" IS '备注';
COMMENT ON TABLE "public"."mch_app_notify_config" IS '商户应用事件通知配置(应用级,通用事件订阅,与支付订单级回调并行)';

-- ----------------------------
-- Table structure for mch_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_channel_merchant";
CREATE TABLE "public"."mch_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "channel_merchant_name" varchar(200) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "enable" bool DEFAULT false,
  "source" varchar(32) COLLATE "pg_catalog"."default",
  "apply_id" int8,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_channel_merchant"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."mch_channel_merchant"."channel_merchant_name" IS '商户名称';
COMMENT ON COLUMN "public"."mch_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."mch_channel_merchant"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."mch_channel_merchant"."source" IS '创建来源';
COMMENT ON COLUMN "public"."mch_channel_merchant"."apply_id" IS '申请单ID';
COMMENT ON COLUMN "public"."mch_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_channel_merchant"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_channel_merchant"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_channel_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON TABLE "public"."mch_channel_merchant" IS '通道商户信息';

-- ----------------------------
-- Table structure for mch_credential
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_credential";
CREATE TABLE "public"."mch_credential" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "secret_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_credential"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_credential"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_credential"."public_key" IS '商户公钥(加密存储)';
COMMENT ON COLUMN "public"."mch_credential"."secret_key" IS '通信密钥(加密存储)';
COMMENT ON COLUMN "public"."mch_credential"."creator" IS '创建者';
COMMENT ON COLUMN "public"."mch_credential"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_credential"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."mch_credential"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_credential"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_credential"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."mch_credential" IS '商户对接配置';

-- ----------------------------
-- Table structure for mch_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_info";
CREATE TABLE "public"."mch_info" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "mch_name" varchar(128) COLLATE "pg_catalog"."default",
  "mch_short_name" varchar(64) COLLATE "pg_catalog"."default",
  "admin_user_id" int8,
  "status" varchar(32) COLLATE "pg_catalog"."default",
  "subject_type" varchar(32) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."mch_info"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_info"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_info"."mch_name" IS '商户名称';
COMMENT ON COLUMN "public"."mch_info"."mch_short_name" IS '商户简称';
COMMENT ON COLUMN "public"."mch_info"."admin_user_id" IS '关联管理员用户ID';
COMMENT ON COLUMN "public"."mch_info"."status" IS '状态';
COMMENT ON COLUMN "public"."mch_info"."subject_type" IS '主体类型';
COMMENT ON COLUMN "public"."mch_info"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_info"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_info"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."mch_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_info"."version" IS '版本号(乐观锁)';
COMMENT ON TABLE "public"."mch_info" IS '商户信息表';

-- ----------------------------
-- Table structure for mch_risk_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_risk_config";
CREATE TABLE "public"."mch_risk_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "geo_fence_enabled" bool NOT NULL DEFAULT false,
  "deleted" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."mch_risk_config"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_risk_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_risk_config"."geo_fence_enabled" IS '是否启用地理围栏（商户级 opt-in）';
COMMENT ON COLUMN "public"."mch_risk_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_risk_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_risk_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_risk_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."mch_risk_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_risk_config"."version" IS '版本号(乐观锁)';
COMMENT ON TABLE "public"."mch_risk_config" IS '商户风控配置表';

-- ----------------------------
-- Table structure for base_city_adjacent
-- ----------------------------
DROP TABLE IF EXISTS "public"."base_city_adjacent";
CREATE TABLE "public"."base_city_adjacent" (
  "id" bigserial NOT NULL,
  "city_code" varchar(4) COLLATE "pg_catalog"."default" NOT NULL,
  "adjacent_city_code" varchar(4) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."base_city_adjacent"."id" IS '主键ID（DB 自增, 纯关系表数据导入专用）';
COMMENT ON COLUMN "public"."base_city_adjacent"."city_code" IS '城市编码（base_city.code）';
COMMENT ON COLUMN "public"."base_city_adjacent"."adjacent_city_code" IS '相邻城市编码（base_city.code）';
COMMENT ON TABLE "public"."base_city_adjacent" IS '城市接壤关系表（双向存储，围栏 balanced 邻市容错用）';

-- ----------------------------
-- Table structure for mch_notice_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_notice_record";
CREATE TABLE "public"."mch_notice_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "task_id" int8 NOT NULL,
  "req_count" int4 NOT NULL,
  "send_type" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "success" bool NOT NULL DEFAULT false,
  "http_status" int4,
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "request_digest" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."mch_notice_record"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_notice_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_notice_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_notice_record"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_notice_record"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_notice_record"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_notice_record"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_notice_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_notice_record"."task_id" IS '通知任务ID';
COMMENT ON COLUMN "public"."mch_notice_record"."req_count" IS '本次对应的发送序号';
COMMENT ON COLUMN "public"."mch_notice_record"."send_type" IS '发送类型: auto / manual';
COMMENT ON COLUMN "public"."mch_notice_record"."success" IS '是否成功';
COMMENT ON COLUMN "public"."mch_notice_record"."http_status" IS 'HTTP状态码';
COMMENT ON COLUMN "public"."mch_notice_record"."error_msg" IS '错误摘要';
COMMENT ON COLUMN "public"."mch_notice_record"."request_digest" IS '请求摘要(截断)';
COMMENT ON TABLE "public"."mch_notice_record" IS '商户出站通知发送记录';

-- ----------------------------
-- Table structure for mch_notice_task
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_notice_task";
CREATE TABLE "public"."mch_notice_task" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_id" int8,
  "biz_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "event" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "source" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "content_mode" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "url" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
  "success" bool NOT NULL DEFAULT false,
  "send_count" int4 NOT NULL DEFAULT 0,
  "delay_count" int4 NOT NULL DEFAULT 0,
  "next_time" timestamptz(6),
  "latest_time" timestamptz(6),
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "transport" varchar(8) COLLATE "pg_catalog"."default" NOT NULL,
  "format" varchar(16) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."mch_notice_task"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_notice_task"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_notice_task"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_notice_task"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."mch_notice_task"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_notice_task"."version" IS '版本号';
COMMENT ON COLUMN "public"."mch_notice_task"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."mch_notice_task"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_notice_task"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."mch_notice_task"."biz_id" IS '业务主键ID';
COMMENT ON COLUMN "public"."mch_notice_task"."biz_no" IS '业务单号';
COMMENT ON COLUMN "public"."mch_notice_task"."event" IS '通知事件码(如 pay.success / refund.close)';
COMMENT ON COLUMN "public"."mch_notice_task"."source" IS 'URL来源: order / app / protocol';
COMMENT ON COLUMN "public"."mch_notice_task"."content_mode" IS '内容策略: snapshot / ref';
COMMENT ON COLUMN "public"."mch_notice_task"."content" IS '通知内容(快照JSON或引用指针JSON)';
COMMENT ON COLUMN "public"."mch_notice_task"."url" IS '目标地址(HTTP时为回调URL, MQ时为Topic名, 如 daxpay.notice.<appId>)';
COMMENT ON COLUMN "public"."mch_notice_task"."success" IS '是否发送成功';
COMMENT ON COLUMN "public"."mch_notice_task"."send_count" IS '已发送次数';
COMMENT ON COLUMN "public"."mch_notice_task"."delay_count" IS '延迟重试次数';
COMMENT ON COLUMN "public"."mch_notice_task"."next_time" IS '下次发送时间';
COMMENT ON COLUMN "public"."mch_notice_task"."latest_time" IS '最后发送时间';
COMMENT ON COLUMN "public"."mch_notice_task"."error_msg" IS '最近一次错误摘要';
COMMENT ON COLUMN "public"."mch_notice_task"."transport" IS '传输通道: http / mq';
COMMENT ON COLUMN "public"."mch_notice_task"."format" IS '报文格式: system / easy_pay';
COMMENT ON TABLE "public"."mch_notice_task" IS '商户出站通知任务';

-- ----------------------------
-- Table structure for mch_store_info
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_store_info";
CREATE TABLE "public"."mch_store_info" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "store_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "store_name" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "contact_phone" varchar(32) COLLATE "pg_catalog"."default",
  "logo_url" varchar(512) COLLATE "pg_catalog"."default",
  "facade_url" varchar(512) COLLATE "pg_catalog"."default",
  "interior_url" varchar(512) COLLATE "pg_catalog"."default",
  "region_code" varchar(12) COLLATE "pg_catalog"."default",
  "address" varchar(256) COLLATE "pg_catalog"."default",
  "longitude" numeric(10,7),
  "latitude" numeric(10,7),
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'enable'::character varying,
  "remark" varchar(512) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "default_store" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_store_info"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_store_info"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_store_info"."store_no" IS '门店号(系统生成, 唯一)';
COMMENT ON COLUMN "public"."mch_store_info"."store_name" IS '门店名称';
COMMENT ON COLUMN "public"."mch_store_info"."contact_phone" IS '联系人电话';
COMMENT ON COLUMN "public"."mch_store_info"."logo_url" IS '门店LOGO';
COMMENT ON COLUMN "public"."mch_store_info"."facade_url" IS '门头照';
COMMENT ON COLUMN "public"."mch_store_info"."interior_url" IS '门店内景照';
COMMENT ON COLUMN "public"."mch_store_info"."region_code" IS '行政区划代码(区县级)';
COMMENT ON COLUMN "public"."mch_store_info"."address" IS '详细地址';
COMMENT ON COLUMN "public"."mch_store_info"."longitude" IS '经度';
COMMENT ON COLUMN "public"."mch_store_info"."latitude" IS '纬度';
COMMENT ON COLUMN "public"."mch_store_info"."status" IS '状态(enable启用/disabled停用)';
COMMENT ON COLUMN "public"."mch_store_info"."remark" IS '备注';
COMMENT ON COLUMN "public"."mch_store_info"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."mch_store_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_store_info"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."mch_store_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_store_info"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."mch_store_info"."deleted" IS '逻辑删除标志';
COMMENT ON COLUMN "public"."mch_store_info"."default_store" IS '是否默认门店(同商户至多一个; 支付未指定 storeNo 时回落)';
COMMENT ON TABLE "public"."mch_store_info" IS '商户门店(商户物理经营场所)';

-- ----------------------------
-- Table structure for mch_user
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_user";
CREATE TABLE "public"."mch_user" (
  "id" int8 NOT NULL DEFAULT nextval('mch_user_id_seq'::regclass),
  "user_id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "administrator" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamp(6) DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "public"."mch_user"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_user"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."mch_user"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."mch_user"."administrator" IS '是否管理员';
COMMENT ON COLUMN "public"."mch_user"."creator" IS '创建者';
COMMENT ON COLUMN "public"."mch_user"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."mch_user" IS '商户用户关联表';

-- ----------------------------
-- Table structure for mch_wx_domain_verify
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_wx_domain_verify";
CREATE TABLE "public"."mch_wx_domain_verify" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "platform" bool NOT NULL DEFAULT false,
  "file_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "verify_code" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "file_content" varchar(200) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(200) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."mch_no" IS '商户号（平台级填 0）';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."platform" IS '是否平台级：false-商户级 true-平台级';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."file_name" IS '完整文件名（如 MP_verify_PjhdRxpB8FhG06Fr.txt）';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."verify_code" IS '验证码（文件名提取，全局唯一）';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."file_content" IS '文件内容（微信生成的随机字符串）';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."remark" IS '备注';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."version" IS '版本号（乐观锁）';
COMMENT ON COLUMN "public"."mch_wx_domain_verify"."deleted" IS '逻辑删除标记';
COMMENT ON TABLE "public"."mch_wx_domain_verify" IS '商户微信域名验证文件';

-- ----------------------------
-- Table structure for notify_message
-- ----------------------------
DROP TABLE IF EXISTS "public"."notify_message";
CREATE TABLE "public"."notify_message" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "title" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "content" varchar(1024) COLLATE "pg_catalog"."default",
  "source" varchar(32) COLLATE "pg_catalog"."default",
  "link" varchar(255) COLLATE "pg_catalog"."default",
  "extra" text COLLATE "pg_catalog"."default",
  "is_read" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."notify_message"."id" IS '主键';
COMMENT ON COLUMN "public"."notify_message"."user_id" IS '接收用户ID';
COMMENT ON COLUMN "public"."notify_message"."title" IS '标题';
COMMENT ON COLUMN "public"."notify_message"."content" IS '正文内容';
COMMENT ON COLUMN "public"."notify_message"."source" IS '业务来源(预留, 如TRADE/REFUND等)';
COMMENT ON COLUMN "public"."notify_message"."link" IS '跳转链接(内部路由或完整http外链)';
COMMENT ON COLUMN "public"."notify_message"."extra" IS '跳转附加参数(JSON字符串)';
COMMENT ON COLUMN "public"."notify_message"."is_read" IS '是否已读';
COMMENT ON COLUMN "public"."notify_message"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."notify_message"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."notify_message"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."notify_message"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."notify_message"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."notify_message"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."notify_message" IS '个人消息(定向通知, 1条对1用户)';

-- ----------------------------
-- Table structure for notify_notice
-- ----------------------------
DROP TABLE IF EXISTS "public"."notify_notice";
CREATE TABLE "public"."notify_notice" (
  "id" int8 NOT NULL,
  "title" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default" NOT NULL,
  "severity" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'normal'::character varying,
  "is_top" bool NOT NULL DEFAULT false,
  "effective_time" timestamptz(6),
  "expire_time" timestamptz(6),
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'draft'::character varying,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."notify_notice"."id" IS '主键';
COMMENT ON COLUMN "public"."notify_notice"."title" IS '标题';
COMMENT ON COLUMN "public"."notify_notice"."content" IS '正文(Markdown原文)';
COMMENT ON COLUMN "public"."notify_notice"."severity" IS '重要程度(normal普通/important重要)';
COMMENT ON COLUMN "public"."notify_notice"."is_top" IS '是否置顶';
COMMENT ON COLUMN "public"."notify_notice"."effective_time" IS '生效时间(为空则立即生效)';
COMMENT ON COLUMN "public"."notify_notice"."expire_time" IS '过期时间(为空则永久有效)';
COMMENT ON COLUMN "public"."notify_notice"."status" IS '状态(draft草稿/published发布/offline下线)';
COMMENT ON COLUMN "public"."notify_notice"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."notify_notice"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."notify_notice"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."notify_notice"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."notify_notice"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."notify_notice"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."notify_notice" IS '公告通知(广播型, 1条对多用户可见)';

-- ----------------------------
-- Table structure for notify_notice_read
-- ----------------------------
DROP TABLE IF EXISTS "public"."notify_notice_read";
CREATE TABLE "public"."notify_notice_read" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "notice_id" int8 NOT NULL,
  "read_time" timestamptz(6),
  "is_ignored" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."notify_notice_read"."id" IS '主键';
COMMENT ON COLUMN "public"."notify_notice_read"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."notify_notice_read"."notice_id" IS '公告ID';
COMMENT ON COLUMN "public"."notify_notice_read"."read_time" IS '阅读时间';
COMMENT ON COLUMN "public"."notify_notice_read"."is_ignored" IS '是否忽略(用户主动隐藏该公告)';
COMMENT ON COLUMN "public"."notify_notice_read"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."notify_notice_read"."create_time" IS '创建时间';
COMMENT ON TABLE "public"."notify_notice_read" IS '公告已读记录(用户x公告)';

-- ----------------------------
-- Table structure for pay_blacklist
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_blacklist";
CREATE TABLE "public"."pay_blacklist" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "wx_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'enable'::character varying,
  "reason" varchar(255) COLLATE "pg_catalog"."default",
  "expire_time" timestamptz(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_blacklist"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_blacklist"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_blacklist"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_blacklist"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_blacklist"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_blacklist"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_blacklist"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_blacklist"."type" IS '名单类型: ip / alipay_user / wechat_openid';
COMMENT ON COLUMN "public"."pay_blacklist"."value" IS '名单值（IP、支付宝userId或微信openId）';
COMMENT ON COLUMN "public"."pay_blacklist"."wx_app_id" IS '微信平台支付应用 AppId；仅 wechat_openid 使用';
COMMENT ON COLUMN "public"."pay_blacklist"."status" IS '状态: enable-启用, disable-禁用';
COMMENT ON COLUMN "public"."pay_blacklist"."reason" IS '拉黑原因';
COMMENT ON COLUMN "public"."pay_blacklist"."expire_time" IS '过期时间（空表示永久有效）';
COMMENT ON COLUMN "public"."pay_blacklist"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_blacklist" IS '支付黑名单（平台级；IP全局 / 支付宝userId全局 / 微信按平台应用）';

-- ----------------------------
-- Table structure for pay_callback_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_callback_record";
CREATE TABLE "public"."pay_callback_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(64) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "trade_no" varchar(100) COLLATE "pg_catalog"."default",
  "out_trade_no" varchar(150) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "callback_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_info" text COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "error_msg" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_callback_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_callback_record"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_callback_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_callback_record"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_callback_record"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_callback_record"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_callback_record"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_callback_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_callback_record"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_callback_record"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."pay_callback_record"."trade_no" IS '平台交易号(支付回调为 trade_no, 退款回调为 refund_no)';
COMMENT ON COLUMN "public"."pay_callback_record"."out_trade_no" IS '通道交易号';
COMMENT ON COLUMN "public"."pay_callback_record"."product" IS '支付产品';
COMMENT ON COLUMN "public"."pay_callback_record"."callback_type" IS '回调类型: pay / refund';
COMMENT ON COLUMN "public"."pay_callback_record"."notify_info" IS '通知消息内容(JSON)';
COMMENT ON COLUMN "public"."pay_callback_record"."status" IS '回调处理状态';
COMMENT ON COLUMN "public"."pay_callback_record"."error_msg" IS '错误信息';
COMMENT ON TABLE "public"."pay_callback_record" IS '通道入站回调记录';

-- ----------------------------
-- Table structure for pay_channel_terminal
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_channel_terminal";
CREATE TABLE "public"."pay_channel_terminal" (
  "id" int8 NOT NULL,
  "channel_mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "out_terminal_no" varchar(64) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'init'::character varying,
  "error_msg" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_channel_terminal"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_channel_terminal"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."pay_channel_terminal"."product" IS '所属支付产品(冗余)';
COMMENT ON COLUMN "public"."pay_channel_terminal"."channel" IS '所属通道编码(冗余)';
COMMENT ON COLUMN "public"."pay_channel_terminal"."type" IS '报送类型 common/wechat/alipay/union';
COMMENT ON COLUMN "public"."pay_channel_terminal"."name" IS '终端名称';
COMMENT ON COLUMN "public"."pay_channel_terminal"."out_terminal_no" IS '通道侧终端号(人工录入)';
COMMENT ON COLUMN "public"."pay_channel_terminal"."status" IS '登记状态 init/wait/submit/logged/error';
COMMENT ON COLUMN "public"."pay_channel_terminal"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_channel_terminal"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_channel_terminal"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_channel_terminal"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_channel_terminal"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_channel_terminal"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_channel_terminal"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_channel_terminal"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_channel_terminal"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_channel_terminal" IS '通道终端台账(人工登记, 无自动报备)';

-- ----------------------------
-- Table structure for pay_close_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_close_record";
CREATE TABLE "public"."pay_close_record" (
  "id" int8 NOT NULL DEFAULT nextval('pay_close_record_id_seq'::regclass),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "trade_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "closed" bool NOT NULL DEFAULT false,
  "close_type" varchar(32) COLLATE "pg_catalog"."default",
  "error_code" varchar(128) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6) DEFAULT now(),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_close_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_close_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_close_record"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_close_record"."trade_no" IS '平台交易号(对应 pay_trade.trade_no)';
COMMENT ON COLUMN "public"."pay_close_record"."biz_trade_no" IS '商户业务单号(对应 pay_normal_order.biz_order_no)';
COMMENT ON COLUMN "public"."pay_close_record"."product" IS '支付产品编码';
COMMENT ON COLUMN "public"."pay_close_record"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_close_record"."closed" IS '是否关闭成功';
COMMENT ON COLUMN "public"."pay_close_record"."close_type" IS '关闭类型(close/cancel)';
COMMENT ON COLUMN "public"."pay_close_record"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_close_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_close_record"."client_ip" IS '客户端 IP';
COMMENT ON COLUMN "public"."pay_close_record"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_close_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_close_record"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_close_record"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_close_record"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_close_record"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."pay_close_record" IS '支付关闭记录';

-- ----------------------------
-- Table structure for pay_easy_pay_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_easy_pay_config";
CREATE TABLE "public"."pay_easy_pay_config" (
  "id" int8 NOT NULL,
  "pid" int4 NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "limit_pay" varchar(64) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_easy_pay_config"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."pid" IS '易支付商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."limit_pay" IS '限制支付方式';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_easy_pay_config"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_easy_pay_config" IS '易支付场景配置表';

-- ----------------------------
-- Table structure for pay_easy_pay_credential
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_easy_pay_credential";
CREATE TABLE "public"."pay_easy_pay_credential" (
  "id" int8 NOT NULL,
  "pid" int4 NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "enable" bool NOT NULL DEFAULT false,
  "enable_v1" bool NOT NULL DEFAULT false,
  "enable_v2" bool NOT NULL DEFAULT true,
  "md5_key" varchar(128) COLLATE "pg_catalog"."default",
  "use_system_key" bool NOT NULL DEFAULT true,
  "public_key" text COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."pid" IS '易支付商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."app_id" IS '应用号（支付出口）';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."enable_v1" IS '是否开启V1接口';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."enable_v2" IS '是否开启V2接口';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."md5_key" IS 'V1 MD5密钥';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."use_system_key" IS 'V2是否使用系统公私钥';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."public_key" IS '商户RSA公钥';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_easy_pay_credential"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_easy_pay_credential" IS '易支付凭证配置表（应用级）';

-- ----------------------------
-- Table structure for pay_easy_pay_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_easy_pay_order";
CREATE TABLE "public"."pay_easy_pay_order" (
  "id" int8 NOT NULL,
  "order_id" int8,
  "pid" int4 NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "out_trade_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "api_trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "type" varchar(32) COLLATE "pg_catalog"."default",
  "status" int4 NOT NULL DEFAULT 0,
  "add_time" timestamptz(6),
  "end_time" timestamptz(6),
  "name" varchar(200) COLLATE "pg_catalog"."default",
  "money" numeric(16,2) NOT NULL,
  "refund_money" numeric(16,2) DEFAULT 0,
  "notify_url" varchar(500) COLLATE "pg_catalog"."default",
  "return_url" varchar(500) COLLATE "pg_catalog"."default",
  "param" varchar(500) COLLATE "pg_catalog"."default",
  "buyer" varchar(128) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "api_version" varchar(8) COLLATE "pg_catalog"."default",
  "pc_call_type" varchar(32) COLLATE "pg_catalog"."default",
  "pay_url" varchar(1000) COLLATE "pg_catalog"."default",
  "pay_body" text COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_easy_pay_order"."id" IS '主键（收银台路径参数）';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."order_id" IS '关联内核容器ID';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."pid" IS '易支付商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."trade_no" IS '平台业务单号（容器orderNo）';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."out_trade_no" IS '商户订单号';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."api_trade_no" IS '通道订单号';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."type" IS '协议支付方式 alipay/wxpay/aggregate';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."status" IS '协议状态 0待付 1成功';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."add_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."end_time" IS '完成时间';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."name" IS '商品名称';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."money" IS '订单金额（元）';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."refund_money" IS '已退款金额（元）';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."notify_url" IS '异步通知地址（本期仅落库）';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."return_url" IS '同步跳转地址';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."param" IS '业务扩展参数';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."buyer" IS '支付用户标识';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."client_ip" IS '客户端IP';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."api_version" IS 'API版本 v1/v2';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."pc_call_type" IS '支付调用方式';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."pay_url" IS '支付链接';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."pay_body" IS '支付参数体';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_easy_pay_order"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_easy_pay_order" IS '易支付协议订单表';

-- ----------------------------
-- Table structure for pay_easy_pay_refund_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_easy_pay_refund_order";
CREATE TABLE "public"."pay_easy_pay_refund_order" (
  "id" int8 NOT NULL,
  "mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "refund_id" int8,
  "easy_pay_order_id" int8,
  "pid" int4,
  "app_id" varchar(64) COLLATE "pg_catalog"."default",
  "refund_no" varchar(64) COLLATE "pg_catalog"."default",
  "biz_refund_no" varchar(64) COLLATE "pg_catalog"."default",
  "trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "out_trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "money" numeric(15,2),
  "status" int4,
  "api_version" varchar(20) COLLATE "pg_catalog"."default",
  "add_time" timestamptz(6),
  "end_time" timestamptz(6),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."id" IS '主键ID';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."refund_id" IS '关联内核退款单ID(pay_refund_order.id)';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."easy_pay_order_id" IS '关联易支付订单ID(pay_easy_pay_order.id)';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."pid" IS '易支付商户号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."refund_no" IS '平台退款单号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."biz_refund_no" IS '商户退款单号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."trade_no" IS '平台业务单号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."out_trade_no" IS '商户订单号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."money" IS '退款金额(元)';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."status" IS '协议退款状态 0=失败/处理中 1=成功';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."api_version" IS 'API版本 v1/v2';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."add_time" IS '退款发起时间';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."end_time" IS '退款完成时间';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_easy_pay_refund_order"."deleted" IS '删除标志(逻辑删除)';
COMMENT ON TABLE "public"."pay_easy_pay_refund_order" IS '易支付协议退款订单表';

-- ----------------------------
-- Table structure for pay_gateway_cashier_item
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_gateway_cashier_item";
CREATE TABLE "public"."pay_gateway_cashier_item" (
  "id" int8 NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "cashier_type" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "client_env" varchar(32) COLLATE "pg_catalog"."default",
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "icon" varchar(32) COLLATE "pg_catalog"."default",
  "recommend" bool NOT NULL DEFAULT false,
  "sort_no" int4 NOT NULL DEFAULT 0,
  "resolve_mode" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "capability" varchar(64) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."cashier_type" IS '收银台类型: h5/web';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."client_env" IS '客户端环境(ClientEnvEnum)；WEB 为空';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."name" IS '前台展示名称';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."icon" IS '图标编码';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."recommend" IS '是否推荐';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."sort_no" IS '排序号(越小越前)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."resolve_mode" IS '解析模式: method/direct';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."method" IS '支付方式(METHOD 模式)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."channel_mch_no" IS '通道商户号(DIRECT 模式)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."capability" IS '支付能力(DIRECT 模式)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_gateway_cashier_item" IS '网关收银台支付项配置(应用级)';

-- ----------------------------
-- Table structure for pay_gateway_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_gateway_order";
CREATE TABLE "public"."pay_gateway_order" (
  "id" int8 NOT NULL,
  "order_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_order_no" varchar(100) COLLATE "pg_catalog"."default",
  "gateway_type" varchar(32) COLLATE "pg_catalog"."default",
  "source" varchar(32) COLLATE "pg_catalog"."default",
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default",
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "return_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "expired_time" timestamptz(6),
  "amount" int8,
  "currency" varchar(16) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "method" varchar(32) COLLATE "pg_catalog"."default",
  "limit_pay" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(64) COLLATE "pg_catalog"."default",
  "openid" varchar(128) COLLATE "pg_catalog"."default",
  "client_env" varchar(32) COLLATE "pg_catalog"."default",
  "device" varchar(16) COLLATE "pg_catalog"."default",
  "pay_time" timestamptz(6),
  "close_time" timestamptz(6),
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "capability" varchar(64) COLLATE "pg_catalog"."default",
  "channel_app_id" varchar(128) COLLATE "pg_catalog"."default",
  "provider" varchar(32) COLLATE "pg_catalog"."default",
  "buyer_id" varchar(128) COLLATE "pg_catalog"."default",
  "trade_product" varchar(64) COLLATE "pg_catalog"."default",
  "trade_way" varchar(64) COLLATE "pg_catalog"."default",
  "bank_type" varchar(32) COLLATE "pg_catalog"."default",
  "promotion_type" varchar(64) COLLATE "pg_catalog"."default",
  "pay_body" text COLLATE "pg_catalog"."default",
  "pay_body_type" varchar(32) COLLATE "pg_catalog"."default",
  "trans_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "relation_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "extra_param" varchar(2048) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "goods_detail" jsonb,
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "store_no" varchar(32) COLLATE "pg_catalog"."default",
  "error_msg" varchar(500) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "link_form" varchar(16) COLLATE "pg_catalog"."default" DEFAULT 'h5'::character varying
)
;
COMMENT ON COLUMN "public"."pay_gateway_order"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_gateway_order"."order_no" IS '平台业务单号(容器身份, 与 tradeNo 独立; 预下单 URL 号)';
COMMENT ON COLUMN "public"."pay_gateway_order"."biz_order_no" IS '商户业务单号';
COMMENT ON COLUMN "public"."pay_gateway_order"."gateway_type" IS '网关支付类型(GatewayPayTypeEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."source" IS '订单来源(业务入口权威, TradeSourceEnum; 预下单写入)';
COMMENT ON COLUMN "public"."pay_gateway_order"."title" IS '标题';
COMMENT ON COLUMN "public"."pay_gateway_order"."description" IS '描述';
COMMENT ON COLUMN "public"."pay_gateway_order"."status" IS '业务状态(GatewayOrderStatusEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "public"."pay_gateway_order"."return_url" IS '同步跳转地址';
COMMENT ON COLUMN "public"."pay_gateway_order"."attach" IS '商户附加参数(回调原样返回)';
COMMENT ON COLUMN "public"."pay_gateway_order"."expired_time" IS '业务单过期时间';
COMMENT ON COLUMN "public"."pay_gateway_order"."amount" IS '业务单金额(最小货币单位)';
COMMENT ON COLUMN "public"."pay_gateway_order"."currency" IS '币种(CurrencyEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."channel" IS '支付通道编码(ChannelEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."method" IS '支付方式(PayMethodEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."limit_pay" IS '限制支付类型(PayLimitPayEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."product" IS '支付产品编码(ProductEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."openid" IS '微信 openid(jsapi/app/miniapp)';
COMMENT ON COLUMN "public"."pay_gateway_order"."client_env" IS '客户端环境(ClientEnvEnum, 支付时回填)';
COMMENT ON COLUMN "public"."pay_gateway_order"."device" IS '最后发起设备(mobile/pc, 支付时回填)';
COMMENT ON COLUMN "public"."pay_gateway_order"."pay_time" IS '支付成功时间';
COMMENT ON COLUMN "public"."pay_gateway_order"."close_time" IS '关闭时间';
COMMENT ON COLUMN "public"."pay_gateway_order"."channel_mch_no" IS '通道商户号(路由回填)';
COMMENT ON COLUMN "public"."pay_gateway_order"."capability" IS '支付能力编码(路由回填)';
COMMENT ON COLUMN "public"."pay_gateway_order"."channel_app_id" IS '通道应用 AppId 快照';
COMMENT ON COLUMN "public"."pay_gateway_order"."provider" IS '支付渠道(PayProviderEnum)';
COMMENT ON COLUMN "public"."pay_gateway_order"."buyer_id" IS '付款用户标识';
COMMENT ON COLUMN "public"."pay_gateway_order"."trade_product" IS '通道方记录的支付产品';
COMMENT ON COLUMN "public"."pay_gateway_order"."trade_way" IS '通道方记录的交易方式';
COMMENT ON COLUMN "public"."pay_gateway_order"."bank_type" IS '银行卡类型(借记卡/贷记卡)';
COMMENT ON COLUMN "public"."pay_gateway_order"."promotion_type" IS '活动类型';
COMMENT ON COLUMN "public"."pay_gateway_order"."pay_body" IS '支付参数体(已拉起缓存, 仅容器)';
COMMENT ON COLUMN "public"."pay_gateway_order"."pay_body_type" IS '支付参数体类型(jsapi/sdk/app)';
COMMENT ON COLUMN "public"."pay_gateway_order"."trans_order_no" IS '透传订单号(三方通道产生)';
COMMENT ON COLUMN "public"."pay_gateway_order"."relation_order_no" IS '实际上送通道的商户订单号(展示冗余)';
COMMENT ON COLUMN "public"."pay_gateway_order"."extra_param" IS '通道附加参数';
COMMENT ON COLUMN "public"."pay_gateway_order"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_gateway_order"."goods_detail" IS '订单商品明细列表(jsonb)';
COMMENT ON COLUMN "public"."pay_gateway_order"."client_ip" IS '下单客户端 IP';
COMMENT ON COLUMN "public"."pay_gateway_order"."store_no" IS '门店号(线下经营归属, 可空)';
COMMENT ON COLUMN "public"."pay_gateway_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_gateway_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_gateway_order"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_gateway_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_gateway_order"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_gateway_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_gateway_order"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_gateway_order"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_gateway_order"."link_form" IS '链接形态: h5/mini(聚合小程序扫码), 缺省 h5';
COMMENT ON TABLE "public"."pay_gateway_order" IS '网关支付业务单容器(聚合扫码/收银台预下单)';

-- ----------------------------
-- Table structure for pay_gateway_pay_client_env
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_gateway_pay_client_env";
CREATE TABLE "public"."pay_gateway_pay_client_env" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "config_id" int8 NOT NULL,
  "client_env" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "pay_form" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "capability" varchar(64) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."config_id" IS '网关支付配置主表ID';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."client_env" IS '客户端环境编码: wechat/alipay/union_pay/douyin';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."pay_form" IS '支付形态: h5/mini';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."method" IS '支付方式(METHOD 模式填)';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."channel_mch_no" IS '通道商户号(DIRECT 模式填)';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."capability" IS '支付能力(DIRECT 模式填)';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_gateway_pay_client_env"."version" IS '版本号(乐观锁)';
COMMENT ON TABLE "public"."pay_gateway_pay_client_env" IS '网关支付客户端环境配置(子表, 码牌/聚合共用)';

-- ----------------------------
-- Table structure for pay_gateway_pay_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_gateway_pay_config";
CREATE TABLE "public"."pay_gateway_pay_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "level" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'auto'::character varying,
  "auto_launch" bool NOT NULL DEFAULT false,
  "deleted" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0
)
;
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."level" IS '配置深度: auto/method/direct';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."auto_launch" IS '是否自动拉起支付(码牌仅对固定金额生效)';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_gateway_pay_config"."version" IS '版本号(乐观锁)';
COMMENT ON TABLE "public"."pay_gateway_pay_config" IS '网关支付配置(应用级, 码牌/聚合共用)';

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
-- Table structure for pay_md_channel
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_channel";
CREATE TABLE "public"."pay_md_channel" (
  "id" int8 NOT NULL,
  "code" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4 DEFAULT 0,
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
COMMENT ON COLUMN "public"."pay_md_channel"."icon" IS '图标';
COMMENT ON COLUMN "public"."pay_md_channel"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_channel"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_channel"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_md_channel"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_channel"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_md_channel"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_md_channel" IS '支付通道';

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
-- Table structure for pay_md_product
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_product";
CREATE TABLE "public"."pay_md_product" (
  "id" int8 NOT NULL,
  "code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
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
COMMENT ON COLUMN "public"."pay_md_product"."sort_no" IS '排序';
COMMENT ON COLUMN "public"."pay_md_product"."creator" IS '创建者';
COMMENT ON COLUMN "public"."pay_md_product"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_product"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."pay_md_product"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_product"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_md_product"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_md_product"."sandbox" IS '是否支持沙箱环境';
COMMENT ON COLUMN "public"."pay_md_product"."enabled" IS '是否启用该通道';
COMMENT ON TABLE "public"."pay_md_product" IS '支付产品';

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
-- Table structure for pay_md_product_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_md_product_config";
CREATE TABLE "public"."pay_md_product_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "active_env" varchar(32) COLLATE "pg_catalog"."default" DEFAULT 'prod'::character varying,
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
COMMENT ON COLUMN "public"."pay_md_product_config"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_md_product_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_product_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_product_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_md_product_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_product_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_md_product_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_md_product_config" IS '支付产品配置';

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
-- Table structure for pay_normal_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_normal_order";
CREATE TABLE "public"."pay_normal_order" (
  "id" int8 NOT NULL,
  "order_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_order_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "source" varchar(32) COLLATE "pg_catalog"."default",
  "title" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "return_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "expired_time" timestamptz(6),
  "amount" int8 NOT NULL,
  "currency" varchar(8) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CNY'::character varying,
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "method" varchar(32) COLLATE "pg_catalog"."default",
  "limit_pay" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "openid" varchar(128) COLLATE "pg_catalog"."default",
  "auth_code" varchar(128) COLLATE "pg_catalog"."default",
  "pay_time" timestamptz(6),
  "close_time" timestamptz(6),
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "capability" varchar(64) COLLATE "pg_catalog"."default",
  "channel_app_id" varchar(128) COLLATE "pg_catalog"."default",
  "provider" varchar(32) COLLATE "pg_catalog"."default",
  "buyer_id" varchar(64) COLLATE "pg_catalog"."default",
  "trade_product" varchar(64) COLLATE "pg_catalog"."default",
  "trade_way" varchar(64) COLLATE "pg_catalog"."default",
  "bank_type" varchar(64) COLLATE "pg_catalog"."default",
  "promotion_type" varchar(64) COLLATE "pg_catalog"."default",
  "pay_body" text COLLATE "pg_catalog"."default",
  "pay_body_type" varchar(32) COLLATE "pg_catalog"."default",
  "trans_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "relation_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "extra_param" varchar(2048) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "goods_detail" jsonb,
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "terminal_no" varchar(128) COLLATE "pg_catalog"."default",
  "store_no" varchar(32) COLLATE "pg_catalog"."default",
  "error_msg" varchar(500) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_normal_order"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_normal_order"."order_no" IS '平台业务单号(容器身份)';
COMMENT ON COLUMN "public"."pay_normal_order"."biz_order_no" IS '商户业务单号';
COMMENT ON COLUMN "public"."pay_normal_order"."source" IS '订单来源(业务入口权威, TradeSourceEnum)';
COMMENT ON COLUMN "public"."pay_normal_order"."title" IS '标题';
COMMENT ON COLUMN "public"."pay_normal_order"."description" IS '描述';
COMMENT ON COLUMN "public"."pay_normal_order"."status" IS '业务状态';
COMMENT ON COLUMN "public"."pay_normal_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "public"."pay_normal_order"."return_url" IS '同步跳转地址';
COMMENT ON COLUMN "public"."pay_normal_order"."attach" IS '商户附加参数';
COMMENT ON COLUMN "public"."pay_normal_order"."expired_time" IS '业务单过期时间';
COMMENT ON COLUMN "public"."pay_normal_order"."amount" IS '业务单金额(最小货币单位/分)';
COMMENT ON COLUMN "public"."pay_normal_order"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_normal_order"."channel" IS '支付通道编码';
COMMENT ON COLUMN "public"."pay_normal_order"."method" IS '支付方式';
COMMENT ON COLUMN "public"."pay_normal_order"."limit_pay" IS '限制支付类型';
COMMENT ON COLUMN "public"."pay_normal_order"."product" IS '支付产品编码';
COMMENT ON COLUMN "public"."pay_normal_order"."openid" IS '微信 openid';
COMMENT ON COLUMN "public"."pay_normal_order"."auth_code" IS '付款码（被扫支付，审计保留）';
COMMENT ON COLUMN "public"."pay_normal_order"."pay_time" IS '支付成功时间';
COMMENT ON COLUMN "public"."pay_normal_order"."close_time" IS '关闭时间';
COMMENT ON COLUMN "public"."pay_normal_order"."channel_mch_no" IS '通道商户号(路由回填)';
COMMENT ON COLUMN "public"."pay_normal_order"."capability" IS '支付能力编码(路由回填)';
COMMENT ON COLUMN "public"."pay_normal_order"."channel_app_id" IS '通道应用 AppId 快照';
COMMENT ON COLUMN "public"."pay_normal_order"."provider" IS '支付渠道(微信/支付宝/银联等)';
COMMENT ON COLUMN "public"."pay_normal_order"."buyer_id" IS '付款用户标识';
COMMENT ON COLUMN "public"."pay_normal_order"."trade_product" IS '通道方记录的支付产品';
COMMENT ON COLUMN "public"."pay_normal_order"."trade_way" IS '通道方记录的交易方式';
COMMENT ON COLUMN "public"."pay_normal_order"."bank_type" IS '银行卡类型';
COMMENT ON COLUMN "public"."pay_normal_order"."promotion_type" IS '活动类型';
COMMENT ON COLUMN "public"."pay_normal_order"."pay_body" IS '支付参数体';
COMMENT ON COLUMN "public"."pay_normal_order"."pay_body_type" IS '支付参数体类型';
COMMENT ON COLUMN "public"."pay_normal_order"."trans_order_no" IS '透传订单号';
COMMENT ON COLUMN "public"."pay_normal_order"."relation_order_no" IS '实际上送通道的商户订单号';
COMMENT ON COLUMN "public"."pay_normal_order"."extra_param" IS '通道附加参数';
COMMENT ON COLUMN "public"."pay_normal_order"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_normal_order"."goods_detail" IS '订单商品明细(jsonb)';
COMMENT ON COLUMN "public"."pay_normal_order"."client_ip" IS '下单客户端 IP';
COMMENT ON COLUMN "public"."pay_normal_order"."terminal_no" IS '终端设备编码';
COMMENT ON COLUMN "public"."pay_normal_order"."store_no" IS '门店号(线下经营归属, 可空)';
COMMENT ON COLUMN "public"."pay_normal_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_normal_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_normal_order"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_normal_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_normal_order"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_normal_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_normal_order"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_normal_order"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_normal_order" IS '普通支付业务单容器';

-- ----------------------------
-- Table structure for pay_platform_mobile_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_platform_mobile_app";
CREATE TABLE "public"."pay_platform_mobile_app" (
  "id" int8 NOT NULL,
  "app_type" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "platform" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "app_config" text COLLATE "pg_catalog"."default",
  "notify_config" jsonb,
  "binding_enabled" bool NOT NULL DEFAULT false,
  "enabled" bool NOT NULL DEFAULT true,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."app_type" IS '端类型: merchant-商户端 / admin-管理端 / cashier-收银台';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."platform" IS '移动平台: wx_h5/wx_mini/alipay_mini/dy_mini/android/ios';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."app_config" IS '平台特有密钥配置(JSON文本, AES-256-GCM加密存储)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."notify_config" IS '消息通知配置(jsonb, 明文, 非敏感)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."binding_enabled" IS '是否启用第三方账号用户绑定';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."deleted" IS '逻辑删除标记';
COMMENT ON TABLE "public"."pay_platform_mobile_app" IS '平台级移动端应用配置';

-- ----------------------------
-- Table structure for pay_refund_order
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_refund_order";
CREATE TABLE "public"."pay_refund_order" (
  "id" int8 NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "refund_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_refund_no" varchar(100) COLLATE "pg_catalog"."default",
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "trade_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_order_no" varchar(100) COLLATE "pg_catalog"."default",
  "out_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "out_refund_no" varchar(150) COLLATE "pg_catalog"."default",
  "amount" int8 NOT NULL,
  "order_amount" int8,
  "currency" varchar(8) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CNY'::character varying,
  "reason" varchar(500) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "finish_time" timestamptz(6),
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "capability" varchar(64) COLLATE "pg_catalog"."default",
  "channel_app_id" varchar(128) COLLATE "pg_catalog"."default",
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "store_no" varchar(32) COLLATE "pg_catalog"."default",
  "error_msg" varchar(500) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "relation_order_no" varchar(150) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_refund_order"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_refund_order"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_refund_order"."refund_no" IS '退款号(平台统一生成)';
COMMENT ON COLUMN "public"."pay_refund_order"."biz_refund_no" IS '商户退款号';
COMMENT ON COLUMN "public"."pay_refund_order"."title" IS '标题';
COMMENT ON COLUMN "public"."pay_refund_order"."trade_no" IS '原支付资金交易号(= pay_trade.trade_no，非容器 orderNo)';
COMMENT ON COLUMN "public"."pay_refund_order"."biz_order_no" IS '商户业务订单号';
COMMENT ON COLUMN "public"."pay_refund_order"."out_order_no" IS '通道支付订单号';
COMMENT ON COLUMN "public"."pay_refund_order"."out_refund_no" IS '通道退款流水号';
COMMENT ON COLUMN "public"."pay_refund_order"."amount" IS '退款金额(分)';
COMMENT ON COLUMN "public"."pay_refund_order"."order_amount" IS '订单总金额(冗余, 分)';
COMMENT ON COLUMN "public"."pay_refund_order"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_refund_order"."reason" IS '退款原因';
COMMENT ON COLUMN "public"."pay_refund_order"."status" IS '退款状态';
COMMENT ON COLUMN "public"."pay_refund_order"."finish_time" IS '退款完成时间';
COMMENT ON COLUMN "public"."pay_refund_order"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_refund_order"."product" IS '支付产品编码';
COMMENT ON COLUMN "public"."pay_refund_order"."channel_mch_no" IS '通道商户号(路由回填)';
COMMENT ON COLUMN "public"."pay_refund_order"."capability" IS '支付能力编码(路由回填)';
COMMENT ON COLUMN "public"."pay_refund_order"."channel_app_id" IS '通道应用 AppId 快照';
COMMENT ON COLUMN "public"."pay_refund_order"."notify_url" IS '异步通知地址';
COMMENT ON COLUMN "public"."pay_refund_order"."attach" IS '商户附加参数';
COMMENT ON COLUMN "public"."pay_refund_order"."client_ip" IS '客户端 IP';
COMMENT ON COLUMN "public"."pay_refund_order"."store_no" IS '门店号(继承自原支付容器, 可空)';
COMMENT ON COLUMN "public"."pay_refund_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_refund_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_refund_order"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_refund_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_refund_order"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_refund_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_refund_order"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_refund_order"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_refund_order"."trade_type" IS '原支付交易形态(冗余自 pay_trade.trade_type，如 normal/gateway)';
COMMENT ON COLUMN "public"."pay_refund_order"."relation_order_no" IS '实际上送通道的商户退款关联号(普通通道=refund_no；特殊通道可变形)';
COMMENT ON TABLE "public"."pay_refund_order" IS '退款订单';

-- ----------------------------
-- Table structure for pay_risk_hit
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_risk_hit";
CREATE TABLE "public"."pay_risk_hit" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "phase" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "hit_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "hit_value" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "blacklist_id" int8,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(64) COLLATE "pg_catalog"."default",
  "trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "order_no" varchar(64) COLLATE "pg_catalog"."default",
  "biz_order_no" varchar(128) COLLATE "pg_catalog"."default",
  "trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "method" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "openid" varchar(128) COLLATE "pg_catalog"."default",
  "buyer_id" varchar(128) COLLATE "pg_catalog"."default",
  "scene" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'unknown'::character varying,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "client_city" varchar(64) COLLATE "pg_catalog"."default",
  "store_city" varchar(64) COLLATE "pg_catalog"."default",
  "store_no" varchar(64) COLLATE "pg_catalog"."default",
  "geo_fence_strategy" varchar(16) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_risk_hit"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_risk_hit"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_risk_hit"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_risk_hit"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_risk_hit"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_risk_hit"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_risk_hit"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_risk_hit"."phase" IS '命中阶段: before_pay-事前拦截, after_pay-事后命中';
COMMENT ON COLUMN "public"."pay_risk_hit"."hit_type" IS '命中类型（与黑名单 type 一致: ip / alipay_user / wechat_openid / overseas_ip / province; 围栏命中: geo_fence）';
COMMENT ON COLUMN "public"."pay_risk_hit"."hit_value" IS '命中值快照（IP 或 openId）';
COMMENT ON COLUMN "public"."pay_risk_hit"."blacklist_id" IS '关联名单 ID（可空）';
COMMENT ON COLUMN "public"."pay_risk_hit"."mch_no" IS '商户号（业务字段，非租户行级隔离）';
COMMENT ON COLUMN "public"."pay_risk_hit"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_risk_hit"."trade_no" IS '平台交易号';
COMMENT ON COLUMN "public"."pay_risk_hit"."order_no" IS '容器单号';
COMMENT ON COLUMN "public"."pay_risk_hit"."biz_order_no" IS '商户业务单号';
COMMENT ON COLUMN "public"."pay_risk_hit"."trade_type" IS '交易类型: normal / gateway';
COMMENT ON COLUMN "public"."pay_risk_hit"."method" IS '支付方式';
COMMENT ON COLUMN "public"."pay_risk_hit"."product" IS '支付产品';
COMMENT ON COLUMN "public"."pay_risk_hit"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_risk_hit"."client_ip" IS '客户端 IP 快照';
COMMENT ON COLUMN "public"."pay_risk_hit"."openid" IS '下单 openId 快照';
COMMENT ON COLUMN "public"."pay_risk_hit"."buyer_id" IS '通道回写付款人标识（buyerId / openId）';
COMMENT ON COLUMN "public"."pay_risk_hit"."scene" IS '来源场景: api/gateway/code/manual/unknown';
COMMENT ON COLUMN "public"."pay_risk_hit"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_risk_hit"."client_city" IS '客户端 IP 归属城市（ip2region 解析快照）';
COMMENT ON COLUMN "public"."pay_risk_hit"."store_city" IS '门店所在城市（围栏命中快照）';
COMMENT ON COLUMN "public"."pay_risk_hit"."store_no" IS '门店号（围栏命中快照）';
COMMENT ON COLUMN "public"."pay_risk_hit"."geo_fence_strategy" IS '地理围栏命中时生效的策略（strict/balanced/loose）';
COMMENT ON TABLE "public"."pay_risk_hit" IS '支付风险命中记录（事前拦截与事后命中，供运营预警与处置）';

-- ----------------------------
-- Table structure for pay_route_basic_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_route_basic_config";
CREATE TABLE "public"."pay_route_basic_config" (
  "id" int8 NOT NULL,
  "strategy_id" int8 NOT NULL,
  "provider" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_route_basic_config"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_route_basic_config"."strategy_id" IS '路由策略ID';
COMMENT ON COLUMN "public"."pay_route_basic_config"."provider" IS '支付渠道';
COMMENT ON COLUMN "public"."pay_route_basic_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."pay_route_basic_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."pay_route_basic_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_route_basic_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."pay_route_basic_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_route_basic_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_route_basic_config"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_route_basic_config" IS '支付通道路由基础模式配置';

-- ----------------------------
-- Table structure for pay_route_scene_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_route_scene_config";
CREATE TABLE "public"."pay_route_scene_config" (
  "id" int8 NOT NULL,
  "strategy_id" int8 NOT NULL,
  "method" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "capability" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_route_scene_config"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_route_scene_config"."strategy_id" IS '路由策略ID';
COMMENT ON COLUMN "public"."pay_route_scene_config"."method" IS '支付方式编码';
COMMENT ON COLUMN "public"."pay_route_scene_config"."channel_mch_no" IS '通道商户号(唯一绑定支付产品)';
COMMENT ON COLUMN "public"."pay_route_scene_config"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."pay_route_scene_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."pay_route_scene_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_route_scene_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."pay_route_scene_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_route_scene_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_route_scene_config"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_route_scene_config" IS '支付通道路由场景模式配置';

-- ----------------------------
-- Table structure for pay_route_strategy
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_route_strategy";
CREATE TABLE "public"."pay_route_strategy" (
  "id" int8 NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "mode" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_route_strategy"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_route_strategy"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_route_strategy"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_route_strategy"."mode" IS '路由模式：basic / scene';
COMMENT ON COLUMN "public"."pay_route_strategy"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_route_strategy"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_route_strategy"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_route_strategy"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_route_strategy"."version" IS '版本号（乐观锁）';
COMMENT ON COLUMN "public"."pay_route_strategy"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_route_strategy" IS '支付通道路由策略';

-- ----------------------------
-- Table structure for pay_sync_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_sync_record";
CREATE TABLE "public"."pay_sync_record" (
  "id" int8 NOT NULL DEFAULT nextval('pay_sync_record_id_seq'::regclass),
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "trade_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "out_trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "out_trade_status" varchar(32) COLLATE "pg_catalog"."default",
  "trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "sync_info" text COLLATE "pg_catalog"."default",
  "adjust" bool NOT NULL DEFAULT false,
  "error_code" varchar(128) COLLATE "pg_catalog"."default",
  "error_msg" varchar(300) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6) DEFAULT now(),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_sync_record"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_sync_record"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_sync_record"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_sync_record"."trade_no" IS '平台交易号(对应 pay_trade.trade_no)';
COMMENT ON COLUMN "public"."pay_sync_record"."biz_trade_no" IS '商户业务单号(对应 pay_normal_order.biz_order_no)';
COMMENT ON COLUMN "public"."pay_sync_record"."out_trade_no" IS '通道交易号(三方通道返回的订单号)';
COMMENT ON COLUMN "public"."pay_sync_record"."out_trade_status" IS '通道返回的资金状态';
COMMENT ON COLUMN "public"."pay_sync_record"."trade_type" IS '交易类型';
COMMENT ON COLUMN "public"."pay_sync_record"."product" IS '支付产品编码';
COMMENT ON COLUMN "public"."pay_sync_record"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_sync_record"."sync_info" IS '网关返回的同步原始报文(json)';
COMMENT ON COLUMN "public"."pay_sync_record"."adjust" IS '本地与通道状态不一致时是否进行了调整';
COMMENT ON COLUMN "public"."pay_sync_record"."error_code" IS '错误码';
COMMENT ON COLUMN "public"."pay_sync_record"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_sync_record"."client_ip" IS '客户端 IP';
COMMENT ON COLUMN "public"."pay_sync_record"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_sync_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_sync_record"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_sync_record"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_sync_record"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_sync_record"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."pay_sync_record" IS '支付同步记录';

-- ----------------------------
-- Table structure for pay_terminal_channel_bind
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_terminal_channel_bind";
CREATE TABLE "public"."pay_terminal_channel_bind" (
  "id" int8 NOT NULL,
  "system_terminal_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_terminal_id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."system_terminal_no" IS '系统终端编码';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."channel_terminal_id" IS '通道终端主键';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_terminal_channel_bind"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_terminal_channel_bind" IS '系统终端与通道终端多对多绑定';

-- ----------------------------
-- Table structure for pay_terminal_device
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_terminal_device";
CREATE TABLE "public"."pay_terminal_device" (
  "id" int8 NOT NULL,
  "terminal_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "store_no" varchar(64) COLLATE "pg_catalog"."default",
  "enable" bool NOT NULL DEFAULT true,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_terminal_device"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_terminal_device"."terminal_no" IS '系统终端编码(平台生成)';
COMMENT ON COLUMN "public"."pay_terminal_device"."name" IS '终端名称';
COMMENT ON COLUMN "public"."pay_terminal_device"."store_no" IS '绑定门店号(可空, 门店1:N终端)';
COMMENT ON COLUMN "public"."pay_terminal_device"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."pay_terminal_device"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_terminal_device"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_terminal_device"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_terminal_device"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_terminal_device"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_terminal_device"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_terminal_device"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_terminal_device"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."pay_terminal_device" IS '系统终端(逻辑终端台账)';

-- ----------------------------
-- Table structure for pay_trade
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_trade";
CREATE TABLE "public"."pay_trade" (
  "id" int8 NOT NULL,
  "trade_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "trade_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "container_id" int8 NOT NULL,
  "amount" int8 NOT NULL,
  "currency" varchar(8) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CNY'::character varying,
  "posted_amount" int8 NOT NULL DEFAULT 0,
  "refundable_balance" int8 NOT NULL DEFAULT 0,
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "pay_time" timestamptz(6),
  "close_time" timestamptz(6),
  "source" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "store_no" varchar(32) COLLATE "pg_catalog"."default",
  "out_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "relation_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "provider" varchar(32) COLLATE "pg_catalog"."default",
  "title" varchar(255) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."pay_trade"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_trade"."trade_no" IS '资金交易号(平台生成)';
COMMENT ON COLUMN "public"."pay_trade"."trade_type" IS '交易形态(normal/gateway/authorize 等)';
COMMENT ON COLUMN "public"."pay_trade"."container_id" IS '关联业务容器ID';
COMMENT ON COLUMN "public"."pay_trade"."amount" IS '本次交易金额(最小货币单位/分)';
COMMENT ON COLUMN "public"."pay_trade"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_trade"."posted_amount" IS '入账金额(分; 结算类 SUCCESS 时=amount)';
COMMENT ON COLUMN "public"."pay_trade"."refundable_balance" IS '可退金额(分)';
COMMENT ON COLUMN "public"."pay_trade"."status" IS '资金状态';
COMMENT ON COLUMN "public"."pay_trade"."pay_time" IS '支付成功时间';
COMMENT ON COLUMN "public"."pay_trade"."close_time" IS '关闭时间';
COMMENT ON COLUMN "public"."pay_trade"."source" IS '订单来源(冗余自容器; 权威在容器)';
COMMENT ON COLUMN "public"."pay_trade"."channel_mch_no" IS '通道商户号(冗余自业务容器, 路由确定后写入)';
COMMENT ON COLUMN "public"."pay_trade"."store_no" IS '门店号(冗余自业务容器, 可空; 权威在容器)';
COMMENT ON COLUMN "public"."pay_trade"."out_order_no" IS '通道订单号';
COMMENT ON COLUMN "public"."pay_trade"."relation_order_no" IS '实际上送通道的商户订单号(反查权威)';
COMMENT ON COLUMN "public"."pay_trade"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_trade"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_trade"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_trade"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_trade"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_trade"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_trade"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_trade"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."pay_trade"."provider" IS '支付渠道(冗余自容器, 支付成功sync后回填; 权威在容器 provider)';
COMMENT ON COLUMN "public"."pay_trade"."title" IS '订单标题';
COMMENT ON COLUMN "public"."pay_trade"."channel" IS '支付通道';
COMMENT ON TABLE "public"."pay_trade" IS '资金交易凭证';

-- ----------------------------
-- Table structure for pay_transfer_order_alipay
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_transfer_order_alipay";
CREATE TABLE "public"."pay_transfer_order_alipay" (
  "id" int8 NOT NULL,
  "transfer_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_transfer_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "out_transfer_no" varchar(150) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "amount" int8 NOT NULL,
  "currency" varchar(8) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CNY'::character varying,
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "reason" varchar(200) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "finish_time" timestamptz(6),
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "req_time" timestamptz(6),
  "error_msg" varchar(2048) COLLATE "pg_catalog"."default",
  "payee_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "payee_account" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "payee_name" varchar(100) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."transfer_no" IS '平台转账单号';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."biz_transfer_no" IS '商户转账号(幂等键, 同一商户下唯一)';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."channel_mch_no" IS '通道商户号(路由确定后写入, 凭证组装用)';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."out_transfer_no" IS '通道转账单号(支付宝 order_id)';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."amount" IS '转账金额(最小货币单位/分)';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."title" IS '转账标题';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."reason" IS '转账原因/备注';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."status" IS '转账状态(processing/success/fail/close)';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."finish_time" IS '转账完成时间';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."notify_url" IS '商户异步通知地址';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."attach" IS '商户扩展参数(回调原样返回)';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."req_time" IS '请求时间';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."payee_type" IS '收款人账号类型(user_id/open_id/login_name)';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."payee_account" IS '收款人账号';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."payee_name" IS '收款人姓名';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_transfer_order_alipay" IS '支付宝转账单';

-- ----------------------------
-- Table structure for pay_transfer_order_douyin
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_transfer_order_douyin";
CREATE TABLE "public"."pay_transfer_order_douyin" (
  "id" int8 NOT NULL,
  "transfer_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_transfer_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "out_transfer_no" varchar(150) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "amount" int8 NOT NULL,
  "currency" varchar(8) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CNY'::character varying,
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "reason" varchar(200) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "finish_time" timestamptz(6),
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "req_time" timestamptz(6),
  "error_msg" varchar(2048) COLLATE "pg_catalog"."default",
  "payee_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "payee_account" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "payee_name" varchar(100) COLLATE "pg_catalog"."default",
  "transfer_scene" varchar(50) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."transfer_no" IS '平台转账单号';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."biz_transfer_no" IS '商户转账号(幂等键, 同一商户下唯一)';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."channel_mch_no" IS '通道商户号(路由确定后写入, 凭证组装用)';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."out_transfer_no" IS '通道转账单号(抖音 orderId)';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."amount" IS '转账金额(最小货币单位/分)';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."title" IS '转账标题';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."reason" IS '转账原因/备注';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."status" IS '转账状态(processing/success/fail/close)';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."finish_time" IS '转账完成时间';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."notify_url" IS '商户异步通知地址';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."attach" IS '商户扩展参数(回调原样返回)';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."req_time" IS '请求时间';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."payee_type" IS '收款人账号类型';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."payee_account" IS '收款人账号';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."payee_name" IS '收款人姓名';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_transfer_order_douyin" IS '抖音转账单';

-- ----------------------------
-- Table structure for pay_transfer_order_wechat
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_transfer_order_wechat";
CREATE TABLE "public"."pay_transfer_order_wechat" (
  "id" int8 NOT NULL,
  "transfer_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_transfer_no" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "out_transfer_no" varchar(150) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "amount" int8 NOT NULL,
  "currency" varchar(8) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CNY'::character varying,
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "reason" varchar(200) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "finish_time" timestamptz(6),
  "notify_url" varchar(200) COLLATE "pg_catalog"."default",
  "attach" varchar(500) COLLATE "pg_catalog"."default",
  "req_time" timestamptz(6),
  "error_msg" varchar(2048) COLLATE "pg_catalog"."default",
  "payee_openid" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "transfer_scene" varchar(50) COLLATE "pg_catalog"."default",
  "transfer_body" varchar(2000) COLLATE "pg_catalog"."default",
  "user_name" varchar(100) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."transfer_no" IS '平台转账单号';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."biz_transfer_no" IS '商户转账号(幂等键, 同一商户下唯一)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."channel_mch_no" IS '通道商户号(路由确定后写入, 凭证组装用)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."out_transfer_no" IS '通道转账单号(微信 paymentNo)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."amount" IS '转账金额(最小货币单位/分)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."title" IS '转账标题';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."reason" IS '转账原因/备注';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."status" IS '转账状态(processing/success/fail/close)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."finish_time" IS '转账完成时间';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."notify_url" IS '商户异步通知地址';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."attach" IS '商户扩展参数(回调原样返回)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."req_time" IS '请求时间';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."payee_openid" IS '收款人微信 openid';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."transfer_scene" IS '转账场景(冗余自通道商户配置)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."transfer_body" IS '拉起转账确认参数(微信二次确认)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."user_name" IS '收款人姓名(金额档位校验用)';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_transfer_order_wechat" IS '微信转账单';

-- ----------------------------
-- Table structure for pay_transfer_trade
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_transfer_trade";
CREATE TABLE "public"."pay_transfer_trade" (
  "id" int8 NOT NULL,
  "trade_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_transfer_no" varchar(100) COLLATE "pg_catalog"."default",
  "container_id" int8 NOT NULL,
  "container_channel" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "provider" varchar(32) COLLATE "pg_catalog"."default",
  "amount" int8 NOT NULL,
  "currency" varchar(8) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'CNY'::character varying,
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "out_transfer_no" varchar(150) COLLATE "pg_catalog"."default",
  "relation_no" varchar(150) COLLATE "pg_catalog"."default",
  "finish_time" timestamptz(6),
  "title" varchar(100) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."pay_transfer_trade"."trade_no" IS '平台转账交易号';
COMMENT ON COLUMN "public"."pay_transfer_trade"."biz_transfer_no" IS '商户转账号(冗余自容器, 同步记录/日志免回容器)';
COMMENT ON COLUMN "public"."pay_transfer_trade"."container_id" IS '关联通道转账单ID';
COMMENT ON COLUMN "public"."pay_transfer_trade"."container_channel" IS '所属通道(wechat/alipay/douyin)';
COMMENT ON COLUMN "public"."pay_transfer_trade"."channel" IS '通道编码(冗余, 跨通道统计)';
COMMENT ON COLUMN "public"."pay_transfer_trade"."provider" IS '钱包渠道(wechat/alipay/douyin)';
COMMENT ON COLUMN "public"."pay_transfer_trade"."amount" IS '转账金额(最小货币单位/分)';
COMMENT ON COLUMN "public"."pay_transfer_trade"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_transfer_trade"."status" IS '转账状态(processing/success/fail/close)';
COMMENT ON COLUMN "public"."pay_transfer_trade"."out_transfer_no" IS '通道转账单号';
COMMENT ON COLUMN "public"."pay_transfer_trade"."relation_no" IS '实际上送通道的商户转账号(反查权威)';
COMMENT ON COLUMN "public"."pay_transfer_trade"."finish_time" IS '转账完成时间';
COMMENT ON COLUMN "public"."pay_transfer_trade"."title" IS '转账标题';
COMMENT ON COLUMN "public"."pay_transfer_trade"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_transfer_trade"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_transfer_trade"."creator" IS '创建人';
COMMENT ON COLUMN "public"."pay_transfer_trade"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_transfer_trade"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."pay_transfer_trade"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_transfer_trade"."version" IS '乐观锁版本';
COMMENT ON COLUMN "public"."pay_transfer_trade"."deleted" IS '逻辑删除';
COMMENT ON TABLE "public"."pay_transfer_trade" IS '转账资金凭证';

-- ----------------------------
-- Table structure for starter_audit_login_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_audit_login_log";
CREATE TABLE "public"."starter_audit_login_log" (
  "id" int8 NOT NULL,
  "user_id" int8,
  "account" varchar(200) COLLATE "pg_catalog"."default",
  "login" bool DEFAULT false,
  "client" varchar(100) COLLATE "pg_catalog"."default",
  "login_type" varchar(100) COLLATE "pg_catalog"."default",
  "ip" varchar(100) COLLATE "pg_catalog"."default",
  "login_location" varchar(200) COLLATE "pg_catalog"."default",
  "browser" varchar(200) COLLATE "pg_catalog"."default",
  "os" varchar(200) COLLATE "pg_catalog"."default",
  "msg" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "login_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."starter_audit_login_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_login_log"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."starter_audit_login_log"."account" IS '用户账号';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login" IS '登录成功状态';
COMMENT ON COLUMN "public"."starter_audit_login_log"."client" IS '登录终端';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_type" IS '登录方式';
COMMENT ON COLUMN "public"."starter_audit_login_log"."ip" IS '登录IP地址';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_location" IS '登录地点';
COMMENT ON COLUMN "public"."starter_audit_login_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."starter_audit_login_log"."os" IS '操作系统';
COMMENT ON COLUMN "public"."starter_audit_login_log"."msg" IS '提示消息';
COMMENT ON COLUMN "public"."starter_audit_login_log"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."starter_audit_login_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."starter_audit_login_log"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."starter_audit_login_log"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."starter_audit_login_log"."version" IS '版本号';
COMMENT ON COLUMN "public"."starter_audit_login_log"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."starter_audit_login_log" IS '登录日志';

-- ----------------------------
-- Table structure for starter_audit_operate_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_audit_operate_log";
CREATE TABLE "public"."starter_audit_operate_log" (
  "id" int8 NOT NULL,
  "title" varchar(200) COLLATE "pg_catalog"."default",
  "operate_id" int8,
  "account" varchar(200) COLLATE "pg_catalog"."default",
  "client" varchar(100) COLLATE "pg_catalog"."default",
  "browser" varchar(200) COLLATE "pg_catalog"."default",
  "os" varchar(200) COLLATE "pg_catalog"."default",
  "business_type" varchar(100) COLLATE "pg_catalog"."default",
  "method" varchar(200) COLLATE "pg_catalog"."default",
  "request_method" varchar(20) COLLATE "pg_catalog"."default",
  "operate_url" varchar(500) COLLATE "pg_catalog"."default",
  "operate_ip" varchar(100) COLLATE "pg_catalog"."default",
  "operate_location" varchar(200) COLLATE "pg_catalog"."default",
  "operate_param" jsonb,
  "operate_return" jsonb,
  "success" bool DEFAULT false,
  "error_msg" varchar(1000) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "operate_time" timestamptz(6)
)
;
COMMENT ON COLUMN "public"."starter_audit_operate_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."title" IS '操作模块';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_id" IS '操作人员ID';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."account" IS '操作人员账号';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."client" IS '终端编码';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."browser" IS '浏览器类型';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."os" IS '操作系统';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."business_type" IS '业务类型';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."method" IS '请求方法';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."request_method" IS '请求方式';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_url" IS '请求URL';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_ip" IS '操作IP';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_location" IS '操作地点';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_param" IS '请求参数';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_return" IS '返回参数';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."success" IS '操作状态';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."error_msg" IS '错误消息';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."version" IS '版本号';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."starter_audit_operate_log" IS '操作日志';

-- ----------------------------
-- Table structure for starter_audit_unipay_log
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_audit_unipay_log";
CREATE TABLE "public"."starter_audit_unipay_log" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "api_path" varchar(256) COLLATE "pg_catalog"."default",
  "api_title" varchar(64) COLLATE "pg_catalog"."default",
  "request_method" varchar(16) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "request_ip" varchar(64) COLLATE "pg_catalog"."default",
  "request_location" varchar(128) COLLATE "pg_catalog"."default",
  "success" bool,
  "error_code" int4,
  "error_msg" varchar(512) COLLATE "pg_catalog"."default",
  "duration_ms" int8,
  "trace_id" varchar(64) COLLATE "pg_catalog"."default",
  "req_param" jsonb,
  "res_body" jsonb,
  "operate_time" timestamptz(6),
  "req_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."api_path" IS '接口路径';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."api_title" IS '接口标题';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."request_method" IS 'HTTP方法';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."client_ip" IS '商户入参声明的客户端IP';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."request_ip" IS '真实接入IP';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."request_location" IS '接入IP归属地';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."success" IS '是否成功';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."error_code" IS '业务错误码';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."duration_ms" IS '耗时毫秒';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."trace_id" IS '链路追踪ID';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."req_param" IS '请求参数(脱敏后)';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."res_body" IS '响应体(脱敏后)';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."operate_time" IS '操作时间UTC';
COMMENT ON TABLE "public"."starter_audit_unipay_log" IS '统一支付接口审计日志';

-- ----------------------------
-- Table structure for starter_platform_file_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."starter_platform_file_record";
CREATE TABLE "public"."starter_platform_file_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "size" int8,
  "filename" varchar(128) COLLATE "pg_catalog"."default",
  "original_filename" varchar(255) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default",
  "ext" varchar(16) COLLATE "pg_catalog"."default",
  "content_type" varchar(64) COLLATE "pg_catalog"."default",
  "access_type" varchar(16) COLLATE "pg_catalog"."default",
  "biz_type" varchar(32) COLLATE "pg_catalog"."default",
  "status" varchar(16) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."starter_platform_file_record"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_platform_file_record"."creator" IS '创建人';
COMMENT ON COLUMN "public"."starter_platform_file_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."starter_platform_file_record"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."starter_platform_file_record"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."starter_platform_file_record"."version" IS '乐观锁';
COMMENT ON COLUMN "public"."starter_platform_file_record"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."starter_platform_file_record"."size" IS '文件大小(字节)';
COMMENT ON COLUMN "public"."starter_platform_file_record"."filename" IS '文件名称(不含路径)';
COMMENT ON COLUMN "public"."starter_platform_file_record"."original_filename" IS '原始文件名';
COMMENT ON COLUMN "public"."starter_platform_file_record"."path" IS '存储路径(以/开头,不含文件名)';
COMMENT ON COLUMN "public"."starter_platform_file_record"."ext" IS '文件扩展名';
COMMENT ON COLUMN "public"."starter_platform_file_record"."content_type" IS 'MIME类型';
COMMENT ON COLUMN "public"."starter_platform_file_record"."access_type" IS '访问类型(public公开/private私有)';
COMMENT ON COLUMN "public"."starter_platform_file_record"."biz_type" IS '业务分类';
COMMENT ON COLUMN "public"."starter_platform_file_record"."status" IS '上传状态(pending待上传/uploaded已上传)';
COMMENT ON COLUMN "public"."starter_platform_file_record"."remark" IS '备注';
COMMENT ON TABLE "public"."starter_platform_file_record" IS '平台文件记录表';

-- ----------------------------
-- Table structure for stripe_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."stripe_channel_merchant";
CREATE TABLE "public"."stripe_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "account_id" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."stripe_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."account_id" IS 'Stripe 账户 ID(acct_xxx)';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."stripe_channel_merchant"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."stripe_channel_merchant" IS 'Stripe 直连通道商户绑定';

-- ----------------------------
-- Table structure for stripe_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."stripe_key_config";
CREATE TABLE "public"."stripe_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "secret_key" text COLLATE "pg_catalog"."default",
  "publishable_key" text COLLATE "pg_catalog"."default",
  "webhook_secret" text COLLATE "pg_catalog"."default",
  "sandbox" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."stripe_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."stripe_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."stripe_key_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."stripe_key_config"."secret_key" IS 'Stripe Secret Key(sk_test/sk_live, 加密存储)';
COMMENT ON COLUMN "public"."stripe_key_config"."publishable_key" IS 'Stripe Publishable Key(pk_test/pk_live, 加密存储)';
COMMENT ON COLUMN "public"."stripe_key_config"."webhook_secret" IS 'Webhook 签名密钥(whsec_xxx, 加密存储)';
COMMENT ON COLUMN "public"."stripe_key_config"."sandbox" IS '是否沙箱(test mode)';
COMMENT ON COLUMN "public"."stripe_key_config"."creator" IS '创建人';
COMMENT ON COLUMN "public"."stripe_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."stripe_key_config"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."stripe_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."stripe_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."stripe_key_config"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."stripe_key_config" IS 'Stripe 直连密钥配置';

-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_dict";
CREATE TABLE "public"."system_dict" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "dict_type" varchar(255) COLLATE "pg_catalog"."default",
  "code" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "enable" bool,
  "internal" bool,
  "i18n_key" varchar(200) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."system_dict"."id" IS '主键';
COMMENT ON COLUMN "public"."system_dict"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_dict"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_dict"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."system_dict"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_dict"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."system_dict"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."system_dict"."name" IS '名称';
COMMENT ON COLUMN "public"."system_dict"."dict_type" IS '字典类型';
COMMENT ON COLUMN "public"."system_dict"."code" IS '编码';
COMMENT ON COLUMN "public"."system_dict"."remark" IS '备注';
COMMENT ON COLUMN "public"."system_dict"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."system_dict"."internal" IS '是否内置';
COMMENT ON COLUMN "public"."system_dict"."i18n_key" IS '国际化key（有值时走语言包翻译）';
COMMENT ON TABLE "public"."system_dict" IS '字典表';

-- ----------------------------
-- Table structure for system_dict_item
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_dict_item";
CREATE TABLE "public"."system_dict_item" (
  "id" int8 NOT NULL,
  "dict_id" int8 NOT NULL,
  "dict_code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "code" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "sort_no" int4,
  "enable" bool DEFAULT true,
  "remark" varchar(500) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "i18n_key" varchar(200) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."system_dict_item"."id" IS '主键';
COMMENT ON COLUMN "public"."system_dict_item"."dict_id" IS '字典ID';
COMMENT ON COLUMN "public"."system_dict_item"."dict_code" IS '字典编码';
COMMENT ON COLUMN "public"."system_dict_item"."code" IS '字典项编码';
COMMENT ON COLUMN "public"."system_dict_item"."sort_no" IS '字典项排序';
COMMENT ON COLUMN "public"."system_dict_item"."enable" IS '是否启用';
COMMENT ON COLUMN "public"."system_dict_item"."remark" IS '备注';
COMMENT ON COLUMN "public"."system_dict_item"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_dict_item"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_dict_item"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."system_dict_item"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_dict_item"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_dict_item"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."system_dict_item"."i18n_key" IS '国际化key（有值时走语言包翻译）';
COMMENT ON TABLE "public"."system_dict_item" IS '字典项';

-- ----------------------------
-- Table structure for system_platform_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_platform_config";
CREATE TABLE "public"."system_platform_config" (
  "id" int8 NOT NULL,
  "config_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "config_name" varchar(100) COLLATE "pg_catalog"."default",
  "config_data" jsonb,
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "enabled" bool DEFAULT true,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."system_platform_config"."id" IS '主键';
COMMENT ON COLUMN "public"."system_platform_config"."config_type" IS '配置类型';
COMMENT ON COLUMN "public"."system_platform_config"."config_name" IS '配置名称';
COMMENT ON COLUMN "public"."system_platform_config"."config_data" IS '配置数据JSON格式';
COMMENT ON COLUMN "public"."system_platform_config"."description" IS '配置描述';
COMMENT ON COLUMN "public"."system_platform_config"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."system_platform_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_platform_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_platform_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."system_platform_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_platform_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_platform_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."system_platform_config" IS '系统平台统一配置';

-- ----------------------------
-- Table structure for system_platform_encrypt_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_platform_encrypt_config";
CREATE TABLE "public"."system_platform_encrypt_config" (
  "id" int8 NOT NULL,
  "config_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "config_name" varchar(100) COLLATE "pg_catalog"."default",
  "config_data" text COLLATE "pg_catalog"."default",
  "description" varchar(500) COLLATE "pg_catalog"."default",
  "enabled" bool DEFAULT true,
  "creator" int8,
  "create_time" timestamp(6),
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."id" IS '主键';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."config_type" IS '配置类型';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."config_name" IS '配置名称';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."config_data" IS '配置数据(加密存储)';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."description" IS '配置描述';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."creator" IS '创建者';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_platform_encrypt_config"."deleted" IS '是否删除';
COMMENT ON TABLE "public"."system_platform_encrypt_config" IS '系统平台加密配置表';

-- ----------------------------
-- Table structure for system_sensitive_word
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_sensitive_word";
CREATE TABLE "public"."system_sensitive_word" (
  "id" int8 NOT NULL,
  "word" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "category" varchar(32) COLLATE "pg_catalog"."default",
  "match_mode" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'contains'::character varying,
  "level" varchar(16) COLLATE "pg_catalog"."default" DEFAULT 'reject'::character varying,
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."system_sensitive_word"."id" IS '主键';
COMMENT ON COLUMN "public"."system_sensitive_word"."word" IS '敏感词原文(建议简体)';
COMMENT ON COLUMN "public"."system_sensitive_word"."category" IS '分类: politic/porn/violence/ad/custom';
COMMENT ON COLUMN "public"."system_sensitive_word"."match_mode" IS '匹配模式: contains/exact';
COMMENT ON COLUMN "public"."system_sensitive_word"."level" IS '处理级别: reject/warn';
COMMENT ON COLUMN "public"."system_sensitive_word"."status" IS '状态: enable/disable';
COMMENT ON COLUMN "public"."system_sensitive_word"."remark" IS '备注';
COMMENT ON COLUMN "public"."system_sensitive_word"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_sensitive_word"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_sensitive_word"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."system_sensitive_word"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_sensitive_word"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_sensitive_word"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."system_sensitive_word" IS '敏感词词库';

-- ----------------------------
-- Table structure for system_sensitive_word_hit
-- ----------------------------
DROP TABLE IF EXISTS "public"."system_sensitive_word_hit";
CREATE TABLE "public"."system_sensitive_word_hit" (
  "id" int8 NOT NULL,
  "word_id" int8,
  "hit_word" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "content_preview" varchar(200) COLLATE "pg_catalog"."default",
  "scene" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "source" varchar(32) COLLATE "pg_catalog"."default",
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(50) COLLATE "pg_catalog"."default",
  "operator_id" int8,
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "request_path" varchar(255) COLLATE "pg_catalog"."default",
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."id" IS '主键';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."word_id" IS '关联词库ID';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."hit_word" IS '命中词快照';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."content_preview" IS '原文摘要';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."scene" IS '场景: pay_title/goods_name/...';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."source" IS '来源: admin/merchant/unipay/app_admin';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."operator_id" IS '操作人用户ID';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."client_ip" IS '客户端IP';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."request_path" IS '请求路径';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."remark" IS '备注';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."version" IS '版本号';
COMMENT ON COLUMN "public"."system_sensitive_word_hit"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."system_sensitive_word_hit" IS '敏感词命中记录';

-- ----------------------------
-- Table structure for ums_direct_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."ums_direct_key_config";
CREATE TABLE "public"."ums_direct_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "ums_app_id" varchar(128) COLLATE "pg_catalog"."default",
  "app_key" text COLLATE "pg_catalog"."default",
  "secret_key" text COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "merchant_no" varchar(64) COLLATE "pg_catalog"."default",
  "terminal_no" varchar(64) COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."ums_direct_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."ums_direct_key_config"."channel_mch_no" IS '通道商户号(唯一关联)';
COMMENT ON COLUMN "public"."ums_direct_key_config"."ums_app_id" IS '银联商务应用 AppId';
COMMENT ON COLUMN "public"."ums_direct_key_config"."app_key" IS '应用密钥(HmacSHA256 签名密钥, 加密存储)';
COMMENT ON COLUMN "public"."ums_direct_key_config"."secret_key" IS '通讯密钥(回调验签 MD5/SHA256 拼接密钥, 加密存储)';
COMMENT ON COLUMN "public"."ums_direct_key_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."ums_direct_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."ums_direct_key_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."ums_direct_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."ums_direct_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."ums_direct_key_config"."deleted" IS '逻辑删除标志';
COMMENT ON COLUMN "public"."ums_direct_key_config"."merchant_no" IS '银联商务商户号(mid)';
COMMENT ON COLUMN "public"."ums_direct_key_config"."terminal_no" IS '终端号(tid)';
COMMENT ON COLUMN "public"."ums_direct_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON TABLE "public"."ums_direct_key_config" IS '银联商务直连密钥配置';

-- ----------------------------
-- Table structure for union_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."union_key_config";
CREATE TABLE "public"."union_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "mer_id" varchar(64) COLLATE "pg_catalog"."default",
  "sign_type" varchar(32) COLLATE "pg_catalog"."default",
  "cert_sign" bool DEFAULT true,
  "key_private_cert" text COLLATE "pg_catalog"."default",
  "key_private_cert_pwd" varchar(256) COLLATE "pg_catalog"."default",
  "acp_middle_cert" text COLLATE "pg_catalog"."default",
  "acp_root_cert" text COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."union_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."union_key_config"."channel_mch_no" IS '通道商户号(唯一关联)';
COMMENT ON COLUMN "public"."union_key_config"."mer_id" IS '银联商户号(merId)';
COMMENT ON COLUMN "public"."union_key_config"."sign_type" IS '签名类型(银联 ACP 固定 RSA2)';
COMMENT ON COLUMN "public"."union_key_config"."cert_sign" IS '是否证书签名';
COMMENT ON COLUMN "public"."union_key_config"."key_private_cert" IS '应用私钥证书(Base64 PKCS12, 加密存储)';
COMMENT ON COLUMN "public"."union_key_config"."key_private_cert_pwd" IS '私钥证书密码(加密存储)';
COMMENT ON COLUMN "public"."union_key_config"."acp_middle_cert" IS '中级证书(Base64 X.509)';
COMMENT ON COLUMN "public"."union_key_config"."acp_root_cert" IS '根证书(Base64 X.509)';
COMMENT ON COLUMN "public"."union_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON COLUMN "public"."union_key_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."union_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."union_key_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."union_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."union_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."union_key_config"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."union_key_config" IS '云闪付密钥配置';

-- ----------------------------
-- Table structure for vbill_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."vbill_isv_channel_merchant";
CREATE TABLE "public"."vbill_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "vbill_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false,
  "sandbox" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."mch_no" IS '平台商户号';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."channel_mch_no" IS '通道商户号(平台生成的唯一标识, VBILL+雪花)';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."product" IS '所属支付产品(对应 ProductEnum.code, 如 vbill_pay)';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."vbill_mch_no" IS '天阙商户号(mno)';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."vbill_isv_channel_merchant"."sandbox" IS '是否沙箱环境商户';
COMMENT ON TABLE "public"."vbill_isv_channel_merchant" IS '随行付通道商户绑定';

-- ----------------------------
-- Table structure for vbill_isv_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."vbill_isv_key_config";
CREATE TABLE "public"."vbill_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "org_id" varchar(64) COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool DEFAULT false
)
;
COMMENT ON COLUMN "public"."vbill_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."product" IS '支付产品编码(对应 ProductEnum.code, 如 vbill_pay)';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."org_id" IS '天阙合作机构ID(orgId)';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."public_key" IS '天阙RSA公钥(X509 Base64, 用于响应/回调验签, 加密存储)';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."private_key" IS '商户RSA私钥(PKCS8 Base64, SHA1withRSA 签名, 加密存储)';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."vbill_isv_key_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."vbill_isv_key_config" IS '随行付服务商密钥配置';

-- ----------------------------
-- Table structure for wechat_direct_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."wechat_direct_channel_merchant";
CREATE TABLE "public"."wechat_direct_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "wx_mch_id" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "transfer_scene" varchar(50) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."channel_mch_no" IS '通道商户号(系统生成雪花号)';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."wx_mch_id" IS '微信直连商户号';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."deleted" IS '逻辑删除标志';
COMMENT ON COLUMN "public"."wechat_direct_channel_merchant"."transfer_scene" IS '转账场景ID(商家转账到零钱, 未配置时发起转账报错)';
COMMENT ON TABLE "public"."wechat_direct_channel_merchant" IS '微信直连通道商户绑定';

-- ----------------------------
-- Table structure for wechat_direct_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."wechat_direct_key_config";
CREATE TABLE "public"."wechat_direct_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "api_key_v3" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "public_key_id" varchar(128) COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "private_cert" text COLLATE "pg_catalog"."default",
  "cert_serial_no" varchar(128) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
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
COMMENT ON TABLE "public"."wechat_direct_key_config" IS '微信直连密钥配置';

-- ----------------------------
-- Table structure for wechat_isv_channel_merchant
-- ----------------------------
DROP TABLE IF EXISTS "public"."wechat_isv_channel_merchant";
CREATE TABLE "public"."wechat_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "sub_mch_id" varchar(32) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."channel_mch_no" IS '通道商户号(WISV+雪花)';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."product" IS '所属支付产品';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."sub_mch_id" IS '微信特约商户号/二级商户号(V3服务商支付 sub_mchid)';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_channel_merchant"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."wechat_isv_channel_merchant" IS '微信服务商通道商户绑定';

-- ----------------------------
-- Table structure for wechat_isv_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."wechat_isv_key_config";
CREATE TABLE "public"."wechat_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "wx_mch_id" varchar(32) COLLATE "pg_catalog"."default",
  "api_key_v3" text COLLATE "pg_catalog"."default",
  "public_key" text COLLATE "pg_catalog"."default",
  "public_key_id" varchar(128) COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "private_cert" text COLLATE "pg_catalog"."default",
  "cert_serial_no" varchar(128) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
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
COMMENT ON TABLE "public"."wechat_isv_key_config" IS '微信服务商密钥配置';

-- ----------------------------
-- Table structure for wx_channel_app_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."wx_channel_app_capability";
CREATE TABLE "public"."wx_channel_app_capability" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "capability" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_scope" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "wx_app_ref_id" int8 NOT NULL
)
;
COMMENT ON COLUMN "public"."wx_channel_app_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."version" IS '版本号';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."app_scope" IS '应用档位：platform/merchant';
COMMENT ON COLUMN "public"."wx_channel_app_capability"."wx_app_ref_id" IS '微信应用主数据主键（由 app_scope 决定指向平台或商户表）';
COMMENT ON TABLE "public"."wx_channel_app_capability" IS '通道商户微信应用能力绑定（同能力可按档位双绑 platform+merchant）';

-- ----------------------------
-- Table structure for wx_mch_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."wx_mch_app";
CREATE TABLE "public"."wx_mch_app" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "wx_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_secret" varchar(512) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."wx_mch_app"."id" IS '主键';
COMMENT ON COLUMN "public"."wx_mch_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."wx_mch_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wx_mch_app"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."wx_mch_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wx_mch_app"."version" IS '版本号';
COMMENT ON COLUMN "public"."wx_mch_app"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."wx_mch_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wx_mch_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."wx_mch_app"."app_type" IS '应用类型：official_account/mini_program/mobile_app';
COMMENT ON COLUMN "public"."wx_mch_app"."wx_app_id" IS '微信应用AppId';
COMMENT ON COLUMN "public"."wx_mch_app"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON TABLE "public"."wx_mch_app" IS '商户微信应用（商户域开放平台身份，跨通道可引用）';

-- ----------------------------
-- Table structure for wx_platform_app
-- ----------------------------
DROP TABLE IF EXISTS "public"."wx_platform_app";
CREATE TABLE "public"."wx_platform_app" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "app_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "wx_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "app_secret" varchar(512) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."wx_platform_app"."id" IS '主键';
COMMENT ON COLUMN "public"."wx_platform_app"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."wx_platform_app"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wx_platform_app"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."wx_platform_app"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wx_platform_app"."version" IS '版本号';
COMMENT ON COLUMN "public"."wx_platform_app"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."wx_platform_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."wx_platform_app"."app_type" IS '应用类型：official_account/mini_program/mobile_app';
COMMENT ON COLUMN "public"."wx_platform_app"."wx_app_id" IS '微信应用AppId';
COMMENT ON COLUMN "public"."wx_platform_app"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON TABLE "public"."wx_platform_app" IS '平台微信应用（开放平台身份，跨通道可引用）';

-- ----------------------------
-- Table structure for wx_platform_app_capability
-- ----------------------------
DROP TABLE IF EXISTS "public"."wx_platform_app_capability";
CREATE TABLE "public"."wx_platform_app_capability" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "capability" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "wx_platform_app_id" int8 NOT NULL,
  "product" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "public"."wx_platform_app_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."version" IS '版本号';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."wx_platform_app_id" IS '平台微信应用ID';
COMMENT ON TABLE "public"."wx_platform_app_capability" IS '平台微信应用默认能力绑定（全局一能力一应用）';

-- ----------------------------
-- Table structure for yeepay_direct_key_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."yeepay_direct_key_config";
CREATE TABLE "public"."yeepay_direct_key_config" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "merchant_no" varchar(64) COLLATE "pg_catalog"."default",
  "yop_isv_no" varchar(64) COLLATE "pg_catalog"."default",
  "app_key" text COLLATE "pg_catalog"."default",
  "private_key" text COLLATE "pg_catalog"."default",
  "yop_public_key" text COLLATE "pg_catalog"."default",
  "wx_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "wx_app_secret" text COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."channel_mch_no" IS '通道商户号(创建时录入不可修改)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."merchant_no" IS '易宝商户号(merchantNo, 创建时录入不可修改)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."yop_isv_no" IS '易宝服务商商编(parentMerchantNo / yopIsvNo, 创建时录入不可修改)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."app_key" IS '通道应用 AppKey(YOP 应用标识, 加密存储)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."private_key" IS '商户 RSA 私钥(PEM PKCS#8, SDK 签名用, 加密存储)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."yop_public_key" IS '易宝平台 RSA 公钥(PEM, SDK 验签用, 加密存储)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."wx_app_id" IS '微信 AppId(微信 H5/JSAPI 场景用, 可空)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."wx_app_secret" IS '微信 AppSecret(微信场景用, 可空, 加密存储)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."yeepay_direct_key_config"."deleted" IS '删除标志';
COMMENT ON TABLE "public"."yeepay_direct_key_config" IS '易宝直连配置';

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."adapay_direct_key_config_id_seq"
OWNED BY "public"."adapay_direct_key_config"."id";
SELECT setval('"public"."adapay_direct_key_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."alipay_direct_app_capability_id_seq"
OWNED BY "public"."alipay_direct_app_capability"."id";
SELECT setval('"public"."alipay_direct_app_capability_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."hmpay_isv_channel_merchant_id_seq"
OWNED BY "public"."hmpay_isv_channel_merchant"."id";
SELECT setval('"public"."hmpay_isv_channel_merchant_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."hmpay_isv_key_config_id_seq"
OWNED BY "public"."hmpay_isv_key_config"."id";
SELECT setval('"public"."hmpay_isv_key_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."mch_app_notify_config_id_seq"
OWNED BY "public"."mch_app_notify_config"."id";
SELECT setval('"public"."mch_app_notify_config_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."mch_user_id_seq"
OWNED BY "public"."mch_user"."id";
SELECT setval('"public"."mch_user_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."pay_close_record_id_seq"
OWNED BY "public"."pay_close_record"."id";
SELECT setval('"public"."pay_close_record_id_seq"', 1, false);

-- ----------------------------
-- Alter sequences owned by
-- ----------------------------
ALTER SEQUENCE "public"."pay_sync_record_id_seq"
OWNED BY "public"."pay_sync_record"."id";
SELECT setval('"public"."pay_sync_record_id_seq"', 1, false);

-- ----------------------------
-- Indexes structure for table adapay_direct_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_adapay_direct_key_config_mch" ON "public"."adapay_direct_key_config" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_adapay_direct_key_config_mch" IS '同一通道商户密钥唯一';

-- ----------------------------
-- Primary Key structure for table adapay_direct_key_config
-- ----------------------------
ALTER TABLE "public"."adapay_direct_key_config" ADD CONSTRAINT "adapay_direct_key_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table alipay_direct_app
-- ----------------------------
ALTER TABLE "public"."alipay_direct_app" ADD CONSTRAINT "pk_alipay_direct_app" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table alipay_direct_app_auth_config
-- ----------------------------
ALTER TABLE "public"."alipay_direct_app_auth_config" ADD CONSTRAINT "pk_alipay_direct_app_auth_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table alipay_direct_app_capability
-- ----------------------------
CREATE UNIQUE INDEX "uk_alipay_direct_app_cap" ON "public"."alipay_direct_app_capability" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "capability" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_alipay_direct_app_cap" IS '同一通道商户下支付能力唯一，防重复开通';

-- ----------------------------
-- Primary Key structure for table alipay_direct_app_capability
-- ----------------------------
ALTER TABLE "public"."alipay_direct_app_capability" ADD CONSTRAINT "alipay_direct_app_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table alipay_direct_app_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_alipay_direct_app_key_sandbox" ON "public"."alipay_direct_app_key_config" USING btree (
  "alipay_direct_app_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_alipay_direct_app_key_sandbox" IS '同一应用同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table alipay_direct_app_key_config
-- ----------------------------
ALTER TABLE "public"."alipay_direct_app_key_config" ADD CONSTRAINT "pk_alipay_direct_app_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table alipay_direct_channel_merchant
-- ----------------------------
ALTER TABLE "public"."alipay_direct_channel_merchant" ADD CONSTRAINT "pk_alipay_direct_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table alipay_isv_app
-- ----------------------------
ALTER TABLE "public"."alipay_isv_app" ADD CONSTRAINT "pk_alipay_isv_app" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table alipay_isv_app_auth_config
-- ----------------------------
ALTER TABLE "public"."alipay_isv_app_auth_config" ADD CONSTRAINT "pk_alipay_isv_app_auth_config" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table alipay_isv_app_key_config
-- ----------------------------
ALTER TABLE "public"."alipay_isv_app_key_config" ADD CONSTRAINT "pk_alipay_isv_app_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table alipay_isv_channel_merchant
-- ----------------------------
ALTER TABLE "public"."alipay_isv_channel_merchant" ADD CONSTRAINT "pk_alipay_isv_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table base_area
-- ----------------------------
ALTER TABLE "public"."base_area" ADD CONSTRAINT "base_area_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Primary Key structure for table base_city
-- ----------------------------
ALTER TABLE "public"."base_city" ADD CONSTRAINT "base_city_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Primary Key structure for table base_province
-- ----------------------------
ALTER TABLE "public"."base_province" ADD CONSTRAINT "base_province_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Indexes structure for table base_street
-- ----------------------------
CREATE INDEX "idx_base_street_area_code" ON "public"."base_street" USING btree (
  "area_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_base_street_area_code" IS '按区县编码查询街道';

-- ----------------------------
-- Primary Key structure for table base_street
-- ----------------------------
ALTER TABLE "public"."base_street" ADD CONSTRAINT "base_street_pkey" PRIMARY KEY ("code");

-- ----------------------------
-- Indexes structure for table base_user_protocol
-- ----------------------------
CREATE INDEX "idx_base_user_protocol_type_client" ON "public"."base_user_protocol" USING btree (
  "type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "client_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_base_user_protocol_type_client" IS '按协议类型与终端类型查询';

-- ----------------------------
-- Primary Key structure for table base_user_protocol
-- ----------------------------
ALTER TABLE "public"."base_user_protocol" ADD CONSTRAINT "base_user_protocol_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table base_user_protocol_version
-- ----------------------------
CREATE INDEX "idx_user_protocol_version_protocol" ON "public"."base_user_protocol_version" USING btree (
  "protocol_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_user_protocol_version_protocol" IS '按协议 ID 查询版本列表';
CREATE UNIQUE INDEX "uk_user_protocol_version_published" ON "public"."base_user_protocol_version" USING btree (
  "protocol_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "language" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE status::text = 'PUBLISHED'::text AND deleted = false;
COMMENT ON INDEX "public"."uk_user_protocol_version_published" IS '同一协议同一语言仅一个已发布版本（已发布）';

-- ----------------------------
-- Primary Key structure for table base_user_protocol_version
-- ----------------------------
ALTER TABLE "public"."base_user_protocol_version" ADD CONSTRAINT "base_user_protocol_version_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table device_qr_code
-- ----------------------------
CREATE INDEX "idx_device_qr_code_store_no" ON "public"."device_qr_code" USING btree (
  "store_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_device_qr_code_store_no" IS '按门店号查询二维码设备';
CREATE UNIQUE INDEX "uk_device_qr_code_code" ON "public"."device_qr_code" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."uk_device_qr_code_code" IS '二维码设备编码唯一';

-- ----------------------------
-- Primary Key structure for table device_qr_code
-- ----------------------------
ALTER TABLE "public"."device_qr_code" ADD CONSTRAINT "device_qr_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table douyin_direct_channel_merchant
-- ----------------------------
CREATE UNIQUE INDEX "uk_douyin_direct_mch_dyid" ON "public"."douyin_direct_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "dy_mch_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_douyin_direct_mch_dyid" IS '同一商户下抖音商户号唯一';

-- ----------------------------
-- Primary Key structure for table douyin_direct_channel_merchant
-- ----------------------------
ALTER TABLE "public"."douyin_direct_channel_merchant" ADD CONSTRAINT "pk_douyin_direct_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table douyin_direct_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_douyin_direct_key_cmchno" ON "public"."douyin_direct_key_config" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_douyin_direct_key_cmchno" IS '同一通道商户密钥配置唯一';

-- ----------------------------
-- Primary Key structure for table douyin_direct_key_config
-- ----------------------------
ALTER TABLE "public"."douyin_direct_key_config" ADD CONSTRAINT "pk_douyin_direct_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table dy_channel_app_capability
-- ----------------------------
CREATE INDEX "idx_dy_channel_app_cap_mch" ON "public"."dy_channel_app_capability" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_dy_channel_app_cap_mch" IS '商户号关联查询';
CREATE INDEX "idx_dy_channel_app_cap_ref" ON "public"."dy_channel_app_capability" USING btree (
  "app_scope" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "dy_app_ref_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_dy_channel_app_cap_ref" IS '按应用范围+应用引用查询能力';
CREATE UNIQUE INDEX "uk_dy_channel_app_cap" ON "public"."dy_channel_app_capability" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "capability" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_scope" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_dy_channel_app_cap" IS '通道商户+能力+应用范围唯一';

-- ----------------------------
-- Primary Key structure for table dy_channel_app_capability
-- ----------------------------
ALTER TABLE "public"."dy_channel_app_capability" ADD CONSTRAINT "dy_channel_app_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table dy_mch_app
-- ----------------------------
CREATE INDEX "idx_dy_mch_app_app_type" ON "public"."dy_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_dy_mch_app_app_type" IS '按商户号+应用类型查询';
CREATE INDEX "idx_dy_mch_app_mch_no" ON "public"."dy_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_dy_mch_app_mch_no" IS '商户号关联查询';
CREATE UNIQUE INDEX "uk_dy_mch_app_mch_douyin" ON "public"."dy_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "douyin_app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_dy_mch_app_mch_douyin" IS '同一商户下抖音 AppID 唯一';

-- ----------------------------
-- Primary Key structure for table dy_mch_app
-- ----------------------------
ALTER TABLE "public"."dy_mch_app" ADD CONSTRAINT "dy_mch_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table dy_platform_app
-- ----------------------------
CREATE INDEX "idx_dy_platform_app_app_type" ON "public"."dy_platform_app" USING btree (
  "app_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_dy_platform_app_app_type" IS '按应用类型筛选';
CREATE UNIQUE INDEX "uk_dy_platform_app_dy_app_id" ON "public"."dy_platform_app" USING btree (
  "douyin_app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_dy_platform_app_dy_app_id" IS '抖音平台 AppID 唯一';

-- ----------------------------
-- Primary Key structure for table dy_platform_app
-- ----------------------------
ALTER TABLE "public"."dy_platform_app" ADD CONSTRAINT "dy_platform_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table dy_platform_app_capability
-- ----------------------------
CREATE UNIQUE INDEX "uk_dy_platform_app_cap_product" ON "public"."dy_platform_app_capability" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "capability" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_dy_platform_app_cap_product" IS '产品+能力唯一';

-- ----------------------------
-- Primary Key structure for table dy_platform_app_capability
-- ----------------------------
ALTER TABLE "public"."dy_platform_app_capability" ADD CONSTRAINT "dy_platform_app_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table fuyou_isv_channel_merchant
-- ----------------------------
CREATE UNIQUE INDEX "uk_fuyou_isv_channel_mch_no" ON "public"."fuyou_isv_channel_merchant" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_fuyou_isv_channel_mch_no" IS '通道商户号唯一';
CREATE UNIQUE INDEX "uk_fuyou_isv_mch_fuyou_no" ON "public"."fuyou_isv_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "fuyou_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_fuyou_isv_mch_fuyou_no" IS '同一商户下富友商户号唯一';

-- ----------------------------
-- Primary Key structure for table fuyou_isv_channel_merchant
-- ----------------------------
ALTER TABLE "public"."fuyou_isv_channel_merchant" ADD CONSTRAINT "pk_fuyou_isv_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table fuyou_isv_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_fuyou_isv_key_prod_sandbox" ON "public"."fuyou_isv_key_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_fuyou_isv_key_prod_sandbox" IS '同一产品同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table fuyou_isv_key_config
-- ----------------------------
ALTER TABLE "public"."fuyou_isv_key_config" ADD CONSTRAINT "pk_fuyou_isv_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table hkrt_isv_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_hkrt_isv_key_prod_sandbox" ON "public"."hkrt_isv_key_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_hkrt_isv_key_prod_sandbox" IS '同一产品同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table hmpay_isv_channel_merchant
-- ----------------------------
ALTER TABLE "public"."hmpay_isv_channel_merchant" ADD CONSTRAINT "hmpay_isv_channel_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table hmpay_isv_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_hmpay_isv_key_prod_sandbox" ON "public"."hmpay_isv_key_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_hmpay_isv_key_prod_sandbox" IS '同一产品同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table hmpay_isv_key_config
-- ----------------------------
ALTER TABLE "public"."hmpay_isv_key_config" ADD CONSTRAINT "hmpay_isv_key_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_perm_code
-- ----------------------------
CREATE UNIQUE INDEX "uk_iam_perm_code_code" ON "public"."iam_perm_code" USING btree (
  "code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_iam_perm_code_code" IS '权限码表编码唯一索引';

-- ----------------------------
-- Primary Key structure for table iam_perm_code
-- ----------------------------
ALTER TABLE "public"."iam_perm_code" ADD CONSTRAINT "iam_perm_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_perm_menu
-- ----------------------------
ALTER TABLE "public"."iam_perm_menu" ADD CONSTRAINT "iam_perm_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_role
-- ----------------------------
ALTER TABLE "public"."iam_role" ADD CONSTRAINT "iam_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_role_code
-- ----------------------------
CREATE INDEX "idx_iam_role_code_role_id" ON "public"."iam_role_code" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_iam_role_code_role_id" IS '角色权限码关联表角色ID索引';

-- ----------------------------
-- Primary Key structure for table iam_role_code
-- ----------------------------
ALTER TABLE "public"."iam_role_code" ADD CONSTRAINT "iam_role_code_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_role_menu
-- ----------------------------
CREATE INDEX "idx_role_menu_role_client" ON "public"."iam_role_menu" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_role_menu_role_client" IS '角色菜单表按角色ID和终端编码的普通索引';

-- ----------------------------
-- Primary Key structure for table iam_role_menu
-- ----------------------------
ALTER TABLE "public"."iam_role_menu" ADD CONSTRAINT "iam_role_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table iam_social_login_config
-- ----------------------------
ALTER TABLE "public"."iam_social_login_config" ADD CONSTRAINT "uk_iam_social_config_source" UNIQUE ("source");

-- ----------------------------
-- Primary Key structure for table iam_social_login_config
-- ----------------------------
ALTER TABLE "public"."iam_social_login_config" ADD CONSTRAINT "iam_social_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_dashboard_preference
-- ----------------------------
CREATE UNIQUE INDEX "uk_user_das_pref_user_client" ON "public"."iam_user_dashboard_preference" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_user_das_pref_user_client" IS '同一用户同一终端仪表盘偏好唯一';

-- ----------------------------
-- Primary Key structure for table iam_user_dashboard_preference
-- ----------------------------
ALTER TABLE "public"."iam_user_dashboard_preference" ADD CONSTRAINT "iam_user_dashboard_preference_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_expand_info
-- ----------------------------
ALTER TABLE "public"."iam_user_expand_info" ADD CONSTRAINT "iam_user_expand_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_info
-- ----------------------------
CREATE INDEX "idx_iam_user_info_client_account" ON "public"."iam_user_info" USING btree (
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "account" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_iam_user_info_client_account" IS '用户信息表终端账号索引';
CREATE INDEX "idx_iam_user_info_client_email" ON "public"."iam_user_info" USING btree (
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false AND email IS NOT NULL AND email::text <> ''::text;
COMMENT ON INDEX "public"."idx_iam_user_info_client_email" IS '用户信息表终端邮箱索引';
CREATE INDEX "idx_iam_user_info_client_phone" ON "public"."iam_user_info" USING btree (
  "client_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "phone" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false AND phone IS NOT NULL AND phone::text <> ''::text;
COMMENT ON INDEX "public"."idx_iam_user_info_client_phone" IS '用户信息表终端手机号索引';

-- ----------------------------
-- Primary Key structure for table iam_user_info
-- ----------------------------
ALTER TABLE "public"."iam_user_info" ADD CONSTRAINT "iam_user_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_password_history
-- ----------------------------
CREATE INDEX "idx_password_history_user_id" ON "public"."iam_user_password_history" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_password_history_user_id" IS '用户ID索引';

-- ----------------------------
-- Primary Key structure for table iam_user_password_history
-- ----------------------------
ALTER TABLE "public"."iam_user_password_history" ADD CONSTRAINT "iam_user_password_history_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table iam_user_password_security
-- ----------------------------
ALTER TABLE "public"."iam_user_password_security" ADD CONSTRAINT "iam_user_password_security_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_role
-- ----------------------------
CREATE INDEX "idx_iam_user_role_user_id" ON "public"."iam_user_role" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_iam_user_role_user_id" IS '用户角色关联表用户ID索引';

-- ----------------------------
-- Primary Key structure for table iam_user_role
-- ----------------------------
ALTER TABLE "public"."iam_user_role" ADD CONSTRAINT "iam_user_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_social
-- ----------------------------
CREATE INDEX "idx_iam_user_social_user_id" ON "public"."iam_user_social" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_iam_user_social_user_id" IS '按用户 ID 查询第三方账号绑定';
CREATE UNIQUE INDEX "uk_iam_user_social_source_open_id" ON "public"."iam_user_social" USING btree (
  "source" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "open_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_iam_user_social_source_open_id" IS '同一第三方来源的 OpenID 唯一绑定';

-- ----------------------------
-- Primary Key structure for table iam_user_social
-- ----------------------------
ALTER TABLE "public"."iam_user_social" ADD CONSTRAINT "pk_iam_user_social" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table iam_user_two_factor
-- ----------------------------
CREATE UNIQUE INDEX "uk_iam_user_two_factor_user_id" ON "public"."iam_user_two_factor" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_iam_user_two_factor_user_id" IS '同一用户两步验证配置唯一';

-- ----------------------------
-- Primary Key structure for table iam_user_two_factor
-- ----------------------------
ALTER TABLE "public"."iam_user_two_factor" ADD CONSTRAINT "iam_user_two_factor_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table lakala_isv_channel_merchant
-- ----------------------------
CREATE UNIQUE INDEX "uk_lakala_isv_channel_mch_no" ON "public"."lakala_isv_channel_merchant" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_lakala_isv_channel_mch_no" IS '通道商户号唯一';
CREATE UNIQUE INDEX "uk_lakala_isv_mch_lakala_no" ON "public"."lakala_isv_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "lakala_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_lakala_isv_mch_lakala_no" IS '同一商户下拉卡拉商户号唯一';

-- ----------------------------
-- Primary Key structure for table lakala_isv_channel_merchant
-- ----------------------------
ALTER TABLE "public"."lakala_isv_channel_merchant" ADD CONSTRAINT "lakala_isv_channel_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table lakala_isv_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_lakala_isv_key_prod_sandbox" ON "public"."lakala_isv_key_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_lakala_isv_key_prod_sandbox" IS '同一产品同一环境密钥唯一';
CREATE UNIQUE INDEX "uk_lakala_isv_key_product" ON "public"."lakala_isv_key_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_lakala_isv_key_product" IS '同一产品密钥唯一（疑似冗余，见 §6）';

-- ----------------------------
-- Primary Key structure for table lakala_isv_key_config
-- ----------------------------
ALTER TABLE "public"."lakala_isv_key_config" ADD CONSTRAINT "lakala_isv_key_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table leshua_isv_channel_merchant
-- ----------------------------
CREATE UNIQUE INDEX "uk_leshua_isv_channel_mch_no" ON "public"."leshua_isv_channel_merchant" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_leshua_isv_channel_mch_no" IS '通道商户号唯一';
CREATE UNIQUE INDEX "uk_leshua_isv_mch_ls_no" ON "public"."leshua_isv_channel_merchant" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "ls_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_leshua_isv_mch_ls_no" IS '同一商户下乐刷商户号唯一';

-- ----------------------------
-- Primary Key structure for table leshua_isv_channel_merchant
-- ----------------------------
ALTER TABLE "public"."leshua_isv_channel_merchant" ADD CONSTRAINT "pk_leshua_isv_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table leshua_isv_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_leshua_isv_key_prod_sandbox" ON "public"."leshua_isv_key_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_leshua_isv_key_prod_sandbox" IS '同一产品同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table leshua_isv_key_config
-- ----------------------------
ALTER TABLE "public"."leshua_isv_key_config" ADD CONSTRAINT "pk_leshua_isv_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_app_info
-- ----------------------------
CREATE INDEX "idx_mch_app_info_mch_no" ON "public"."mch_app_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_app_info_mch_no" IS '商户号索引';
CREATE UNIQUE INDEX "uk_mch_app_info_app_id" ON "public"."mch_app_info" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."uk_mch_app_info_app_id" IS '应用号唯一索引';
CREATE UNIQUE INDEX "uk_mch_app_info_default" ON "public"."mch_app_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE default_app = true AND deleted = false;
COMMENT ON INDEX "public"."uk_mch_app_info_default" IS '每商户仅一个默认应用（默认应用）';

-- ----------------------------
-- Primary Key structure for table mch_app_info
-- ----------------------------
ALTER TABLE "public"."mch_app_info" ADD CONSTRAINT "mch_app_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_app_notify_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_mch_app_notify_config" ON "public"."mch_app_notify_config" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_mch_app_notify_config" IS '同一应用仅一条通知配置';

-- ----------------------------
-- Primary Key structure for table mch_app_notify_config
-- ----------------------------
ALTER TABLE "public"."mch_app_notify_config" ADD CONSTRAINT "pk_mch_app_notify_config" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table mch_channel_merchant
-- ----------------------------
ALTER TABLE "public"."mch_channel_merchant" ADD CONSTRAINT "pk_mch_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_credential
-- ----------------------------
CREATE INDEX "idx_mch_credential_mch_no" ON "public"."mch_credential" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_credential_mch_no" IS '商户号索引';

-- ----------------------------
-- Primary Key structure for table mch_credential
-- ----------------------------
ALTER TABLE "public"."mch_credential" ADD CONSTRAINT "mch_credential_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_info
-- ----------------------------
CREATE INDEX "idx_mch_info_mch_no" ON "public"."mch_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_info_mch_no" IS '商户号关联查询';
CREATE INDEX "idx_mch_info_status" ON "public"."mch_info" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_info_status" IS '状态筛选';

-- ----------------------------
-- Primary Key structure for table mch_info
-- ----------------------------
ALTER TABLE "public"."mch_info" ADD CONSTRAINT "mch_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key / Indexes structure for table mch_risk_config
-- ----------------------------
ALTER TABLE "public"."mch_risk_config" ADD CONSTRAINT "mch_risk_config_pkey" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "uk_mch_risk_config_mch_no" ON "public"."mch_risk_config" USING btree ("mch_no");
COMMENT ON INDEX "uk_mch_risk_config_mch_no" IS '同一商户风控配置唯一（1:1 商户）';

-- ----------------------------
-- Primary Key structure for table base_city_adjacent
-- ----------------------------
ALTER TABLE "public"."base_city_adjacent" ADD CONSTRAINT "base_city_adjacent_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table base_city_adjacent
-- ----------------------------
CREATE UNIQUE INDEX "uk_base_city_adjacent" ON "public"."base_city_adjacent" USING btree ("city_code", "adjacent_city_code");
COMMENT ON INDEX "uk_base_city_adjacent" IS '同一城市与其相邻城市关系唯一（防重复灌入）';

-- ----------------------------
-- Indexes structure for table mch_notice_record
-- ----------------------------
CREATE INDEX "idx_mch_notice_record_create_time" ON "public"."mch_notice_record" USING btree (
  "create_time" "pg_catalog"."timestamptz_ops" DESC NULLS FIRST
);
COMMENT ON INDEX "public"."idx_mch_notice_record_create_time" IS '按时间倒序分页/范围扫描（通知投递记录高频）';
CREATE INDEX "idx_mch_notice_record_task_id" ON "public"."mch_notice_record" USING btree (
  "task_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_notice_record_task_id" IS '按通知任务 ID 查询投递记录';

-- ----------------------------
-- Primary Key structure for table mch_notice_record
-- ----------------------------
ALTER TABLE "public"."mch_notice_record" ADD CONSTRAINT "mch_notice_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_notice_task
-- ----------------------------
CREATE INDEX "idx_mch_notice_task_biz_no" ON "public"."mch_notice_task" USING btree (
  "biz_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_notice_task_biz_no" IS '按业务单号查询通知任务';
CREATE INDEX "idx_mch_notice_task_create_time" ON "public"."mch_notice_task" USING btree (
  "create_time" "pg_catalog"."timestamptz_ops" DESC NULLS FIRST
);
COMMENT ON INDEX "public"."idx_mch_notice_task_create_time" IS '按时间倒序分页/范围扫描（通知任务高频）';
CREATE INDEX "idx_mch_notice_task_next_time" ON "public"."mch_notice_task" USING btree (
  "next_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false AND success = false;
COMMENT ON INDEX "public"."idx_mch_notice_task_next_time" IS '兜底扫描: 未成功且 next_time 已到的任务';
CREATE INDEX "idx_mch_notice_task_success" ON "public"."mch_notice_task" USING btree (
  "success" "pg_catalog"."bool_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_notice_task_success" IS '按投递结果筛选（成功/失败统计）';
CREATE UNIQUE INDEX "uk_mch_notice_task_biz" ON "public"."mch_notice_task" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "event" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "biz_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "transport" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "format" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "source" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_mch_notice_task_biz" IS '商户+应用+事件+业务号+传输通道+报文格式+来源维度的幂等唯一约束';

-- ----------------------------
-- Primary Key structure for table mch_notice_task
-- ----------------------------
ALTER TABLE "public"."mch_notice_task" ADD CONSTRAINT "mch_notice_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_store_info
-- ----------------------------
CREATE INDEX "idx_mch_store_info_mch_default" ON "public"."mch_store_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "default_store" "pg_catalog"."bool_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_store_info_mch_default" IS '按商户号查默认门店';
CREATE INDEX "idx_mch_store_info_mch_no" ON "public"."mch_store_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "deleted" "pg_catalog"."bool_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_store_info_mch_no" IS '按商户号查询门店列表';
CREATE UNIQUE INDEX "uk_mch_store_info_default" ON "public"."mch_store_info" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE default_store = true AND deleted = false;
COMMENT ON INDEX "public"."uk_mch_store_info_default" IS '每商户仅一个默认门店（默认门店）';
CREATE UNIQUE INDEX "uk_mch_store_info_store_no" ON "public"."mch_store_info" USING btree (
  "store_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."uk_mch_store_info_store_no" IS '门店号唯一';

-- ----------------------------
-- Primary Key structure for table mch_store_info
-- ----------------------------
ALTER TABLE "public"."mch_store_info" ADD CONSTRAINT "mch_store_info_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Uniques structure for table mch_user
-- ----------------------------
ALTER TABLE "public"."mch_user" ADD CONSTRAINT "mch_user_mch_no_user_id_key" UNIQUE ("mch_no", "user_id");

-- ----------------------------
-- Primary Key structure for table mch_user
-- ----------------------------
ALTER TABLE "public"."mch_user" ADD CONSTRAINT "mch_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table mch_wx_domain_verify
-- ----------------------------
CREATE INDEX "idx_mch_wx_domain_verify_mch_no" ON "public"."mch_wx_domain_verify" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_wx_domain_verify_mch_no" IS '商户号关联查询';
CREATE INDEX "idx_mch_wx_domain_verify_platform" ON "public"."mch_wx_domain_verify" USING btree (
  "platform" "pg_catalog"."bool_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_mch_wx_domain_verify_platform" IS '按平台筛选';
CREATE UNIQUE INDEX "uk_mch_wx_domain_verify_code" ON "public"."mch_wx_domain_verify" USING btree (
  "verify_code" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_mch_wx_domain_verify_code" IS '域名校验码唯一';

-- ----------------------------
-- Primary Key structure for table mch_wx_domain_verify
-- ----------------------------
ALTER TABLE "public"."mch_wx_domain_verify" ADD CONSTRAINT "pk_mch_wx_domain_verify" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table notify_message
-- ----------------------------
CREATE INDEX "idx_notify_message_user" ON "public"."notify_message" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "deleted" "pg_catalog"."bool_ops" ASC NULLS LAST,
  "is_read" "pg_catalog"."bool_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_notify_message_user" IS '按用户查询站内信（含已读/未读筛选）';

-- ----------------------------
-- Primary Key structure for table notify_message
-- ----------------------------
ALTER TABLE "public"."notify_message" ADD CONSTRAINT "notify_message_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table notify_notice
-- ----------------------------
CREATE INDEX "idx_notify_notice_status" ON "public"."notify_notice" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "deleted" "pg_catalog"."bool_ops" ASC NULLS LAST,
  "effective_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST,
  "expire_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_notify_notice_status" IS '按状态+生效/失效时间筛选有效公告';

-- ----------------------------
-- Primary Key structure for table notify_notice
-- ----------------------------
ALTER TABLE "public"."notify_notice" ADD CONSTRAINT "notify_notice_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table notify_notice_read
-- ----------------------------
CREATE INDEX "idx_notify_notice_read_user" ON "public"."notify_notice_read" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_notify_notice_read_user" IS '按用户查询公告阅读记录';

-- ----------------------------
-- Uniques structure for table notify_notice_read
-- ----------------------------
ALTER TABLE "public"."notify_notice_read" ADD CONSTRAINT "uk_notify_notice_read" UNIQUE ("user_id", "notice_id");

-- ----------------------------
-- Primary Key structure for table notify_notice_read
-- ----------------------------
ALTER TABLE "public"."notify_notice_read" ADD CONSTRAINT "notify_notice_read_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_blacklist
-- ----------------------------
CREATE INDEX "idx_pay_blacklist_status" ON "public"."pay_blacklist" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_blacklist_status" IS '状态筛选（启用/停用）';
CREATE INDEX "idx_pay_blacklist_type_value" ON "public"."pay_blacklist" USING btree (
  "type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "value" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_blacklist_type_value" IS '按类型+值查询黑名单';
CREATE UNIQUE INDEX "uk_pay_blacklist_type_value_app" ON "public"."pay_blacklist" USING btree (
  "type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "value" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  COALESCE(wx_app_id, ''::character varying) COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."uk_pay_blacklist_type_value_app" IS '类型+值+微信应用维度唯一约束（微信应用为空按空串参与唯一性）';

-- ----------------------------
-- Primary Key structure for table pay_blacklist
-- ----------------------------
ALTER TABLE "public"."pay_blacklist" ADD CONSTRAINT "pay_blacklist_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_callback_record
-- ----------------------------
CREATE INDEX "idx_pay_callback_record_channel_mch_no" ON "public"."pay_callback_record" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_callback_record_channel_mch_no" IS '按通道商户号查询回调';
CREATE INDEX "idx_pay_callback_record_create_time" ON "public"."pay_callback_record" USING btree (
  "create_time" "pg_catalog"."timestamptz_ops" DESC NULLS FIRST
);
COMMENT ON INDEX "public"."idx_pay_callback_record_create_time" IS '按时间倒序分页/范围扫描（回调流水高频）';
CREATE INDEX "idx_pay_callback_record_mch_no" ON "public"."pay_callback_record" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_callback_record_mch_no" IS '商户号关联查询';
CREATE INDEX "idx_pay_callback_record_out_trade_no" ON "public"."pay_callback_record" USING btree (
  "out_trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_callback_record_out_trade_no" IS '按商户订单号查询回调';
CREATE INDEX "idx_pay_callback_record_product" ON "public"."pay_callback_record" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_callback_record_product" IS '按支付产品筛选';
CREATE INDEX "idx_pay_callback_record_trade_no" ON "public"."pay_callback_record" USING btree (
  "trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_callback_record_trade_no" IS '按平台交易号查询回调';

-- ----------------------------
-- Primary Key structure for table pay_callback_record
-- ----------------------------
ALTER TABLE "public"."pay_callback_record" ADD CONSTRAINT "pay_callback_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_channel_terminal
-- ----------------------------
CREATE INDEX "idx_pay_channel_terminal_channel_mch_no" ON "public"."pay_channel_terminal" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_channel_terminal_channel_mch_no" IS '按通道商户号查询终端';
CREATE INDEX "idx_pay_channel_terminal_mch_no" ON "public"."pay_channel_terminal" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_channel_terminal_mch_no" IS '商户号关联查询';
CREATE INDEX "idx_pay_channel_terminal_status" ON "public"."pay_channel_terminal" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_channel_terminal_status" IS '状态筛选';

-- ----------------------------
-- Primary Key structure for table pay_channel_terminal
-- ----------------------------
ALTER TABLE "public"."pay_channel_terminal" ADD CONSTRAINT "pay_channel_terminal_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_close_record
-- ----------------------------
ALTER TABLE "public"."pay_close_record" ADD CONSTRAINT "pay_close_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_easy_pay_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_easy_pay_config_app_id" ON "public"."pay_easy_pay_config" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_easy_pay_config_app_id" IS '同一应用配置唯一';
CREATE UNIQUE INDEX "uk_easy_pay_config_pid" ON "public"."pay_easy_pay_config" USING btree (
  "pid" "pg_catalog"."int4_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_easy_pay_config_pid" IS '同一易支付商户号配置唯一';

-- ----------------------------
-- Primary Key structure for table pay_easy_pay_config
-- ----------------------------
ALTER TABLE "public"."pay_easy_pay_config" ADD CONSTRAINT "pk_pay_easy_pay_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_easy_pay_credential
-- ----------------------------
CREATE UNIQUE INDEX "uk_easy_pay_credential_app_id" ON "public"."pay_easy_pay_credential" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_easy_pay_credential_app_id" IS '同一应用凭证唯一';
CREATE UNIQUE INDEX "uk_easy_pay_credential_pid" ON "public"."pay_easy_pay_credential" USING btree (
  "pid" "pg_catalog"."int4_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_easy_pay_credential_pid" IS '同一易支付商户号凭证唯一';

-- ----------------------------
-- Primary Key structure for table pay_easy_pay_credential
-- ----------------------------
ALTER TABLE "public"."pay_easy_pay_credential" ADD CONSTRAINT "pk_pay_easy_pay_credential" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_easy_pay_order
-- ----------------------------
CREATE INDEX "idx_easy_pay_order_order_id" ON "public"."pay_easy_pay_order" USING btree (
  "order_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_easy_pay_order_order_id" IS '按支付订单 ID 查询';
CREATE INDEX "idx_easy_pay_order_pid_out" ON "public"."pay_easy_pay_order" USING btree (
  "pid" "pg_catalog"."int4_ops" ASC NULLS LAST,
  "out_trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_easy_pay_order_pid_out" IS '按易支付商户号+商户订单号查询';
CREATE INDEX "idx_easy_pay_order_trade_no" ON "public"."pay_easy_pay_order" USING btree (
  "trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_easy_pay_order_trade_no" IS '按平台交易号查询';
CREATE UNIQUE INDEX "uk_easy_pay_order_app_out" ON "public"."pay_easy_pay_order" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "out_trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_easy_pay_order_app_out" IS '同一应用下商户订单号唯一';

-- ----------------------------
-- Primary Key structure for table pay_easy_pay_order
-- ----------------------------
ALTER TABLE "public"."pay_easy_pay_order" ADD CONSTRAINT "pk_pay_easy_pay_order" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_easy_pay_refund_order
-- ----------------------------
CREATE INDEX "idx_pay_easy_pay_refund_order_out_trade_no" ON "public"."pay_easy_pay_refund_order" USING btree (
  "out_trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_easy_pay_refund_order_out_trade_no" IS '按商户订单号查询退款';
CREATE INDEX "idx_pay_easy_pay_refund_order_refund_id" ON "public"."pay_easy_pay_refund_order" USING btree (
  "refund_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_easy_pay_refund_order_refund_id" IS '按平台退款单 ID 查询';
CREATE INDEX "idx_pay_easy_pay_refund_order_refund_no" ON "public"."pay_easy_pay_refund_order" USING btree (
  "refund_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_easy_pay_refund_order_refund_no" IS '按商户退款号查询';

-- ----------------------------
-- Primary Key structure for table pay_easy_pay_refund_order
-- ----------------------------
ALTER TABLE "public"."pay_easy_pay_refund_order" ADD CONSTRAINT "pay_easy_pay_refund_order_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_gateway_cashier_item
-- ----------------------------
CREATE INDEX "idx_gateway_cashier_item_bucket" ON "public"."pay_gateway_cashier_item" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "cashier_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "client_env" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_gateway_cashier_item_bucket" IS '按应用+收银类型+客户端环境查询收银台项';
CREATE INDEX "idx_gateway_cashier_item_mch" ON "public"."pay_gateway_cashier_item" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_gateway_cashier_item_mch" IS '商户号关联查询';

-- ----------------------------
-- Primary Key structure for table pay_gateway_cashier_item
-- ----------------------------
ALTER TABLE "public"."pay_gateway_cashier_item" ADD CONSTRAINT "pk_pay_gateway_cashier_item" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_gateway_order
-- ----------------------------
CREATE INDEX "idx_gateway_order_app_biz" ON "public"."pay_gateway_order" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "biz_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_gateway_order_app_biz" IS '按应用+业务订单号查询';
CREATE INDEX "idx_gateway_order_mch_store" ON "public"."pay_gateway_order" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "store_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_gateway_order_mch_store" IS '按商户+门店维度查询';
CREATE UNIQUE INDEX "uk_gateway_order_order_no" ON "public"."pay_gateway_order" USING btree (
  "order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_gateway_order_order_no" IS '网关订单号唯一';

-- ----------------------------
-- Primary Key structure for table pay_gateway_order
-- ----------------------------
ALTER TABLE "public"."pay_gateway_order" ADD CONSTRAINT "pk_pay_gateway_order" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_gateway_pay_client_env
-- ----------------------------
CREATE INDEX "idx_pay_gateway_pay_env_config_id" ON "public"."pay_gateway_pay_client_env" USING btree (
  "config_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE UNIQUE INDEX "uk_pay_gateway_pay_env" ON "public"."pay_gateway_pay_client_env" USING btree (
  "config_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "client_env" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "pay_form" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;

-- ----------------------------
-- Primary Key structure for table pay_gateway_pay_client_env
-- ----------------------------
ALTER TABLE "public"."pay_gateway_pay_client_env" ADD CONSTRAINT "pay_gateway_pay_client_env_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_gateway_pay_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_gateway_pay_config_app" ON "public"."pay_gateway_pay_config" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;

-- ----------------------------
-- Primary Key structure for table pay_gateway_pay_config
-- ----------------------------
ALTER TABLE "public"."pay_gateway_pay_config" ADD CONSTRAINT "pay_gateway_pay_config_pkey" PRIMARY KEY ("id");

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
CREATE UNIQUE INDEX "uk_pay_md_product_config_product" ON "public"."pay_md_product_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_md_product_config_product" IS '同一产品配置唯一';

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

-- ----------------------------
-- Indexes structure for table pay_normal_order
-- ----------------------------
CREATE INDEX "idx_normal_order_app_biz" ON "public"."pay_normal_order" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "biz_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_normal_order_app_biz" IS '按应用+业务订单号查询';
CREATE INDEX "idx_normal_order_mch_store" ON "public"."pay_normal_order" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "store_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_normal_order_mch_store" IS '按商户+门店维度查询';
CREATE UNIQUE INDEX "uk_normal_order_mch_biz" ON "public"."pay_normal_order" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "biz_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_normal_order_mch_biz" IS '支付商户业务单号唯一约束: 同商户同 biz_order_no 仅允许一单, 防重复建单';
CREATE UNIQUE INDEX "uk_normal_order_order_no" ON "public"."pay_normal_order" USING btree (
  "order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_normal_order_order_no" IS '普通订单号唯一';

-- ----------------------------
-- Primary Key structure for table pay_normal_order
-- ----------------------------
ALTER TABLE "public"."pay_normal_order" ADD CONSTRAINT "pk_pay_normal_order" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_platform_mobile_app
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_platform_mobile_app_type_platform" ON "public"."pay_platform_mobile_app" USING btree (
  "app_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "platform" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_platform_mobile_app_type_platform" IS '同一应用类型+平台唯一';

-- ----------------------------
-- Primary Key structure for table pay_platform_mobile_app
-- ----------------------------
ALTER TABLE "public"."pay_platform_mobile_app" ADD CONSTRAINT "pay_platform_mobile_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_refund_order
-- ----------------------------
CREATE INDEX "idx_refund_order_app_biz" ON "public"."pay_refund_order" USING btree (
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "biz_refund_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_refund_order_app_biz" IS '按应用+业务退款号查询';
CREATE INDEX "idx_refund_order_mch_store" ON "public"."pay_refund_order" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "store_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_refund_order_mch_store" IS '按商户+门店维度查询';
CREATE INDEX "idx_refund_order_order_no" ON "public"."pay_refund_order" USING btree (
  "trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_refund_order_order_no" IS '按支付订单号查询退款';
CREATE INDEX "idx_refund_order_status_create_time" ON "public"."pay_refund_order" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_refund_order_status_create_time" IS '按退款状态+创建时间窗口扫描(定时同步)';
CREATE UNIQUE INDEX "uk_refund_order_mch_biz" ON "public"."pay_refund_order" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "biz_refund_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE biz_refund_no IS NOT NULL AND deleted = false;
COMMENT ON INDEX "public"."uk_refund_order_mch_biz" IS '退款商户业务单号唯一约束: 同商户同 biz_refund_no 仅允许一单, 防重复退款双扣';
CREATE UNIQUE INDEX "uk_refund_order_refund_no" ON "public"."pay_refund_order" USING btree (
  "refund_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_refund_order_refund_no" IS '退款号唯一';

-- ----------------------------
-- Primary Key structure for table pay_refund_order
-- ----------------------------
ALTER TABLE "public"."pay_refund_order" ADD CONSTRAINT "pk_pay_refund_order" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_risk_hit
-- ----------------------------
CREATE INDEX "idx_pay_risk_hit_create_time" ON "public"."pay_risk_hit" USING btree (
  "create_time" "pg_catalog"."timestamptz_ops" DESC NULLS FIRST
);
COMMENT ON INDEX "public"."idx_pay_risk_hit_create_time" IS '按时间倒序分页/范围扫描（风控命中高频）';
CREATE INDEX "idx_pay_risk_hit_hit_type_value" ON "public"."pay_risk_hit" USING btree (
  "hit_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "hit_value" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_risk_hit_hit_type_value" IS '按命中类型+值查询';
CREATE INDEX "idx_pay_risk_hit_phase" ON "public"."pay_risk_hit" USING btree (
  "phase" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_risk_hit_phase" IS '按风控阶段筛选';
CREATE INDEX "idx_pay_risk_hit_trade_no" ON "public"."pay_risk_hit" USING btree (
  "trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_risk_hit_trade_no" IS '按交易号查询风控命中';

-- ----------------------------
-- Primary Key structure for table pay_risk_hit
-- ----------------------------
ALTER TABLE "public"."pay_risk_hit" ADD CONSTRAINT "pay_risk_hit_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_route_basic_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_route_basic_config_provider" ON "public"."pay_route_basic_config" USING btree (
  "strategy_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "provider" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_route_basic_config_provider" IS '同一策略下渠道唯一';

-- ----------------------------
-- Primary Key structure for table pay_route_basic_config
-- ----------------------------
ALTER TABLE "public"."pay_route_basic_config" ADD CONSTRAINT "pay_route_basic_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_route_scene_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_pay_route_scene_config_method" ON "public"."pay_route_scene_config" USING btree (
  "strategy_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "method" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_route_scene_config_method" IS '同一策略下支付方式唯一';

-- ----------------------------
-- Primary Key structure for table pay_route_scene_config
-- ----------------------------
ALTER TABLE "public"."pay_route_scene_config" ADD CONSTRAINT "pay_route_scene_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_route_strategy
-- ----------------------------
ALTER TABLE "public"."pay_route_strategy" ADD CONSTRAINT "pk_pay_route_strategy" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table pay_sync_record
-- ----------------------------
ALTER TABLE "public"."pay_sync_record" ADD CONSTRAINT "pay_sync_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_terminal_channel_bind
-- ----------------------------
CREATE INDEX "idx_pay_terminal_channel_bind_channel" ON "public"."pay_terminal_channel_bind" USING btree (
  "channel_terminal_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_terminal_channel_bind_channel" IS '按通道终端 ID 查询绑定';
CREATE INDEX "idx_pay_terminal_channel_bind_mch" ON "public"."pay_terminal_channel_bind" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_terminal_channel_bind_mch" IS '商户号关联查询';
CREATE INDEX "idx_pay_terminal_channel_bind_system" ON "public"."pay_terminal_channel_bind" USING btree (
  "system_terminal_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_terminal_channel_bind_system" IS '按系统终端号查询绑定';
CREATE UNIQUE INDEX "uk_pay_terminal_channel_bind" ON "public"."pay_terminal_channel_bind" USING btree (
  "system_terminal_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "channel_terminal_id" "pg_catalog"."int8_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_terminal_channel_bind" IS '系统终端+通道终端绑定唯一';

-- ----------------------------
-- Primary Key structure for table pay_terminal_channel_bind
-- ----------------------------
ALTER TABLE "public"."pay_terminal_channel_bind" ADD CONSTRAINT "pay_terminal_channel_bind_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_terminal_device
-- ----------------------------
CREATE INDEX "idx_pay_terminal_device_mch_no" ON "public"."pay_terminal_device" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_terminal_device_mch_no" IS '商户号关联查询';
CREATE INDEX "idx_pay_terminal_device_store_no" ON "public"."pay_terminal_device" USING btree (
  "store_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_terminal_device_store_no" IS '按门店号查询设备';
CREATE UNIQUE INDEX "uk_pay_terminal_device_terminal_no" ON "public"."pay_terminal_device" USING btree (
  "terminal_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_terminal_device_terminal_no" IS '终端号唯一';

-- ----------------------------
-- Primary Key structure for table pay_terminal_device
-- ----------------------------
ALTER TABLE "public"."pay_terminal_device" ADD CONSTRAINT "pay_terminal_device_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_trade
-- ----------------------------
CREATE INDEX "idx_pay_trade_container" ON "public"."pay_trade" USING btree (
  "container_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "trade_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_trade_container" IS '按业务容器+交易类型查询';
CREATE INDEX "idx_pay_trade_mch_channel" ON "public"."pay_trade" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_trade_mch_channel" IS '按商户+通道商户维度查询';
CREATE INDEX "idx_pay_trade_mch_store" ON "public"."pay_trade" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "store_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_trade_mch_store" IS '按商户+门店维度查询';
CREATE INDEX "idx_pay_trade_out_order_no" ON "public"."pay_trade" USING btree (
  "out_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_trade_out_order_no" IS '按商户订单号查询交易';
CREATE INDEX "idx_pay_trade_relation_order_no" ON "public"."pay_trade" USING btree (
  "relation_order_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_trade_relation_order_no" IS '按关联订单号查询交易';
CREATE INDEX "idx_pay_trade_status_create_time" ON "public"."pay_trade" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_trade_status_create_time" IS '按资金状态+创建时间窗口扫描(定时同步)';
CREATE UNIQUE INDEX "uk_pay_trade_trade_no" ON "public"."pay_trade" USING btree (
  "trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_trade_trade_no" IS '平台交易号唯一';

-- ----------------------------
-- Primary Key structure for table pay_trade
-- ----------------------------
ALTER TABLE "public"."pay_trade" ADD CONSTRAINT "pk_pay_trade" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_transfer_order_alipay
-- ----------------------------
CREATE INDEX "idx_pay_transfer_order_alipay_mch" ON "public"."pay_transfer_order_alipay" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_order_alipay_mch" IS '按商户号+创建时间查询';
CREATE INDEX "idx_pay_transfer_order_alipay_status_time" ON "public"."pay_transfer_order_alipay" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_order_alipay_status_time" IS '按状态+创建时间窗口扫描(定时同步)';
CREATE UNIQUE INDEX "uk_pay_transfer_order_alipay_biz" ON "public"."pay_transfer_order_alipay" USING btree (
  "biz_transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_transfer_order_alipay_biz" IS '同一应用同一商户转账号唯一(幂等约束)';
CREATE UNIQUE INDEX "uk_pay_transfer_order_alipay_no" ON "public"."pay_transfer_order_alipay" USING btree (
  "transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_transfer_order_alipay_no" IS '平台转账单号唯一';

-- ----------------------------
-- Primary Key structure for table pay_transfer_order_alipay
-- ----------------------------
ALTER TABLE "public"."pay_transfer_order_alipay" ADD CONSTRAINT "pk_pay_transfer_order_alipay" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_transfer_order_douyin
-- ----------------------------
CREATE INDEX "idx_pay_transfer_order_douyin_mch" ON "public"."pay_transfer_order_douyin" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_order_douyin_mch" IS '按商户号+创建时间查询';
CREATE INDEX "idx_pay_transfer_order_douyin_status_time" ON "public"."pay_transfer_order_douyin" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_order_douyin_status_time" IS '按状态+创建时间窗口扫描(定时同步)';
CREATE UNIQUE INDEX "uk_pay_transfer_order_douyin_biz" ON "public"."pay_transfer_order_douyin" USING btree (
  "biz_transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_transfer_order_douyin_biz" IS '同一应用同一商户转账号唯一(幂等约束)';
CREATE UNIQUE INDEX "uk_pay_transfer_order_douyin_no" ON "public"."pay_transfer_order_douyin" USING btree (
  "transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_transfer_order_douyin_no" IS '平台转账单号唯一';

-- ----------------------------
-- Primary Key structure for table pay_transfer_order_douyin
-- ----------------------------
ALTER TABLE "public"."pay_transfer_order_douyin" ADD CONSTRAINT "pk_pay_transfer_order_douyin" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_transfer_order_wechat
-- ----------------------------
CREATE INDEX "idx_pay_transfer_order_wechat_mch" ON "public"."pay_transfer_order_wechat" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_order_wechat_mch" IS '按商户号+创建时间查询';
CREATE INDEX "idx_pay_transfer_order_wechat_status_time" ON "public"."pay_transfer_order_wechat" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_order_wechat_status_time" IS '按状态+创建时间窗口扫描(定时同步)';
CREATE UNIQUE INDEX "uk_pay_transfer_order_wechat_biz" ON "public"."pay_transfer_order_wechat" USING btree (
  "biz_transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_transfer_order_wechat_biz" IS '同一应用同一商户转账号唯一(幂等约束)';
CREATE UNIQUE INDEX "uk_pay_transfer_order_wechat_no" ON "public"."pay_transfer_order_wechat" USING btree (
  "transfer_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_transfer_order_wechat_no" IS '平台转账单号唯一';

-- ----------------------------
-- Primary Key structure for table pay_transfer_order_wechat
-- ----------------------------
ALTER TABLE "public"."pay_transfer_order_wechat" ADD CONSTRAINT "pk_pay_transfer_order_wechat" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table pay_transfer_trade
-- ----------------------------
CREATE INDEX "idx_pay_transfer_trade_container" ON "public"."pay_transfer_trade" USING btree (
  "container_id" "pg_catalog"."int8_ops" ASC NULLS LAST,
  "container_channel" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_pay_transfer_trade_container" IS '按所属转账单+通道查询';
CREATE INDEX "idx_pay_transfer_trade_mch" ON "public"."pay_transfer_trade" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_trade_mch" IS '按商户号+创建时间查询';
CREATE INDEX "idx_pay_transfer_trade_status_time" ON "public"."pay_transfer_trade" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."idx_pay_transfer_trade_status_time" IS '按状态+创建时间窗口扫描(定时同步)';
CREATE UNIQUE INDEX "uk_pay_transfer_trade_no" ON "public"."pay_transfer_trade" USING btree (
  "trade_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_pay_transfer_trade_no" IS '平台转账交易号唯一';

-- ----------------------------
-- Primary Key structure for table pay_transfer_trade
-- ----------------------------
ALTER TABLE "public"."pay_transfer_trade" ADD CONSTRAINT "pk_pay_transfer_trade" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table starter_audit_unipay_log
-- ----------------------------
CREATE INDEX "idx_starter_audit_unipay_log_mch_time" ON "public"."starter_audit_unipay_log" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "operate_time" "pg_catalog"."timestamptz_ops" DESC NULLS FIRST
);
COMMENT ON INDEX "public"."idx_starter_audit_unipay_log_mch_time" IS '按商户号+时间倒序查询审计日志';
CREATE INDEX "idx_starter_audit_unipay_log_success_time" ON "public"."starter_audit_unipay_log" USING btree (
  "success" "pg_catalog"."bool_ops" ASC NULLS LAST,
  "operate_time" "pg_catalog"."timestamptz_ops" DESC NULLS FIRST
);
COMMENT ON INDEX "public"."idx_starter_audit_unipay_log_success_time" IS '按成功标志+时间筛选审计日志';
CREATE INDEX "idx_starter_audit_unipay_log_time" ON "public"."starter_audit_unipay_log" USING btree (
  "operate_time" "pg_catalog"."timestamptz_ops" DESC NULLS FIRST
);
COMMENT ON INDEX "public"."idx_starter_audit_unipay_log_time" IS '按时间倒序分页/范围扫描（审计日志高频）';
CREATE INDEX "idx_starter_audit_unipay_log_trace" ON "public"."starter_audit_unipay_log" USING btree (
  "trace_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_starter_audit_unipay_log_trace" IS '按链路追踪 ID 查询';

-- ----------------------------
-- Primary Key structure for table starter_audit_unipay_log
-- ----------------------------
ALTER TABLE "public"."starter_audit_unipay_log" ADD CONSTRAINT "starter_audit_unipay_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table starter_platform_file_record
-- ----------------------------
ALTER TABLE "public"."starter_platform_file_record" ADD CONSTRAINT "starter_platform_file_record_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table stripe_channel_merchant
-- ----------------------------
ALTER TABLE "public"."stripe_channel_merchant" ADD CONSTRAINT "pk_stripe_direct_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table stripe_key_config
-- ----------------------------
ALTER TABLE "public"."stripe_key_config" ADD CONSTRAINT "pk_stripe_direct_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table system_sensitive_word
-- ----------------------------
CREATE INDEX "idx_system_sensitive_word_status" ON "public"."system_sensitive_word" USING btree (
  "status" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_system_sensitive_word_status" IS '状态筛选（启用/停用）';
CREATE UNIQUE INDEX "uk_system_sensitive_word_word" ON "public"."system_sensitive_word" USING btree (
  "word" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_system_sensitive_word_word" IS '敏感词唯一';

-- ----------------------------
-- Primary Key structure for table system_sensitive_word
-- ----------------------------
ALTER TABLE "public"."system_sensitive_word" ADD CONSTRAINT "system_sensitive_word_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table system_sensitive_word_hit
-- ----------------------------
CREATE INDEX "idx_system_sensitive_word_hit_mch" ON "public"."system_sensitive_word_hit" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_system_sensitive_word_hit_mch" IS '按商户号+时间查询命中记录';
CREATE INDEX "idx_system_sensitive_word_hit_scene" ON "public"."system_sensitive_word_hit" USING btree (
  "scene" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_system_sensitive_word_hit_scene" IS '按场景筛选命中';
CREATE INDEX "idx_system_sensitive_word_hit_time" ON "public"."system_sensitive_word_hit" USING btree (
  "create_time" "pg_catalog"."timestamptz_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_system_sensitive_word_hit_time" IS '按时间分页/范围扫描';
CREATE INDEX "idx_system_sensitive_word_hit_word" ON "public"."system_sensitive_word_hit" USING btree (
  "hit_word" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_system_sensitive_word_hit_word" IS '按命中词查询';

-- ----------------------------
-- Primary Key structure for table system_sensitive_word_hit
-- ----------------------------
ALTER TABLE "public"."system_sensitive_word_hit" ADD CONSTRAINT "system_sensitive_word_hit_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table ums_direct_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_ums_direct_key_cmchno" ON "public"."ums_direct_key_config" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_ums_direct_key_cmchno" IS '通道商户号唯一';
CREATE UNIQUE INDEX "uk_ums_direct_key_mch_sandbox" ON "public"."ums_direct_key_config" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_ums_direct_key_mch_sandbox" IS '同一通道商户同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table ums_direct_key_config
-- ----------------------------
ALTER TABLE "public"."ums_direct_key_config" ADD CONSTRAINT "ums_direct_key_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table union_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_union_key_mch_sandbox" ON "public"."union_key_config" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_union_key_mch_sandbox" IS '同一通道商户同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table union_key_config
-- ----------------------------
ALTER TABLE "public"."union_key_config" ADD CONSTRAINT "union_key_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table vbill_isv_channel_merchant
-- ----------------------------
ALTER TABLE "public"."vbill_isv_channel_merchant" ADD CONSTRAINT "vbill_isv_channel_merchant_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table vbill_isv_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_vbill_isv_key_prod_sandbox" ON "public"."vbill_isv_key_config" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_vbill_isv_key_prod_sandbox" IS '同一产品同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table vbill_isv_key_config
-- ----------------------------
ALTER TABLE "public"."vbill_isv_key_config" ADD CONSTRAINT "vbill_isv_key_config_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wechat_direct_channel_merchant
-- ----------------------------
ALTER TABLE "public"."wechat_direct_channel_merchant" ADD CONSTRAINT "pk_mch_wechat_direct_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wechat_direct_key_config
-- ----------------------------
ALTER TABLE "public"."wechat_direct_key_config" ADD CONSTRAINT "pk_wechat_direct_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wechat_isv_channel_merchant
-- ----------------------------
ALTER TABLE "public"."wechat_isv_channel_merchant" ADD CONSTRAINT "pk_mch_wechat_isv_channel_merchant" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wechat_isv_key_config
-- ----------------------------
ALTER TABLE "public"."wechat_isv_key_config" ADD CONSTRAINT "pk_wechat_isv_key_config" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table wx_channel_app_capability
-- ----------------------------
CREATE INDEX "idx_wx_channel_app_cap_mch" ON "public"."wx_channel_app_capability" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_wx_channel_app_cap_mch" IS '商户号关联查询';
CREATE INDEX "idx_wx_channel_app_cap_ref" ON "public"."wx_channel_app_capability" USING btree (
  "app_scope" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "wx_app_ref_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_wx_channel_app_cap_ref" IS '按应用范围+应用引用查询能力';
CREATE UNIQUE INDEX "uk_wx_channel_app_cap" ON "public"."wx_channel_app_capability" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "capability" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_scope" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_wx_channel_app_cap" IS '通道商户+能力+应用范围唯一';

-- ----------------------------
-- Primary Key structure for table wx_channel_app_capability
-- ----------------------------
ALTER TABLE "public"."wx_channel_app_capability" ADD CONSTRAINT "wx_channel_app_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table wx_mch_app
-- ----------------------------
CREATE INDEX "idx_wx_mch_app_app_type" ON "public"."wx_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "app_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_wx_mch_app_app_type" IS '按商户号+应用类型查询';
CREATE INDEX "idx_wx_mch_app_mch_no" ON "public"."wx_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_wx_mch_app_mch_no" IS '商户号关联查询';
CREATE UNIQUE INDEX "uk_wx_mch_app_mch_wx" ON "public"."wx_mch_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "wx_app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_wx_mch_app_mch_wx" IS '同一商户下微信 AppID 唯一';

-- ----------------------------
-- Primary Key structure for table wx_mch_app
-- ----------------------------
ALTER TABLE "public"."wx_mch_app" ADD CONSTRAINT "wx_mch_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table wx_platform_app
-- ----------------------------
CREATE INDEX "idx_wx_platform_app_app_type" ON "public"."wx_platform_app" USING btree (
  "app_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_wx_platform_app_app_type" IS '按应用类型筛选';
CREATE UNIQUE INDEX "uk_wx_platform_app_wx_app_id" ON "public"."wx_platform_app" USING btree (
  "wx_app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_wx_platform_app_wx_app_id" IS '微信平台 AppID 唯一';

-- ----------------------------
-- Primary Key structure for table wx_platform_app
-- ----------------------------
ALTER TABLE "public"."wx_platform_app" ADD CONSTRAINT "wx_platform_app_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table wx_platform_app_capability
-- ----------------------------
CREATE UNIQUE INDEX "uk_wx_platform_app_cap_product" ON "public"."wx_platform_app_capability" USING btree (
  "product" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "capability" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_wx_platform_app_cap_product" IS '产品+能力唯一';

-- ----------------------------
-- Primary Key structure for table wx_platform_app_capability
-- ----------------------------
ALTER TABLE "public"."wx_platform_app_capability" ADD CONSTRAINT "wx_platform_app_capability_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table yeepay_direct_key_config
-- ----------------------------
CREATE UNIQUE INDEX "uk_yeepay_direct_key_cmchno_sandbox" ON "public"."yeepay_direct_key_config" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_yeepay_direct_key_cmchno_sandbox" IS '同一通道商户同一环境密钥唯一';

-- ----------------------------
-- Primary Key structure for table yeepay_direct_key_config
-- ----------------------------
ALTER TABLE "public"."yeepay_direct_key_config" ADD CONSTRAINT "pk_yeepay_direct_key_config" PRIMARY KEY ("id");
