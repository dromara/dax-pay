-- 添加上次登录失败时间字段
ALTER TABLE iam_user_password_security 
ADD COLUMN last_failure_time TIMESTAMP;

COMMENT ON COLUMN iam_user_password_security.last_failure_time IS '上次登录失败时间';
