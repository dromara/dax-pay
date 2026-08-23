-- ----------------------------
-- 表结构升级脚本 (2026-08-23)
-- 内容:
--   一、历史遗留 timestamp without time zone 列统一转 timestamptz(6), 共 21 张表 44 列
--       背景: 项目约定时间字段统一 timestamptz(6) + 实体 OffsetDateTime(UTC)。
--       历史列存的是 UTC 字面量(自定义 OffsetDateTimeTypeHandler 统一按 UTC 写入),
--       故迁移统一 USING "列" AT TIME ZONE 'UTC' 还原正确绝对时刻。
--       每表包 DO 条件块: 列已是 timestamptz 时跳过, 重复执行无副作用(幂等)。
--   二、补齐缺失的列注释 52 条 (COMMENT ON COLUMN)
--   三、补齐缺失的索引注释 6 条 (COMMENT ON INDEX)
-- ----------------------------

-- ----------------------------
-- 一、timestamp without time zone → timestamptz(6)
-- ----------------------------

-- 通道商户配置类
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'hkrt_isv_channel_merchant'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."hkrt_isv_channel_merchant"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'hkrt_isv_key_config'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."hkrt_isv_key_config"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

-- 账号安全
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'iam_user_password_security'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."iam_user_password_security"
      ALTER COLUMN "lock_time" TYPE timestamptz(6) USING "lock_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "password_expire_time" TYPE timestamptz(6) USING "password_expire_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_change_password_time" TYPE timestamptz(6) USING "last_change_password_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

-- 商户域
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'mch_app_info'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."mch_app_info"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'mch_credential'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."mch_credential"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'mch_info'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."mch_info"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'mch_risk_config'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."mch_risk_config"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

-- mch_user 仅 create_time 违规(last_modified_time 本就是 timestamptz), DEFAULT CURRENT_TIMESTAMP 改型后语义自动正确
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'mch_user'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."mch_user"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

-- 支付元数据 (pay_md_* 7 张表, 种子数据表)
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'pay_md_capability'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."pay_md_capability"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'pay_md_channel'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."pay_md_channel"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'pay_md_method'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."pay_md_method"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'pay_md_product'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."pay_md_product"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'pay_md_product_capability'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."pay_md_product_capability"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'pay_md_provider'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."pay_md_provider"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'pay_md_provider_method'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."pay_md_provider_method"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

-- 审计日志
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'starter_audit_login_log'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."starter_audit_login_log"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'starter_audit_operate_log'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."starter_audit_operate_log"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

-- 平台基础配置
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'system_dict'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."system_dict"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'system_dict_item'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."system_dict_item"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'system_platform_config'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."system_platform_config"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.columns
             WHERE table_schema = 'public' AND table_name = 'system_platform_encrypt_config'
               AND column_name = 'create_time' AND data_type = 'timestamp without time zone') THEN
    ALTER TABLE "public"."system_platform_encrypt_config"
      ALTER COLUMN "create_time" TYPE timestamptz(6) USING "create_time" AT TIME ZONE 'UTC',
      ALTER COLUMN "last_modified_time" TYPE timestamptz(6) USING "last_modified_time" AT TIME ZONE 'UTC';
  END IF;
END $$;

-- ----------------------------
-- 二、补齐缺失的列注释 (52 条)
-- 文案规范: 审计公共列沿用全库多数派范式, 业务列抄对应实体类注释
-- ----------------------------

-- 通道密钥/商户配置表主键
COMMENT ON COLUMN "public"."adapay_direct_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."hmpay_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."hmpay_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."lakala_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."lakala_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."ums_direct_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."union_key_config"."id" IS '主键';

-- 转账/分账单主键
COMMENT ON COLUMN "public"."pay_transfer_trade"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_transfer_order_wechat"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_transfer_order_douyin"."id" IS '主键';

-- 支付元数据能力表审计列
COMMENT ON COLUMN "public"."pay_md_product_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_md_product_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."pay_md_product_capability"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."pay_md_product_capability"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."pay_md_product_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."pay_md_product_capability"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_md_product_capability"."deleted" IS '逻辑删除标志';

-- 支付宝直连应用
COMMENT ON COLUMN "public"."alipay_direct_app"."app_type" IS '应用类型: mini_program-小程序 mobile_app-移动应用 web_app-网站应用';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_direct_app_capability"."version" IS '乐观锁版本号';

-- 商户应用通知配置审计列
COMMENT ON COLUMN "public"."mch_app_notify_config"."id" IS '主键';
COMMENT ON COLUMN "public"."mch_app_notify_config"."creator" IS '创建者ID';
COMMENT ON COLUMN "public"."mch_app_notify_config"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."mch_app_notify_config"."last_modifier" IS '最后修改者ID';
COMMENT ON COLUMN "public"."mch_app_notify_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."mch_app_notify_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."mch_app_notify_config"."deleted" IS '逻辑删除标志';

-- 权限/账号域审计列
COMMENT ON COLUMN "public"."iam_perm_code"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_perm_code"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_perm_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_perm_menu"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_role"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_role"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_info"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."iam_user_password_history"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_modified_time" IS '最后修改时间';

-- 账号业务时间列(文案抄实体注释)
COMMENT ON COLUMN "public"."iam_user_expand_info"."last_login_time" IS '上次登录时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."register_time" IS '注册时间';
COMMENT ON COLUMN "public"."iam_user_expand_info"."current_login_time" IS '本次登录时间';
COMMENT ON COLUMN "public"."iam_user_password_security"."last_failure_time" IS '上次登录失败时间';

-- 支付元数据/微信能力/审计/地区业务列(文案抄实体注释)
COMMENT ON COLUMN "public"."pay_md_provider"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."pay_md_provider"."description" IS '描述';
COMMENT ON COLUMN "public"."wx_platform_app_capability"."product" IS '支付产品编码';
COMMENT ON COLUMN "public"."starter_audit_login_log"."login_time" IS '访问时间';
COMMENT ON COLUMN "public"."starter_audit_operate_log"."operate_time" IS '操作时间';
COMMENT ON COLUMN "public"."starter_audit_unipay_log"."req_id" IS '请求ID(商户传入, 审计索引)';
COMMENT ON COLUMN "public"."base_area"."code" IS '区域编码';

-- ----------------------------
-- 三、补齐缺失的索引注释 (6 条)
-- ----------------------------
COMMENT ON INDEX "public"."uk_iam_social_config_source" IS '同一社交登录来源唯一';
COMMENT ON INDEX "public"."mch_user_mch_no_user_id_key" IS '同一商户同一用户唯一';
COMMENT ON INDEX "public"."uk_notify_notice_read" IS '同一用户同一通知已读记录唯一';
COMMENT ON INDEX "public"."idx_pay_gateway_pay_env_config_id" IS '网关支付环境外键查询索引(关联 pay_gateway_pay_config 主键)';
COMMENT ON INDEX "public"."uk_pay_gateway_pay_env" IS '同一支付配置同客户端环境同支付形态唯一(部分唯一索引)';
COMMENT ON INDEX "public"."uk_pay_gateway_pay_config_app" IS '同一支付网关应用唯一(部分唯一索引)';

-- ----------------------------
-- 四、精简冗长注释 (23 条, 2026-08-23)
-- 规范: 注释只保留主标题, 枚举取值展开(code-说明/取值域列举)不入注释,
--       取值含义以字典/文档为准; 用途/约束类括号说明(加密存储/部分唯一索引等)不受影响
-- ----------------------------
COMMENT ON COLUMN "public"."alipay_direct_app"."app_type" IS '应用类型';
COMMENT ON COLUMN "public"."iam_perm_menu"."menu_type" IS '菜单类型';
COMMENT ON COLUMN "public"."iam_perm_menu"."badge_type" IS '徽章类型';
COMMENT ON COLUMN "public"."mch_app_notify_config"."notify_way" IS '传输通道';
COMMENT ON COLUMN "public"."pay_blacklist"."status" IS '状态';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."app_type" IS '端类型';
COMMENT ON COLUMN "public"."pay_risk_hit"."phase" IS '命中阶段';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."receiver_type" IS '接收方类型';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."status" IS '绑定状态';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."receiver_type" IS '接收方类型';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."status" IS '绑定状态';
COMMENT ON COLUMN "public"."base_user_protocol_version"."status" IS '状态';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."receiver_type" IS '接收方类型';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."status" IS '绑定状态';
COMMENT ON COLUMN "public"."mch_store_info"."status" IS '状态';
COMMENT ON COLUMN "public"."notify_notice"."severity" IS '重要程度';
COMMENT ON COLUMN "public"."notify_notice"."status" IS '状态';
COMMENT ON COLUMN "public"."starter_platform_file_record"."access_type" IS '访问类型';
COMMENT ON COLUMN "public"."starter_platform_file_record"."status" IS '上传状态';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."receiver_type" IS '接收方类型';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."status" IS '绑定状态';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."receiver_type" IS '接收方类型';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."status" IS '绑定状态';
