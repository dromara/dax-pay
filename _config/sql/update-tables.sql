-- 站内通知表结构(公告 + 已读 + 个人消息预留)
-- 执行顺序: update-tables.sql → update-datas.sql

-- ----------------------------
-- Table structure for notify_notice
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."notify_notice" (
  "id" int8 NOT NULL,
  "title" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "severity" varchar(32) COLLATE "pg_catalog"."default",
  "is_top" bool NOT NULL DEFAULT false,
  "effective_time" timestamptz(6),
  "expire_time" timestamptz(6),
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamptz(6),
  PRIMARY KEY ("id")
);

COMMENT ON TABLE "public"."notify_notice" IS '公告通知(广播型)';
COMMENT ON COLUMN "public"."notify_notice"."id" IS '主键';
COMMENT ON COLUMN "public"."notify_notice"."title" IS '标题';
COMMENT ON COLUMN "public"."notify_notice"."content" IS '正文(Markdown原文)';
COMMENT ON COLUMN "public"."notify_notice"."severity" IS '重要程度(normal普通/important重要)';
COMMENT ON COLUMN "public"."notify_notice"."is_top" IS '是否置顶';
COMMENT ON COLUMN "public"."notify_notice"."effective_time" IS '生效时间(为空则立即生效)';
COMMENT ON COLUMN "public"."notify_notice"."expire_time" IS '过期时间(为空则永久有效)';
COMMENT ON COLUMN "public"."notify_notice"."status" IS '状态(draft草稿/published发布/offline下线)';
COMMENT ON COLUMN "public"."notify_notice"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."notify_notice"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."notify_notice"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."notify_notice"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."notify_notice"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."notify_notice"."create_time" IS '创建时间';

CREATE INDEX IF NOT EXISTS "idx_notify_notice_status" ON "public"."notify_notice" ("status");
CREATE INDEX IF NOT EXISTS "idx_notify_notice_create_time" ON "public"."notify_notice" ("create_time" DESC);

-- ----------------------------
-- Table structure for notify_notice_read
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."notify_notice_read" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "notice_id" int8 NOT NULL,
  "read_time" timestamptz(6),
  "is_ignored" bool NOT NULL DEFAULT false,
  "creator" int8,
  "create_time" timestamptz(6),
  PRIMARY KEY ("id")
);

COMMENT ON TABLE "public"."notify_notice_read" IS '公告已读记录(用户 x 公告)';
COMMENT ON COLUMN "public"."notify_notice_read"."id" IS '主键';
COMMENT ON COLUMN "public"."notify_notice_read"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."notify_notice_read"."notice_id" IS '公告ID';
COMMENT ON COLUMN "public"."notify_notice_read"."read_time" IS '阅读时间';
COMMENT ON COLUMN "public"."notify_notice_read"."is_ignored" IS '是否忽略(用户主动隐藏该公告)';
COMMENT ON COLUMN "public"."notify_notice_read"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."notify_notice_read"."create_time" IS '创建时间';

CREATE UNIQUE INDEX IF NOT EXISTS "uk_notify_notice_read_user_notice"
  ON "public"."notify_notice_read" ("user_id", "notice_id");
CREATE INDEX IF NOT EXISTS "idx_notify_notice_read_user" ON "public"."notify_notice_read" ("user_id");

-- ----------------------------
-- Table structure for notify_message
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."notify_message" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "title" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "source" varchar(64) COLLATE "pg_catalog"."default",
  "link" varchar(512) COLLATE "pg_catalog"."default",
  "extra" text COLLATE "pg_catalog"."default",
  "is_read" bool NOT NULL DEFAULT false,
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamptz(6),
  PRIMARY KEY ("id")
);

COMMENT ON TABLE "public"."notify_message" IS '个人消息(定向通知, 预留)';
COMMENT ON COLUMN "public"."notify_message"."id" IS '主键';
COMMENT ON COLUMN "public"."notify_message"."user_id" IS '接收用户ID';
COMMENT ON COLUMN "public"."notify_message"."title" IS '标题';
COMMENT ON COLUMN "public"."notify_message"."content" IS '正文内容';
COMMENT ON COLUMN "public"."notify_message"."source" IS '业务来源(预留)';
COMMENT ON COLUMN "public"."notify_message"."link" IS '跳转链接(内部路由或完整http外链)';
COMMENT ON COLUMN "public"."notify_message"."extra" IS '跳转附加参数(JSON字符串)';
COMMENT ON COLUMN "public"."notify_message"."is_read" IS '是否已读';
COMMENT ON COLUMN "public"."notify_message"."deleted" IS '逻辑删除';
COMMENT ON COLUMN "public"."notify_message"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."notify_message"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."notify_message"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."notify_message"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."notify_message"."create_time" IS '创建时间';

CREATE INDEX IF NOT EXISTS "idx_notify_message_user" ON "public"."notify_message" ("user_id");
CREATE INDEX IF NOT EXISTS "idx_notify_message_user_read" ON "public"."notify_message" ("user_id", "is_read");

-- ----------------------------
-- pay_trade: 入账金额 posted_amount
-- 结算类 SUCCESS = amount; 预授权冻结(authorize)等非结算动作 = 0
-- ----------------------------
ALTER TABLE "public"."pay_trade" ADD COLUMN IF NOT EXISTS "posted_amount" int8 NOT NULL DEFAULT 0;
COMMENT ON COLUMN "public"."pay_trade"."posted_amount" IS '入账金额(最小货币单位); 结算类成功=amount, 预授权冻结等非结算动作=0';

-- 存量成功单回填(authorize 保持 0)
UPDATE "public"."pay_trade"
SET "posted_amount" = COALESCE("amount", 0)
WHERE "status" = 'success'
  AND COALESCE("trade_type", '') <> 'authorize'
  AND COALESCE("posted_amount", 0) = 0;

-- ----------------------------
-- channel_app_id: 订单/退款通道应用 AppId 快照
-- ----------------------------
ALTER TABLE "public"."pay_normal_order" ADD COLUMN IF NOT EXISTS "channel_app_id" varchar(128);
COMMENT ON COLUMN "public"."pay_normal_order"."channel_app_id" IS '通道应用AppId(本笔交易实际使用)';

ALTER TABLE "public"."pay_gateway_order" ADD COLUMN IF NOT EXISTS "channel_app_id" varchar(128);
COMMENT ON COLUMN "public"."pay_gateway_order"."channel_app_id" IS '通道应用AppId(本笔交易实际使用)';

ALTER TABLE "public"."pay_refund_order" ADD COLUMN IF NOT EXISTS "channel_app_id" varchar(128);
COMMENT ON COLUMN "public"."pay_refund_order"."channel_app_id" IS '通道应用AppId(继承自原支付单)';

-- ----------------------------
-- 支付订单号体系: orderNo(业务单) 与 tradeNo(资金) 分离; payBody 仅容器; trade 去掉 pay_body/expired_time
-- ----------------------------
ALTER TABLE "public"."pay_normal_order" ADD COLUMN IF NOT EXISTS "order_no" varchar(64);
COMMENT ON COLUMN "public"."pay_normal_order"."order_no" IS '平台业务单号(与 trade_no 独立; 普通通道默认上送)';

ALTER TABLE "public"."pay_gateway_order" ADD COLUMN IF NOT EXISTS "pay_body" text;
COMMENT ON COLUMN "public"."pay_gateway_order"."pay_body" IS '支付参数体(已拉起缓存, 仅容器)';
ALTER TABLE "public"."pay_gateway_order" ADD COLUMN IF NOT EXISTS "pay_body_type" varchar(32);
COMMENT ON COLUMN "public"."pay_gateway_order"."pay_body_type" IS '支付参数体类型';

-- 实际上送串索引(回调反查)
CREATE INDEX IF NOT EXISTS "idx_pay_trade_relation_order_no" ON "public"."pay_trade" ("relation_order_no");
CREATE INDEX IF NOT EXISTS "idx_pay_normal_order_order_no" ON "public"."pay_normal_order" ("order_no");

-- trade 瘦身: pay_body / pay_body_type / expired_time 迁出或废弃(若列存在则删除)
ALTER TABLE "public"."pay_trade" DROP COLUMN IF EXISTS "pay_body";
ALTER TABLE "public"."pay_trade" DROP COLUMN IF EXISTS "pay_body_type";
ALTER TABLE "public"."pay_trade" DROP COLUMN IF EXISTS "expired_time";

COMMENT ON COLUMN "public"."pay_trade"."relation_order_no" IS '实际上送通道的商户订单号(普通=order_no, 特殊=变形号; 回调反查权威)';
