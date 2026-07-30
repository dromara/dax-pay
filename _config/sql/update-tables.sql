-- 微信通道旧应用体系下线: 删除已废弃的直连/服务商应用表
-- 应用身份已迁移至 wx_platform_app / wx_mch_app / wx_channel_app_capability 统一体系, 旧表数据直接弃用

DROP TABLE IF EXISTS "public"."wechat_direct_app";
DROP TABLE IF EXISTS "public"."wechat_direct_app_auth_config";
DROP TABLE IF EXISTS "public"."wechat_direct_app_capability";
DROP TABLE IF EXISTS "public"."wechat_isv_app";
DROP TABLE IF EXISTS "public"."wechat_isv_app_auth_config";
DROP TABLE IF EXISTS "public"."wechat_isv_app_capability";
DROP TABLE IF EXISTS "public"."wechat_isv_mch_app";
DROP TABLE IF EXISTS "public"."wechat_isv_mch_app_auth_config";
DROP TABLE IF EXISTS "public"."wechat_isv_mch_app_capability";

-- 微信服务商通道商户: 移除冗余的认证应用类型字段
-- 实际 sp/sub 路由由通道能力绑定(WxChannelAppCapability)决定, 该字段不被任何业务逻辑消费
ALTER TABLE "public"."wechat_isv_channel_merchant" DROP COLUMN IF EXISTS "auth_app_type";
