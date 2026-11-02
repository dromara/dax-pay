-- ----------------------------
-- 增量表结构脚本(上一版本 -> 当前版本)
-- 发版时本文件无需改动, 保持空白即可; 下一版本周期内的表结构变化再追加于此
-- ----------------------------

-- ========== 2026-10-24 虚拟设备简版(收款播报+打印): 虚拟设备台账 + 应用设备联动配置两表 ==========
-- 虚拟设备(播报打印一体机, DaxPay 收款播报 APP)独立台账: 设备号平台自造号段(VD+日期+随机段),
-- 生成入平台库存后绑定商户/应用; 开源版无代理商域, 无 agent_no 列。
CREATE TABLE IF NOT EXISTS public.pay_virtual_device (
    id bigint NOT NULL,
    name character varying(128),
    device_no character varying(64),
    batch_no character varying(64),
    mch_no character varying(32),
    app_id character varying(64),
    store_no character varying(32),
    enable boolean DEFAULT true NOT NULL,
    secret character varying(64),
    activated_time timestamp(6) with time zone,
    last_pay_ack_time timestamp(6) with time zone,
    last_pay_ack_msg_id character varying(64),
    last_pay_ack_ok boolean,
    last_online_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT now(),
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    CONSTRAINT pk_pay_virtual_device PRIMARY KEY (id)
);
-- 存量库补列: 建表语句已含该列, 本行幂等(覆盖建表后新增激活时间的开发/测试环境)
ALTER TABLE public.pay_virtual_device ADD COLUMN IF NOT EXISTS activated_time timestamp(6) with time zone;
COMMENT ON TABLE public.pay_virtual_device IS '虚拟设备: 播报打印一体机(收款播报 APP)独立台账, 设备号平台自造号段(VD+日期+随机段), 生成入平台库存后绑定商户/应用';
COMMENT ON COLUMN public.pay_virtual_device.name IS '设备名称';
COMMENT ON COLUMN public.pay_virtual_device.device_no IS '设备号(平台自造 SN: VD+日期+6位随机段, 创建后不可改, 激活唯一准入凭据)';
COMMENT ON COLUMN public.pay_virtual_device.batch_no IS '批次号(批量生成时写入)';
COMMENT ON COLUMN public.pay_virtual_device.mch_no IS '所属商户号(绑定后写入, 空=平台库存, 未绑定不可激活)';
COMMENT ON COLUMN public.pay_virtual_device.app_id IS '关联应用号(绑定应用后有值, 空=全部应用)';
COMMENT ON COLUMN public.pay_virtual_device.store_no IS '使用门店号(空=未分配, 不参与门店级播报/打印匹配, 对应 mch_store_info.store_no)';
COMMENT ON COLUMN public.pay_virtual_device.enable IS '是否启用(停用后设备心跳返回 disabled, 终端停止播报)';
COMMENT ON COLUMN public.pay_virtual_device.secret IS '播报加密密钥(AES-256-GCM 32字符, 激活时生成并轮换, 解绑时清空即回到未激活态)';
COMMENT ON COLUMN public.pay_virtual_device.activated_time IS '最近一次成功激活时间(激活轮换 secret 时写入, 解绑清空即回到未激活态, 未成功激活过为空)';
COMMENT ON COLUMN public.pay_virtual_device.last_pay_ack_time IS '最近一次收款播报回执时间(仅 cmd=pay.success 的 ack 回写, 心跳不覆盖)';
COMMENT ON COLUMN public.pay_virtual_device.last_pay_ack_msg_id IS '最近一次收款播报回执消息ID(与推送 msgId 对应, 排障定位用)';
COMMENT ON COLUMN public.pay_virtual_device.last_pay_ack_ok IS '最近一次收款播报回执结果(true=设备已完成播报)';
COMMENT ON COLUMN public.pay_virtual_device.last_online_time IS '设备最后在线时间(心跳 30 分钟节流落库, 非实时; 实时在线判定走 Redis ZSET)';
CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_virtual_device_device_no ON public.pay_virtual_device USING btree (device_no) WHERE ((deleted = false) AND (device_no IS NOT NULL));
COMMENT ON INDEX public.uk_pay_virtual_device_device_no IS '设备号唯一(SN 为激活唯一锚点, 仅未删除行)';
CREATE INDEX IF NOT EXISTS idx_pay_virtual_device_batch_no ON public.pay_virtual_device USING btree (batch_no);
COMMENT ON INDEX public.idx_pay_virtual_device_batch_no IS '批次号索引(按批次查询)';
CREATE INDEX IF NOT EXISTS idx_pay_virtual_device_mch_no ON public.pay_virtual_device USING btree (mch_no);
COMMENT ON INDEX public.idx_pay_virtual_device_mch_no IS '商户号索引(商户名下设备查询/解绑级联入口)';
CREATE INDEX IF NOT EXISTS idx_pay_virtual_device_mch_store ON public.pay_virtual_device USING btree (mch_no, store_no);
COMMENT ON INDEX public.idx_pay_virtual_device_mch_store IS '商户+使用门店索引(播报/打印按门店作用域匹配)';
CREATE INDEX IF NOT EXISTS idx_pay_virtual_device_mch_app ON public.pay_virtual_device USING btree (mch_no, app_id);
COMMENT ON INDEX public.idx_pay_virtual_device_mch_app IS '商户+应用索引(播报/打印按应用作用域匹配)';

-- 应用级设备联动配置(一应用一行: 播报/打印/门店严格匹配三个开关+内置版式代码; 行不存在=默认全开, 存量商户行为零变化)
-- 开源版无模板表: template_code 为内置版式常量代码(首期 standard 标准小票), 非 pay_print_template 外键。
CREATE TABLE IF NOT EXISTS public.pay_mch_app_device_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(64) NOT NULL,
    broadcast_enable boolean DEFAULT true NOT NULL,
    print_enable boolean DEFAULT true NOT NULL,
    store_strict_match boolean,
    template_code character varying(32) DEFAULT 'standard'::character varying NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT now(),
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    CONSTRAINT pk_pay_mch_app_device_config PRIMARY KEY (id)
);
-- 存量库补列: 建表语句已含该列, 本行幂等(覆盖本次增量前已按旧结构建过表的开发/测试环境)
ALTER TABLE public.pay_mch_app_device_config ADD COLUMN IF NOT EXISTS store_strict_match boolean;
COMMENT ON TABLE public.pay_mch_app_device_config IS '商户应用设备联动配置: 播报/打印/门店严格匹配开关与打印版式代码挂应用维度, 配置收归一点; 行不存在=默认全开';
COMMENT ON COLUMN public.pay_mch_app_device_config.mch_no IS '所属商户号(冗余自应用, 便于归属校验与审计)';
COMMENT ON COLUMN public.pay_mch_app_device_config.app_id IS '应用号(全局唯一, 一应用一行)';
COMMENT ON COLUMN public.pay_mch_app_device_config.broadcast_enable IS '是否播报(false=该应用支付成功不触发任何设备联动播报)';
COMMENT ON COLUMN public.pay_mch_app_device_config.print_enable IS '是否允许打印(false=虚拟设备消息不附带 printData)';
COMMENT ON COLUMN public.pay_mch_app_device_config.store_strict_match IS '门店订单严格匹配(true=门店订单仅推绑定该门店的设备, 未绑门店设备不参与; false/空=未绑门店设备视为商户通用照常推送)';
COMMENT ON COLUMN public.pay_mch_app_device_config.template_code IS '打印版式代码(内置版式常量, 首期 standard 标准小票; 非 pay_print_template 外键)';
CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_mch_app_device_config_app ON public.pay_mch_app_device_config USING btree (app_id) WHERE (deleted = false);
COMMENT ON INDEX public.uk_pay_mch_app_device_config_app IS '一应用一行配置(仅未删除行; 行不存在由消费方解释为默认全开)';

-- ========== 2026-10-31 mock 模拟通道(开源版 P0): 6 张 mock 私有表 ==========
-- 来源: plus 同名表定义摘录; 时间列统一 timestamptz(6)(open 规范), 剔除 plus 代理商域 agent_no 列(open 无代理商域);
-- mock 通道为仿真第三方通道(mock_pay 直连 / mock_isv 服务商 双产品), 仅 DEV 装配 mock 子应用, 联调/演示用;
-- 幂等写法, 全新安装库与开发库执行均无副作用。
CREATE TABLE IF NOT EXISTS public.mock_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    isv_id bigint,
    CONSTRAINT pk_mock_isv_channel_merchant PRIMARY KEY (id)
);
COMMENT ON TABLE public.mock_isv_channel_merchant IS 'Mock 服务商通道商户绑定';
COMMENT ON COLUMN public.mock_isv_channel_merchant.id IS '主键';
COMMENT ON COLUMN public.mock_isv_channel_merchant.mch_no IS '商户号';
COMMENT ON COLUMN public.mock_isv_channel_merchant.channel_mch_no IS '通道商户号(模拟控制台手填, 与主表同值)';
COMMENT ON COLUMN public.mock_isv_channel_merchant.product IS '所属支付产品(恒为 mock_isv)';
COMMENT ON COLUMN public.mock_isv_channel_merchant.creator IS '创建人';
COMMENT ON COLUMN public.mock_isv_channel_merchant.create_time IS '创建时间';
COMMENT ON COLUMN public.mock_isv_channel_merchant.last_modifier IS '最后修改人';
COMMENT ON COLUMN public.mock_isv_channel_merchant.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.mock_isv_channel_merchant.version IS '乐观锁版本号';
COMMENT ON COLUMN public.mock_isv_channel_merchant.deleted IS '逻辑删除标志';
COMMENT ON COLUMN public.mock_isv_channel_merchant.isv_id IS '归属模拟服务商ID(指向 mock_isv_info.id, 创建时选定, 创建后不可改)';
CREATE UNIQUE INDEX IF NOT EXISTS uk_mock_isv_channel_merchant_channel_mch ON public.mock_isv_channel_merchant USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_mock_isv_channel_merchant_channel_mch IS '通道商户号唯一(未删除行)';

CREATE TABLE IF NOT EXISTS public.mock_isv_info (
    id bigint NOT NULL,
    isv_no character varying(32) NOT NULL,
    isv_name character varying(64) NOT NULL,
    enable boolean DEFAULT true NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    pay_enable boolean DEFAULT true NOT NULL,
    CONSTRAINT pk_mock_isv_info PRIMARY KEY (id)
);
COMMENT ON TABLE public.mock_isv_info IS 'Mock 模拟服务商主数据';
COMMENT ON COLUMN public.mock_isv_info.id IS '主键';
COMMENT ON COLUMN public.mock_isv_info.isv_no IS '服务商编号(MISV+16位数字, 创建时自动生成不可改; 通道商户主表 isv_no 固化来源)';
COMMENT ON COLUMN public.mock_isv_info.isv_name IS '服务商名称';
COMMENT ON COLUMN public.mock_isv_info.enable IS '是否允许新建服务商通道商户(停用后不影响已有通道商户支付)';
COMMENT ON COLUMN public.mock_isv_info.creator IS '创建人';
COMMENT ON COLUMN public.mock_isv_info.create_time IS '创建时间';
COMMENT ON COLUMN public.mock_isv_info.last_modifier IS '最后修改人';
COMMENT ON COLUMN public.mock_isv_info.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.mock_isv_info.version IS '乐观锁版本号';
COMMENT ON COLUMN public.mock_isv_info.deleted IS '逻辑删除标志';
COMMENT ON COLUMN public.mock_isv_info.pay_enable IS '是否启用服务商支付(停用后仅阻止新支付下单, 不影响存量交易处理)';
CREATE UNIQUE INDEX IF NOT EXISTS uk_mock_isv_info_isvno ON public.mock_isv_info USING btree (isv_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_mock_isv_info_isvno IS '服务商编号唯一(未删除行)';

CREATE TABLE IF NOT EXISTS public.mock_isv_credential (
    id bigint NOT NULL,
    isv_no character varying(32) NOT NULL,
    merchant_private_key text,
    platform_public_key text,
    gateway_base_url character varying(256),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    CONSTRAINT pk_mock_isv_credential PRIMARY KEY (id)
);
COMMENT ON TABLE public.mock_isv_credential IS 'Mock 服务商接入密钥(按服务商一行, 仅 DEV 装配)';
COMMENT ON COLUMN public.mock_isv_credential.id IS '主键';
COMMENT ON COLUMN public.mock_isv_credential.isv_no IS '服务商编号(指向 mock_isv_info.isv_no, 一服务商一行未删除记录)';
COMMENT ON COLUMN public.mock_isv_credential.merchant_private_key IS '商户私钥(PEM PKCS#8, 加密存储; 留空保留原值)';
COMMENT ON COLUMN public.mock_isv_credential.platform_public_key IS '平台公钥(PEM SPKI, 加密存储; 与商户私钥须齐配)';
COMMENT ON COLUMN public.mock_isv_credential.gateway_base_url IS '服务商接入网关地址(可空; 通道子应用侧非空优先选址, 空回落 yml base-url)';
COMMENT ON COLUMN public.mock_isv_credential.creator IS '创建人';
COMMENT ON COLUMN public.mock_isv_credential.create_time IS '创建时间';
COMMENT ON COLUMN public.mock_isv_credential.last_modifier IS '最后修改人';
COMMENT ON COLUMN public.mock_isv_credential.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.mock_isv_credential.version IS '乐观锁版本号';
COMMENT ON COLUMN public.mock_isv_credential.deleted IS '逻辑删除标志';
CREATE UNIQUE INDEX IF NOT EXISTS uk_mock_isv_credential_isvno ON public.mock_isv_credential USING btree (isv_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_mock_isv_credential_isvno IS '服务商编号唯一(未删除行)';

CREATE TABLE IF NOT EXISTS public.mock_channel_credential (
    id bigint NOT NULL,
    merchant_private_key text,
    platform_public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    CONSTRAINT pk_mock_channel_credential PRIMARY KEY (id)
);
COMMENT ON TABLE public.mock_channel_credential IS 'Mock 通道网关签名密钥(全局单行, 仅 DEV 装配)';
COMMENT ON COLUMN public.mock_channel_credential.id IS '主键(全局单行, 固定为1)';
COMMENT ON COLUMN public.mock_channel_credential.merchant_private_key IS '商户私钥(PEM PKCS#8 多行原文, 加密存储; 请求签名用, 与模拟网关侧商户公钥成对)';
COMMENT ON COLUMN public.mock_channel_credential.platform_public_key IS '平台公钥(PEM SPKI 多行原文, 加密存储; 响应/回调验签用, 与模拟网关侧平台私钥成对)';
COMMENT ON COLUMN public.mock_channel_credential.creator IS '创建人';
COMMENT ON COLUMN public.mock_channel_credential.create_time IS '创建时间';
COMMENT ON COLUMN public.mock_channel_credential.last_modifier IS '最后修改人';
COMMENT ON COLUMN public.mock_channel_credential.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.mock_channel_credential.version IS '乐观锁版本号';
COMMENT ON COLUMN public.mock_channel_credential.deleted IS '逻辑删除标志';

CREATE TABLE IF NOT EXISTS public.mock_direct_credential (
    id bigint NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    merchant_private_key text,
    platform_public_key text,
    gateway_base_url character varying(256),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    CONSTRAINT pk_mock_direct_credential PRIMARY KEY (id)
);
COMMENT ON TABLE public.mock_direct_credential IS 'Mock 直连商户接入密钥(按通道商户一行, 仅 DEV 装配)';
COMMENT ON COLUMN public.mock_direct_credential.id IS '主键';
COMMENT ON COLUMN public.mock_direct_credential.channel_mch_no IS '通道商户号(通道商户主表 channel_mch_no, 一通道商户一行未删除记录)';
COMMENT ON COLUMN public.mock_direct_credential.merchant_private_key IS '商户私钥(PEM PKCS#8, 加密存储; 留空保留原值)';
COMMENT ON COLUMN public.mock_direct_credential.platform_public_key IS '平台公钥(PEM SPKI 多行原文, 加密存储; 直连产品网关响应/回调验签用, 留空保存保留原值)';
COMMENT ON COLUMN public.mock_direct_credential.gateway_base_url IS '商户接入网关地址(可空; 通道子应用侧非空优先选址, 空回落 yml base-url)';
COMMENT ON COLUMN public.mock_direct_credential.creator IS '创建人';
COMMENT ON COLUMN public.mock_direct_credential.create_time IS '创建时间';
COMMENT ON COLUMN public.mock_direct_credential.last_modifier IS '最后修改人';
COMMENT ON COLUMN public.mock_direct_credential.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.mock_direct_credential.version IS '乐观锁版本号';
COMMENT ON COLUMN public.mock_direct_credential.deleted IS '逻辑删除标志';
CREATE UNIQUE INDEX IF NOT EXISTS uk_mock_direct_credential_cmchno ON public.mock_direct_credential USING btree (channel_mch_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_mock_direct_credential_cmchno IS '通道商户号唯一(未删除行)';

CREATE TABLE IF NOT EXISTS public.mock_alloc_receiver (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    receiver_type character varying(32) NOT NULL,
    receiver_account character varying(256) NOT NULL,
    account_hash character varying(64) NOT NULL,
    receiver_name character varying(256),
    alias character varying(50),
    status character varying(16) NOT NULL,
    error_msg text,
    bind_time timestamp(6) with time zone,
    unbind_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    CONSTRAINT pk_mock_alloc_receiver PRIMARY KEY (id)
);
COMMENT ON TABLE public.mock_alloc_receiver IS 'Mock 分账接收方绑定档案';
COMMENT ON COLUMN public.mock_alloc_receiver.id IS '主键';
COMMENT ON COLUMN public.mock_alloc_receiver.mch_no IS '商户号';
COMMENT ON COLUMN public.mock_alloc_receiver.channel_mch_no IS '通道商户号(关联通道商户主表)';
COMMENT ON COLUMN public.mock_alloc_receiver.product IS '所属支付产品(mock_pay/mock_isv, 冗余自通道商户)';
COMMENT ON COLUMN public.mock_alloc_receiver.receiver_type IS '接收方类型';
COMMENT ON COLUMN public.mock_alloc_receiver.receiver_account IS '接收方账号(AES-256-GCM 加密存储)';
COMMENT ON COLUMN public.mock_alloc_receiver.account_hash IS '接收方账号 SHA-256 哈希(密文不确定, 查重与等值定位用)';
COMMENT ON COLUMN public.mock_alloc_receiver.receiver_name IS '接收方名称(AES-256-GCM 加密存储, MERCHANT_ID 时必填商户全称)';
COMMENT ON COLUMN public.mock_alloc_receiver.alias IS '接收方别名(本地备注, 不上送通道, 可随时修改)';
COMMENT ON COLUMN public.mock_alloc_receiver.status IS '绑定状态(bound/unbound/fail)';
COMMENT ON COLUMN public.mock_alloc_receiver.error_msg IS '最近一次绑定/解绑失败原因';
COMMENT ON COLUMN public.mock_alloc_receiver.bind_time IS '绑定成功时间';
COMMENT ON COLUMN public.mock_alloc_receiver.unbind_time IS '解绑成功时间';
COMMENT ON COLUMN public.mock_alloc_receiver.creator IS '创建人';
COMMENT ON COLUMN public.mock_alloc_receiver.create_time IS '创建时间';
COMMENT ON COLUMN public.mock_alloc_receiver.last_modifier IS '最后修改人';
COMMENT ON COLUMN public.mock_alloc_receiver.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.mock_alloc_receiver.version IS '乐观锁版本号';
COMMENT ON COLUMN public.mock_alloc_receiver.deleted IS '逻辑删除标志';
CREATE UNIQUE INDEX IF NOT EXISTS uk_mock_alloc_receiver ON public.mock_alloc_receiver USING btree (channel_mch_no, receiver_type, account_hash) WHERE (deleted = false);
COMMENT ON INDEX public.uk_mock_alloc_receiver IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';

-- ========== 2026-10-31 分账多次分账/独立完结: 分账单类型与完结模式 + 原支付已分累计金额 ==========
-- 分账单类型(alloc-分账单 / finish-完结单): 完结单解冻剩余冻结资金给商户, 无接收方明细
ALTER TABLE public.pay_alloc_order ADD COLUMN IF NOT EXISTS alloc_type character varying(32) DEFAULT 'alloc'::character varying NOT NULL;
COMMENT ON COLUMN public.pay_alloc_order.alloc_type IS '分账单类型(alloc-分账单 / finish-完结单, 完结单解冻剩余冻结资金给商户, 无接收方明细)';
-- 完结模式(auto-分账即完结 / manual-需显式完结), 仅 alloc 类型单有意义
ALTER TABLE public.pay_alloc_order ADD COLUMN IF NOT EXISTS finish_mode character varying(32) DEFAULT 'auto'::character varying NOT NULL;
COMMENT ON COLUMN public.pay_alloc_order.finish_mode IS '完结模式(auto 分账即完结/manual 需显式完结)';
-- 原支付已分账累计金额(分, 不含失败与完结单), 多次分账额度校验依据
ALTER TABLE public.pay_trade ADD COLUMN IF NOT EXISTS allocated_amount bigint;
COMMENT ON COLUMN public.pay_trade.allocated_amount IS '已分账累计金额(分, 不含失败与完结单)';

-- ========== 2026-10-31 支付宝/微信通道密钥配置加网关地址列(按通道主体覆盖 SDK 网关, 留空走官方) ==========
-- alipay serverUrl 须填完整网关 URL(SDK 原样 POST 不拼 path, 如 http://127.0.0.1:10400/gateway.do);
-- wechat apiHostUrl 填域名根(如 http://127.0.0.1:10400, WxJava 自动拼 /v3/** 路径);
-- 幂等写法, 全新安装库执行无副作用(全新安装由全量 tables.sql 建表)
ALTER TABLE public.alipay_direct_app_key_config ADD COLUMN IF NOT EXISTS server_url character varying(512);
ALTER TABLE public.alipay_isv_app_key_config ADD COLUMN IF NOT EXISTS server_url character varying(512);
ALTER TABLE public.wechat_direct_key_config ADD COLUMN IF NOT EXISTS api_host_url character varying(512);
ALTER TABLE public.wechat_isv_key_config ADD COLUMN IF NOT EXISTS api_host_url character varying(512);
COMMENT ON COLUMN public.alipay_direct_app_key_config.server_url IS '网关地址(留空使用官方网关; 联调演练填模拟网关完整地址, 如 http://127.0.0.1:10400/gateway.do)';
COMMENT ON COLUMN public.alipay_isv_app_key_config.server_url IS '网关地址(留空使用官方网关; 联调演练填模拟网关完整地址, 如 http://127.0.0.1:10400/gateway.do)';
COMMENT ON COLUMN public.wechat_direct_key_config.api_host_url IS 'API域名(留空使用官方 api.mch.weixin.qq.com; 联调演练填模拟网关地址, 如 http://127.0.0.1:10400)';
COMMENT ON COLUMN public.wechat_isv_key_config.api_host_url IS 'API域名(留空使用官方 api.mch.weixin.qq.com; 联调演练填模拟网关地址, 如 http://127.0.0.1:10400)';

-- ========== 2026-10-31 门店默认支付应用(码牌等门店级场景的应用归属来源, 空则回落商户默认应用) ==========
-- 门店级默认应用: 码牌未指定应用且显式门店时按门店带出 appId, 空则后端回落商户默认应用;
-- 幂等写法, 全新安装库执行无副作用(全新安装由全量 tables.sql 建表)
ALTER TABLE public.mch_store_info ADD COLUMN IF NOT EXISTS app_id character varying(32);
COMMENT ON COLUMN public.mch_store_info.app_id IS '门店默认支付应用号(空则使用商户默认应用)';

-- ========== 2026-10-31 主体注销治理批A: 唯一索引补软删过滤(软删行不再占用唯一键, 同业务键注销后重建放行) ==========
-- 口径移植自 plus 8100363e; open 全量 table.sql 此前已完成大部分唯一索引 partial 化, 本段仅补齐剩余 6 个全行唯一索引;
-- plus 侧 agent 域 8 索引 / 限额 / 风控配置 / 投诉 / 支付分 / 预授权 / 应收账单 / 租户能力 / 文件名等表 open 不存在, 不在本段范围;
-- 幂等写法: DROP IF EXISTS + CREATE, 重复执行无副作用。
DROP INDEX IF EXISTS public.uk_device_qr_code_code;
CREATE UNIQUE INDEX uk_device_qr_code_code ON public.device_qr_code USING btree (code) WHERE (deleted = false);
COMMENT ON INDEX public.uk_device_qr_code_code IS '二维码设备编码唯一(仅未删除行, 注销后同码重建放行)';
DROP INDEX IF EXISTS public.uk_mch_app_info_app_id;
CREATE UNIQUE INDEX uk_mch_app_info_app_id ON public.mch_app_info USING btree (app_id) WHERE (deleted = false);
COMMENT ON INDEX public.uk_mch_app_info_app_id IS '应用号唯一索引(仅未删除行, 商户注销级联软删后同号重建放行; appId 骨架行保留可追溯)';
DROP INDEX IF EXISTS public.uk_mch_store_info_store_no;
CREATE UNIQUE INDEX uk_mch_store_info_store_no ON public.mch_store_info USING btree (store_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_mch_store_info_store_no IS '门店号唯一(仅未删除行, 商户注销级联软删后同号重建放行)';
DROP INDEX IF EXISTS public.uk_pay_alloc_order_alloc_no;
CREATE UNIQUE INDEX uk_pay_alloc_order_alloc_no ON public.pay_alloc_order USING btree (alloc_no) WHERE (deleted = false);
COMMENT ON INDEX public.uk_pay_alloc_order_alloc_no IS '平台分账单号唯一约束(仅未删除行)';
DROP INDEX IF EXISTS public.uk_pay_blacklist_type_value_app;
CREATE UNIQUE INDEX uk_pay_blacklist_type_value_app ON public.pay_blacklist USING btree (type, value, COALESCE(wx_app_id, ''::character varying)) WHERE (deleted = false);
COMMENT ON INDEX public.uk_pay_blacklist_type_value_app IS '类型+值+微信应用维度唯一约束（微信应用为空按空串参与唯一性; 仅未删除行）';
DROP INDEX IF EXISTS public.uk_user_das_pref_user_client;
CREATE UNIQUE INDEX uk_user_das_pref_user_client ON public.iam_user_dashboard_preference USING btree (user_id, client_code, terminal) WHERE (deleted = false);
COMMENT ON INDEX public.uk_user_das_pref_user_client IS '同一用户同一身份域终端同一壳仪表盘偏好唯一(仅未删除行, 用户注销后重建同配置放行)';

-- ========== 2026-10-31 主体注销治理批A: 23 张表 deleted 列补 NOT NULL(裸 SQL 写 NULL 会让该行对逻辑删查询永久隐身) ==========
-- 口径移植自 plus 8100363e 的 28 表清单 ∩ open 实际存在表; plus 侧 mch_agent_transfer_record / mch_limit_rule / pay_md_channel_onb /
-- pay_risk_alert / yeepay_isv_channel_merchant 五表 open 不存在, 不在本段范围;
-- 幂等写法: 先刷 NULL 为 false 再收紧约束, 重复执行无副作用。
UPDATE public.adapay_direct_key_config SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.adapay_direct_key_config ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.adapay_direct_key_config ALTER COLUMN deleted SET NOT NULL;
UPDATE public.alipay_direct_app_capability SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.alipay_direct_app_capability ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.alipay_direct_app_capability ALTER COLUMN deleted SET NOT NULL;
UPDATE public.hmpay_isv_channel_merchant SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.hmpay_isv_channel_merchant ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.hmpay_isv_channel_merchant ALTER COLUMN deleted SET NOT NULL;
UPDATE public.iam_perm_code SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.iam_perm_code ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.iam_perm_code ALTER COLUMN deleted SET NOT NULL;
UPDATE public.iam_perm_menu SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.iam_perm_menu ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.iam_perm_menu ALTER COLUMN deleted SET NOT NULL;
UPDATE public.iam_role SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.iam_role ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.iam_role ALTER COLUMN deleted SET NOT NULL;
UPDATE public.iam_social_login_config SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.iam_social_login_config ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.iam_social_login_config ALTER COLUMN deleted SET NOT NULL;
UPDATE public.iam_user_expand_info SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.iam_user_expand_info ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.iam_user_expand_info ALTER COLUMN deleted SET NOT NULL;
UPDATE public.iam_user_info SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.iam_user_info ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.iam_user_info ALTER COLUMN deleted SET NOT NULL;
UPDATE public.iam_user_password_security SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.iam_user_password_security ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.iam_user_password_security ALTER COLUMN deleted SET NOT NULL;
UPDATE public.mch_app_info SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.mch_app_info ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.mch_app_info ALTER COLUMN deleted SET NOT NULL;
UPDATE public.mch_credential SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.mch_credential ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.mch_credential ALTER COLUMN deleted SET NOT NULL;
UPDATE public.pay_blacklist SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.pay_blacklist ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.pay_blacklist ALTER COLUMN deleted SET NOT NULL;
UPDATE public.pay_easy_pay_refund_order SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.pay_easy_pay_refund_order ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.pay_easy_pay_refund_order ALTER COLUMN deleted SET NOT NULL;
UPDATE public.pay_md_channel SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.pay_md_channel ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.pay_md_channel ALTER COLUMN deleted SET NOT NULL;
UPDATE public.pay_md_product SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.pay_md_product ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.pay_md_product ALTER COLUMN deleted SET NOT NULL;
UPDATE public.starter_audit_login_log SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.starter_audit_login_log ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.starter_audit_login_log ALTER COLUMN deleted SET NOT NULL;
UPDATE public.starter_audit_operate_log SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.starter_audit_operate_log ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.starter_audit_operate_log ALTER COLUMN deleted SET NOT NULL;
UPDATE public.starter_platform_file_record SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.starter_platform_file_record ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.starter_platform_file_record ALTER COLUMN deleted SET NOT NULL;
UPDATE public.system_dict SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.system_dict ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.system_dict ALTER COLUMN deleted SET NOT NULL;
UPDATE public.system_dict_item SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.system_dict_item ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.system_dict_item ALTER COLUMN deleted SET NOT NULL;
UPDATE public.system_platform_encrypt_config SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.system_platform_encrypt_config ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.system_platform_encrypt_config ALTER COLUMN deleted SET NOT NULL;
UPDATE public.vbill_isv_channel_merchant SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.vbill_isv_channel_merchant ALTER COLUMN deleted SET DEFAULT false;
ALTER TABLE public.vbill_isv_channel_merchant ALTER COLUMN deleted SET NOT NULL;

-- ========== 2026-10-31 主体注销治理批A: mch_user 补 user_id 唯一索引(一人一商户, 物理删表无 deleted 列故全行唯一) ==========
-- 与商户删除的用户级联注销构成双保险: 级联删挂靠行后按 userId 复查为空才委托 iam 注销, 唯一索引从数据库层兜底同一用户不可能挂靠两个商户;
-- 执行前先查重(有结果须先人工清理再建唯一索引):
-- SELECT user_id, count(*) FROM public.mch_user GROUP BY user_id HAVING count(*) > 1
CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_user_user_id ON public.mch_user USING btree (user_id);
COMMENT ON INDEX public.uk_mch_user_user_id IS '同一用户仅归属一个商户(跨商户挂靠被数据库拒绝)';

-- ========== 2026-10-31 数据库结构治理(批次 A/C/D): 主键/业务唯一索引/类型统一/死列与冗余清理 ==========
-- 审计背景: 30 张表无主键(id 无唯一锚点, 重复行可静默写入); 通道密钥/ISV 表仅 hkrt_isv_channel_merchant 缺业务唯一键;
-- 139 个关联键列宽不足(雪花/64 位长单号存在截断风险); 易支付两表金额以 numeric(元) 存储, 与全库 bigint(分) 口径不一致;
-- 另清理 20 个死列、4 个前缀冗余索引、2 张表 deleted 可空、11 处残留 nextval 默认值与序列、审计日志 create_time 未回填,
-- 并修正 IAM 种子(补 bootx→role1 用户角色绑定、清 4 条悬挂角色菜单)。
-- 本段是存量部署升级用的幂等增量; 全新安装由全量 table.sql 直接建出治理后结构(定版时随全量脚本重导出收口)。
-- 幂等口径: 主键判 pg_index.indisprimary; 索引 CREATE ... IF NOT EXISTS + COMMENT(COMMENT 可重复覆盖);
-- 类型扩宽与金额换算均包 DO 块并判断当前列定义(重复执行不会二次乘 100); 删索引/删列/删序列一律 IF EXISTS;
-- 回填先刷 NULL 再 SET NOT NULL。实库预检(id 查重/候选唯一键查重)已在治理执行阶段完成, 本段不含 SELECT 预检语句。

-- ---------- A1. 补主键(30 张无主键表; id 列已预检无重复, 已有主键则跳过) ----------
DO $$
DECLARE
  t text;
BEGIN
  FOREACH t IN ARRAY ARRAY[
    'baofu_direct_key_config',
    'cpcn_direct_key_config',
    'duolabao_direct_key_config',
    'epaylinks_direct_key_config',
    'fubei_direct_key_config',
    'fuyou_direct_key_config',
    'fuyou_rsa_key_config',
    'helipay_key_config',
    'hkrt_isv_channel_merchant',
    'hkrt_isv_key_config',
    'jee_pay_key_config',
    'jlpay_direct_key_config',
    'kuaiqian_direct_key_config',
    'llianpay_direct_key_config',
    'long_pay_direct_key_config',
    'notify_mail_record',
    'sand_direct_key_config',
    'shouqianba_direct_key_config',
    'starter_audit_login_log',
    'starter_audit_operate_log',
    'system_dict',
    'system_dict_item',
    'system_platform_config',
    'system_platform_encrypt_config',
    'umfpay_direct_key_config',
    'xingyi_direct_key_config',
    'yisheng_direct_key_config',
    'yizhifubj_direct_key_config',
    'youzan_direct_key_config',
    'ysep_key_config'
  ] LOOP
    IF NOT EXISTS (
      SELECT 1
      FROM pg_index i
      JOIN pg_class c ON c.oid = i.indrelid
      JOIN pg_namespace n ON n.oid = c.relnamespace
      WHERE n.nspname = 'public'
        AND c.relname = t
        AND i.indisprimary
    ) THEN
      EXECUTE format('ALTER TABLE public.%I ADD CONSTRAINT %I PRIMARY KEY (id)', t, 'pk_' || t);
    END IF;
  END LOOP;
END $$;

-- ---------- A2. 通道密钥/ISV 表：仅 hkrt_isv_channel_merchant 新增业务唯一索引 ----------
-- 通道密钥/ISV 表：除 hkrt_isv_channel_merchant 外，其余表在既有全量 schema 中已有等价或更严格的唯一索引
-- （如 uk_*_cmchno_sandbox / uk_*_key_channel_mch），本批只补主键，不重复建索引；
-- 仅 hkrt_isv_channel_merchant 新增业务唯一索引。
-- 该索引带 deleted = false 软删过滤(同业务键注销后重建放行); 存量库若已有重复 (channel_mch_no, product, sandbox) 需先清理重复行再执行。
CREATE UNIQUE INDEX IF NOT EXISTS uk_hkrt_isv_channel_merchant_channel_product_sandbox ON public.hkrt_isv_channel_merchant USING btree (channel_mch_no, product, sandbox) WHERE deleted = false;
COMMENT ON INDEX public.uk_hkrt_isv_channel_merchant_channel_product_sandbox IS '业务唯一键(未删除行): channel_mch_no, product, sandbox';

-- ---------- A3. 配置/字典/通道商户/审计日志/订单表补索引(14 个) ----------
-- 配置/字典
CREATE UNIQUE INDEX IF NOT EXISTS uk_system_platform_config_type ON public.system_platform_config USING btree (config_type) WHERE deleted = false;
COMMENT ON INDEX public.uk_system_platform_config_type IS '配置类型唯一(未删除行), 配置取数走 config_type 等值查询';

CREATE UNIQUE INDEX IF NOT EXISTS uk_system_platform_encrypt_config_type ON public.system_platform_encrypt_config USING btree (config_type) WHERE deleted = false;
COMMENT ON INDEX public.uk_system_platform_encrypt_config_type IS '加密配置类型唯一(未删除行)';

CREATE UNIQUE INDEX IF NOT EXISTS uk_system_dict_type_code ON public.system_dict USING btree (dict_type, code) WHERE deleted = false;
COMMENT ON INDEX public.uk_system_dict_type_code IS '字典类型+编码唯一(未删除行)';

CREATE INDEX IF NOT EXISTS idx_system_dict_item_dict_id ON public.system_dict_item USING btree (dict_id);
COMMENT ON INDEX public.idx_system_dict_item_dict_id IS '字典项按字典ID检索';

CREATE UNIQUE INDEX IF NOT EXISTS uk_system_dict_item_dict_code ON public.system_dict_item USING btree (dict_id, code) WHERE deleted = false;
COMMENT ON INDEX public.uk_system_dict_item_dict_code IS '同字典下编码唯一(未删除行)';

-- 通道商户(下单路由主链路)
CREATE INDEX IF NOT EXISTS idx_mch_channel_merchant_mch_no ON public.mch_channel_merchant USING btree (mch_no);
COMMENT ON INDEX public.idx_mch_channel_merchant_mch_no IS '通道商户按商户号检索';

CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_channel_merchant_mch_channel ON public.mch_channel_merchant USING btree (mch_no, channel_mch_no) WHERE deleted = false;
COMMENT ON INDEX public.uk_mch_channel_merchant_mch_channel IS '商户下通道商户号唯一(未删除行), 下单路由主链路';

-- 审计日志
CREATE INDEX IF NOT EXISTS idx_audit_login_log_user_time ON public.starter_audit_login_log USING btree (user_id, login_time);
COMMENT ON INDEX public.idx_audit_login_log_user_time IS '登录日志按用户+登录时间检索';

CREATE INDEX IF NOT EXISTS idx_audit_operate_log_operate_time ON public.starter_audit_operate_log USING btree (operate_id, operate_time);
COMMENT ON INDEX public.idx_audit_operate_log_operate_time IS '操作日志按操作人+操作时间检索';

-- 订单表时间维(商户 + 创建时间)
CREATE INDEX IF NOT EXISTS idx_normal_order_mch_create_time ON public.pay_normal_order USING btree (mch_no, create_time);
COMMENT ON INDEX public.idx_normal_order_mch_create_time IS '商户维度按创建时间检索';

CREATE INDEX IF NOT EXISTS idx_gateway_order_mch_create_time ON public.pay_gateway_order USING btree (mch_no, create_time);
COMMENT ON INDEX public.idx_gateway_order_mch_create_time IS '商户维度按创建时间检索';

CREATE INDEX IF NOT EXISTS idx_pay_alloc_order_mch_create_time ON public.pay_alloc_order USING btree (mch_no, create_time);
COMMENT ON INDEX public.idx_pay_alloc_order_mch_create_time IS '商户维度按创建时间检索';

CREATE INDEX IF NOT EXISTS idx_pay_trade_mch_create_time ON public.pay_trade USING btree (mch_no, create_time);
COMMENT ON INDEX public.idx_pay_trade_mch_create_time IS '商户维度按创建时间检索';

CREATE INDEX IF NOT EXISTS idx_refund_order_mch_create_time ON public.pay_refund_order USING btree (mch_no, create_time);
COMMENT ON INDEX public.idx_refund_order_mch_create_time IS '商户维度按创建时间检索';

-- ---------- A4. 清理前缀冗余索引(4 个; 单列索引是同表既有复合索引的最左前缀, 保留复合索引即可) ----------
DROP INDEX IF EXISTS public.idx_dy_mch_app_mch_no;
DROP INDEX IF EXISTS public.idx_wx_mch_app_mch_no;
DROP INDEX IF EXISTS public.uk_lakala_isv_key_product;
DROP INDEX IF EXISTS public.uk_ums_direct_key_cmchno;

-- ---------- A5. deleted 补 NOT NULL(2 张表; 裸 SQL 写 NULL 会让该行对逻辑删查询永久隐身, 默认值已为 false) ----------
UPDATE public.hmpay_isv_key_config SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.hmpay_isv_key_config ALTER COLUMN deleted SET NOT NULL;

UPDATE public.vbill_isv_key_config SET deleted = false WHERE deleted IS NULL;
ALTER TABLE public.vbill_isv_key_config ALTER COLUMN deleted SET NOT NULL;

-- ---------- A6. 清理残留 nextval 默认值与序列(11 处; id 由应用侧雪花算法赋值, 不再用库序列) ----------
ALTER TABLE public.adapay_direct_key_config ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.adapay_direct_key_config_id_seq;

ALTER TABLE public.alipay_direct_app_capability ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.alipay_direct_app_capability_id_seq;

ALTER TABLE public.dougong_isv_channel_merchant ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.dougong_isv_channel_merchant_id_seq;

ALTER TABLE public.dougong_isv_key_config ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.dougong_isv_key_config_id_seq;

ALTER TABLE public.easy_pay_key_config ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.easy_pay_key_config_id_seq;

ALTER TABLE public.hmpay_isv_channel_merchant ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.hmpay_isv_channel_merchant_id_seq;

ALTER TABLE public.hmpay_isv_key_config ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.hmpay_isv_key_config_id_seq;

ALTER TABLE public.mch_app_notify_config ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.mch_app_notify_config_id_seq;

ALTER TABLE public.mch_user ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.mch_user_id_seq;

ALTER TABLE public.pay_close_record ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.pay_close_record_id_seq;

ALTER TABLE public.pay_sync_record ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.pay_sync_record_id_seq;

-- ---------- A7. IAM 种子修正(补 bootx→role1 用户角色绑定, 清 4 条悬挂角色菜单) ----------
-- bootx 为 iam_user_info.id = 1 的超管, role1 为 iam_role.id = 1 的超级管理员角色。
-- 优先使用固定 id = 1, 与全量 data.sql 的 bootx 角色绑定行保持一致(便于 redact-data.mjs 白名单按 id 保留);
-- 目标库 id = 1 已被其他 (user_id, role_id) 占用时回退 max(id)+1, 避免主键冲突。
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM public.iam_user_role WHERE user_id = 1 AND role_id = 1) THEN
    BEGIN
      INSERT INTO public.iam_user_role (id, user_id, role_id) VALUES (1, 1, 1);
    EXCEPTION WHEN unique_violation THEN
      INSERT INTO public.iam_user_role (id, user_id, role_id)
      SELECT COALESCE(max(id), 0) + 1, 1, 1 FROM public.iam_user_role;
    END;
  END IF;
END $$;

-- 4 条悬挂角色菜单(菜单本体已不存在, 仅角色授权表残留), 按 menu_id 定点删除。
DELETE FROM public.iam_role_menu WHERE menu_id IN (91129, 91130, 91131, 91150);

-- ---------- C1. 关联键类型统一(139 列: mch_no/channel_mch_no/app_id → varchar(64), trade_no/biz_order_no → varchar(128)) ----------
-- PG 扩长 varchar 是元数据级操作, 不重写表、不重建索引; 仅当当前长度小于目标长度才执行, 重复执行无副作用。
-- 逐列判 information_schema.columns 的 character_maximum_length: 列或表不存在(老部署未建的表)、非字符类型则跳过不报错。
DO $$
DECLARE
  r record;
BEGIN
  FOR r IN
    SELECT * FROM (VALUES
      -- mch_no: 100 列
      ('douyin_transfer_config', 'mch_no', 64),
      ('fuyou_isv_channel_merchant', 'mch_no', 64),
      ('leshua_isv_channel_merchant', 'mch_no', 64),
      ('alipay_transfer_config', 'mch_no', 64),
      ('sheng_key_config', 'mch_no', 64),
      ('fubei_direct_key_config', 'mch_no', 64),
      ('mch_credential', 'mch_no', 64),
      ('sheng_isv_channel_merchant', 'mch_no', 64),
      ('dougong_isv_channel_merchant', 'mch_no', 64),
      ('llianpay_direct_key_config', 'mch_no', 64),
      ('mch_user', 'mch_no', 64),
      ('easy_pay_key_config', 'mch_no', 64),
      ('jlpay_direct_key_config', 'mch_no', 64),
      ('pay_virtual_device', 'mch_no', 64),
      ('pay_mch_app_device_config', 'mch_no', 64),
      ('mock_isv_channel_merchant', 'mch_no', 64),
      ('yeepay_direct_key_config', 'mch_no', 64),
      ('pay_risk_hit', 'mch_no', 64),
      ('wechat_direct_alloc_receiver', 'mch_no', 64),
      ('alipay_direct_alloc_receiver', 'mch_no', 64),
      ('pay_transfer_order_wechat', 'mch_no', 64),
      ('mch_notice_record', 'mch_no', 64),
      ('pay_callback_record', 'mch_no', 64),
      ('mch_notice_task', 'mch_no', 64),
      ('pay_transfer_order_alipay', 'mch_no', 64),
      ('pay_transfer_trade', 'mch_no', 64),
      ('wx_channel_app_capability', 'mch_no', 64),
      ('alipay_direct_app_capability', 'mch_no', 64),
      ('wx_mch_app', 'mch_no', 64),
      ('pay_sync_record', 'mch_no', 64),
      ('pay_close_record', 'mch_no', 64),
      ('epaylinks_direct_key_config', 'mch_no', 64),
      ('fuyou_direct_key_config', 'mch_no', 64),
      ('umfpay_direct_key_config', 'mch_no', 64),
      ('mock_alloc_receiver', 'mch_no', 64),
      ('pay_alloc_order', 'mch_no', 64),
      ('pay_trade', 'mch_no', 64),
      ('alipay_direct_app_key_config', 'mch_no', 64),
      ('wechat_direct_key_config', 'mch_no', 64),
      ('fuyou_rsa_key_config', 'mch_no', 64),
      ('dy_channel_app_capability', 'mch_no', 64),
      ('dy_mch_app', 'mch_no', 64),
      ('pay_easy_pay_credential', 'mch_no', 64),
      ('kuaiqian_direct_key_config', 'mch_no', 64),
      ('yisheng_direct_key_config', 'mch_no', 64),
      ('pay_easy_pay_order', 'mch_no', 64),
      ('pay_transfer_order_douyin', 'mch_no', 64),
      ('shouqianba_direct_key_config', 'mch_no', 64),
      ('cpcn_direct_key_config', 'mch_no', 64),
      ('wechat_transfer_config', 'mch_no', 64),
      ('alipay_transfer_scene_config', 'mch_no', 64),
      ('system_sensitive_word_hit', 'mch_no', 64),
      ('pay_gateway_pay_config', 'mch_no', 64),
      ('starter_audit_unipay_log', 'mch_no', 64),
      ('pay_gateway_pay_client_env', 'mch_no', 64),
      ('sand_direct_key_config', 'mch_no', 64),
      ('baofu_direct_key_config', 'mch_no', 64),
      ('pay_normal_order', 'mch_no', 64),
      ('pay_terminal_channel_bind', 'mch_no', 64),
      ('pay_channel_terminal', 'mch_no', 64),
      ('pay_terminal_device', 'mch_no', 64),
      ('pay_gateway_order', 'mch_no', 64),
      ('pay_refund_order', 'mch_no', 64),
      ('pay_route_strategy', 'mch_no', 64),
      ('device_qr_code', 'mch_no', 64),
      ('xingyi_direct_key_config', 'mch_no', 64),
      ('pay_gateway_cashier_item', 'mch_no', 64),
      ('pay_easy_pay_config', 'mch_no', 64),
      ('alipay_isv_channel_merchant', 'mch_no', 64),
      ('alipay_direct_channel_merchant', 'mch_no', 64),
      ('alipay_direct_app', 'mch_no', 64),
      ('ysep_key_config', 'mch_no', 64),
      ('duolabao_direct_key_config', 'mch_no', 64),
      ('wechat_isv_channel_merchant', 'mch_no', 64),
      ('wechat_direct_channel_merchant', 'mch_no', 64),
      ('pay_abnormal_order', 'mch_no', 64),
      ('pay_fund_flow', 'mch_no', 64),
      ('douyin_direct_key_config', 'mch_no', 64),
      ('wechat_isv_alloc_receiver', 'mch_no', 64),
      ('alipay_isv_alloc_receiver', 'mch_no', 64),
      ('douyin_direct_alloc_receiver', 'mch_no', 64),
      ('lakala_isv_channel_merchant', 'mch_no', 64),
      ('long_pay_direct_key_config', 'mch_no', 64),
      ('hmpay_isv_channel_merchant', 'mch_no', 64),
      ('ums_direct_key_config', 'mch_no', 64),
      ('douyin_direct_channel_merchant', 'mch_no', 64),
      ('mch_channel_merchant', 'mch_no', 64),
      ('mch_app_notify_config', 'mch_no', 64),
      ('mch_wx_domain_verify', 'mch_no', 64),
      ('jee_pay_key_config', 'mch_no', 64),
      ('youzan_direct_key_config', 'mch_no', 64),
      ('mch_store_info', 'mch_no', 64),
      ('mch_app_info', 'mch_no', 64),
      ('adapay_direct_key_config', 'mch_no', 64),
      ('mch_info', 'mch_no', 64),
      ('helipay_key_config', 'mch_no', 64),
      ('union_key_config', 'mch_no', 64),
      ('stripe_key_config', 'mch_no', 64),
      ('stripe_channel_merchant', 'mch_no', 64),
      ('yizhifubj_direct_key_config', 'mch_no', 64),
      -- channel_mch_no: 4 列
      ('pay_route_scene_config', 'channel_mch_no', 64),
      ('pay_route_basic_config', 'channel_mch_no', 64),
      ('pay_channel_terminal', 'channel_mch_no', 64),
      ('mch_channel_merchant', 'channel_mch_no', 64),
      -- app_id: 17 列
      ('pay_sync_record', 'app_id', 64),
      ('pay_close_record', 'app_id', 64),
      ('pay_alloc_order', 'app_id', 64),
      ('pay_trade', 'app_id', 64),
      ('pay_easy_pay_credential', 'app_id', 64),
      ('pay_easy_pay_order', 'app_id', 64),
      ('system_sensitive_word_hit', 'app_id', 64),
      ('pay_gateway_pay_config', 'app_id', 64),
      ('pay_normal_order', 'app_id', 64),
      ('pay_gateway_order', 'app_id', 64),
      ('pay_refund_order', 'app_id', 64),
      ('pay_route_strategy', 'app_id', 64),
      ('pay_gateway_cashier_item', 'app_id', 64),
      ('pay_easy_pay_config', 'app_id', 64),
      ('mch_app_notify_config', 'app_id', 64),
      ('mch_store_info', 'app_id', 64),
      ('mch_app_info', 'app_id', 64),
      -- trade_no: 12 列
      ('pay_risk_hit', 'trade_no', 128),
      ('pay_callback_record', 'trade_no', 128),
      ('pay_transfer_trade', 'trade_no', 128),
      ('pay_sync_record', 'trade_no', 128),
      ('pay_close_record', 'trade_no', 128),
      ('pay_easy_pay_refund_order', 'trade_no', 128),
      ('pay_alloc_order', 'trade_no', 128),
      ('pay_trade', 'trade_no', 128),
      ('pay_easy_pay_order', 'trade_no', 128),
      ('pay_refund_order', 'trade_no', 128),
      ('pay_abnormal_order', 'trade_no', 128),
      ('pay_fund_flow', 'trade_no', 128),
      -- biz_order_no: 6 列
      ('pay_alloc_order', 'biz_order_no', 128),
      ('pay_normal_order', 'biz_order_no', 128),
      ('pay_gateway_order', 'biz_order_no', 128),
      ('pay_refund_order', 'biz_order_no', 128),
      ('pay_abnormal_order', 'biz_order_no', 128),
      ('pay_fund_flow', 'biz_order_no', 128)
    ) AS v(tbl, col, len)
  LOOP
    IF EXISTS (
      SELECT 1
      FROM information_schema.columns c
      WHERE c.table_schema = 'public'
        AND c.table_name = r.tbl
        AND c.column_name = r.col
        AND c.data_type IN ('character varying', 'character')
        AND c.character_maximum_length < r.len
    ) THEN
      EXECUTE format('ALTER TABLE public.%I ALTER COLUMN %I TYPE character varying(%s)', r.tbl, r.col, r.len);
    END IF;
  END LOOP;
END $$;

-- ---------- C2. 易支付金额列 numeric(元) → bigint(分)(3 列; 列类型已是 bigint 则整段跳过, 重复执行不会再乘 100) ----------
DO $$
DECLARE
  r record;
BEGIN
  FOR r IN
    SELECT * FROM (VALUES
      ('pay_easy_pay_order', 'money'),
      ('pay_easy_pay_order', 'refund_money'),
      ('pay_easy_pay_refund_order', 'money')
    ) AS v(tbl, col)
  LOOP
    IF EXISTS (
      SELECT 1
      FROM information_schema.columns c
      WHERE c.table_schema = 'public'
        AND c.table_name = r.tbl
        AND c.column_name = r.col
        AND c.data_type = 'numeric'
    ) THEN
      EXECUTE format('ALTER TABLE public.%I ALTER COLUMN %I TYPE bigint USING (%I * 100)::bigint', r.tbl, r.col, r.col);
    END IF;
  END LOOP;
END $$;

COMMENT ON COLUMN public.pay_easy_pay_order.money IS '订单金额（分）';
COMMENT ON COLUMN public.pay_easy_pay_order.refund_money IS '已退款金额（分）';
COMMENT ON COLUMN public.pay_easy_pay_refund_order.money IS '退款金额(分)';

-- ---------- D1. 死列清理(20 个; 实体与 SQL 均已不再引用) ----------
-- 先删依赖索引(早期开发库残留的单列索引, 全量 table.sql 中不存在), 再删列。
-- 故意不加 CASCADE: 旧库若在这些列上有自定义索引/视图, 会立刻报错而不是静默级联删除, 便于人工确认后处理。
DROP INDEX IF EXISTS public.idx_role_menu_role_client;
ALTER TABLE public.iam_perm_menu DROP COLUMN IF EXISTS name;
ALTER TABLE public.iam_perm_menu DROP COLUMN IF EXISTS root;
ALTER TABLE public.iam_perm_menu DROP COLUMN IF EXISTS active_icon;
ALTER TABLE public.iam_role_menu DROP COLUMN IF EXISTS client_code;
ALTER TABLE public.pay_md_product DROP COLUMN IF EXISTS name;
ALTER TABLE public.pay_md_capability DROP COLUMN IF EXISTS enabled;
ALTER TABLE public.pay_md_product_capability DROP COLUMN IF EXISTS enabled;
ALTER TABLE public.pay_md_product_capability DROP COLUMN IF EXISTS remark;
ALTER TABLE public.pay_gateway_order DROP COLUMN IF EXISTS link_form;
ALTER TABLE public.mock_isv_credential DROP COLUMN IF EXISTS key_serial;
ALTER TABLE public.mock_direct_credential DROP COLUMN IF EXISTS key_serial;
ALTER TABLE public.alipay_direct_app_capability DROP COLUMN IF EXISTS update_time;
ALTER TABLE public.iam_role DROP COLUMN IF EXISTS data_scope;
ALTER TABLE public.hkrt_isv_key_config DROP COLUMN IF EXISTS wx_app_id;
ALTER TABLE public.hmpay_isv_channel_merchant DROP COLUMN IF EXISTS wx_app_id;
ALTER TABLE public.hmpay_isv_channel_merchant DROP COLUMN IF EXISTS wx_channel_auth;
ALTER TABLE public.douyin_direct_channel_merchant DROP COLUMN IF EXISTS transfer_scene;
ALTER TABLE public.pay_transfer_order_alipay DROP COLUMN IF EXISTS transfer_scene_config_id;
ALTER TABLE public.pay_transfer_order_douyin DROP COLUMN IF EXISTS user_recv_perception;
ALTER TABLE public.stripe_key_config DROP COLUMN IF EXISTS sandbox;

-- ---------- D2. 审计日志 create_time 回填(历史行 create_time 为空时用操作时间兜底, 已填充的行不动) ----------
UPDATE public.starter_audit_login_log SET create_time = login_time WHERE create_time IS NULL;
UPDATE public.starter_audit_operate_log SET create_time = operate_time WHERE create_time IS NULL;

-- ---------- D3. 虚拟设备两表基础列注释补齐(全量 schema 既有 7 个基础列, 此前漏 COMMENT) ----------
COMMENT ON COLUMN public.pay_virtual_device.id IS '主键';
COMMENT ON COLUMN public.pay_virtual_device.creator IS '创建者ID';
COMMENT ON COLUMN public.pay_virtual_device.create_time IS '创建时间';
COMMENT ON COLUMN public.pay_virtual_device.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.pay_virtual_device.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.pay_virtual_device.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.pay_virtual_device.deleted IS '删除标志';
COMMENT ON COLUMN public.pay_mch_app_device_config.id IS '主键';
COMMENT ON COLUMN public.pay_mch_app_device_config.creator IS '创建者ID';
COMMENT ON COLUMN public.pay_mch_app_device_config.create_time IS '创建时间';
COMMENT ON COLUMN public.pay_mch_app_device_config.last_modifier IS '最后修改者ID';
COMMENT ON COLUMN public.pay_mch_app_device_config.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN public.pay_mch_app_device_config.version IS '版本号(乐观锁)';
COMMENT ON COLUMN public.pay_mch_app_device_config.deleted IS '删除标志';

-- ---------- D4. 存量注释漂移订正(与全量 table.sql 口径对齐) ----------
-- query_sync: 全量库用分号分隔, 旧增量/旧全量用逗号; 统一为分号版本。
COMMENT ON COLUMN public.iam_perm_menu.query_sync IS 'query参数同步页(单页签模式): 开启后该路径所有query参数组合共用一个页签(页签key忽略query)。仅适用于页面内部自行响应query变化的参数定位页(如?tab=分组页/useQueryTab配套); 普通实体定位工作台(仅setup读一次query)开启会导致同页签切参数不刷新数据';
-- alloc_status: 旧注释漏 unsupported/allocated/finished, 统一为完整枚举口径。
COMMENT ON COLUMN public.pay_trade.alloc_status IS '分账状态(none-待分账/unsupported-不支持已降级/processing-分账中/allocated-已分账未完结/finished-已分账且完结/done-历史存量已分账)';
