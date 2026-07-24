-- ====================================================================
-- 增量表结构脚本（PostgreSQL）
-- 升级顺序：先本文件，再 update-datas.sql
-- ====================================================================

-- ----------------------------
-- Table structure for pay_blacklist
-- 支付黑名单（平台级；IP全局 / 支付宝userId全局 / 微信按平台应用）
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
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'enable',
  "reason" varchar(255) COLLATE "pg_catalog"."default",
  "expire_time" timestamptz(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON TABLE "public"."pay_blacklist" IS '支付黑名单（平台级；IP全局 / 支付宝userId全局 / 微信按平台应用）';
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

ALTER TABLE "public"."pay_blacklist" ADD CONSTRAINT "pay_blacklist_pkey" PRIMARY KEY ("id");
CREATE INDEX "idx_pay_blacklist_type_value" ON "public"."pay_blacklist" USING btree ("type", "value");
CREATE INDEX "idx_pay_blacklist_status" ON "public"."pay_blacklist" USING btree ("status");
CREATE UNIQUE INDEX "uk_pay_blacklist_type_value_app" ON "public"."pay_blacklist" USING btree ("type", "value", (COALESCE("wx_app_id", '')));
