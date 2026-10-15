-- ----------------------------
-- 增量表结构脚本(上一版本 -> 当前版本)
-- 发版时本文件无需改动, 保持空白即可; 下一版本周期内的表结构变化再追加于此
-- ----------------------------

-- 2026-10-14 补建用户协议条款项表 (UserProtocolItem 实体与 /user/protocol/item 接口此前已存在, 表结构缺失, 全新建表对存量数据无影响)
CREATE TABLE public.base_user_protocol_item (
    id bigint NOT NULL,
    protocol_id bigint NOT NULL,
    sort_no double precision,
    content text NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.base_user_protocol_item IS '用户协议条款项';
COMMENT ON COLUMN public.base_user_protocol_item.id IS '主键';
COMMENT ON COLUMN public.base_user_protocol_item.protocol_id IS '协议ID';
COMMENT ON COLUMN public.base_user_protocol_item.sort_no IS '排序号';
COMMENT ON COLUMN public.base_user_protocol_item.content IS '协议内容';
COMMENT ON COLUMN public.base_user_protocol_item.creator IS '创建者ID';
COMMENT ON COLUMN public.base_user_protocol_item.create_time IS '创建时间';
COMMENT ON COLUMN public.base_user_protocol_item.last_modifier IS '最后修改ID';
COMMENT ON COLUMN public.base_user_protocol_item.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.base_user_protocol_item.version IS '版本号, 使用乐观锁';
COMMENT ON COLUMN public.base_user_protocol_item.deleted IS '逻辑删除标志';

ALTER TABLE ONLY public.base_user_protocol_item
    ADD CONSTRAINT base_user_protocol_item_pkey PRIMARY KEY (id);

CREATE INDEX idx_user_protocol_item_protocol ON public.base_user_protocol_item USING btree (protocol_id);
COMMENT ON INDEX public.idx_user_protocol_item_protocol IS '按协议 ID 查询条款项列表';

-- 2026-10-14 删除支付宝应用授权认证配置两张表 (该配置仅存 user_id_type 字段且从不参与运行时请求, 属死配置; 同步删除前后端代码与页面, 存量数据无需保留)
DROP TABLE IF EXISTS public.alipay_direct_app_auth_config;
DROP TABLE IF EXISTS public.alipay_isv_app_auth_config;

-- 2026-10-15 新增富友商户前置(直连商户模式)通道密钥配置表 (aggregatePay 直连商户: 商户号+商户密钥+终端号, 全新表对存量数据无影响)
CREATE TABLE public.fuyou_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    product character varying(32),
    sandbox boolean DEFAULT false,
    mchnt_cd character varying(64),
    mchnt_key text,
    term_no character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.fuyou_direct_key_config IS '富友直连商户密钥配置';
COMMENT ON COLUMN public.fuyou_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.fuyou_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.fuyou_direct_key_config.channel_mch_no IS '通道商户号(FUYOU+雪花)';
COMMENT ON COLUMN public.fuyou_direct_key_config.product IS '所属支付产品(对应 ProductEnum.code)';
COMMENT ON COLUMN public.fuyou_direct_key_config.sandbox IS '是否沙箱环境商户';
COMMENT ON COLUMN public.fuyou_direct_key_config.mchnt_cd IS '富友商户号(mchnt_cd)';
COMMENT ON COLUMN public.fuyou_direct_key_config.mchnt_key IS '商户密钥(mchnt_key, MD5 签名用, 加密存储)';
COMMENT ON COLUMN public.fuyou_direct_key_config.term_no IS '终端号(term_id)';
COMMENT ON COLUMN public.fuyou_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.fuyou_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.fuyou_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.fuyou_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.fuyou_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.fuyou_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_fuyou_direct_key_channel_mch ON public.fuyou_direct_key_config USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_fuyou_direct_key_channel_mch IS '通道商户号唯一';

CREATE UNIQUE INDEX uk_fuyou_direct_mch_mchnt_cd ON public.fuyou_direct_key_config USING btree (mch_no, mchnt_cd) WHERE (deleted = false);
COMMENT ON INDEX public.uk_fuyou_direct_mch_mchnt_cd IS '同一商户下富友商户号唯一';
