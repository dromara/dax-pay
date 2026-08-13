-- ============================================================
-- 升级表结构脚本（增量，按版本追加）
-- 适用于已有存量数据的线上环境；全新安装走 tables.sql → datas.sql
-- ============================================================

-- ------------------------------------------------------------
-- 分账(Allocation)功能 - 数据表变更
-- 说明: 分账订单主表(pay_alloc_order) + 分账明细表(pay_alloc_detail, 每接收方一行)
-- 2026-08-10 重建: 原文件被清空且 git 无提交历史, 本区段由远程库
-- (192.168.1.229 daxpay-dev) pg_dump --schema-only 导出整理, 幂等可重复执行
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pay_alloc_detail (
    id bigint NOT NULL,
    alloc_no varchar(100) NOT NULL,
    receiver_type varchar(32) NOT NULL,
    receiver_account varchar(256) NOT NULL,
    receiver_name varchar(256),
    amount bigint NOT NULL,
    result varchar(32) DEFAULT 'pending' NOT NULL,
    out_detail_id varchar(150),
    error_code varchar(64),
    error_msg varchar(500),
    finish_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);
COMMENT ON TABLE pay_alloc_detail IS '分账明细(每个接收方一行)';
COMMENT ON COLUMN pay_alloc_detail.id IS '主键';
COMMENT ON COLUMN pay_alloc_detail.alloc_no IS '分账单号(关联 pay_alloc_order.alloc_no)';
COMMENT ON COLUMN pay_alloc_detail.receiver_type IS '接收方类型(MERCHANT_ID/PERSONAL_OPENID/USER_ID/LOGIN_NAME 等, 按通道)';
COMMENT ON COLUMN pay_alloc_detail.receiver_account IS '接收方账号(AES-256-GCM 加密存储)';
COMMENT ON COLUMN pay_alloc_detail.receiver_name IS '接收方姓名(AES-256-GCM 加密存储, 可空)';
COMMENT ON COLUMN pay_alloc_detail.amount IS '分账金额(分)';
COMMENT ON COLUMN pay_alloc_detail.result IS '明细结果(pending/success/fail)';
COMMENT ON COLUMN pay_alloc_detail.out_detail_id IS '通道侧明细ID(同步/回调时回填)';
COMMENT ON COLUMN pay_alloc_detail.error_code IS '错误码';
COMMENT ON COLUMN pay_alloc_detail.error_msg IS '错误信息';
COMMENT ON COLUMN pay_alloc_detail.finish_time IS '明细完成时间';
COMMENT ON COLUMN pay_alloc_detail.creator IS '创建人';
COMMENT ON COLUMN pay_alloc_detail.create_time IS '创建时间';
COMMENT ON COLUMN pay_alloc_detail.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_alloc_detail.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_alloc_detail.version IS '乐观锁版本';
COMMENT ON COLUMN pay_alloc_detail.deleted IS '逻辑删除';
CREATE TABLE IF NOT EXISTS pay_alloc_order (
    id bigint NOT NULL,
    app_id varchar(32) NOT NULL,
    alloc_no varchar(100) NOT NULL,
    biz_alloc_no varchar(100) NOT NULL,
    title varchar(100),
    description varchar(500),
    trade_no varchar(100) NOT NULL,
    trade_type varchar(32),
    biz_order_no varchar(100),
    out_order_no varchar(150),
    out_alloc_no varchar(150),
    amount bigint NOT NULL,
    order_amount bigint,
    currency varchar(8) DEFAULT 'CNY' NOT NULL,
    status varchar(32) NOT NULL,
    finish_time timestamp(6) with time zone,
    channel varchar(32),
    provider varchar(32),
    product varchar(32),
    channel_mch_no varchar(64),
    capability varchar(64),
    channel_app_id varchar(128),
    notify_url varchar(200),
    attach varchar(500),
    client_ip varchar(64),
    error_code varchar(64),
    error_msg varchar(500),
    mch_no varchar(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);
COMMENT ON TABLE pay_alloc_order IS '分账订单';
COMMENT ON COLUMN pay_alloc_order.id IS '主键';
COMMENT ON COLUMN pay_alloc_order.app_id IS '应用号';
COMMENT ON COLUMN pay_alloc_order.alloc_no IS '分账单号(平台统一生成, 全局唯一)';
COMMENT ON COLUMN pay_alloc_order.biz_alloc_no IS '商户分账单号(商户传入, 商户侧唯一, 幂等键)';
COMMENT ON COLUMN pay_alloc_order.title IS '标题(继承自原支付容器)';
COMMENT ON COLUMN pay_alloc_order.description IS '分账描述';
COMMENT ON COLUMN pay_alloc_order.trade_no IS '原支付资金交易号(= pay_trade.trade_no)';
COMMENT ON COLUMN pay_alloc_order.trade_type IS '原支付交易形态(冗余自 pay_trade.trade_type)';
COMMENT ON COLUMN pay_alloc_order.biz_order_no IS '商户业务订单号(冗余, 便于查询)';
COMMENT ON COLUMN pay_alloc_order.out_order_no IS '通道支付订单号(冗余, 分账上送通道用)';
COMMENT ON COLUMN pay_alloc_order.out_alloc_no IS '通道分账单号(通道返回, 如支付宝 settle_no / 微信 transaction_id / 抖音 orderId)';
COMMENT ON COLUMN pay_alloc_order.amount IS '分账总金额(各接收方金额之和, 分)';
COMMENT ON COLUMN pay_alloc_order.order_amount IS '原订单总金额(冗余, 分)';
COMMENT ON COLUMN pay_alloc_order.currency IS '币种';
COMMENT ON COLUMN pay_alloc_order.status IS '分账状态(processing/success/partial/fail)';
COMMENT ON COLUMN pay_alloc_order.finish_time IS '分账完成时间';
COMMENT ON COLUMN pay_alloc_order.channel IS '支付通道';
COMMENT ON COLUMN pay_alloc_order.provider IS '支付渠道';
COMMENT ON COLUMN pay_alloc_order.product IS '支付产品编码(策略选型)';
COMMENT ON COLUMN pay_alloc_order.channel_mch_no IS '通道商户号(继承自原支付)';
COMMENT ON COLUMN pay_alloc_order.capability IS '支付能力编码(继承自原支付)';
COMMENT ON COLUMN pay_alloc_order.channel_app_id IS '通道应用 AppId 快照(继承自原支付)';
COMMENT ON COLUMN pay_alloc_order.notify_url IS '异步通知地址(出站商户通知用)';
COMMENT ON COLUMN pay_alloc_order.attach IS '商户附加参数(回调原样返回)';
COMMENT ON COLUMN pay_alloc_order.client_ip IS '客户端 IP';
COMMENT ON COLUMN pay_alloc_order.error_code IS '错误码';
COMMENT ON COLUMN pay_alloc_order.error_msg IS '错误信息';
COMMENT ON COLUMN pay_alloc_order.mch_no IS '商户号';
COMMENT ON COLUMN pay_alloc_order.creator IS '创建人';
COMMENT ON COLUMN pay_alloc_order.create_time IS '创建时间';
COMMENT ON COLUMN pay_alloc_order.last_modifier IS '最后修改人';
COMMENT ON COLUMN pay_alloc_order.last_modified_time IS '最后修改时间';
COMMENT ON COLUMN pay_alloc_order.version IS '乐观锁版本';
COMMENT ON COLUMN pay_alloc_order.deleted IS '逻辑删除';
ALTER TABLE ONLY pay_alloc_detail
    ADD CONSTRAINT pk_pay_alloc_detail PRIMARY KEY (id);
ALTER TABLE ONLY pay_alloc_order
    ADD CONSTRAINT pk_pay_alloc_order PRIMARY KEY (id);
CREATE INDEX IF NOT EXISTS idx_pay_alloc_detail_alloc_no ON pay_alloc_detail USING btree (alloc_no);
COMMENT ON INDEX idx_pay_alloc_detail_alloc_no IS '分账单号反查明细';
CREATE INDEX IF NOT EXISTS idx_pay_alloc_order_mch_biz ON pay_alloc_order USING btree (mch_no, biz_alloc_no);
COMMENT ON INDEX idx_pay_alloc_order_mch_biz IS '商户号+商户分账单号幂等查询';
CREATE INDEX IF NOT EXISTS idx_pay_alloc_order_status_time ON pay_alloc_order USING btree (status, create_time);
COMMENT ON INDEX idx_pay_alloc_order_status_time IS '定时同步扫描(状态+创建时间)';
CREATE INDEX IF NOT EXISTS idx_pay_alloc_order_trade_no ON pay_alloc_order USING btree (trade_no);
COMMENT ON INDEX idx_pay_alloc_order_trade_no IS '原支付交易号反查分账单';
CREATE UNIQUE INDEX IF NOT EXISTS uk_pay_alloc_order_alloc_no ON pay_alloc_order USING btree (alloc_no);
COMMENT ON INDEX uk_pay_alloc_order_alloc_no IS '平台分账单号唯一约束';

-- ------------------------------------------------------------
-- 分账容器标记(增量, 幂等)
-- 2026-08-11 网关支付接入分账: 网关容器保存是否分账标记(与普通支付容器对齐)
-- ------------------------------------------------------------
-- 网关支付容器增加分账标记(预下单声明, 支付时透传通道冻结资金)
ALTER TABLE pay_gateway_order ADD COLUMN IF NOT EXISTS allocation boolean;
COMMENT ON COLUMN pay_gateway_order.allocation IS '是否分账订单(预下单透传通道分账标识, true 表示资金冻结仅可分账拆分)';
-- 普通支付容器分账标记(存量库补列, 幂等)
ALTER TABLE pay_normal_order ADD COLUMN IF NOT EXISTS allocation boolean;
COMMENT ON COLUMN pay_normal_order.allocation IS '是否分账订单(下单时透传通道分账标识, true 表示资金冻结仅可分账拆分)';
