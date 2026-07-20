-- ====================================================================
-- 增量建表脚本（PostgreSQL）
-- 与全量 tables.sql 配合使用：tables.sql 创建基础表，本文件追加新增表
-- 字段注释一律使用 COMMENT ON 语句（符合 PG 标准 DDL）
-- ====================================================================


-- ----------------------------
-- Table structure for pay_blacklist
-- 支付黑名单配置（平台级名单，维度为 IP / openId）
-- ----------------------------
DROP TABLE IF EXISTS "public"."pay_blacklist";
CREATE TABLE "public"."pay_blacklist" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "type" varchar(32) NOT NULL,
  "value" varchar(128) NOT NULL,
  "channel" varchar(32),
  "channel_app_id" varchar(64),
  "status" varchar(16) NOT NULL DEFAULT 'enable',
  "reason" varchar(255),
  "expire_time" timestamptz(6),
  "remark" varchar(255)
)
;
COMMENT ON COLUMN "public"."pay_blacklist"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_blacklist"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_blacklist"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_blacklist"."last_modifier" IS '最后修改ID';
COMMENT ON COLUMN "public"."pay_blacklist"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_blacklist"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_blacklist"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."pay_blacklist"."type" IS '名单类型: ip-IP地址, open_id-用户标识';
COMMENT ON COLUMN "public"."pay_blacklist"."value" IS '名单值（IP 地址或 openId）';
COMMENT ON COLUMN "public"."pay_blacklist"."channel" IS '通道族（openId 建议 wechat/alipay；IP 可空，空表示全局）';
COMMENT ON COLUMN "public"."pay_blacklist"."channel_app_id" IS '通道应用 AppId（可选，防 openId 跨应用误杀）';
COMMENT ON COLUMN "public"."pay_blacklist"."status" IS '状态: enable-启用, disable-禁用';
COMMENT ON COLUMN "public"."pay_blacklist"."reason" IS '拉黑原因';
COMMENT ON COLUMN "public"."pay_blacklist"."expire_time" IS '过期时间（空表示永久有效）';
COMMENT ON COLUMN "public"."pay_blacklist"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_blacklist" IS '支付黑名单配置（平台级名单，维度为 IP / openId，不含商户号事前拉黑）';


-- ----------------------------
-- Primary Key & Indexes for pay_blacklist
-- ----------------------------
ALTER TABLE "public"."pay_blacklist" ADD CONSTRAINT "pay_blacklist_pkey" PRIMARY KEY ("id");
CREATE INDEX "idx_pay_blacklist_type_value" ON "public"."pay_blacklist" USING btree ("type", "value");
CREATE INDEX "idx_pay_blacklist_status" ON "public"."pay_blacklist" USING btree ("status");


-- ----------------------------
-- Table structure for pay_risk_hit
-- 支付风险命中记录（事前拦截与事后命中）
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
  "phase" varchar(16) NOT NULL,
  "hit_type" varchar(32) NOT NULL,
  "hit_value" varchar(128) NOT NULL,
  "blacklist_id" int8,
  "mch_no" varchar(32),
  "app_id" varchar(64),
  "trade_no" varchar(64),
  "order_no" varchar(64),
  "biz_order_no" varchar(128),
  "trade_type" varchar(32),
  "method" varchar(32),
  "product" varchar(32),
  "channel" varchar(32),
  "client_ip" varchar(64),
  "openid" varchar(128),
  "buyer_id" varchar(128),
  "scene" varchar(16) NOT NULL DEFAULT 'unknown',
  "handle_status" varchar(20) NOT NULL DEFAULT 'pending',
  "handle_remark" varchar(500),
  "handle_user_id" int8,
  "handle_time" timestamptz(6),
  "remark" varchar(255)
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
COMMENT ON COLUMN "public"."pay_risk_hit"."hit_type" IS '命中类型（与黑名单 type 一致: ip / open_id）';
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
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_status" IS '处理状态: pending/ignored/added_blacklist/merchant_disabled/other';
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_remark" IS '处理说明';
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_user_id" IS '处理人';
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_time" IS '处理时间';
COMMENT ON COLUMN "public"."pay_risk_hit"."remark" IS '备注';
COMMENT ON TABLE "public"."pay_risk_hit" IS '支付风险命中记录（事前拦截与事后命中，供运营预警与处置）';


-- ----------------------------
-- Primary Key & Indexes for pay_risk_hit
-- ----------------------------
ALTER TABLE "public"."pay_risk_hit" ADD CONSTRAINT "pay_risk_hit_pkey" PRIMARY KEY ("id");
CREATE INDEX "idx_pay_risk_hit_phase" ON "public"."pay_risk_hit" USING btree ("phase");
CREATE INDEX "idx_pay_risk_hit_hit_type_value" ON "public"."pay_risk_hit" USING btree ("hit_type", "hit_value");
CREATE INDEX "idx_pay_risk_hit_handle_status" ON "public"."pay_risk_hit" USING btree ("handle_status");
CREATE INDEX "idx_pay_risk_hit_trade_no" ON "public"."pay_risk_hit" USING btree ("trade_no");
CREATE INDEX "idx_pay_risk_hit_create_time" ON "public"."pay_risk_hit" USING btree ("create_time" DESC);
