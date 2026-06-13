-- ============================================================
-- DDL 迁移脚本: timestamp → timestamptz
-- 将数据库所有时间列从 timestamp (无时区) 改为 timestamptz (带时区)
-- 现有数据按 Asia/Shanghai 时区解释后转为 UTC 存储
-- ============================================================
-- 按风险等级分批执行, 每批执行后验证数据再继续
-- Batch 1: 基础字典/参数配置 (低风险)
-- Batch 2: 审计日志 (低风险)
-- Batch 3: 文件管理 (低风险)
-- Batch 4: IAM 配置 (中风险)
-- Batch 5: IAM 用户 (中风险)
-- Batch 6: 通道配置表 (中风险)
-- Batch 7: 商户 (高风险)
-- Batch 8: 支付订单 (极高风险)
-- Batch 9: 对账/回调 (中风险)
-- ============================================================

-- ============================================================
-- 辅助函数: 安全转换 timestamp 列到 timestamptz
-- ============================================================
CREATE OR REPLACE FUNCTION migrate_timestamp_to_timestamptz(schema_name text, table_name text, column_name text)
RETURNS void AS $$
DECLARE
    col_type text;
    col_notnull text;
    col_default text;
BEGIN
    -- 获取当前列类型
    SELECT pg_catalog.format_type(a.atttypid, a.atttypmod)
    INTO col_type
    FROM pg_catalog.pg_attribute a
    JOIN pg_catalog.pg_class c ON a.attrelid = c.oid
    JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
    WHERE n.nspname = schema_name
      AND c.relname = table_name
      AND a.attname = column_name
      AND a.attnum > 0;

    IF col_type IS NULL THEN
        RAISE NOTICE '列 %.%.% 不存在，跳过', schema_name, table_name, column_name;
        RETURN;
    END IF;

    -- 检查是否有 NOT NULL 约束
    SELECT CASE WHEN a.attnotnull THEN 'NOT NULL' ELSE '' END
    INTO col_notnull
    FROM pg_catalog.pg_attribute a
    JOIN pg_catalog.pg_class c ON a.attrelid = c.oid
    JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
    WHERE n.nspname = schema_name
      AND c.relname = table_name
      AND a.attname = column_name
      AND a.attnum > 0;

    -- 获取默认值
    SELECT pg_get_expr(ad.adbin, ad.adrelid)
    INTO col_default
    FROM pg_catalog.pg_attrdef ad
    JOIN pg_catalog.pg_attribute a ON ad.adrelid = a.attrelid AND ad.adnum = a.attnum
    JOIN pg_catalog.pg_class c ON a.attrelid = c.oid
    JOIN pg_catalog.pg_namespace n ON c.relnamespace = n.oid
    WHERE n.nspname = schema_name
      AND c.relname = table_name
      AND a.attname = column_name;

    -- 新建 timestamptz 列, 现有值按 Asia/Shanghai 解释后转 UTC
    EXECUTE format('ALTER TABLE %I.%I ADD COLUMN %I_tz timestamptz %s',
                   schema_name, table_name, column_name, col_notnull);
    EXECUTE format('UPDATE %I.%I SET %I_tz = (%I AT TIME ZONE ''Asia/Shanghai'') AT TIME ZONE ''UTC''',
                   schema_name, table_name, column_name, column_name);
    EXECUTE format('ALTER TABLE %I.%I DROP COLUMN %I', schema_name, table_name, column_name);
    EXECUTE format('ALTER TABLE %I.%I RENAME COLUMN %I_tz TO %I', schema_name, table_name, column_name, column_name);

    -- 恢复默认值
    IF col_default IS NOT NULL THEN
        EXECUTE format('ALTER TABLE %I.%I ALTER COLUMN %I SET DEFAULT %s',
                       schema_name, table_name, column_name, col_default);
    END IF;

    RAISE NOTICE '已迁移 %.%.%', schema_name, table_name, column_name;
END;
$$ LANGUAGE plpgsql;

-- ============================================================
-- Batch 1: 基础字典/参数配置 (低风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'base_dict', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'base_dict', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'base_dict_item', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'base_dict_item', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'base_param', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'base_param', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'base_user_protocol', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'base_user_protocol', 'last_modified_time');

-- ============================================================
-- Batch 2: 审计日志 (低风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'starter_audit_login_log', 'login_time');
SELECT migrate_timestamp_to_timestamptz('public', 'starter_audit_operate_log', 'operate_time');

-- ============================================================
-- Batch 3: 文件管理 (低风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'starter_file_platform', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'starter_file_platform', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'starter_file_upload_info', 'create_time');

-- ============================================================
-- Batch 4: IAM 配置 (中风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'iam_client', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_client', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_perm_code', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_perm_code', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_perm_menu', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_perm_menu', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_role', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_role', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user', 'last_modified_time');

-- ============================================================
-- Batch 5: IAM 用户 (中风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_expand_info', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_expand_info', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_expand_info', 'last_login_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_expand_info', 'register_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_expand_info', 'current_login_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_expand_info', 'last_change_password_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_info', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_info', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_password_history', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'iam_user_password_security', 'last_failure_time');

-- ============================================================
-- Batch 6: 通道配置表 (中风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'pay_adapay_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_adapay_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_aggregate_bar_pay_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_aggregate_bar_pay_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_aggregate_pay_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_aggregate_pay_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_aggregate_qr_pay_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_aggregate_qr_pay_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_alipay_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_alipay_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_alipay_isv_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_alipay_isv_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_alipay_sub_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_alipay_sub_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_channel_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_channel_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_wechat_pay_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_wechat_pay_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_wechat_isv_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_wechat_isv_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_wechat_sub_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_wechat_sub_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_mini_quickly_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_mini_quickly_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_basic_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_basic_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_integration_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_integration_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_url_config', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_url_config', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_security_config', 'created_at');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_security_config', 'updated_at');

-- ============================================================
-- Batch 7: 商户 (高风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_user', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_mch_app', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_mch_app', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_credential', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_credential', 'last_modified_time');

-- ============================================================
-- Batch 8: 支付订单 (极高风险 - 需先在预发布环境验证)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order', 'expired_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order', 'pay_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order', 'close_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order_expand', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order_expand', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_order_expand', 'req_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_refund_order', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_refund_order', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_refund_order', 'finish_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_refund_order', 'req_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_transfer_order', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_transfer_order', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_transfer_order', 'finish_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_transfer_order', 'req_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_onb_mch_info', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_onb_mch_info', 'last_modified_time');

-- ============================================================
-- Batch 9: 对账/回调/消息 (中风险)
-- ============================================================
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_callback_task', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_callback_task', 'last_modified_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_callback_task', 'next_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_callback_task', 'latest_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_merchant_callback_record', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_trade_callback_record', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_trade_flow_record', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_trade_sync_record', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_close_record', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_wechat_message_record', 'send_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_wechat_message_record', 'create_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_wechat_message_record', 'update_time');
SELECT migrate_timestamp_to_timestamptz('public', 'pay_platform_security_config_history', 'modified_at');

-- ============================================================
-- 清理辅助函数
-- ============================================================
DROP FUNCTION IF EXISTS migrate_timestamp_to_timestamptz(text, text, text);

-- ============================================================
-- 验证迁移结果
-- ============================================================
-- 检查是否还有任何 timestamp 类型的列
-- SELECT table_name, column_name, data_type
-- FROM information_schema.columns
-- WHERE table_schema = 'public'
--   AND data_type = 'timestamp without time zone'
--   AND table_name LIKE 'pay_%' OR table_name LIKE 'iam_%' OR table_name LIKE 'starter_%' OR table_name LIKE 'base_%';
