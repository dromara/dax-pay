
-- Adapay 直连密钥配置表
CREATE TABLE IF NOT EXISTS adapay_direct_key_config (
    id bigserial PRIMARY KEY,
    mch_no varchar(32),
    channel_mch_no varchar(64),
    merchant_no varchar(64),
    adapay_app_id varchar(64),
    api_key text,
    private_key text,
    public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamptz(6),
    last_modifier bigint,
    last_modified_time timestamptz(6),
    version int DEFAULT 0,
    deleted boolean DEFAULT false
);
COMMENT ON TABLE adapay_direct_key_config IS 'Adapay 直连密钥配置';
COMMENT ON COLUMN adapay_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN adapay_direct_key_config.channel_mch_no IS '通道商户号(创建时录入不可修改)';
COMMENT ON COLUMN adapay_direct_key_config.merchant_no IS 'Adapay 商户号(创建时录入不可修改)';
COMMENT ON COLUMN adapay_direct_key_config.adapay_app_id IS 'Adapay 应用ID(app_id)';
COMMENT ON COLUMN adapay_direct_key_config.api_key IS 'Adapay API Key(请求头Authorization, 加密存储)';
COMMENT ON COLUMN adapay_direct_key_config.private_key IS '商户RSA私钥(PKCS#8 Base64, 请求签名, 加密存储)';
COMMENT ON COLUMN adapay_direct_key_config.public_key IS 'Adapay 平台公钥(X509 Base64, 响应验签, 加密存储; 为空使用全局默认)';
COMMENT ON COLUMN adapay_direct_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN adapay_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN adapay_direct_key_config.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN adapay_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN adapay_direct_key_config.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN adapay_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN adapay_direct_key_config.deleted IS '删除标志';

-- ============================================================
-- 乐刷通道(LESHUA)建表
-- 服务商模式: 服务商密钥全局唯一 + 子商户绑定乐刷商户号
-- ============================================================

-- 乐刷服务商密钥配置(全局唯一, 按 product 查询)
DROP TABLE IF EXISTS "public"."leshua_isv_key_config";
CREATE TABLE "public"."leshua_isv_key_config" (
  "id" int8 NOT NULL,
  "product" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "ls_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "trade_key" text COLLATE "pg_catalog"."default",
  "notify_key" text COLLATE "pg_catalog"."default",
  "sign_type" varchar(16) COLLATE "pg_catalog"."default",
  "ls_isv_no" varchar(64) COLLATE "pg_catalog"."default",
  "sandbox" bool DEFAULT false,
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON TABLE "public"."leshua_isv_key_config" IS '乐刷服务商密钥配置';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."id" IS '主键';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."product" IS '产品编码(对应 ProductEnum.code, 如 leshua_pay)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."ls_mch_no" IS '乐刷商户号(merchant_id, 服务商级或商户级, 全局唯一)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."trade_key" IS '交易密钥(tradeKey, 请求签名与响应/回调验签, 加密存储)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."notify_key" IS '异步通知密钥(notifyKey, 加密存储)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."sign_type" IS '签名类型(MD5 / SM3)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."ls_isv_no" IS '乐刷服务商号(进件场景使用, 可选)';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."sandbox" IS '是否沙箱环境';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."creator" IS '创建者';
COMMENT ON COLUMN "public"."leshua_isv_key_config"."create_time" IS '创建时间';

-- 乐刷通道商户绑定(子商户绑定乐刷商户号)
DROP TABLE IF EXISTS "public"."leshua_isv_channel_merchant";
CREATE TABLE "public"."leshua_isv_channel_merchant" (
  "id" int8 NOT NULL,
  "mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "product" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "ls_mch_no" varchar(64) COLLATE "pg_catalog"."default",
  "deleted" bool NOT NULL DEFAULT false,
  "last_modifier" int8,
  "last_modified_time" timestamp(6),
  "version" int4 NOT NULL DEFAULT 0,
  "creator" int8,
  "create_time" timestamp(6)
)
;
COMMENT ON TABLE "public"."leshua_isv_channel_merchant" IS '乐刷通道商户绑定';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."id" IS '主键';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."mch_no" IS '平台商户号';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."channel_mch_no" IS '通道商户号(平台生成的唯一标识, LESHUA+雪花)';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."product" IS '所属支付产品(对应 ProductEnum.code, 如 leshua_pay)';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."ls_mch_no" IS '乐刷商户编号(merchant_id)';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."deleted" IS '删除标志';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."last_modifier" IS '最后修改者';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."creator" IS '创建者';
COMMENT ON COLUMN "public"."leshua_isv_channel_merchant"."create_time" IS '创建时间';

-- 斗拱(汇付天下)服务商密钥配置表
CREATE TABLE IF NOT EXISTS dougong_isv_key_config (
    id bigserial PRIMARY KEY,
    product varchar(32),
    sys_id varchar(64),
    product_id varchar(64),
    private_key text,
    dg_public_key text,
    creator bigint,
    create_time timestamptz(6),
    last_modifier bigint,
    last_modified_time timestamptz(6),
    version int DEFAULT 0,
    deleted boolean DEFAULT false
);
COMMENT ON TABLE dougong_isv_key_config IS '斗拱服务商密钥配置';
COMMENT ON COLUMN dougong_isv_key_config.product IS '产品编码';
COMMENT ON COLUMN dougong_isv_key_config.sys_id IS '服务商系统ID(sysId)';
COMMENT ON COLUMN dougong_isv_key_config.product_id IS '产品号(productId)';
COMMENT ON COLUMN dougong_isv_key_config.private_key IS '商户RSA私钥(PEM, 加密存储)';
COMMENT ON COLUMN dougong_isv_key_config.dg_public_key IS '斗拱RSA公钥(PEM, 加密存储, 回调验签用)';
COMMENT ON COLUMN dougong_isv_key_config.creator IS '创建者ID';
COMMENT ON COLUMN dougong_isv_key_config.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN dougong_isv_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN dougong_isv_key_config.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN dougong_isv_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN dougong_isv_key_config.deleted IS '删除标志';

-- 斗拱(汇付天下)通道商户绑定表
CREATE TABLE IF NOT EXISTS dougong_isv_channel_merchant (
    id bigserial PRIMARY KEY,
    mch_no varchar(32),
    channel_mch_no varchar(64),
    product varchar(32),
    merchant_no varchar(64),
    app_id varchar(64),
    creator bigint,
    create_time timestamptz(6),
    last_modifier bigint,
    last_modified_time timestamptz(6),
    version int DEFAULT 0,
    deleted boolean DEFAULT false
);
COMMENT ON TABLE dougong_isv_channel_merchant IS '斗拱通道商户绑定';
COMMENT ON COLUMN dougong_isv_channel_merchant.mch_no IS '商户号';
COMMENT ON COLUMN dougong_isv_channel_merchant.channel_mch_no IS '通道商户号(DOUGONG+雪花)';
COMMENT ON COLUMN dougong_isv_channel_merchant.product IS '所属支付产品';
COMMENT ON COLUMN dougong_isv_channel_merchant.merchant_no IS '汇付商户号(merchantNo/huifuId)';
COMMENT ON COLUMN dougong_isv_channel_merchant.app_id IS '商户AppId(汇付SDK BasePay.putMerConfigs的key)';
COMMENT ON COLUMN dougong_isv_channel_merchant.creator IS '创建者ID';
COMMENT ON COLUMN dougong_isv_channel_merchant.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN dougong_isv_channel_merchant.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN dougong_isv_channel_merchant.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN dougong_isv_channel_merchant.version IS '版本号(乐观锁)';
COMMENT ON COLUMN dougong_isv_channel_merchant.deleted IS '删除标志';

-- ============================================================
-- 随行付通道(VBILL/天阙科技)建表
-- 服务商模式: 服务商密钥全局唯一 + 子商户绑定天阙商户号(mno)
-- ============================================================

-- 随行付服务商密钥配置(全局唯一, 按 product 查询)
CREATE TABLE IF NOT EXISTS vbill_isv_key_config (
    id int8 NOT NULL,
    product varchar(32) NOT NULL,
    org_id varchar(64) NOT NULL,
    public_key text,
    private_key text,
    sandbox boolean DEFAULT false,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted boolean DEFAULT false,
    PRIMARY KEY (id)
);
COMMENT ON TABLE vbill_isv_key_config IS '随行付服务商密钥配置';
COMMENT ON COLUMN vbill_isv_key_config.id IS '主键';
COMMENT ON COLUMN vbill_isv_key_config.product IS '支付产品编码(对应 ProductEnum.code, 如 vbill_pay)';
COMMENT ON COLUMN vbill_isv_key_config.org_id IS '天阙合作机构ID(orgId)';
COMMENT ON COLUMN vbill_isv_key_config.public_key IS '天阙RSA公钥(X509 Base64, 用于响应/回调验签, 加密存储)';
COMMENT ON COLUMN vbill_isv_key_config.private_key IS '商户RSA私钥(PKCS8 Base64, SHA1withRSA 签名, 加密存储)';
COMMENT ON COLUMN vbill_isv_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN vbill_isv_key_config.creator IS '创建者ID';
COMMENT ON COLUMN vbill_isv_key_config.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN vbill_isv_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN vbill_isv_key_config.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN vbill_isv_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN vbill_isv_key_config.deleted IS '删除标志';

-- 随行付通道商户绑定(子商户绑定天阙商户号 mno)
CREATE TABLE IF NOT EXISTS vbill_isv_channel_merchant (
    id int8 NOT NULL,
    mch_no varchar(64) NOT NULL,
    channel_mch_no varchar(64) NOT NULL,
    product varchar(32) NOT NULL,
    vbill_mch_no varchar(64) NOT NULL,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted boolean DEFAULT false,
    PRIMARY KEY (id)
);
COMMENT ON TABLE vbill_isv_channel_merchant IS '随行付通道商户绑定';
COMMENT ON COLUMN vbill_isv_channel_merchant.id IS '主键';
COMMENT ON COLUMN vbill_isv_channel_merchant.mch_no IS '平台商户号';
COMMENT ON COLUMN vbill_isv_channel_merchant.channel_mch_no IS '通道商户号(平台生成的唯一标识, VBILL+雪花)';
COMMENT ON COLUMN vbill_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code, 如 vbill_pay)';
COMMENT ON COLUMN vbill_isv_channel_merchant.vbill_mch_no IS '天阙商户号(mno)';
COMMENT ON COLUMN vbill_isv_channel_merchant.creator IS '创建者ID';
COMMENT ON COLUMN vbill_isv_channel_merchant.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN vbill_isv_channel_merchant.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN vbill_isv_channel_merchant.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN vbill_isv_channel_merchant.version IS '版本号(乐观锁)';
COMMENT ON COLUMN vbill_isv_channel_merchant.deleted IS '删除标志';

-- ============================================================
-- 河马付通道(HMPAY/杉德旗下产品)建表
-- 服务商模式: 服务商密钥全局唯一 + 子商户绑定杉德商户号(merchantNo) + 门店号(storeId)
-- ============================================================

-- 河马付(杉德)服务商密钥配置表
CREATE TABLE IF NOT EXISTS hmpay_isv_key_config (
    id bigserial PRIMARY KEY,
    product varchar(32),
    sand_app_id varchar(64),
    private_key text,
    public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamptz(6),
    last_modifier bigint,
    last_modified_time timestamptz(6),
    version int DEFAULT 0,
    deleted boolean DEFAULT false
);
COMMENT ON TABLE hmpay_isv_key_config IS '河马付服务商密钥配置';
COMMENT ON COLUMN hmpay_isv_key_config.product IS '产品编码(对应 ProductEnum.code, 如 hm_pay)';
COMMENT ON COLUMN hmpay_isv_key_config.sand_app_id IS '杉德代理号(sandAppId / app_id)';
COMMENT ON COLUMN hmpay_isv_key_config.private_key IS '商户RSA私钥(PKCS#8 Base64, 加密存储, 签名用)';
COMMENT ON COLUMN hmpay_isv_key_config.public_key IS '杉德RSA公钥(X509 Base64, 加密存储, 回调/响应验签用)';
COMMENT ON COLUMN hmpay_isv_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN hmpay_isv_key_config.creator IS '创建者ID';
COMMENT ON COLUMN hmpay_isv_key_config.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN hmpay_isv_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN hmpay_isv_key_config.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN hmpay_isv_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN hmpay_isv_key_config.deleted IS '删除标志';

-- 河马付(杉德)通道商户绑定表
CREATE TABLE IF NOT EXISTS hmpay_isv_channel_merchant (
    id bigserial PRIMARY KEY,
    mch_no varchar(32),
    channel_mch_no varchar(64),
    product varchar(32),
    merchant_no varchar(64),
    store_id varchar(64),
    creator bigint,
    create_time timestamptz(6),
    last_modifier bigint,
    last_modified_time timestamptz(6),
    version int DEFAULT 0,
    deleted boolean DEFAULT false
);
COMMENT ON TABLE hmpay_isv_channel_merchant IS '河马付通道商户绑定';
COMMENT ON COLUMN hmpay_isv_channel_merchant.mch_no IS '商户号';
COMMENT ON COLUMN hmpay_isv_channel_merchant.channel_mch_no IS '通道商户号(HMPAY+雪花)';
COMMENT ON COLUMN hmpay_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code, 如 hm_pay)';
COMMENT ON COLUMN hmpay_isv_channel_merchant.merchant_no IS '杉德商户编号(merchantNo / sub_app_id)';
COMMENT ON COLUMN hmpay_isv_channel_merchant.store_id IS '门店号(storeId)';
COMMENT ON COLUMN hmpay_isv_channel_merchant.creator IS '创建者ID';
COMMENT ON COLUMN hmpay_isv_channel_merchant.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN hmpay_isv_channel_merchant.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN hmpay_isv_channel_merchant.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN hmpay_isv_channel_merchant.version IS '版本号(乐观锁)';
COMMENT ON COLUMN hmpay_isv_channel_merchant.deleted IS '删除标志';

-- 清理已废弃的微信应用ID/通道渠道认证列(幂等, 兼容已建表环境)
ALTER TABLE hmpay_isv_channel_merchant DROP COLUMN IF EXISTS wx_app_id;
ALTER TABLE hmpay_isv_channel_merchant DROP COLUMN IF EXISTS wx_channel_auth;

-- ============================================================
-- 富友通道(FUYOU)建表
-- 服务商模式: 服务商密钥全局唯一(机构号+私钥/公钥) + 子商户绑定富友商户号(mchnt_cd)+终端号(term_id)
-- 签名算法: MD5withRSA + GBK 编码 + XML 报文
-- ============================================================

-- 富友服务商密钥配置(全局唯一, 按 product 查询)
CREATE TABLE IF NOT EXISTS fuyou_isv_key_config (
    id int8 NOT NULL,
    product varchar(32) NOT NULL,
    fy_app_id varchar(64),
    order_prefix varchar(16),
    public_key text,
    private_key text,
    sandbox boolean DEFAULT false,
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted boolean DEFAULT false,
    PRIMARY KEY (id)
);
COMMENT ON TABLE fuyou_isv_key_config IS '富友服务商密钥配置';
COMMENT ON COLUMN fuyou_isv_key_config.id IS '主键';
COMMENT ON COLUMN fuyou_isv_key_config.product IS '支付产品编码(对应 ProductEnum.code, 如 fuyou_pay)';
COMMENT ON COLUMN fuyou_isv_key_config.fy_app_id IS '富友应用编号(机构号 ins_cd)';
COMMENT ON COLUMN fuyou_isv_key_config.order_prefix IS '富友订单前缀(关联订单号前缀, 富友回调凭 mchnt_order_no 反查平台订单)';
COMMENT ON COLUMN fuyou_isv_key_config.public_key IS '富友RSA公钥(X509 Base64, 用于响应/回调验签, 加密存储)';
COMMENT ON COLUMN fuyou_isv_key_config.private_key IS '商户RSA私钥(PKCS8 Base64, MD5withRSA 签名, 加密存储)';
COMMENT ON COLUMN fuyou_isv_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN fuyou_isv_key_config.creator IS '创建者ID';
COMMENT ON COLUMN fuyou_isv_key_config.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN fuyou_isv_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN fuyou_isv_key_config.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN fuyou_isv_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN fuyou_isv_key_config.deleted IS '删除标志';

-- 富友通道商户绑定(子商户绑定富友商户号 mchnt_cd + 终端号 term_id)
CREATE TABLE IF NOT EXISTS fuyou_isv_channel_merchant (
    id int8 NOT NULL,
    mch_no varchar(64) NOT NULL,
    channel_mch_no varchar(64) NOT NULL,
    product varchar(32) NOT NULL,
    fuyou_mch_no varchar(64) NOT NULL,
    term_no varchar(32),
    creator int8,
    create_time timestamptz(6),
    last_modifier int8,
    last_modified_time timestamptz(6),
    version int4 DEFAULT 0,
    deleted boolean DEFAULT false,
    PRIMARY KEY (id)
);
COMMENT ON TABLE fuyou_isv_channel_merchant IS '富友通道商户绑定';
COMMENT ON COLUMN fuyou_isv_channel_merchant.id IS '主键';
COMMENT ON COLUMN fuyou_isv_channel_merchant.mch_no IS '平台商户号';
COMMENT ON COLUMN fuyou_isv_channel_merchant.channel_mch_no IS '通道商户号(平台生成的唯一标识, FUYOU+雪花)';
COMMENT ON COLUMN fuyou_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code, 如 fuyou_pay)';
COMMENT ON COLUMN fuyou_isv_channel_merchant.fuyou_mch_no IS '富友商户号(mchnt_cd)';
COMMENT ON COLUMN fuyou_isv_channel_merchant.term_no IS '终端号(term_id)';
COMMENT ON COLUMN fuyou_isv_channel_merchant.creator IS '创建者ID';
COMMENT ON COLUMN fuyou_isv_channel_merchant.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN fuyou_isv_channel_merchant.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN fuyou_isv_channel_merchant.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN fuyou_isv_channel_merchant.version IS '版本号(乐观锁)';
COMMENT ON COLUMN fuyou_isv_channel_merchant.deleted IS '删除标志';

-- ============================================================
-- 海科融通通道(HKRT)字段变更
-- 服务商密钥配置删除微信 AppId 字段(JSAPI 场景不需要该参数)
-- ============================================================

ALTER TABLE hkrt_isv_key_config DROP COLUMN IF EXISTS wx_app_id;

-- ============================================================
-- 支付产品表删除 icon 字段
-- 图标统一由前端 productLogoMap 维护, 不再依赖数据库存储
-- ============================================================
ALTER TABLE pay_md_product DROP COLUMN IF EXISTS icon;

-- ============================================================
-- 易宝通道(YEEPAY)建表
-- 直连模式: 商户身份(merchantNo/yopIsvNo) + YOP SDK 密钥(appKey/privateKey/yopPublicKey)
-- ============================================================

-- 易宝直连密钥配置(直连商户维度, 含商户身份与 YOP SDK 密钥, 敏感字段加密存储)
CREATE TABLE IF NOT EXISTS yeepay_direct_key_config (
    id bigserial PRIMARY KEY,
    mch_no varchar(32),
    channel_mch_no varchar(64),
    merchant_no varchar(64),
    yop_isv_no varchar(64),
    app_key text,
    private_key text,
    yop_public_key text,
    wx_app_id varchar(64),
    wx_app_secret text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamptz(6),
    last_modifier bigint,
    last_modified_time timestamptz(6),
    version int DEFAULT 0,
    deleted boolean DEFAULT false
);
COMMENT ON TABLE yeepay_direct_key_config IS '易宝直连密钥配置';
COMMENT ON COLUMN yeepay_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN yeepay_direct_key_config.channel_mch_no IS '通道商户号(创建时录入不可修改)';
COMMENT ON COLUMN yeepay_direct_key_config.merchant_no IS '易宝商户号(merchantNo, 创建时录入不可修改)';
COMMENT ON COLUMN yeepay_direct_key_config.yop_isv_no IS '易宝服务商商编(parentMerchantNo/yopIsvNo, 创建时录入不可修改)';
COMMENT ON COLUMN yeepay_direct_key_config.app_key IS '通道应用AppKey(YOP应用标识, 加密存储)';
COMMENT ON COLUMN yeepay_direct_key_config.private_key IS '商户RSA私钥(PEM PKCS#8, SDK签名, 加密存储)';
COMMENT ON COLUMN yeepay_direct_key_config.yop_public_key IS '易宝平台RSA公钥(PEM, SDK验签, 加密存储)';
COMMENT ON COLUMN yeepay_direct_key_config.wx_app_id IS '微信AppId(微信H5/JSAPI场景用, 可空)';
COMMENT ON COLUMN yeepay_direct_key_config.wx_app_secret IS '微信AppSecret(微信场景用, 可空, 加密存储)';
COMMENT ON COLUMN yeepay_direct_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN yeepay_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN yeepay_direct_key_config.create_time IS '创建时间(UTC)';
COMMENT ON COLUMN yeepay_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN yeepay_direct_key_config.last_modified_time IS '最后修改时间(UTC)';
COMMENT ON COLUMN yeepay_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN yeepay_direct_key_config.deleted IS '删除标志';

-- ============================================================
-- 沙箱环境维度改造: 服务商/直连密钥配置表统一增加 sandbox 列
-- 密钥按 (业务维度, sandbox) 双环境并存, activeEnv 决定运行时取哪套
-- ============================================================

-- 拉卡拉服务商密钥配置(pay_md.sql 建表, 此处补 sandbox 列)
ALTER TABLE lakala_isv_key_config ADD COLUMN IF NOT EXISTS sandbox boolean DEFAULT false;
COMMENT ON COLUMN lakala_isv_key_config.sandbox IS '是否沙箱环境';

-- 海科融通服务商密钥配置(pay_md.sql 建表, 此处补 sandbox 列)
ALTER TABLE hkrt_isv_key_config ADD COLUMN IF NOT EXISTS sandbox boolean DEFAULT false;
COMMENT ON COLUMN hkrt_isv_key_config.sandbox IS '是否沙箱环境';

-- 银联商务直连密钥配置(pay_md.sql 建表, 此处补 sandbox 列)
ALTER TABLE ums_direct_key_config ADD COLUMN IF NOT EXISTS sandbox boolean DEFAULT false;
COMMENT ON COLUMN ums_direct_key_config.sandbox IS '是否沙箱环境';

-- 支付宝直连商户应用密钥配置(pay_md.sql 建表, 此处补 sandbox 列)
ALTER TABLE alipay_direct_app_key_config ADD COLUMN IF NOT EXISTS sandbox boolean DEFAULT false;
COMMENT ON COLUMN alipay_direct_app_key_config.sandbox IS '是否沙箱环境';

-- ============================================================
-- 双环境唯一索引: 保证同一业务维度下 prod/sandbox 各只有一条密钥记录
-- ============================================================

-- 服务商密钥(按 product + sandbox 唯一)
CREATE UNIQUE INDEX IF NOT EXISTS uk_lakala_isv_key_prod_sandbox ON lakala_isv_key_config (product, sandbox) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_fuyou_isv_key_prod_sandbox ON fuyou_isv_key_config (product, sandbox) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_leshua_isv_key_prod_sandbox ON leshua_isv_key_config (product, sandbox) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_hkrt_isv_key_prod_sandbox ON hkrt_isv_key_config (product, sandbox) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_vbill_isv_key_prod_sandbox ON vbill_isv_key_config (product, sandbox) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_hmpay_isv_key_prod_sandbox ON hmpay_isv_key_config (product, sandbox) WHERE deleted = false;

-- 直连密钥(按 channel_mch_no + sandbox 唯一)
CREATE UNIQUE INDEX IF NOT EXISTS uk_yeepay_direct_key_mch_sandbox ON yeepay_direct_key_config (channel_mch_no, sandbox) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_adapay_direct_key_mch_sandbox ON adapay_direct_key_config (channel_mch_no, sandbox) WHERE deleted = false;
CREATE UNIQUE INDEX IF NOT EXISTS uk_ums_direct_key_mch_sandbox ON ums_direct_key_config (channel_mch_no, sandbox) WHERE deleted = false;

-- 支付宝直连密钥(按 alipay_direct_app_id + sandbox 唯一)
CREATE UNIQUE INDEX IF NOT EXISTS uk_alipay_direct_app_key_sandbox ON alipay_direct_app_key_config (alipay_direct_app_id, sandbox) WHERE deleted = false;
