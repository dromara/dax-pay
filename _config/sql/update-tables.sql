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


-- ----------------------------
-- Table structure for mch_notice_task
-- 商户出站通知任务
-- ----------------------------
DROP TABLE IF EXISTS "public"."mch_notice_record";
DROP TABLE IF EXISTS "public"."mch_notice_task";
CREATE TABLE "public"."mch_notice_task" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) NOT NULL,
  "app_id" varchar(64) NOT NULL,
  "biz_id" int8,
  "biz_no" varchar(64) NOT NULL,
  "event" varchar(64) NOT NULL,
  "protocol" varchar(32) NOT NULL,
  "source" varchar(16) NOT NULL,
  "content_mode" varchar(16) NOT NULL,
  "content" text,
  "url" varchar(512) NOT NULL,
  "success" bool NOT NULL DEFAULT false,
  "send_count" int4 NOT NULL DEFAULT 0,
  "delay_count" int4 NOT NULL DEFAULT 0,
  "next_time" timestamptz(6),
  "latest_time" timestamptz(6),
  "error_msg" varchar(300)
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
COMMENT ON COLUMN "public"."mch_notice_task"."protocol" IS '通知协议: system / easy_pay';
COMMENT ON COLUMN "public"."mch_notice_task"."source" IS 'URL来源: order / app / protocol';
COMMENT ON COLUMN "public"."mch_notice_task"."content_mode" IS '内容策略: snapshot / ref';
COMMENT ON COLUMN "public"."mch_notice_task"."content" IS '通知内容(快照JSON或引用指针JSON)';
COMMENT ON COLUMN "public"."mch_notice_task"."url" IS '商户接收地址';
COMMENT ON COLUMN "public"."mch_notice_task"."success" IS '是否发送成功';
COMMENT ON COLUMN "public"."mch_notice_task"."send_count" IS '已发送次数';
COMMENT ON COLUMN "public"."mch_notice_task"."delay_count" IS '延迟重试次数';
COMMENT ON COLUMN "public"."mch_notice_task"."next_time" IS '下次发送时间';
COMMENT ON COLUMN "public"."mch_notice_task"."latest_time" IS '最后发送时间';
COMMENT ON COLUMN "public"."mch_notice_task"."error_msg" IS '最近一次错误摘要';
COMMENT ON TABLE "public"."mch_notice_task" IS '商户出站通知任务';

ALTER TABLE "public"."mch_notice_task" ADD CONSTRAINT "mch_notice_task_pkey" PRIMARY KEY ("id");
CREATE UNIQUE INDEX "uk_mch_notice_task_biz" ON "public"."mch_notice_task" USING btree ("mch_no", "app_id", "event", "biz_no", "protocol", "source") WHERE deleted = false;
CREATE INDEX "idx_mch_notice_task_biz_no" ON "public"."mch_notice_task" USING btree ("biz_no");
CREATE INDEX "idx_mch_notice_task_success" ON "public"."mch_notice_task" USING btree ("success");
CREATE INDEX "idx_mch_notice_task_create_time" ON "public"."mch_notice_task" USING btree ("create_time" DESC);


-- ----------------------------
-- Table structure for mch_notice_record
-- 商户出站通知发送记录
-- ----------------------------
CREATE TABLE "public"."mch_notice_record" (
  "id" int8 NOT NULL,
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false,
  "mch_no" varchar(32) NOT NULL,
  "task_id" int8 NOT NULL,
  "req_count" int4 NOT NULL,
  "send_type" varchar(16) NOT NULL,
  "success" bool NOT NULL DEFAULT false,
  "http_status" int4,
  "error_msg" varchar(300),
  "request_digest" varchar(500)
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

ALTER TABLE "public"."mch_notice_record" ADD CONSTRAINT "mch_notice_record_pkey" PRIMARY KEY ("id");
CREATE INDEX "idx_mch_notice_record_task_id" ON "public"."mch_notice_record" USING btree ("task_id");
CREATE INDEX "idx_mch_notice_record_create_time" ON "public"."mch_notice_record" USING btree ("create_time" DESC);

-- ----------------------------
-- Table structure for pay_callback_record
-- 通道入站回调记录(只审计不重放)
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
  "mch_no" varchar(32) NOT NULL,
  "app_id" varchar(64),
  "channel_mch_no" varchar(64),
  "trade_no" varchar(100),
  "out_trade_no" varchar(150),
  "channel" varchar(32) NOT NULL,
  "callback_type" varchar(20) NOT NULL,
  "notify_info" text NOT NULL,
  "status" varchar(20) NOT NULL,
  "error_msg" varchar(500)
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
COMMENT ON COLUMN "public"."pay_callback_record"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_callback_record"."callback_type" IS '回调类型: pay / refund';
COMMENT ON COLUMN "public"."pay_callback_record"."notify_info" IS '通知消息内容(JSON)';
COMMENT ON COLUMN "public"."pay_callback_record"."status" IS '回调处理状态';
COMMENT ON COLUMN "public"."pay_callback_record"."error_msg" IS '错误信息';
COMMENT ON TABLE "public"."pay_callback_record" IS '通道入站回调记录';

ALTER TABLE "public"."pay_callback_record" ADD CONSTRAINT "pay_callback_record_pkey" PRIMARY KEY ("id");
CREATE INDEX "idx_pay_callback_record_trade_no" ON "public"."pay_callback_record" USING btree ("trade_no");
CREATE INDEX "idx_pay_callback_record_out_trade_no" ON "public"."pay_callback_record" USING btree ("out_trade_no");
CREATE INDEX "idx_pay_callback_record_channel" ON "public"."pay_callback_record" USING btree ("channel");
CREATE INDEX "idx_pay_callback_record_mch_no" ON "public"."pay_callback_record" USING btree ("mch_no");
CREATE INDEX "idx_pay_callback_record_channel_mch_no" ON "public"."pay_callback_record" USING btree ("channel_mch_no");
CREATE INDEX "idx_pay_callback_record_create_time" ON "public"."pay_callback_record" USING btree ("create_time" DESC);

-- 已建表环境增量: 回调记录补充通道商户号(勿与上方 DROP/CREATE 同时重复执行于全新建表之外)
ALTER TABLE "public"."pay_callback_record" ADD COLUMN IF NOT EXISTS "channel_mch_no" varchar(64);
COMMENT ON COLUMN "public"."pay_callback_record"."channel_mch_no" IS '通道商户号';
CREATE INDEX IF NOT EXISTS "idx_pay_callback_record_channel_mch_no" ON "public"."pay_callback_record" USING btree ("channel_mch_no");
-- 已建表环境增量: 去掉未使用的错误码列
ALTER TABLE "public"."pay_callback_record" DROP COLUMN IF EXISTS "error_code";

