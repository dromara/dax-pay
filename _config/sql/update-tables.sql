-- ========================================
-- 网关支付配置统一(码牌/聚合合并为一张表)
-- 开发阶段: 直接清除旧数据, 不做迁移
-- ========================================

-- 1. 新建统一主表 pay_gateway_pay_config
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
);
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
CREATE UNIQUE INDEX "uk_pay_gateway_pay_config_app" ON "public"."pay_gateway_pay_config" USING btree ("app_id") WHERE deleted = false;
ALTER TABLE "public"."pay_gateway_pay_config" ADD CONSTRAINT "pay_gateway_pay_config_pkey" PRIMARY KEY ("id");

-- 2. 新建统一子表 pay_gateway_pay_client_env
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
);
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
CREATE INDEX "idx_pay_gateway_pay_env_config_id" ON "public"."pay_gateway_pay_client_env" USING btree ("config_id");
CREATE UNIQUE INDEX "uk_pay_gateway_pay_env" ON "public"."pay_gateway_pay_client_env" USING btree ("config_id", "client_env", "pay_form") WHERE deleted = false;
ALTER TABLE "public"."pay_gateway_pay_client_env" ADD CONSTRAINT "pay_gateway_pay_client_env_pkey" PRIMARY KEY ("id");

-- 3. 删除旧表(码牌 + 聚合 共 4 张)
DROP TABLE IF EXISTS "public"."pay_gateway_aggregate_client_env";
DROP TABLE IF EXISTS "public"."pay_gateway_aggregate_config";
DROP TABLE IF EXISTS "public"."pay_gateway_code_client_env";
DROP TABLE IF EXISTS "public"."pay_gateway_code_config";

-- 4. 聚合订单表加 link_form 字段(聚合小程序链接前缀分流)
ALTER TABLE "public"."pay_gateway_order" ADD COLUMN IF NOT EXISTS "link_form" varchar(16) COLLATE "pg_catalog"."default" DEFAULT 'h5';
COMMENT ON COLUMN "public"."pay_gateway_order"."link_form" IS '链接形态: h5/mini(聚合小程序扫码), 缺省 h5';

-- 5. 菜单: 删除旧的码牌配置 + 聚合配置菜单, 新增统一网关支付配置菜单
DELETE FROM "public"."iam_perm_menu" WHERE menu_code IN ('merchant:gateway-aggregate', 'merchant:gateway-code');

-- admin 端(4040121 原聚合位 → 统一配置)
INSERT INTO "public"."iam_perm_menu" VALUES (
  4040121, 4040130, 'merchant:gateway-pay-config', 'admin', 'GatewayPayConfig',
  'menu.payment.merchant.gatewayPayConfig', NULL, 't', 'f',
  '/payment/merchant/gateway-config/GatewayPayConfig', '/payment/merchant/gateway-config',
  NULL, 3, 'f', 't', 'f', 1, 1, 1, 'f', 'subpage',
  NULL, NULL, NULL, NULL, NULL, NULL, NOW(), NOW());

-- merchant 端(91405 原聚合位 → 统一配置)
INSERT INTO "public"."iam_perm_menu" VALUES (
  91405, 91403, 'merchant:gateway-pay-config', 'merchant', 'GatewayPayConfig',
  'menu.payment.merchant.gatewayPayConfig', 'lucide:qr-code', 't', 'f',
  '/payment/merchant/gateway-config/GatewayPayConfig', '/mch/gateway-config',
  NULL, 2, 'f', 'f', 'f', 0, 1, 1, 'f', 'subpage',
  NULL, NULL, NULL, NULL, '', '', NOW(), NOW());
