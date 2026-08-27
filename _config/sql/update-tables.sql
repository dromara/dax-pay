-- 升级表结构脚本: 新增邮件发送记录表
-- 邮件通知功能(平台配置→邮件发件箱 / 通知中心→邮件发送记录)数据落表;
-- DDL 与全量脚本 table.sql 中同表保持一致, 索引注释以本脚本为准。
-- 脚本幂等(DROP 后 CREATE), 可重复执行。

-- ----------------------------
-- Table structure for notify_mail_record
-- ----------------------------
DROP TABLE IF EXISTS "public"."notify_mail_record";
CREATE TABLE "public"."notify_mail_record" (
  "id" int8 NOT NULL,
  "receiver_email" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_user_id" int8,
  "subject" varchar(500) COLLATE "pg_catalog"."default" NOT NULL,
  "content" text COLLATE "pg_catalog"."default",
  "business_type" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "error_msg" varchar(2000) COLLATE "pg_catalog"."default",
  "retry_count" int4 NOT NULL DEFAULT 0,
  "send_time" timestamptz(6),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."notify_mail_record"."id" IS '主键';
COMMENT ON COLUMN "public"."notify_mail_record"."receiver_email" IS '收件邮箱';
COMMENT ON COLUMN "public"."notify_mail_record"."receiver_user_id" IS '收件用户ID(关联iam_user_info, 非用户发送可空)';
COMMENT ON COLUMN "public"."notify_mail_record"."subject" IS '邮件主题';
COMMENT ON COLUMN "public"."notify_mail_record"."content" IS '邮件正文(HTML)';
COMMENT ON COLUMN "public"."notify_mail_record"."business_type" IS '业务场景(test测试发送/manual手动发送等)';
COMMENT ON COLUMN "public"."notify_mail_record"."status" IS '发送状态(sending发送中/success成功/fail失败)';
COMMENT ON COLUMN "public"."notify_mail_record"."error_msg" IS '失败原因';
COMMENT ON COLUMN "public"."notify_mail_record"."retry_count" IS '重试次数';
COMMENT ON COLUMN "public"."notify_mail_record"."send_time" IS '实际发送时间';
COMMENT ON COLUMN "public"."notify_mail_record"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."notify_mail_record"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."notify_mail_record"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."notify_mail_record"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."notify_mail_record"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."notify_mail_record"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."notify_mail_record" IS '邮件发送记录';

-- ----------------------------
-- Primary Key structure for notify_mail_record
-- ----------------------------
ALTER TABLE "public"."notify_mail_record" ADD CONSTRAINT "pk_notify_mail_record" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for notify_mail_record
-- ----------------------------
CREATE INDEX "idx_notify_mail_record_create_time" ON "public"."notify_mail_record" USING btree ("create_time");
CREATE INDEX "idx_notify_mail_record_receiver_email" ON "public"."notify_mail_record" USING btree ("receiver_email");
CREATE INDEX "idx_notify_mail_record_status" ON "public"."notify_mail_record" USING btree ("status");
COMMENT ON INDEX "public"."idx_notify_mail_record_create_time" IS '创建时间索引: 按时间倒序排查近期发送记录';
COMMENT ON INDEX "public"."idx_notify_mail_record_receiver_email" IS '收件邮箱索引: 按邮箱查询发送历史';
COMMENT ON INDEX "public"."idx_notify_mail_record_status" IS '发送状态索引: 按状态筛选(失败记录排查与重发)';
