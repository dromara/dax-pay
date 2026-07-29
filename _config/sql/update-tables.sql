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
