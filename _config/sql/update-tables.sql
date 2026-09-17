-- 表结构更新(2026-08-30): notify_mail_record 两列注释精简为紧凑枚举值风格
COMMENT ON COLUMN public.notify_mail_record.business_type IS '业务场景(test/manual等)';
COMMENT ON COLUMN public.notify_mail_record.status IS '发送状态(sending/success/fail)';

-- 表结构更新(2026-09-06): 风控精简, 移除门店地理围栏(移交商业版)
-- 命中记录去掉围栏快照列(client_city 保留, 省市地区黑名单命中仍记录 IP 归属城市)
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS store_city;
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS store_no;
ALTER TABLE pay_risk_hit DROP COLUMN IF EXISTS geo_fence_strategy;
-- 商户级围栏 opt-in 配置表随围栏功能整体移除
DROP TABLE IF EXISTS mch_risk_config;


-- 风控精简: 移除城市接壤关系表(围栏 balanced 邻市策略专用, 随门店地理围栏一并移交商业版)
DROP TABLE IF EXISTS base_city_adjacent;
DROP SEQUENCE IF EXISTS base_city_adjacent_id_seq;

-- 表结构更新(2026-09-10): 分账接收方新增本地别名(纯本地备注字段, 不上送通道, 可随时修改)
-- 五张通道档案表同步加列, 仅用于平台侧识别管理, 与通道侧接收方名称(receiver_name)无关
ALTER TABLE public.wechat_direct_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.wechat_direct_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.wechat_isv_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.wechat_isv_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.alipay_direct_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.alipay_direct_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.alipay_isv_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.alipay_isv_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';
ALTER TABLE public.douyin_direct_alloc_receiver ADD COLUMN IF NOT EXISTS alias varchar(50);
COMMENT ON COLUMN public.douyin_direct_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道)';


-- 表结构更新(2026-09-11): 账号/商户号/角色编码唯一性下沉到数据库
-- 背景: 唯一性此前仅由应用层保证, check-then-insert 存在并发竞态; 且一旦出现重复账号,
--       登录按 client_code+account 查单条会抛多结果异常, 导致新旧账号一并不可用。
--       仓内其余业务键(app_id/store_no/code/terminal_no)均已用唯一索引兜底, 此处补齐这三张表。
-- 前置: 库中须无重复数据(升级前先自查, 有重复需先清理)
DROP INDEX IF EXISTS public.idx_iam_user_info_client_account;
CREATE UNIQUE INDEX uk_iam_user_info_client_account ON public.iam_user_info (client_code, account) WHERE deleted = false;
COMMENT ON INDEX uk_iam_user_info_client_account IS '同一身份域下账号全局唯一(登录凭据, 重复会导致登录异常)';
DROP INDEX IF EXISTS public.idx_mch_info_mch_no;
CREATE UNIQUE INDEX uk_mch_info_mch_no ON public.mch_info (mch_no) WHERE deleted = false;
COMMENT ON INDEX uk_mch_info_mch_no IS '商户号全局唯一(商户业务主键, 重复会导致按号查询异常)';
CREATE UNIQUE INDEX IF NOT EXISTS uk_iam_role_code ON public.iam_role (code) WHERE deleted = false;
COMMENT ON INDEX uk_iam_role_code IS '角色编码全局唯一(按 code 解析内置角色, 重复会导致角色解析异常)';


-- 表结构更新(2026-09-17): 新增盛付通通道密钥配置表(盛付通通道编排模块 daxpay-channel-sheng)
-- 盛付通不提供集测接口(官方二次确认), 不设沙箱字段, 单一生产网关, 每通道商户一条配置
-- 2026-09-17晚 双产品拆分: 本表定稿为商户模式(去 sub_mch_id, 服务商模式改走 sheng_isv_* 两张新表, 见本文件末尾同日追加块);
-- 本表未随任何版本发布, CREATE 按终态直接维护; 已按旧结构建过表的 dev 库须 DROP 后重放本段
CREATE TABLE public.sheng_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    sheng_mch_id character varying(16),
    sdp_app_id character varying(32),
    merchant_private_key text,
    shengpay_public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);
COMMENT ON TABLE public.sheng_key_config IS '盛付通通道密钥配置(商户模式)';
COMMENT ON COLUMN public.sheng_key_config.id IS '主键';
COMMENT ON COLUMN public.sheng_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.sheng_key_config.channel_mch_no IS '通道商户号(唯一关联, 创建后不可修改)';
COMMENT ON COLUMN public.sheng_key_config.sheng_mch_id IS '盛付通商户号(mchId, 商户自有对接商户号)';
COMMENT ON COLUMN public.sheng_key_config.sdp_app_id IS '盛付通分配AppId(可空, 直连场景必填)';
COMMENT ON COLUMN public.sheng_key_config.merchant_private_key IS '商户私钥(PKCS8, SHA1withRSA 签名, 加密存储)';
COMMENT ON COLUMN public.sheng_key_config.shengpay_public_key IS '盛付通验签公钥(加密存储)';
COMMENT ON COLUMN public.sheng_key_config.creator IS '创建人ID';
COMMENT ON COLUMN public.sheng_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.sheng_key_config.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN public.sheng_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.sheng_key_config.version IS '乐观锁版本号';
COMMENT ON COLUMN public.sheng_key_config.deleted IS '逻辑删除标志';
ALTER TABLE ONLY public.sheng_key_config
    ADD CONSTRAINT sheng_key_config_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_sheng_key_cmchno ON public.sheng_key_config USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX uk_sheng_key_cmchno IS '同一通道商户盛付通密钥唯一(直连商户维度, 无沙箱双环境)';

-- 表结构更新(2026-09-17晚): 盛付通双产品拆分——新增服务商模式两表
-- sheng_isv_key_config: 服务商密钥按产品全局一份(一个服务商一套密钥, 轮换单点);
-- sheng_isv_channel_merchant: 子商户绑定(每子商户一行, sub_mch_id 必填且须与服务商存在代理关系);
-- 沿用盛付通无沙箱结论, 两表均不设 sandbox 字段
CREATE TABLE public.sheng_isv_key_config (
    id bigint NOT NULL,
    product character varying(32) NOT NULL,
    sheng_mch_id character varying(16),
    merchant_private_key text,
    shengpay_public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);
COMMENT ON TABLE public.sheng_isv_key_config IS '盛付通服务商密钥配置(产品级全局一份)';
COMMENT ON COLUMN public.sheng_isv_key_config.id IS '主键';
COMMENT ON COLUMN public.sheng_isv_key_config.product IS '产品编码(sheng_isv, 服务商密钥按产品全局一份)';
COMMENT ON COLUMN public.sheng_isv_key_config.sheng_mch_id IS '服务商商户号(mchId, 代子商户发起时上送)';
COMMENT ON COLUMN public.sheng_isv_key_config.merchant_private_key IS '服务商私钥(PKCS8/PKCS1 兼容, SHA1withRSA 签名, 加密存储)';
COMMENT ON COLUMN public.sheng_isv_key_config.shengpay_public_key IS '盛付通验签公钥(加密存储)';
COMMENT ON COLUMN public.sheng_isv_key_config.creator IS '创建人ID';
COMMENT ON COLUMN public.sheng_isv_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.sheng_isv_key_config.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN public.sheng_isv_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.sheng_isv_key_config.version IS '乐观锁版本号';
COMMENT ON COLUMN public.sheng_isv_key_config.deleted IS '逻辑删除标志';
ALTER TABLE ONLY public.sheng_isv_key_config
    ADD CONSTRAINT sheng_isv_key_config_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_sheng_isv_key_product ON public.sheng_isv_key_config USING btree (product) WHERE (deleted = false);
COMMENT ON INDEX uk_sheng_isv_key_product IS '服务商密钥按产品全局唯一(一个服务商一套密钥, 轮换单点)';

CREATE TABLE public.sheng_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32),
    sub_mch_id character varying(16) NOT NULL,
    sdp_app_id character varying(32),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);
COMMENT ON TABLE public.sheng_isv_channel_merchant IS '盛付通服务商子商户绑定(通道商户维度)';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.id IS '主键';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.mch_no IS '商户号';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.channel_mch_no IS '通道商户号(唯一关联, 创建后不可修改)';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.product IS '产品编码(sheng_isv)';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.sub_mch_id IS '子商户号(服务商模式必填, 须与服务商商户号存在代理关系)';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.sdp_app_id IS '盛付通分配AppId(可空, 子商户应用)';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.creator IS '创建人ID';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.create_time IS '创建时间';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.last_modifier IS '最后修改人ID';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.version IS '乐观锁版本号';
COMMENT ON COLUMN public.sheng_isv_channel_merchant.deleted IS '逻辑删除标志';
ALTER TABLE ONLY public.sheng_isv_channel_merchant
    ADD CONSTRAINT sheng_isv_channel_merchant_pkey PRIMARY KEY (id);
CREATE UNIQUE INDEX uk_sheng_isv_cmchno ON public.sheng_isv_channel_merchant USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX uk_sheng_isv_cmchno IS '同一通道商户服务商绑定唯一';
CREATE UNIQUE INDEX uk_sheng_isv_mch_sub ON public.sheng_isv_channel_merchant USING btree (mch_no, sub_mch_id) WHERE (deleted = false);
COMMENT ON INDEX uk_sheng_isv_mch_sub IS '同一商户同子商户号唯一(防重复绑定)';
