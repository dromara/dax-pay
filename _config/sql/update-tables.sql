-- ----------------------------
-- 分账接收方绑定(通道侧注册) 5 张表
-- 微信直连/服务商、支付宝直连/服务商、抖音直连, 各通道字段流程不一致故独立建表
-- ----------------------------
DROP TABLE IF EXISTS "public"."alipay_direct_alloc_receiver";
CREATE TABLE "public"."alipay_direct_alloc_receiver" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_account" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "account_hash" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_name" varchar(256) COLLATE "pg_catalog"."default",
  "direct_app_ref_id" int8,
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "error_msg" text COLLATE "pg_catalog"."default",
  "bind_time" timestamptz(6),
  "unbind_time" timestamptz(6),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."channel_mch_no" IS '通道商户号(关联通用通道商户主表)';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."receiver_type" IS '接收方类型(USER_ID用户号/LOGIN_NAME登录账号)';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."receiver_account" IS '接收方账号(AES-256-GCM加密存储)';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."account_hash" IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."receiver_name" IS '接收方名称(AES-256-GCM加密存储, 可空)';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."direct_app_ref_id" IS '发起绑定的支付宝应用引用(alipay_direct_app主键, 重新绑定复用)';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."status" IS '绑定状态(bound已绑定/unbound已解绑/fail绑定失败)';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."error_msg" IS '最近一次绑定/解绑失败原因';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."bind_time" IS '绑定成功时间';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."unbind_time" IS '解绑成功时间';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_direct_alloc_receiver"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."alipay_direct_alloc_receiver" IS '支付宝直连分账接收方(通道侧绑定档案)';
CREATE UNIQUE INDEX "uk_alipay_direct_alloc_receiver" ON "public"."alipay_direct_alloc_receiver" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "receiver_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "account_hash" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_alipay_direct_alloc_receiver" IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';
ALTER TABLE "public"."alipay_direct_alloc_receiver" ADD CONSTRAINT "pk_alipay_direct_alloc_receiver" PRIMARY KEY ("id");

DROP TABLE IF EXISTS "public"."alipay_isv_alloc_receiver";
CREATE TABLE "public"."alipay_isv_alloc_receiver" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_account" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "account_hash" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_name" varchar(256) COLLATE "pg_catalog"."default",
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "error_msg" text COLLATE "pg_catalog"."default",
  "bind_time" timestamptz(6),
  "unbind_time" timestamptz(6),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."id" IS '主键';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."channel_mch_no" IS '通道商户号(关联通用通道商户主表)';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."receiver_type" IS '接收方类型(USER_ID用户号/LOGIN_NAME登录账号)';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."receiver_account" IS '接收方账号(AES-256-GCM加密存储)';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."account_hash" IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."receiver_name" IS '接收方名称(AES-256-GCM加密存储, 可空)';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."status" IS '绑定状态(bound已绑定/unbound已解绑/fail绑定失败)';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."error_msg" IS '最近一次绑定/解绑失败原因';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."bind_time" IS '绑定成功时间';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."unbind_time" IS '解绑成功时间';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."creator" IS '创建人';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."alipay_isv_alloc_receiver"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."alipay_isv_alloc_receiver" IS '支付宝服务商分账接收方(通道侧绑定档案, 凭证由子商户授权绑定自动决定)';
CREATE UNIQUE INDEX "uk_alipay_isv_alloc_receiver" ON "public"."alipay_isv_alloc_receiver" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "receiver_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "account_hash" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_alipay_isv_alloc_receiver" IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';
ALTER TABLE "public"."alipay_isv_alloc_receiver" ADD CONSTRAINT "pk_alipay_isv_alloc_receiver" PRIMARY KEY ("id");

DROP TABLE IF EXISTS "public"."douyin_direct_alloc_receiver";
CREATE TABLE "public"."douyin_direct_alloc_receiver" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_account" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "account_hash" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_name" varchar(256) COLLATE "pg_catalog"."default",
  "relation_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "custom_relation" varchar(64) COLLATE "pg_catalog"."default",
  "channel_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "error_msg" text COLLATE "pg_catalog"."default",
  "bind_time" timestamptz(6),
  "unbind_time" timestamptz(6),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."id" IS '主键';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."channel_mch_no" IS '通道商户号(关联通用通道商户主表)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."receiver_type" IS '接收方类型(MERCHANT_ID商户号/PERSONAL_OPENID个人openid)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."receiver_account" IS '接收方账号(AES-256-GCM加密存储)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."account_hash" IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."receiver_name" IS '接收方名称(AES-256-GCM加密存储, MERCHANT_ID时必填商户全称)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."relation_type" IS '分账关系类型(抖音原生大写, CUSTOM时需custom_relation)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."custom_relation" IS '自定义分账关系名(relation_type=CUSTOM时必填)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."channel_app_id" IS '绑定时所用商户档抖音应用appid(重新绑定复用)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."status" IS '绑定状态(bound已绑定/unbound已解绑/fail绑定失败)';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."error_msg" IS '最近一次绑定/解绑失败原因';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."bind_time" IS '绑定成功时间';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."unbind_time" IS '解绑成功时间';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."creator" IS '创建人';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."douyin_direct_alloc_receiver"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."douyin_direct_alloc_receiver" IS '抖音直连分账接收方(通道侧绑定档案)';
CREATE UNIQUE INDEX "uk_douyin_direct_alloc_receiver" ON "public"."douyin_direct_alloc_receiver" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "receiver_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "account_hash" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_douyin_direct_alloc_receiver" IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';
ALTER TABLE "public"."douyin_direct_alloc_receiver" ADD CONSTRAINT "pk_douyin_direct_alloc_receiver" PRIMARY KEY ("id");

DROP TABLE IF EXISTS "public"."wechat_direct_alloc_receiver";
CREATE TABLE "public"."wechat_direct_alloc_receiver" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_account" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "account_hash" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_name" varchar(256) COLLATE "pg_catalog"."default",
  "relation_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "custom_relation" varchar(64) COLLATE "pg_catalog"."default",
  "channel_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "error_msg" text COLLATE "pg_catalog"."default",
  "bind_time" timestamptz(6),
  "unbind_time" timestamptz(6),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."channel_mch_no" IS '通道商户号(关联通用通道商户主表)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."receiver_type" IS '接收方类型(MERCHANT_ID商户号/PERSONAL_OPENID个人openid)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."receiver_account" IS '接收方账号(AES-256-GCM加密存储, openid为channel_app_id维度)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."account_hash" IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."receiver_name" IS '接收方名称(AES-256-GCM加密存储, MERCHANT_ID时必填商户全称)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."relation_type" IS '分账关系类型(微信原生小写映射, CUSTOM时需custom_relation)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."custom_relation" IS '自定义分账关系名(relation_type=CUSTOM时必填)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."channel_app_id" IS '绑定时所用商户档微信应用appid(重新绑定复用)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."status" IS '绑定状态(bound已绑定/unbound已解绑/fail绑定失败)';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."error_msg" IS '最近一次绑定/解绑失败原因';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."bind_time" IS '绑定成功时间';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."unbind_time" IS '解绑成功时间';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_direct_alloc_receiver"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."wechat_direct_alloc_receiver" IS '微信直连分账接收方(通道侧绑定档案)';
CREATE UNIQUE INDEX "uk_wechat_direct_alloc_receiver" ON "public"."wechat_direct_alloc_receiver" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "receiver_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "account_hash" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_wechat_direct_alloc_receiver" IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';
ALTER TABLE "public"."wechat_direct_alloc_receiver" ADD CONSTRAINT "pk_wechat_direct_alloc_receiver" PRIMARY KEY ("id");

DROP TABLE IF EXISTS "public"."wechat_isv_alloc_receiver";
CREATE TABLE "public"."wechat_isv_alloc_receiver" (
  "id" int8 NOT NULL,
  "mch_no" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "channel_mch_no" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_account" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "account_hash" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "receiver_name" varchar(256) COLLATE "pg_catalog"."default",
  "relation_type" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "custom_relation" varchar(64) COLLATE "pg_catalog"."default",
  "sp_app_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "sub_app_id" varchar(64) COLLATE "pg_catalog"."default",
  "status" varchar(16) COLLATE "pg_catalog"."default" NOT NULL,
  "error_msg" text COLLATE "pg_catalog"."default",
  "bind_time" timestamptz(6),
  "unbind_time" timestamptz(6),
  "creator" int8,
  "create_time" timestamptz(6),
  "last_modifier" int8,
  "last_modified_time" timestamptz(6),
  "version" int4 NOT NULL DEFAULT 0,
  "deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."id" IS '主键';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."mch_no" IS '商户号';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."channel_mch_no" IS '通道商户号(关联通用通道商户主表)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."receiver_type" IS '接收方类型(MERCHANT_ID商户号/PERSONAL_OPENID个人openid/PERSONAL_SUB_OPENID子商户应用openid)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."receiver_account" IS '接收方账号(AES-256-GCM加密存储, openid为对应appid维度)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."account_hash" IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."receiver_name" IS '接收方名称(AES-256-GCM加密存储, MERCHANT_ID时必填商户全称)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."relation_type" IS '分账关系类型(微信原生小写映射, CUSTOM时需custom_relation)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."custom_relation" IS '自定义分账关系名(relation_type=CUSTOM时必填)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."sp_app_id" IS '绑定时所用平台档(服务商)应用appid(重新绑定复用)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."sub_app_id" IS '子商户应用appid(可空, PERSONAL_SUB_OPENID时必填)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."status" IS '绑定状态(bound已绑定/unbound已解绑/fail绑定失败)';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."error_msg" IS '最近一次绑定/解绑失败原因';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."bind_time" IS '绑定成功时间';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."unbind_time" IS '解绑成功时间';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."creator" IS '创建人';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."last_modifier" IS '最后修改人';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."last_modified_time" IS '最后修改时间';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."version" IS '乐观锁版本号';
COMMENT ON COLUMN "public"."wechat_isv_alloc_receiver"."deleted" IS '逻辑删除标志';
COMMENT ON TABLE "public"."wechat_isv_alloc_receiver" IS '微信服务商分账接收方(通道侧绑定档案, 挂特约商户sub_mchid维度)';
CREATE UNIQUE INDEX "uk_wechat_isv_alloc_receiver" ON "public"."wechat_isv_alloc_receiver" USING btree (
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "receiver_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "account_hash" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_wechat_isv_alloc_receiver" IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';
ALTER TABLE "public"."wechat_isv_alloc_receiver" ADD CONSTRAINT "pk_wechat_isv_alloc_receiver" PRIMARY KEY ("id");

-- ----------------------------
-- 支付码牌新增分账标识
-- ----------------------------
ALTER TABLE "public"."device_qr_code" ADD COLUMN "allocation" bool NOT NULL DEFAULT false;
COMMENT ON COLUMN "public"."device_qr_code"."allocation" IS '是否分账码牌(开启后扫码支付向下单链路透传分账标识; 产品不支持分账时自动降级普通收款, 交易分账状态记为 unsupported)';

-- ------------------------------------------------------------
-- 修复 pay_trade.alloc_status 默认值污染(幂等)
-- 2026-08-18: 历史增量脚本曾以 DEFAULT 'none' 加列, 导致普通(非分账)订单也落 'none'(待分账);
-- 语义修正: null=非分账订单, 仅下单声明 allocation=true 的交易才初始化 none/unsupported, 不设列默认值
-- ------------------------------------------------------------
ALTER TABLE "public"."pay_trade" ALTER COLUMN "alloc_status" DROP DEFAULT;
COMMENT ON COLUMN "public"."pay_trade"."alloc_status" IS '分账状态(null-非分账订单/none-待分账/unsupported-不支持分账/processing-分账中/done-已分账)';
-- 存量回填: 容器未声明分账的 trade 置回 null(真实分账单保留, 按 trade_type 关联对应容器判定)
UPDATE "public"."pay_trade" t SET alloc_status = NULL
WHERE t.alloc_status = 'none'
  AND NOT (
    (t.trade_type = 'gateway' AND EXISTS (SELECT 1 FROM pay_gateway_order g
        WHERE g.id = t.container_id AND g.allocation = true))
    OR (t.trade_type = 'normal' AND EXISTS (SELECT 1 FROM pay_normal_order n
        WHERE n.id = t.container_id AND n.allocation = true))
  );

-- ------------------------------------------------------------
-- 支付宝应用表补唯一索引(2026-08-18)
-- 此前 alipay_direct_app.ali_app_id 与 alipay_isv_app.ali_app_id 均无唯一约束,
-- 同一应用可重复建档, 接收方绑定记录 directAppRefId 指向歧义; 现补唯一索引与业务层查重对齐,
-- 并对齐微信/抖音应用表的唯一约束设计(商户档 per 商户唯一 / 平台档全局唯一)。
-- 前置条件: 存量数据无重复(按索引列分组 count>1 需先人工清理)。
-- ------------------------------------------------------------
CREATE UNIQUE INDEX "uk_alipay_direct_app_mch_channel_appid" ON "public"."alipay_direct_app" USING btree (
  "mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "channel_mch_no" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "ali_app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_alipay_direct_app_mch_channel_appid" IS '同一商户同一通道商户下支付宝应用ID唯一(与业务层查重作用域对齐, 对齐微信/抖音商户应用唯一约束)';

CREATE UNIQUE INDEX "uk_alipay_isv_app_appid" ON "public"."alipay_isv_app" USING btree (
  "ali_app_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_alipay_isv_app_appid" IS '支付宝服务商应用ID全局唯一(与业务层查重作用域对齐, 对齐微信/抖音平台应用唯一约束)';
