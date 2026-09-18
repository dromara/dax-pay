-- 2026-09-18 斗拱(汇付天下)通道服务商模式两张表: 服务商全局密钥配置 + 通道商户绑定
-- 背景: 斗拱通道代码合入时 DDL 未随脚本落盘, 存量库需补建; 结构对齐盛付通 ISV 范式(uk 部分唯一索引 + 逻辑删除)
-- 幂等: 已存在(含 id 冲突)时跳过, 支持在已手工执行过部分语句的库上安全重放

-- 斗拱服务商密钥配置(产品级全局一份)
CREATE TABLE IF NOT EXISTS public.dougong_isv_key_config (
    id bigint NOT NULL,
    product character varying(32) NOT NULL,
    sys_id character varying(64),
    product_id character varying(64),
    private_key text,
    dg_public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.dougong_isv_key_config IS '斗拱服务商密钥配置(产品级全局一份)';
COMMENT ON COLUMN public.dougong_isv_key_config.id IS '主键';
COMMENT ON COLUMN public.dougong_isv_key_config.product IS '产品编码(dougong_isv, 服务商密钥按产品全局一份)';
COMMENT ON COLUMN public.dougong_isv_key_config.sys_id IS '服务商系统ID(sysId, 汇付控制台分配; 首次查询自动创建的占位记录为空, 支付前由应用层校验必填)';
COMMENT ON COLUMN public.dougong_isv_key_config.product_id IS '汇付产品号(productId; 首次查询自动创建的占位记录为空, 支付前由应用层校验必填)';
COMMENT ON COLUMN public.dougong_isv_key_config.private_key IS '商户RSA私钥(PEM, 请求签名, 加密存储)';
COMMENT ON COLUMN public.dougong_isv_key_config.dg_public_key IS '斗拱RSA公钥(PEM, 响应验签, 加密存储)';
COMMENT ON COLUMN public.dougong_isv_key_config.creator IS '创建人ID';
COMMENT ON COLUMN public.dougong_isv_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.dougong_isv_key_config.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN public.dougong_isv_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.dougong_isv_key_config.version IS '乐观锁版本号';
COMMENT ON COLUMN public.dougong_isv_key_config.deleted IS '逻辑删除标志';

CREATE SEQUENCE IF NOT EXISTS public.dougong_isv_key_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.dougong_isv_key_config_id_seq OWNED BY public.dougong_isv_key_config.id;
ALTER TABLE ONLY public.dougong_isv_key_config ALTER COLUMN id SET DEFAULT nextval('public.dougong_isv_key_config_id_seq'::regclass);

-- 主键约束(存在时跳过)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'dougong_isv_key_config_pkey') THEN
        ALTER TABLE ONLY public.dougong_isv_key_config
            ADD CONSTRAINT dougong_isv_key_config_pkey PRIMARY KEY (id);
    END IF;
END
$$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_dougong_isv_key_product ON public.dougong_isv_key_config USING btree (product) WHERE (deleted = false);
COMMENT ON INDEX public.uk_dougong_isv_key_product IS '服务商密钥按产品全局唯一(一个服务商一套密钥, 轮换单点)';

-- 斗拱通道商户绑定(服务商模式, 通道商户维度)
CREATE TABLE IF NOT EXISTS public.dougong_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    merchant_no character varying(64) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.dougong_isv_channel_merchant IS '斗拱通道商户绑定(服务商模式, 通道商户维度)';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.id IS '主键';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.mch_no IS '商户号';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.channel_mch_no IS '通道商户号(DOUGONG+雪花, 唯一关联, 创建后不可修改)';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.product IS '产品编码(dougong_isv)';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.merchant_no IS '汇付商户号(merchantNo/huifuId, 须与服务商存在代理关系)';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.creator IS '创建人ID';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.create_time IS '创建时间';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.version IS '乐观锁版本号';
COMMENT ON COLUMN public.dougong_isv_channel_merchant.deleted IS '逻辑删除标志';

CREATE SEQUENCE IF NOT EXISTS public.dougong_isv_channel_merchant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.dougong_isv_channel_merchant_id_seq OWNED BY public.dougong_isv_channel_merchant.id;
ALTER TABLE ONLY public.dougong_isv_channel_merchant ALTER COLUMN id SET DEFAULT nextval('public.dougong_isv_channel_merchant_id_seq'::regclass);

-- 主键约束(存在时跳过)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'dougong_isv_channel_merchant_pkey') THEN
        ALTER TABLE ONLY public.dougong_isv_channel_merchant
            ADD CONSTRAINT dougong_isv_channel_merchant_pkey PRIMARY KEY (id);
    END IF;
END
$$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_dougong_isv_cmchno ON public.dougong_isv_channel_merchant USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_dougong_isv_cmchno IS '通道商户号唯一关联(与pay_channel_merchant一对一)';

CREATE UNIQUE INDEX IF NOT EXISTS uk_dougong_isv_mch_mer ON public.dougong_isv_channel_merchant USING btree (mch_no, merchant_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_dougong_isv_mch_mer IS '同一商户下汇付商户号不重复';

-- =============================================================
-- 2026-09-18 易支付通道(easy_pay)接入
-- 通道商户维度的易支付三方聚合平台对接配置(表结构见 table.sql 同步补齐)
-- =============================================================

-- 易支付通道密钥配置(通道商户维度)
CREATE TABLE IF NOT EXISTS public.easy_pay_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    server_url character varying(255) NOT NULL,
    partner_id character varying(32) NOT NULL,
    merchant_private_key text,
    platform_public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.easy_pay_key_config IS '易支付通道密钥配置(通道商户维度)';
COMMENT ON COLUMN public.easy_pay_key_config.id IS '主键';
COMMENT ON COLUMN public.easy_pay_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.easy_pay_key_config.channel_mch_no IS '通道商户号(EASY+雪花, 唯一关联, 创建后不可修改)';
COMMENT ON COLUMN public.easy_pay_key_config.server_url IS '易支付平台网关地址(如 https://pay.xxx.com)';
COMMENT ON COLUMN public.easy_pay_key_config.partner_id IS '易支付商户ID(pid, 上游平台分配)';
COMMENT ON COLUMN public.easy_pay_key_config.merchant_private_key IS '商户RSA私钥(PKCS8, SHA256withRSA 签名, 加密存储)';
COMMENT ON COLUMN public.easy_pay_key_config.platform_public_key IS '易支付平台验签公钥(加密存储)';
COMMENT ON COLUMN public.easy_pay_key_config.creator IS '创建人ID';
COMMENT ON COLUMN public.easy_pay_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.easy_pay_key_config.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN public.easy_pay_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.easy_pay_key_config.version IS '乐观锁版本号';
COMMENT ON COLUMN public.easy_pay_key_config.deleted IS '逻辑删除标志';

CREATE SEQUENCE IF NOT EXISTS public.easy_pay_key_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE public.easy_pay_key_config_id_seq OWNED BY public.easy_pay_key_config.id;
ALTER TABLE ONLY public.easy_pay_key_config ALTER COLUMN id SET DEFAULT nextval('public.easy_pay_key_config_id_seq'::regclass);

-- 主键约束(存在时跳过)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'easy_pay_key_config_pkey') THEN
        ALTER TABLE ONLY public.easy_pay_key_config
            ADD CONSTRAINT easy_pay_key_config_pkey PRIMARY KEY (id);
    END IF;
END
$$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_easy_pay_key_cmchno ON public.easy_pay_key_config USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_easy_pay_key_cmchno IS '同一通道商户易支付密钥唯一';

CREATE UNIQUE INDEX IF NOT EXISTS uk_easy_pay_key_mch_pid ON public.easy_pay_key_config USING btree (mch_no, partner_id) WHERE (deleted = false);
COMMENT ON INDEX public.uk_easy_pay_key_mch_pid IS '同一商户下易支付商户ID不重复';
