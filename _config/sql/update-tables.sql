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

-- 2026-10-16 新增富友 RSA 密钥对配置表 (互联网直连 fuyou_internet / H5聚合收银台 fuyou_h5 两产品共用: 商户号+商户私钥+富友平台公钥, JSON+RSA 加密信封, 全新表对存量数据无影响)
CREATE TABLE public.fuyou_rsa_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    product character varying(32),
    sandbox boolean DEFAULT false,
    mchnt_cd character varying(64),
    private_key text,
    public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.fuyou_rsa_key_config IS '富友RSA密钥对配置(互联网直连/H5聚合收银台共用)';
COMMENT ON COLUMN public.fuyou_rsa_key_config.id IS '主键';
COMMENT ON COLUMN public.fuyou_rsa_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.fuyou_rsa_key_config.channel_mch_no IS '通道商户号(FUYOU+雪花)';
COMMENT ON COLUMN public.fuyou_rsa_key_config.product IS '所属支付产品(fuyou_internet/fuyou_h5, 对应 ProductEnum.code)';
COMMENT ON COLUMN public.fuyou_rsa_key_config.sandbox IS '是否沙箱环境商户';
COMMENT ON COLUMN public.fuyou_rsa_key_config.mchnt_cd IS '富友商户号(mchnt_cd)';
COMMENT ON COLUMN public.fuyou_rsa_key_config.private_key IS '商户RSA私钥(PKCS1/PKCS8, 响应/回调解密用, 加密存储)';
COMMENT ON COLUMN public.fuyou_rsa_key_config.public_key IS '富友平台RSA公钥(X509, 请求加密用, 加密存储)';
COMMENT ON COLUMN public.fuyou_rsa_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.fuyou_rsa_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.fuyou_rsa_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.fuyou_rsa_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.fuyou_rsa_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.fuyou_rsa_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_fuyou_rsa_key_channel_mch ON public.fuyou_rsa_key_config USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_fuyou_rsa_key_channel_mch IS '通道商户号唯一';

CREATE UNIQUE INDEX uk_fuyou_rsa_mch_product_mchnt ON public.fuyou_rsa_key_config USING btree (mch_no, product, mchnt_cd) WHERE (deleted = false);
COMMENT ON INDEX public.uk_fuyou_rsa_mch_product_mchnt IS '同一商户同一产品下富友商户号唯一';

-- 2026-10-16 新增快钱直连通道密钥配置表 (kuaiqian_pay 产品: 快钱会员号+当面付商户终端号+三份证书(商户 pfx/平台 cer/SSL p12), MAS 统一网关对接)
CREATE TABLE public.kuaiqian_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    member_code character varying(64),
    vendor_member_code character varying(64),
    merchant_id character varying(64),
    terminal_id character varying(64),
    merchant_cert_pfx text,
    merchant_cert_password text,
    platform_cert text,
    ssl_cert_p12 text,
    ssl_cert_password text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.kuaiqian_direct_key_config IS '快钱直连配置';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.channel_mch_no IS '通道商户号(KUAIQIAN+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.member_code IS '快钱会员号/账户号(memberCode, 人民币网关商户账号 = memberCode + 01, 创建时录入不可修改)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.vendor_member_code IS '服务商快钱子账户号(可空; 设置后 head.memberCode=子账户号, vendorMemberCode=会员号)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.merchant_id IS '当面付商户号(merchantId)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.terminal_id IS '当面付终端号(terminalId)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.merchant_cert_pfx IS '商户证书 pfx 内容(Base64, PKCS12, 签名与响应解密用, 加密存储)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.merchant_cert_password IS '商户证书密码(加密存储)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.platform_cert IS '快钱平台证书 cer 内容(Base64/PEM, 报文加密与验签用, 加密存储)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.ssl_cert_p12 IS 'SSL 客户端证书 p12 内容(Base64, MAS 网关双向认证用, 加密存储)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.ssl_cert_password IS 'SSL 客户端证书密码(加密存储)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.kuaiqian_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_kuaiqian_direct_key_cmchno_sandbox ON public.kuaiqian_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_kuaiqian_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增收钱吧直连通道密钥配置表 (shouqianba 产品: 终端号 terminal_sn + 终端密钥 terminal_key, upay v2 协议直连商户模式)
CREATE TABLE public.shouqianba_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    terminal_sn character varying(64),
    terminal_key text,
    operator character varying(32),
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.shouqianba_direct_key_config IS '收钱吧直连配置';
COMMENT ON COLUMN public.shouqianba_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.shouqianba_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.shouqianba_direct_key_config.channel_mch_no IS '通道商户号(SHOUQIANBA+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.shouqianba_direct_key_config.terminal_sn IS '收钱吧终端号(terminal_sn, 终端激活后获得, 创建时录入不可修改)';
COMMENT ON COLUMN public.shouqianba_direct_key_config.terminal_key IS '收钱吧终端密钥(terminal_key, 签名用, 加密存储; 签到重置后更新)';
COMMENT ON COLUMN public.shouqianba_direct_key_config.operator IS '操作员(收钱吧要求上送, 缺省 daxpay)';
COMMENT ON COLUMN public.shouqianba_direct_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN public.shouqianba_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.shouqianba_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.shouqianba_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.shouqianba_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.shouqianba_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.shouqianba_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_shouqianba_direct_key_cmchno_sandbox ON public.shouqianba_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_shouqianba_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增杉德直连通道密钥配置表 (sand_pay 产品: 杉德商户编号 mid+市场产品+商户 pfx 私钥证书/密码+杉德公钥证书, v4 开放平台 JSON+RSA 数字信封直连商户模式)
CREATE TABLE public.sand_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    mid character varying(64),
    merchant_cert_pfx text,
    merchant_cert_password text,
    sand_cert text,
    market_product character varying(16),
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.sand_direct_key_config IS '杉德直连配置';
COMMENT ON COLUMN public.sand_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.sand_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.sand_direct_key_config.channel_mch_no IS '通道商户号(SAND+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.sand_direct_key_config.mid IS '杉德商户编号(mid, 创建时录入不可修改)';
COMMENT ON COLUMN public.sand_direct_key_config.merchant_cert_pfx IS '商户私钥证书 pfx 内容(Base64, PKCS12, 签名与响应 encryptKey 解密用, 加密存储)';
COMMENT ON COLUMN public.sand_direct_key_config.merchant_cert_password IS '商户私钥证书密码(加密存储)';
COMMENT ON COLUMN public.sand_direct_key_config.sand_cert IS '杉德平台公钥证书 cer 内容(Base64/PEM, 请求加密与响应/回调验签用, 加密存储)';
COMMENT ON COLUMN public.sand_direct_key_config.market_product IS '市场产品编码(QZF 标准线上收款 / CSDB 企业杉德宝)';
COMMENT ON COLUMN public.sand_direct_key_config.sandbox IS '是否沙箱环境(true 走 UAT 网关)';
COMMENT ON COLUMN public.sand_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.sand_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.sand_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.sand_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.sand_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.sand_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_sand_direct_key_cmchno_sandbox ON public.sand_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_sand_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增星驿付直连通道密钥配置表 (xingyi_pay 产品: 机构号+商户号+商户RSA公钥, 驿云付 YYF 接口对接)
CREATE TABLE public.xingyi_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    aget_id character varying(64),
    cust_id character varying(64),
    merchant_public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.xingyi_direct_key_config IS '星驿付直连配置';
COMMENT ON COLUMN public.xingyi_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.xingyi_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.xingyi_direct_key_config.channel_mch_no IS '通道商户号(XINGYI+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.xingyi_direct_key_config.aget_id IS '机构号(agetId, 星驿付分配的机构标识, 创建时录入不可修改)';
COMMENT ON COLUMN public.xingyi_direct_key_config.cust_id IS '星驿付商户号(custId, 创建时录入不可修改)';
COMMENT ON COLUMN public.xingyi_direct_key_config.merchant_public_key IS '商户RSA公钥(Base64, 兼容PEM头尾; 请求签名与回调验签共用, 加密存储)';
COMMENT ON COLUMN public.xingyi_direct_key_config.sandbox IS '是否沙箱环境(true走测试网关xyf-server-test)';
COMMENT ON COLUMN public.xingyi_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.xingyi_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.xingyi_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.xingyi_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.xingyi_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.xingyi_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_xingyi_direct_key_cmchno_sandbox ON public.xingyi_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_xingyi_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增银盛直连通道密钥配置表 (一通道两产品: ysep_pay 银盛支付开放平台 + yseqt_pay 银盛e企通, 通道商户号全局唯一, 行内 product 表达归属)
CREATE TABLE public.ysep_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    product character varying(32),
    sponsor_mch_no character varying(64),
    payee_mch_no character varying(64),
    business_code character varying(64),
    merchant_cert_pfx text,
    merchant_cert_password text,
    ysepay_public_cert text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.ysep_key_config IS '银盛直连密钥配置(一通道两产品共用: ysep_pay 开放平台 / yseqt_pay e企通)';
COMMENT ON COLUMN public.ysep_key_config.id IS '主键';
COMMENT ON COLUMN public.ysep_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.ysep_key_config.channel_mch_no IS '通道商户号(YSEP+雪花, 创建时录入不可修改, 全局唯一)';
COMMENT ON COLUMN public.ysep_key_config.product IS '所属支付产品(ysep_pay 银盛支付 / yseqt_pay 银盛e企通, 表达密钥行归属, 对应 ProductEnum.code)';
COMMENT ON COLUMN public.ysep_key_config.sponsor_mch_no IS '发起方商户号(ysep_pay=partner_id / yseqt_pay=srcMerchantNo)';
COMMENT ON COLUMN public.ysep_key_config.payee_mch_no IS '收款商户号(ysep_pay=seller_id / yseqt_pay=payeeMerchantNo)';
COMMENT ON COLUMN public.ysep_key_config.business_code IS '业务代码(ysep_pay 专用 business_code, 银盛后台分配; yseqt_pay 不使用)';
COMMENT ON COLUMN public.ysep_key_config.merchant_cert_pfx IS '商户私钥证书 pfx 内容(Base64, PKCS12, SHA1withRSA 请求签名, 加密存储)';
COMMENT ON COLUMN public.ysep_key_config.merchant_cert_password IS '商户私钥证书密码(加密存储)';
COMMENT ON COLUMN public.ysep_key_config.ysepay_public_cert IS '银盛平台公钥证书 cer 内容(Base64/PEM, 响应/回调验签, 加密存储)';
COMMENT ON COLUMN public.ysep_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.ysep_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.ysep_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.ysep_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.ysep_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.ysep_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_ysep_key_channel_mch ON public.ysep_key_config USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_ysep_key_channel_mch IS '通道商户号唯一(一通道商户一条密钥, 产品由行内 product 表达)';

-- 建行龙支付通道(long_pay, 建设银行聚合支付, 直连商户模式)
CREATE TABLE public.long_pay_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    merchant_id character varying(64),
    pos_id character varying(64),
    branch_id character varying(64),
    merchant_public_key text,
    ccb_public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.long_pay_direct_key_config IS '建行龙支付直连配置';
COMMENT ON COLUMN public.long_pay_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.long_pay_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.long_pay_direct_key_config.channel_mch_no IS '通道商户号(LONGPAY+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.long_pay_direct_key_config.merchant_id IS '建行商户号(MERCHANTID, 开户后分配, 创建时录入不可修改)';
COMMENT ON COLUMN public.long_pay_direct_key_config.pos_id IS '建行柜台号(POSID, 创建时录入不可修改)';
COMMENT ON COLUMN public.long_pay_direct_key_config.branch_id IS '建行分行号(BRANCHID, 创建时录入不可修改)';
COMMENT ON COLUMN public.long_pay_direct_key_config.merchant_public_key IS '柜台公钥(请求 MD5 摘要 MAC 用, 取后 30 位参与拼串, 加密存储)';
COMMENT ON COLUMN public.long_pay_direct_key_config.ccb_public_key IS '建行公钥(异步通知 RSA MD5withRSA 验签用, DER hex 或 PEM, 加密存储)';
COMMENT ON COLUMN public.long_pay_direct_key_config.sandbox IS '是否沙箱环境(建行无公开沙箱网关, 预留双环境密钥并存)';
COMMENT ON COLUMN public.long_pay_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.long_pay_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.long_pay_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.long_pay_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.long_pay_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.long_pay_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_long_pay_key_cmchno_sandbox ON public.long_pay_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_long_pay_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增 Jeepay 计全通道密钥配置表 (jee_pay 产品: 平台网关地址+商户号+应用ID+应用密钥, JSON+MD5 普通商户模式, 对接三方 Jeepay 聚合平台)
CREATE TABLE public.jee_pay_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    server_url character varying(255),
    jeepay_mch_no character varying(64),
    app_id character varying(64),
    app_secret text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.jee_pay_key_config IS 'Jeepay计全通道密钥配置(通道商户维度)';
COMMENT ON COLUMN public.jee_pay_key_config.id IS '主键';
COMMENT ON COLUMN public.jee_pay_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.jee_pay_key_config.channel_mch_no IS '通道商户号(JEE+雪花, 唯一关联, 创建后不可修改)';
COMMENT ON COLUMN public.jee_pay_key_config.server_url IS 'Jeepay平台网关地址(如 https://pay.jeepay.vip)';
COMMENT ON COLUMN public.jee_pay_key_config.jeepay_mch_no IS 'Jeepay商户号(mchNo, 上游平台分配)';
COMMENT ON COLUMN public.jee_pay_key_config.app_id IS '应用ID(appId, Jeepay 商户后台的应用标识)';
COMMENT ON COLUMN public.jee_pay_key_config.app_secret IS '应用密钥(appSecret, MD5 签名钥, 请求签名与回调验签共用, 加密存储)';
COMMENT ON COLUMN public.jee_pay_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.jee_pay_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.jee_pay_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.jee_pay_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.jee_pay_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.jee_pay_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_jee_pay_key_channel_mch ON public.jee_pay_key_config USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_jee_pay_key_channel_mch IS '通道商户号唯一(一通道商户一条密钥)';

-- 2026-10-16 新增合利宝通道密钥配置表 (heli_pay 产品: 扫码产品线 form+MD5 直连商户模式, 微信/支付宝扫码+公众号JS+H5)
CREATE TABLE public.helipay_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    customer_number character varying(64),
    sub_customer_number character varying(64),
    sign_key text,
    public_app_id character varying(64),
    h5_app_id character varying(64),
    report_id character varying(128),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.helipay_key_config IS '合利宝通道密钥配置(扫码产品线直连商户模式)';
COMMENT ON COLUMN public.helipay_key_config.id IS '主键';
COMMENT ON COLUMN public.helipay_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.helipay_key_config.channel_mch_no IS '通道商户号(HELIPAY+雪花, 唯一关联, 创建后不可修改)';
COMMENT ON COLUMN public.helipay_key_config.customer_number IS '合利宝商户编号(customerNumber, 上送 P3_customerNumber)';
COMMENT ON COLUMN public.helipay_key_config.sub_customer_number IS '子商户号(可空, 设置后覆盖 P3_customerNumber 发起交易)';
COMMENT ON COLUMN public.helipay_key_config.sign_key IS '扫码产品签名密钥(MD5 签名与回调验签共用, 加密存储)';
COMMENT ON COLUMN public.helipay_key_config.public_app_id IS '公众号/生活号支付 AppId(微信 JS 支付必填, 支付宝生活号固定传 1 由子应用处理)';
COMMENT ON COLUMN public.helipay_key_config.h5_app_id IS 'H5 支付 AppId(可空, 微信 H5 场景使用)';
COMMENT ON COLUMN public.helipay_key_config.report_id IS '扫码支付报备ID(可空, 上送 P15_subMerchantId, 多个逗号分隔)';
COMMENT ON COLUMN public.helipay_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.helipay_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.helipay_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.helipay_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.helipay_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.helipay_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_helipay_key_channel_mch ON public.helipay_key_config USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_helipay_key_channel_mch IS '通道商户号唯一(一通道商户一条密钥)';

-- 2026-10-16 新增付呗直连通道密钥配置表 (fubei_pay 产品: 开放平台应用ID+接口密钥+商户ID+门店ID, JSON+MD5 直连商户模式, 对接付呗聚合支付)
CREATE TABLE public.fubei_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    app_id character varying(64),
    merchant_id character varying(64),
    store_id character varying(64),
    app_secret text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.fubei_direct_key_config IS '付呗直连密钥配置(通道商户维度)';
COMMENT ON COLUMN public.fubei_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.fubei_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.fubei_direct_key_config.channel_mch_no IS '通道商户号(FUBEI+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.fubei_direct_key_config.app_id IS '付呗开放平台应用ID(app_id, 创建时录入不可修改)';
COMMENT ON COLUMN public.fubei_direct_key_config.merchant_id IS '付呗商户ID(merchant_id, 服务商形态时必传, 可空)';
COMMENT ON COLUMN public.fubei_direct_key_config.store_id IS '付呗门店ID(store_id, 交易必传)';
COMMENT ON COLUMN public.fubei_direct_key_config.app_secret IS '接口密钥(app_secret, MD5 签名与回调验签共用, 加密存储)';
COMMENT ON COLUMN public.fubei_direct_key_config.sandbox IS '是否沙箱环境(付呗无独立沙箱网关, 预留双环境密钥并存)';
COMMENT ON COLUMN public.fubei_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.fubei_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.fubei_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.fubei_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.fubei_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.fubei_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_fubei_direct_key_cmchno_sandbox ON public.fubei_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_fubei_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增连连支付直连通道密钥配置表 (llian_pay 产品: 连连商户号+商户私钥+连连平台公钥, 连连 openapi 统收单线 + MD5withRSA 头签名)
CREATE TABLE public.llianpay_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    mch_id character varying(64),
    sub_mchid character varying(64),
    payee_uid character varying(64),
    busi_type character varying(16),
    merchant_private_key text,
    llianpay_public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.llianpay_direct_key_config IS '连连支付直连配置';
COMMENT ON COLUMN public.llianpay_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.llianpay_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.llianpay_direct_key_config.channel_mch_no IS '通道商户号(LLIANPAY+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.llianpay_direct_key_config.mch_id IS '连连商户编号(mch_id, 创建时录入不可修改)';
COMMENT ON COLUMN public.llianpay_direct_key_config.sub_mchid IS '二级商户号(可空; 平台商模式上送 sub_mchid)';
COMMENT ON COLUMN public.llianpay_direct_key_config.payee_uid IS '收款方用户ID(可空; 留空为商户自身收款, 配置后用户收款)';
COMMENT ON COLUMN public.llianpay_direct_key_config.busi_type IS '业务属性(busi_type, 如 100001 虚拟商品购买)';
COMMENT ON COLUMN public.llianpay_direct_key_config.merchant_private_key IS '商户私钥(Base64 裸串, 兼容 PKCS1/PKCS8, 请求签名用, 加密存储)';
COMMENT ON COLUMN public.llianpay_direct_key_config.llianpay_public_key IS '连连平台公钥(Base64 裸串, X509 格式, 响应/回调验签用, 加密存储)';
COMMENT ON COLUMN public.llianpay_direct_key_config.sandbox IS '是否沙箱环境(连连测试网关 mpay-openapi-ste2, 生产/沙箱密钥双环境并存)';
COMMENT ON COLUMN public.llianpay_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.llianpay_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.llianpay_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.llianpay_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.llianpay_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.llianpay_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_llianpay_direct_key_cmchno_sandbox ON public.llianpay_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_llianpay_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增嘉联直连通道密钥配置表 (jl_pay 产品: OpenAPI 开放平台应用appid+商户号+终端号+SM2密钥对(商户私钥签名/平台公钥验签), 国密直连商户模式)
CREATE TABLE public.jlpay_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    app_id character varying(64),
    mch_id character varying(64),
    term_no character varying(64),
    merchant_private_key text,
    platform_public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.jlpay_direct_key_config IS '嘉联直连配置';
COMMENT ON COLUMN public.jlpay_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.jlpay_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.jlpay_direct_key_config.channel_mch_no IS '通道商户号(JLPAY+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.jlpay_direct_key_config.app_id IS '嘉联开放平台应用appid(请求头 x-jlpay-appid, 创建时录入不可修改)';
COMMENT ON COLUMN public.jlpay_direct_key_config.mch_id IS '嘉联商户号(mch_id, 创建时录入不可修改)';
COMMENT ON COLUMN public.jlpay_direct_key_config.term_no IS '嘉联终端号(term_no)';
COMMENT ON COLUMN public.jlpay_direct_key_config.merchant_private_key IS '商户SM2私钥(hex或PEM文本, 请求签名用, 加密存储)';
COMMENT ON COLUMN public.jlpay_direct_key_config.platform_public_key IS '嘉联平台SM2公钥(hex或PEM文本, 响应/通知验签用, 加密存储)';
COMMENT ON COLUMN public.jlpay_direct_key_config.sandbox IS '是否沙箱环境(true走UAT网关 openapi-uat.jlpay.com)';
COMMENT ON COLUMN public.jlpay_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.jlpay_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.jlpay_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.jlpay_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.jlpay_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.jlpay_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_jlpay_direct_key_cmchno_sandbox ON public.jlpay_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_jlpay_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增易票联直连通道密钥配置表 (epay_links 产品: EFPS 网关商户编号+证书序列号+商户私钥+平台公钥证书, JSON+SHA256withRSA 头签名直连商户模式)
CREATE TABLE public.epaylinks_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    customer_code character varying(64),
    channel_mcht_no character varying(64),
    sign_no text,
    merchant_private_key text,
    platform_cert text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.epaylinks_direct_key_config IS '易票联直连配置';
COMMENT ON COLUMN public.epaylinks_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.epaylinks_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.epaylinks_direct_key_config.channel_mch_no IS '通道商户号(EPAYLINKS+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.customer_code IS '易票联商户编号(customerCode, 创建时录入不可修改)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.channel_mcht_no IS '上游商户号(可空, 设置后下单上送 channelMchtNo 指定受理商户)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.sign_no IS '证书序列号(sign_no, 与商户私钥证书配套, 请求头 x-efps-sign-no, 加密存储)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.merchant_private_key IS '商户私钥(PEM/Base64, 兼容 PKCS1/PKCS8, 请求签名用, 加密存储)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.platform_cert IS '易票联平台公钥证书(efps.cer, PEM/Base64, 回调验签用, 加密存储)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.sandbox IS '是否沙箱环境(易票联无独立沙箱网关, 生产/沙箱密钥双环境并存)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.epaylinks_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.epaylinks_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.epaylinks_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.epaylinks_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.epaylinks_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_epaylinks_direct_key_cmchno_sandbox ON public.epaylinks_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_epaylinks_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增联动优势直连通道密钥配置表 (umf_pay 产品: 联动优势商户号mer_id+商户私钥/平台公钥 RSA(SHA1) 密钥对, 老网关 payservice.do 表单直连商户模式)
CREATE TABLE public.umfpay_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    mer_id character varying(64),
    merchant_private_key text,
    platform_public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.umfpay_direct_key_config IS '联动优势直连配置';
COMMENT ON COLUMN public.umfpay_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.umfpay_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.umfpay_direct_key_config.channel_mch_no IS '通道商户号(UMFPAY+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.umfpay_direct_key_config.mer_id IS '联动优势商户号(mer_id, 创建时录入不可修改)';
COMMENT ON COLUMN public.umfpay_direct_key_config.merchant_private_key IS '商户私钥(PEM/Base64文本, 兼容PKCS1/PKCS8, 请求/应答签名用, 加密存储)';
COMMENT ON COLUMN public.umfpay_direct_key_config.platform_public_key IS '联动优势平台公钥(PEM/Base64文本, X509格式, 响应/回调验签用, 加密存储)';
COMMENT ON COLUMN public.umfpay_direct_key_config.sandbox IS '是否沙箱环境(联动优势老网关无独立沙箱地址, 生产/沙箱密钥双环境并存)';
COMMENT ON COLUMN public.umfpay_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.umfpay_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.umfpay_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.umfpay_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.umfpay_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.umfpay_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_umfpay_direct_key_cmchno_sandbox ON public.umfpay_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_umfpay_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增易生直连通道密钥配置表 (yisheng_pay 产品: 合作伙伴ID partner+商户编号 merchant_id+商户私钥/易生平台公钥 RSA(SHA256) 密钥对, api_gateway.do 表单类支付宝协议直连商户模式)
CREATE TABLE public.yisheng_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    partner character varying(64),
    merchant_id character varying(64),
    merchant_private_key text,
    platform_public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.yisheng_direct_key_config IS '易生直连配置';
COMMENT ON COLUMN public.yisheng_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.yisheng_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.yisheng_direct_key_config.channel_mch_no IS '通道商户号(YISHENG+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.yisheng_direct_key_config.partner IS '合作伙伴ID(reqId, 网关公共参数 partner, 创建时录入不可修改)';
COMMENT ON COLUMN public.yisheng_direct_key_config.merchant_id IS '易生商户编号(merchant_id, 创建时录入不可修改)';
COMMENT ON COLUMN public.yisheng_direct_key_config.merchant_private_key IS '商户RSA私钥(Base64/PEM文本, 兼容PKCS1/PKCS8, biz_content 请求签名用, 加密存储)';
COMMENT ON COLUMN public.yisheng_direct_key_config.platform_public_key IS '易生平台RSA公钥(Base64/PEM文本, X509格式, 响应/回调验签用, 加密存储)';
COMMENT ON COLUMN public.yisheng_direct_key_config.sandbox IS '是否沙箱环境(易生网关生产/沙箱同址, 生产/沙箱密钥双环境并存)';
COMMENT ON COLUMN public.yisheng_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.yisheng_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.yisheng_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.yisheng_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.yisheng_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.yisheng_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_yisheng_direct_key_cmchno_sandbox ON public.yisheng_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_yisheng_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增中金直连通道密钥配置表 (cpcn_pay 产品: 中金机构号+网关地址+指定分账子商户号+两份证书(商户 pfx 国密SM2私钥/中金平台证书), 5011/5016/5021 线国密 SM2 签名非数字信封模式)
CREATE TABLE public.cpcn_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    institution_id character varying(64),
    gateway_url character varying(255),
    appoint_merchant_ids character varying(512),
    merchant_cert_pfx text,
    merchant_cert_password character varying(255),
    platform_cert text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.cpcn_direct_key_config IS '中金直连配置';
COMMENT ON COLUMN public.cpcn_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.cpcn_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.cpcn_direct_key_config.channel_mch_no IS '通道商户号(CPCN+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.cpcn_direct_key_config.institution_id IS '中金机构编号(InstitutionID, 创建时录入不可修改)';
COMMENT ON COLUMN public.cpcn_direct_key_config.gateway_url IS '中金网关地址(开户开通后下发, 无统一公开网关)';
COMMENT ON COLUMN public.cpcn_direct_key_config.appoint_merchant_ids IS '指定分账子商户号(可空, 多个用英文逗号分隔, 上送 Extension.AppointMerchantInfos)';
COMMENT ON COLUMN public.cpcn_direct_key_config.merchant_cert_pfx IS '商户证书pfx内容(Base64, PKCS12国密SM2私钥, 请求签名用, 加密存储)';
COMMENT ON COLUMN public.cpcn_direct_key_config.merchant_cert_password IS '商户证书密码(加密存储)';
COMMENT ON COLUMN public.cpcn_direct_key_config.platform_cert IS '中金平台证书cer内容(Base64或PEM, 响应/通知验签用, 加密存储)';
COMMENT ON COLUMN public.cpcn_direct_key_config.sandbox IS '是否沙箱环境(中金测试环境与生产环境网关/证书独立, 双环境密钥并存)';
COMMENT ON COLUMN public.cpcn_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.cpcn_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.cpcn_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.cpcn_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.cpcn_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.cpcn_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_cpcn_direct_key_cmchno_sandbox ON public.cpcn_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_cpcn_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增宝付直连通道密钥配置表 (baofu_pay 产品: 商户号+终端号+聚合交易商户号+两份证书(商户 pfx/宝付平台 cer), 聚合 juhe 网关表单+RSA(SHA256 hex) 协议直连商户模式)
CREATE TABLE public.baofu_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    mer_id character varying(64),
    ter_id character varying(64),
    sub_mch_id character varying(64),
    merchant_cert_pfx text,
    merchant_cert_password text,
    platform_cert text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.baofu_direct_key_config IS '宝付直连配置';
COMMENT ON COLUMN public.baofu_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.baofu_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.baofu_direct_key_config.channel_mch_no IS '通道商户号(BAOFU+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.baofu_direct_key_config.mer_id IS '宝付商户号(merId, 收款线, 创建时录入不可修改)';
COMMENT ON COLUMN public.baofu_direct_key_config.ter_id IS '宝付终端号(terId, 收款线, 创建时录入不可修改)';
COMMENT ON COLUMN public.baofu_direct_key_config.sub_mch_id IS '聚合交易商户号(subMchId, 在微信/支付宝报备的商户号, 聚合收银台场景上送)';
COMMENT ON COLUMN public.baofu_direct_key_config.merchant_cert_pfx IS '商户证书 pfx 内容(Base64, PKCS12, 请求签名用, 加密存储)';
COMMENT ON COLUMN public.baofu_direct_key_config.merchant_cert_password IS '商户证书密码(加密存储)';
COMMENT ON COLUMN public.baofu_direct_key_config.platform_cert IS '宝付平台证书 cer 内容(Base64/PEM, 响应与回调验签用, 加密存储)';
COMMENT ON COLUMN public.baofu_direct_key_config.sandbox IS '是否沙箱环境(宝付聚合线无公开沙箱网关, 双环境密钥并存)';
COMMENT ON COLUMN public.baofu_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.baofu_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.baofu_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.baofu_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.baofu_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.baofu_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_baofu_direct_key_cmchno_sandbox ON public.baofu_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_baofu_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';


-- 2026-10-16 新增哆啦宝直连通道密钥配置表 (duolabao_pay 产品: 商户编号 customerNum+店铺编号 shopNum+代理商编号 agentNum+accessKey/secretKey 密钥对, 开放平台 openapi.duolabao.com V4.0 对接)
CREATE TABLE public.duolabao_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    customer_num character varying(64),
    shop_num character varying(64),
    agent_num character varying(64),
    access_key text,
    secret_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.duolabao_direct_key_config IS '哆啦宝直连配置';
COMMENT ON COLUMN public.duolabao_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.duolabao_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.duolabao_direct_key_config.channel_mch_no IS '通道商户号(DUOLABAO+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.duolabao_direct_key_config.customer_num IS '商户编号(customerNum, 哆啦宝商户唯一标识, 创建时录入不可修改)';
COMMENT ON COLUMN public.duolabao_direct_key_config.shop_num IS '店铺编号(shopNum, 可空)';
COMMENT ON COLUMN public.duolabao_direct_key_config.agent_num IS '代理商编号(agentNum, 可空, 非代理商不需要填写)';
COMMENT ON COLUMN public.duolabao_direct_key_config.access_key IS '公钥(accessKey, Header 上送的身份标识, 加密存储)';
COMMENT ON COLUMN public.duolabao_direct_key_config.secret_key IS '私钥(secretKey, token 签名与回调验签用, 不上送, 加密存储)';
COMMENT ON COLUMN public.duolabao_direct_key_config.sandbox IS '是否沙箱环境(哆啦宝无独立沙箱网关, 标识透传记录)';
COMMENT ON COLUMN public.duolabao_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.duolabao_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.duolabao_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.duolabao_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.duolabao_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.duolabao_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_duolabao_direct_key_cmchno_sandbox ON public.duolabao_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_duolabao_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增有赞直连通道密钥配置表 (youzan_pay 产品: 高汇通商户号 + 商户RSA私钥/有赞平台公钥/公钥唯一标识密钥三元组, youzan.pay.unified 统收单线 JSON + SHA256withRSA 请求头签名直连商户模式)
CREATE TABLE public.youzan_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    merchant_id character varying(64),
    merchant_private_key text,
    platform_public_key text,
    public_key_id character varying(128),
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.youzan_direct_key_config IS '有赞直连配置';
COMMENT ON COLUMN public.youzan_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.youzan_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.youzan_direct_key_config.channel_mch_no IS '通道商户号(YOUZAN+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.youzan_direct_key_config.merchant_id IS '高汇通商户号(mch_id, 请求头 mchid 与 body mch_id 同值, 创建时录入不可修改)';
COMMENT ON COLUMN public.youzan_direct_key_config.merchant_private_key IS '商户RSA私钥(Base64/PEM文本, 兼容PKCS1/PKCS8, 请求签名用, 加密存储)';
COMMENT ON COLUMN public.youzan_direct_key_config.platform_public_key IS '有赞支付平台RSA公钥(Base64/PEM文本, X509格式, 响应/回调验签用, 加密存储)';
COMMENT ON COLUMN public.youzan_direct_key_config.public_key_id IS '公钥唯一标识(请求头 publickeyid, 与平台公钥成对下发, 加密存储)';
COMMENT ON COLUMN public.youzan_direct_key_config.sandbox IS '是否沙箱环境(高汇通无独立沙箱网关, 生产/沙箱密钥双环境并存)';
COMMENT ON COLUMN public.youzan_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.youzan_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.youzan_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.youzan_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.youzan_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.youzan_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_youzan_direct_key_cmchno_sandbox ON public.youzan_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_youzan_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-16 新增一直付(北京)直连通道密钥配置表 (yizhifubj_pay 产品: 一直付商户编号+服务端ID+报备序列号+商户 pfx 证书/5upay 平台证书, 5upay 网关 AES+RSA 数字信封协议)
CREATE TABLE public.yizhifubj_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    merchant_id character varying(64),
    partner_id character varying(64),
    report_serial_no character varying(128),
    cert_pfx text,
    cert_password text,
    platform_cert text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);

COMMENT ON TABLE public.yizhifubj_direct_key_config IS '一直付(北京)直连配置';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.id IS '主键';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.mch_no IS '商户号';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.channel_mch_no IS '通道商户号(YIZHIFUBJ+雪花, 创建时录入不可修改)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.merchant_id IS '一直付商户编号(merchantId, 创建时录入不可修改)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.partner_id IS '服务端ID(partnerId, 可空, 机构服务端形态下下发)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.report_serial_no IS '报备序列号(reportSerialNo, 可空, 下单上送通道侧报备用)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.cert_pfx IS '商户 pfx 证书内容(Base64, PKCS12, 请求签名与 encryptKey 解密用, 加密存储)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.cert_password IS '商户证书密码(加密存储)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.platform_cert IS '5upay 平台证书内容(Base64/DER, 可空, AES 密钥加密与验签用, 空时子应用用内置默认证书, 加密存储)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.sandbox IS '是否沙箱环境';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.creator IS '创建者ID';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.create_time IS '创建时间';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.yizhifubj_direct_key_config.deleted IS '删除标志';

CREATE UNIQUE INDEX uk_yizhifubj_direct_key_cmchno_sandbox ON public.yizhifubj_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);
COMMENT ON INDEX public.uk_yizhifubj_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';

-- 2026-10-19 菜单表新增 query_sync 列(query参数同步页/单页签模式)
-- 开启后该路径所有 query 参数组合共用一个页签(页签 key 忽略 query, 对应前端路由 meta.fullPathKey=false)
-- 仅适用于页面内部自行响应 query 变化的参数定位页(?tab= 分组页/Hub 页), 默认关闭维持页签按完整路径区分
ALTER TABLE public.iam_perm_menu ADD COLUMN IF NOT EXISTS query_sync boolean NOT NULL DEFAULT false;
COMMENT ON COLUMN public.iam_perm_menu.query_sync IS 'query参数同步页(单页签模式): 开启后该路径所有query参数组合共用一个页签(页签key忽略query)。仅适用于页面内部自行响应query变化的参数定位页(如?tab=分组页/useQueryTab配套), 普通实体定位工作台(仅setup读一次query)开启会导致同页签切参数不刷新数据';
