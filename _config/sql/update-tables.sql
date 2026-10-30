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

