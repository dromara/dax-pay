-- ----------------------------
-- 支付宝服务商应用表
-- ----------------------------
DROP TABLE IF EXISTS alipay_isv_app;
CREATE TABLE alipay_isv_app (
    id int8 NOT NULL,
    app_name varchar(64) COLLATE pg_catalog.default,
    ali_app_id varchar(64) COLLATE pg_catalog.default,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE alipay_isv_app IS '支付宝服务商应用表';
COMMENT ON COLUMN alipay_isv_app.id IS '主键';
COMMENT ON COLUMN alipay_isv_app.app_name IS '应用名称';
COMMENT ON COLUMN alipay_isv_app.ali_app_id IS '支付宝应用APPID';
COMMENT ON COLUMN alipay_isv_app.creator IS '创建者ID';
COMMENT ON COLUMN alipay_isv_app.create_time IS '创建时间';
COMMENT ON COLUMN alipay_isv_app.last_modifier IS '最后修改ID';
COMMENT ON COLUMN alipay_isv_app.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN alipay_isv_app.version IS '版本号';
COMMENT ON COLUMN alipay_isv_app.deleted IS '删除标志';

ALTER TABLE alipay_isv_app ADD CONSTRAINT pk_alipay_isv_app PRIMARY KEY (id);

-- ----------------------------
-- 支付宝服务商应用密钥配置表
-- ----------------------------
DROP TABLE IF EXISTS alipay_isv_app_key_config;
CREATE TABLE alipay_isv_app_key_config (
    id int8 NOT NULL,
    app_id int8,
    auth_type varchar(32) COLLATE pg_catalog.default,
    alipay_public_key text COLLATE pg_catalog.default,
    private_key text COLLATE pg_catalog.default,
    app_cert text COLLATE pg_catalog.default,
    alipay_cert text COLLATE pg_catalog.default,
    alipay_root_cert text COLLATE pg_catalog.default,
    secret_key text COLLATE pg_catalog.default,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE alipay_isv_app_key_config IS '支付宝服务商应用密钥配置表';
COMMENT ON COLUMN alipay_isv_app_key_config.id IS '主键';
COMMENT ON COLUMN alipay_isv_app_key_config.app_id IS '关联应用ID';
COMMENT ON COLUMN alipay_isv_app_key_config.auth_type IS '认证类型';
COMMENT ON COLUMN alipay_isv_app_key_config.alipay_public_key IS '支付宝公钥';
COMMENT ON COLUMN alipay_isv_app_key_config.private_key IS '商户私钥';
COMMENT ON COLUMN alipay_isv_app_key_config.app_cert IS '应用公钥证书';
COMMENT ON COLUMN alipay_isv_app_key_config.alipay_cert IS '支付宝公钥证书';
COMMENT ON COLUMN alipay_isv_app_key_config.alipay_root_cert IS '支付宝根证书';
COMMENT ON COLUMN alipay_isv_app_key_config.secret_key IS '密钥';
COMMENT ON COLUMN alipay_isv_app_key_config.creator IS '创建者ID';
COMMENT ON COLUMN alipay_isv_app_key_config.create_time IS '创建时间';
COMMENT ON COLUMN alipay_isv_app_key_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN alipay_isv_app_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN alipay_isv_app_key_config.version IS '版本号';
COMMENT ON COLUMN alipay_isv_app_key_config.deleted IS '删除标志';

ALTER TABLE alipay_isv_app_key_config ADD CONSTRAINT pk_alipay_isv_app_key_config PRIMARY KEY (id);

-- ----------------------------
-- 支付宝服务商应用授权认证配置表
-- ----------------------------
DROP TABLE IF EXISTS alipay_isv_app_auth_config;
CREATE TABLE alipay_isv_app_auth_config (
    id int8 NOT NULL,
    app_id int8,
    user_id_type varchar(32) COLLATE pg_catalog.default,
    auth_callback_url varchar(512) COLLATE pg_catalog.default,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE alipay_isv_app_auth_config IS '支付宝服务商应用授权认证配置表';
COMMENT ON COLUMN alipay_isv_app_auth_config.id IS '主键';
COMMENT ON COLUMN alipay_isv_app_auth_config.app_id IS '关联应用ID';
COMMENT ON COLUMN alipay_isv_app_auth_config.user_id_type IS '用户ID类型';
COMMENT ON COLUMN alipay_isv_app_auth_config.auth_callback_url IS '授权回调URL';
COMMENT ON COLUMN alipay_isv_app_auth_config.creator IS '创建者ID';
COMMENT ON COLUMN alipay_isv_app_auth_config.create_time IS '创建时间';
COMMENT ON COLUMN alipay_isv_app_auth_config.last_modifier IS '最后修改ID';
COMMENT ON COLUMN alipay_isv_app_auth_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN alipay_isv_app_auth_config.version IS '版本号';
COMMENT ON COLUMN alipay_isv_app_auth_config.deleted IS '删除标志';

ALTER TABLE alipay_isv_app_auth_config ADD CONSTRAINT pk_alipay_isv_app_auth_config PRIMARY KEY (id);
