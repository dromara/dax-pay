-- 用户密码历史表
CREATE TABLE IF NOT EXISTS iam_user_password_history (
    id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    password_hash VARCHAR(200) NOT NULL,
    creator BIGINT,
    create_time TIMESTAMP,
    PRIMARY KEY (id)
);

COMMENT ON TABLE iam_user_password_history IS '用户密码历史表';
COMMENT ON COLUMN iam_user_password_history.id IS '主键';
COMMENT ON COLUMN iam_user_password_history.user_id IS '用户ID';
COMMENT ON COLUMN iam_user_password_history.password_hash IS '密码哈希值';
COMMENT ON COLUMN iam_user_password_history.creator IS '创建者ID';
COMMENT ON COLUMN iam_user_password_history.create_time IS '创建时间';

CREATE INDEX IF NOT EXISTS idx_password_history_user_id ON iam_user_password_history(user_id);
COMMENT ON INDEX idx_password_history_user_id IS '用户ID索引';
