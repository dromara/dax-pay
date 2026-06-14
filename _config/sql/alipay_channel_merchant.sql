-- ----------------------------
-- 支付宝服务商通道商户绑定表
-- 一条记录代表"子商户挂靠在某个服务商应用下"的授权关系
-- ----------------------------
DROP TABLE IF EXISTS mch_alipay_isv_channel_merchant;
CREATE TABLE mch_alipay_isv_channel_merchant (
    id int8 NOT NULL,
    mch_no varchar(32) COLLATE pg_catalog."default",
    channel_mch_no varchar(64) COLLATE pg_catalog."default",
    product varchar(32) COLLATE pg_catalog."default",
    app_id int8,
    alipay_user_id varchar(32) COLLATE pg_catalog."default",
    app_auth_token varchar(128) COLLATE pg_catalog."default",
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE mch_alipay_isv_channel_merchant IS '支付宝服务商通道商户绑定表';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.id IS '主键';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.mch_no IS '商户号';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.channel_mch_no IS '通道商户号(AISV+雪花)';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.product IS '所属支付产品';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.app_id IS '关联服务商应用ID(系统主键, 指向alipay_isv_app.id)';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.alipay_user_id IS '子商户支付宝识别码(2088开头)';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.app_auth_token IS '应用授权令牌(会过期/刷新)';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.creator IS '创建者ID';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.create_time IS '创建时间';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.last_modifier IS '最后修改ID';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.version IS '版本号';
COMMENT ON COLUMN mch_alipay_isv_channel_merchant.deleted IS '删除标志';

ALTER TABLE mch_alipay_isv_channel_merchant ADD CONSTRAINT pk_mch_alipay_isv_channel_merchant PRIMARY KEY (id);
-- 同一服务商应用下子商户不重复
ALTER TABLE mch_alipay_isv_channel_merchant ADD CONSTRAINT uk_isv_app_alipay_user UNIQUE (app_id, alipay_user_id);

-- ----------------------------
-- 支付宝直连通道商户绑定表
-- 一个商户PID对应一个channelMchNo, 商户的多个应用共享此绑定
-- ----------------------------
DROP TABLE IF EXISTS mch_alipay_direct_channel_merchant;
CREATE TABLE mch_alipay_direct_channel_merchant (
    id int8 NOT NULL,
    mch_no varchar(32) COLLATE pg_catalog."default",
    channel_mch_no varchar(64) COLLATE pg_catalog."default",
    product varchar(32) COLLATE pg_catalog."default",
    alipay_user_id varchar(32) COLLATE pg_catalog."default",
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted bool NOT NULL DEFAULT false
);

COMMENT ON TABLE mch_alipay_direct_channel_merchant IS '支付宝直连通道商户绑定表';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.id IS '主键';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.mch_no IS '商户号';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.channel_mch_no IS '通道商户号(系统生成雪花号)';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.product IS '所属支付产品';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.alipay_user_id IS '支付宝商家唯一识别码(2088开头)';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.creator IS '创建者ID';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.create_time IS '创建时间';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.last_modifier IS '最后修改ID';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.version IS '版本号';
COMMENT ON COLUMN mch_alipay_direct_channel_merchant.deleted IS '删除标志';

ALTER TABLE mch_alipay_direct_channel_merchant ADD CONSTRAINT pk_mch_alipay_direct_channel_merchant PRIMARY KEY (id);
-- 直连一PID一channelMchNo: 同一商户下同一alipayUserId唯一
ALTER TABLE mch_alipay_direct_channel_merchant ADD CONSTRAINT uk_direct_mch_alipay_user UNIQUE (mch_no, alipay_user_id);
