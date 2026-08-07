-- ----------------------------
-- 数据库升级脚本(增量)
-- 按时间顺序追加, 已有列/表请勿重复执行
-- ----------------------------

-- 支付宝转账单新增转账场景标识列(支付宝=转账场景配置ID, FAIL重试时恢复场景用)
ALTER TABLE "public"."pay_transfer_order_alipay" ADD COLUMN "transfer_scene" varchar(50) COLLATE "pg_catalog"."default";
COMMENT ON COLUMN "public"."pay_transfer_order_alipay"."transfer_scene" IS '转账场景标识(支付宝=转账场景配置ID, FAIL重试时恢复场景用)';
