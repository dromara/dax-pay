-- 支付宝直连商户应用增加应用类型字段
ALTER TABLE "public"."alipay_direct_app"
  ADD COLUMN "app_type" varchar(32);
