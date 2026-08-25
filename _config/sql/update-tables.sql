-- ----------------------------
-- 表结构升级脚本 (2026-08-25)
-- 内容:
--   一、新增 iam_user_passkey 表(用户通行密钥 WebAuthn 凭据绑定, Passkey 登录功能)
--       与全量 table.sql 中同名表结构保持一致, 幂等可重复执行。
-- ----------------------------

-- ----------------------------
-- 一、iam_user_passkey(用户通行密钥绑定)
-- ----------------------------

-- 建表(幂等: 表已存在时跳过)
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM information_schema.tables
                 WHERE table_schema = 'public' AND table_name = 'iam_user_passkey') THEN
    CREATE TABLE "public"."iam_user_passkey" (
      "id" int8 NOT NULL,
      "user_id" int8 NOT NULL,
      "client_code" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
      "credential_id" varchar(512) COLLATE "pg_catalog"."default" NOT NULL,
      "public_key" varchar(1024) COLLATE "pg_catalog"."default" NOT NULL,
      "sign_count" int8 NOT NULL DEFAULT 0,
      "device_name" varchar(128) COLLATE "pg_catalog"."default",
      "transports" varchar(128) COLLATE "pg_catalog"."default",
      "backup_eligible" bool NOT NULL DEFAULT false,
      "backup_state" bool NOT NULL DEFAULT false,
      "last_used_time" timestamptz(6),
      "creator" int8,
      "create_time" timestamptz(6),
      "last_modifier" int8,
      "last_modified_time" timestamptz(6),
      "version" int4 NOT NULL DEFAULT 0,
      "deleted" bool NOT NULL DEFAULT false
    );
  END IF;
END $$;

COMMENT ON TABLE "public"."iam_user_passkey" IS '用户通行密钥(WebAuthn 凭据)绑定记录';
COMMENT ON COLUMN "public"."iam_user_passkey"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_passkey"."user_id" IS '用户ID(关联 iam_user_info.id)';
COMMENT ON COLUMN "public"."iam_user_passkey"."client_code" IS '终端编码(admin/merchant)';
COMMENT ON COLUMN "public"."iam_user_passkey"."credential_id" IS 'WebAuthn 凭据ID(base64url)';
COMMENT ON COLUMN "public"."iam_user_passkey"."public_key" IS 'COSE 公钥(base64url)';
COMMENT ON COLUMN "public"."iam_user_passkey"."sign_count" IS '签名计数(防认证器克隆)';
COMMENT ON COLUMN "public"."iam_user_passkey"."device_name" IS '设备可辨识名(用户自定义)';
COMMENT ON COLUMN "public"."iam_user_passkey"."transports" IS '凭据传输方式(internal/hybrid/usb/nfc/ble, 逗号分隔)';
COMMENT ON COLUMN "public"."iam_user_passkey"."backup_eligible" IS '凭据是否可多设备同步(passkey)';
COMMENT ON COLUMN "public"."iam_user_passkey"."backup_state" IS '凭据当前是否处于同步状态';
COMMENT ON COLUMN "public"."iam_user_passkey"."last_used_time" IS '最后使用时间';
COMMENT ON COLUMN "public"."iam_user_passkey"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."iam_user_passkey"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_passkey"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."iam_user_passkey"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_passkey"."version" IS '版本号(乐观锁)';
COMMENT ON COLUMN "public"."iam_user_passkey"."deleted" IS '逻辑删除标志';

-- 唯一索引(同一 WebAuthn 凭据全局唯一, 只能绑定一个用户; 部分索引排除逻辑删除行)
CREATE UNIQUE INDEX IF NOT EXISTS "uk_iam_user_passkey_credential_id" ON "public"."iam_user_passkey" USING btree (
  "credential_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_iam_user_passkey_credential_id" IS '同一 WebAuthn 凭据全局唯一(只能绑定一个用户)';

-- 普通索引(按用户 ID 查询已绑定通行密钥)
CREATE INDEX IF NOT EXISTS "idx_iam_user_passkey_user_id" ON "public"."iam_user_passkey" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
COMMENT ON INDEX "public"."idx_iam_user_passkey_user_id" IS '按用户 ID 查询已绑定通行密钥';

-- 主键(幂等: 约束已存在时跳过)
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint
                 WHERE conname = 'iam_user_passkey_pkey'
                   AND conrelid = '"public"."iam_user_passkey"'::regclass) THEN
    ALTER TABLE "public"."iam_user_passkey" ADD CONSTRAINT "iam_user_passkey_pkey" PRIMARY KEY ("id");
  END IF;
END $$;
