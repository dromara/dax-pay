-- ============================================================
-- 抖音转账配置表(转账发起应用绑定, 发起转账时决定转出主体与收款人openId来源)
-- 对齐微信 wechat_transfer_config 范式; 转账场景为主数据枚举无需落库, 本表仅存发起应用
-- ============================================================
DROP TABLE IF EXISTS "public"."douyin_transfer_config";

CREATE TABLE "public"."douyin_transfer_config" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "transfer_app_ref_id" int8
);
COMMENT ON COLUMN "public"."douyin_transfer_config"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_transfer_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."douyin_transfer_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_transfer_config"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."douyin_transfer_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_transfer_config"."version" IS '版本号';
COMMENT ON COLUMN "public"."douyin_transfer_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."douyin_transfer_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_transfer_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."douyin_transfer_config"."transfer_app_ref_id" IS '转账发起应用引用(dy_mch_app 主键, 须为网站应用 web_app, 支持手机H5获取OpenId)';
COMMENT ON TABLE "public"."douyin_transfer_config" IS '抖音转账配置(一对一, 指定转账发起应用, 决定转出主体与收款人openId来源)';

-- 主键约束 先删后增
ALTER TABLE "public"."douyin_transfer_config" DROP CONSTRAINT IF EXISTS "pk_douyin_transfer_config";
ALTER TABLE "public"."douyin_transfer_config" ADD CONSTRAINT "pk_douyin_transfer_config" PRIMARY KEY ("id");

-- 通道商户查询索引 先删后增
DROP INDEX IF EXISTS "public"."idx_douyin_transfer_config_mch";
CREATE INDEX "idx_douyin_transfer_config_mch" ON "public"."douyin_transfer_config" USING btree ("channel_mch_no", "deleted");
COMMENT ON INDEX "public"."idx_douyin_transfer_config_mch" IS '按通道商户号查询转账配置';

-- 通道商户唯一索引(一个通道商户一条转账配置) 先删后增
DROP INDEX IF EXISTS "public"."uk_douyin_transfer_config_mch";
CREATE UNIQUE INDEX "uk_douyin_transfer_config_mch" ON "public"."douyin_transfer_config" ("channel_mch_no") WHERE deleted = false;
COMMENT ON INDEX "public"."uk_douyin_transfer_config_mch" IS '同一通道商户仅一条转账配置(部分唯一索引)';
