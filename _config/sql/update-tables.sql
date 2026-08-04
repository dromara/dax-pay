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
