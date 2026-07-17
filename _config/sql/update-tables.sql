-- 支付风控：黑名单配置 + 风险命中记录（daxpay-plugin-risk）
-- 升级脚本：已部署库执行本文件；COMMENT 使用 PG 标准 COMMENT ON

-- ----------------------------
-- Table structure for pay_blacklist
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."pay_blacklist" (
  "id" int8 NOT NULL,
  "type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "value" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "channel_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "reason" varchar(255) COLLATE "pg_catalog"."default",
  "expire_time" timestamptz(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
);

COMMENT ON TABLE "public"."pay_blacklist" IS '支付黑名单配置';
COMMENT ON COLUMN "public"."pay_blacklist"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_blacklist"."type" IS '类型: ip/open_id';
COMMENT ON COLUMN "public"."pay_blacklist"."value" IS '名单值(IP或openId)';
COMMENT ON COLUMN "public"."pay_blacklist"."channel" IS '通道族(openId建议wechat/alipay, IP可空)';
COMMENT ON COLUMN "public"."pay_blacklist"."channel_app_id" IS '通道应用AppId(可选)';
COMMENT ON COLUMN "public"."pay_blacklist"."status" IS '状态: enable/disable';
COMMENT ON COLUMN "public"."pay_blacklist"."reason" IS '拉黑原因';
COMMENT ON COLUMN "public"."pay_blacklist"."expire_time" IS '过期时间(空=永久)';
COMMENT ON COLUMN "public"."pay_blacklist"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_blacklist"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_blacklist"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_blacklist"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_blacklist"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_blacklist"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_blacklist"."deleted" IS '删除标志';

ALTER TABLE "public"."pay_blacklist" DROP CONSTRAINT IF EXISTS "pay_blacklist_pkey";
ALTER TABLE "public"."pay_blacklist" ADD CONSTRAINT "pay_blacklist_pkey" PRIMARY KEY ("id");

CREATE INDEX IF NOT EXISTS "idx_pay_blacklist_type_value" ON "public"."pay_blacklist" ("type", "value");
CREATE INDEX IF NOT EXISTS "idx_pay_blacklist_status_expire" ON "public"."pay_blacklist" ("status", "expire_time");

-- ----------------------------
-- Table structure for pay_risk_hit
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."pay_risk_hit" (
  "id" int8 NOT NULL,
  "phase" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "hit_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "hit_value" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
  "blacklist_id" int8,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "trade_no" varchar(64) COLLATE "pg_catalog"."default",
  "order_no" varchar(64) COLLATE "pg_catalog"."default",
  "biz_order_no" varchar(100) COLLATE "pg_catalog"."default",
  "trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "method" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(32) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "openid" varchar(128) COLLATE "pg_catalog"."default",
  "buyer_id" varchar(128) COLLATE "pg_catalog"."default",
  "scene" varchar(32) COLLATE "pg_catalog"."default",
  "handle_status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'pending',
  "handle_remark" varchar(500) COLLATE "pg_catalog"."default",
  "handle_user_id" int8,
  "handle_time" timestamptz(6),
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
);

COMMENT ON TABLE "public"."pay_risk_hit" IS '支付风险命中记录';
COMMENT ON COLUMN "public"."pay_risk_hit"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_risk_hit"."phase" IS '阶段: before_pay/after_pay';
COMMENT ON COLUMN "public"."pay_risk_hit"."hit_type" IS '命中类型: ip/open_id';
COMMENT ON COLUMN "public"."pay_risk_hit"."hit_value" IS '命中值快照';
COMMENT ON COLUMN "public"."pay_risk_hit"."blacklist_id" IS '关联黑名单ID';
COMMENT ON COLUMN "public"."pay_risk_hit"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_risk_hit"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_risk_hit"."trade_no" IS '平台交易号';
COMMENT ON COLUMN "public"."pay_risk_hit"."order_no" IS '容器单号';
COMMENT ON COLUMN "public"."pay_risk_hit"."biz_order_no" IS '商户业务单号';
COMMENT ON COLUMN "public"."pay_risk_hit"."trade_type" IS '交易类型';
COMMENT ON COLUMN "public"."pay_risk_hit"."method" IS '支付方式';
COMMENT ON COLUMN "public"."pay_risk_hit"."product" IS '支付产品';
COMMENT ON COLUMN "public"."pay_risk_hit"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_risk_hit"."client_ip" IS '客户端IP快照';
COMMENT ON COLUMN "public"."pay_risk_hit"."openid" IS '下单openId快照';
COMMENT ON COLUMN "public"."pay_risk_hit"."buyer_id" IS '通道buyerId快照';
COMMENT ON COLUMN "public"."pay_risk_hit"."scene" IS '来源场景: api/gateway/code/manual';
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_status" IS '处理状态: pending/ignored/added_blacklist/merchant_disabled/other';
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_remark" IS '处理说明';
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_user_id" IS '处理人ID';
COMMENT ON COLUMN "public"."pay_risk_hit"."handle_time" IS '处理时间';
COMMENT ON COLUMN "public"."pay_risk_hit"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_risk_hit"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_risk_hit"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_risk_hit"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_risk_hit"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_risk_hit"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_risk_hit"."deleted" IS '删除标志';

ALTER TABLE "public"."pay_risk_hit" DROP CONSTRAINT IF EXISTS "pay_risk_hit_pkey";
ALTER TABLE "public"."pay_risk_hit" ADD CONSTRAINT "pay_risk_hit_pkey" PRIMARY KEY ("id");

CREATE INDEX IF NOT EXISTS "idx_pay_risk_hit_handle_time" ON "public"."pay_risk_hit" ("handle_status", "create_time");
CREATE INDEX IF NOT EXISTS "idx_pay_risk_hit_type_value" ON "public"."pay_risk_hit" ("hit_type", "hit_value");
CREATE INDEX IF NOT EXISTS "idx_pay_risk_hit_mch_no" ON "public"."pay_risk_hit" ("mch_no");
CREATE INDEX IF NOT EXISTS "idx_pay_risk_hit_trade_no" ON "public"."pay_risk_hit" ("trade_no");
CREATE INDEX IF NOT EXISTS "idx_pay_risk_hit_order_no" ON "public"."pay_risk_hit" ("order_no");

-- ----------------------------
-- 敏感词词库 + 命中审计（capability-sensitive-word）
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."system_sensitive_word" (
  "id" int8 NOT NULL,
  "word" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "category" varchar(32) COLLATE "pg_catalog"."default",
  "match_mode" varchar(16) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'contains',
  "level" varchar(16) COLLATE "pg_catalog"."default" DEFAULT 'reject',
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
);

COMMENT ON TABLE "public"."system_sensitive_word" IS '敏感词词库';
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

ALTER TABLE "public"."system_sensitive_word" DROP CONSTRAINT IF EXISTS "system_sensitive_word_pkey";
ALTER TABLE "public"."system_sensitive_word" ADD CONSTRAINT "system_sensitive_word_pkey" PRIMARY KEY ("id");

CREATE UNIQUE INDEX IF NOT EXISTS "uk_system_sensitive_word_word" ON "public"."system_sensitive_word" ("word") WHERE deleted = false;
CREATE INDEX IF NOT EXISTS "idx_system_sensitive_word_status" ON "public"."system_sensitive_word" ("status");

CREATE TABLE IF NOT EXISTS "public"."system_sensitive_word_hit" (
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
);

COMMENT ON TABLE "public"."system_sensitive_word_hit" IS '敏感词命中记录';
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

ALTER TABLE "public"."system_sensitive_word_hit" DROP CONSTRAINT IF EXISTS "system_sensitive_word_hit_pkey";
ALTER TABLE "public"."system_sensitive_word_hit" ADD CONSTRAINT "system_sensitive_word_hit_pkey" PRIMARY KEY ("id");

CREATE INDEX IF NOT EXISTS "idx_system_sensitive_word_hit_time" ON "public"."system_sensitive_word_hit" ("create_time");
CREATE INDEX IF NOT EXISTS "idx_system_sensitive_word_hit_word" ON "public"."system_sensitive_word_hit" ("hit_word");
CREATE INDEX IF NOT EXISTS "idx_system_sensitive_word_hit_mch" ON "public"."system_sensitive_word_hit" ("mch_no", "create_time");
CREATE INDEX IF NOT EXISTS "idx_system_sensitive_word_hit_scene" ON "public"."system_sensitive_word_hit" ("scene");

-- ----------------------------
-- Table structure for pay_gateway_cashier_item
-- 网关收银台支付项配置(应用级): H5 按 client_env 五档 / MINI 四档 / WEB 扁平(client_env 空)
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."pay_gateway_cashier_item" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "app_id" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "cashier_type" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "client_env" varchar(32) COLLATE "pg_catalog"."default",
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "icon" varchar(32) COLLATE "pg_catalog"."default",
  "recommend" bool NOT NULL DEFAULT false,
  "sort_no" int4 DEFAULT 0,
  "resolve_mode" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "method" varchar(32) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "capability" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
);

COMMENT ON TABLE "public"."pay_gateway_cashier_item" IS '网关收银台支付项配置(应用级)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."cashier_type" IS '收银台类型: h5/web/mini';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."client_env" IS '客户端环境(H5五档/MINI四档; WEB为空)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."name" IS '前台展示名称';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."icon" IS '图标编码(与 PayProvider 对齐)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."recommend" IS '是否推荐';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."sort_no" IS '排序号(越小越前)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."resolve_mode" IS '解析模式: method/direct';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."method" IS '支付方式(METHOD 模式)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."channel_mch_no" IS '通道商户号(DIRECT 模式)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."capability" IS '支付能力(DIRECT 模式)';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."version" IS '版本号';
COMMENT ON COLUMN "public"."pay_gateway_cashier_item"."deleted" IS '删除标志';

ALTER TABLE "public"."pay_gateway_cashier_item" DROP CONSTRAINT IF EXISTS "pay_gateway_cashier_item_pkey";
ALTER TABLE "public"."pay_gateway_cashier_item" ADD CONSTRAINT "pay_gateway_cashier_item_pkey" PRIMARY KEY ("id");

CREATE INDEX IF NOT EXISTS "idx_pay_gateway_cashier_item_bucket"
  ON "public"."pay_gateway_cashier_item" ("app_id", "cashier_type", "client_env");
CREATE INDEX IF NOT EXISTS "idx_pay_gateway_cashier_item_mch"
  ON "public"."pay_gateway_cashier_item" ("mch_no", "app_id");

-- ----------------------------
-- Table structure for starter_audit_unipay_log（统一支付接口审计日志）
-- 索引键：mch_no + req_id；不单独存 appId/业务单号（可在 body 中查看）
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."starter_audit_unipay_log" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "req_id" varchar(64) COLLATE "pg_catalog"."default",
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
  "operate_time" timestamptz(6)
);

COMMENT ON TABLE "public"."starter_audit_unipay_log" IS '统一支付接口审计日志';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."id" IS '主键';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."req_id" IS '请求ID(商户传入,审计主索引)';
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

ALTER TABLE "public"."starter_audit_unipay_log" DROP CONSTRAINT IF EXISTS "starter_audit_unipay_log_pkey";
ALTER TABLE "public"."starter_audit_unipay_log" ADD CONSTRAINT "starter_audit_unipay_log_pkey" PRIMARY KEY ("id");

-- 若已按旧版建表：补 req_id、删业务键列（审计表可重建，无历史依赖）
ALTER TABLE "public"."starter_audit_unipay_log" ADD COLUMN IF NOT EXISTS "req_id" varchar(64);
ALTER TABLE "public"."starter_audit_unipay_log" DROP COLUMN IF EXISTS "app_id";
ALTER TABLE "public"."starter_audit_unipay_log" DROP COLUMN IF EXISTS "channel_mch_no";
ALTER TABLE "public"."starter_audit_unipay_log" DROP COLUMN IF EXISTS "biz_order_no";
ALTER TABLE "public"."starter_audit_unipay_log" DROP COLUMN IF EXISTS "order_no";
ALTER TABLE "public"."starter_audit_unipay_log" DROP COLUMN IF EXISTS "trade_no";

DROP INDEX IF EXISTS "idx_starter_audit_unipay_log_biz_order";
CREATE INDEX IF NOT EXISTS "idx_starter_audit_unipay_log_time" ON "public"."starter_audit_unipay_log" ("operate_time" DESC);
CREATE INDEX IF NOT EXISTS "idx_starter_audit_unipay_log_mch_time" ON "public"."starter_audit_unipay_log" ("mch_no", "operate_time" DESC);
CREATE INDEX IF NOT EXISTS "idx_starter_audit_unipay_log_mch_req" ON "public"."starter_audit_unipay_log" ("mch_no", "req_id");
CREATE INDEX IF NOT EXISTS "idx_starter_audit_unipay_log_req" ON "public"."starter_audit_unipay_log" ("req_id");
CREATE INDEX IF NOT EXISTS "idx_starter_audit_unipay_log_trace" ON "public"."starter_audit_unipay_log" ("trace_id");
CREATE INDEX IF NOT EXISTS "idx_starter_audit_unipay_log_success_time" ON "public"."starter_audit_unipay_log" ("success", "operate_time" DESC);

-- ----------------------------
-- Table structure for pay_refund_order（退款订单）
-- 对齐实体 RefundOrder：trade_no + trade_type + relation_order_no，无 method
-- capability 仅通道凭证组装运行时使用，管理端不展示
-- ----------------------------
CREATE TABLE IF NOT EXISTS "public"."pay_refund_order" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default",
  "app_id" varchar(32) COLLATE "pg_catalog"."default",
  "refund_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "biz_refund_no" varchar(100) COLLATE "pg_catalog"."default",
  "relation_order_no" varchar(100) COLLATE "pg_catalog"."default",
  "trade_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "trade_type" varchar(32) COLLATE "pg_catalog"."default",
  "biz_order_no" varchar(100) COLLATE "pg_catalog"."default",
  "out_order_no" varchar(150) COLLATE "pg_catalog"."default",
  "title" varchar(200) COLLATE "pg_catalog"."default",
  "out_refund_no" varchar(150) COLLATE "pg_catalog"."default",
  "amount" int8 NOT NULL,
  "order_amount" int8,
  "currency" varchar(8) COLLATE "pg_catalog"."default",
  "reason" varchar(256) COLLATE "pg_catalog"."default",
  "status" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "finish_time" timestamptz(6),
  "error_msg" varchar(500) COLLATE "pg_catalog"."default",
  "channel" varchar(32) COLLATE "pg_catalog"."default",
  "product" varchar(64) COLLATE "pg_catalog"."default",
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "capability" varchar(64) COLLATE "pg_catalog"."default",
  "channel_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "notify_url" varchar(256) COLLATE "pg_catalog"."default",
  "attach" varchar(512) COLLATE "pg_catalog"."default",
  "client_ip" varchar(64) COLLATE "pg_catalog"."default",
  "store_no" varchar(64) COLLATE "pg_catalog"."default",
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
);

COMMENT ON TABLE "public"."pay_refund_order" IS '退款订单';
COMMENT ON COLUMN "public"."pay_refund_order"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_refund_order"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."pay_refund_order"."app_id" IS '应用号';
COMMENT ON COLUMN "public"."pay_refund_order"."refund_no" IS '平台退款号';
COMMENT ON COLUMN "public"."pay_refund_order"."biz_refund_no" IS '商户退款号';
COMMENT ON COLUMN "public"."pay_refund_order"."relation_order_no" IS '实际上送通道的商户退款关联号(普通=refund_no,特殊通道可变形)';
COMMENT ON COLUMN "public"."pay_refund_order"."trade_no" IS '原支付资金交易号(pay_trade.trade_no)';
COMMENT ON COLUMN "public"."pay_refund_order"."trade_type" IS '原支付交易形态(normal/gateway等, 冗余自 pay_trade.trade_type)';
COMMENT ON COLUMN "public"."pay_refund_order"."biz_order_no" IS '商户业务订单号(冗余)';
COMMENT ON COLUMN "public"."pay_refund_order"."out_order_no" IS '通道原支付订单号';
COMMENT ON COLUMN "public"."pay_refund_order"."title" IS '标题(冗余自原支付)';
COMMENT ON COLUMN "public"."pay_refund_order"."out_refund_no" IS '通道退款流水号';
COMMENT ON COLUMN "public"."pay_refund_order"."amount" IS '退款金额(分)';
COMMENT ON COLUMN "public"."pay_refund_order"."order_amount" IS '原支付总金额(分)';
COMMENT ON COLUMN "public"."pay_refund_order"."currency" IS '币种';
COMMENT ON COLUMN "public"."pay_refund_order"."reason" IS '退款原因';
COMMENT ON COLUMN "public"."pay_refund_order"."status" IS '退款状态: progress/success/fail/close';
COMMENT ON COLUMN "public"."pay_refund_order"."finish_time" IS '退款完成时间';
COMMENT ON COLUMN "public"."pay_refund_order"."error_msg" IS '错误信息';
COMMENT ON COLUMN "public"."pay_refund_order"."channel" IS '支付通道';
COMMENT ON COLUMN "public"."pay_refund_order"."product" IS '支付产品编码';
COMMENT ON COLUMN "public"."pay_refund_order"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."pay_refund_order"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."pay_refund_order"."channel_app_id" IS '通道应用AppId';
COMMENT ON COLUMN "public"."pay_refund_order"."notify_url" IS '商户异步通知地址';
COMMENT ON COLUMN "public"."pay_refund_order"."attach" IS '商户附加参数';
COMMENT ON COLUMN "public"."pay_refund_order"."client_ip" IS '客户端IP';
COMMENT ON COLUMN "public"."pay_refund_order"."store_no" IS '门店号';
COMMENT ON COLUMN "public"."pay_refund_order"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_refund_order"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_refund_order"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_refund_order"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_refund_order"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."pay_refund_order"."deleted" IS '删除标志';

ALTER TABLE "public"."pay_refund_order" DROP CONSTRAINT IF EXISTS "pay_refund_order_pkey";
ALTER TABLE "public"."pay_refund_order" ADD CONSTRAINT "pay_refund_order_pkey" PRIMARY KEY ("id");

CREATE UNIQUE INDEX IF NOT EXISTS "uk_pay_refund_order_refund_no" ON "public"."pay_refund_order" ("refund_no") WHERE deleted = false;
CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_relation_no" ON "public"."pay_refund_order" ("relation_order_no");
CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_out_refund_no" ON "public"."pay_refund_order" ("out_refund_no");
CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_trade_no" ON "public"."pay_refund_order" ("trade_no");
CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_trade_type" ON "public"."pay_refund_order" ("trade_type");
CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_biz_refund_no" ON "public"."pay_refund_order" ("biz_refund_no");
CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_biz_order_no" ON "public"."pay_refund_order" ("biz_order_no");
CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_mch_time" ON "public"."pay_refund_order" ("mch_no", "create_time" DESC);

-- 若旧表已存在 order_no / method 列，迁移到新模型（幂等）
DO $$
BEGIN
  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'public' AND table_name = 'pay_refund_order' AND column_name = 'order_no'
  ) AND NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'public' AND table_name = 'pay_refund_order' AND column_name = 'trade_no'
  ) THEN
    ALTER TABLE "public"."pay_refund_order" RENAME COLUMN "order_no" TO "trade_no";
  END IF;

  IF EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'public' AND table_name = 'pay_refund_order' AND column_name = 'method'
  ) THEN
    ALTER TABLE "public"."pay_refund_order" DROP COLUMN "method";
  END IF;

  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'public' AND table_name = 'pay_refund_order' AND column_name = 'relation_order_no'
  ) THEN
    ALTER TABLE "public"."pay_refund_order" ADD COLUMN "relation_order_no" varchar(100);
    UPDATE "public"."pay_refund_order" SET "relation_order_no" = "refund_no" WHERE "relation_order_no" IS NULL;
  END IF;

  -- 交易类型：冗余自 pay_trade.trade_type
  IF NOT EXISTS (
    SELECT 1 FROM information_schema.columns
    WHERE table_schema = 'public' AND table_name = 'pay_refund_order' AND column_name = 'trade_type'
  ) THEN
    ALTER TABLE "public"."pay_refund_order" ADD COLUMN "trade_type" varchar(32);
  END IF;
END $$;

COMMENT ON COLUMN "public"."pay_refund_order"."trade_type" IS '原支付交易形态(normal/gateway等, 冗余自 pay_trade.trade_type)';

-- 历史数据回填 trade_type
UPDATE "public"."pay_refund_order" r
SET "trade_type" = t."trade_type"
FROM "public"."pay_trade" t
WHERE r."trade_no" = t."trade_no"
  AND r."trade_type" IS NULL
  AND (t."deleted" = false OR t."deleted" IS NULL);

CREATE INDEX IF NOT EXISTS "idx_pay_refund_order_trade_type" ON "public"."pay_refund_order" ("trade_type");
