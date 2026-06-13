-- ----------------------------
-- 微信服务商应用表
-- ----------------------------
DROP TABLE IF EXISTS wechat_isv_app;
CREATE TABLE wechat_isv_app (
    id int8 NOT NULL,
    app_name varchar(64) COLLATE pg_catalog.default,
    app_type varchar(32) COLLATE pg_catalog.default,
    wx_app_id varchar(64) COLLATE pg_catalog.default,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE wechat_isv_app IS '微信服务商应用表';
COMMENT ON COLUMN wechat_isv_app.id IS '主键';
COMMENT ON COLUMN wechat_isv_app.app_name IS '应用名称';
COMMENT ON COLUMN wechat_isv_app.app_type IS '应用类型';
COMMENT ON COLUMN wechat_isv_app.wx_app_id IS '微信应用AppId';
COMMENT ON COLUMN wechat_isv_app.creator IS '创建者ID';
COMMENT ON COLUMN wechat_isv_app.create_time IS '创建时间';
COMMENT ON COLUMN wechat_isv_app.last_modifier IS '最后修改ID';
COMMENT ON COLUMN wechat_isv_app.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN wechat_isv_app.version IS '版本号';
COMMENT ON COLUMN wechat_isv_app.deleted IS '删除标志';

ALTER TABLE wechat_isv_app ADD CONSTRAINT pk_wechat_isv_app PRIMARY KEY (id);

-- ----------------------------
-- 微信服务商密钥配置表（平台为唯一服务商，密钥全局唯一，按 product 区分）
-- ----------------------------
DROP TABLE IF EXISTS wechat_isv_key_config;
CREATE TABLE wechat_isv_key_config (
    id int8 NOT NULL,
    product varchar(32) COLLATE pg_catalog.default,
    wx_mch_id varchar(64) COLLATE pg_catalog.default,
    api_key_v3 text COLLATE pg_catalog.default,
    public_key text COLLATE pg_catalog.default,
    public_key_id varchar(128) COLLATE pg_catalog.default,
    private_key text COLLATE pg_catalog.default,
    private_cert text COLLATE pg_catalog.default,
    cert_serial_no varchar(128) COLLATE pg_catalog.default,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE wechat_isv_key_config IS '微信服务商密钥配置表';
COMMENT ON COLUMN wechat_isv_key_config.id IS '主键';
COMMENT ON COLUMN wechat_isv_key_config.product IS '产品编码';
COMMENT ON COLUMN wechat_isv_key_config.wx_mch_id IS '微信服务商商户号';
COMMENT ON COLUMN wechat_isv_key_config.api_key_v3 IS 'APIv3密钥';
COMMENT ON COLUMN wechat_isv_key_config.public_key IS '支付公钥';
COMMENT ON COLUMN wechat_isv_key_config.public_key_id IS '支付公钥ID';
COMMENT ON COLUMN wechat_isv_key_config.private_key IS 'API证书私钥';
COMMENT ON COLUMN wechat_isv_key_config.private_cert IS 'API证书';
COMMENT ON COLUMN wechat_isv_key_config.cert_serial_no IS '证书序列号';
COMMENT ON COLUMN wechat_isv_key_config.creator IS '创建者ID';
COMMENT ON COLUMN wechat_isv_key_config.create_time IS '创建时间';
COMMENT ON COLUMN wechat_isv_key_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN wechat_isv_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN wechat_isv_key_config.version IS '版本号';
COMMENT ON COLUMN wechat_isv_key_config.deleted IS '删除标志';

ALTER TABLE wechat_isv_key_config ADD CONSTRAINT pk_wechat_isv_key_config PRIMARY KEY (id);

-- ----------------------------
-- 微信服务商应用授权认证配置表
-- ----------------------------
DROP TABLE IF EXISTS wechat_isv_app_auth_config;
CREATE TABLE wechat_isv_app_auth_config (
    id int8 NOT NULL,
    app_id int8,
    app_secret text COLLATE pg_catalog.default,
    auth_callback_url varchar(512) COLLATE pg_catalog.default,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE wechat_isv_app_auth_config IS '微信服务商应用授权认证配置表';
COMMENT ON COLUMN wechat_isv_app_auth_config.id IS '主键';
COMMENT ON COLUMN wechat_isv_app_auth_config.app_id IS '关联应用ID';
COMMENT ON COLUMN wechat_isv_app_auth_config.app_secret IS '应用密钥';
COMMENT ON COLUMN wechat_isv_app_auth_config.auth_callback_url IS '授权回调URL';
COMMENT ON COLUMN wechat_isv_app_auth_config.creator IS '创建者ID';
COMMENT ON COLUMN wechat_isv_app_auth_config.create_time IS '创建时间';
COMMENT ON COLUMN wechat_isv_app_auth_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN wechat_isv_app_auth_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN wechat_isv_app_auth_config.version IS '版本号';
COMMENT ON COLUMN wechat_isv_app_auth_config.deleted IS '删除标志';

ALTER TABLE wechat_isv_app_auth_config ADD CONSTRAINT pk_wechat_isv_app_auth_config PRIMARY KEY (id);
