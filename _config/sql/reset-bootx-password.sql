-- ============================================================
-- bootx 超级管理员密码重置为开发默认密码(121212) | 2026-08-31
-- 用途: 开发库密码改动后忘记时, 对目标库直接执行本脚本急救, 无需重新导库
-- 哈希与 _config/script/redact-data.mjs 的 BOOTX_DEV_PASSWORD_HASH 共用同一固定值,
-- 换默认密码时两处(及 data.sql 的 bootx 行)必须同步修改
-- 哈希由项目同款 hutool BCrypt.hashpw 生成, 登录侧 AbstractPasswordLoginHandler 的 checkpw 校验互通
-- 本机无 psql, 通过 JDBC 单文件脚本执行
-- ============================================================

-- 重置 bootx 密码为开发默认密码(仅命中内置超管, 按 id+账号双条件防误伤)
UPDATE "public"."iam_user_info"
SET password    = '$2a$10$HiIvaX7tbGWDeRVSciX/LuIAIYUgVJwasWtstsXsakpt0d9Sw.cKG',
    last_modifier = 1,
    last_modified_time = now()
WHERE id = 1 AND account = 'bootx';

-- 顺带清除密码错误计数与锁定状态(重置前反复试错可能已锁死; 无记录时本句静默跳过)
UPDATE "public"."iam_user_password_security"
SET password_error_count = 0,
    lock_time = NULL
WHERE id = 1;
