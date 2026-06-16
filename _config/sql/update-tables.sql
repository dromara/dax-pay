-- 支付宝直连商户应用增加应用类型字段
ALTER TABLE "public"."alipay_direct_app"
  ADD COLUMN "app_type" varchar(32);

-- 抖音直连商户应用授权认证配置增加应用密钥字段
ALTER TABLE "public"."douyin_direct_app_auth_config"
  ADD COLUMN "app_secret" varchar(512);
