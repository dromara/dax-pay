-- 初始化密码策略配置
INSERT INTO pay_platform_security_config (config_group, config_key, config_value, value_type, description, enabled) VALUES
('password_policy', 'enabled', 'true', 'BOOLEAN', '是否启用密码强度验证', true),
('password_policy', 'min_length', '8', 'INTEGER', '密码最小长度', true),
('password_policy', 'max_length', '32', 'INTEGER', '密码最大长度', true),
('password_policy', 'require_uppercase', 'true', 'BOOLEAN', '是否要求包含大写字母', true),
('password_policy', 'require_lowercase', 'true', 'BOOLEAN', '是否要求包含小写字母', true),
('password_policy', 'require_digit', 'true', 'BOOLEAN', '是否要求包含数字', true),
('password_policy', 'require_special_char', 'true', 'BOOLEAN', '是否要求包含特殊字符', true),
('password_policy', 'special_chars', '!@#$%^&*()_+-=[]{}|;:,.<>?', 'STRING', '特殊字符集合', true),
('password_policy', 'rotation_days', '90', 'INTEGER', '密码轮换周期（天），0或负数表示禁用', true),
('password_policy', 'history_count', '5', 'INTEGER', '密码历史记录数量，0表示禁用', true);

-- 初始化会话管理配置
INSERT INTO pay_platform_security_config (config_group, config_key, config_value, value_type, description, enabled) VALUES
('session_management', 'enabled', 'true', 'BOOLEAN', '是否启用会话管理', true),
('session_management', 'max_online_hours', '8', 'INTEGER', '最大在线时长（小时），0或负数表示无限制', true),
('session_management', 'max_concurrent_sessions', '3', 'INTEGER', '最大并发会话数，0或负数表示无限制', true),
('session_management', 'concurrent_strategy', 'REJECT_NEW', 'STRING', '并发会话策略：REJECT_NEW-拒绝新登录，KICK_OLDEST-踢出最早会话', true),
('session_management', 'auto_cleanup', 'true', 'BOOLEAN', '会话超时后是否自动清理', true);

-- 初始化登录安全配置
INSERT INTO pay_platform_security_config (config_group, config_key, config_value, value_type, description, enabled) VALUES
('login_security', 'lockout_enabled', 'true', 'BOOLEAN', '是否启用登录失败锁定', true),
('login_security', 'max_failed_attempts', '5', 'INTEGER', '最大登录失败次数', true),
('login_security', 'lockout_duration_minutes', '30', 'INTEGER', '账户锁定持续时间（分钟）', true),
('login_security', 'failure_reset_minutes', '15', 'INTEGER', '失败计数重置时间（分钟），超过此时间后重置失败计数', true);

-- 初始化 IP 访问控制配置
INSERT INTO pay_platform_security_config (config_group, config_key, config_value, value_type, description, enabled) VALUES
('ip_access_control', 'enabled', 'false', 'BOOLEAN', '是否启用 IP 白名单', true),
('ip_access_control', 'whitelist', '[]', 'JSON_ARRAY', 'IP 白名单列表，支持单个IP和CIDR格式', true),
('ip_access_control', 'blacklist_enabled', 'false', 'BOOLEAN', '是否启用 IP 黑名单', true),
('ip_access_control', 'blacklist', '[]', 'JSON_ARRAY', 'IP 黑名单列表', true),
('ip_access_control', 'mode', 'WHITELIST_ONLY', 'STRING', 'IP 限制模式：WHITELIST_ONLY-仅白名单，BLACKLIST_ONLY-仅黑名单，WHITELIST_PRIORITY-白名单优先', true);

-- 初始化双因素认证配置
INSERT INTO pay_platform_security_config (config_group, config_key, config_value, value_type, description, enabled) VALUES
('two_factor_auth', 'enabled', 'false', 'BOOLEAN', '是否启用双因素认证', true),
('two_factor_auth', 'mandatory', 'false', 'BOOLEAN', '是否强制所有用户启用 2FA', true),
('two_factor_auth', 'algorithm', 'HmacSHA1', 'STRING', 'TOTP 算法类型', true),
('two_factor_auth', 'time_step', '30', 'INTEGER', 'TOTP 时间步长（秒）', true),
('two_factor_auth', 'code_length', '6', 'INTEGER', 'TOTP 验证码长度', true),
('two_factor_auth', 'time_window_offset', '1', 'INTEGER', '允许的时间窗口偏移（前后各几个时间步）', true),
('two_factor_auth', 'issuer', 'DaxPay Platform', 'STRING', '发行者名称（显示在认证器应用中）', true);

-- 初始化审计日志配置
INSERT INTO pay_platform_security_config (config_group, config_key, config_value, value_type, description, enabled) VALUES
('audit_log', 'enabled', 'true', 'BOOLEAN', '是否启用审计日志', true),
('audit_log', 'log_login_success', 'true', 'BOOLEAN', '是否记录登录成功事件', true),
('audit_log', 'log_login_failure', 'true', 'BOOLEAN', '是否记录登录失败事件', true),
('audit_log', 'log_password_change', 'true', 'BOOLEAN', '是否记录密码修改事件', true),
('audit_log', 'log_account_lockout', 'true', 'BOOLEAN', '是否记录账户锁定/解锁事件', true),
('audit_log', 'log_config_change', 'true', 'BOOLEAN', '是否记录配置变更事件', true),
('audit_log', 'retention_days', '90', 'INTEGER', '日志保留天数，0表示永久保留', true),
('audit_log', 'storage', 'DATABASE', 'STRING', '日志存储方式：DATABASE-数据库存储，FILE-文件存储，BOTH-同时存储', true);

-- 初始化异常登录检测配置
INSERT INTO pay_platform_security_config (config_group, config_key, config_value, value_type, description, enabled) VALUES
('anomaly_detection', 'enabled', 'false', 'BOOLEAN', '是否启用异常登录检测', true),
('anomaly_detection', 'sensitivity_level', 'MEDIUM', 'STRING', '检测敏感度级别：LOW-低，MEDIUM-中，HIGH-高', true),
('anomaly_detection', 'detect_anomalous_ip', 'true', 'BOOLEAN', '是否检测异常 IP', true),
('anomaly_detection', 'detect_anomalous_time', 'true', 'BOOLEAN', '是否检测异常登录时间', true),
('anomaly_detection', 'detect_anomalous_device', 'true', 'BOOLEAN', '是否检测异常设备', true),
('anomaly_detection', 'ip_change_threshold', '80', 'INTEGER', 'IP 地址变化阈值（百分比），超过此值视为异常', true),
('anomaly_detection', 'time_deviation_threshold', '6', 'INTEGER', '登录时间偏离阈值（小时），超过此值视为异常', true),
('anomaly_detection', 'response_strategy', 'LOG_ONLY', 'STRING', '异常行为响应策略：LOG_ONLY-仅记录日志，LOG_AND_NOTIFY-记录并通知，LOG_NOTIFY_AND_CHALLENGE-记录通知并要求额外验证，LOG_NOTIFY_AND_BLOCK-记录通知并阻止登录', true),
('anomaly_detection', 'send_notification', 'false', 'BOOLEAN', '是否发送异常通知', true),
('anomaly_detection', 'notification_emails', '[]', 'JSON_ARRAY', '通知接收人邮箱列表', true),
('anomaly_detection', 'baseline_retention_days', '30', 'INTEGER', '用户行为基线数据保留天数', true);
