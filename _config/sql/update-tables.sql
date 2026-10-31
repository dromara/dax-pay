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
COMMENT ON TABLE public.pay_virtual_device IS '虚拟设备: 播报打印一体机(收款播报 APP)独立台账, 设备号平台自造号段(VD+日期+随机段), 生成入平台库存后绑定商户/应用';
COMMENT ON COLUMN public.pay_virtual_device.name IS '设备名称';
COMMENT ON COLUMN public.pay_virtual_device.device_no IS '设备号(平台自造 SN: VD+日期+6位随机段, 创建后不可改, 激活唯一准入凭据)';
COMMENT ON COLUMN public.pay_virtual_device.batch_no IS '批次号(批量生成时写入)';
COMMENT ON COLUMN public.pay_virtual_device.mch_no IS '所属商户号(绑定后写入, 空=平台库存, 未绑定不可激活)';
COMMENT ON COLUMN public.pay_virtual_device.app_id IS '关联应用号(绑定应用后有值, 空=全部应用)';
COMMENT ON COLUMN public.pay_virtual_device.store_no IS '使用门店号(空=未分配, 不参与门店级播报/打印匹配, 对应 mch_store_info.store_no)';
COMMENT ON COLUMN public.pay_virtual_device.enable IS '是否启用(停用后设备心跳返回 disabled, 终端停止播报)';
COMMENT ON COLUMN public.pay_virtual_device.secret IS '播报加密密钥(AES-256-GCM 32字符, 激活时生成并轮换, 解绑时清空即回到未激活态)';
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
    key_serial character varying(64),
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
COMMENT ON COLUMN public.mock_isv_credential.key_serial IS '密钥序列号(已废弃: 保留列兼容存量, 不再读写; 取钥改按主体编号 key_owner_no, 装配按两件套)';
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
    key_serial character varying(64),
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
COMMENT ON COLUMN public.mock_direct_credential.key_serial IS '密钥序列号(已废弃: 保留列兼容存量, 不再读写; 取钥改按主体编号 key_owner_no, 装配按两件套)';
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
