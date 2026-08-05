-- ========================================
-- adapay_direct_key_config 直连密钥配置结构调整
-- 开发阶段: Adapay 商户身份由 app_id + apiKey + RSA 私钥承载
-- 无需独立商户号字段, 移除 merchant_no; 新增 sandbox 沙箱环境列与唯一索引
-- ========================================

-- 1. 删除 Adapay 商户号列(身份由 app_id 承载; 该列旧版仅存商户身份快照, 直接删除)
ALTER TABLE "public"."adapay_direct_key_config" DROP COLUMN IF EXISTS "merchant_no";

-- 2. 新增沙箱环境列(默认非沙箱, 双环境并存: 同一通道商户同一环境密钥唯一)
ALTER TABLE "public"."adapay_direct_key_config" ADD COLUMN IF NOT EXISTS "sandbox" bool DEFAULT false;
COMMENT ON COLUMN "public"."adapay_direct_key_config"."sandbox" IS '是否沙箱环境';

-- 3. 唯一索引: 同一通道商户同一环境密钥唯一(存量数据存在重复时建索引会失败, 需先清理)
CREATE UNIQUE INDEX IF NOT EXISTS "uk_adapay_direct_key_config_sandbox" ON "public"."adapay_direct_key_config" USING btree (
  "channel_mch_no" "pg_catalog"."text_ops" ASC NULLS LAST,
  "sandbox" "pg_catalog"."bool_ops" ASC NULLS LAST
) WHERE deleted = false;
COMMENT ON INDEX "public"."uk_adapay_direct_key_config_sandbox" IS '同一通道商户同一环境密钥唯一';

-- ===== pay_trade 增加支付通道冗余字段 =====
-- 冗余自容器 product→channel, 用于按接入通道维度看资金/报表免 JOIN; 与 pay_transfer_trade 口径对齐
ALTER TABLE "public"."pay_trade" ADD COLUMN "channel" varchar(32) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."pay_trade"."channel" IS '支付通道(冗余自容器 product→channel; 创建即终值; 权威在容器 channel)';

-- 存量数据回填: 从业务容器反向填充 channel
-- 普通支付容器(normal)
UPDATE "public"."pay_trade" t
SET "channel" = c."channel"
FROM "public"."pay_normal_order" c
WHERE t."container_id" = c."id"
  AND t."trade_type" = 'normal'
  AND t."channel" IS NULL;

-- 网关支付容器(gateway)
UPDATE "public"."pay_trade" t
SET "channel" = c."channel"
FROM "public"."pay_gateway_order" c
WHERE t."container_id" = c."id"
  AND t."trade_type" = 'gateway'
  AND t."channel" IS NULL;
