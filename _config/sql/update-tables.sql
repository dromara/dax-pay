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

-- 移动端应用配置(平台级, 按端类型+移动平台维度, 每组合一条)
CREATE TABLE IF NOT EXISTS "public"."pay_platform_mobile_app" (
    "id"                  bigint       NOT NULL PRIMARY KEY,
    "app_type"            varchar(20)  NOT NULL,
    "platform"            varchar(20)  NOT NULL,
    "app_name"            varchar(100),
    "app_config"          jsonb,
    "notify_config"       jsonb,
    "binding_enabled"     boolean      NOT NULL DEFAULT false,
    "enabled"             boolean      NOT NULL DEFAULT true,
    "remark"              varchar(500),
    "creator"             bigint,
    "create_time"         timestamptz(6),
    "last_modifier"       bigint,
    "last_modified_time"  timestamptz(6),
    "version"             integer      NOT NULL DEFAULT 0,
    "deleted"             boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."pay_platform_mobile_app" IS '移动端应用配置(平台级, 按端+平台维度)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."id" IS '主键';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."app_type" IS '端类型(merchant商户端/admin管理端/cashier收银台)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."platform" IS '移动平台(wx_h5微信公众号/wx_mini微信小程序/alipay_mini支付宝小程序/dy_mini抖音小程序/android安卓/ios iOS)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."app_name" IS '应用名称(展示用)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."app_config" IS '平台特有密钥配置(jsonb, 敏感字段加密存储)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."notify_config" IS '消息通知配置(jsonb)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."binding_enabled" IS '是否启用第三方账号用户绑定';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."enabled" IS '是否启用';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."remark" IS '备注';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."pay_platform_mobile_app"."deleted" IS '逻辑删除标志';
CREATE UNIQUE INDEX IF NOT EXISTS "uk_pay_mobile_app_type_platform"
    ON "public"."pay_platform_mobile_app" ("app_type", "platform")
    WHERE "deleted" = false;

-- ============================================================
-- 抖音支付直连: 通道商户绑定 / 应用 / 密钥 / 能力 / 授权配置
-- ============================================================

-- 抖音直连通道商户绑定(一个抖音商户号 dy_mch_id 对应一个 channel_mch_no)
CREATE TABLE IF NOT EXISTS "public"."douyin_direct_channel_merchant" (
    "id"                  bigint       NOT NULL PRIMARY KEY,
    "mch_no"              varchar(32)  NOT NULL,
    "channel_mch_no"      varchar(64)  NOT NULL,
    "product"             varchar(32)  NOT NULL,
    "dy_mch_id"           varchar(64)  NOT NULL,
    "creator"             bigint,
    "create_time"         timestamptz(6),
    "last_modifier"       bigint,
    "last_modified_time"  timestamptz(6),
    "version"             integer      NOT NULL DEFAULT 0,
    "deleted"             boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."douyin_direct_channel_merchant" IS '抖音直连通道商户绑定';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."channel_mch_no" IS '通道商户号(系统生成雪花号)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."product" IS '所属支付产品(如 douyin_pay)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."dy_mch_id" IS '抖音商户号(MCHID)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_channel_merchant"."deleted" IS '逻辑删除标志';
-- 同一商户下抖音商户号不重复
CREATE UNIQUE INDEX IF NOT EXISTS "uk_douyin_direct_mch_dyid"
    ON "public"."douyin_direct_channel_merchant" ("mch_no", "dy_mch_id")
    WHERE "deleted" = false;

-- 抖音直连商户应用(每个应用关联一个通道商户号, 拥有独立的抖音应用 AppId)
CREATE TABLE IF NOT EXISTS "public"."douyin_direct_app" (
    "id"                  bigint       NOT NULL PRIMARY KEY,
    "mch_no"              varchar(32)  NOT NULL,
    "channel_mch_no"      varchar(64)  NOT NULL,
    "app_name"            varchar(128) NOT NULL,
    "douyin_app_id"       varchar(64)  NOT NULL,
    "app_type"            varchar(32)  NOT NULL,
    "creator"             bigint,
    "create_time"         timestamptz(6),
    "last_modifier"       bigint,
    "last_modified_time"  timestamptz(6),
    "version"             integer      NOT NULL DEFAULT 0,
    "deleted"             boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."douyin_direct_app" IS '抖音直连商户应用';
COMMENT ON COLUMN "public"."douyin_direct_app"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_app"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."douyin_direct_app"."app_name" IS '应用名称';
COMMENT ON COLUMN "public"."douyin_direct_app"."douyin_app_id" IS '抖音应用AppId(APPID)';
COMMENT ON COLUMN "public"."douyin_direct_app"."app_type" IS '应用类型(mini_program小程序/mobile_app移动应用/web_app网站应用)';
COMMENT ON COLUMN "public"."douyin_direct_app"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."douyin_direct_app"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_app"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."douyin_direct_app"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_app"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_app"."deleted" IS '逻辑删除标志';
-- 同一通道商户下抖音应用ID不重复
CREATE UNIQUE INDEX IF NOT EXISTS "uk_douyin_direct_app_appid"
    ON "public"."douyin_direct_app" ("channel_mch_no", "douyin_app_id")
    WHERE "deleted" = false;

-- 抖音直连密钥配置(直连商户维度, 一个抖音商户号共享一套密钥)
CREATE TABLE IF NOT EXISTS "public"."douyin_direct_key_config" (
    "id"                      bigint       NOT NULL PRIMARY KEY,
    "mch_no"                  varchar(32)  NOT NULL,
    "channel_mch_no"          varchar(64)  NOT NULL,
    "merchant_private_key"    text,
    "merchant_serial_number"  varchar(128),
    "encrypt_key"             text,
    "creator"                 bigint,
    "create_time"             timestamptz(6),
    "last_modifier"           bigint,
    "last_modified_time"      timestamptz(6),
    "version"                 integer      NOT NULL DEFAULT 0,
    "deleted"                 boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."douyin_direct_key_config" IS '抖音直连密钥配置';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."channel_mch_no" IS '通道商户号(唯一关联)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."merchant_private_key" IS '商户私钥(MERCHANT_PRIVATE_KEY, 加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."merchant_serial_number" IS '商家公钥证书序列号(MERCHANT_SERIAL_NO)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."encrypt_key" IS '接口加密密钥(ENCRYPT_KEY, 加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_key_config"."deleted" IS '逻辑删除标志';
-- 一个通道商户号唯一一份密钥配置
CREATE UNIQUE INDEX IF NOT EXISTS "uk_douyin_direct_key_cmchno"
    ON "public"."douyin_direct_key_config" ("channel_mch_no")
    WHERE "deleted" = false;

-- 抖音直连商户应用支付能力关联(建立通道商户维度下"支付能力→应用"的绑定关系)
CREATE TABLE IF NOT EXISTS "public"."douyin_direct_app_capability" (
    "id"                    bigint       NOT NULL PRIMARY KEY,
    "mch_no"                varchar(32)  NOT NULL,
    "channel_mch_no"        varchar(64)  NOT NULL,
    "capability"            varchar(64)  NOT NULL,
    "douyin_direct_app_id"  bigint       NOT NULL,
    "creator"               bigint,
    "create_time"           timestamptz(6),
    "last_modifier"         bigint,
    "last_modified_time"    timestamptz(6),
    "version"               integer      NOT NULL DEFAULT 0,
    "deleted"               boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."douyin_direct_app_capability" IS '抖音直连商户应用支付能力关联';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."capability" IS '支付能力编码(如 DOUYIN_QR/DOUYIN_JSAPI 等)';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."douyin_direct_app_id" IS '关联抖音直连应用ID(指向 douyin_direct_app.id)';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_app_capability"."deleted" IS '逻辑删除标志';
-- 同一通道商户下, 一个支付能力只能绑定一个应用
CREATE UNIQUE INDEX IF NOT EXISTS "uk_douyin_direct_cap_cmchno_cap"
    ON "public"."douyin_direct_app_capability" ("channel_mch_no", "capability")
    WHERE "deleted" = false;

-- 抖音直连商户应用授权认证配置(应用密钥 / 授权回调地址)
CREATE TABLE IF NOT EXISTS "public"."douyin_direct_app_auth_config" (
    "id"                    bigint       NOT NULL PRIMARY KEY,
    "mch_no"                varchar(32)  NOT NULL,
    "channel_mch_no"        varchar(64)  NOT NULL,
    "douyin_direct_app_id"  bigint       NOT NULL,
    "app_secret"            text,
    "auth_callback_url"     varchar(512),
    "creator"               bigint,
    "create_time"           timestamptz(6),
    "last_modifier"         bigint,
    "last_modified_time"    timestamptz(6),
    "version"               integer      NOT NULL DEFAULT 0,
    "deleted"               boolean      NOT NULL DEFAULT false
);
COMMENT ON TABLE  "public"."douyin_direct_app_auth_config" IS '抖音直连商户应用授权认证配置';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."channel_mch_no" IS '通道商户号';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."douyin_direct_app_id" IS '关联抖音直连应用ID(指向 douyin_direct_app.id)';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."app_secret" IS '应用密钥(加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."auth_callback_url" IS '授权回调地址';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."creator" IS '创建人ID';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."create_time" IS '创建时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."last_modifier" IS '最后修改人ID';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."last_modified_time" IS '最后修改时间(UTC)';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_app_auth_config"."deleted" IS '逻辑删除标志';
