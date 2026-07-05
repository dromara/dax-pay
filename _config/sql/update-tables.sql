-- 用户工作台快捷入口偏好表: 用户个性化工作台快捷入口(显隐+排序), PC与移动按 client_code 分开管理

CREATE TABLE IF NOT EXISTS "public"."iam_user_dashboard_preference" (
    "id"                  bigint       NOT NULL PRIMARY KEY,
    "user_id"             bigint       NOT NULL,
    "client_code"         varchar(32)  NOT NULL,
    "entries"             jsonb        NOT NULL DEFAULT '[]'::jsonb,
    "creator"             bigint,
    "create_time"         timestamptz(6),
    "last_modifier"       bigint,
    "last_modified_time"  timestamptz(6),
    "version"             integer      NOT NULL DEFAULT 0,
    "deleted"             boolean      NOT NULL DEFAULT false
);

COMMENT ON TABLE  "public"."iam_user_dashboard_preference" IS '用户工作台快捷入口偏好';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."id" IS '主键';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."client_code" IS '终端编码(WEB/MOBILE), PC与移动分开管理';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."entries" IS '已选快捷入口有序序列(纯key数组), 如 ["merchant","notify"]';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."iam_user_dashboard_preference"."deleted" IS '逻辑删除标志';

-- 每用户每终端唯一一份配置(仅在未逻辑删除时生效, 允许删后重建)
CREATE UNIQUE INDEX IF NOT EXISTS "uk_user_das_pref_user_client"
    ON "public"."iam_user_dashboard_preference" ("user_id", "client_code")
    WHERE "deleted" = false;

-- ============================================================
-- 微信服务商子商户应用 / 能力绑定 / 授权配置
-- ============================================================

-- 微信服务商通道商户应用(子商户自己的微信应用, channelMchNo 维度)
CREATE TABLE IF NOT EXISTS "public"."wechat_isv_mch_app" (
    "id"                  bigint       NOT NULL PRIMARY KEY,
    "mch_no"              varchar(32)  NOT NULL,
    "channel_mch_no"      varchar(64)  NOT NULL,
    "app_name"            varchar(128) NOT NULL,
    "app_type"            varchar(32)  NOT NULL,
    "wx_app_id"           varchar(64)  NOT NULL,
    "creator"             bigint,
    "create_time"         timestamptz(6),
    "last_modifier"       bigint,
    "last_modified_time"  timestamptz(6),
    "version"             integer      NOT NULL DEFAULT 0,
    "deleted"             boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."wechat_isv_mch_app" IS '微信服务商通道商户应用(子商户自己的微信应用)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."channel_mch_no" IS '通道商户号(服务商特约商户)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."app_type" IS '应用类型(official_account/mini_program/mobile_app)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."wx_app_id" IS '微信应用AppId(对应微信支付sub_appid)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_mch_app"."deleted" IS '逻辑删除标志';
CREATE UNIQUE INDEX IF NOT EXISTS "uk_wechat_isv_mch_app_channel_wxappid"
    ON "public"."wechat_isv_mch_app" ("channel_mch_no", "wx_app_id")
    WHERE "deleted" = false;

-- 微信服务商通道商户应用支付能力关联(channelMchNo 维度, 仅存子商户显式选自己应用的记录)
CREATE TABLE IF NOT EXISTS "public"."wechat_isv_mch_app_capability" (
    "id"                      bigint       NOT NULL PRIMARY KEY,
    "mch_no"                  varchar(32)  NOT NULL,
    "channel_mch_no"          varchar(64)  NOT NULL,
    "capability"              varchar(64)  NOT NULL,
    "wechat_isv_mch_app_id"   bigint       NOT NULL,
    "creator"                 bigint,
    "create_time"             timestamptz(6),
    "last_modifier"           bigint,
    "last_modified_time"      timestamptz(6),
    "version"                 integer      NOT NULL DEFAULT 0,
    "deleted"                 boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."wechat_isv_mch_app_capability" IS '微信服务商通道商户应用支付能力关联';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."channel_mch_no" IS '通道商户号(服务商特约商户)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."capability" IS '支付能力编码';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."wechat_isv_mch_app_id" IS '关联服务商通道商户应用ID(指向wechat_isv_mch_app.id)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_capability"."deleted" IS '逻辑删除标志';
CREATE UNIQUE INDEX IF NOT EXISTS "uk_wechat_isv_mch_app_cap_channel_cap"
    ON "public"."wechat_isv_mch_app_capability" ("channel_mch_no", "capability")
    WHERE "deleted" = false;

-- 微信服务商通道商户应用授权配置(appSecret等, 一对一)
CREATE TABLE IF NOT EXISTS "public"."wechat_isv_mch_app_auth_config" (
    "id"                      bigint       NOT NULL PRIMARY KEY,
    "mch_no"                  varchar(32)  NOT NULL,
    "channel_mch_no"          varchar(64)  NOT NULL,
    "wechat_isv_mch_app_id"   bigint       NOT NULL,
    "app_secret"              varchar(512),
    "auth_callback_url"       varchar(512),
    "creator"                 bigint,
    "create_time"             timestamptz(6),
    "last_modifier"           bigint,
    "last_modified_time"      timestamptz(6),
    "version"                 integer      NOT NULL DEFAULT 0,
    "deleted"                 boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."wechat_isv_mch_app_auth_config" IS '微信服务商通道商户应用授权配置';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."channel_mch_no" IS '通道商户号(服务商特约商户)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."wechat_isv_mch_app_id" IS '关联服务商通道商户应用ID';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."auth_callback_url" IS '授权回调地址(仅公众号)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_mch_app_auth_config"."deleted" IS '逻辑删除标志';
CREATE UNIQUE INDEX IF NOT EXISTS "uk_wechat_isv_mch_app_auth_channel_app"
    ON "public"."wechat_isv_mch_app_auth_config" ("channel_mch_no", "wechat_isv_mch_app_id")
    WHERE "deleted" = false;
