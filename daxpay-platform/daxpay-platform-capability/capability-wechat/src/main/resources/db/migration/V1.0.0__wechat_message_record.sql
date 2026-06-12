-- 微信消息记录表
CREATE TABLE IF NOT EXISTS pay_platform_wechat_message_record (
    id BIGSERIAL PRIMARY KEY,
    message_type VARCHAR(20) NOT NULL,
    open_id VARCHAR(64) NOT NULL,
    template_id VARCHAR(64) NOT NULL,
    template_data TEXT,
    url VARCHAR(500),
    status VARCHAR(20) NOT NULL,
    msg_id VARCHAR(64),
    error_code VARCHAR(20),
    error_msg VARCHAR(500),
    send_time TIMESTAMP,
    scene VARCHAR(50),
    app_id VARCHAR(32),
    creator BIGINT,
    create_time TIMESTAMP,
    updater BIGINT,
    update_time TIMESTAMP,
    deleted SMALLINT DEFAULT 0
);

-- 添加注释
COMMENT ON TABLE pay_platform_wechat_message_record IS '微信消息记录表';
COMMENT ON COLUMN pay_platform_wechat_message_record.id IS '主键ID';
COMMENT ON COLUMN pay_platform_wechat_message_record.message_type IS '消息类型：template-公众号模板消息，uniform-小程序统一服务消息';
COMMENT ON COLUMN pay_platform_wechat_message_record.open_id IS '接收者OpenId';
COMMENT ON COLUMN pay_platform_wechat_message_record.template_id IS '模板ID';
COMMENT ON COLUMN pay_platform_wechat_message_record.template_data IS '模板数据（JSON格式）';
COMMENT ON COLUMN pay_platform_wechat_message_record.url IS '跳转链接或小程序页面路径';
COMMENT ON COLUMN pay_platform_wechat_message_record.status IS '发送状态：success-成功，failed-失败，retry-待重试';
COMMENT ON COLUMN pay_platform_wechat_message_record.msg_id IS '微信消息ID';
COMMENT ON COLUMN pay_platform_wechat_message_record.error_code IS '错误码';
COMMENT ON COLUMN pay_platform_wechat_message_record.error_msg IS '错误信息';
COMMENT ON COLUMN pay_platform_wechat_message_record.send_time IS '发送时间';
COMMENT ON COLUMN pay_platform_wechat_message_record.scene IS '业务场景标识';
COMMENT ON COLUMN pay_platform_wechat_message_record.app_id IS '使用的AppId';
COMMENT ON COLUMN pay_platform_wechat_message_record.creator IS '创建者';
COMMENT ON COLUMN pay_platform_wechat_message_record.create_time IS '创建时间';
COMMENT ON COLUMN pay_platform_wechat_message_record.updater IS '更新者';
COMMENT ON COLUMN pay_platform_wechat_message_record.update_time IS '更新时间';
COMMENT ON COLUMN pay_platform_wechat_message_record.deleted IS '删除标志';

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_wechat_msg_open_id ON pay_platform_wechat_message_record(open_id);
CREATE INDEX IF NOT EXISTS idx_wechat_msg_status ON pay_platform_wechat_message_record(status);
CREATE INDEX IF NOT EXISTS idx_wechat_msg_scene ON pay_platform_wechat_message_record(scene);
CREATE INDEX IF NOT EXISTS idx_wechat_msg_send_time ON pay_platform_wechat_message_record(send_time);
CREATE INDEX IF NOT EXISTS idx_wechat_msg_app_id ON pay_platform_wechat_message_record(app_id);
