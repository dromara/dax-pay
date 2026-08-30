-- ============================================================
-- DaxPay 全量建表结构 | 导出时间 2026-08-30
-- 来源: daxpay-dev @ 192.168.1.229:5432 (PostgreSQL 16.14)
-- 命令: pg_dump --schema-only --clean --if-exists --no-owner --no-privileges
--       (客户端 pg_dump 18.6; 已剥离 \restrict/\unrestrict 元命令)
-- 说明: 2026-08-30 起由 Navicat 手动导出切换为 pg_dump 脚本化导出
--       全新安装顺序不变: table.sql -> data.sql
-- ============================================================

--
-- PostgreSQL database dump
--

\restrict cC0Gprlh4bJpKGGZ3It6dKhV5ijjjfbQlzlUxFh72p0RdAptxlwF5yaY78fTi33

-- Dumped from database version 16.14
-- Dumped by pg_dump version 18.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP INDEX IF EXISTS public.uk_yeepay_direct_key_cmchno_sandbox;
DROP INDEX IF EXISTS public.uk_wx_platform_app_wx_app_id;
DROP INDEX IF EXISTS public.uk_wx_platform_app_cap_product;
DROP INDEX IF EXISTS public.uk_wx_mch_app_mch_wx;
DROP INDEX IF EXISTS public.uk_wx_channel_app_cap;
DROP INDEX IF EXISTS public.uk_wechat_transfer_config;
DROP INDEX IF EXISTS public.uk_wechat_isv_alloc_receiver;
DROP INDEX IF EXISTS public.uk_wechat_direct_alloc_receiver;
DROP INDEX IF EXISTS public.uk_vbill_isv_key_prod_sandbox;
DROP INDEX IF EXISTS public.uk_user_protocol_version_published;
DROP INDEX IF EXISTS public.uk_user_das_pref_user_client;
DROP INDEX IF EXISTS public.uk_union_key_mch_sandbox;
DROP INDEX IF EXISTS public.uk_ums_direct_key_mch_sandbox;
DROP INDEX IF EXISTS public.uk_ums_direct_key_cmchno;
DROP INDEX IF EXISTS public.uk_system_sensitive_word_word;
DROP INDEX IF EXISTS public.uk_refund_order_refund_no;
DROP INDEX IF EXISTS public.uk_refund_order_mch_biz;
DROP INDEX IF EXISTS public.uk_pay_transfer_trade_no;
DROP INDEX IF EXISTS public.uk_pay_transfer_order_wechat_no;
DROP INDEX IF EXISTS public.uk_pay_transfer_order_douyin_no;
DROP INDEX IF EXISTS public.uk_pay_transfer_order_alipay_no;
DROP INDEX IF EXISTS public.uk_pay_trade_trade_no;
DROP INDEX IF EXISTS public.uk_pay_terminal_device_terminal_no;
DROP INDEX IF EXISTS public.uk_pay_terminal_channel_bind;
DROP INDEX IF EXISTS public.uk_pay_route_scene_config_method;
DROP INDEX IF EXISTS public.uk_pay_route_basic_config_provider;
DROP INDEX IF EXISTS public.uk_pay_platform_mobile_app_type_platform;
DROP INDEX IF EXISTS public.uk_pay_md_provider_method_pair;
DROP INDEX IF EXISTS public.uk_pay_md_provider_code;
DROP INDEX IF EXISTS public.uk_pay_md_product_config_product;
DROP INDEX IF EXISTS public.uk_pay_md_product_capability_pair;
DROP INDEX IF EXISTS public.uk_pay_md_method_code;
DROP INDEX IF EXISTS public.uk_pay_md_capability_code;
DROP INDEX IF EXISTS public.uk_pay_gateway_pay_env;
DROP INDEX IF EXISTS public.uk_pay_gateway_pay_config_app;
DROP INDEX IF EXISTS public.uk_pay_fund_flow_refund;
DROP INDEX IF EXISTS public.uk_pay_fund_flow_pay_trade;
DROP INDEX IF EXISTS public.uk_pay_blacklist_type_value_app;
DROP INDEX IF EXISTS public.uk_pay_alloc_order_mch_biz;
DROP INDEX IF EXISTS public.uk_pay_alloc_order_alloc_no;
DROP INDEX IF EXISTS public.uk_pay_abnormal_order_trade_pending;
DROP INDEX IF EXISTS public.uk_normal_order_order_no;
DROP INDEX IF EXISTS public.uk_normal_order_mch_biz;
DROP INDEX IF EXISTS public.uk_mch_wx_domain_verify_code;
DROP INDEX IF EXISTS public.uk_mch_store_info_store_no;
DROP INDEX IF EXISTS public.uk_mch_store_info_default;
DROP INDEX IF EXISTS public.uk_mch_risk_config_mch_no;
DROP INDEX IF EXISTS public.uk_mch_notice_task_biz;
DROP INDEX IF EXISTS public.uk_mch_app_notify_config;
DROP INDEX IF EXISTS public.uk_mch_app_info_default;
DROP INDEX IF EXISTS public.uk_mch_app_info_app_id;
DROP INDEX IF EXISTS public.uk_leshua_isv_mch_ls_no;
DROP INDEX IF EXISTS public.uk_leshua_isv_key_prod_sandbox;
DROP INDEX IF EXISTS public.uk_leshua_isv_channel_mch_no;
DROP INDEX IF EXISTS public.uk_lakala_isv_mch_lakala_no;
DROP INDEX IF EXISTS public.uk_lakala_isv_key_product;
DROP INDEX IF EXISTS public.uk_lakala_isv_key_prod_sandbox;
DROP INDEX IF EXISTS public.uk_lakala_isv_channel_mch_no;
DROP INDEX IF EXISTS public.uk_iam_user_two_factor_user_id;
DROP INDEX IF EXISTS public.uk_iam_user_social_source_open_id;
DROP INDEX IF EXISTS public.uk_iam_user_passkey_credential_id;
DROP INDEX IF EXISTS public.uk_iam_perm_code_code;
DROP INDEX IF EXISTS public.uk_hmpay_isv_key_prod_sandbox;
DROP INDEX IF EXISTS public.uk_hkrt_isv_key_prod_sandbox;
DROP INDEX IF EXISTS public.uk_gateway_order_order_no;
DROP INDEX IF EXISTS public.uk_gateway_order_mch_biz;
DROP INDEX IF EXISTS public.uk_fuyou_isv_mch_fuyou_no;
DROP INDEX IF EXISTS public.uk_fuyou_isv_key_prod_sandbox;
DROP INDEX IF EXISTS public.uk_fuyou_isv_channel_mch_no;
DROP INDEX IF EXISTS public.uk_easy_pay_order_app_out;
DROP INDEX IF EXISTS public.uk_easy_pay_credential_pid;
DROP INDEX IF EXISTS public.uk_easy_pay_credential_app_id;
DROP INDEX IF EXISTS public.uk_easy_pay_config_pid;
DROP INDEX IF EXISTS public.uk_easy_pay_config_app_id;
DROP INDEX IF EXISTS public.uk_dy_platform_app_dy_app_id;
DROP INDEX IF EXISTS public.uk_dy_platform_app_cap_product;
DROP INDEX IF EXISTS public.uk_dy_mch_app_mch_douyin;
DROP INDEX IF EXISTS public.uk_dy_channel_app_cap;
DROP INDEX IF EXISTS public.uk_douyin_transfer_config_mch;
DROP INDEX IF EXISTS public.uk_douyin_direct_mch_dyid;
DROP INDEX IF EXISTS public.uk_douyin_direct_key_cmchno;
DROP INDEX IF EXISTS public.uk_douyin_direct_alloc_receiver;
DROP INDEX IF EXISTS public.uk_device_qr_code_code;
DROP INDEX IF EXISTS public.uk_base_city_adjacent;
DROP INDEX IF EXISTS public.uk_alipay_transfer_scene_config_scene;
DROP INDEX IF EXISTS public.uk_alipay_transfer_scene_config_default;
DROP INDEX IF EXISTS public.uk_alipay_transfer_config_mch;
DROP INDEX IF EXISTS public.uk_alipay_isv_app_appid;
DROP INDEX IF EXISTS public.uk_alipay_isv_alloc_receiver;
DROP INDEX IF EXISTS public.uk_alipay_direct_app_mch_channel_appid;
DROP INDEX IF EXISTS public.uk_alipay_direct_app_key_sandbox;
DROP INDEX IF EXISTS public.uk_alipay_direct_app_cap;
DROP INDEX IF EXISTS public.uk_alipay_direct_alloc_receiver;
DROP INDEX IF EXISTS public.uk_adapay_direct_key_config_mch;
DROP INDEX IF EXISTS public.idx_wx_platform_app_app_type;
DROP INDEX IF EXISTS public.idx_wx_mch_app_mch_no;
DROP INDEX IF EXISTS public.idx_wx_mch_app_app_type;
DROP INDEX IF EXISTS public.idx_wx_channel_app_cap_ref;
DROP INDEX IF EXISTS public.idx_wx_channel_app_cap_mch;
DROP INDEX IF EXISTS public.idx_wechat_transfer_config_app;
DROP INDEX IF EXISTS public.idx_user_protocol_version_protocol;
DROP INDEX IF EXISTS public.idx_system_sensitive_word_status;
DROP INDEX IF EXISTS public.idx_system_sensitive_word_hit_word;
DROP INDEX IF EXISTS public.idx_system_sensitive_word_hit_time;
DROP INDEX IF EXISTS public.idx_system_sensitive_word_hit_scene;
DROP INDEX IF EXISTS public.idx_system_sensitive_word_hit_mch;
DROP INDEX IF EXISTS public.idx_starter_audit_unipay_log_trace;
DROP INDEX IF EXISTS public.idx_starter_audit_unipay_log_time;
DROP INDEX IF EXISTS public.idx_starter_audit_unipay_log_success_time;
DROP INDEX IF EXISTS public.idx_starter_audit_unipay_log_mch_time;
DROP INDEX IF EXISTS public.idx_role_menu_role_client;
DROP INDEX IF EXISTS public.idx_refund_order_status_create_time;
DROP INDEX IF EXISTS public.idx_refund_order_order_no;
DROP INDEX IF EXISTS public.idx_refund_order_mch_store;
DROP INDEX IF EXISTS public.idx_refund_order_app_biz;
DROP INDEX IF EXISTS public.idx_pay_transfer_trade_status_time;
DROP INDEX IF EXISTS public.idx_pay_transfer_trade_mch;
DROP INDEX IF EXISTS public.idx_pay_transfer_trade_container;
DROP INDEX IF EXISTS public.idx_pay_transfer_order_wechat_status_time;
DROP INDEX IF EXISTS public.idx_pay_transfer_order_wechat_mch;
DROP INDEX IF EXISTS public.idx_pay_transfer_order_douyin_status_time;
DROP INDEX IF EXISTS public.idx_pay_transfer_order_douyin_mch;
DROP INDEX IF EXISTS public.idx_pay_transfer_order_alipay_status_time;
DROP INDEX IF EXISTS public.idx_pay_transfer_order_alipay_mch;
DROP INDEX IF EXISTS public.idx_pay_trade_status_create_time;
DROP INDEX IF EXISTS public.idx_pay_trade_relation_order_no;
DROP INDEX IF EXISTS public.idx_pay_trade_out_order_no;
DROP INDEX IF EXISTS public.idx_pay_trade_mch_store;
DROP INDEX IF EXISTS public.idx_pay_trade_mch_channel;
DROP INDEX IF EXISTS public.idx_pay_trade_container;
DROP INDEX IF EXISTS public.idx_pay_terminal_device_store_no;
DROP INDEX IF EXISTS public.idx_pay_terminal_device_mch_no;
DROP INDEX IF EXISTS public.idx_pay_terminal_channel_bind_system;
DROP INDEX IF EXISTS public.idx_pay_terminal_channel_bind_mch;
DROP INDEX IF EXISTS public.idx_pay_terminal_channel_bind_channel;
DROP INDEX IF EXISTS public.idx_pay_risk_hit_trade_no;
DROP INDEX IF EXISTS public.idx_pay_risk_hit_phase;
DROP INDEX IF EXISTS public.idx_pay_risk_hit_hit_type_value;
DROP INDEX IF EXISTS public.idx_pay_risk_hit_create_time;
DROP INDEX IF EXISTS public.idx_pay_md_product_code;
DROP INDEX IF EXISTS public.idx_pay_gateway_pay_env_config_id;
DROP INDEX IF EXISTS public.idx_pay_fund_flow_mch_time;
DROP INDEX IF EXISTS public.idx_pay_fund_flow_create_time;
DROP INDEX IF EXISTS public.idx_pay_easy_pay_refund_order_refund_no;
DROP INDEX IF EXISTS public.idx_pay_easy_pay_refund_order_refund_id;
DROP INDEX IF EXISTS public.idx_pay_easy_pay_refund_order_out_trade_no;
DROP INDEX IF EXISTS public.idx_pay_channel_terminal_status;
DROP INDEX IF EXISTS public.idx_pay_channel_terminal_mch_no;
DROP INDEX IF EXISTS public.idx_pay_channel_terminal_channel_mch_no;
DROP INDEX IF EXISTS public.idx_pay_callback_record_trade_no;
DROP INDEX IF EXISTS public.idx_pay_callback_record_product;
DROP INDEX IF EXISTS public.idx_pay_callback_record_out_trade_no;
DROP INDEX IF EXISTS public.idx_pay_callback_record_mch_no;
DROP INDEX IF EXISTS public.idx_pay_callback_record_create_time;
DROP INDEX IF EXISTS public.idx_pay_callback_record_channel_mch_no;
DROP INDEX IF EXISTS public.idx_pay_blacklist_type_value;
DROP INDEX IF EXISTS public.idx_pay_blacklist_status;
DROP INDEX IF EXISTS public.idx_pay_alloc_order_trade_no;
DROP INDEX IF EXISTS public.idx_pay_alloc_order_status_time;
DROP INDEX IF EXISTS public.idx_pay_alloc_detail_alloc_no;
DROP INDEX IF EXISTS public.idx_pay_abnormal_order_create_time;
DROP INDEX IF EXISTS public.idx_password_history_user_id;
DROP INDEX IF EXISTS public.idx_notify_notice_status;
DROP INDEX IF EXISTS public.idx_notify_notice_read_user;
DROP INDEX IF EXISTS public.idx_notify_message_user;
DROP INDEX IF EXISTS public.idx_notify_mail_record_status;
DROP INDEX IF EXISTS public.idx_notify_mail_record_receiver_email;
DROP INDEX IF EXISTS public.idx_notify_mail_record_create_time;
DROP INDEX IF EXISTS public.idx_normal_order_mch_store;
DROP INDEX IF EXISTS public.idx_normal_order_app_biz;
DROP INDEX IF EXISTS public.idx_mch_wx_domain_verify_platform;
DROP INDEX IF EXISTS public.idx_mch_wx_domain_verify_mch_no;
DROP INDEX IF EXISTS public.idx_mch_store_info_mch_no;
DROP INDEX IF EXISTS public.idx_mch_store_info_mch_default;
DROP INDEX IF EXISTS public.idx_mch_notice_task_success;
DROP INDEX IF EXISTS public.idx_mch_notice_task_next_time;
DROP INDEX IF EXISTS public.idx_mch_notice_task_create_time;
DROP INDEX IF EXISTS public.idx_mch_notice_task_biz_no;
DROP INDEX IF EXISTS public.idx_mch_notice_record_task_id;
DROP INDEX IF EXISTS public.idx_mch_notice_record_create_time;
DROP INDEX IF EXISTS public.idx_mch_info_status;
DROP INDEX IF EXISTS public.idx_mch_info_mch_no;
DROP INDEX IF EXISTS public.idx_mch_credential_mch_no;
DROP INDEX IF EXISTS public.idx_mch_app_info_mch_no;
DROP INDEX IF EXISTS public.idx_iam_user_social_user_id;
DROP INDEX IF EXISTS public.idx_iam_user_role_user_id;
DROP INDEX IF EXISTS public.idx_iam_user_passkey_user_id;
DROP INDEX IF EXISTS public.idx_iam_user_info_client_phone;
DROP INDEX IF EXISTS public.idx_iam_user_info_client_email;
DROP INDEX IF EXISTS public.idx_iam_user_info_client_account;
DROP INDEX IF EXISTS public.idx_iam_role_code_role_id;
DROP INDEX IF EXISTS public.idx_gateway_order_mch_store;
DROP INDEX IF EXISTS public.idx_gateway_order_app_biz;
DROP INDEX IF EXISTS public.idx_gateway_cashier_item_mch;
DROP INDEX IF EXISTS public.idx_gateway_cashier_item_bucket;
DROP INDEX IF EXISTS public.idx_easy_pay_order_trade_no;
DROP INDEX IF EXISTS public.idx_easy_pay_order_pid_out;
DROP INDEX IF EXISTS public.idx_easy_pay_order_order_id;
DROP INDEX IF EXISTS public.idx_dy_platform_app_app_type;
DROP INDEX IF EXISTS public.idx_dy_mch_app_mch_no;
DROP INDEX IF EXISTS public.idx_dy_mch_app_app_type;
DROP INDEX IF EXISTS public.idx_dy_channel_app_cap_ref;
DROP INDEX IF EXISTS public.idx_dy_channel_app_cap_mch;
DROP INDEX IF EXISTS public.idx_douyin_transfer_config_mch;
DROP INDEX IF EXISTS public.idx_device_qr_code_store_no;
DROP INDEX IF EXISTS public.idx_base_user_protocol_type_client;
DROP INDEX IF EXISTS public.idx_base_street_area_code;
DROP INDEX IF EXISTS public.idx_alipay_transfer_scene_config_mch;
DROP INDEX IF EXISTS public.idx_alipay_transfer_config_channel;
ALTER TABLE IF EXISTS ONLY public.wx_platform_app DROP CONSTRAINT IF EXISTS wx_platform_app_pkey;
ALTER TABLE IF EXISTS ONLY public.wx_platform_app_capability DROP CONSTRAINT IF EXISTS wx_platform_app_capability_pkey;
ALTER TABLE IF EXISTS ONLY public.wx_mch_app DROP CONSTRAINT IF EXISTS wx_mch_app_pkey;
ALTER TABLE IF EXISTS ONLY public.wx_channel_app_capability DROP CONSTRAINT IF EXISTS wx_channel_app_capability_pkey;
ALTER TABLE IF EXISTS ONLY public.vbill_isv_key_config DROP CONSTRAINT IF EXISTS vbill_isv_key_config_pkey;
ALTER TABLE IF EXISTS ONLY public.vbill_isv_channel_merchant DROP CONSTRAINT IF EXISTS vbill_isv_channel_merchant_pkey;
ALTER TABLE IF EXISTS ONLY public.union_key_config DROP CONSTRAINT IF EXISTS union_key_config_pkey;
ALTER TABLE IF EXISTS ONLY public.ums_direct_key_config DROP CONSTRAINT IF EXISTS ums_direct_key_config_pkey;
ALTER TABLE IF EXISTS ONLY public.notify_notice_read DROP CONSTRAINT IF EXISTS uk_notify_notice_read;
ALTER TABLE IF EXISTS ONLY public.iam_social_login_config DROP CONSTRAINT IF EXISTS uk_iam_social_config_source;
ALTER TABLE IF EXISTS ONLY public.system_sensitive_word DROP CONSTRAINT IF EXISTS system_sensitive_word_pkey;
ALTER TABLE IF EXISTS ONLY public.system_sensitive_word_hit DROP CONSTRAINT IF EXISTS system_sensitive_word_hit_pkey;
ALTER TABLE IF EXISTS ONLY public.starter_platform_file_record DROP CONSTRAINT IF EXISTS starter_platform_file_record_pkey;
ALTER TABLE IF EXISTS ONLY public.starter_audit_unipay_log DROP CONSTRAINT IF EXISTS starter_audit_unipay_log_pkey;
ALTER TABLE IF EXISTS ONLY public.yeepay_direct_key_config DROP CONSTRAINT IF EXISTS pk_yeepay_direct_key_config;
ALTER TABLE IF EXISTS ONLY public.wechat_transfer_config DROP CONSTRAINT IF EXISTS pk_wechat_transfer_config;
ALTER TABLE IF EXISTS ONLY public.wechat_isv_key_config DROP CONSTRAINT IF EXISTS pk_wechat_isv_key_config;
ALTER TABLE IF EXISTS ONLY public.wechat_isv_alloc_receiver DROP CONSTRAINT IF EXISTS pk_wechat_isv_alloc_receiver;
ALTER TABLE IF EXISTS ONLY public.wechat_direct_key_config DROP CONSTRAINT IF EXISTS pk_wechat_direct_key_config;
ALTER TABLE IF EXISTS ONLY public.wechat_direct_alloc_receiver DROP CONSTRAINT IF EXISTS pk_wechat_direct_alloc_receiver;
ALTER TABLE IF EXISTS ONLY public.stripe_key_config DROP CONSTRAINT IF EXISTS pk_stripe_direct_key_config;
ALTER TABLE IF EXISTS ONLY public.stripe_channel_merchant DROP CONSTRAINT IF EXISTS pk_stripe_direct_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.pay_transfer_trade DROP CONSTRAINT IF EXISTS pk_pay_transfer_trade;
ALTER TABLE IF EXISTS ONLY public.pay_transfer_order_wechat DROP CONSTRAINT IF EXISTS pk_pay_transfer_order_wechat;
ALTER TABLE IF EXISTS ONLY public.pay_transfer_order_douyin DROP CONSTRAINT IF EXISTS pk_pay_transfer_order_douyin;
ALTER TABLE IF EXISTS ONLY public.pay_transfer_order_alipay DROP CONSTRAINT IF EXISTS pk_pay_transfer_order_alipay;
ALTER TABLE IF EXISTS ONLY public.pay_trade DROP CONSTRAINT IF EXISTS pk_pay_trade;
ALTER TABLE IF EXISTS ONLY public.pay_route_strategy DROP CONSTRAINT IF EXISTS pk_pay_route_strategy;
ALTER TABLE IF EXISTS ONLY public.pay_refund_order DROP CONSTRAINT IF EXISTS pk_pay_refund_order;
ALTER TABLE IF EXISTS ONLY public.pay_normal_order DROP CONSTRAINT IF EXISTS pk_pay_normal_order;
ALTER TABLE IF EXISTS ONLY public.pay_md_product_config DROP CONSTRAINT IF EXISTS pk_pay_md_product_config;
ALTER TABLE IF EXISTS ONLY public.pay_gateway_order DROP CONSTRAINT IF EXISTS pk_pay_gateway_order;
ALTER TABLE IF EXISTS ONLY public.pay_gateway_cashier_item DROP CONSTRAINT IF EXISTS pk_pay_gateway_cashier_item;
ALTER TABLE IF EXISTS ONLY public.pay_fund_flow DROP CONSTRAINT IF EXISTS pk_pay_fund_flow;
ALTER TABLE IF EXISTS ONLY public.pay_easy_pay_order DROP CONSTRAINT IF EXISTS pk_pay_easy_pay_order;
ALTER TABLE IF EXISTS ONLY public.pay_easy_pay_credential DROP CONSTRAINT IF EXISTS pk_pay_easy_pay_credential;
ALTER TABLE IF EXISTS ONLY public.pay_easy_pay_config DROP CONSTRAINT IF EXISTS pk_pay_easy_pay_config;
ALTER TABLE IF EXISTS ONLY public.pay_alloc_order DROP CONSTRAINT IF EXISTS pk_pay_alloc_order;
ALTER TABLE IF EXISTS ONLY public.pay_alloc_detail DROP CONSTRAINT IF EXISTS pk_pay_alloc_detail;
ALTER TABLE IF EXISTS ONLY public.pay_abnormal_order DROP CONSTRAINT IF EXISTS pk_pay_abnormal_order;
ALTER TABLE IF EXISTS ONLY public.mch_wx_domain_verify DROP CONSTRAINT IF EXISTS pk_mch_wx_domain_verify;
ALTER TABLE IF EXISTS ONLY public.wechat_isv_channel_merchant DROP CONSTRAINT IF EXISTS pk_mch_wechat_isv_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.wechat_direct_channel_merchant DROP CONSTRAINT IF EXISTS pk_mch_wechat_direct_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.mch_channel_merchant DROP CONSTRAINT IF EXISTS pk_mch_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.mch_app_notify_config DROP CONSTRAINT IF EXISTS pk_mch_app_notify_config;
ALTER TABLE IF EXISTS ONLY public.leshua_isv_key_config DROP CONSTRAINT IF EXISTS pk_leshua_isv_key_config;
ALTER TABLE IF EXISTS ONLY public.leshua_isv_channel_merchant DROP CONSTRAINT IF EXISTS pk_leshua_isv_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.iam_user_social DROP CONSTRAINT IF EXISTS pk_iam_user_social;
ALTER TABLE IF EXISTS ONLY public.fuyou_isv_key_config DROP CONSTRAINT IF EXISTS pk_fuyou_isv_key_config;
ALTER TABLE IF EXISTS ONLY public.fuyou_isv_channel_merchant DROP CONSTRAINT IF EXISTS pk_fuyou_isv_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.douyin_transfer_config DROP CONSTRAINT IF EXISTS pk_douyin_transfer_config;
ALTER TABLE IF EXISTS ONLY public.douyin_direct_key_config DROP CONSTRAINT IF EXISTS pk_douyin_direct_key_config;
ALTER TABLE IF EXISTS ONLY public.douyin_direct_channel_merchant DROP CONSTRAINT IF EXISTS pk_douyin_direct_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.douyin_direct_alloc_receiver DROP CONSTRAINT IF EXISTS pk_douyin_direct_alloc_receiver;
ALTER TABLE IF EXISTS ONLY public.alipay_transfer_scene_config DROP CONSTRAINT IF EXISTS pk_alipay_transfer_scene_config;
ALTER TABLE IF EXISTS ONLY public.alipay_transfer_config DROP CONSTRAINT IF EXISTS pk_alipay_transfer_config;
ALTER TABLE IF EXISTS ONLY public.alipay_isv_channel_merchant DROP CONSTRAINT IF EXISTS pk_alipay_isv_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.alipay_isv_app_key_config DROP CONSTRAINT IF EXISTS pk_alipay_isv_app_key_config;
ALTER TABLE IF EXISTS ONLY public.alipay_isv_app_auth_config DROP CONSTRAINT IF EXISTS pk_alipay_isv_app_auth_config;
ALTER TABLE IF EXISTS ONLY public.alipay_isv_app DROP CONSTRAINT IF EXISTS pk_alipay_isv_app;
ALTER TABLE IF EXISTS ONLY public.alipay_isv_alloc_receiver DROP CONSTRAINT IF EXISTS pk_alipay_isv_alloc_receiver;
ALTER TABLE IF EXISTS ONLY public.alipay_direct_channel_merchant DROP CONSTRAINT IF EXISTS pk_alipay_direct_channel_merchant;
ALTER TABLE IF EXISTS ONLY public.alipay_direct_app_key_config DROP CONSTRAINT IF EXISTS pk_alipay_direct_app_key_config;
ALTER TABLE IF EXISTS ONLY public.alipay_direct_app_auth_config DROP CONSTRAINT IF EXISTS pk_alipay_direct_app_auth_config;
ALTER TABLE IF EXISTS ONLY public.alipay_direct_app DROP CONSTRAINT IF EXISTS pk_alipay_direct_app;
ALTER TABLE IF EXISTS ONLY public.alipay_direct_alloc_receiver DROP CONSTRAINT IF EXISTS pk_alipay_direct_alloc_receiver;
ALTER TABLE IF EXISTS ONLY public.pay_terminal_device DROP CONSTRAINT IF EXISTS pay_terminal_device_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_terminal_channel_bind DROP CONSTRAINT IF EXISTS pay_terminal_channel_bind_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_sync_record DROP CONSTRAINT IF EXISTS pay_sync_record_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_route_scene_config DROP CONSTRAINT IF EXISTS pay_route_scene_config_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_route_basic_config DROP CONSTRAINT IF EXISTS pay_route_basic_config_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_risk_hit DROP CONSTRAINT IF EXISTS pay_risk_hit_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_platform_mobile_app DROP CONSTRAINT IF EXISTS pay_platform_mobile_app_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_md_provider DROP CONSTRAINT IF EXISTS pay_md_provider_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_md_provider_method DROP CONSTRAINT IF EXISTS pay_md_provider_method_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_md_product DROP CONSTRAINT IF EXISTS pay_md_product_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_md_product_capability DROP CONSTRAINT IF EXISTS pay_md_product_capability_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_md_method DROP CONSTRAINT IF EXISTS pay_md_method_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_md_channel DROP CONSTRAINT IF EXISTS pay_md_channel_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_md_capability DROP CONSTRAINT IF EXISTS pay_md_capability_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_gateway_pay_config DROP CONSTRAINT IF EXISTS pay_gateway_pay_config_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_gateway_pay_client_env DROP CONSTRAINT IF EXISTS pay_gateway_pay_client_env_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_easy_pay_refund_order DROP CONSTRAINT IF EXISTS pay_easy_pay_refund_order_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_close_record DROP CONSTRAINT IF EXISTS pay_close_record_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_channel_terminal DROP CONSTRAINT IF EXISTS pay_channel_terminal_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_callback_record DROP CONSTRAINT IF EXISTS pay_callback_record_pkey;
ALTER TABLE IF EXISTS ONLY public.pay_blacklist DROP CONSTRAINT IF EXISTS pay_blacklist_pkey;
ALTER TABLE IF EXISTS ONLY public.notify_notice_read DROP CONSTRAINT IF EXISTS notify_notice_read_pkey;
ALTER TABLE IF EXISTS ONLY public.notify_notice DROP CONSTRAINT IF EXISTS notify_notice_pkey;
ALTER TABLE IF EXISTS ONLY public.notify_message DROP CONSTRAINT IF EXISTS notify_message_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_user DROP CONSTRAINT IF EXISTS mch_user_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_user DROP CONSTRAINT IF EXISTS mch_user_mch_no_user_id_key;
ALTER TABLE IF EXISTS ONLY public.mch_store_info DROP CONSTRAINT IF EXISTS mch_store_info_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_risk_config DROP CONSTRAINT IF EXISTS mch_risk_config_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_notice_task DROP CONSTRAINT IF EXISTS mch_notice_task_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_notice_record DROP CONSTRAINT IF EXISTS mch_notice_record_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_info DROP CONSTRAINT IF EXISTS mch_info_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_credential DROP CONSTRAINT IF EXISTS mch_credential_pkey;
ALTER TABLE IF EXISTS ONLY public.mch_app_info DROP CONSTRAINT IF EXISTS mch_app_info_pkey;
ALTER TABLE IF EXISTS ONLY public.lakala_isv_key_config DROP CONSTRAINT IF EXISTS lakala_isv_key_config_pkey;
ALTER TABLE IF EXISTS ONLY public.lakala_isv_channel_merchant DROP CONSTRAINT IF EXISTS lakala_isv_channel_merchant_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_two_factor DROP CONSTRAINT IF EXISTS iam_user_two_factor_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_role DROP CONSTRAINT IF EXISTS iam_user_role_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_password_security DROP CONSTRAINT IF EXISTS iam_user_password_security_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_password_history DROP CONSTRAINT IF EXISTS iam_user_password_history_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_passkey DROP CONSTRAINT IF EXISTS iam_user_passkey_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_info DROP CONSTRAINT IF EXISTS iam_user_info_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_expand_info DROP CONSTRAINT IF EXISTS iam_user_expand_info_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_user_dashboard_preference DROP CONSTRAINT IF EXISTS iam_user_dashboard_preference_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_social_login_config DROP CONSTRAINT IF EXISTS iam_social_config_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_role DROP CONSTRAINT IF EXISTS iam_role_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_role_menu DROP CONSTRAINT IF EXISTS iam_role_menu_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_role_code DROP CONSTRAINT IF EXISTS iam_role_code_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_perm_menu DROP CONSTRAINT IF EXISTS iam_perm_menu_pkey;
ALTER TABLE IF EXISTS ONLY public.iam_perm_code DROP CONSTRAINT IF EXISTS iam_perm_code_pkey;
ALTER TABLE IF EXISTS ONLY public.hmpay_isv_key_config DROP CONSTRAINT IF EXISTS hmpay_isv_key_config_pkey;
ALTER TABLE IF EXISTS ONLY public.hmpay_isv_channel_merchant DROP CONSTRAINT IF EXISTS hmpay_isv_channel_merchant_pkey;
ALTER TABLE IF EXISTS ONLY public.dy_platform_app DROP CONSTRAINT IF EXISTS dy_platform_app_pkey;
ALTER TABLE IF EXISTS ONLY public.dy_platform_app_capability DROP CONSTRAINT IF EXISTS dy_platform_app_capability_pkey;
ALTER TABLE IF EXISTS ONLY public.dy_mch_app DROP CONSTRAINT IF EXISTS dy_mch_app_pkey;
ALTER TABLE IF EXISTS ONLY public.dy_channel_app_capability DROP CONSTRAINT IF EXISTS dy_channel_app_capability_pkey;
ALTER TABLE IF EXISTS ONLY public.device_qr_code DROP CONSTRAINT IF EXISTS device_qr_code_pkey;
ALTER TABLE IF EXISTS ONLY public.base_user_protocol_version DROP CONSTRAINT IF EXISTS base_user_protocol_version_pkey;
ALTER TABLE IF EXISTS ONLY public.base_user_protocol DROP CONSTRAINT IF EXISTS base_user_protocol_pkey;
ALTER TABLE IF EXISTS ONLY public.base_street DROP CONSTRAINT IF EXISTS base_street_pkey;
ALTER TABLE IF EXISTS ONLY public.base_province DROP CONSTRAINT IF EXISTS base_province_pkey;
ALTER TABLE IF EXISTS ONLY public.base_city DROP CONSTRAINT IF EXISTS base_city_pkey;
ALTER TABLE IF EXISTS ONLY public.base_city_adjacent DROP CONSTRAINT IF EXISTS base_city_adjacent_pkey;
ALTER TABLE IF EXISTS ONLY public.base_area DROP CONSTRAINT IF EXISTS base_area_pkey;
ALTER TABLE IF EXISTS ONLY public.alipay_direct_app_capability DROP CONSTRAINT IF EXISTS alipay_direct_app_capability_pkey;
ALTER TABLE IF EXISTS ONLY public.adapay_direct_key_config DROP CONSTRAINT IF EXISTS adapay_direct_key_config_pkey;
ALTER TABLE IF EXISTS public.pay_sync_record ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.pay_close_record ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.mch_user ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.mch_app_notify_config ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.hmpay_isv_key_config ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.hmpay_isv_channel_merchant ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.base_city_adjacent ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.alipay_direct_app_capability ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.adapay_direct_key_config ALTER COLUMN id DROP DEFAULT;
DROP TABLE IF EXISTS public.yeepay_direct_key_config;
DROP TABLE IF EXISTS public.wx_platform_app_capability;
DROP TABLE IF EXISTS public.wx_platform_app;
DROP TABLE IF EXISTS public.wx_mch_app;
DROP TABLE IF EXISTS public.wx_channel_app_capability;
DROP TABLE IF EXISTS public.wechat_transfer_config;
DROP TABLE IF EXISTS public.wechat_isv_key_config;
DROP TABLE IF EXISTS public.wechat_isv_channel_merchant;
DROP TABLE IF EXISTS public.wechat_isv_alloc_receiver;
DROP TABLE IF EXISTS public.wechat_direct_key_config;
DROP TABLE IF EXISTS public.wechat_direct_channel_merchant;
DROP TABLE IF EXISTS public.wechat_direct_alloc_receiver;
DROP TABLE IF EXISTS public.vbill_isv_key_config;
DROP TABLE IF EXISTS public.vbill_isv_channel_merchant;
DROP TABLE IF EXISTS public.union_key_config;
DROP TABLE IF EXISTS public.ums_direct_key_config;
DROP TABLE IF EXISTS public.system_sensitive_word_hit;
DROP TABLE IF EXISTS public.system_sensitive_word;
DROP TABLE IF EXISTS public.system_platform_encrypt_config;
DROP TABLE IF EXISTS public.system_platform_config;
DROP TABLE IF EXISTS public.system_dict_item;
DROP TABLE IF EXISTS public.system_dict;
DROP TABLE IF EXISTS public.stripe_key_config;
DROP TABLE IF EXISTS public.stripe_channel_merchant;
DROP TABLE IF EXISTS public.starter_platform_file_record;
DROP TABLE IF EXISTS public.starter_audit_unipay_log;
DROP TABLE IF EXISTS public.starter_audit_operate_log;
DROP TABLE IF EXISTS public.starter_audit_login_log;
DROP TABLE IF EXISTS public.pay_transfer_trade;
DROP TABLE IF EXISTS public.pay_transfer_order_wechat;
DROP TABLE IF EXISTS public.pay_transfer_order_douyin;
DROP TABLE IF EXISTS public.pay_transfer_order_alipay;
DROP TABLE IF EXISTS public.pay_trade;
DROP TABLE IF EXISTS public.pay_terminal_device;
DROP TABLE IF EXISTS public.pay_terminal_channel_bind;
DROP SEQUENCE IF EXISTS public.pay_sync_record_id_seq;
DROP TABLE IF EXISTS public.pay_sync_record;
DROP TABLE IF EXISTS public.pay_route_strategy;
DROP TABLE IF EXISTS public.pay_route_scene_config;
DROP TABLE IF EXISTS public.pay_route_basic_config;
DROP TABLE IF EXISTS public.pay_risk_hit;
DROP TABLE IF EXISTS public.pay_refund_order;
DROP TABLE IF EXISTS public.pay_platform_mobile_app;
DROP TABLE IF EXISTS public.pay_normal_order;
DROP TABLE IF EXISTS public.pay_md_provider_method;
DROP TABLE IF EXISTS public.pay_md_provider;
DROP TABLE IF EXISTS public.pay_md_product_config;
DROP TABLE IF EXISTS public.pay_md_product_capability;
DROP TABLE IF EXISTS public.pay_md_product;
DROP TABLE IF EXISTS public.pay_md_method;
DROP TABLE IF EXISTS public.pay_md_channel;
DROP TABLE IF EXISTS public.pay_md_capability;
DROP TABLE IF EXISTS public.pay_gateway_pay_config;
DROP TABLE IF EXISTS public.pay_gateway_pay_client_env;
DROP TABLE IF EXISTS public.pay_gateway_order;
DROP TABLE IF EXISTS public.pay_gateway_cashier_item;
DROP TABLE IF EXISTS public.pay_fund_flow;
DROP TABLE IF EXISTS public.pay_easy_pay_refund_order;
DROP TABLE IF EXISTS public.pay_easy_pay_order;
DROP TABLE IF EXISTS public.pay_easy_pay_credential;
DROP TABLE IF EXISTS public.pay_easy_pay_config;
DROP SEQUENCE IF EXISTS public.pay_close_record_id_seq;
DROP TABLE IF EXISTS public.pay_close_record;
DROP TABLE IF EXISTS public.pay_channel_terminal;
DROP TABLE IF EXISTS public.pay_callback_record;
DROP TABLE IF EXISTS public.pay_blacklist;
DROP TABLE IF EXISTS public.pay_alloc_order;
DROP TABLE IF EXISTS public.pay_alloc_detail;
DROP TABLE IF EXISTS public.pay_abnormal_order;
DROP TABLE IF EXISTS public.notify_notice_read;
DROP TABLE IF EXISTS public.notify_notice;
DROP TABLE IF EXISTS public.notify_message;
DROP TABLE IF EXISTS public.notify_mail_record;
DROP TABLE IF EXISTS public.mch_wx_domain_verify;
DROP SEQUENCE IF EXISTS public.mch_user_id_seq;
DROP TABLE IF EXISTS public.mch_user;
DROP TABLE IF EXISTS public.mch_store_info;
DROP TABLE IF EXISTS public.mch_risk_config;
DROP TABLE IF EXISTS public.mch_notice_task;
DROP TABLE IF EXISTS public.mch_notice_record;
DROP TABLE IF EXISTS public.mch_info;
DROP TABLE IF EXISTS public.mch_credential;
DROP TABLE IF EXISTS public.mch_channel_merchant;
DROP SEQUENCE IF EXISTS public.mch_app_notify_config_id_seq;
DROP TABLE IF EXISTS public.mch_app_notify_config;
DROP TABLE IF EXISTS public.mch_app_info;
DROP TABLE IF EXISTS public.leshua_isv_key_config;
DROP TABLE IF EXISTS public.leshua_isv_channel_merchant;
DROP TABLE IF EXISTS public.lakala_isv_key_config;
DROP TABLE IF EXISTS public.lakala_isv_channel_merchant;
DROP TABLE IF EXISTS public.iam_user_two_factor;
DROP TABLE IF EXISTS public.iam_user_social;
DROP TABLE IF EXISTS public.iam_user_role;
DROP TABLE IF EXISTS public.iam_user_password_security;
DROP TABLE IF EXISTS public.iam_user_password_history;
DROP TABLE IF EXISTS public.iam_user_passkey;
DROP TABLE IF EXISTS public.iam_user_info;
DROP TABLE IF EXISTS public.iam_user_expand_info;
DROP TABLE IF EXISTS public.iam_user_dashboard_preference;
DROP TABLE IF EXISTS public.iam_social_login_config;
DROP TABLE IF EXISTS public.iam_role_menu;
DROP TABLE IF EXISTS public.iam_role_code;
DROP TABLE IF EXISTS public.iam_role;
DROP TABLE IF EXISTS public.iam_perm_menu;
DROP TABLE IF EXISTS public.iam_perm_code;
DROP SEQUENCE IF EXISTS public.hmpay_isv_key_config_id_seq;
DROP TABLE IF EXISTS public.hmpay_isv_key_config;
DROP SEQUENCE IF EXISTS public.hmpay_isv_channel_merchant_id_seq;
DROP TABLE IF EXISTS public.hmpay_isv_channel_merchant;
DROP TABLE IF EXISTS public.hkrt_isv_key_config;
DROP TABLE IF EXISTS public.hkrt_isv_channel_merchant;
DROP TABLE IF EXISTS public.fuyou_isv_key_config;
DROP TABLE IF EXISTS public.fuyou_isv_channel_merchant;
DROP TABLE IF EXISTS public.dy_platform_app_capability;
DROP TABLE IF EXISTS public.dy_platform_app;
DROP TABLE IF EXISTS public.dy_mch_app;
DROP TABLE IF EXISTS public.dy_channel_app_capability;
DROP TABLE IF EXISTS public.douyin_transfer_config;
DROP TABLE IF EXISTS public.douyin_direct_key_config;
DROP TABLE IF EXISTS public.douyin_direct_channel_merchant;
DROP TABLE IF EXISTS public.douyin_direct_alloc_receiver;
DROP TABLE IF EXISTS public.device_qr_code;
DROP TABLE IF EXISTS public.base_user_protocol_version;
DROP TABLE IF EXISTS public.base_user_protocol;
DROP TABLE IF EXISTS public.base_street;
DROP TABLE IF EXISTS public.base_province;
DROP SEQUENCE IF EXISTS public.base_city_adjacent_id_seq;
DROP TABLE IF EXISTS public.base_city_adjacent;
DROP TABLE IF EXISTS public.base_city;
DROP TABLE IF EXISTS public.base_area;
DROP TABLE IF EXISTS public.alipay_transfer_scene_config;
DROP TABLE IF EXISTS public.alipay_transfer_config;
DROP TABLE IF EXISTS public.alipay_isv_channel_merchant;
DROP TABLE IF EXISTS public.alipay_isv_app_key_config;
DROP TABLE IF EXISTS public.alipay_isv_app_auth_config;
DROP TABLE IF EXISTS public.alipay_isv_app;
DROP TABLE IF EXISTS public.alipay_isv_alloc_receiver;
DROP TABLE IF EXISTS public.alipay_direct_channel_merchant;
DROP TABLE IF EXISTS public.alipay_direct_app_key_config;
DROP SEQUENCE IF EXISTS public.alipay_direct_app_capability_id_seq;
DROP TABLE IF EXISTS public.alipay_direct_app_capability;
DROP TABLE IF EXISTS public.alipay_direct_app_auth_config;
DROP TABLE IF EXISTS public.alipay_direct_app;
DROP TABLE IF EXISTS public.alipay_direct_alloc_receiver;
DROP SEQUENCE IF EXISTS public.adapay_direct_key_config_id_seq;
DROP TABLE IF EXISTS public.adapay_direct_key_config;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: adapay_direct_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.adapay_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    adapay_app_id character varying(64),
    api_key text,
    private_key text,
    public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false
);


--
-- Name: TABLE adapay_direct_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.adapay_direct_key_config IS 'Adapay 直连密钥配置';


--
-- Name: COLUMN adapay_direct_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.id IS '主键';


--
-- Name: COLUMN adapay_direct_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.mch_no IS '商户号';


--
-- Name: COLUMN adapay_direct_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.channel_mch_no IS '通道商户号(创建时录入不可修改)';


--
-- Name: COLUMN adapay_direct_key_config.adapay_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.adapay_app_id IS 'Adapay 应用ID(app_id)';


--
-- Name: COLUMN adapay_direct_key_config.api_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.api_key IS 'Adapay API Key(请求头Authorization, 加密存储)';


--
-- Name: COLUMN adapay_direct_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.private_key IS '商户RSA私钥(PKCS#8 Base64, 请求签名, 加密存储)';


--
-- Name: COLUMN adapay_direct_key_config.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.public_key IS 'Adapay 平台公钥(X509 Base64, 响应验签, 加密存储; 为空使用全局默认)';


--
-- Name: COLUMN adapay_direct_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.creator IS '创建者ID';


--
-- Name: COLUMN adapay_direct_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.create_time IS '创建时间';


--
-- Name: COLUMN adapay_direct_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN adapay_direct_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN adapay_direct_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.version IS '版本号(乐观锁)';


--
-- Name: COLUMN adapay_direct_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.adapay_direct_key_config.deleted IS '删除标志';


--
-- Name: adapay_direct_key_config_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.adapay_direct_key_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: adapay_direct_key_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.adapay_direct_key_config_id_seq OWNED BY public.adapay_direct_key_config.id;


--
-- Name: alipay_direct_alloc_receiver; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_direct_alloc_receiver (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    receiver_type character varying(32) NOT NULL,
    receiver_account character varying(256) NOT NULL,
    account_hash character varying(64) NOT NULL,
    receiver_name character varying(256),
    direct_app_ref_id bigint,
    status character varying(16) NOT NULL,
    error_msg text,
    bind_time timestamp(6) with time zone,
    unbind_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_direct_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_direct_alloc_receiver IS '支付宝直连分账接收方(通道侧绑定档案)';


--
-- Name: COLUMN alipay_direct_alloc_receiver.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.id IS '主键';


--
-- Name: COLUMN alipay_direct_alloc_receiver.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.mch_no IS '商户号';


--
-- Name: COLUMN alipay_direct_alloc_receiver.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.channel_mch_no IS '通道商户号(关联通用通道商户主表)';


--
-- Name: COLUMN alipay_direct_alloc_receiver.receiver_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.receiver_type IS '接收方类型';


--
-- Name: COLUMN alipay_direct_alloc_receiver.receiver_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.receiver_account IS '接收方账号(AES-256-GCM加密存储)';


--
-- Name: COLUMN alipay_direct_alloc_receiver.account_hash; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.account_hash IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';


--
-- Name: COLUMN alipay_direct_alloc_receiver.receiver_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.receiver_name IS '接收方名称(AES-256-GCM加密存储, 可空)';


--
-- Name: COLUMN alipay_direct_alloc_receiver.direct_app_ref_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.direct_app_ref_id IS '发起绑定的支付宝应用引用(alipay_direct_app主键, 重新绑定复用)';


--
-- Name: COLUMN alipay_direct_alloc_receiver.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.status IS '绑定状态';


--
-- Name: COLUMN alipay_direct_alloc_receiver.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.error_msg IS '最近一次绑定/解绑失败原因';


--
-- Name: COLUMN alipay_direct_alloc_receiver.bind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.bind_time IS '绑定成功时间';


--
-- Name: COLUMN alipay_direct_alloc_receiver.unbind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.unbind_time IS '解绑成功时间';


--
-- Name: COLUMN alipay_direct_alloc_receiver.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.creator IS '创建人';


--
-- Name: COLUMN alipay_direct_alloc_receiver.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.create_time IS '创建时间';


--
-- Name: COLUMN alipay_direct_alloc_receiver.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_direct_alloc_receiver.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_direct_alloc_receiver.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_direct_alloc_receiver.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_alloc_receiver.deleted IS '逻辑删除标志';


--
-- Name: alipay_direct_app; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_direct_app (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    app_name character varying(64),
    ali_app_id character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    app_type character varying(32)
);


--
-- Name: TABLE alipay_direct_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_direct_app IS '支付宝直连商户应用';


--
-- Name: COLUMN alipay_direct_app.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.id IS '主键';


--
-- Name: COLUMN alipay_direct_app.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.mch_no IS '商户号';


--
-- Name: COLUMN alipay_direct_app.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN alipay_direct_app.app_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.app_name IS '应用名称';


--
-- Name: COLUMN alipay_direct_app.ali_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.ali_app_id IS '支付宝应用ID';


--
-- Name: COLUMN alipay_direct_app.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.creator IS '创建人';


--
-- Name: COLUMN alipay_direct_app.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.create_time IS '创建时间';


--
-- Name: COLUMN alipay_direct_app.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_direct_app.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_direct_app.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_direct_app.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.deleted IS '逻辑删除标志';


--
-- Name: COLUMN alipay_direct_app.app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app.app_type IS '应用类型';


--
-- Name: alipay_direct_app_auth_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_direct_app_auth_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    alipay_direct_app_id bigint,
    user_id_type character varying(32),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_direct_app_auth_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_direct_app_auth_config IS '支付宝直连商户应用授权配置';


--
-- Name: COLUMN alipay_direct_app_auth_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.id IS '主键';


--
-- Name: COLUMN alipay_direct_app_auth_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.mch_no IS '商户号';


--
-- Name: COLUMN alipay_direct_app_auth_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN alipay_direct_app_auth_config.alipay_direct_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.alipay_direct_app_id IS '关联应用 ID';


--
-- Name: COLUMN alipay_direct_app_auth_config.user_id_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.user_id_type IS '用户标识类型';


--
-- Name: COLUMN alipay_direct_app_auth_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.creator IS '创建人';


--
-- Name: COLUMN alipay_direct_app_auth_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.create_time IS '创建时间';


--
-- Name: COLUMN alipay_direct_app_auth_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_direct_app_auth_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_direct_app_auth_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_direct_app_auth_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_auth_config.deleted IS '逻辑删除标志';


--
-- Name: alipay_direct_app_capability; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_direct_app_capability (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    capability character varying(32) NOT NULL,
    alipay_direct_app_id bigint NOT NULL,
    create_time timestamp(6) with time zone DEFAULT now(),
    update_time timestamp(6) with time zone DEFAULT now(),
    deleted boolean DEFAULT false,
    creator bigint,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0
);


--
-- Name: TABLE alipay_direct_app_capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_direct_app_capability IS '支付宝直连商户应用支付能力关联';


--
-- Name: COLUMN alipay_direct_app_capability.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.id IS '主键';


--
-- Name: COLUMN alipay_direct_app_capability.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.mch_no IS '商户号';


--
-- Name: COLUMN alipay_direct_app_capability.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN alipay_direct_app_capability.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.capability IS '支付能力编码';


--
-- Name: COLUMN alipay_direct_app_capability.alipay_direct_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.alipay_direct_app_id IS '关联支付宝直连应用ID';


--
-- Name: COLUMN alipay_direct_app_capability.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.create_time IS '创建时间';


--
-- Name: COLUMN alipay_direct_app_capability.update_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.update_time IS '更新时间';


--
-- Name: COLUMN alipay_direct_app_capability.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.deleted IS '删除标志';


--
-- Name: COLUMN alipay_direct_app_capability.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.creator IS '创建者ID';


--
-- Name: COLUMN alipay_direct_app_capability.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN alipay_direct_app_capability.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_direct_app_capability.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_capability.version IS '乐观锁版本号';


--
-- Name: alipay_direct_app_capability_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.alipay_direct_app_capability_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: alipay_direct_app_capability_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.alipay_direct_app_capability_id_seq OWNED BY public.alipay_direct_app_capability.id;


--
-- Name: alipay_direct_app_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_direct_app_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    alipay_direct_app_id bigint,
    auth_type character varying(32),
    alipay_public_key text,
    private_key text,
    app_cert text,
    alipay_cert text,
    alipay_root_cert text,
    secret_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE alipay_direct_app_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_direct_app_key_config IS '支付宝直连商户应用密钥配置';


--
-- Name: COLUMN alipay_direct_app_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.id IS '主键';


--
-- Name: COLUMN alipay_direct_app_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.mch_no IS '商户号';


--
-- Name: COLUMN alipay_direct_app_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN alipay_direct_app_key_config.alipay_direct_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.alipay_direct_app_id IS '关联应用 ID';


--
-- Name: COLUMN alipay_direct_app_key_config.auth_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.auth_type IS '认证类型';


--
-- Name: COLUMN alipay_direct_app_key_config.alipay_public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.alipay_public_key IS '支付宝公钥';


--
-- Name: COLUMN alipay_direct_app_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.private_key IS '应用私钥(加密存储)';


--
-- Name: COLUMN alipay_direct_app_key_config.app_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.app_cert IS '应用公钥证书(加密存储)';


--
-- Name: COLUMN alipay_direct_app_key_config.alipay_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.alipay_cert IS '支付宝公钥证书(加密存储)';


--
-- Name: COLUMN alipay_direct_app_key_config.alipay_root_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.alipay_root_cert IS '支付宝CA根证书(加密存储)';


--
-- Name: COLUMN alipay_direct_app_key_config.secret_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.secret_key IS 'AES通信密钥(加密存储)';


--
-- Name: COLUMN alipay_direct_app_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.creator IS '创建人';


--
-- Name: COLUMN alipay_direct_app_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.create_time IS '创建时间';


--
-- Name: COLUMN alipay_direct_app_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_direct_app_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_direct_app_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_direct_app_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.deleted IS '逻辑删除标志';


--
-- Name: COLUMN alipay_direct_app_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_app_key_config.sandbox IS '是否沙箱环境';


--
-- Name: alipay_direct_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_direct_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    alipay_user_id character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE alipay_direct_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_direct_channel_merchant IS '支付宝直连通道商户绑定';


--
-- Name: COLUMN alipay_direct_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.id IS '主键';


--
-- Name: COLUMN alipay_direct_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN alipay_direct_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.channel_mch_no IS '通道商户号(系统生成雪花号)';


--
-- Name: COLUMN alipay_direct_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.product IS '所属支付产品';


--
-- Name: COLUMN alipay_direct_channel_merchant.alipay_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.alipay_user_id IS '支付宝商家唯一识别码(2088开头的16位数字)';


--
-- Name: COLUMN alipay_direct_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.creator IS '创建人';


--
-- Name: COLUMN alipay_direct_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN alipay_direct_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_direct_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_direct_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_direct_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.deleted IS '逻辑删除标志';


--
-- Name: COLUMN alipay_direct_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_direct_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: alipay_isv_alloc_receiver; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_isv_alloc_receiver (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    receiver_type character varying(32) NOT NULL,
    receiver_account character varying(256) NOT NULL,
    account_hash character varying(64) NOT NULL,
    receiver_name character varying(256),
    status character varying(16) NOT NULL,
    error_msg text,
    bind_time timestamp(6) with time zone,
    unbind_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_isv_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_isv_alloc_receiver IS '支付宝服务商分账接收方(通道侧绑定档案, 凭证由子商户授权绑定自动决定)';


--
-- Name: COLUMN alipay_isv_alloc_receiver.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.id IS '主键';


--
-- Name: COLUMN alipay_isv_alloc_receiver.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.mch_no IS '商户号';


--
-- Name: COLUMN alipay_isv_alloc_receiver.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.channel_mch_no IS '通道商户号(关联通用通道商户主表)';


--
-- Name: COLUMN alipay_isv_alloc_receiver.receiver_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.receiver_type IS '接收方类型';


--
-- Name: COLUMN alipay_isv_alloc_receiver.receiver_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.receiver_account IS '接收方账号(AES-256-GCM加密存储)';


--
-- Name: COLUMN alipay_isv_alloc_receiver.account_hash; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.account_hash IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';


--
-- Name: COLUMN alipay_isv_alloc_receiver.receiver_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.receiver_name IS '接收方名称(AES-256-GCM加密存储, 可空)';


--
-- Name: COLUMN alipay_isv_alloc_receiver.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.status IS '绑定状态';


--
-- Name: COLUMN alipay_isv_alloc_receiver.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.error_msg IS '最近一次绑定/解绑失败原因';


--
-- Name: COLUMN alipay_isv_alloc_receiver.bind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.bind_time IS '绑定成功时间';


--
-- Name: COLUMN alipay_isv_alloc_receiver.unbind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.unbind_time IS '解绑成功时间';


--
-- Name: COLUMN alipay_isv_alloc_receiver.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.creator IS '创建人';


--
-- Name: COLUMN alipay_isv_alloc_receiver.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.create_time IS '创建时间';


--
-- Name: COLUMN alipay_isv_alloc_receiver.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_isv_alloc_receiver.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_isv_alloc_receiver.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_isv_alloc_receiver.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_alloc_receiver.deleted IS '逻辑删除标志';


--
-- Name: alipay_isv_app; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_isv_app (
    id bigint NOT NULL,
    app_name character varying(64),
    ali_app_id character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_isv_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_isv_app IS '支付宝服务商应用';


--
-- Name: COLUMN alipay_isv_app.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.id IS '主键';


--
-- Name: COLUMN alipay_isv_app.app_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.app_name IS '应用名称';


--
-- Name: COLUMN alipay_isv_app.ali_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.ali_app_id IS '支付宝应用ID';


--
-- Name: COLUMN alipay_isv_app.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.creator IS '创建人';


--
-- Name: COLUMN alipay_isv_app.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.create_time IS '创建时间';


--
-- Name: COLUMN alipay_isv_app.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_isv_app.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_isv_app.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_isv_app.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app.deleted IS '逻辑删除标志';


--
-- Name: alipay_isv_app_auth_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_isv_app_auth_config (
    id bigint NOT NULL,
    alipay_isv_app_id bigint,
    user_id_type character varying(32),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_isv_app_auth_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_isv_app_auth_config IS '支付宝服务商应用授权配置';


--
-- Name: COLUMN alipay_isv_app_auth_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.id IS '主键';


--
-- Name: COLUMN alipay_isv_app_auth_config.alipay_isv_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.alipay_isv_app_id IS '支付宝服务商应用 ID';


--
-- Name: COLUMN alipay_isv_app_auth_config.user_id_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.user_id_type IS '用户标识类型';


--
-- Name: COLUMN alipay_isv_app_auth_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.creator IS '创建人';


--
-- Name: COLUMN alipay_isv_app_auth_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.create_time IS '创建时间';


--
-- Name: COLUMN alipay_isv_app_auth_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_isv_app_auth_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_isv_app_auth_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_isv_app_auth_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_auth_config.deleted IS '逻辑删除标志';


--
-- Name: alipay_isv_app_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_isv_app_key_config (
    id bigint NOT NULL,
    alipay_isv_app_id bigint,
    auth_type character varying(32),
    alipay_public_key text,
    private_key text,
    app_cert text,
    alipay_cert text,
    alipay_root_cert text,
    secret_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_isv_app_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_isv_app_key_config IS '支付宝服务商应用密钥配置';


--
-- Name: COLUMN alipay_isv_app_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.id IS '主键';


--
-- Name: COLUMN alipay_isv_app_key_config.alipay_isv_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.alipay_isv_app_id IS '支付宝服务商应用 ID';


--
-- Name: COLUMN alipay_isv_app_key_config.auth_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.auth_type IS '认证类型';


--
-- Name: COLUMN alipay_isv_app_key_config.alipay_public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.alipay_public_key IS '支付宝公钥';


--
-- Name: COLUMN alipay_isv_app_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.private_key IS '应用私钥(加密存储)';


--
-- Name: COLUMN alipay_isv_app_key_config.app_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.app_cert IS '应用公钥证书(加密存储)';


--
-- Name: COLUMN alipay_isv_app_key_config.alipay_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.alipay_cert IS '支付宝公钥证书(加密存储)';


--
-- Name: COLUMN alipay_isv_app_key_config.alipay_root_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.alipay_root_cert IS '支付宝CA根证书(加密存储)';


--
-- Name: COLUMN alipay_isv_app_key_config.secret_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.secret_key IS 'AES通信密钥(加密存储)';


--
-- Name: COLUMN alipay_isv_app_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.creator IS '创建人';


--
-- Name: COLUMN alipay_isv_app_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.create_time IS '创建时间';


--
-- Name: COLUMN alipay_isv_app_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_isv_app_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_isv_app_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_isv_app_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_app_key_config.deleted IS '逻辑删除标志';


--
-- Name: alipay_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    isv_app_id bigint,
    alipay_user_id character varying(64),
    app_auth_token character varying(128),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_isv_channel_merchant IS '支付宝服务商通道商户绑定';


--
-- Name: COLUMN alipay_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN alipay_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN alipay_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.channel_mch_no IS '通道商户号(AISV+雪花)';


--
-- Name: COLUMN alipay_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.product IS '所属支付产品';


--
-- Name: COLUMN alipay_isv_channel_merchant.isv_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.isv_app_id IS '关联服务商应用 ID';


--
-- Name: COLUMN alipay_isv_channel_merchant.alipay_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.alipay_user_id IS '子商户支付宝识别码(2088开头的16位数字)';


--
-- Name: COLUMN alipay_isv_channel_merchant.app_auth_token; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.app_auth_token IS '应用授权令牌(服务商代子商户调用接口的凭据, 会过期/刷新)';


--
-- Name: COLUMN alipay_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.creator IS '创建人';


--
-- Name: COLUMN alipay_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN alipay_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.last_modifier IS '最后修改人';


--
-- Name: COLUMN alipay_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_isv_channel_merchant.deleted IS '逻辑删除标志';


--
-- Name: alipay_transfer_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_transfer_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    transfer_app_ref_id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_transfer_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_transfer_config IS '支付宝转账配置(通道商户一对一绑定转账转出应用, 发起转账时按配置解析应用与密钥)';


--
-- Name: COLUMN alipay_transfer_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.id IS '主键';


--
-- Name: COLUMN alipay_transfer_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.mch_no IS '商户号';


--
-- Name: COLUMN alipay_transfer_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.channel_mch_no IS '通道商户号(一对一)';


--
-- Name: COLUMN alipay_transfer_config.transfer_app_ref_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.transfer_app_ref_id IS '转账转出应用引用(alipay_direct_app主键, 决定转账使用的aliAppId与密钥)';


--
-- Name: COLUMN alipay_transfer_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.creator IS '创建人ID';


--
-- Name: COLUMN alipay_transfer_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.create_time IS '创建时间';


--
-- Name: COLUMN alipay_transfer_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN alipay_transfer_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_transfer_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_transfer_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_config.deleted IS '逻辑删除标志';


--
-- Name: alipay_transfer_scene_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alipay_transfer_scene_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    scene_name character varying(64) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    is_default boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE alipay_transfer_scene_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.alipay_transfer_scene_config IS '支付宝转账场景配置(主数据枚举驱动, 仅存启用/默认状态行, 最多启用3个, 默认必须启用)';


--
-- Name: COLUMN alipay_transfer_scene_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.id IS '主键';


--
-- Name: COLUMN alipay_transfer_scene_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.mch_no IS '商户号';


--
-- Name: COLUMN alipay_transfer_scene_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN alipay_transfer_scene_config.scene_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.scene_name IS '转账场景名称(8枚举:现金营销/企业退款/佣金报酬/业务结算/二手回收/公益补助/行政补贴和退款/保险理赔)';


--
-- Name: COLUMN alipay_transfer_scene_config.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.enabled IS '是否启用(一个通道商户最多启用3个转账场景)';


--
-- Name: COLUMN alipay_transfer_scene_config.is_default; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.is_default IS '是否默认场景(默认必须启用, 由部分唯一索引约束)';


--
-- Name: COLUMN alipay_transfer_scene_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.creator IS '创建人ID';


--
-- Name: COLUMN alipay_transfer_scene_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.create_time IS '创建时间';


--
-- Name: COLUMN alipay_transfer_scene_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN alipay_transfer_scene_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN alipay_transfer_scene_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.version IS '乐观锁版本号';


--
-- Name: COLUMN alipay_transfer_scene_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.alipay_transfer_scene_config.deleted IS '逻辑删除标志';


--
-- Name: base_area; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.base_area (
    code character varying(6) NOT NULL,
    name character varying(60) NOT NULL,
    city_code character varying(4) NOT NULL
);


--
-- Name: TABLE base_area; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.base_area IS '县区表';


--
-- Name: COLUMN base_area.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_area.code IS '区域编码';


--
-- Name: COLUMN base_area.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_area.name IS '区域名称';


--
-- Name: COLUMN base_area.city_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_area.city_code IS '城市编码';


--
-- Name: base_city; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.base_city (
    code character varying(4) NOT NULL,
    name character varying(60) NOT NULL,
    province_code character varying(2) NOT NULL
);


--
-- Name: TABLE base_city; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.base_city IS '城市表';


--
-- Name: COLUMN base_city.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_city.code IS '城市编码';


--
-- Name: COLUMN base_city.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_city.name IS '城市名称';


--
-- Name: COLUMN base_city.province_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_city.province_code IS '省份编码';


--
-- Name: base_city_adjacent; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.base_city_adjacent (
    city_code character varying(4) NOT NULL,
    adjacent_city_code character varying(4) NOT NULL,
    id bigint NOT NULL
);


--
-- Name: TABLE base_city_adjacent; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.base_city_adjacent IS '城市接壤关系表（双向存储，围栏 balanced 邻市容错用）';


--
-- Name: COLUMN base_city_adjacent.city_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_city_adjacent.city_code IS '城市编码（base_city.code）';


--
-- Name: COLUMN base_city_adjacent.adjacent_city_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_city_adjacent.adjacent_city_code IS '相邻城市编码（base_city.code）';


--
-- Name: COLUMN base_city_adjacent.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_city_adjacent.id IS '主键ID（DB 自增, 纯关系表数据导入专用）';


--
-- Name: base_city_adjacent_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.base_city_adjacent_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: base_city_adjacent_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.base_city_adjacent_id_seq OWNED BY public.base_city_adjacent.id;


--
-- Name: base_province; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.base_province (
    code character varying(2) NOT NULL,
    name character varying(30) NOT NULL
);


--
-- Name: TABLE base_province; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.base_province IS '省份表';


--
-- Name: COLUMN base_province.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_province.code IS '省份编码';


--
-- Name: COLUMN base_province.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_province.name IS '省份名称';


--
-- Name: base_street; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.base_street (
    code character varying(9) NOT NULL,
    name character varying(60) NOT NULL,
    area_code character varying(6) NOT NULL
);


--
-- Name: TABLE base_street; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.base_street IS '街道表';


--
-- Name: COLUMN base_street.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_street.code IS '编码';


--
-- Name: COLUMN base_street.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_street.name IS '街道名称';


--
-- Name: COLUMN base_street.area_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_street.area_code IS '县区编码';


--
-- Name: base_user_protocol; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.base_user_protocol (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    show_name character varying(100),
    type character varying(50) NOT NULL,
    client_type character varying(50) NOT NULL,
    default_protocol boolean DEFAULT false NOT NULL,
    default_language character varying(10) DEFAULT 'zh-CN'::character varying NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE base_user_protocol; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.base_user_protocol IS '用户协议';


--
-- Name: COLUMN base_user_protocol.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.id IS '主键';


--
-- Name: COLUMN base_user_protocol.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.name IS '名称';


--
-- Name: COLUMN base_user_protocol.show_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.show_name IS '显示名称';


--
-- Name: COLUMN base_user_protocol.type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.type IS '协议类型(USER_AGREEMENT/PRIVACY_POLICY/THIRD_PARTY_INFO/CHILDREN_POLICY)';


--
-- Name: COLUMN base_user_protocol.client_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.client_type IS '端类型(WEB/APP/MINIAPP)';


--
-- Name: COLUMN base_user_protocol.default_protocol; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.default_protocol IS '是否默认协议(同类型同端唯一)';


--
-- Name: COLUMN base_user_protocol.default_language; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.default_language IS '默认语言(对外拉取时回退使用)';


--
-- Name: COLUMN base_user_protocol.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.creator IS '创建者ID';


--
-- Name: COLUMN base_user_protocol.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.create_time IS '创建时间';


--
-- Name: COLUMN base_user_protocol.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.last_modifier IS '最后修改ID';


--
-- Name: COLUMN base_user_protocol.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN base_user_protocol.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.version IS '乐观锁版本号';


--
-- Name: COLUMN base_user_protocol.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol.deleted IS '逻辑删除标志';


--
-- Name: base_user_protocol_version; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.base_user_protocol_version (
    id bigint NOT NULL,
    protocol_id bigint NOT NULL,
    language character varying(10) NOT NULL,
    version_no integer,
    version_label character varying(32),
    title character varying(200) NOT NULL,
    content text NOT NULL,
    content_html text,
    content_format character varying(20) DEFAULT 'MARKDOWN'::character varying,
    status character varying(20) DEFAULT 'DRAFT'::character varying NOT NULL,
    effective_time timestamp(6) with time zone,
    summary character varying(500),
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE base_user_protocol_version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.base_user_protocol_version IS '用户协议版本';


--
-- Name: COLUMN base_user_protocol_version.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.id IS '主键';


--
-- Name: COLUMN base_user_protocol_version.protocol_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.protocol_id IS '协议ID';


--
-- Name: COLUMN base_user_protocol_version.language; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.language IS '语言';


--
-- Name: COLUMN base_user_protocol_version.version_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.version_no IS '版本号(同协议同语言自增)';


--
-- Name: COLUMN base_user_protocol_version.version_label; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.version_label IS '版本标签, 如 v1.0.0';


--
-- Name: COLUMN base_user_protocol_version.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.title IS '标题';


--
-- Name: COLUMN base_user_protocol_version.content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.content IS '协议内容(Markdown)';


--
-- Name: COLUMN base_user_protocol_version.content_html; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.content_html IS '渲染后的HTML';


--
-- Name: COLUMN base_user_protocol_version.content_format; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.content_format IS '内容格式';


--
-- Name: COLUMN base_user_protocol_version.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.status IS '状态';


--
-- Name: COLUMN base_user_protocol_version.effective_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.effective_time IS '生效时间';


--
-- Name: COLUMN base_user_protocol_version.summary; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.summary IS '变更说明';


--
-- Name: COLUMN base_user_protocol_version.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.creator IS '创建者ID';


--
-- Name: COLUMN base_user_protocol_version.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.create_time IS '创建时间';


--
-- Name: COLUMN base_user_protocol_version.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.last_modifier IS '最后修改ID';


--
-- Name: COLUMN base_user_protocol_version.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN base_user_protocol_version.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.version IS '乐观锁版本号';


--
-- Name: COLUMN base_user_protocol_version.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.base_user_protocol_version.deleted IS '逻辑删除标志';


--
-- Name: device_qr_code; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.device_qr_code (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(100),
    batch_no character varying(64),
    mch_no character varying(32),
    app_id character varying(64),
    program_type character varying(20) DEFAULT 'h5'::character varying NOT NULL,
    amount_type character varying(20) NOT NULL,
    fixed_amount bigint,
    status character varying(20) NOT NULL,
    remark character varying(500),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    store_no character varying(64),
    allocation boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE device_qr_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.device_qr_code IS '支付码牌';


--
-- Name: COLUMN device_qr_code.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.id IS '主键';


--
-- Name: COLUMN device_qr_code.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.code IS '码牌编码(唯一, 二维码参数)';


--
-- Name: COLUMN device_qr_code.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.name IS '码牌名称';


--
-- Name: COLUMN device_qr_code.batch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.batch_no IS '批次号';


--
-- Name: COLUMN device_qr_code.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.mch_no IS '所属商户号(空=空白库存)';


--
-- Name: COLUMN device_qr_code.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.app_id IS '关联应用号(空=商户默认应用)';


--
-- Name: COLUMN device_qr_code.program_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.program_type IS '落地程序类型 h5/小程序(mini_app)';


--
-- Name: COLUMN device_qr_code.amount_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.amount_type IS '金额类型 random/fixed';


--
-- Name: COLUMN device_qr_code.fixed_amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.fixed_amount IS '固定金额(分)';


--
-- Name: COLUMN device_qr_code.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.status IS '状态 enabled/disabled';


--
-- Name: COLUMN device_qr_code.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.remark IS '备注';


--
-- Name: COLUMN device_qr_code.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.creator IS '创建者ID';


--
-- Name: COLUMN device_qr_code.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.create_time IS '创建时间';


--
-- Name: COLUMN device_qr_code.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN device_qr_code.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN device_qr_code.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.version IS '版本号';


--
-- Name: COLUMN device_qr_code.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.deleted IS '逻辑删除标识';


--
-- Name: COLUMN device_qr_code.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.store_no IS '绑定门店号(可空; 对应 mch_store_info.store_no)';


--
-- Name: COLUMN device_qr_code.allocation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.device_qr_code.allocation IS '是否分账码牌(开启后扫码支付向下单链路透传分账标识; 产品不支持分账时自动降级普通收款, 交易分账状态记为 unsupported)';


--
-- Name: douyin_direct_alloc_receiver; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.douyin_direct_alloc_receiver (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    receiver_type character varying(32) NOT NULL,
    receiver_account character varying(256) NOT NULL,
    account_hash character varying(64) NOT NULL,
    receiver_name character varying(256),
    relation_type character varying(32) NOT NULL,
    custom_relation character varying(64),
    channel_app_id character varying(64) NOT NULL,
    status character varying(16) NOT NULL,
    error_msg text,
    bind_time timestamp(6) with time zone,
    unbind_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE douyin_direct_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.douyin_direct_alloc_receiver IS '抖音直连分账接收方(通道侧绑定档案)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.id IS '主键';


--
-- Name: COLUMN douyin_direct_alloc_receiver.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.mch_no IS '商户号';


--
-- Name: COLUMN douyin_direct_alloc_receiver.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.channel_mch_no IS '通道商户号(关联通用通道商户主表)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.receiver_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.receiver_type IS '接收方类型';


--
-- Name: COLUMN douyin_direct_alloc_receiver.receiver_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.receiver_account IS '接收方账号(AES-256-GCM加密存储)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.account_hash; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.account_hash IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.receiver_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.receiver_name IS '接收方名称(AES-256-GCM加密存储, MERCHANT_ID时必填商户全称)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.relation_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.relation_type IS '分账关系类型(抖音原生大写, CUSTOM时需custom_relation)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.custom_relation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.custom_relation IS '自定义分账关系名(relation_type=CUSTOM时必填)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.channel_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.channel_app_id IS '绑定时所用商户档抖音应用appid(重新绑定复用)';


--
-- Name: COLUMN douyin_direct_alloc_receiver.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.status IS '绑定状态';


--
-- Name: COLUMN douyin_direct_alloc_receiver.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.error_msg IS '最近一次绑定/解绑失败原因';


--
-- Name: COLUMN douyin_direct_alloc_receiver.bind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.bind_time IS '绑定成功时间';


--
-- Name: COLUMN douyin_direct_alloc_receiver.unbind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.unbind_time IS '解绑成功时间';


--
-- Name: COLUMN douyin_direct_alloc_receiver.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.creator IS '创建人';


--
-- Name: COLUMN douyin_direct_alloc_receiver.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.create_time IS '创建时间';


--
-- Name: COLUMN douyin_direct_alloc_receiver.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.last_modifier IS '最后修改人';


--
-- Name: COLUMN douyin_direct_alloc_receiver.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN douyin_direct_alloc_receiver.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.version IS '乐观锁版本号';


--
-- Name: COLUMN douyin_direct_alloc_receiver.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_alloc_receiver.deleted IS '逻辑删除标志';


--
-- Name: douyin_direct_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.douyin_direct_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    dy_mch_id character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    transfer_scene character varying(50)
);


--
-- Name: TABLE douyin_direct_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.douyin_direct_channel_merchant IS '抖音直连通道商户绑定';


--
-- Name: COLUMN douyin_direct_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.id IS '主键';


--
-- Name: COLUMN douyin_direct_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN douyin_direct_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.channel_mch_no IS '通道商户号(系统生成雪花号)';


--
-- Name: COLUMN douyin_direct_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.product IS '所属支付产品(如 douyin_pay)';


--
-- Name: COLUMN douyin_direct_channel_merchant.dy_mch_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.dy_mch_id IS '抖音商户号(MCHID)';


--
-- Name: COLUMN douyin_direct_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.creator IS '创建人ID';


--
-- Name: COLUMN douyin_direct_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN douyin_direct_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN douyin_direct_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN douyin_direct_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN douyin_direct_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.deleted IS '逻辑删除标志';


--
-- Name: COLUMN douyin_direct_channel_merchant.transfer_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_channel_merchant.transfer_scene IS '转账场景ID(商家转账, 未配置时发起转账报错)';


--
-- Name: douyin_direct_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.douyin_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    merchant_private_key text,
    merchant_serial_number character varying(64),
    encrypt_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE douyin_direct_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.douyin_direct_key_config IS '抖音直连密钥配置';


--
-- Name: COLUMN douyin_direct_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.id IS '主键';


--
-- Name: COLUMN douyin_direct_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.mch_no IS '商户号';


--
-- Name: COLUMN douyin_direct_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.channel_mch_no IS '通道商户号(唯一关联)';


--
-- Name: COLUMN douyin_direct_key_config.merchant_private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.merchant_private_key IS '商户私钥(MERCHANT_PRIVATE_KEY, 加密存储)';


--
-- Name: COLUMN douyin_direct_key_config.merchant_serial_number; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.merchant_serial_number IS '商家公钥证书序列号(MERCHANT_SERIAL_NO)';


--
-- Name: COLUMN douyin_direct_key_config.encrypt_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.encrypt_key IS '接口加密密钥(ENCRYPT_KEY, 加密存储)';


--
-- Name: COLUMN douyin_direct_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.creator IS '创建人ID';


--
-- Name: COLUMN douyin_direct_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.create_time IS '创建时间';


--
-- Name: COLUMN douyin_direct_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN douyin_direct_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN douyin_direct_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN douyin_direct_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_direct_key_config.deleted IS '逻辑删除标志';


--
-- Name: douyin_transfer_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.douyin_transfer_config (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    transfer_app_ref_id bigint
);


--
-- Name: TABLE douyin_transfer_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.douyin_transfer_config IS '抖音转账配置(一对一, 指定转账发起应用, 决定转出主体与收款人openId来源)';


--
-- Name: COLUMN douyin_transfer_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.id IS '主键';


--
-- Name: COLUMN douyin_transfer_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.creator IS '创建者ID';


--
-- Name: COLUMN douyin_transfer_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.create_time IS '创建时间';


--
-- Name: COLUMN douyin_transfer_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.last_modifier IS '最后修改ID';


--
-- Name: COLUMN douyin_transfer_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN douyin_transfer_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.version IS '版本号';


--
-- Name: COLUMN douyin_transfer_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.deleted IS '删除标志';


--
-- Name: COLUMN douyin_transfer_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.mch_no IS '商户号';


--
-- Name: COLUMN douyin_transfer_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN douyin_transfer_config.transfer_app_ref_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.douyin_transfer_config.transfer_app_ref_id IS '转账发起应用引用(dy_mch_app 主键, 须为网站应用 web_app, 支持手机H5获取OpenId)';


--
-- Name: dy_channel_app_capability; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dy_channel_app_capability (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    capability character varying(64) NOT NULL,
    app_scope character varying(16) NOT NULL,
    dy_app_ref_id bigint NOT NULL
);


--
-- Name: TABLE dy_channel_app_capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.dy_channel_app_capability IS '通道商户抖音应用能力绑定（同能力可按档位双绑 platform+merchant）';


--
-- Name: COLUMN dy_channel_app_capability.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.id IS '主键';


--
-- Name: COLUMN dy_channel_app_capability.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.creator IS '创建者ID';


--
-- Name: COLUMN dy_channel_app_capability.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.create_time IS '创建时间';


--
-- Name: COLUMN dy_channel_app_capability.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.last_modifier IS '最后修改ID';


--
-- Name: COLUMN dy_channel_app_capability.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN dy_channel_app_capability.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.version IS '版本号';


--
-- Name: COLUMN dy_channel_app_capability.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.deleted IS '删除标志';


--
-- Name: COLUMN dy_channel_app_capability.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.mch_no IS '商户号';


--
-- Name: COLUMN dy_channel_app_capability.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN dy_channel_app_capability.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.capability IS '支付能力编码';


--
-- Name: COLUMN dy_channel_app_capability.app_scope; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.app_scope IS '应用档位：platform/merchant';


--
-- Name: COLUMN dy_channel_app_capability.dy_app_ref_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_channel_app_capability.dy_app_ref_id IS '抖音应用主数据主键（由 app_scope 决定指向平台或商户表）';


--
-- Name: dy_mch_app; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dy_mch_app (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_name character varying(64) NOT NULL,
    app_type character varying(32) NOT NULL,
    douyin_app_id character varying(64) NOT NULL,
    app_secret character varying(512)
);


--
-- Name: TABLE dy_mch_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.dy_mch_app IS '商户抖音应用（商户域开放平台身份，跨通道可引用）';


--
-- Name: COLUMN dy_mch_app.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.id IS '主键';


--
-- Name: COLUMN dy_mch_app.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.creator IS '创建者ID';


--
-- Name: COLUMN dy_mch_app.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.create_time IS '创建时间';


--
-- Name: COLUMN dy_mch_app.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.last_modifier IS '最后修改ID';


--
-- Name: COLUMN dy_mch_app.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN dy_mch_app.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.version IS '版本号';


--
-- Name: COLUMN dy_mch_app.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.deleted IS '删除标志';


--
-- Name: COLUMN dy_mch_app.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.mch_no IS '商户号';


--
-- Name: COLUMN dy_mch_app.app_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.app_name IS '应用名称';


--
-- Name: COLUMN dy_mch_app.app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.app_type IS '应用类型：mini_program/mobile_app/web_app';


--
-- Name: COLUMN dy_mch_app.douyin_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.douyin_app_id IS '抖音应用AppId';


--
-- Name: COLUMN dy_mch_app.app_secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_mch_app.app_secret IS '应用密钥(加密存储)';


--
-- Name: dy_platform_app; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dy_platform_app (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    app_name character varying(64) NOT NULL,
    app_type character varying(32) NOT NULL,
    douyin_app_id character varying(64) NOT NULL,
    app_secret character varying(512)
);


--
-- Name: TABLE dy_platform_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.dy_platform_app IS '平台抖音应用（开放平台身份，跨通道可引用）';


--
-- Name: COLUMN dy_platform_app.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.id IS '主键';


--
-- Name: COLUMN dy_platform_app.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.creator IS '创建者ID';


--
-- Name: COLUMN dy_platform_app.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.create_time IS '创建时间';


--
-- Name: COLUMN dy_platform_app.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.last_modifier IS '最后修改ID';


--
-- Name: COLUMN dy_platform_app.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN dy_platform_app.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.version IS '版本号';


--
-- Name: COLUMN dy_platform_app.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.deleted IS '删除标志';


--
-- Name: COLUMN dy_platform_app.app_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.app_name IS '应用名称';


--
-- Name: COLUMN dy_platform_app.app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.app_type IS '应用类型：mini_program/mobile_app/web_app';


--
-- Name: COLUMN dy_platform_app.douyin_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.douyin_app_id IS '抖音应用AppId';


--
-- Name: COLUMN dy_platform_app.app_secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app.app_secret IS '应用密钥(加密存储)';


--
-- Name: dy_platform_app_capability; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.dy_platform_app_capability (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    capability character varying(64) NOT NULL,
    dy_platform_app_id bigint NOT NULL,
    product character varying(64) NOT NULL
);


--
-- Name: TABLE dy_platform_app_capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.dy_platform_app_capability IS '平台抖音应用默认能力绑定（全局一能力一应用）';


--
-- Name: COLUMN dy_platform_app_capability.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.id IS '主键';


--
-- Name: COLUMN dy_platform_app_capability.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.creator IS '创建者ID';


--
-- Name: COLUMN dy_platform_app_capability.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.create_time IS '创建时间';


--
-- Name: COLUMN dy_platform_app_capability.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.last_modifier IS '最后修改ID';


--
-- Name: COLUMN dy_platform_app_capability.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN dy_platform_app_capability.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.version IS '版本号';


--
-- Name: COLUMN dy_platform_app_capability.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.deleted IS '删除标志';


--
-- Name: COLUMN dy_platform_app_capability.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.capability IS '支付能力编码';


--
-- Name: COLUMN dy_platform_app_capability.dy_platform_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.dy_platform_app_id IS '平台抖音应用ID';


--
-- Name: COLUMN dy_platform_app_capability.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.dy_platform_app_capability.product IS '产品编码';


--
-- Name: fuyou_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fuyou_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    product character varying(32),
    sandbox boolean DEFAULT false,
    fuyou_mch_no character varying(64),
    term_no character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE fuyou_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.fuyou_isv_channel_merchant IS '富友通道商户绑定';


--
-- Name: COLUMN fuyou_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN fuyou_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN fuyou_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.channel_mch_no IS '通道商户号(FUYOU+雪花)';


--
-- Name: COLUMN fuyou_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code)';


--
-- Name: COLUMN fuyou_isv_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: COLUMN fuyou_isv_channel_merchant.fuyou_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.fuyou_mch_no IS '富友商户号(mchnt_cd)';


--
-- Name: COLUMN fuyou_isv_channel_merchant.term_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.term_no IS '终端号(term_id)';


--
-- Name: COLUMN fuyou_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.creator IS '创建者ID';


--
-- Name: COLUMN fuyou_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN fuyou_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN fuyou_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN fuyou_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.version IS '版本号(乐观锁)';


--
-- Name: COLUMN fuyou_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_channel_merchant.deleted IS '删除标志';


--
-- Name: fuyou_isv_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.fuyou_isv_key_config (
    id bigint NOT NULL,
    product character varying(32) NOT NULL,
    fy_app_id character varying(64),
    order_prefix character varying(64),
    private_key text,
    public_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE fuyou_isv_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.fuyou_isv_key_config IS '富友服务商密钥配置';


--
-- Name: COLUMN fuyou_isv_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.id IS '主键';


--
-- Name: COLUMN fuyou_isv_key_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.product IS '产品编码(对应 ProductEnum.code, 如 fuyou_pay)';


--
-- Name: COLUMN fuyou_isv_key_config.fy_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.fy_app_id IS '富友应用编号(机构号 ins_cd)';


--
-- Name: COLUMN fuyou_isv_key_config.order_prefix; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.order_prefix IS '富友订单前缀(关联订单号前缀, 富友回调凭 mchnt_order_no 反查平台订单)';


--
-- Name: COLUMN fuyou_isv_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.private_key IS '商户RSA私钥(PKCS8 Base64, MD5withRSA 签名, 加密存储)';


--
-- Name: COLUMN fuyou_isv_key_config.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.public_key IS '富友RSA公钥(X509 Base64, 响应/回调验签, 加密存储)';


--
-- Name: COLUMN fuyou_isv_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.sandbox IS '是否沙箱环境';


--
-- Name: COLUMN fuyou_isv_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.creator IS '创建者ID';


--
-- Name: COLUMN fuyou_isv_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.create_time IS '创建时间';


--
-- Name: COLUMN fuyou_isv_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN fuyou_isv_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN fuyou_isv_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.version IS '版本号(乐观锁)';


--
-- Name: COLUMN fuyou_isv_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.fuyou_isv_key_config.deleted IS '删除标志';


--
-- Name: hkrt_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hkrt_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(64) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(64) NOT NULL,
    merch_no character varying(64),
    pn character varying(64),
    deleted boolean DEFAULT false NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE hkrt_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.hkrt_isv_channel_merchant IS '海科融通通道商户绑定';


--
-- Name: COLUMN hkrt_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN hkrt_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.mch_no IS '平台商户号';


--
-- Name: COLUMN hkrt_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.channel_mch_no IS '通道商户号(平台生成的唯一标识, HKRT+雪花)';


--
-- Name: COLUMN hkrt_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code, 如 hkrt_pay)';


--
-- Name: COLUMN hkrt_isv_channel_merchant.merch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.merch_no IS '海科商户号(merch_no)';


--
-- Name: COLUMN hkrt_isv_channel_merchant.pn; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.pn IS 'SAAS 终端号(pn)';


--
-- Name: COLUMN hkrt_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.deleted IS '删除标志';


--
-- Name: COLUMN hkrt_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.last_modifier IS '最后修改者';


--
-- Name: COLUMN hkrt_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN hkrt_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN hkrt_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.creator IS '创建者';


--
-- Name: COLUMN hkrt_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN hkrt_isv_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: hkrt_isv_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hkrt_isv_key_config (
    id bigint NOT NULL,
    product character varying(64) NOT NULL,
    agent_no character varying(64),
    access_id character varying(128),
    access_key text,
    wx_app_id character varying(64),
    deleted boolean DEFAULT false NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE hkrt_isv_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.hkrt_isv_key_config IS '海科融通服务商密钥配置';


--
-- Name: COLUMN hkrt_isv_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.id IS '主键';


--
-- Name: COLUMN hkrt_isv_key_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.product IS '产品编码(对应 ProductEnum.code, 如 hkrt_pay)';


--
-- Name: COLUMN hkrt_isv_key_config.agent_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.agent_no IS '服务商编号(agent_no)';


--
-- Name: COLUMN hkrt_isv_key_config.access_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.access_id IS '接入机构标识(access_id)';


--
-- Name: COLUMN hkrt_isv_key_config.access_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.access_key IS '签名密钥(access_key, MD5 大写签名, 加密存储)';


--
-- Name: COLUMN hkrt_isv_key_config.wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.wx_app_id IS '微信公众号 AppId(JSAPI 场景透传, 可选)';


--
-- Name: COLUMN hkrt_isv_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.deleted IS '删除标志';


--
-- Name: COLUMN hkrt_isv_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.last_modifier IS '最后修改者';


--
-- Name: COLUMN hkrt_isv_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN hkrt_isv_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN hkrt_isv_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.creator IS '创建者';


--
-- Name: COLUMN hkrt_isv_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.create_time IS '创建时间';


--
-- Name: COLUMN hkrt_isv_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hkrt_isv_key_config.sandbox IS '是否沙箱环境';


--
-- Name: hmpay_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hmpay_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    product character varying(32),
    merchant_no character varying(64),
    store_id character varying(64),
    wx_app_id character varying(64),
    wx_channel_auth boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE hmpay_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.hmpay_isv_channel_merchant IS '河马付通道商户绑定';


--
-- Name: COLUMN hmpay_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN hmpay_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN hmpay_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.channel_mch_no IS '通道商户号(HMPAY+雪花)';


--
-- Name: COLUMN hmpay_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code, 如 hm_pay)';


--
-- Name: COLUMN hmpay_isv_channel_merchant.merchant_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.merchant_no IS '杉德商户编号(merchantNo / sub_app_id)';


--
-- Name: COLUMN hmpay_isv_channel_merchant.store_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.store_id IS '门店号(storeId)';


--
-- Name: COLUMN hmpay_isv_channel_merchant.wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.wx_app_id IS '微信应用ID(公众号/小程序 appId, 用于微信 JSAPI/小程序支付)';


--
-- Name: COLUMN hmpay_isv_channel_merchant.wx_channel_auth; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.wx_channel_auth IS '是否使用通道渠道认证(微信服务商授权模式)';


--
-- Name: COLUMN hmpay_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.creator IS '创建者ID';


--
-- Name: COLUMN hmpay_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN hmpay_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN hmpay_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN hmpay_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.version IS '版本号(乐观锁)';


--
-- Name: COLUMN hmpay_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.deleted IS '删除标志';


--
-- Name: COLUMN hmpay_isv_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: hmpay_isv_channel_merchant_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hmpay_isv_channel_merchant_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hmpay_isv_channel_merchant_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hmpay_isv_channel_merchant_id_seq OWNED BY public.hmpay_isv_channel_merchant.id;


--
-- Name: hmpay_isv_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hmpay_isv_key_config (
    id bigint NOT NULL,
    product character varying(32),
    sand_app_id character varying(64),
    private_key text,
    public_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE hmpay_isv_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.hmpay_isv_key_config IS '河马付服务商密钥配置';


--
-- Name: COLUMN hmpay_isv_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.id IS '主键';


--
-- Name: COLUMN hmpay_isv_key_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.product IS '产品编码(对应 ProductEnum.code, 如 hm_pay)';


--
-- Name: COLUMN hmpay_isv_key_config.sand_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.sand_app_id IS '杉德代理号(sandAppId / app_id)';


--
-- Name: COLUMN hmpay_isv_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.private_key IS '商户RSA私钥(PKCS#8 Base64, 加密存储, 签名用)';


--
-- Name: COLUMN hmpay_isv_key_config.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.public_key IS '杉德RSA公钥(X509 Base64, 加密存储, 回调/响应验签用)';


--
-- Name: COLUMN hmpay_isv_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.creator IS '创建者ID';


--
-- Name: COLUMN hmpay_isv_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.create_time IS '创建时间';


--
-- Name: COLUMN hmpay_isv_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN hmpay_isv_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN hmpay_isv_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.version IS '版本号(乐观锁)';


--
-- Name: COLUMN hmpay_isv_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.deleted IS '删除标志';


--
-- Name: COLUMN hmpay_isv_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.hmpay_isv_key_config.sandbox IS '是否沙箱环境';


--
-- Name: hmpay_isv_key_config_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hmpay_isv_key_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: hmpay_isv_key_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hmpay_isv_key_config_id_seq OWNED BY public.hmpay_isv_key_config.id;


--
-- Name: iam_perm_code; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_perm_code (
    id bigint NOT NULL,
    code character varying(100) NOT NULL,
    menu_code character varying(100),
    internal boolean DEFAULT false,
    remark character varying(500),
    creator bigint,
    last_modifier bigint,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    create_time timestamp with time zone,
    last_modified_time timestamp with time zone,
    i18n_key character varying(200)
);


--
-- Name: TABLE iam_perm_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_perm_code IS '权限码';


--
-- Name: COLUMN iam_perm_code.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.id IS '主键';


--
-- Name: COLUMN iam_perm_code.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.code IS '权限码编码';


--
-- Name: COLUMN iam_perm_code.menu_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.menu_code IS '菜单编码';


--
-- Name: COLUMN iam_perm_code.internal; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.internal IS '是否系统内置';


--
-- Name: COLUMN iam_perm_code.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.remark IS '备注';


--
-- Name: COLUMN iam_perm_code.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.creator IS '创建者ID';


--
-- Name: COLUMN iam_perm_code.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.last_modifier IS '最后修改ID';


--
-- Name: COLUMN iam_perm_code.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.version IS '版本号';


--
-- Name: COLUMN iam_perm_code.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.deleted IS '删除标志';


--
-- Name: COLUMN iam_perm_code.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.create_time IS '创建时间';


--
-- Name: COLUMN iam_perm_code.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_perm_code.i18n_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_code.i18n_key IS '国际化key（由 code 推导: perm.{code}）';


--
-- Name: iam_perm_menu; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_perm_menu (
    id bigint NOT NULL,
    pid bigint,
    menu_code character varying(100),
    client_code character varying(100),
    name character varying(200),
    i18n_key character varying(200),
    icon character varying(200),
    hidden boolean DEFAULT false,
    hide_children_menu boolean DEFAULT false,
    component character varying(500),
    path character varying(500),
    redirect character varying(500),
    sort_no double precision,
    root boolean DEFAULT false,
    keep_alive boolean DEFAULT false,
    affix_tab boolean DEFAULT false,
    creator bigint,
    last_modifier bigint,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    menu_type character varying(20),
    active_icon character varying(100),
    badge character varying(50),
    badge_type character varying(20),
    badge_variants character varying(50),
    iframe_src character varying(500),
    link character varying(500),
    create_time timestamp(6) with time zone,
    last_modified_time timestamp(6) with time zone
);


--
-- Name: TABLE iam_perm_menu; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_perm_menu IS '菜单权限配置';


--
-- Name: COLUMN iam_perm_menu.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.id IS '主键';


--
-- Name: COLUMN iam_perm_menu.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.pid IS '父菜单ID,0表示根菜单';


--
-- Name: COLUMN iam_perm_menu.menu_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.menu_code IS '菜单编码';


--
-- Name: COLUMN iam_perm_menu.client_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.client_code IS '关联终端code';


--
-- Name: COLUMN iam_perm_menu.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.name IS '路由名称，建议唯一';


--
-- Name: COLUMN iam_perm_menu.i18n_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.i18n_key IS '国际化key';


--
-- Name: COLUMN iam_perm_menu.icon; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.icon IS '菜单图标';


--
-- Name: COLUMN iam_perm_menu.hidden; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.hidden IS '是否隐藏';


--
-- Name: COLUMN iam_perm_menu.hide_children_menu; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.hide_children_menu IS '是否隐藏子菜单';


--
-- Name: COLUMN iam_perm_menu.component; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.component IS '组件';


--
-- Name: COLUMN iam_perm_menu.path; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.path IS '访问路径';


--
-- Name: COLUMN iam_perm_menu.redirect; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.redirect IS '菜单跳转地址(重定向)';


--
-- Name: COLUMN iam_perm_menu.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.sort_no IS '菜单排序';


--
-- Name: COLUMN iam_perm_menu.root; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.root IS '是否是一级菜单';


--
-- Name: COLUMN iam_perm_menu.keep_alive; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.keep_alive IS '是否开启页面缓存';


--
-- Name: COLUMN iam_perm_menu.affix_tab; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.affix_tab IS '是否固定标签页';


--
-- Name: COLUMN iam_perm_menu.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.creator IS '创建者ID';


--
-- Name: COLUMN iam_perm_menu.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.last_modifier IS '最后修改ID';


--
-- Name: COLUMN iam_perm_menu.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.version IS '版本号';


--
-- Name: COLUMN iam_perm_menu.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.deleted IS '删除标志';


--
-- Name: COLUMN iam_perm_menu.menu_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.menu_type IS '菜单类型';


--
-- Name: COLUMN iam_perm_menu.active_icon; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.active_icon IS '激活状态图标';


--
-- Name: COLUMN iam_perm_menu.badge; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.badge IS '徽章显示文本';


--
-- Name: COLUMN iam_perm_menu.badge_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.badge_type IS '徽章类型';


--
-- Name: COLUMN iam_perm_menu.badge_variants; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.badge_variants IS '徽章样式变体';


--
-- Name: COLUMN iam_perm_menu.iframe_src; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.iframe_src IS '内嵌页面URL地址';


--
-- Name: COLUMN iam_perm_menu.link; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.link IS '外部链接URL地址';


--
-- Name: COLUMN iam_perm_menu.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.create_time IS '创建时间';


--
-- Name: COLUMN iam_perm_menu.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_perm_menu.last_modified_time IS '最后修改时间';


--
-- Name: iam_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_role (
    id bigint NOT NULL,
    code character varying(100) NOT NULL,
    client_code character varying(100) NOT NULL,
    data_scope character varying(50),
    internal boolean DEFAULT false,
    remark character varying(500),
    creator bigint,
    last_modifier bigint,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    create_time timestamp with time zone,
    last_modified_time timestamp with time zone,
    i18n_key character varying(200)
);


--
-- Name: TABLE iam_role; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_role IS '角色';


--
-- Name: COLUMN iam_role.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.id IS '主键';


--
-- Name: COLUMN iam_role.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.code IS '角色编码';


--
-- Name: COLUMN iam_role.client_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.client_code IS '终端编码';


--
-- Name: COLUMN iam_role.data_scope; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.data_scope IS '数据权限范围';


--
-- Name: COLUMN iam_role.internal; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.internal IS '是否系统内置';


--
-- Name: COLUMN iam_role.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.remark IS '备注';


--
-- Name: COLUMN iam_role.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.creator IS '创建者ID';


--
-- Name: COLUMN iam_role.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.last_modifier IS '最后修改ID';


--
-- Name: COLUMN iam_role.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.version IS '版本号';


--
-- Name: COLUMN iam_role.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.deleted IS '删除标志';


--
-- Name: COLUMN iam_role.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.create_time IS '创建时间';


--
-- Name: COLUMN iam_role.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_role.i18n_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role.i18n_key IS '国际化key（有值时走语言包翻译）';


--
-- Name: iam_role_code; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_role_code (
    id bigint NOT NULL,
    role_id bigint NOT NULL,
    code_id bigint NOT NULL
);


--
-- Name: TABLE iam_role_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_role_code IS '角色权限码关系';


--
-- Name: COLUMN iam_role_code.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role_code.id IS '主键';


--
-- Name: COLUMN iam_role_code.role_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role_code.role_id IS '角色ID';


--
-- Name: COLUMN iam_role_code.code_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role_code.code_id IS '权限码ID';


--
-- Name: iam_role_menu; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_role_menu (
    id bigint NOT NULL,
    role_id bigint NOT NULL,
    client_code character varying(50),
    menu_id bigint NOT NULL
);


--
-- Name: TABLE iam_role_menu; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_role_menu IS '角色-菜单关联表';


--
-- Name: COLUMN iam_role_menu.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role_menu.id IS '主键';


--
-- Name: COLUMN iam_role_menu.role_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role_menu.role_id IS '角色ID';


--
-- Name: COLUMN iam_role_menu.client_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role_menu.client_code IS '终端编码: ADMIN/ISV/AGENT/MCH';


--
-- Name: COLUMN iam_role_menu.menu_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_role_menu.menu_id IS '菜单ID';


--
-- Name: iam_social_login_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_social_login_config (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    source character varying(32) NOT NULL,
    client_id character varying(128),
    client_secret character varying(256),
    extra jsonb DEFAULT '{}'::jsonb,
    configured boolean DEFAULT false,
    enabled boolean DEFAULT false
);


--
-- Name: TABLE iam_social_login_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_social_login_config IS '第三方平台登录配置表';


--
-- Name: COLUMN iam_social_login_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.id IS '主键';


--
-- Name: COLUMN iam_social_login_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.creator IS '创建人';


--
-- Name: COLUMN iam_social_login_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.create_time IS '创建时间';


--
-- Name: COLUMN iam_social_login_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN iam_social_login_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_social_login_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.version IS '乐观锁';


--
-- Name: COLUMN iam_social_login_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.deleted IS '逻辑删除';


--
-- Name: COLUMN iam_social_login_config.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.source IS '平台编码';


--
-- Name: COLUMN iam_social_login_config.client_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.client_id IS '客户端ID';


--
-- Name: COLUMN iam_social_login_config.client_secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.client_secret IS '客户端密钥';


--
-- Name: COLUMN iam_social_login_config.extra; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.extra IS '平台特有配置';


--
-- Name: COLUMN iam_social_login_config.configured; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.configured IS '是否已完成配置';


--
-- Name: COLUMN iam_social_login_config.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_social_login_config.enabled IS '是否启用';


--
-- Name: iam_user_dashboard_preference; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_dashboard_preference (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    client_code character varying(32) NOT NULL,
    entries jsonb DEFAULT '[]'::jsonb NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    terminal character varying(16) DEFAULT 'web'::character varying NOT NULL
);


--
-- Name: TABLE iam_user_dashboard_preference; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_dashboard_preference IS '用户工作台快捷入口偏好';


--
-- Name: COLUMN iam_user_dashboard_preference.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.id IS '主键';


--
-- Name: COLUMN iam_user_dashboard_preference.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.user_id IS '用户ID';


--
-- Name: COLUMN iam_user_dashboard_preference.client_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.client_code IS '终端编码(WEB/MOBILE), PC与移动分开管理';


--
-- Name: COLUMN iam_user_dashboard_preference.entries; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.entries IS '已选快捷入口有序序列(纯key数组), 如 ["merchant","notify"]';


--
-- Name: COLUMN iam_user_dashboard_preference.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.creator IS '创建人ID';


--
-- Name: COLUMN iam_user_dashboard_preference.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.create_time IS '创建时间';


--
-- Name: COLUMN iam_user_dashboard_preference.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN iam_user_dashboard_preference.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_user_dashboard_preference.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.version IS '乐观锁版本号';


--
-- Name: COLUMN iam_user_dashboard_preference.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.deleted IS '逻辑删除标志';


--
-- Name: COLUMN iam_user_dashboard_preference.terminal; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_dashboard_preference.terminal IS '请求终端(壳维度): web=PC Web端 / app=移动管理端, 与client_code正交, 同身份域下PC与App各存一份';


--
-- Name: iam_user_expand_info; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_expand_info (
    id bigint NOT NULL,
    sex character varying(10),
    avatar character varying(500),
    birthday date,
    last_login_ip character varying(100),
    login_count integer,
    register_source character varying(100),
    register_channel character varying(100),
    creator bigint,
    last_modifier bigint,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    create_time timestamp with time zone,
    last_modified_time timestamp with time zone,
    last_login_time timestamp with time zone,
    register_time timestamp with time zone,
    current_login_time timestamp with time zone
);


--
-- Name: TABLE iam_user_expand_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_expand_info IS '用户扩展信息';


--
-- Name: COLUMN iam_user_expand_info.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.id IS '主键';


--
-- Name: COLUMN iam_user_expand_info.sex; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.sex IS '性别';


--
-- Name: COLUMN iam_user_expand_info.avatar; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.avatar IS '头像图片ID';


--
-- Name: COLUMN iam_user_expand_info.birthday; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.birthday IS '生日';


--
-- Name: COLUMN iam_user_expand_info.last_login_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.last_login_ip IS '最后登录IP';


--
-- Name: COLUMN iam_user_expand_info.login_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.login_count IS '登录次数';


--
-- Name: COLUMN iam_user_expand_info.register_source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.register_source IS '注册来源';


--
-- Name: COLUMN iam_user_expand_info.register_channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.register_channel IS '注册渠道';


--
-- Name: COLUMN iam_user_expand_info.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.creator IS '创建者ID';


--
-- Name: COLUMN iam_user_expand_info.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.last_modifier IS '最后修改ID';


--
-- Name: COLUMN iam_user_expand_info.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.version IS '版本号';


--
-- Name: COLUMN iam_user_expand_info.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.deleted IS '删除标志';


--
-- Name: COLUMN iam_user_expand_info.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.create_time IS '创建时间';


--
-- Name: COLUMN iam_user_expand_info.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_user_expand_info.last_login_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.last_login_time IS '上次登录时间';


--
-- Name: COLUMN iam_user_expand_info.register_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.register_time IS '注册时间';


--
-- Name: COLUMN iam_user_expand_info.current_login_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_expand_info.current_login_time IS '本次登录时间';


--
-- Name: iam_user_info; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_info (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    client_code character varying(100) NOT NULL,
    account character varying(100) NOT NULL,
    password character varying(200) NOT NULL,
    phone character varying(50),
    email character varying(100),
    administrator boolean DEFAULT false,
    status character varying(20) DEFAULT 'normal'::character varying,
    creator bigint,
    last_modifier bigint,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    create_time timestamp with time zone,
    last_modified_time timestamp with time zone
);


--
-- Name: TABLE iam_user_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_info IS '用户核心信息';


--
-- Name: COLUMN iam_user_info.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.id IS '主键';


--
-- Name: COLUMN iam_user_info.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.name IS '名称';


--
-- Name: COLUMN iam_user_info.client_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.client_code IS '终端编码';


--
-- Name: COLUMN iam_user_info.account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.account IS '账号';


--
-- Name: COLUMN iam_user_info.password; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.password IS '密码';


--
-- Name: COLUMN iam_user_info.phone; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.phone IS '手机号';


--
-- Name: COLUMN iam_user_info.email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.email IS '邮箱';


--
-- Name: COLUMN iam_user_info.administrator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.administrator IS '是否管理员';


--
-- Name: COLUMN iam_user_info.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.status IS '账号状态';


--
-- Name: COLUMN iam_user_info.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.creator IS '创建者ID';


--
-- Name: COLUMN iam_user_info.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.last_modifier IS '最后修改ID';


--
-- Name: COLUMN iam_user_info.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.version IS '版本号';


--
-- Name: COLUMN iam_user_info.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.deleted IS '删除标志';


--
-- Name: COLUMN iam_user_info.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.create_time IS '创建时间';


--
-- Name: COLUMN iam_user_info.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_info.last_modified_time IS '最后修改时间';


--
-- Name: iam_user_passkey; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_passkey (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    client_code character varying(32) NOT NULL,
    credential_id character varying(512) NOT NULL,
    public_key character varying(1024) NOT NULL,
    sign_count bigint DEFAULT 0 NOT NULL,
    device_name character varying(128),
    transports character varying(128),
    backup_eligible boolean DEFAULT false NOT NULL,
    backup_state boolean DEFAULT false NOT NULL,
    last_used_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE iam_user_passkey; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_passkey IS '用户通行密钥(WebAuthn 凭据)绑定记录';


--
-- Name: COLUMN iam_user_passkey.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.id IS '主键';


--
-- Name: COLUMN iam_user_passkey.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.user_id IS '用户ID(关联 iam_user_info.id)';


--
-- Name: COLUMN iam_user_passkey.client_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.client_code IS '终端编码(admin/merchant)';


--
-- Name: COLUMN iam_user_passkey.credential_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.credential_id IS 'WebAuthn 凭据ID(base64url)';


--
-- Name: COLUMN iam_user_passkey.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.public_key IS 'COSE 公钥(base64url)';


--
-- Name: COLUMN iam_user_passkey.sign_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.sign_count IS '签名计数(防认证器克隆)';


--
-- Name: COLUMN iam_user_passkey.device_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.device_name IS '设备可辨识名(用户自定义)';


--
-- Name: COLUMN iam_user_passkey.transports; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.transports IS '凭据传输方式(internal/hybrid/usb/nfc/ble, 逗号分隔)';


--
-- Name: COLUMN iam_user_passkey.backup_eligible; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.backup_eligible IS '凭据是否可多设备同步(passkey)';


--
-- Name: COLUMN iam_user_passkey.backup_state; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.backup_state IS '凭据当前是否处于同步状态';


--
-- Name: COLUMN iam_user_passkey.last_used_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.last_used_time IS '最后使用时间';


--
-- Name: COLUMN iam_user_passkey.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.creator IS '创建者ID';


--
-- Name: COLUMN iam_user_passkey.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.create_time IS '创建时间';


--
-- Name: COLUMN iam_user_passkey.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN iam_user_passkey.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_user_passkey.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.version IS '版本号(乐观锁)';


--
-- Name: COLUMN iam_user_passkey.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_passkey.deleted IS '逻辑删除标志';


--
-- Name: iam_user_password_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_password_history (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    password character varying(200) NOT NULL,
    creator bigint,
    create_time timestamp with time zone
);


--
-- Name: TABLE iam_user_password_history; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_password_history IS '用户密码历史表';


--
-- Name: COLUMN iam_user_password_history.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_history.id IS '主键';


--
-- Name: COLUMN iam_user_password_history.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_history.user_id IS '用户ID';


--
-- Name: COLUMN iam_user_password_history.password; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_history.password IS '密码';


--
-- Name: COLUMN iam_user_password_history.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_history.creator IS '创建者ID';


--
-- Name: COLUMN iam_user_password_history.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_history.create_time IS '创建时间';


--
-- Name: iam_user_password_security; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_password_security (
    id bigint NOT NULL,
    password_error_count integer,
    lock_time timestamp(6) with time zone,
    password_expire_time timestamp(6) with time zone,
    last_change_password_time timestamp(6) with time zone,
    initial_password boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    last_failure_time timestamp with time zone
);


--
-- Name: TABLE iam_user_password_security; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_password_security IS '用户密码安全信息';


--
-- Name: COLUMN iam_user_password_security.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.id IS '主键';


--
-- Name: COLUMN iam_user_password_security.password_error_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.password_error_count IS '密码错误次数';


--
-- Name: COLUMN iam_user_password_security.lock_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.lock_time IS '锁定结束时间';


--
-- Name: COLUMN iam_user_password_security.password_expire_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.password_expire_time IS '密码过期时间';


--
-- Name: COLUMN iam_user_password_security.last_change_password_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.last_change_password_time IS '上次修改密码时间';


--
-- Name: COLUMN iam_user_password_security.initial_password; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.initial_password IS '是否初始密码';


--
-- Name: COLUMN iam_user_password_security.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.creator IS '创建者ID';


--
-- Name: COLUMN iam_user_password_security.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.create_time IS '创建时间';


--
-- Name: COLUMN iam_user_password_security.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.last_modifier IS '最后修改ID';


--
-- Name: COLUMN iam_user_password_security.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_user_password_security.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.version IS '版本号';


--
-- Name: COLUMN iam_user_password_security.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.deleted IS '删除标志';


--
-- Name: COLUMN iam_user_password_security.last_failure_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_password_security.last_failure_time IS '上次登录失败时间';


--
-- Name: iam_user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_role (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- Name: TABLE iam_user_role; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_role IS '用户角色关系';


--
-- Name: COLUMN iam_user_role.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_role.id IS '主键';


--
-- Name: COLUMN iam_user_role.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_role.user_id IS '用户ID';


--
-- Name: COLUMN iam_user_role.role_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_role.role_id IS '角色ID';


--
-- Name: iam_user_social; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_social (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    client_code character varying(32) DEFAULT 'admin'::character varying NOT NULL,
    source character varying(32) NOT NULL,
    open_id character varying(128) NOT NULL,
    username character varying(128),
    avatar character varying(512),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE iam_user_social; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_social IS '用户第三方账号绑定';


--
-- Name: COLUMN iam_user_social.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.id IS '主键';


--
-- Name: COLUMN iam_user_social.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.user_id IS '本地用户ID(关联 iam_user_info.id)';


--
-- Name: COLUMN iam_user_social.client_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.client_code IS '终端编码(admin/merchant)';


--
-- Name: COLUMN iam_user_social.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.source IS '平台编码(weChat/weCom/qq/github/gitee/feishu/dingTalk)';


--
-- Name: COLUMN iam_user_social.open_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.open_id IS '平台用户唯一标识(openid/uuid)';


--
-- Name: COLUMN iam_user_social.username; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.username IS '平台昵称';


--
-- Name: COLUMN iam_user_social.avatar; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.avatar IS '平台头像';


--
-- Name: COLUMN iam_user_social.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.creator IS '创建者ID';


--
-- Name: COLUMN iam_user_social.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.create_time IS '创建时间';


--
-- Name: COLUMN iam_user_social.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN iam_user_social.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_user_social.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.version IS '版本号(乐观锁)';


--
-- Name: COLUMN iam_user_social.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_social.deleted IS '删除标志(逻辑删除)';


--
-- Name: iam_user_two_factor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iam_user_two_factor (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    secret character varying(512) NOT NULL,
    backup_codes jsonb,
    backup_codes_remaining integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE iam_user_two_factor; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.iam_user_two_factor IS '用户双因素认证绑定记录';


--
-- Name: COLUMN iam_user_two_factor.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.id IS '主键';


--
-- Name: COLUMN iam_user_two_factor.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.user_id IS '用户ID';


--
-- Name: COLUMN iam_user_two_factor.secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.secret IS 'TOTP 密钥';


--
-- Name: COLUMN iam_user_two_factor.backup_codes; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.backup_codes IS '备用验证码';


--
-- Name: COLUMN iam_user_two_factor.backup_codes_remaining; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.backup_codes_remaining IS '剩余可用备用验证码数量';


--
-- Name: COLUMN iam_user_two_factor.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.creator IS '创建人ID';


--
-- Name: COLUMN iam_user_two_factor.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.create_time IS '创建时间';


--
-- Name: COLUMN iam_user_two_factor.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN iam_user_two_factor.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN iam_user_two_factor.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.version IS '版本号(乐观锁)';


--
-- Name: COLUMN iam_user_two_factor.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.iam_user_two_factor.deleted IS '逻辑删除标志';


--
-- Name: lakala_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lakala_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    lakala_mch_no character varying(64) NOT NULL,
    term_no character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE lakala_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.lakala_isv_channel_merchant IS '拉卡拉通道商户绑定';


--
-- Name: COLUMN lakala_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN lakala_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.mch_no IS '平台商户号';


--
-- Name: COLUMN lakala_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.channel_mch_no IS '通道商户号(LAKALA+雪花)';


--
-- Name: COLUMN lakala_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.product IS '所属支付产品';


--
-- Name: COLUMN lakala_isv_channel_merchant.lakala_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.lakala_mch_no IS '拉卡拉商户编号(merchantNo)';


--
-- Name: COLUMN lakala_isv_channel_merchant.term_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.term_no IS '终端号';


--
-- Name: COLUMN lakala_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.creator IS '创建人ID';


--
-- Name: COLUMN lakala_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN lakala_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN lakala_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN lakala_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN lakala_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.deleted IS '逻辑删除标志';


--
-- Name: COLUMN lakala_isv_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: lakala_isv_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.lakala_isv_key_config (
    id bigint NOT NULL,
    product character varying(32) NOT NULL,
    lkl_app_id character varying(64),
    mch_serial_no character varying(128),
    private_key text,
    public_key text,
    sm4_key character varying(128),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    org_code character varying(64),
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE lakala_isv_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.lakala_isv_key_config IS '拉卡拉服务商密钥配置(全局唯一)';


--
-- Name: COLUMN lakala_isv_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.id IS '主键';


--
-- Name: COLUMN lakala_isv_key_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.product IS '产品编码 @see ProductEnum';


--
-- Name: COLUMN lakala_isv_key_config.lkl_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.lkl_app_id IS '拉卡拉应用编号';


--
-- Name: COLUMN lakala_isv_key_config.mch_serial_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.mch_serial_no IS '商户证书序列号';


--
-- Name: COLUMN lakala_isv_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.private_key IS '商户RSA私钥PEM(加密存储)';


--
-- Name: COLUMN lakala_isv_key_config.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.public_key IS '拉卡拉RSA公钥PEM(加密存储)';


--
-- Name: COLUMN lakala_isv_key_config.sm4_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.sm4_key IS 'SM4对称密钥(加密存储, 进件敏感字段加密用)';


--
-- Name: COLUMN lakala_isv_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.creator IS '创建人ID';


--
-- Name: COLUMN lakala_isv_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.create_time IS '创建时间';


--
-- Name: COLUMN lakala_isv_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN lakala_isv_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN lakala_isv_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN lakala_isv_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.deleted IS '逻辑删除标志';


--
-- Name: COLUMN lakala_isv_key_config.org_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.org_code IS '机构代码';


--
-- Name: COLUMN lakala_isv_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.lakala_isv_key_config.sandbox IS '是否沙箱环境';


--
-- Name: leshua_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.leshua_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    product character varying(32),
    sandbox boolean DEFAULT false,
    ls_mch_no character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE leshua_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.leshua_isv_channel_merchant IS '乐刷通道商户绑定';


--
-- Name: COLUMN leshua_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN leshua_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN leshua_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.channel_mch_no IS '通道商户号(LESHUA+雪花)';


--
-- Name: COLUMN leshua_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code)';


--
-- Name: COLUMN leshua_isv_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: COLUMN leshua_isv_channel_merchant.ls_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.ls_mch_no IS '乐刷商户编号(merchant_id)';


--
-- Name: COLUMN leshua_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.creator IS '创建者ID';


--
-- Name: COLUMN leshua_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN leshua_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN leshua_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN leshua_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.version IS '版本号(乐观锁)';


--
-- Name: COLUMN leshua_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_channel_merchant.deleted IS '删除标志';


--
-- Name: leshua_isv_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.leshua_isv_key_config (
    id bigint NOT NULL,
    product character varying(32) NOT NULL,
    ls_mch_no character varying(64),
    trade_key text,
    notify_key text,
    sign_type character varying(16),
    ls_isv_no character varying(64),
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE leshua_isv_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.leshua_isv_key_config IS '乐刷服务商密钥配置';


--
-- Name: COLUMN leshua_isv_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.id IS '主键';


--
-- Name: COLUMN leshua_isv_key_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.product IS '产品编码(对应 ProductEnum.code, 如 leshua_pay)';


--
-- Name: COLUMN leshua_isv_key_config.ls_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.ls_mch_no IS '乐刷商户号(merchant_id, 服务商级或商户级, 全局唯一)';


--
-- Name: COLUMN leshua_isv_key_config.trade_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.trade_key IS '交易密钥(tradeKey, 请求签名与响应/回调验签, 加密存储)';


--
-- Name: COLUMN leshua_isv_key_config.notify_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.notify_key IS '异步通知密钥(notifyKey, 部分场景回调验签, 加密存储)';


--
-- Name: COLUMN leshua_isv_key_config.sign_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.sign_type IS '签名类型(MD5 / SM3)';


--
-- Name: COLUMN leshua_isv_key_config.ls_isv_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.ls_isv_no IS '乐刷服务商号(lsIsvNo, 进件场景使用, 可选)';


--
-- Name: COLUMN leshua_isv_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.sandbox IS '是否沙箱环境';


--
-- Name: COLUMN leshua_isv_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.creator IS '创建者ID';


--
-- Name: COLUMN leshua_isv_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.create_time IS '创建时间';


--
-- Name: COLUMN leshua_isv_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN leshua_isv_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN leshua_isv_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.version IS '版本号(乐观锁)';


--
-- Name: COLUMN leshua_isv_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.leshua_isv_key_config.deleted IS '删除标志';


--
-- Name: mch_app_info; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_app_info (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(32) NOT NULL,
    app_name character varying(64) NOT NULL,
    status character varying(32) NOT NULL,
    default_app boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false
);


--
-- Name: TABLE mch_app_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_app_info IS '商户应用信息';


--
-- Name: COLUMN mch_app_info.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.id IS '主键';


--
-- Name: COLUMN mch_app_info.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.mch_no IS '商户号';


--
-- Name: COLUMN mch_app_info.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.app_id IS '应用号';


--
-- Name: COLUMN mch_app_info.app_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.app_name IS '应用名称';


--
-- Name: COLUMN mch_app_info.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.status IS '应用状态，字典 mch_app_status';


--
-- Name: COLUMN mch_app_info.default_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.default_app IS '是否默认应用';


--
-- Name: COLUMN mch_app_info.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.creator IS '创建者';


--
-- Name: COLUMN mch_app_info.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.create_time IS '创建时间';


--
-- Name: COLUMN mch_app_info.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.last_modifier IS '最后修改者';


--
-- Name: COLUMN mch_app_info.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_app_info.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.version IS '版本号';


--
-- Name: COLUMN mch_app_info.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_info.deleted IS '逻辑删除';


--
-- Name: mch_app_notify_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_app_notify_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(32) NOT NULL,
    notify_url character varying(255),
    notify_way character varying(20) DEFAULT 'http'::character varying,
    subscribed_events character varying(100),
    status boolean DEFAULT false,
    remark character varying(255),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE mch_app_notify_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_app_notify_config IS '商户应用事件通知配置(应用级,通用事件订阅,与支付订单级回调并行)';


--
-- Name: COLUMN mch_app_notify_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.id IS '主键';


--
-- Name: COLUMN mch_app_notify_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.mch_no IS '商户号';


--
-- Name: COLUMN mch_app_notify_config.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.app_id IS '应用ID';


--
-- Name: COLUMN mch_app_notify_config.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.notify_url IS '回调地址(https, notifyWay=http 时生效)';


--
-- Name: COLUMN mch_app_notify_config.notify_way; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.notify_way IS '传输通道';


--
-- Name: COLUMN mch_app_notify_config.subscribed_events; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.subscribed_events IS '订阅事件类型(逗号分隔, 支持前缀匹配: pay 匹配 pay.*, refund 匹配 refund.*)';


--
-- Name: COLUMN mch_app_notify_config.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.status IS '启用状态(true-启用 false-禁用)';


--
-- Name: COLUMN mch_app_notify_config.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.remark IS '备注';


--
-- Name: COLUMN mch_app_notify_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.creator IS '创建者ID';


--
-- Name: COLUMN mch_app_notify_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.create_time IS '创建时间';


--
-- Name: COLUMN mch_app_notify_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN mch_app_notify_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_app_notify_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.version IS '乐观锁版本号';


--
-- Name: COLUMN mch_app_notify_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_app_notify_config.deleted IS '逻辑删除标志';


--
-- Name: mch_app_notify_config_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.mch_app_notify_config_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: mch_app_notify_config_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.mch_app_notify_config_id_seq OWNED BY public.mch_app_notify_config.id;


--
-- Name: mch_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(32),
    channel_merchant_name character varying(200),
    product character varying(32),
    enable boolean DEFAULT false,
    source character varying(32),
    apply_id bigint,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false NOT NULL,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE mch_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_channel_merchant IS '通道商户信息';


--
-- Name: COLUMN mch_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.id IS '主键';


--
-- Name: COLUMN mch_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN mch_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN mch_channel_merchant.channel_merchant_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.channel_merchant_name IS '商户名称';


--
-- Name: COLUMN mch_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.product IS '所属支付产品';


--
-- Name: COLUMN mch_channel_merchant.enable; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.enable IS '是否启用';


--
-- Name: COLUMN mch_channel_merchant.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.source IS '创建来源';


--
-- Name: COLUMN mch_channel_merchant.apply_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.apply_id IS '申请单ID';


--
-- Name: COLUMN mch_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.creator IS '创建者ID';


--
-- Name: COLUMN mch_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN mch_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.last_modifier IS '最后修改ID';


--
-- Name: COLUMN mch_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.version IS '版本号';


--
-- Name: COLUMN mch_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.deleted IS '删除标志';


--
-- Name: COLUMN mch_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: mch_credential; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_credential (
    id bigint NOT NULL,
    mch_no character varying(32),
    public_key text,
    secret_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false
);


--
-- Name: TABLE mch_credential; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_credential IS '商户对接配置';


--
-- Name: COLUMN mch_credential.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.id IS '主键';


--
-- Name: COLUMN mch_credential.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.mch_no IS '商户号';


--
-- Name: COLUMN mch_credential.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.public_key IS '商户公钥(加密存储)';


--
-- Name: COLUMN mch_credential.secret_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.secret_key IS '通信密钥(加密存储)';


--
-- Name: COLUMN mch_credential.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.creator IS '创建者';


--
-- Name: COLUMN mch_credential.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.create_time IS '创建时间';


--
-- Name: COLUMN mch_credential.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.last_modifier IS '最后修改者';


--
-- Name: COLUMN mch_credential.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_credential.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.version IS '版本号';


--
-- Name: COLUMN mch_credential.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_credential.deleted IS '逻辑删除';


--
-- Name: mch_info; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_info (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    mch_name character varying(128),
    mch_short_name character varying(64),
    admin_user_id bigint,
    status character varying(32),
    subject_type character varying(32),
    deleted boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE mch_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_info IS '商户信息表';


--
-- Name: COLUMN mch_info.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.id IS '主键';


--
-- Name: COLUMN mch_info.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.mch_no IS '商户号';


--
-- Name: COLUMN mch_info.mch_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.mch_name IS '商户名称';


--
-- Name: COLUMN mch_info.mch_short_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.mch_short_name IS '商户简称';


--
-- Name: COLUMN mch_info.admin_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.admin_user_id IS '关联管理员用户ID';


--
-- Name: COLUMN mch_info.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.status IS '状态';


--
-- Name: COLUMN mch_info.subject_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.subject_type IS '主体类型';


--
-- Name: COLUMN mch_info.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.deleted IS '删除标志';


--
-- Name: COLUMN mch_info.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.creator IS '创建者ID';


--
-- Name: COLUMN mch_info.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.create_time IS '创建时间';


--
-- Name: COLUMN mch_info.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN mch_info.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_info.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_info.version IS '版本号(乐观锁)';


--
-- Name: mch_notice_record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_notice_record (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    task_id bigint NOT NULL,
    req_count integer NOT NULL,
    send_type character varying(16) NOT NULL,
    success boolean DEFAULT false NOT NULL,
    http_status integer,
    error_msg character varying(300),
    request_digest character varying(500)
);


--
-- Name: TABLE mch_notice_record; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_notice_record IS '商户出站通知发送记录';


--
-- Name: COLUMN mch_notice_record.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.id IS '主键';


--
-- Name: COLUMN mch_notice_record.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.creator IS '创建者ID';


--
-- Name: COLUMN mch_notice_record.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.create_time IS '创建时间';


--
-- Name: COLUMN mch_notice_record.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.last_modifier IS '最后修改ID';


--
-- Name: COLUMN mch_notice_record.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_notice_record.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.version IS '版本号';


--
-- Name: COLUMN mch_notice_record.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.deleted IS '删除标志';


--
-- Name: COLUMN mch_notice_record.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.mch_no IS '商户号';


--
-- Name: COLUMN mch_notice_record.task_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.task_id IS '通知任务ID';


--
-- Name: COLUMN mch_notice_record.req_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.req_count IS '本次对应的发送序号';


--
-- Name: COLUMN mch_notice_record.send_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.send_type IS '发送类型: auto / manual';


--
-- Name: COLUMN mch_notice_record.success; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.success IS '是否成功';


--
-- Name: COLUMN mch_notice_record.http_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.http_status IS 'HTTP状态码';


--
-- Name: COLUMN mch_notice_record.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.error_msg IS '错误摘要';


--
-- Name: COLUMN mch_notice_record.request_digest; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_record.request_digest IS '请求摘要(截断)';


--
-- Name: mch_notice_task; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_notice_task (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(64) NOT NULL,
    biz_id bigint,
    biz_no character varying(64) NOT NULL,
    event character varying(64) NOT NULL,
    source character varying(16) NOT NULL,
    content_mode character varying(16) NOT NULL,
    content text,
    url character varying(512) NOT NULL,
    success boolean DEFAULT false NOT NULL,
    send_count integer DEFAULT 0 NOT NULL,
    delay_count integer DEFAULT 0 NOT NULL,
    next_time timestamp(6) with time zone,
    latest_time timestamp(6) with time zone,
    error_msg character varying(300),
    transport character varying(8) NOT NULL,
    format character varying(16) NOT NULL
);


--
-- Name: TABLE mch_notice_task; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_notice_task IS '商户出站通知任务';


--
-- Name: COLUMN mch_notice_task.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.id IS '主键';


--
-- Name: COLUMN mch_notice_task.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.creator IS '创建者ID';


--
-- Name: COLUMN mch_notice_task.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.create_time IS '创建时间';


--
-- Name: COLUMN mch_notice_task.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.last_modifier IS '最后修改ID';


--
-- Name: COLUMN mch_notice_task.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_notice_task.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.version IS '版本号';


--
-- Name: COLUMN mch_notice_task.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.deleted IS '删除标志';


--
-- Name: COLUMN mch_notice_task.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.mch_no IS '商户号';


--
-- Name: COLUMN mch_notice_task.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.app_id IS '应用号';


--
-- Name: COLUMN mch_notice_task.biz_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.biz_id IS '业务主键ID';


--
-- Name: COLUMN mch_notice_task.biz_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.biz_no IS '业务单号';


--
-- Name: COLUMN mch_notice_task.event; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.event IS '通知事件码(如 pay.success / refund.close)';


--
-- Name: COLUMN mch_notice_task.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.source IS 'URL来源: order / app / protocol';


--
-- Name: COLUMN mch_notice_task.content_mode; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.content_mode IS '内容策略: snapshot / ref';


--
-- Name: COLUMN mch_notice_task.content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.content IS '通知内容(快照JSON或引用指针JSON)';


--
-- Name: COLUMN mch_notice_task.url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.url IS '目标地址(HTTP时为回调URL, MQ时为Topic名, 如 daxpay.notice.<appId>)';


--
-- Name: COLUMN mch_notice_task.success; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.success IS '是否发送成功';


--
-- Name: COLUMN mch_notice_task.send_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.send_count IS '已发送次数';


--
-- Name: COLUMN mch_notice_task.delay_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.delay_count IS '延迟重试次数';


--
-- Name: COLUMN mch_notice_task.next_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.next_time IS '下次发送时间';


--
-- Name: COLUMN mch_notice_task.latest_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.latest_time IS '最后发送时间';


--
-- Name: COLUMN mch_notice_task.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.error_msg IS '最近一次错误摘要';


--
-- Name: COLUMN mch_notice_task.transport; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.transport IS '传输通道: http / mq';


--
-- Name: COLUMN mch_notice_task.format; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_notice_task.format IS '报文格式: system / easy_pay';


--
-- Name: mch_risk_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_risk_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    geo_fence_enabled boolean DEFAULT false NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE mch_risk_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_risk_config IS '商户风控配置表';


--
-- Name: COLUMN mch_risk_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.id IS '主键';


--
-- Name: COLUMN mch_risk_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.mch_no IS '商户号';


--
-- Name: COLUMN mch_risk_config.geo_fence_enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.geo_fence_enabled IS '是否启用地理围栏（商户级 opt-in）';


--
-- Name: COLUMN mch_risk_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.deleted IS '删除标志';


--
-- Name: COLUMN mch_risk_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.creator IS '创建者ID';


--
-- Name: COLUMN mch_risk_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.create_time IS '创建时间';


--
-- Name: COLUMN mch_risk_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN mch_risk_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_risk_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_risk_config.version IS '版本号(乐观锁)';


--
-- Name: mch_store_info; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_store_info (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    store_no character varying(32) NOT NULL,
    store_name character varying(128) NOT NULL,
    contact_phone character varying(32),
    logo_url character varying(512),
    facade_url character varying(512),
    interior_url character varying(512),
    region_code character varying(12),
    address character varying(256),
    longitude numeric(10,7),
    latitude numeric(10,7),
    status character varying(16) DEFAULT 'enable'::character varying NOT NULL,
    remark character varying(512),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    default_store boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE mch_store_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_store_info IS '商户门店(商户物理经营场所)';


--
-- Name: COLUMN mch_store_info.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.id IS '主键';


--
-- Name: COLUMN mch_store_info.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.mch_no IS '商户号';


--
-- Name: COLUMN mch_store_info.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.store_no IS '门店号(系统生成, 唯一)';


--
-- Name: COLUMN mch_store_info.store_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.store_name IS '门店名称';


--
-- Name: COLUMN mch_store_info.contact_phone; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.contact_phone IS '联系人电话';


--
-- Name: COLUMN mch_store_info.logo_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.logo_url IS '门店LOGO';


--
-- Name: COLUMN mch_store_info.facade_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.facade_url IS '门头照';


--
-- Name: COLUMN mch_store_info.interior_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.interior_url IS '门店内景照';


--
-- Name: COLUMN mch_store_info.region_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.region_code IS '行政区划代码(区县级)';


--
-- Name: COLUMN mch_store_info.address; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.address IS '详细地址';


--
-- Name: COLUMN mch_store_info.longitude; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.longitude IS '经度';


--
-- Name: COLUMN mch_store_info.latitude; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.latitude IS '纬度';


--
-- Name: COLUMN mch_store_info.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.status IS '状态';


--
-- Name: COLUMN mch_store_info.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.remark IS '备注';


--
-- Name: COLUMN mch_store_info.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.creator IS '创建人ID';


--
-- Name: COLUMN mch_store_info.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.create_time IS '创建时间';


--
-- Name: COLUMN mch_store_info.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN mch_store_info.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_store_info.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.version IS '版本号(乐观锁)';


--
-- Name: COLUMN mch_store_info.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.deleted IS '逻辑删除标志';


--
-- Name: COLUMN mch_store_info.default_store; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_store_info.default_store IS '是否默认门店(同商户至多一个; 支付未指定 storeNo 时回落)';


--
-- Name: mch_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_user (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    administrator boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: TABLE mch_user; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_user IS '商户用户关联表';


--
-- Name: COLUMN mch_user.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_user.id IS '主键';


--
-- Name: COLUMN mch_user.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_user.user_id IS '用户ID';


--
-- Name: COLUMN mch_user.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_user.mch_no IS '商户号';


--
-- Name: COLUMN mch_user.administrator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_user.administrator IS '是否管理员';


--
-- Name: COLUMN mch_user.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_user.creator IS '创建者';


--
-- Name: COLUMN mch_user.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_user.create_time IS '创建时间';


--
-- Name: mch_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.mch_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: mch_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.mch_user_id_seq OWNED BY public.mch_user.id;


--
-- Name: mch_wx_domain_verify; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.mch_wx_domain_verify (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    platform boolean DEFAULT false NOT NULL,
    file_name character varying(100) NOT NULL,
    verify_code character varying(64) NOT NULL,
    file_content character varying(200) NOT NULL,
    remark character varying(200),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE mch_wx_domain_verify; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.mch_wx_domain_verify IS '商户微信域名验证文件';


--
-- Name: COLUMN mch_wx_domain_verify.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.id IS '主键';


--
-- Name: COLUMN mch_wx_domain_verify.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.mch_no IS '商户号（平台级填 0）';


--
-- Name: COLUMN mch_wx_domain_verify.platform; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.platform IS '是否平台级：false-商户级 true-平台级';


--
-- Name: COLUMN mch_wx_domain_verify.file_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.file_name IS '完整文件名（如 MP_verify_PjhdRxpB8FhG06Fr.txt）';


--
-- Name: COLUMN mch_wx_domain_verify.verify_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.verify_code IS '验证码（文件名提取，全局唯一）';


--
-- Name: COLUMN mch_wx_domain_verify.file_content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.file_content IS '文件内容（微信生成的随机字符串）';


--
-- Name: COLUMN mch_wx_domain_verify.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.remark IS '备注';


--
-- Name: COLUMN mch_wx_domain_verify.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.creator IS '创建者ID';


--
-- Name: COLUMN mch_wx_domain_verify.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.create_time IS '创建时间';


--
-- Name: COLUMN mch_wx_domain_verify.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN mch_wx_domain_verify.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN mch_wx_domain_verify.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.version IS '版本号（乐观锁）';


--
-- Name: COLUMN mch_wx_domain_verify.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.mch_wx_domain_verify.deleted IS '逻辑删除标记';


--
-- Name: notify_mail_record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notify_mail_record (
    id bigint NOT NULL,
    receiver_email character varying(255) NOT NULL,
    receiver_user_id bigint,
    subject character varying(500) NOT NULL,
    content text,
    business_type character varying(50) NOT NULL,
    status character varying(20) NOT NULL,
    error_msg character varying(2000),
    retry_count integer DEFAULT 0 NOT NULL,
    send_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE notify_mail_record; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.notify_mail_record IS '邮件发送记录';


--
-- Name: COLUMN notify_mail_record.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.id IS '主键';


--
-- Name: COLUMN notify_mail_record.receiver_email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.receiver_email IS '收件邮箱';


--
-- Name: COLUMN notify_mail_record.receiver_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.receiver_user_id IS '收件用户ID(关联iam_user_info, 非用户发送可空)';


--
-- Name: COLUMN notify_mail_record.subject; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.subject IS '邮件主题';


--
-- Name: COLUMN notify_mail_record.content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.content IS '邮件正文(HTML)';


--
-- Name: COLUMN notify_mail_record.business_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.business_type IS '业务场景(test/manual等)';


--
-- Name: COLUMN notify_mail_record.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.status IS '发送状态(sending/success/fail)';


--
-- Name: COLUMN notify_mail_record.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.error_msg IS '失败原因';


--
-- Name: COLUMN notify_mail_record.retry_count; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.retry_count IS '重试次数';


--
-- Name: COLUMN notify_mail_record.send_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.send_time IS '实际发送时间';


--
-- Name: COLUMN notify_mail_record.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.creator IS '创建人ID';


--
-- Name: COLUMN notify_mail_record.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.create_time IS '创建时间';


--
-- Name: COLUMN notify_mail_record.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN notify_mail_record.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN notify_mail_record.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.version IS '版本号(乐观锁)';


--
-- Name: COLUMN notify_mail_record.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_mail_record.deleted IS '逻辑删除标志';


--
-- Name: notify_message; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notify_message (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    title character varying(128) NOT NULL,
    content character varying(1024),
    source character varying(32),
    link character varying(255),
    extra text,
    is_read boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE notify_message; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.notify_message IS '个人消息(定向通知, 1条对1用户)';


--
-- Name: COLUMN notify_message.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.id IS '主键';


--
-- Name: COLUMN notify_message.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.user_id IS '接收用户ID';


--
-- Name: COLUMN notify_message.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.title IS '标题';


--
-- Name: COLUMN notify_message.content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.content IS '正文内容';


--
-- Name: COLUMN notify_message.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.source IS '业务来源(预留, 如TRADE/REFUND等)';


--
-- Name: COLUMN notify_message.link; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.link IS '跳转链接(内部路由或完整http外链)';


--
-- Name: COLUMN notify_message.extra; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.extra IS '跳转附加参数(JSON字符串)';


--
-- Name: COLUMN notify_message.is_read; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.is_read IS '是否已读';


--
-- Name: COLUMN notify_message.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.creator IS '创建人ID';


--
-- Name: COLUMN notify_message.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.create_time IS '创建时间';


--
-- Name: COLUMN notify_message.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN notify_message.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN notify_message.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.version IS '版本号(乐观锁)';


--
-- Name: COLUMN notify_message.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_message.deleted IS '逻辑删除标志';


--
-- Name: notify_notice; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notify_notice (
    id bigint NOT NULL,
    title character varying(128) NOT NULL,
    content text NOT NULL,
    severity character varying(16) DEFAULT 'normal'::character varying NOT NULL,
    is_top boolean DEFAULT false NOT NULL,
    effective_time timestamp(6) with time zone,
    expire_time timestamp(6) with time zone,
    status character varying(16) DEFAULT 'draft'::character varying NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE notify_notice; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.notify_notice IS '公告通知(广播型, 1条对多用户可见)';


--
-- Name: COLUMN notify_notice.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.id IS '主键';


--
-- Name: COLUMN notify_notice.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.title IS '标题';


--
-- Name: COLUMN notify_notice.content; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.content IS '正文(Markdown原文)';


--
-- Name: COLUMN notify_notice.severity; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.severity IS '重要程度';


--
-- Name: COLUMN notify_notice.is_top; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.is_top IS '是否置顶';


--
-- Name: COLUMN notify_notice.effective_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.effective_time IS '生效时间(为空则立即生效)';


--
-- Name: COLUMN notify_notice.expire_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.expire_time IS '过期时间(为空则永久有效)';


--
-- Name: COLUMN notify_notice.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.status IS '状态';


--
-- Name: COLUMN notify_notice.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.creator IS '创建人ID';


--
-- Name: COLUMN notify_notice.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.create_time IS '创建时间';


--
-- Name: COLUMN notify_notice.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN notify_notice.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN notify_notice.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.version IS '版本号(乐观锁)';


--
-- Name: COLUMN notify_notice.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice.deleted IS '逻辑删除标志';


--
-- Name: notify_notice_read; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notify_notice_read (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    notice_id bigint NOT NULL,
    read_time timestamp(6) with time zone,
    is_ignored boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone
);


--
-- Name: TABLE notify_notice_read; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.notify_notice_read IS '公告已读记录(用户x公告)';


--
-- Name: COLUMN notify_notice_read.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice_read.id IS '主键';


--
-- Name: COLUMN notify_notice_read.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice_read.user_id IS '用户ID';


--
-- Name: COLUMN notify_notice_read.notice_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice_read.notice_id IS '公告ID';


--
-- Name: COLUMN notify_notice_read.read_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice_read.read_time IS '阅读时间';


--
-- Name: COLUMN notify_notice_read.is_ignored; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice_read.is_ignored IS '是否忽略(用户主动隐藏该公告)';


--
-- Name: COLUMN notify_notice_read.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice_read.creator IS '创建人ID';


--
-- Name: COLUMN notify_notice_read.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.notify_notice_read.create_time IS '创建时间';


--
-- Name: pay_abnormal_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_abnormal_order (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(64),
    trade_no character varying(64) NOT NULL,
    biz_order_no character varying(100),
    trade_type character varying(32),
    title character varying(100),
    amount bigint,
    currency character varying(8),
    trade_status character varying(32),
    abnormal_type character varying(32) NOT NULL,
    source character varying(32) NOT NULL,
    channel character varying(32),
    provider character varying(32),
    channel_mch_no character varying(64),
    out_order_no character varying(150),
    channel_status character varying(32),
    callback_notify_info text,
    handle_status character varying(32) DEFAULT 'pending'::character varying NOT NULL,
    handle_action character varying(32),
    handler character varying(64),
    handle_time timestamp(6) with time zone,
    handle_remark character varying(300),
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT now(),
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_abnormal_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_abnormal_order IS '异常订单';


--
-- Name: COLUMN pay_abnormal_order.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.id IS '主键';


--
-- Name: COLUMN pay_abnormal_order.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.mch_no IS '商户号';


--
-- Name: COLUMN pay_abnormal_order.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.app_id IS '应用号';


--
-- Name: COLUMN pay_abnormal_order.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.trade_no IS '平台交易号';


--
-- Name: COLUMN pay_abnormal_order.biz_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.biz_order_no IS '商户业务单号';


--
-- Name: COLUMN pay_abnormal_order.trade_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.trade_type IS '交易形态(normal/gateway)';


--
-- Name: COLUMN pay_abnormal_order.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.title IS '订单标题';


--
-- Name: COLUMN pay_abnormal_order.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.amount IS '交易金额(最小货币单位)';


--
-- Name: COLUMN pay_abnormal_order.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.currency IS '币种';


--
-- Name: COLUMN pay_abnormal_order.trade_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.trade_status IS '发现异常时的资金状态(close/fail/cancel)';


--
-- Name: COLUMN pay_abnormal_order.abnormal_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.abnormal_type IS '异常类型(close_paid/fail_paid/cancel_paid)';


--
-- Name: COLUMN pay_abnormal_order.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.source IS '发现来源(callback/sync/job)';


--
-- Name: COLUMN pay_abnormal_order.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.channel IS '支付通道';


--
-- Name: COLUMN pay_abnormal_order.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.provider IS '支付渠道';


--
-- Name: COLUMN pay_abnormal_order.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN pay_abnormal_order.out_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.out_order_no IS '通道交易号';


--
-- Name: COLUMN pay_abnormal_order.channel_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.channel_status IS '通道侧订单状态';


--
-- Name: COLUMN pay_abnormal_order.callback_notify_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.callback_notify_info IS '通道回调报文快照';


--
-- Name: COLUMN pay_abnormal_order.handle_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.handle_status IS '处理状态(pending/confirmed/ignored)';


--
-- Name: COLUMN pay_abnormal_order.handle_action; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.handle_action IS '处置动作(confirm_success/ignore)';


--
-- Name: COLUMN pay_abnormal_order.handler; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.handler IS '处理人账号';


--
-- Name: COLUMN pay_abnormal_order.handle_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.handle_time IS '处置时间';


--
-- Name: COLUMN pay_abnormal_order.handle_remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.handle_remark IS '处置备注';


--
-- Name: COLUMN pay_abnormal_order.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.creator IS '创建人';


--
-- Name: COLUMN pay_abnormal_order.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.create_time IS '创建时间';


--
-- Name: COLUMN pay_abnormal_order.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_abnormal_order.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_abnormal_order.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_abnormal_order.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_abnormal_order.deleted IS '逻辑删除标志';


--
-- Name: pay_alloc_detail; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_alloc_detail (
    id bigint NOT NULL,
    alloc_no character varying(100) NOT NULL,
    receiver_type character varying(32) NOT NULL,
    receiver_account character varying(256) NOT NULL,
    receiver_name character varying(256),
    amount bigint NOT NULL,
    result character varying(32) DEFAULT 'pending'::character varying NOT NULL,
    out_detail_id character varying(150),
    error_code character varying(64),
    error_msg character varying(500),
    finish_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_alloc_detail; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_alloc_detail IS '分账明细(每个接收方一行)';


--
-- Name: COLUMN pay_alloc_detail.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.id IS '主键';


--
-- Name: COLUMN pay_alloc_detail.alloc_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.alloc_no IS '分账单号(关联 pay_alloc_order.alloc_no)';


--
-- Name: COLUMN pay_alloc_detail.receiver_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.receiver_type IS '接收方类型(MERCHANT_ID/PERSONAL_OPENID/USER_ID/LOGIN_NAME 等, 按通道)';


--
-- Name: COLUMN pay_alloc_detail.receiver_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.receiver_account IS '接收方账号(AES-256-GCM 加密存储)';


--
-- Name: COLUMN pay_alloc_detail.receiver_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.receiver_name IS '接收方姓名(AES-256-GCM 加密存储, 可空)';


--
-- Name: COLUMN pay_alloc_detail.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.amount IS '分账金额(分)';


--
-- Name: COLUMN pay_alloc_detail.result; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.result IS '明细结果(pending/success/fail)';


--
-- Name: COLUMN pay_alloc_detail.out_detail_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.out_detail_id IS '通道侧明细ID(同步/回调时回填)';


--
-- Name: COLUMN pay_alloc_detail.error_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.error_code IS '错误码';


--
-- Name: COLUMN pay_alloc_detail.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.error_msg IS '错误信息';


--
-- Name: COLUMN pay_alloc_detail.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.finish_time IS '明细完成时间';


--
-- Name: COLUMN pay_alloc_detail.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.creator IS '创建人';


--
-- Name: COLUMN pay_alloc_detail.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.create_time IS '创建时间';


--
-- Name: COLUMN pay_alloc_detail.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_alloc_detail.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_alloc_detail.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.version IS '乐观锁版本';


--
-- Name: COLUMN pay_alloc_detail.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_detail.deleted IS '逻辑删除';


--
-- Name: pay_alloc_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_alloc_order (
    id bigint NOT NULL,
    app_id character varying(32) NOT NULL,
    alloc_no character varying(100) NOT NULL,
    biz_alloc_no character varying(100) NOT NULL,
    title character varying(100),
    description character varying(500),
    trade_no character varying(100) NOT NULL,
    trade_type character varying(32),
    biz_order_no character varying(100),
    out_order_no character varying(150),
    out_alloc_no character varying(150),
    amount bigint NOT NULL,
    order_amount bigint,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    status character varying(32) NOT NULL,
    finish_time timestamp(6) with time zone,
    channel character varying(32),
    provider character varying(32),
    product character varying(32),
    channel_mch_no character varying(64),
    capability character varying(64),
    channel_app_id character varying(128),
    notify_url character varying(200),
    attach character varying(500),
    client_ip character varying(64),
    error_code character varying(64),
    error_msg character varying(500),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_alloc_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_alloc_order IS '分账订单';


--
-- Name: COLUMN pay_alloc_order.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.id IS '主键';


--
-- Name: COLUMN pay_alloc_order.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.app_id IS '应用号';


--
-- Name: COLUMN pay_alloc_order.alloc_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.alloc_no IS '分账单号(平台统一生成, 全局唯一)';


--
-- Name: COLUMN pay_alloc_order.biz_alloc_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.biz_alloc_no IS '商户分账单号(商户传入, 商户侧唯一, 幂等键)';


--
-- Name: COLUMN pay_alloc_order.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.title IS '标题(继承自原支付容器)';


--
-- Name: COLUMN pay_alloc_order.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.description IS '分账描述';


--
-- Name: COLUMN pay_alloc_order.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.trade_no IS '原支付资金交易号(= pay_trade.trade_no)';


--
-- Name: COLUMN pay_alloc_order.trade_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.trade_type IS '原支付交易形态(冗余自 pay_trade.trade_type)';


--
-- Name: COLUMN pay_alloc_order.biz_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.biz_order_no IS '商户业务订单号(冗余, 便于查询)';


--
-- Name: COLUMN pay_alloc_order.out_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.out_order_no IS '通道支付订单号(冗余, 分账上送通道用)';


--
-- Name: COLUMN pay_alloc_order.out_alloc_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.out_alloc_no IS '通道分账单号(通道返回, 如支付宝 settle_no / 微信 transaction_id / 抖音 orderId)';


--
-- Name: COLUMN pay_alloc_order.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.amount IS '分账总金额(各接收方金额之和, 分)';


--
-- Name: COLUMN pay_alloc_order.order_amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.order_amount IS '原订单总金额(冗余, 分)';


--
-- Name: COLUMN pay_alloc_order.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.currency IS '币种';


--
-- Name: COLUMN pay_alloc_order.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.status IS '分账状态(processing/success/partial/fail)';


--
-- Name: COLUMN pay_alloc_order.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.finish_time IS '分账完成时间';


--
-- Name: COLUMN pay_alloc_order.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.channel IS '支付通道';


--
-- Name: COLUMN pay_alloc_order.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.provider IS '支付渠道';


--
-- Name: COLUMN pay_alloc_order.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.product IS '支付产品编码(策略选型)';


--
-- Name: COLUMN pay_alloc_order.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.channel_mch_no IS '通道商户号(继承自原支付)';


--
-- Name: COLUMN pay_alloc_order.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.capability IS '支付能力编码(继承自原支付)';


--
-- Name: COLUMN pay_alloc_order.channel_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.channel_app_id IS '通道应用 AppId 快照(继承自原支付)';


--
-- Name: COLUMN pay_alloc_order.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.notify_url IS '异步通知地址(出站商户通知用)';


--
-- Name: COLUMN pay_alloc_order.attach; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.attach IS '商户附加参数(回调原样返回)';


--
-- Name: COLUMN pay_alloc_order.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.client_ip IS '客户端 IP';


--
-- Name: COLUMN pay_alloc_order.error_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.error_code IS '错误码';


--
-- Name: COLUMN pay_alloc_order.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.error_msg IS '错误信息';


--
-- Name: COLUMN pay_alloc_order.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.mch_no IS '商户号';


--
-- Name: COLUMN pay_alloc_order.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.creator IS '创建人';


--
-- Name: COLUMN pay_alloc_order.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.create_time IS '创建时间';


--
-- Name: COLUMN pay_alloc_order.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_alloc_order.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_alloc_order.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.version IS '乐观锁版本';


--
-- Name: COLUMN pay_alloc_order.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_alloc_order.deleted IS '逻辑删除';


--
-- Name: pay_blacklist; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_blacklist (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    type character varying(32) NOT NULL,
    value character varying(128) NOT NULL,
    wx_app_id character varying(64),
    status character varying(16) DEFAULT 'enable'::character varying NOT NULL,
    reason character varying(255),
    expire_time timestamp(6) with time zone,
    remark character varying(255)
);


--
-- Name: TABLE pay_blacklist; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_blacklist IS '支付黑名单（平台级；IP全局 / 支付宝userId全局 / 微信按平台应用）';


--
-- Name: COLUMN pay_blacklist.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.id IS '主键';


--
-- Name: COLUMN pay_blacklist.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.creator IS '创建者ID';


--
-- Name: COLUMN pay_blacklist.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.create_time IS '创建时间';


--
-- Name: COLUMN pay_blacklist.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.last_modifier IS '最后修改ID';


--
-- Name: COLUMN pay_blacklist.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_blacklist.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.version IS '版本号';


--
-- Name: COLUMN pay_blacklist.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.deleted IS '删除标志';


--
-- Name: COLUMN pay_blacklist.type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.type IS '名单类型: ip / alipay_user / wechat_openid';


--
-- Name: COLUMN pay_blacklist.value; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.value IS '名单值（IP、支付宝userId或微信openId）';


--
-- Name: COLUMN pay_blacklist.wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.wx_app_id IS '微信平台支付应用 AppId；仅 wechat_openid 使用';


--
-- Name: COLUMN pay_blacklist.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.status IS '状态';


--
-- Name: COLUMN pay_blacklist.reason; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.reason IS '拉黑原因';


--
-- Name: COLUMN pay_blacklist.expire_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.expire_time IS '过期时间（空表示永久有效）';


--
-- Name: COLUMN pay_blacklist.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_blacklist.remark IS '备注';


--
-- Name: pay_callback_record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_callback_record (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(64),
    channel_mch_no character varying(64),
    trade_no character varying(100),
    out_trade_no character varying(150),
    product character varying(32),
    callback_type character varying(20) NOT NULL,
    notify_info text NOT NULL,
    status character varying(20) NOT NULL,
    error_msg character varying(500)
);


--
-- Name: TABLE pay_callback_record; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_callback_record IS '通道入站回调记录';


--
-- Name: COLUMN pay_callback_record.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.id IS '主键';


--
-- Name: COLUMN pay_callback_record.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.creator IS '创建者ID';


--
-- Name: COLUMN pay_callback_record.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.create_time IS '创建时间';


--
-- Name: COLUMN pay_callback_record.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.last_modifier IS '最后修改ID';


--
-- Name: COLUMN pay_callback_record.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_callback_record.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.version IS '版本号';


--
-- Name: COLUMN pay_callback_record.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.deleted IS '删除标志';


--
-- Name: COLUMN pay_callback_record.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.mch_no IS '商户号';


--
-- Name: COLUMN pay_callback_record.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.app_id IS '应用号';


--
-- Name: COLUMN pay_callback_record.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN pay_callback_record.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.trade_no IS '平台交易号(支付回调为 trade_no, 退款回调为 refund_no)';


--
-- Name: COLUMN pay_callback_record.out_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.out_trade_no IS '通道交易号';


--
-- Name: COLUMN pay_callback_record.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.product IS '支付产品';


--
-- Name: COLUMN pay_callback_record.callback_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.callback_type IS '回调类型: pay / refund';


--
-- Name: COLUMN pay_callback_record.notify_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.notify_info IS '通知消息内容(JSON)';


--
-- Name: COLUMN pay_callback_record.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.status IS '回调处理状态';


--
-- Name: COLUMN pay_callback_record.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_callback_record.error_msg IS '错误信息';


--
-- Name: pay_channel_terminal; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_channel_terminal (
    id bigint NOT NULL,
    channel_mch_no character varying(32) NOT NULL,
    product character varying(64) NOT NULL,
    channel character varying(64) NOT NULL,
    type character varying(32) NOT NULL,
    name character varying(100) NOT NULL,
    out_terminal_no character varying(64),
    status character varying(32) DEFAULT 'init'::character varying NOT NULL,
    error_msg character varying(255),
    remark character varying(255),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_channel_terminal; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_channel_terminal IS '通道终端台账(人工登记, 无自动报备)';


--
-- Name: COLUMN pay_channel_terminal.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.id IS '主键';


--
-- Name: COLUMN pay_channel_terminal.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN pay_channel_terminal.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.product IS '所属支付产品(冗余)';


--
-- Name: COLUMN pay_channel_terminal.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.channel IS '所属通道编码(冗余)';


--
-- Name: COLUMN pay_channel_terminal.type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.type IS '报送类型 common/wechat/alipay/union';


--
-- Name: COLUMN pay_channel_terminal.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.name IS '终端名称';


--
-- Name: COLUMN pay_channel_terminal.out_terminal_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.out_terminal_no IS '通道侧终端号(人工录入)';


--
-- Name: COLUMN pay_channel_terminal.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.status IS '登记状态 init/wait/submit/logged/error';


--
-- Name: COLUMN pay_channel_terminal.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.error_msg IS '错误信息';


--
-- Name: COLUMN pay_channel_terminal.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.remark IS '备注';


--
-- Name: COLUMN pay_channel_terminal.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.mch_no IS '商户号';


--
-- Name: COLUMN pay_channel_terminal.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.creator IS '创建者ID';


--
-- Name: COLUMN pay_channel_terminal.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.create_time IS '创建时间';


--
-- Name: COLUMN pay_channel_terminal.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_channel_terminal.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_channel_terminal.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_channel_terminal.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_channel_terminal.deleted IS '删除标志';


--
-- Name: pay_close_record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_close_record (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(32),
    trade_no character varying(64) NOT NULL,
    biz_trade_no character varying(64),
    product character varying(32),
    channel character varying(32),
    closed boolean DEFAULT false NOT NULL,
    close_type character varying(32),
    error_code character varying(128),
    error_msg character varying(300),
    client_ip character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT now(),
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_close_record; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_close_record IS '支付关闭记录';


--
-- Name: COLUMN pay_close_record.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.id IS '主键';


--
-- Name: COLUMN pay_close_record.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.mch_no IS '商户号';


--
-- Name: COLUMN pay_close_record.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.app_id IS '应用号';


--
-- Name: COLUMN pay_close_record.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.trade_no IS '平台交易号(对应 pay_trade.trade_no)';


--
-- Name: COLUMN pay_close_record.biz_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.biz_trade_no IS '商户业务单号(对应 pay_normal_order.biz_order_no)';


--
-- Name: COLUMN pay_close_record.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.product IS '支付产品编码';


--
-- Name: COLUMN pay_close_record.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.channel IS '支付通道';


--
-- Name: COLUMN pay_close_record.closed; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.closed IS '是否关闭成功';


--
-- Name: COLUMN pay_close_record.close_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.close_type IS '关闭类型(close/cancel)';


--
-- Name: COLUMN pay_close_record.error_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.error_code IS '错误码';


--
-- Name: COLUMN pay_close_record.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.error_msg IS '错误信息';


--
-- Name: COLUMN pay_close_record.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.client_ip IS '客户端 IP';


--
-- Name: COLUMN pay_close_record.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.creator IS '创建人';


--
-- Name: COLUMN pay_close_record.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.create_time IS '创建时间';


--
-- Name: COLUMN pay_close_record.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_close_record.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_close_record.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_close_record.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_close_record.deleted IS '逻辑删除标志';


--
-- Name: pay_close_record_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pay_close_record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pay_close_record_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pay_close_record_id_seq OWNED BY public.pay_close_record.id;


--
-- Name: pay_easy_pay_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_easy_pay_config (
    id bigint NOT NULL,
    pid integer NOT NULL,
    app_id character varying(32) NOT NULL,
    limit_pay character varying(64),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_easy_pay_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_easy_pay_config IS '易支付场景配置表';


--
-- Name: COLUMN pay_easy_pay_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.id IS '主键';


--
-- Name: COLUMN pay_easy_pay_config.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.pid IS '易支付商户号';


--
-- Name: COLUMN pay_easy_pay_config.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.app_id IS '应用号';


--
-- Name: COLUMN pay_easy_pay_config.limit_pay; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.limit_pay IS '限制支付方式';


--
-- Name: COLUMN pay_easy_pay_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.mch_no IS '商户号';


--
-- Name: COLUMN pay_easy_pay_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.creator IS '创建人';


--
-- Name: COLUMN pay_easy_pay_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.create_time IS '创建时间';


--
-- Name: COLUMN pay_easy_pay_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_easy_pay_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_easy_pay_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.version IS '乐观锁版本';


--
-- Name: COLUMN pay_easy_pay_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_config.deleted IS '逻辑删除';


--
-- Name: pay_easy_pay_credential; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_easy_pay_credential (
    id bigint NOT NULL,
    pid integer NOT NULL,
    app_id character varying(32) NOT NULL,
    enable boolean DEFAULT false NOT NULL,
    enable_v1 boolean DEFAULT false NOT NULL,
    enable_v2 boolean DEFAULT true NOT NULL,
    md5_key character varying(128),
    use_system_key boolean DEFAULT true NOT NULL,
    public_key text,
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_easy_pay_credential; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_easy_pay_credential IS '易支付凭证配置表（应用级）';


--
-- Name: COLUMN pay_easy_pay_credential.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.id IS '主键';


--
-- Name: COLUMN pay_easy_pay_credential.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.pid IS '易支付商户号';


--
-- Name: COLUMN pay_easy_pay_credential.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.app_id IS '应用号（支付出口）';


--
-- Name: COLUMN pay_easy_pay_credential.enable; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.enable IS '是否启用';


--
-- Name: COLUMN pay_easy_pay_credential.enable_v1; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.enable_v1 IS '是否开启V1接口';


--
-- Name: COLUMN pay_easy_pay_credential.enable_v2; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.enable_v2 IS '是否开启V2接口';


--
-- Name: COLUMN pay_easy_pay_credential.md5_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.md5_key IS 'V1 MD5密钥';


--
-- Name: COLUMN pay_easy_pay_credential.use_system_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.use_system_key IS 'V2是否使用系统公私钥';


--
-- Name: COLUMN pay_easy_pay_credential.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.public_key IS '商户RSA公钥';


--
-- Name: COLUMN pay_easy_pay_credential.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.mch_no IS '商户号';


--
-- Name: COLUMN pay_easy_pay_credential.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.creator IS '创建人';


--
-- Name: COLUMN pay_easy_pay_credential.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.create_time IS '创建时间';


--
-- Name: COLUMN pay_easy_pay_credential.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_easy_pay_credential.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_easy_pay_credential.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.version IS '乐观锁版本';


--
-- Name: COLUMN pay_easy_pay_credential.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_credential.deleted IS '逻辑删除';


--
-- Name: pay_easy_pay_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_easy_pay_order (
    id bigint NOT NULL,
    order_id bigint,
    pid integer NOT NULL,
    app_id character varying(32) NOT NULL,
    trade_no character varying(64),
    out_trade_no character varying(100) NOT NULL,
    api_trade_no character varying(64),
    type character varying(32),
    status integer DEFAULT 0 NOT NULL,
    add_time timestamp(6) with time zone,
    end_time timestamp(6) with time zone,
    name character varying(200),
    money numeric(16,2) NOT NULL,
    refund_money numeric(16,2) DEFAULT 0,
    notify_url character varying(500),
    return_url character varying(500),
    param character varying(500),
    buyer character varying(128),
    client_ip character varying(64),
    api_version character varying(8),
    pc_call_type character varying(32),
    pay_url character varying(1000),
    pay_body text,
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_easy_pay_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_easy_pay_order IS '易支付协议订单表';


--
-- Name: COLUMN pay_easy_pay_order.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.id IS '主键（收银台路径参数）';


--
-- Name: COLUMN pay_easy_pay_order.order_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.order_id IS '关联内核容器ID';


--
-- Name: COLUMN pay_easy_pay_order.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.pid IS '易支付商户号';


--
-- Name: COLUMN pay_easy_pay_order.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.app_id IS '应用号';


--
-- Name: COLUMN pay_easy_pay_order.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.trade_no IS '平台业务单号（容器orderNo）';


--
-- Name: COLUMN pay_easy_pay_order.out_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.out_trade_no IS '商户订单号';


--
-- Name: COLUMN pay_easy_pay_order.api_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.api_trade_no IS '通道订单号';


--
-- Name: COLUMN pay_easy_pay_order.type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.type IS '协议支付方式 alipay/wxpay/aggregate';


--
-- Name: COLUMN pay_easy_pay_order.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.status IS '协议状态 0待付 1成功';


--
-- Name: COLUMN pay_easy_pay_order.add_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.add_time IS '创建时间';


--
-- Name: COLUMN pay_easy_pay_order.end_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.end_time IS '完成时间';


--
-- Name: COLUMN pay_easy_pay_order.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.name IS '商品名称';


--
-- Name: COLUMN pay_easy_pay_order.money; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.money IS '订单金额（元）';


--
-- Name: COLUMN pay_easy_pay_order.refund_money; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.refund_money IS '已退款金额（元）';


--
-- Name: COLUMN pay_easy_pay_order.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.notify_url IS '异步通知地址（本期仅落库）';


--
-- Name: COLUMN pay_easy_pay_order.return_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.return_url IS '同步跳转地址';


--
-- Name: COLUMN pay_easy_pay_order.param; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.param IS '业务扩展参数';


--
-- Name: COLUMN pay_easy_pay_order.buyer; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.buyer IS '支付用户标识';


--
-- Name: COLUMN pay_easy_pay_order.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.client_ip IS '客户端IP';


--
-- Name: COLUMN pay_easy_pay_order.api_version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.api_version IS 'API版本 v1/v2';


--
-- Name: COLUMN pay_easy_pay_order.pc_call_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.pc_call_type IS '支付调用方式';


--
-- Name: COLUMN pay_easy_pay_order.pay_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.pay_url IS '支付链接';


--
-- Name: COLUMN pay_easy_pay_order.pay_body; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.pay_body IS '支付参数体';


--
-- Name: COLUMN pay_easy_pay_order.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.mch_no IS '商户号';


--
-- Name: COLUMN pay_easy_pay_order.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.creator IS '创建人';


--
-- Name: COLUMN pay_easy_pay_order.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.create_time IS '创建时间';


--
-- Name: COLUMN pay_easy_pay_order.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_easy_pay_order.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_easy_pay_order.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.version IS '乐观锁版本';


--
-- Name: COLUMN pay_easy_pay_order.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_order.deleted IS '逻辑删除';


--
-- Name: pay_easy_pay_refund_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_easy_pay_refund_order (
    id bigint NOT NULL,
    mch_no character varying(64),
    refund_id bigint,
    easy_pay_order_id bigint,
    pid integer,
    app_id character varying(64),
    refund_no character varying(64),
    biz_refund_no character varying(64),
    trade_no character varying(64),
    out_trade_no character varying(64),
    money numeric(15,2),
    status integer,
    api_version character varying(20),
    add_time timestamp(6) with time zone,
    end_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false
);


--
-- Name: TABLE pay_easy_pay_refund_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_easy_pay_refund_order IS '易支付协议退款订单表';


--
-- Name: COLUMN pay_easy_pay_refund_order.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.id IS '主键ID';


--
-- Name: COLUMN pay_easy_pay_refund_order.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.mch_no IS '商户号';


--
-- Name: COLUMN pay_easy_pay_refund_order.refund_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.refund_id IS '关联内核退款单ID(pay_refund_order.id)';


--
-- Name: COLUMN pay_easy_pay_refund_order.easy_pay_order_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.easy_pay_order_id IS '关联易支付订单ID(pay_easy_pay_order.id)';


--
-- Name: COLUMN pay_easy_pay_refund_order.pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.pid IS '易支付商户号';


--
-- Name: COLUMN pay_easy_pay_refund_order.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.app_id IS '应用号';


--
-- Name: COLUMN pay_easy_pay_refund_order.refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.refund_no IS '平台退款单号';


--
-- Name: COLUMN pay_easy_pay_refund_order.biz_refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.biz_refund_no IS '商户退款单号';


--
-- Name: COLUMN pay_easy_pay_refund_order.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.trade_no IS '平台业务单号';


--
-- Name: COLUMN pay_easy_pay_refund_order.out_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.out_trade_no IS '商户订单号';


--
-- Name: COLUMN pay_easy_pay_refund_order.money; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.money IS '退款金额(元)';


--
-- Name: COLUMN pay_easy_pay_refund_order.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.status IS '协议退款状态 0=失败/处理中 1=成功';


--
-- Name: COLUMN pay_easy_pay_refund_order.api_version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.api_version IS 'API版本 v1/v2';


--
-- Name: COLUMN pay_easy_pay_refund_order.add_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.add_time IS '退款发起时间';


--
-- Name: COLUMN pay_easy_pay_refund_order.end_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.end_time IS '退款完成时间';


--
-- Name: COLUMN pay_easy_pay_refund_order.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.creator IS '创建者ID';


--
-- Name: COLUMN pay_easy_pay_refund_order.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.create_time IS '创建时间';


--
-- Name: COLUMN pay_easy_pay_refund_order.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_easy_pay_refund_order.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_easy_pay_refund_order.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.version IS '版本号';


--
-- Name: COLUMN pay_easy_pay_refund_order.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_easy_pay_refund_order.deleted IS '删除标志(逻辑删除)';


--
-- Name: pay_fund_flow; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_fund_flow (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(64),
    flow_type character varying(32) NOT NULL,
    trade_no character varying(64) NOT NULL,
    refund_no character varying(64),
    biz_order_no character varying(100),
    title character varying(100),
    amount bigint NOT NULL,
    currency character varying(8),
    channel character varying(32),
    provider character varying(32),
    channel_mch_no character varying(64),
    out_order_no character varying(150),
    finish_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT now(),
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_fund_flow; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_fund_flow IS '资金流水表(只增不改)';


--
-- Name: COLUMN pay_fund_flow.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.id IS '主键';


--
-- Name: COLUMN pay_fund_flow.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.mch_no IS '商户号';


--
-- Name: COLUMN pay_fund_flow.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.app_id IS '应用号';


--
-- Name: COLUMN pay_fund_flow.flow_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.flow_type IS '流水类型(pay/refund)';


--
-- Name: COLUMN pay_fund_flow.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.trade_no IS '原支付交易号';


--
-- Name: COLUMN pay_fund_flow.refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.refund_no IS '退款单号';


--
-- Name: COLUMN pay_fund_flow.biz_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.biz_order_no IS '商户业务单号';


--
-- Name: COLUMN pay_fund_flow.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.title IS '订单标题';


--
-- Name: COLUMN pay_fund_flow.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.amount IS '流水金额(最小货币单位)';


--
-- Name: COLUMN pay_fund_flow.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.currency IS '币种';


--
-- Name: COLUMN pay_fund_flow.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.channel IS '支付通道';


--
-- Name: COLUMN pay_fund_flow.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.provider IS '支付渠道';


--
-- Name: COLUMN pay_fund_flow.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN pay_fund_flow.out_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.out_order_no IS '通道交易号';


--
-- Name: COLUMN pay_fund_flow.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.finish_time IS '资金完成时间';


--
-- Name: COLUMN pay_fund_flow.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.creator IS '创建人';


--
-- Name: COLUMN pay_fund_flow.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.create_time IS '创建时间';


--
-- Name: COLUMN pay_fund_flow.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_fund_flow.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_fund_flow.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_fund_flow.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_fund_flow.deleted IS '逻辑删除标志';


--
-- Name: pay_gateway_cashier_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_gateway_cashier_item (
    id bigint NOT NULL,
    app_id character varying(32) NOT NULL,
    cashier_type character varying(16) NOT NULL,
    client_env character varying(32),
    name character varying(64) NOT NULL,
    icon character varying(32),
    recommend boolean DEFAULT false NOT NULL,
    sort_no integer DEFAULT 0 NOT NULL,
    resolve_mode character varying(16) NOT NULL,
    method character varying(32),
    channel_mch_no character varying(64),
    capability character varying(64),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_gateway_cashier_item; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_gateway_cashier_item IS '网关收银台支付项配置(应用级)';


--
-- Name: COLUMN pay_gateway_cashier_item.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.id IS '主键';


--
-- Name: COLUMN pay_gateway_cashier_item.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.app_id IS '应用号';


--
-- Name: COLUMN pay_gateway_cashier_item.cashier_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.cashier_type IS '收银台类型: h5/web';


--
-- Name: COLUMN pay_gateway_cashier_item.client_env; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.client_env IS '客户端环境(ClientEnvEnum)；WEB 为空';


--
-- Name: COLUMN pay_gateway_cashier_item.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.name IS '前台展示名称';


--
-- Name: COLUMN pay_gateway_cashier_item.icon; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.icon IS '图标编码';


--
-- Name: COLUMN pay_gateway_cashier_item.recommend; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.recommend IS '是否推荐';


--
-- Name: COLUMN pay_gateway_cashier_item.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.sort_no IS '排序号(越小越前)';


--
-- Name: COLUMN pay_gateway_cashier_item.resolve_mode; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.resolve_mode IS '解析模式: method/direct';


--
-- Name: COLUMN pay_gateway_cashier_item.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.method IS '支付方式(METHOD 模式)';


--
-- Name: COLUMN pay_gateway_cashier_item.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.channel_mch_no IS '通道商户号(DIRECT 模式)';


--
-- Name: COLUMN pay_gateway_cashier_item.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.capability IS '支付能力(DIRECT 模式)';


--
-- Name: COLUMN pay_gateway_cashier_item.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.mch_no IS '商户号';


--
-- Name: COLUMN pay_gateway_cashier_item.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.creator IS '创建人';


--
-- Name: COLUMN pay_gateway_cashier_item.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.create_time IS '创建时间';


--
-- Name: COLUMN pay_gateway_cashier_item.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_gateway_cashier_item.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_gateway_cashier_item.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.version IS '乐观锁版本';


--
-- Name: COLUMN pay_gateway_cashier_item.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_cashier_item.deleted IS '逻辑删除';


--
-- Name: pay_gateway_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_gateway_order (
    id bigint NOT NULL,
    order_no character varying(100) NOT NULL,
    biz_order_no character varying(100),
    gateway_type character varying(32),
    source character varying(32),
    title character varying(100),
    description character varying(500),
    status character varying(32),
    notify_url character varying(200),
    return_url character varying(200),
    attach character varying(500),
    expired_time timestamp(6) with time zone,
    amount bigint,
    currency character varying(16),
    channel character varying(32),
    method character varying(32),
    limit_pay character varying(32),
    product character varying(64),
    openid character varying(128),
    client_env character varying(32),
    device character varying(16),
    pay_time timestamp(6) with time zone,
    close_time timestamp(6) with time zone,
    channel_mch_no character varying(64),
    capability character varying(64),
    channel_app_id character varying(128),
    provider character varying(32),
    buyer_id character varying(128),
    trade_product character varying(64),
    trade_way character varying(64),
    bank_type character varying(32),
    promotion_type character varying(64),
    pay_body text,
    pay_body_type character varying(32),
    trans_order_no character varying(150),
    relation_order_no character varying(150),
    extra_param character varying(2048),
    app_id character varying(32),
    goods_detail jsonb,
    client_ip character varying(64),
    store_no character varying(32),
    error_msg character varying(500),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    link_form character varying(16) DEFAULT 'h5'::character varying,
    allocation boolean
);


--
-- Name: TABLE pay_gateway_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_gateway_order IS '网关支付业务单容器(聚合扫码/收银台预下单)';


--
-- Name: COLUMN pay_gateway_order.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.id IS '主键';


--
-- Name: COLUMN pay_gateway_order.order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.order_no IS '平台业务单号(容器身份, 与 tradeNo 独立; 预下单 URL 号)';


--
-- Name: COLUMN pay_gateway_order.biz_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.biz_order_no IS '商户业务单号';


--
-- Name: COLUMN pay_gateway_order.gateway_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.gateway_type IS '网关支付类型(GatewayPayTypeEnum)';


--
-- Name: COLUMN pay_gateway_order.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.source IS '订单来源(业务入口权威, TradeSourceEnum; 预下单写入)';


--
-- Name: COLUMN pay_gateway_order.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.title IS '标题';


--
-- Name: COLUMN pay_gateway_order.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.description IS '描述';


--
-- Name: COLUMN pay_gateway_order.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.status IS '业务状态(GatewayOrderStatusEnum)';


--
-- Name: COLUMN pay_gateway_order.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.notify_url IS '异步通知地址';


--
-- Name: COLUMN pay_gateway_order.return_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.return_url IS '同步跳转地址';


--
-- Name: COLUMN pay_gateway_order.attach; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.attach IS '商户附加参数(回调原样返回)';


--
-- Name: COLUMN pay_gateway_order.expired_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.expired_time IS '业务单过期时间';


--
-- Name: COLUMN pay_gateway_order.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.amount IS '业务单金额(最小货币单位)';


--
-- Name: COLUMN pay_gateway_order.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.currency IS '币种(CurrencyEnum)';


--
-- Name: COLUMN pay_gateway_order.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.channel IS '支付通道编码(ChannelEnum)';


--
-- Name: COLUMN pay_gateway_order.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.method IS '支付方式(PayMethodEnum)';


--
-- Name: COLUMN pay_gateway_order.limit_pay; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.limit_pay IS '限制支付类型(PayLimitPayEnum)';


--
-- Name: COLUMN pay_gateway_order.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.product IS '支付产品编码(ProductEnum)';


--
-- Name: COLUMN pay_gateway_order.openid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.openid IS '微信 openid(jsapi/app/miniapp)';


--
-- Name: COLUMN pay_gateway_order.client_env; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.client_env IS '客户端环境(ClientEnvEnum, 支付时回填)';


--
-- Name: COLUMN pay_gateway_order.device; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.device IS '最后发起设备(mobile/pc, 支付时回填)';


--
-- Name: COLUMN pay_gateway_order.pay_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.pay_time IS '支付成功时间';


--
-- Name: COLUMN pay_gateway_order.close_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.close_time IS '关闭时间';


--
-- Name: COLUMN pay_gateway_order.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.channel_mch_no IS '通道商户号(路由回填)';


--
-- Name: COLUMN pay_gateway_order.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.capability IS '支付能力编码(路由回填)';


--
-- Name: COLUMN pay_gateway_order.channel_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.channel_app_id IS '通道应用 AppId 快照';


--
-- Name: COLUMN pay_gateway_order.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.provider IS '支付渠道(PayProviderEnum)';


--
-- Name: COLUMN pay_gateway_order.buyer_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.buyer_id IS '付款用户标识';


--
-- Name: COLUMN pay_gateway_order.trade_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.trade_product IS '通道方记录的支付产品';


--
-- Name: COLUMN pay_gateway_order.trade_way; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.trade_way IS '通道方记录的交易方式';


--
-- Name: COLUMN pay_gateway_order.bank_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.bank_type IS '银行卡类型(借记卡/贷记卡)';


--
-- Name: COLUMN pay_gateway_order.promotion_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.promotion_type IS '活动类型';


--
-- Name: COLUMN pay_gateway_order.pay_body; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.pay_body IS '支付参数体(已拉起缓存, 仅容器)';


--
-- Name: COLUMN pay_gateway_order.pay_body_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.pay_body_type IS '支付参数体类型(jsapi/sdk/app)';


--
-- Name: COLUMN pay_gateway_order.trans_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.trans_order_no IS '透传订单号(三方通道产生)';


--
-- Name: COLUMN pay_gateway_order.relation_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.relation_order_no IS '实际上送通道的商户订单号(展示冗余)';


--
-- Name: COLUMN pay_gateway_order.extra_param; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.extra_param IS '通道附加参数';


--
-- Name: COLUMN pay_gateway_order.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.app_id IS '应用号';


--
-- Name: COLUMN pay_gateway_order.goods_detail; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.goods_detail IS '订单商品明细列表(jsonb)';


--
-- Name: COLUMN pay_gateway_order.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.client_ip IS '下单客户端 IP';


--
-- Name: COLUMN pay_gateway_order.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.store_no IS '门店号(线下经营归属, 可空)';


--
-- Name: COLUMN pay_gateway_order.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.error_msg IS '错误信息';


--
-- Name: COLUMN pay_gateway_order.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.mch_no IS '商户号';


--
-- Name: COLUMN pay_gateway_order.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.creator IS '创建人';


--
-- Name: COLUMN pay_gateway_order.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.create_time IS '创建时间';


--
-- Name: COLUMN pay_gateway_order.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_gateway_order.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_gateway_order.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.version IS '乐观锁版本';


--
-- Name: COLUMN pay_gateway_order.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_gateway_order.link_form; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.link_form IS '链接形态: h5/mini(聚合小程序扫码), 缺省 h5';


--
-- Name: COLUMN pay_gateway_order.allocation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_order.allocation IS '是否分账订单(预下单透传通道分账标识, true 表示资金冻结仅可分账拆分)';


--
-- Name: pay_gateway_pay_client_env; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_gateway_pay_client_env (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    config_id bigint NOT NULL,
    client_env character varying(32) NOT NULL,
    pay_form character varying(16) NOT NULL,
    method character varying(32),
    channel_mch_no character varying(64),
    capability character varying(64),
    deleted boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE pay_gateway_pay_client_env; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_gateway_pay_client_env IS '网关支付客户端环境配置(子表, 码牌/聚合共用)';


--
-- Name: COLUMN pay_gateway_pay_client_env.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.id IS '主键';


--
-- Name: COLUMN pay_gateway_pay_client_env.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.mch_no IS '商户号';


--
-- Name: COLUMN pay_gateway_pay_client_env.config_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.config_id IS '网关支付配置主表ID';


--
-- Name: COLUMN pay_gateway_pay_client_env.client_env; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.client_env IS '客户端环境编码: wechat/alipay/union_pay/douyin';


--
-- Name: COLUMN pay_gateway_pay_client_env.pay_form; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.pay_form IS '支付形态: h5/mini';


--
-- Name: COLUMN pay_gateway_pay_client_env.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.method IS '支付方式(METHOD 模式填)';


--
-- Name: COLUMN pay_gateway_pay_client_env.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.channel_mch_no IS '通道商户号(DIRECT 模式填)';


--
-- Name: COLUMN pay_gateway_pay_client_env.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.capability IS '支付能力(DIRECT 模式填)';


--
-- Name: COLUMN pay_gateway_pay_client_env.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_gateway_pay_client_env.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.creator IS '创建者ID';


--
-- Name: COLUMN pay_gateway_pay_client_env.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.create_time IS '创建时间';


--
-- Name: COLUMN pay_gateway_pay_client_env.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_gateway_pay_client_env.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_gateway_pay_client_env.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_client_env.version IS '版本号(乐观锁)';


--
-- Name: pay_gateway_pay_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_gateway_pay_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(32) NOT NULL,
    level character varying(16) DEFAULT 'auto'::character varying NOT NULL,
    auto_launch boolean DEFAULT false NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL
);


--
-- Name: TABLE pay_gateway_pay_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_gateway_pay_config IS '网关支付配置(应用级, 码牌/聚合共用)';


--
-- Name: COLUMN pay_gateway_pay_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.id IS '主键';


--
-- Name: COLUMN pay_gateway_pay_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.mch_no IS '商户号';


--
-- Name: COLUMN pay_gateway_pay_config.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.app_id IS '应用号';


--
-- Name: COLUMN pay_gateway_pay_config.level; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.level IS '配置深度: auto/method/direct';


--
-- Name: COLUMN pay_gateway_pay_config.auto_launch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.auto_launch IS '是否自动拉起支付(码牌仅对固定金额生效)';


--
-- Name: COLUMN pay_gateway_pay_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_gateway_pay_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.creator IS '创建者ID';


--
-- Name: COLUMN pay_gateway_pay_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.create_time IS '创建时间';


--
-- Name: COLUMN pay_gateway_pay_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_gateway_pay_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_gateway_pay_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_gateway_pay_config.version IS '版本号(乐观锁)';


--
-- Name: pay_md_capability; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_capability (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    sort_no integer DEFAULT 0 NOT NULL,
    enabled boolean DEFAULT true NOT NULL,
    description character varying(512),
    deleted boolean DEFAULT false NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone
);


--
-- Name: TABLE pay_md_capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_capability IS '支付能力主数据';


--
-- Name: COLUMN pay_md_capability.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.id IS '主键';


--
-- Name: COLUMN pay_md_capability.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.code IS '支付能力编码（PayCapabilityEnum.code）';


--
-- Name: COLUMN pay_md_capability.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.sort_no IS '全局排序';


--
-- Name: COLUMN pay_md_capability.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.enabled IS '是否启用';


--
-- Name: COLUMN pay_md_capability.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.description IS '说明';


--
-- Name: COLUMN pay_md_capability.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_md_capability.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_md_capability.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_capability.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.version IS '版本号（乐观锁）';


--
-- Name: COLUMN pay_md_capability.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.creator IS '创建者ID';


--
-- Name: COLUMN pay_md_capability.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_capability.create_time IS '创建时间';


--
-- Name: pay_md_channel; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_channel (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    sort_no integer DEFAULT 0,
    icon character varying(200),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false
);


--
-- Name: TABLE pay_md_channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_channel IS '支付通道';


--
-- Name: COLUMN pay_md_channel.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.id IS '主键';


--
-- Name: COLUMN pay_md_channel.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.code IS '通道编码';


--
-- Name: COLUMN pay_md_channel.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.sort_no IS '排序';


--
-- Name: COLUMN pay_md_channel.icon; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.icon IS '图标';


--
-- Name: COLUMN pay_md_channel.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.creator IS '创建者ID';


--
-- Name: COLUMN pay_md_channel.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.create_time IS '创建时间';


--
-- Name: COLUMN pay_md_channel.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.last_modifier IS '最后修改ID';


--
-- Name: COLUMN pay_md_channel.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_channel.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.version IS '版本号';


--
-- Name: COLUMN pay_md_channel.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_channel.deleted IS '删除标志';


--
-- Name: pay_md_method; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_method (
    id bigint NOT NULL,
    code character varying(32) NOT NULL,
    sort_no integer DEFAULT 0 NOT NULL,
    description character varying(512),
    deleted boolean DEFAULT false NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone
);


--
-- Name: TABLE pay_md_method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_method IS '支付方式';


--
-- Name: COLUMN pay_md_method.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.id IS '主键';


--
-- Name: COLUMN pay_md_method.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.code IS '支付方式编码（全局唯一）';


--
-- Name: COLUMN pay_md_method.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.sort_no IS '全局排序';


--
-- Name: COLUMN pay_md_method.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.description IS '说明';


--
-- Name: COLUMN pay_md_method.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_md_method.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_md_method.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_method.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_md_method.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.creator IS '创建者ID';


--
-- Name: COLUMN pay_md_method.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_method.create_time IS '创建时间';


--
-- Name: pay_md_product; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_product (
    id bigint NOT NULL,
    code character varying(32) NOT NULL,
    name character varying(64) NOT NULL,
    channel character varying(32) NOT NULL,
    sort_no integer DEFAULT 0,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    sandbox boolean DEFAULT false,
    enabled boolean DEFAULT true NOT NULL
);


--
-- Name: TABLE pay_md_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_product IS '支付产品';


--
-- Name: COLUMN pay_md_product.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.id IS '主键';


--
-- Name: COLUMN pay_md_product.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.code IS '产品编码';


--
-- Name: COLUMN pay_md_product.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.name IS '产品名称';


--
-- Name: COLUMN pay_md_product.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.channel IS '关联通道编码';


--
-- Name: COLUMN pay_md_product.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.sort_no IS '排序';


--
-- Name: COLUMN pay_md_product.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.creator IS '创建者';


--
-- Name: COLUMN pay_md_product.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.create_time IS '创建时间';


--
-- Name: COLUMN pay_md_product.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.last_modifier IS '最后修改者';


--
-- Name: COLUMN pay_md_product.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_product.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.version IS '版本号';


--
-- Name: COLUMN pay_md_product.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_md_product.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.sandbox IS '是否支持沙箱环境';


--
-- Name: COLUMN pay_md_product.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product.enabled IS '是否启用该通道';


--
-- Name: pay_md_product_capability; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_product_capability (
    id bigint NOT NULL,
    product_code character varying(32) NOT NULL,
    capability_code character varying(64) NOT NULL,
    sort_no integer DEFAULT 0 NOT NULL,
    enabled boolean DEFAULT true NOT NULL,
    remark character varying(512),
    deleted boolean DEFAULT false NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone
);


--
-- Name: TABLE pay_md_product_capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_product_capability IS '支付产品与支付能力关联';


--
-- Name: COLUMN pay_md_product_capability.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.id IS '主键';


--
-- Name: COLUMN pay_md_product_capability.product_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.product_code IS '支付产品编码';


--
-- Name: COLUMN pay_md_product_capability.capability_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.capability_code IS '支付能力编码';


--
-- Name: COLUMN pay_md_product_capability.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.sort_no IS '排序';


--
-- Name: COLUMN pay_md_product_capability.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.enabled IS '是否启用';


--
-- Name: COLUMN pay_md_product_capability.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.remark IS '备注';


--
-- Name: COLUMN pay_md_product_capability.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.deleted IS '逻辑删除标志';


--
-- Name: COLUMN pay_md_product_capability.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_md_product_capability.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_product_capability.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_md_product_capability.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.creator IS '创建者ID';


--
-- Name: COLUMN pay_md_product_capability.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_capability.create_time IS '创建时间';


--
-- Name: pay_md_product_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_product_config (
    id bigint NOT NULL,
    product character varying(32) NOT NULL,
    channel character varying(32) NOT NULL,
    active_env character varying(32) DEFAULT 'prod'::character varying,
    remark character varying(255),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_md_product_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_product_config IS '支付产品配置';


--
-- Name: COLUMN pay_md_product_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.id IS '主键';


--
-- Name: COLUMN pay_md_product_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.product IS '产品编码';


--
-- Name: COLUMN pay_md_product_config.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.channel IS '通道编码';


--
-- Name: COLUMN pay_md_product_config.active_env; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.active_env IS '生效环境: prod/sandbox';


--
-- Name: COLUMN pay_md_product_config.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.remark IS '备注';


--
-- Name: COLUMN pay_md_product_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.creator IS '创建者ID';


--
-- Name: COLUMN pay_md_product_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.create_time IS '创建时间';


--
-- Name: COLUMN pay_md_product_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.last_modifier IS '最后修改ID';


--
-- Name: COLUMN pay_md_product_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_product_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.version IS '版本号';


--
-- Name: COLUMN pay_md_product_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_product_config.deleted IS '删除标志';


--
-- Name: pay_md_provider; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_provider (
    id bigint NOT NULL,
    code character varying(32) NOT NULL,
    icon character varying(255),
    sort_no integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    enabled boolean DEFAULT true NOT NULL,
    description character varying(500)
);


--
-- Name: TABLE pay_md_provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_provider IS '支付渠道';


--
-- Name: COLUMN pay_md_provider.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.id IS '主键';


--
-- Name: COLUMN pay_md_provider.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.code IS '支付渠道编码（PayProviderEnum.code：aggregate_pay/wechat/alipay/union_pay/visa/mastercard）';


--
-- Name: COLUMN pay_md_provider.icon; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.icon IS '图标（可选，覆盖前端默认展示）';


--
-- Name: COLUMN pay_md_provider.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.sort_no IS '排序（管理端 Tab/列表顺序）';


--
-- Name: COLUMN pay_md_provider.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.deleted IS '删除标志（逻辑删除）';


--
-- Name: COLUMN pay_md_provider.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_md_provider.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_provider.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.version IS '版本号（乐观锁）';


--
-- Name: COLUMN pay_md_provider.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.creator IS '创建者ID';


--
-- Name: COLUMN pay_md_provider.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.create_time IS '创建时间';


--
-- Name: COLUMN pay_md_provider.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.enabled IS '是否启用';


--
-- Name: COLUMN pay_md_provider.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider.description IS '描述';


--
-- Name: pay_md_provider_method; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_md_provider_method (
    id bigint NOT NULL,
    provider character varying(32) NOT NULL,
    method character varying(32) NOT NULL,
    sort_no integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    description character varying(500)
);


--
-- Name: TABLE pay_md_provider_method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_md_provider_method IS '支付渠道和方式关联';


--
-- Name: COLUMN pay_md_provider_method.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.id IS '主键';


--
-- Name: COLUMN pay_md_provider_method.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.provider IS '支付渠道编码（对应 PayProviderMethod.provider / PayProviderEnum.code）';


--
-- Name: COLUMN pay_md_provider_method.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.method IS '支付方式编码（PayMethodEnum.code）';


--
-- Name: COLUMN pay_md_provider_method.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.sort_no IS '渠道内排序';


--
-- Name: COLUMN pay_md_provider_method.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.deleted IS '删除标志（逻辑删除）';


--
-- Name: COLUMN pay_md_provider_method.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_md_provider_method.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_md_provider_method.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.version IS '版本号（乐观锁）';


--
-- Name: COLUMN pay_md_provider_method.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.creator IS '创建者ID';


--
-- Name: COLUMN pay_md_provider_method.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.create_time IS '创建时间';


--
-- Name: COLUMN pay_md_provider_method.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_md_provider_method.description IS '目录项说明';


--
-- Name: pay_normal_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_normal_order (
    id bigint NOT NULL,
    order_no character varying(100) NOT NULL,
    biz_order_no character varying(100) NOT NULL,
    source character varying(32),
    title character varying(100) NOT NULL,
    description character varying(500),
    status character varying(32) NOT NULL,
    notify_url character varying(200),
    return_url character varying(200),
    attach character varying(500),
    expired_time timestamp(6) with time zone,
    amount bigint NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    channel character varying(32),
    method character varying(32),
    limit_pay character varying(32),
    product character varying(32),
    openid character varying(128),
    auth_code character varying(128),
    pay_time timestamp(6) with time zone,
    close_time timestamp(6) with time zone,
    channel_mch_no character varying(64),
    capability character varying(64),
    channel_app_id character varying(128),
    provider character varying(32),
    buyer_id character varying(64),
    trade_product character varying(64),
    trade_way character varying(64),
    bank_type character varying(64),
    promotion_type character varying(64),
    pay_body text,
    pay_body_type character varying(32),
    trans_order_no character varying(150),
    relation_order_no character varying(150),
    extra_param character varying(2048),
    app_id character varying(32) NOT NULL,
    goods_detail jsonb,
    client_ip character varying(64),
    terminal_no character varying(128),
    store_no character varying(32),
    error_msg character varying(500),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    allocation boolean DEFAULT false
);


--
-- Name: TABLE pay_normal_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_normal_order IS '普通支付业务单容器';


--
-- Name: COLUMN pay_normal_order.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.id IS '主键';


--
-- Name: COLUMN pay_normal_order.order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.order_no IS '平台业务单号(容器身份)';


--
-- Name: COLUMN pay_normal_order.biz_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.biz_order_no IS '商户业务单号';


--
-- Name: COLUMN pay_normal_order.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.source IS '订单来源(业务入口权威, TradeSourceEnum)';


--
-- Name: COLUMN pay_normal_order.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.title IS '标题';


--
-- Name: COLUMN pay_normal_order.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.description IS '描述';


--
-- Name: COLUMN pay_normal_order.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.status IS '业务状态';


--
-- Name: COLUMN pay_normal_order.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.notify_url IS '异步通知地址';


--
-- Name: COLUMN pay_normal_order.return_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.return_url IS '同步跳转地址';


--
-- Name: COLUMN pay_normal_order.attach; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.attach IS '商户附加参数';


--
-- Name: COLUMN pay_normal_order.expired_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.expired_time IS '业务单过期时间';


--
-- Name: COLUMN pay_normal_order.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.amount IS '业务单金额(最小货币单位/分)';


--
-- Name: COLUMN pay_normal_order.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.currency IS '币种';


--
-- Name: COLUMN pay_normal_order.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.channel IS '支付通道编码';


--
-- Name: COLUMN pay_normal_order.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.method IS '支付方式';


--
-- Name: COLUMN pay_normal_order.limit_pay; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.limit_pay IS '限制支付类型';


--
-- Name: COLUMN pay_normal_order.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.product IS '支付产品编码';


--
-- Name: COLUMN pay_normal_order.openid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.openid IS '微信 openid';


--
-- Name: COLUMN pay_normal_order.auth_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.auth_code IS '付款码（被扫支付，审计保留）';


--
-- Name: COLUMN pay_normal_order.pay_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.pay_time IS '支付成功时间';


--
-- Name: COLUMN pay_normal_order.close_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.close_time IS '关闭时间';


--
-- Name: COLUMN pay_normal_order.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.channel_mch_no IS '通道商户号(路由回填)';


--
-- Name: COLUMN pay_normal_order.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.capability IS '支付能力编码(路由回填)';


--
-- Name: COLUMN pay_normal_order.channel_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.channel_app_id IS '通道应用 AppId 快照';


--
-- Name: COLUMN pay_normal_order.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.provider IS '支付渠道(微信/支付宝/银联等)';


--
-- Name: COLUMN pay_normal_order.buyer_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.buyer_id IS '付款用户标识';


--
-- Name: COLUMN pay_normal_order.trade_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.trade_product IS '通道方记录的支付产品';


--
-- Name: COLUMN pay_normal_order.trade_way; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.trade_way IS '通道方记录的交易方式';


--
-- Name: COLUMN pay_normal_order.bank_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.bank_type IS '银行卡类型';


--
-- Name: COLUMN pay_normal_order.promotion_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.promotion_type IS '活动类型';


--
-- Name: COLUMN pay_normal_order.pay_body; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.pay_body IS '支付参数体';


--
-- Name: COLUMN pay_normal_order.pay_body_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.pay_body_type IS '支付参数体类型';


--
-- Name: COLUMN pay_normal_order.trans_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.trans_order_no IS '透传订单号';


--
-- Name: COLUMN pay_normal_order.relation_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.relation_order_no IS '实际上送通道的商户订单号';


--
-- Name: COLUMN pay_normal_order.extra_param; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.extra_param IS '通道附加参数';


--
-- Name: COLUMN pay_normal_order.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.app_id IS '应用号';


--
-- Name: COLUMN pay_normal_order.goods_detail; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.goods_detail IS '订单商品明细(jsonb)';


--
-- Name: COLUMN pay_normal_order.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.client_ip IS '下单客户端 IP';


--
-- Name: COLUMN pay_normal_order.terminal_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.terminal_no IS '终端设备编码';


--
-- Name: COLUMN pay_normal_order.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.store_no IS '门店号(线下经营归属, 可空)';


--
-- Name: COLUMN pay_normal_order.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.error_msg IS '错误信息';


--
-- Name: COLUMN pay_normal_order.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.mch_no IS '商户号';


--
-- Name: COLUMN pay_normal_order.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.creator IS '创建人';


--
-- Name: COLUMN pay_normal_order.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.create_time IS '创建时间';


--
-- Name: COLUMN pay_normal_order.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_normal_order.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_normal_order.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.version IS '乐观锁版本';


--
-- Name: COLUMN pay_normal_order.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_normal_order.allocation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_normal_order.allocation IS '是否分账订单(下单时透传通道分账标识, true 表示资金冻结仅可分账拆分)';


--
-- Name: pay_platform_mobile_app; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_platform_mobile_app (
    id bigint NOT NULL,
    app_type character varying(20) NOT NULL,
    platform character varying(20) NOT NULL,
    app_config text,
    notify_config jsonb,
    binding_enabled boolean DEFAULT false NOT NULL,
    enabled boolean DEFAULT true NOT NULL,
    remark character varying(500),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_platform_mobile_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_platform_mobile_app IS '平台级移动端应用配置';


--
-- Name: COLUMN pay_platform_mobile_app.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.id IS '主键';


--
-- Name: COLUMN pay_platform_mobile_app.app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.app_type IS '端类型';


--
-- Name: COLUMN pay_platform_mobile_app.platform; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.platform IS '移动平台: wx_h5/wx_mini/alipay_mini/dy_mini/android/ios';


--
-- Name: COLUMN pay_platform_mobile_app.app_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.app_config IS '平台特有密钥配置(JSON文本, AES-256-GCM加密存储)';


--
-- Name: COLUMN pay_platform_mobile_app.notify_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.notify_config IS '消息通知配置(jsonb, 明文, 非敏感)';


--
-- Name: COLUMN pay_platform_mobile_app.binding_enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.binding_enabled IS '是否启用第三方账号用户绑定';


--
-- Name: COLUMN pay_platform_mobile_app.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.enabled IS '是否启用';


--
-- Name: COLUMN pay_platform_mobile_app.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.remark IS '备注';


--
-- Name: COLUMN pay_platform_mobile_app.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.creator IS '创建者ID';


--
-- Name: COLUMN pay_platform_mobile_app.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.create_time IS '创建时间';


--
-- Name: COLUMN pay_platform_mobile_app.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_platform_mobile_app.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_platform_mobile_app.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.version IS '版本号(乐观锁)';


--
-- Name: COLUMN pay_platform_mobile_app.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_platform_mobile_app.deleted IS '逻辑删除标记';


--
-- Name: pay_refund_order; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_refund_order (
    id bigint NOT NULL,
    app_id character varying(32) NOT NULL,
    refund_no character varying(100) NOT NULL,
    biz_refund_no character varying(100),
    title character varying(100),
    trade_no character varying(100) NOT NULL,
    biz_order_no character varying(100),
    out_order_no character varying(150),
    out_refund_no character varying(150),
    amount bigint NOT NULL,
    order_amount bigint,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    reason character varying(500),
    status character varying(32) NOT NULL,
    finish_time timestamp(6) with time zone,
    channel character varying(32),
    product character varying(32),
    channel_mch_no character varying(64),
    capability character varying(64),
    channel_app_id character varying(128),
    notify_url character varying(200),
    attach character varying(500),
    client_ip character varying(64),
    store_no character varying(32),
    error_msg character varying(500),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    trade_type character varying(32),
    relation_order_no character varying(150)
);


--
-- Name: TABLE pay_refund_order; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_refund_order IS '退款订单';


--
-- Name: COLUMN pay_refund_order.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.id IS '主键';


--
-- Name: COLUMN pay_refund_order.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.app_id IS '应用号';


--
-- Name: COLUMN pay_refund_order.refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.refund_no IS '退款号(平台统一生成)';


--
-- Name: COLUMN pay_refund_order.biz_refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.biz_refund_no IS '商户退款号';


--
-- Name: COLUMN pay_refund_order.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.title IS '标题';


--
-- Name: COLUMN pay_refund_order.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.trade_no IS '原支付资金交易号(= pay_trade.trade_no，非容器 orderNo)';


--
-- Name: COLUMN pay_refund_order.biz_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.biz_order_no IS '商户业务订单号';


--
-- Name: COLUMN pay_refund_order.out_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.out_order_no IS '通道支付订单号';


--
-- Name: COLUMN pay_refund_order.out_refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.out_refund_no IS '通道退款流水号';


--
-- Name: COLUMN pay_refund_order.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.amount IS '退款金额(分)';


--
-- Name: COLUMN pay_refund_order.order_amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.order_amount IS '订单总金额(冗余, 分)';


--
-- Name: COLUMN pay_refund_order.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.currency IS '币种';


--
-- Name: COLUMN pay_refund_order.reason; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.reason IS '退款原因';


--
-- Name: COLUMN pay_refund_order.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.status IS '退款状态';


--
-- Name: COLUMN pay_refund_order.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.finish_time IS '退款完成时间';


--
-- Name: COLUMN pay_refund_order.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.channel IS '支付通道';


--
-- Name: COLUMN pay_refund_order.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.product IS '支付产品编码';


--
-- Name: COLUMN pay_refund_order.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.channel_mch_no IS '通道商户号(路由回填)';


--
-- Name: COLUMN pay_refund_order.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.capability IS '支付能力编码(路由回填)';


--
-- Name: COLUMN pay_refund_order.channel_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.channel_app_id IS '通道应用 AppId 快照';


--
-- Name: COLUMN pay_refund_order.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.notify_url IS '异步通知地址';


--
-- Name: COLUMN pay_refund_order.attach; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.attach IS '商户附加参数';


--
-- Name: COLUMN pay_refund_order.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.client_ip IS '客户端 IP';


--
-- Name: COLUMN pay_refund_order.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.store_no IS '门店号(继承自原支付容器, 可空)';


--
-- Name: COLUMN pay_refund_order.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.error_msg IS '错误信息';


--
-- Name: COLUMN pay_refund_order.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.mch_no IS '商户号';


--
-- Name: COLUMN pay_refund_order.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.creator IS '创建人';


--
-- Name: COLUMN pay_refund_order.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.create_time IS '创建时间';


--
-- Name: COLUMN pay_refund_order.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_refund_order.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_refund_order.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.version IS '乐观锁版本';


--
-- Name: COLUMN pay_refund_order.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_refund_order.trade_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.trade_type IS '原支付交易形态(冗余自 pay_trade.trade_type，如 normal/gateway)';


--
-- Name: COLUMN pay_refund_order.relation_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_refund_order.relation_order_no IS '实际上送通道的商户退款关联号(普通通道=refund_no；特殊通道可变形)';


--
-- Name: pay_risk_hit; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_risk_hit (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    phase character varying(16) NOT NULL,
    hit_type character varying(32) NOT NULL,
    hit_value character varying(128) NOT NULL,
    blacklist_id bigint,
    mch_no character varying(32),
    app_id character varying(64),
    trade_no character varying(64),
    order_no character varying(64),
    biz_order_no character varying(128),
    trade_type character varying(32),
    method character varying(32),
    product character varying(32),
    channel character varying(32),
    client_ip character varying(64),
    openid character varying(128),
    buyer_id character varying(128),
    scene character varying(16) DEFAULT 'unknown'::character varying NOT NULL,
    remark character varying(255),
    client_city character varying(64),
    store_city character varying(64),
    store_no character varying(64),
    geo_fence_strategy character varying(16)
);


--
-- Name: TABLE pay_risk_hit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_risk_hit IS '支付风险命中记录（事前拦截与事后命中，供运营预警与处置）';


--
-- Name: COLUMN pay_risk_hit.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.id IS '主键';


--
-- Name: COLUMN pay_risk_hit.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.creator IS '创建者ID';


--
-- Name: COLUMN pay_risk_hit.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.create_time IS '创建时间';


--
-- Name: COLUMN pay_risk_hit.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.last_modifier IS '最后修改ID';


--
-- Name: COLUMN pay_risk_hit.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_risk_hit.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.version IS '版本号';


--
-- Name: COLUMN pay_risk_hit.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.deleted IS '删除标志';


--
-- Name: COLUMN pay_risk_hit.phase; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.phase IS '命中阶段';


--
-- Name: COLUMN pay_risk_hit.hit_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.hit_type IS '命中类型（与黑名单 type 一致: ip / open_id）';


--
-- Name: COLUMN pay_risk_hit.hit_value; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.hit_value IS '命中值快照（IP 或 openId）';


--
-- Name: COLUMN pay_risk_hit.blacklist_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.blacklist_id IS '关联名单 ID（可空）';


--
-- Name: COLUMN pay_risk_hit.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.mch_no IS '商户号（业务字段，非租户行级隔离）';


--
-- Name: COLUMN pay_risk_hit.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.app_id IS '应用号';


--
-- Name: COLUMN pay_risk_hit.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.trade_no IS '平台交易号';


--
-- Name: COLUMN pay_risk_hit.order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.order_no IS '容器单号';


--
-- Name: COLUMN pay_risk_hit.biz_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.biz_order_no IS '商户业务单号';


--
-- Name: COLUMN pay_risk_hit.trade_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.trade_type IS '交易类型: normal / gateway';


--
-- Name: COLUMN pay_risk_hit.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.method IS '支付方式';


--
-- Name: COLUMN pay_risk_hit.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.product IS '支付产品';


--
-- Name: COLUMN pay_risk_hit.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.channel IS '支付通道';


--
-- Name: COLUMN pay_risk_hit.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.client_ip IS '客户端 IP 快照';


--
-- Name: COLUMN pay_risk_hit.openid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.openid IS '下单 openId 快照';


--
-- Name: COLUMN pay_risk_hit.buyer_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.buyer_id IS '通道回写付款人标识（buyerId / openId）';


--
-- Name: COLUMN pay_risk_hit.scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.scene IS '来源场景: api/gateway/code/manual/unknown';


--
-- Name: COLUMN pay_risk_hit.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.remark IS '备注';


--
-- Name: COLUMN pay_risk_hit.client_city; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.client_city IS '客户端 IP 归属城市（ip2region 解析快照）';


--
-- Name: COLUMN pay_risk_hit.store_city; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.store_city IS '门店所在城市（围栏命中快照）';


--
-- Name: COLUMN pay_risk_hit.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.store_no IS '门店号（围栏命中快照）';


--
-- Name: COLUMN pay_risk_hit.geo_fence_strategy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_risk_hit.geo_fence_strategy IS '地理围栏命中时生效的策略（strict/balanced/loose）';


--
-- Name: pay_route_basic_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_route_basic_config (
    id bigint NOT NULL,
    strategy_id bigint NOT NULL,
    provider character varying(32) NOT NULL,
    channel_mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_route_basic_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_route_basic_config IS '支付通道路由基础模式配置';


--
-- Name: COLUMN pay_route_basic_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.id IS '主键';


--
-- Name: COLUMN pay_route_basic_config.strategy_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.strategy_id IS '路由策略ID';


--
-- Name: COLUMN pay_route_basic_config.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.provider IS '支付渠道';


--
-- Name: COLUMN pay_route_basic_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN pay_route_basic_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.creator IS '创建人ID';


--
-- Name: COLUMN pay_route_basic_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.create_time IS '创建时间';


--
-- Name: COLUMN pay_route_basic_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN pay_route_basic_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_route_basic_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.version IS '版本号';


--
-- Name: COLUMN pay_route_basic_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_basic_config.deleted IS '逻辑删除';


--
-- Name: pay_route_scene_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_route_scene_config (
    id bigint NOT NULL,
    strategy_id bigint NOT NULL,
    method character varying(32) NOT NULL,
    channel_mch_no character varying(32) NOT NULL,
    capability character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_route_scene_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_route_scene_config IS '支付通道路由场景模式配置';


--
-- Name: COLUMN pay_route_scene_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.id IS '主键';


--
-- Name: COLUMN pay_route_scene_config.strategy_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.strategy_id IS '路由策略ID';


--
-- Name: COLUMN pay_route_scene_config.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.method IS '支付方式编码';


--
-- Name: COLUMN pay_route_scene_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.channel_mch_no IS '通道商户号(唯一绑定支付产品)';


--
-- Name: COLUMN pay_route_scene_config.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.capability IS '支付能力编码';


--
-- Name: COLUMN pay_route_scene_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.creator IS '创建人ID';


--
-- Name: COLUMN pay_route_scene_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.create_time IS '创建时间';


--
-- Name: COLUMN pay_route_scene_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN pay_route_scene_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_route_scene_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.version IS '版本号';


--
-- Name: COLUMN pay_route_scene_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_scene_config.deleted IS '逻辑删除';


--
-- Name: pay_route_strategy; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_route_strategy (
    id bigint NOT NULL,
    app_id character varying(32) NOT NULL,
    mch_no character varying(32) NOT NULL,
    mode character varying(32),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_route_strategy; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_route_strategy IS '支付通道路由策略';


--
-- Name: COLUMN pay_route_strategy.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.id IS '主键';


--
-- Name: COLUMN pay_route_strategy.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.app_id IS '应用号';


--
-- Name: COLUMN pay_route_strategy.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.mch_no IS '商户号';


--
-- Name: COLUMN pay_route_strategy.mode; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.mode IS '路由模式：basic / scene';


--
-- Name: COLUMN pay_route_strategy.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.creator IS '创建者ID';


--
-- Name: COLUMN pay_route_strategy.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.create_time IS '创建时间';


--
-- Name: COLUMN pay_route_strategy.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.last_modifier IS '最后修改ID';


--
-- Name: COLUMN pay_route_strategy.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_route_strategy.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.version IS '版本号（乐观锁）';


--
-- Name: COLUMN pay_route_strategy.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_route_strategy.deleted IS '删除标志';


--
-- Name: pay_sync_record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_sync_record (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_id character varying(32),
    trade_no character varying(64) NOT NULL,
    biz_trade_no character varying(64),
    out_trade_no character varying(64),
    out_trade_status character varying(32),
    trade_type character varying(32),
    product character varying(32),
    channel character varying(32),
    sync_info text,
    adjust boolean DEFAULT false NOT NULL,
    error_code character varying(128),
    error_msg character varying(300),
    client_ip character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone DEFAULT now(),
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_sync_record; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_sync_record IS '支付同步记录';


--
-- Name: COLUMN pay_sync_record.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.id IS '主键';


--
-- Name: COLUMN pay_sync_record.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.mch_no IS '商户号';


--
-- Name: COLUMN pay_sync_record.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.app_id IS '应用号';


--
-- Name: COLUMN pay_sync_record.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.trade_no IS '平台交易号(对应 pay_trade.trade_no)';


--
-- Name: COLUMN pay_sync_record.biz_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.biz_trade_no IS '商户业务单号(对应 pay_normal_order.biz_order_no)';


--
-- Name: COLUMN pay_sync_record.out_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.out_trade_no IS '通道交易号(三方通道返回的订单号)';


--
-- Name: COLUMN pay_sync_record.out_trade_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.out_trade_status IS '通道返回的资金状态';


--
-- Name: COLUMN pay_sync_record.trade_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.trade_type IS '交易类型';


--
-- Name: COLUMN pay_sync_record.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.product IS '支付产品编码';


--
-- Name: COLUMN pay_sync_record.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.channel IS '支付通道';


--
-- Name: COLUMN pay_sync_record.sync_info; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.sync_info IS '网关返回的同步原始报文(json)';


--
-- Name: COLUMN pay_sync_record.adjust; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.adjust IS '本地与通道状态不一致时是否进行了调整';


--
-- Name: COLUMN pay_sync_record.error_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.error_code IS '错误码';


--
-- Name: COLUMN pay_sync_record.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.error_msg IS '错误信息';


--
-- Name: COLUMN pay_sync_record.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.client_ip IS '客户端 IP';


--
-- Name: COLUMN pay_sync_record.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.creator IS '创建人';


--
-- Name: COLUMN pay_sync_record.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.create_time IS '创建时间';


--
-- Name: COLUMN pay_sync_record.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_sync_record.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_sync_record.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_sync_record.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_sync_record.deleted IS '逻辑删除标志';


--
-- Name: pay_sync_record_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pay_sync_record_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: pay_sync_record_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.pay_sync_record_id_seq OWNED BY public.pay_sync_record.id;


--
-- Name: pay_terminal_channel_bind; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_terminal_channel_bind (
    id bigint NOT NULL,
    system_terminal_no character varying(32) NOT NULL,
    channel_terminal_id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_terminal_channel_bind; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_terminal_channel_bind IS '系统终端与通道终端多对多绑定';


--
-- Name: COLUMN pay_terminal_channel_bind.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.id IS '主键';


--
-- Name: COLUMN pay_terminal_channel_bind.system_terminal_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.system_terminal_no IS '系统终端编码';


--
-- Name: COLUMN pay_terminal_channel_bind.channel_terminal_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.channel_terminal_id IS '通道终端主键';


--
-- Name: COLUMN pay_terminal_channel_bind.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.mch_no IS '商户号';


--
-- Name: COLUMN pay_terminal_channel_bind.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.creator IS '创建者ID';


--
-- Name: COLUMN pay_terminal_channel_bind.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.create_time IS '创建时间';


--
-- Name: COLUMN pay_terminal_channel_bind.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_terminal_channel_bind.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_terminal_channel_bind.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_terminal_channel_bind.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_channel_bind.deleted IS '删除标志';


--
-- Name: pay_terminal_device; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_terminal_device (
    id bigint NOT NULL,
    terminal_no character varying(32) NOT NULL,
    name character varying(100) NOT NULL,
    store_no character varying(64),
    enable boolean DEFAULT true NOT NULL,
    remark character varying(255),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_terminal_device; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_terminal_device IS '系统终端(逻辑终端台账)';


--
-- Name: COLUMN pay_terminal_device.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.id IS '主键';


--
-- Name: COLUMN pay_terminal_device.terminal_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.terminal_no IS '系统终端编码(平台生成)';


--
-- Name: COLUMN pay_terminal_device.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.name IS '终端名称';


--
-- Name: COLUMN pay_terminal_device.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.store_no IS '绑定门店号(可空, 门店1:N终端)';


--
-- Name: COLUMN pay_terminal_device.enable; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.enable IS '是否启用';


--
-- Name: COLUMN pay_terminal_device.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.remark IS '备注';


--
-- Name: COLUMN pay_terminal_device.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.mch_no IS '商户号';


--
-- Name: COLUMN pay_terminal_device.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.creator IS '创建者ID';


--
-- Name: COLUMN pay_terminal_device.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.create_time IS '创建时间';


--
-- Name: COLUMN pay_terminal_device.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN pay_terminal_device.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_terminal_device.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.version IS '乐观锁版本号';


--
-- Name: COLUMN pay_terminal_device.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_terminal_device.deleted IS '删除标志';


--
-- Name: pay_trade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_trade (
    id bigint NOT NULL,
    trade_no character varying(100) NOT NULL,
    trade_type character varying(32) NOT NULL,
    container_id bigint NOT NULL,
    amount bigint NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    posted_amount bigint DEFAULT 0 NOT NULL,
    refundable_balance bigint DEFAULT 0 NOT NULL,
    status character varying(32) NOT NULL,
    pay_time timestamp(6) with time zone,
    close_time timestamp(6) with time zone,
    source character varying(32),
    channel_mch_no character varying(64),
    store_no character varying(32),
    out_order_no character varying(150),
    relation_order_no character varying(150),
    app_id character varying(32) NOT NULL,
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    provider character varying(32),
    title character varying(255),
    channel character varying(32),
    alloc_status character varying(32)
);


--
-- Name: TABLE pay_trade; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_trade IS '资金交易凭证';


--
-- Name: COLUMN pay_trade.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.id IS '主键';


--
-- Name: COLUMN pay_trade.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.trade_no IS '资金交易号(平台生成)';


--
-- Name: COLUMN pay_trade.trade_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.trade_type IS '交易形态(normal/gateway/authorize 等)';


--
-- Name: COLUMN pay_trade.container_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.container_id IS '关联业务容器ID';


--
-- Name: COLUMN pay_trade.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.amount IS '本次交易金额(最小货币单位/分)';


--
-- Name: COLUMN pay_trade.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.currency IS '币种';


--
-- Name: COLUMN pay_trade.posted_amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.posted_amount IS '入账金额(分; 结算类 SUCCESS 时=amount)';


--
-- Name: COLUMN pay_trade.refundable_balance; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.refundable_balance IS '可退金额(分)';


--
-- Name: COLUMN pay_trade.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.status IS '资金状态';


--
-- Name: COLUMN pay_trade.pay_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.pay_time IS '支付成功时间';


--
-- Name: COLUMN pay_trade.close_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.close_time IS '关闭时间';


--
-- Name: COLUMN pay_trade.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.source IS '订单来源(冗余自容器; 权威在容器)';


--
-- Name: COLUMN pay_trade.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.channel_mch_no IS '通道商户号(冗余自业务容器, 路由确定后写入)';


--
-- Name: COLUMN pay_trade.store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.store_no IS '门店号(冗余自业务容器, 可空; 权威在容器)';


--
-- Name: COLUMN pay_trade.out_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.out_order_no IS '通道订单号';


--
-- Name: COLUMN pay_trade.relation_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.relation_order_no IS '实际上送通道的商户订单号(反查权威)';


--
-- Name: COLUMN pay_trade.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.app_id IS '应用号';


--
-- Name: COLUMN pay_trade.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.mch_no IS '商户号';


--
-- Name: COLUMN pay_trade.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.creator IS '创建人';


--
-- Name: COLUMN pay_trade.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.create_time IS '创建时间';


--
-- Name: COLUMN pay_trade.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_trade.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_trade.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.version IS '乐观锁版本';


--
-- Name: COLUMN pay_trade.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_trade.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.provider IS '支付渠道(冗余自容器, 支付成功sync后回填; 权威在容器 provider)';


--
-- Name: COLUMN pay_trade.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.title IS '订单标题';


--
-- Name: COLUMN pay_trade.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.channel IS '支付通道';


--
-- Name: COLUMN pay_trade.alloc_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_trade.alloc_status IS '分账状态(none-未分账/processing-分账中/done-已分账)';


--
-- Name: pay_transfer_order_alipay; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_transfer_order_alipay (
    id bigint NOT NULL,
    transfer_no character varying(32) NOT NULL,
    biz_transfer_no character varying(100) NOT NULL,
    channel_mch_no character varying(64),
    out_transfer_no character varying(150),
    mch_no character varying(32) NOT NULL,
    amount bigint NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    title character varying(100),
    reason character varying(200),
    status character varying(32) NOT NULL,
    finish_time timestamp(6) with time zone,
    notify_url character varying(200),
    attach character varying(500),
    req_time timestamp(6) with time zone,
    error_msg character varying(2048),
    payee_type character varying(32) NOT NULL,
    payee_account character varying(100) NOT NULL,
    payee_name character varying(100),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    pay_fund_order_id character varying(64),
    transfer_scene_config_id character varying(32),
    report_infos text,
    transfer_scene character varying(50)
);


--
-- Name: TABLE pay_transfer_order_alipay; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_transfer_order_alipay IS '支付宝转账单';


--
-- Name: COLUMN pay_transfer_order_alipay.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.id IS '主键';


--
-- Name: COLUMN pay_transfer_order_alipay.transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.transfer_no IS '平台转账单号';


--
-- Name: COLUMN pay_transfer_order_alipay.biz_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.biz_transfer_no IS '商户转账号(幂等键, 同一商户下唯一)';


--
-- Name: COLUMN pay_transfer_order_alipay.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.channel_mch_no IS '通道商户号(路由确定后写入, 凭证组装用)';


--
-- Name: COLUMN pay_transfer_order_alipay.out_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.out_transfer_no IS '通道转账单号(支付宝 order_id)';


--
-- Name: COLUMN pay_transfer_order_alipay.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.mch_no IS '商户号';


--
-- Name: COLUMN pay_transfer_order_alipay.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.amount IS '转账金额(最小货币单位/分)';


--
-- Name: COLUMN pay_transfer_order_alipay.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.currency IS '币种';


--
-- Name: COLUMN pay_transfer_order_alipay.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.title IS '转账标题';


--
-- Name: COLUMN pay_transfer_order_alipay.reason; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.reason IS '转账原因/备注';


--
-- Name: COLUMN pay_transfer_order_alipay.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.status IS '转账状态(processing/success/fail/close)';


--
-- Name: COLUMN pay_transfer_order_alipay.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.finish_time IS '转账完成时间';


--
-- Name: COLUMN pay_transfer_order_alipay.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.notify_url IS '商户异步通知地址';


--
-- Name: COLUMN pay_transfer_order_alipay.attach; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.attach IS '商户扩展参数(回调原样返回)';


--
-- Name: COLUMN pay_transfer_order_alipay.req_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.req_time IS '请求时间';


--
-- Name: COLUMN pay_transfer_order_alipay.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.error_msg IS '错误信息';


--
-- Name: COLUMN pay_transfer_order_alipay.payee_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.payee_type IS '收款人账号类型(user_id/open_id/login_name)';


--
-- Name: COLUMN pay_transfer_order_alipay.payee_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.payee_account IS '收款人账号';


--
-- Name: COLUMN pay_transfer_order_alipay.payee_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.payee_name IS '收款人姓名';


--
-- Name: COLUMN pay_transfer_order_alipay.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.creator IS '创建人';


--
-- Name: COLUMN pay_transfer_order_alipay.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.create_time IS '创建时间';


--
-- Name: COLUMN pay_transfer_order_alipay.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_transfer_order_alipay.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_transfer_order_alipay.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.version IS '乐观锁版本';


--
-- Name: COLUMN pay_transfer_order_alipay.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_transfer_order_alipay.pay_fund_order_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.pay_fund_order_id IS '支付宝资金流水号(财务对账,区别于 out_transfer_no)';


--
-- Name: COLUMN pay_transfer_order_alipay.transfer_scene_config_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.transfer_scene_config_id IS '转账场景配置ID(支付宝专用, FAIL重试时恢复场景用)';


--
-- Name: COLUMN pay_transfer_order_alipay.report_infos; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.report_infos IS '转账场景报备信息(JSON序列化, FAIL重试时恢复报备用)';


--
-- Name: COLUMN pay_transfer_order_alipay.transfer_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_alipay.transfer_scene IS '转账场景标识(支付宝=转账场景配置ID, FAIL重试时恢复场景用)';


--
-- Name: pay_transfer_order_douyin; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_transfer_order_douyin (
    id bigint NOT NULL,
    transfer_no character varying(32) NOT NULL,
    biz_transfer_no character varying(100) NOT NULL,
    channel_mch_no character varying(64),
    out_transfer_no character varying(150),
    mch_no character varying(32) NOT NULL,
    amount bigint NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    title character varying(100),
    reason character varying(200),
    status character varying(32) NOT NULL,
    finish_time timestamp(6) with time zone,
    notify_url character varying(200),
    attach character varying(500),
    req_time timestamp(6) with time zone,
    error_msg character varying(2048),
    payee_type character varying(32) NOT NULL,
    payee_account character varying(100) NOT NULL,
    payee_name character varying(100),
    transfer_scene character varying(50),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    user_recv_perception character varying(64),
    report_infos text
);


--
-- Name: TABLE pay_transfer_order_douyin; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_transfer_order_douyin IS '抖音转账单';


--
-- Name: COLUMN pay_transfer_order_douyin.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.id IS '主键';


--
-- Name: COLUMN pay_transfer_order_douyin.transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.transfer_no IS '平台转账单号';


--
-- Name: COLUMN pay_transfer_order_douyin.biz_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.biz_transfer_no IS '商户转账号(幂等键, 同一商户下唯一)';


--
-- Name: COLUMN pay_transfer_order_douyin.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.channel_mch_no IS '通道商户号(路由确定后写入, 凭证组装用)';


--
-- Name: COLUMN pay_transfer_order_douyin.out_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.out_transfer_no IS '通道转账单号(抖音 orderId)';


--
-- Name: COLUMN pay_transfer_order_douyin.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.mch_no IS '商户号';


--
-- Name: COLUMN pay_transfer_order_douyin.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.amount IS '转账金额(最小货币单位/分)';


--
-- Name: COLUMN pay_transfer_order_douyin.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.currency IS '币种';


--
-- Name: COLUMN pay_transfer_order_douyin.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.title IS '转账标题';


--
-- Name: COLUMN pay_transfer_order_douyin.reason; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.reason IS '转账原因/备注';


--
-- Name: COLUMN pay_transfer_order_douyin.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.status IS '转账状态(processing/success/fail/close)';


--
-- Name: COLUMN pay_transfer_order_douyin.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.finish_time IS '转账完成时间';


--
-- Name: COLUMN pay_transfer_order_douyin.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.notify_url IS '商户异步通知地址';


--
-- Name: COLUMN pay_transfer_order_douyin.attach; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.attach IS '商户扩展参数(回调原样返回)';


--
-- Name: COLUMN pay_transfer_order_douyin.req_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.req_time IS '请求时间';


--
-- Name: COLUMN pay_transfer_order_douyin.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.error_msg IS '错误信息';


--
-- Name: COLUMN pay_transfer_order_douyin.payee_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.payee_type IS '收款人账号类型';


--
-- Name: COLUMN pay_transfer_order_douyin.payee_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.payee_account IS '收款人账号';


--
-- Name: COLUMN pay_transfer_order_douyin.payee_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.payee_name IS '收款人姓名';


--
-- Name: COLUMN pay_transfer_order_douyin.transfer_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.transfer_scene IS '转账场景标识(抖音=转账场景配置ID, FAIL重试时恢复场景用)';


--
-- Name: COLUMN pay_transfer_order_douyin.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.creator IS '创建人';


--
-- Name: COLUMN pay_transfer_order_douyin.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.create_time IS '创建时间';


--
-- Name: COLUMN pay_transfer_order_douyin.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_transfer_order_douyin.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_transfer_order_douyin.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.version IS '乐观锁版本';


--
-- Name: COLUMN pay_transfer_order_douyin.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_transfer_order_douyin.user_recv_perception; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.user_recv_perception IS '用户收款感知(收款人在抖音中看到的文案)';


--
-- Name: COLUMN pay_transfer_order_douyin.report_infos; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_douyin.report_infos IS '转账场景报备信息(JSON序列化, FAIL重试时恢复报备用)';


--
-- Name: pay_transfer_order_wechat; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_transfer_order_wechat (
    id bigint NOT NULL,
    transfer_no character varying(32) NOT NULL,
    biz_transfer_no character varying(100) NOT NULL,
    channel_mch_no character varying(64),
    out_transfer_no character varying(150),
    mch_no character varying(32) NOT NULL,
    amount bigint NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    title character varying(100),
    reason character varying(200),
    status character varying(32) NOT NULL,
    finish_time timestamp(6) with time zone,
    notify_url character varying(200),
    attach character varying(500),
    req_time timestamp(6) with time zone,
    error_msg character varying(2048),
    payee_openid character varying(100) NOT NULL,
    transfer_scene character varying(50),
    transfer_body character varying(2000),
    user_name character varying(100),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    wx_app_id character varying(64)
);


--
-- Name: TABLE pay_transfer_order_wechat; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_transfer_order_wechat IS '微信转账单';


--
-- Name: COLUMN pay_transfer_order_wechat.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.id IS '主键';


--
-- Name: COLUMN pay_transfer_order_wechat.transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.transfer_no IS '平台转账单号';


--
-- Name: COLUMN pay_transfer_order_wechat.biz_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.biz_transfer_no IS '商户转账号(幂等键, 同一商户下唯一)';


--
-- Name: COLUMN pay_transfer_order_wechat.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.channel_mch_no IS '通道商户号(路由确定后写入, 凭证组装用)';


--
-- Name: COLUMN pay_transfer_order_wechat.out_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.out_transfer_no IS '通道转账单号(微信 paymentNo)';


--
-- Name: COLUMN pay_transfer_order_wechat.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.mch_no IS '商户号';


--
-- Name: COLUMN pay_transfer_order_wechat.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.amount IS '转账金额(最小货币单位/分)';


--
-- Name: COLUMN pay_transfer_order_wechat.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.currency IS '币种';


--
-- Name: COLUMN pay_transfer_order_wechat.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.title IS '转账标题';


--
-- Name: COLUMN pay_transfer_order_wechat.reason; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.reason IS '转账原因/备注';


--
-- Name: COLUMN pay_transfer_order_wechat.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.status IS '转账状态(processing/success/fail/close)';


--
-- Name: COLUMN pay_transfer_order_wechat.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.finish_time IS '转账完成时间';


--
-- Name: COLUMN pay_transfer_order_wechat.notify_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.notify_url IS '商户异步通知地址';


--
-- Name: COLUMN pay_transfer_order_wechat.attach; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.attach IS '商户扩展参数(回调原样返回)';


--
-- Name: COLUMN pay_transfer_order_wechat.req_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.req_time IS '请求时间';


--
-- Name: COLUMN pay_transfer_order_wechat.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.error_msg IS '错误信息';


--
-- Name: COLUMN pay_transfer_order_wechat.payee_openid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.payee_openid IS '收款人微信 openid';


--
-- Name: COLUMN pay_transfer_order_wechat.transfer_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.transfer_scene IS '转账场景(冗余自通道商户配置)';


--
-- Name: COLUMN pay_transfer_order_wechat.transfer_body; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.transfer_body IS '拉起转账确认参数(微信二次确认)';


--
-- Name: COLUMN pay_transfer_order_wechat.user_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.user_name IS '收款人姓名(金额档位校验用)';


--
-- Name: COLUMN pay_transfer_order_wechat.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.creator IS '创建人';


--
-- Name: COLUMN pay_transfer_order_wechat.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.create_time IS '创建时间';


--
-- Name: COLUMN pay_transfer_order_wechat.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_transfer_order_wechat.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_transfer_order_wechat.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.version IS '乐观锁版本';


--
-- Name: COLUMN pay_transfer_order_wechat.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.deleted IS '逻辑删除';


--
-- Name: COLUMN pay_transfer_order_wechat.wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_order_wechat.wx_app_id IS '转账发起应用AppId(从转账配置解析, openid归属校验用)';


--
-- Name: pay_transfer_trade; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.pay_transfer_trade (
    id bigint NOT NULL,
    trade_no character varying(32) NOT NULL,
    biz_transfer_no character varying(100),
    container_id bigint NOT NULL,
    container_channel character varying(32) NOT NULL,
    channel character varying(32),
    provider character varying(32),
    amount bigint NOT NULL,
    currency character varying(8) DEFAULT 'CNY'::character varying NOT NULL,
    status character varying(32) NOT NULL,
    out_transfer_no character varying(150),
    relation_no character varying(150),
    finish_time timestamp(6) with time zone,
    title character varying(100),
    mch_no character varying(32) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE pay_transfer_trade; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.pay_transfer_trade IS '转账资金凭证';


--
-- Name: COLUMN pay_transfer_trade.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.id IS '主键';


--
-- Name: COLUMN pay_transfer_trade.trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.trade_no IS '平台转账交易号';


--
-- Name: COLUMN pay_transfer_trade.biz_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.biz_transfer_no IS '商户转账号(冗余自容器, 同步记录/日志免回容器)';


--
-- Name: COLUMN pay_transfer_trade.container_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.container_id IS '关联通道转账单ID';


--
-- Name: COLUMN pay_transfer_trade.container_channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.container_channel IS '所属通道(wechat/alipay/douyin)';


--
-- Name: COLUMN pay_transfer_trade.channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.channel IS '通道编码(冗余, 跨通道统计)';


--
-- Name: COLUMN pay_transfer_trade.provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.provider IS '钱包渠道(wechat/alipay/douyin)';


--
-- Name: COLUMN pay_transfer_trade.amount; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.amount IS '转账金额(最小货币单位/分)';


--
-- Name: COLUMN pay_transfer_trade.currency; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.currency IS '币种';


--
-- Name: COLUMN pay_transfer_trade.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.status IS '转账状态(processing/success/fail/close)';


--
-- Name: COLUMN pay_transfer_trade.out_transfer_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.out_transfer_no IS '通道转账单号';


--
-- Name: COLUMN pay_transfer_trade.relation_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.relation_no IS '实际上送通道的商户转账号(反查权威)';


--
-- Name: COLUMN pay_transfer_trade.finish_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.finish_time IS '转账完成时间';


--
-- Name: COLUMN pay_transfer_trade.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.title IS '转账标题';


--
-- Name: COLUMN pay_transfer_trade.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.mch_no IS '商户号';


--
-- Name: COLUMN pay_transfer_trade.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.creator IS '创建人';


--
-- Name: COLUMN pay_transfer_trade.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.create_time IS '创建时间';


--
-- Name: COLUMN pay_transfer_trade.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.last_modifier IS '最后修改人';


--
-- Name: COLUMN pay_transfer_trade.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN pay_transfer_trade.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.version IS '乐观锁版本';


--
-- Name: COLUMN pay_transfer_trade.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.pay_transfer_trade.deleted IS '逻辑删除';


--
-- Name: starter_audit_login_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.starter_audit_login_log (
    id bigint NOT NULL,
    user_id bigint,
    account character varying(200),
    login boolean DEFAULT false,
    client character varying(100),
    login_type character varying(100),
    ip character varying(100),
    login_location character varying(200),
    browser character varying(200),
    os character varying(200),
    msg character varying(500),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    login_time timestamp with time zone
);


--
-- Name: TABLE starter_audit_login_log; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.starter_audit_login_log IS '登录日志';


--
-- Name: COLUMN starter_audit_login_log.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.id IS '主键';


--
-- Name: COLUMN starter_audit_login_log.user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.user_id IS '用户ID';


--
-- Name: COLUMN starter_audit_login_log.account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.account IS '用户账号';


--
-- Name: COLUMN starter_audit_login_log.login; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.login IS '登录成功状态';


--
-- Name: COLUMN starter_audit_login_log.client; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.client IS '登录终端';


--
-- Name: COLUMN starter_audit_login_log.login_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.login_type IS '登录方式';


--
-- Name: COLUMN starter_audit_login_log.ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.ip IS '登录IP地址';


--
-- Name: COLUMN starter_audit_login_log.login_location; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.login_location IS '登录地点';


--
-- Name: COLUMN starter_audit_login_log.browser; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.browser IS '浏览器类型';


--
-- Name: COLUMN starter_audit_login_log.os; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.os IS '操作系统';


--
-- Name: COLUMN starter_audit_login_log.msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.msg IS '提示消息';


--
-- Name: COLUMN starter_audit_login_log.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.creator IS '创建者ID';


--
-- Name: COLUMN starter_audit_login_log.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.create_time IS '创建时间';


--
-- Name: COLUMN starter_audit_login_log.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.last_modifier IS '最后修改ID';


--
-- Name: COLUMN starter_audit_login_log.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN starter_audit_login_log.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.version IS '版本号';


--
-- Name: COLUMN starter_audit_login_log.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.deleted IS '删除标志';


--
-- Name: COLUMN starter_audit_login_log.login_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_login_log.login_time IS '访问时间';


--
-- Name: starter_audit_operate_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.starter_audit_operate_log (
    id bigint NOT NULL,
    title character varying(200),
    operate_id bigint,
    account character varying(200),
    client character varying(100),
    browser character varying(200),
    os character varying(200),
    business_type character varying(100),
    method character varying(200),
    request_method character varying(20),
    operate_url character varying(500),
    operate_ip character varying(100),
    operate_location character varying(200),
    operate_param jsonb,
    operate_return jsonb,
    success boolean DEFAULT false,
    error_msg character varying(1000),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    operate_time timestamp with time zone
);


--
-- Name: TABLE starter_audit_operate_log; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.starter_audit_operate_log IS '操作日志';


--
-- Name: COLUMN starter_audit_operate_log.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.id IS '主键';


--
-- Name: COLUMN starter_audit_operate_log.title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.title IS '操作模块';


--
-- Name: COLUMN starter_audit_operate_log.operate_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.operate_id IS '操作人员ID';


--
-- Name: COLUMN starter_audit_operate_log.account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.account IS '操作人员账号';


--
-- Name: COLUMN starter_audit_operate_log.client; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.client IS '终端编码';


--
-- Name: COLUMN starter_audit_operate_log.browser; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.browser IS '浏览器类型';


--
-- Name: COLUMN starter_audit_operate_log.os; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.os IS '操作系统';


--
-- Name: COLUMN starter_audit_operate_log.business_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.business_type IS '业务类型';


--
-- Name: COLUMN starter_audit_operate_log.method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.method IS '请求方法';


--
-- Name: COLUMN starter_audit_operate_log.request_method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.request_method IS '请求方式';


--
-- Name: COLUMN starter_audit_operate_log.operate_url; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.operate_url IS '请求URL';


--
-- Name: COLUMN starter_audit_operate_log.operate_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.operate_ip IS '操作IP';


--
-- Name: COLUMN starter_audit_operate_log.operate_location; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.operate_location IS '操作地点';


--
-- Name: COLUMN starter_audit_operate_log.operate_param; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.operate_param IS '请求参数';


--
-- Name: COLUMN starter_audit_operate_log.operate_return; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.operate_return IS '返回参数';


--
-- Name: COLUMN starter_audit_operate_log.success; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.success IS '操作状态';


--
-- Name: COLUMN starter_audit_operate_log.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.error_msg IS '错误消息';


--
-- Name: COLUMN starter_audit_operate_log.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.creator IS '创建者ID';


--
-- Name: COLUMN starter_audit_operate_log.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.create_time IS '创建时间';


--
-- Name: COLUMN starter_audit_operate_log.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.last_modifier IS '最后修改ID';


--
-- Name: COLUMN starter_audit_operate_log.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN starter_audit_operate_log.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.version IS '版本号';


--
-- Name: COLUMN starter_audit_operate_log.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.deleted IS '删除标志';


--
-- Name: COLUMN starter_audit_operate_log.operate_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_operate_log.operate_time IS '操作时间';


--
-- Name: starter_audit_unipay_log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.starter_audit_unipay_log (
    id bigint NOT NULL,
    mch_no character varying(32),
    api_path character varying(256),
    api_title character varying(64),
    request_method character varying(16),
    client_ip character varying(64),
    request_ip character varying(64),
    request_location character varying(128),
    success boolean,
    error_code integer,
    error_msg character varying(512),
    duration_ms bigint,
    trace_id character varying(64),
    req_param jsonb,
    res_body jsonb,
    operate_time timestamp(6) with time zone,
    req_id character varying(64)
);


--
-- Name: TABLE starter_audit_unipay_log; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.starter_audit_unipay_log IS '统一支付接口审计日志';


--
-- Name: COLUMN starter_audit_unipay_log.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.id IS '主键';


--
-- Name: COLUMN starter_audit_unipay_log.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.mch_no IS '商户号';


--
-- Name: COLUMN starter_audit_unipay_log.api_path; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.api_path IS '接口路径';


--
-- Name: COLUMN starter_audit_unipay_log.api_title; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.api_title IS '接口标题';


--
-- Name: COLUMN starter_audit_unipay_log.request_method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.request_method IS 'HTTP方法';


--
-- Name: COLUMN starter_audit_unipay_log.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.client_ip IS '商户入参声明的客户端IP';


--
-- Name: COLUMN starter_audit_unipay_log.request_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.request_ip IS '真实接入IP';


--
-- Name: COLUMN starter_audit_unipay_log.request_location; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.request_location IS '接入IP归属地';


--
-- Name: COLUMN starter_audit_unipay_log.success; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.success IS '是否成功';


--
-- Name: COLUMN starter_audit_unipay_log.error_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.error_code IS '业务错误码';


--
-- Name: COLUMN starter_audit_unipay_log.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.error_msg IS '错误信息';


--
-- Name: COLUMN starter_audit_unipay_log.duration_ms; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.duration_ms IS '耗时毫秒';


--
-- Name: COLUMN starter_audit_unipay_log.trace_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.trace_id IS '链路追踪ID';


--
-- Name: COLUMN starter_audit_unipay_log.req_param; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.req_param IS '请求参数(脱敏后)';


--
-- Name: COLUMN starter_audit_unipay_log.res_body; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.res_body IS '响应体(脱敏后)';


--
-- Name: COLUMN starter_audit_unipay_log.operate_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.operate_time IS '操作时间UTC';


--
-- Name: COLUMN starter_audit_unipay_log.req_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_audit_unipay_log.req_id IS '请求ID(商户传入, 审计索引)';


--
-- Name: starter_platform_file_record; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.starter_platform_file_record (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    size bigint,
    filename character varying(128),
    original_filename character varying(255),
    path character varying(255),
    ext character varying(16),
    content_type character varying(64),
    access_type character varying(16),
    biz_type character varying(32),
    status character varying(16),
    remark character varying(255)
);


--
-- Name: TABLE starter_platform_file_record; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.starter_platform_file_record IS '平台文件记录表';


--
-- Name: COLUMN starter_platform_file_record.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.id IS '主键';


--
-- Name: COLUMN starter_platform_file_record.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.creator IS '创建人';


--
-- Name: COLUMN starter_platform_file_record.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.create_time IS '创建时间';


--
-- Name: COLUMN starter_platform_file_record.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.last_modifier IS '最后修改人';


--
-- Name: COLUMN starter_platform_file_record.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN starter_platform_file_record.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.version IS '乐观锁';


--
-- Name: COLUMN starter_platform_file_record.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.deleted IS '逻辑删除';


--
-- Name: COLUMN starter_platform_file_record.size; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.size IS '文件大小(字节)';


--
-- Name: COLUMN starter_platform_file_record.filename; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.filename IS '文件名称(不含路径)';


--
-- Name: COLUMN starter_platform_file_record.original_filename; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.original_filename IS '原始文件名';


--
-- Name: COLUMN starter_platform_file_record.path; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.path IS '存储路径(以/开头,不含文件名)';


--
-- Name: COLUMN starter_platform_file_record.ext; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.ext IS '文件扩展名';


--
-- Name: COLUMN starter_platform_file_record.content_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.content_type IS 'MIME类型';


--
-- Name: COLUMN starter_platform_file_record.access_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.access_type IS '访问类型';


--
-- Name: COLUMN starter_platform_file_record.biz_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.biz_type IS '业务分类';


--
-- Name: COLUMN starter_platform_file_record.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.status IS '上传状态';


--
-- Name: COLUMN starter_platform_file_record.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.starter_platform_file_record.remark IS '备注';


--
-- Name: stripe_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stripe_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32),
    account_id character varying(64),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE stripe_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.stripe_channel_merchant IS 'Stripe 直连通道商户绑定';


--
-- Name: COLUMN stripe_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.id IS '主键';


--
-- Name: COLUMN stripe_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN stripe_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN stripe_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.product IS '所属支付产品';


--
-- Name: COLUMN stripe_channel_merchant.account_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.account_id IS 'Stripe 账户 ID(acct_xxx)';


--
-- Name: COLUMN stripe_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.creator IS '创建人';


--
-- Name: COLUMN stripe_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN stripe_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.last_modifier IS '最后修改人';


--
-- Name: COLUMN stripe_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN stripe_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN stripe_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_channel_merchant.deleted IS '逻辑删除标志';


--
-- Name: stripe_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.stripe_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    secret_key text,
    publishable_key text,
    webhook_secret text,
    sandbox boolean DEFAULT false NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE stripe_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.stripe_key_config IS 'Stripe 直连密钥配置';


--
-- Name: COLUMN stripe_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.id IS '主键';


--
-- Name: COLUMN stripe_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.mch_no IS '商户号';


--
-- Name: COLUMN stripe_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN stripe_key_config.secret_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.secret_key IS 'Stripe Secret Key(sk_test/sk_live, 加密存储)';


--
-- Name: COLUMN stripe_key_config.publishable_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.publishable_key IS 'Stripe Publishable Key(pk_test/pk_live, 加密存储)';


--
-- Name: COLUMN stripe_key_config.webhook_secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.webhook_secret IS 'Webhook 签名密钥(whsec_xxx, 加密存储)';


--
-- Name: COLUMN stripe_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.sandbox IS '是否沙箱(test mode)';


--
-- Name: COLUMN stripe_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.creator IS '创建人';


--
-- Name: COLUMN stripe_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.create_time IS '创建时间';


--
-- Name: COLUMN stripe_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN stripe_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN stripe_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN stripe_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.stripe_key_config.deleted IS '逻辑删除标志';


--
-- Name: system_dict; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_dict (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    name character varying(255),
    dict_type character varying(255),
    code character varying(255),
    remark character varying(500),
    enable boolean,
    internal boolean,
    i18n_key character varying(200)
);


--
-- Name: TABLE system_dict; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.system_dict IS '字典表';


--
-- Name: COLUMN system_dict.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.id IS '主键';


--
-- Name: COLUMN system_dict.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.creator IS '创建者ID';


--
-- Name: COLUMN system_dict.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.create_time IS '创建时间';


--
-- Name: COLUMN system_dict.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN system_dict.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN system_dict.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.version IS '版本号(乐观锁)';


--
-- Name: COLUMN system_dict.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.deleted IS '删除标志';


--
-- Name: COLUMN system_dict.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.name IS '名称';


--
-- Name: COLUMN system_dict.dict_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.dict_type IS '字典类型';


--
-- Name: COLUMN system_dict.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.code IS '编码';


--
-- Name: COLUMN system_dict.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.remark IS '备注';


--
-- Name: COLUMN system_dict.enable; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.enable IS '是否启用';


--
-- Name: COLUMN system_dict.internal; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.internal IS '是否内置';


--
-- Name: COLUMN system_dict.i18n_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict.i18n_key IS '国际化key（有值时走语言包翻译）';


--
-- Name: system_dict_item; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_dict_item (
    id bigint NOT NULL,
    dict_id bigint NOT NULL,
    dict_code character varying(100) NOT NULL,
    code character varying(100) NOT NULL,
    sort_no integer,
    enable boolean DEFAULT true,
    remark character varying(500),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    i18n_key character varying(200)
);


--
-- Name: TABLE system_dict_item; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.system_dict_item IS '字典项';


--
-- Name: COLUMN system_dict_item.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.id IS '主键';


--
-- Name: COLUMN system_dict_item.dict_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.dict_id IS '字典ID';


--
-- Name: COLUMN system_dict_item.dict_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.dict_code IS '字典编码';


--
-- Name: COLUMN system_dict_item.code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.code IS '字典项编码';


--
-- Name: COLUMN system_dict_item.sort_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.sort_no IS '字典项排序';


--
-- Name: COLUMN system_dict_item.enable; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.enable IS '是否启用';


--
-- Name: COLUMN system_dict_item.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.remark IS '备注';


--
-- Name: COLUMN system_dict_item.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.creator IS '创建者ID';


--
-- Name: COLUMN system_dict_item.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.create_time IS '创建时间';


--
-- Name: COLUMN system_dict_item.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.last_modifier IS '最后修改ID';


--
-- Name: COLUMN system_dict_item.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN system_dict_item.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.version IS '版本号';


--
-- Name: COLUMN system_dict_item.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.deleted IS '删除标志';


--
-- Name: COLUMN system_dict_item.i18n_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_dict_item.i18n_key IS '国际化key（有值时走语言包翻译）';


--
-- Name: system_platform_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_platform_config (
    id bigint NOT NULL,
    config_type character varying(50) NOT NULL,
    config_name character varying(100),
    config_data jsonb,
    description character varying(500),
    enabled boolean DEFAULT true,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE system_platform_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.system_platform_config IS '系统平台统一配置';


--
-- Name: COLUMN system_platform_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.id IS '主键';


--
-- Name: COLUMN system_platform_config.config_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.config_type IS '配置类型';


--
-- Name: COLUMN system_platform_config.config_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.config_name IS '配置名称';


--
-- Name: COLUMN system_platform_config.config_data; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.config_data IS '配置数据JSON格式';


--
-- Name: COLUMN system_platform_config.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.description IS '配置描述';


--
-- Name: COLUMN system_platform_config.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.enabled IS '是否启用';


--
-- Name: COLUMN system_platform_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.creator IS '创建者ID';


--
-- Name: COLUMN system_platform_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.create_time IS '创建时间';


--
-- Name: COLUMN system_platform_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.last_modifier IS '最后修改ID';


--
-- Name: COLUMN system_platform_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN system_platform_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.version IS '版本号';


--
-- Name: COLUMN system_platform_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_config.deleted IS '删除标志';


--
-- Name: system_platform_encrypt_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_platform_encrypt_config (
    id bigint NOT NULL,
    config_type character varying(50) NOT NULL,
    config_name character varying(100),
    config_data text,
    description character varying(500),
    enabled boolean DEFAULT true,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer,
    deleted boolean DEFAULT false
);


--
-- Name: TABLE system_platform_encrypt_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.system_platform_encrypt_config IS '系统平台加密配置表';


--
-- Name: COLUMN system_platform_encrypt_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.id IS '主键';


--
-- Name: COLUMN system_platform_encrypt_config.config_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.config_type IS '配置类型';


--
-- Name: COLUMN system_platform_encrypt_config.config_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.config_name IS '配置名称';


--
-- Name: COLUMN system_platform_encrypt_config.config_data; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.config_data IS '配置数据(加密存储)';


--
-- Name: COLUMN system_platform_encrypt_config.description; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.description IS '配置描述';


--
-- Name: COLUMN system_platform_encrypt_config.enabled; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.enabled IS '是否启用';


--
-- Name: COLUMN system_platform_encrypt_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.creator IS '创建者';


--
-- Name: COLUMN system_platform_encrypt_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.create_time IS '创建时间';


--
-- Name: COLUMN system_platform_encrypt_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.last_modifier IS '最后修改者';


--
-- Name: COLUMN system_platform_encrypt_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN system_platform_encrypt_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.version IS '版本号';


--
-- Name: COLUMN system_platform_encrypt_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_platform_encrypt_config.deleted IS '是否删除';


--
-- Name: system_sensitive_word; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_sensitive_word (
    id bigint NOT NULL,
    word character varying(64) NOT NULL,
    category character varying(32),
    match_mode character varying(16) DEFAULT 'contains'::character varying NOT NULL,
    level character varying(16) DEFAULT 'reject'::character varying,
    status character varying(16) NOT NULL,
    remark character varying(255),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE system_sensitive_word; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.system_sensitive_word IS '敏感词词库';


--
-- Name: COLUMN system_sensitive_word.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.id IS '主键';


--
-- Name: COLUMN system_sensitive_word.word; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.word IS '敏感词原文(建议简体)';


--
-- Name: COLUMN system_sensitive_word.category; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.category IS '分类: politic/porn/violence/ad/custom';


--
-- Name: COLUMN system_sensitive_word.match_mode; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.match_mode IS '匹配模式: contains/exact';


--
-- Name: COLUMN system_sensitive_word.level; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.level IS '处理级别: reject/warn';


--
-- Name: COLUMN system_sensitive_word.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.status IS '状态: enable/disable';


--
-- Name: COLUMN system_sensitive_word.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.remark IS '备注';


--
-- Name: COLUMN system_sensitive_word.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.creator IS '创建者ID';


--
-- Name: COLUMN system_sensitive_word.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.create_time IS '创建时间';


--
-- Name: COLUMN system_sensitive_word.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN system_sensitive_word.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN system_sensitive_word.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.version IS '版本号';


--
-- Name: COLUMN system_sensitive_word.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word.deleted IS '删除标志';


--
-- Name: system_sensitive_word_hit; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.system_sensitive_word_hit (
    id bigint NOT NULL,
    word_id bigint,
    hit_word character varying(64) NOT NULL,
    content_preview character varying(200),
    scene character varying(32) NOT NULL,
    source character varying(32),
    mch_no character varying(32),
    app_id character varying(50),
    operator_id bigint,
    client_ip character varying(64),
    request_path character varying(255),
    remark character varying(255),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE system_sensitive_word_hit; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.system_sensitive_word_hit IS '敏感词命中记录';


--
-- Name: COLUMN system_sensitive_word_hit.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.id IS '主键';


--
-- Name: COLUMN system_sensitive_word_hit.word_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.word_id IS '关联词库ID';


--
-- Name: COLUMN system_sensitive_word_hit.hit_word; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.hit_word IS '命中词快照';


--
-- Name: COLUMN system_sensitive_word_hit.content_preview; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.content_preview IS '原文摘要';


--
-- Name: COLUMN system_sensitive_word_hit.scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.scene IS '场景: pay_title/goods_name/...';


--
-- Name: COLUMN system_sensitive_word_hit.source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.source IS '来源: admin/merchant/unipay/app_admin';


--
-- Name: COLUMN system_sensitive_word_hit.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.mch_no IS '商户号';


--
-- Name: COLUMN system_sensitive_word_hit.app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.app_id IS '应用号';


--
-- Name: COLUMN system_sensitive_word_hit.operator_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.operator_id IS '操作人用户ID';


--
-- Name: COLUMN system_sensitive_word_hit.client_ip; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.client_ip IS '客户端IP';


--
-- Name: COLUMN system_sensitive_word_hit.request_path; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.request_path IS '请求路径';


--
-- Name: COLUMN system_sensitive_word_hit.remark; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.remark IS '备注';


--
-- Name: COLUMN system_sensitive_word_hit.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.creator IS '创建者ID';


--
-- Name: COLUMN system_sensitive_word_hit.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.create_time IS '创建时间';


--
-- Name: COLUMN system_sensitive_word_hit.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN system_sensitive_word_hit.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN system_sensitive_word_hit.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.version IS '版本号';


--
-- Name: COLUMN system_sensitive_word_hit.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.system_sensitive_word_hit.deleted IS '删除标志';


--
-- Name: ums_direct_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.ums_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    ums_app_id character varying(128),
    app_key text,
    secret_key text,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    merchant_no character varying(64),
    terminal_no character varying(64),
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE ums_direct_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.ums_direct_key_config IS '银联商务直连密钥配置';


--
-- Name: COLUMN ums_direct_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.id IS '主键';


--
-- Name: COLUMN ums_direct_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.mch_no IS '商户号';


--
-- Name: COLUMN ums_direct_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.channel_mch_no IS '通道商户号(唯一关联)';


--
-- Name: COLUMN ums_direct_key_config.ums_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.ums_app_id IS '银联商务应用 AppId';


--
-- Name: COLUMN ums_direct_key_config.app_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.app_key IS '应用密钥(HmacSHA256 签名密钥, 加密存储)';


--
-- Name: COLUMN ums_direct_key_config.secret_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.secret_key IS '通讯密钥(回调验签 MD5/SHA256 拼接密钥, 加密存储)';


--
-- Name: COLUMN ums_direct_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.creator IS '创建人ID';


--
-- Name: COLUMN ums_direct_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.create_time IS '创建时间';


--
-- Name: COLUMN ums_direct_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN ums_direct_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN ums_direct_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN ums_direct_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.deleted IS '逻辑删除标志';


--
-- Name: COLUMN ums_direct_key_config.merchant_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.merchant_no IS '银联商务商户号(mid)';


--
-- Name: COLUMN ums_direct_key_config.terminal_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.terminal_no IS '终端号(tid)';


--
-- Name: COLUMN ums_direct_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.ums_direct_key_config.sandbox IS '是否沙箱环境';


--
-- Name: union_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.union_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    mer_id character varying(64),
    sign_type character varying(32),
    cert_sign boolean DEFAULT true,
    key_private_cert text,
    key_private_cert_pwd character varying(256),
    acp_middle_cert text,
    acp_root_cert text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE union_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.union_key_config IS '云闪付密钥配置';


--
-- Name: COLUMN union_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.id IS '主键';


--
-- Name: COLUMN union_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.mch_no IS '商户号';


--
-- Name: COLUMN union_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.channel_mch_no IS '通道商户号(唯一关联)';


--
-- Name: COLUMN union_key_config.mer_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.mer_id IS '银联商户号(merId)';


--
-- Name: COLUMN union_key_config.sign_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.sign_type IS '签名类型(银联 ACP 固定 RSA2)';


--
-- Name: COLUMN union_key_config.cert_sign; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.cert_sign IS '是否证书签名';


--
-- Name: COLUMN union_key_config.key_private_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.key_private_cert IS '应用私钥证书(Base64 PKCS12, 加密存储)';


--
-- Name: COLUMN union_key_config.key_private_cert_pwd; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.key_private_cert_pwd IS '私钥证书密码(加密存储)';


--
-- Name: COLUMN union_key_config.acp_middle_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.acp_middle_cert IS '中级证书(Base64 X.509)';


--
-- Name: COLUMN union_key_config.acp_root_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.acp_root_cert IS '根证书(Base64 X.509)';


--
-- Name: COLUMN union_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.sandbox IS '是否沙箱环境';


--
-- Name: COLUMN union_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.creator IS '创建人ID';


--
-- Name: COLUMN union_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.create_time IS '创建时间';


--
-- Name: COLUMN union_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.last_modifier IS '最后修改人ID';


--
-- Name: COLUMN union_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN union_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN union_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.union_key_config.deleted IS '逻辑删除标志';


--
-- Name: vbill_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.vbill_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(64) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    vbill_mch_no character varying(64) NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false,
    sandbox boolean DEFAULT false
);


--
-- Name: TABLE vbill_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.vbill_isv_channel_merchant IS '随行付通道商户绑定';


--
-- Name: COLUMN vbill_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN vbill_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.mch_no IS '平台商户号';


--
-- Name: COLUMN vbill_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.channel_mch_no IS '通道商户号(平台生成的唯一标识, VBILL+雪花)';


--
-- Name: COLUMN vbill_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.product IS '所属支付产品(对应 ProductEnum.code, 如 vbill_pay)';


--
-- Name: COLUMN vbill_isv_channel_merchant.vbill_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.vbill_mch_no IS '天阙商户号(mno)';


--
-- Name: COLUMN vbill_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.creator IS '创建者ID';


--
-- Name: COLUMN vbill_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN vbill_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN vbill_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN vbill_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.version IS '版本号(乐观锁)';


--
-- Name: COLUMN vbill_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.deleted IS '删除标志';


--
-- Name: COLUMN vbill_isv_channel_merchant.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_channel_merchant.sandbox IS '是否沙箱环境商户';


--
-- Name: vbill_isv_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.vbill_isv_key_config (
    id bigint NOT NULL,
    product character varying(32) NOT NULL,
    org_id character varying(64),
    public_key text,
    private_key text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0,
    deleted boolean DEFAULT false
);


--
-- Name: TABLE vbill_isv_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.vbill_isv_key_config IS '随行付服务商密钥配置';


--
-- Name: COLUMN vbill_isv_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.id IS '主键';


--
-- Name: COLUMN vbill_isv_key_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.product IS '支付产品编码(对应 ProductEnum.code, 如 vbill_pay)';


--
-- Name: COLUMN vbill_isv_key_config.org_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.org_id IS '天阙合作机构ID(orgId)';


--
-- Name: COLUMN vbill_isv_key_config.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.public_key IS '天阙RSA公钥(X509 Base64, 用于响应/回调验签, 加密存储)';


--
-- Name: COLUMN vbill_isv_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.private_key IS '商户RSA私钥(PKCS8 Base64, SHA1withRSA 签名, 加密存储)';


--
-- Name: COLUMN vbill_isv_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.sandbox IS '是否沙箱环境';


--
-- Name: COLUMN vbill_isv_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.creator IS '创建者ID';


--
-- Name: COLUMN vbill_isv_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.create_time IS '创建时间';


--
-- Name: COLUMN vbill_isv_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN vbill_isv_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN vbill_isv_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.version IS '版本号(乐观锁)';


--
-- Name: COLUMN vbill_isv_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.vbill_isv_key_config.deleted IS '删除标志';


--
-- Name: wechat_direct_alloc_receiver; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wechat_direct_alloc_receiver (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    receiver_type character varying(32) NOT NULL,
    receiver_account character varying(256) NOT NULL,
    account_hash character varying(64) NOT NULL,
    receiver_name character varying(256),
    relation_type character varying(32) NOT NULL,
    custom_relation character varying(64),
    channel_app_id character varying(64) NOT NULL,
    status character varying(16) NOT NULL,
    error_msg text,
    bind_time timestamp(6) with time zone,
    unbind_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE wechat_direct_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wechat_direct_alloc_receiver IS '微信直连分账接收方(通道侧绑定档案)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.id IS '主键';


--
-- Name: COLUMN wechat_direct_alloc_receiver.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.mch_no IS '商户号';


--
-- Name: COLUMN wechat_direct_alloc_receiver.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.channel_mch_no IS '通道商户号(关联通用通道商户主表)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.receiver_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.receiver_type IS '接收方类型';


--
-- Name: COLUMN wechat_direct_alloc_receiver.receiver_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.receiver_account IS '接收方账号(AES-256-GCM加密存储, openid为channel_app_id维度)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.account_hash; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.account_hash IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.receiver_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.receiver_name IS '接收方名称(AES-256-GCM加密存储, MERCHANT_ID时必填商户全称)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.relation_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.relation_type IS '分账关系类型(微信原生小写映射, CUSTOM时需custom_relation)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.custom_relation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.custom_relation IS '自定义分账关系名(relation_type=CUSTOM时必填)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.channel_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.channel_app_id IS '绑定时所用商户档微信应用appid(重新绑定复用)';


--
-- Name: COLUMN wechat_direct_alloc_receiver.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.status IS '绑定状态';


--
-- Name: COLUMN wechat_direct_alloc_receiver.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.error_msg IS '最近一次绑定/解绑失败原因';


--
-- Name: COLUMN wechat_direct_alloc_receiver.bind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.bind_time IS '绑定成功时间';


--
-- Name: COLUMN wechat_direct_alloc_receiver.unbind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.unbind_time IS '解绑成功时间';


--
-- Name: COLUMN wechat_direct_alloc_receiver.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.creator IS '创建人';


--
-- Name: COLUMN wechat_direct_alloc_receiver.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.create_time IS '创建时间';


--
-- Name: COLUMN wechat_direct_alloc_receiver.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.last_modifier IS '最后修改人';


--
-- Name: COLUMN wechat_direct_alloc_receiver.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wechat_direct_alloc_receiver.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.version IS '乐观锁版本号';


--
-- Name: COLUMN wechat_direct_alloc_receiver.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_alloc_receiver.deleted IS '逻辑删除标志';


--
-- Name: wechat_direct_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wechat_direct_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    wx_mch_id character varying(32),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    transfer_scene character varying(50)
);


--
-- Name: TABLE wechat_direct_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wechat_direct_channel_merchant IS '微信直连通道商户绑定';


--
-- Name: COLUMN wechat_direct_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.id IS '主键';


--
-- Name: COLUMN wechat_direct_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN wechat_direct_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.channel_mch_no IS '通道商户号(系统生成雪花号)';


--
-- Name: COLUMN wechat_direct_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.product IS '所属支付产品';


--
-- Name: COLUMN wechat_direct_channel_merchant.wx_mch_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.wx_mch_id IS '微信直连商户号';


--
-- Name: COLUMN wechat_direct_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.creator IS '创建人';


--
-- Name: COLUMN wechat_direct_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN wechat_direct_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.last_modifier IS '最后修改人';


--
-- Name: COLUMN wechat_direct_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wechat_direct_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN wechat_direct_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.deleted IS '逻辑删除标志';


--
-- Name: COLUMN wechat_direct_channel_merchant.transfer_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_channel_merchant.transfer_scene IS '转账场景ID(商家转账到零钱, 未配置时发起转账报错)';


--
-- Name: wechat_direct_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wechat_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    api_key_v3 text,
    public_key text,
    public_key_id character varying(128),
    private_key text,
    private_cert text,
    cert_serial_no character varying(128),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE wechat_direct_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wechat_direct_key_config IS '微信直连密钥配置';


--
-- Name: COLUMN wechat_direct_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.id IS '主键';


--
-- Name: COLUMN wechat_direct_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.mch_no IS '商户号';


--
-- Name: COLUMN wechat_direct_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN wechat_direct_key_config.api_key_v3; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.api_key_v3 IS 'APIv3密钥(加密存储)';


--
-- Name: COLUMN wechat_direct_key_config.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.public_key IS '支付公钥(加密存储)';


--
-- Name: COLUMN wechat_direct_key_config.public_key_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.public_key_id IS '支付公钥ID';


--
-- Name: COLUMN wechat_direct_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.private_key IS '商户私钥(加密存储)';


--
-- Name: COLUMN wechat_direct_key_config.private_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.private_cert IS '商户证书(加密存储)';


--
-- Name: COLUMN wechat_direct_key_config.cert_serial_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.cert_serial_no IS '证书序列号';


--
-- Name: COLUMN wechat_direct_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.creator IS '创建人';


--
-- Name: COLUMN wechat_direct_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.create_time IS '创建时间';


--
-- Name: COLUMN wechat_direct_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN wechat_direct_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wechat_direct_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN wechat_direct_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_direct_key_config.deleted IS '逻辑删除标志';


--
-- Name: wechat_isv_alloc_receiver; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wechat_isv_alloc_receiver (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    receiver_type character varying(32) NOT NULL,
    receiver_account character varying(256) NOT NULL,
    account_hash character varying(64) NOT NULL,
    receiver_name character varying(256),
    relation_type character varying(32) NOT NULL,
    custom_relation character varying(64),
    sp_app_id character varying(64) NOT NULL,
    sub_app_id character varying(64),
    status character varying(16) NOT NULL,
    error_msg text,
    bind_time timestamp(6) with time zone,
    unbind_time timestamp(6) with time zone,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE wechat_isv_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wechat_isv_alloc_receiver IS '微信服务商分账接收方(通道侧绑定档案, 挂特约商户sub_mchid维度)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.id IS '主键';


--
-- Name: COLUMN wechat_isv_alloc_receiver.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.mch_no IS '商户号';


--
-- Name: COLUMN wechat_isv_alloc_receiver.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.channel_mch_no IS '通道商户号(关联通用通道商户主表)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.receiver_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.receiver_type IS '接收方类型';


--
-- Name: COLUMN wechat_isv_alloc_receiver.receiver_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.receiver_account IS '接收方账号(AES-256-GCM加密存储, openid为对应appid维度)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.account_hash; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.account_hash IS '接收方账号SHA-256哈希(密文不确定, 查重与等值定位用)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.receiver_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.receiver_name IS '接收方名称(AES-256-GCM加密存储, MERCHANT_ID时必填商户全称)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.relation_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.relation_type IS '分账关系类型(微信原生小写映射, CUSTOM时需custom_relation)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.custom_relation; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.custom_relation IS '自定义分账关系名(relation_type=CUSTOM时必填)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.sp_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.sp_app_id IS '绑定时所用平台档(服务商)应用appid(重新绑定复用)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.sub_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.sub_app_id IS '子商户应用appid(可空, PERSONAL_SUB_OPENID时必填)';


--
-- Name: COLUMN wechat_isv_alloc_receiver.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.status IS '绑定状态';


--
-- Name: COLUMN wechat_isv_alloc_receiver.error_msg; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.error_msg IS '最近一次绑定/解绑失败原因';


--
-- Name: COLUMN wechat_isv_alloc_receiver.bind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.bind_time IS '绑定成功时间';


--
-- Name: COLUMN wechat_isv_alloc_receiver.unbind_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.unbind_time IS '解绑成功时间';


--
-- Name: COLUMN wechat_isv_alloc_receiver.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.creator IS '创建人';


--
-- Name: COLUMN wechat_isv_alloc_receiver.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.create_time IS '创建时间';


--
-- Name: COLUMN wechat_isv_alloc_receiver.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.last_modifier IS '最后修改人';


--
-- Name: COLUMN wechat_isv_alloc_receiver.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wechat_isv_alloc_receiver.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.version IS '乐观锁版本号';


--
-- Name: COLUMN wechat_isv_alloc_receiver.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_alloc_receiver.deleted IS '逻辑删除标志';


--
-- Name: wechat_isv_channel_merchant; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wechat_isv_channel_merchant (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    product character varying(32) NOT NULL,
    sub_mch_id character varying(32),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE wechat_isv_channel_merchant; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wechat_isv_channel_merchant IS '微信服务商通道商户绑定';


--
-- Name: COLUMN wechat_isv_channel_merchant.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.id IS '主键';


--
-- Name: COLUMN wechat_isv_channel_merchant.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.mch_no IS '商户号';


--
-- Name: COLUMN wechat_isv_channel_merchant.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.channel_mch_no IS '通道商户号(WISV+雪花)';


--
-- Name: COLUMN wechat_isv_channel_merchant.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.product IS '所属支付产品';


--
-- Name: COLUMN wechat_isv_channel_merchant.sub_mch_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.sub_mch_id IS '微信特约商户号/二级商户号(V3服务商支付 sub_mchid)';


--
-- Name: COLUMN wechat_isv_channel_merchant.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.creator IS '创建人';


--
-- Name: COLUMN wechat_isv_channel_merchant.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.create_time IS '创建时间';


--
-- Name: COLUMN wechat_isv_channel_merchant.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.last_modifier IS '最后修改人';


--
-- Name: COLUMN wechat_isv_channel_merchant.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wechat_isv_channel_merchant.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.version IS '乐观锁版本号';


--
-- Name: COLUMN wechat_isv_channel_merchant.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_channel_merchant.deleted IS '逻辑删除标志';


--
-- Name: wechat_isv_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wechat_isv_key_config (
    id bigint NOT NULL,
    product character varying(32),
    wx_mch_id character varying(32),
    api_key_v3 text,
    public_key text,
    public_key_id character varying(128),
    private_key text,
    private_cert text,
    cert_serial_no character varying(128),
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE wechat_isv_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wechat_isv_key_config IS '微信服务商密钥配置';


--
-- Name: COLUMN wechat_isv_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.id IS '主键';


--
-- Name: COLUMN wechat_isv_key_config.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.product IS '产品编码';


--
-- Name: COLUMN wechat_isv_key_config.wx_mch_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.wx_mch_id IS '微信服务商商户号';


--
-- Name: COLUMN wechat_isv_key_config.api_key_v3; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.api_key_v3 IS 'APIv3密钥(加密存储)';


--
-- Name: COLUMN wechat_isv_key_config.public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.public_key IS '支付公钥(加密存储)';


--
-- Name: COLUMN wechat_isv_key_config.public_key_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.public_key_id IS '支付公钥ID';


--
-- Name: COLUMN wechat_isv_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.private_key IS '商户私钥(加密存储)';


--
-- Name: COLUMN wechat_isv_key_config.private_cert; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.private_cert IS '商户证书(加密存储)';


--
-- Name: COLUMN wechat_isv_key_config.cert_serial_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.cert_serial_no IS '证书序列号';


--
-- Name: COLUMN wechat_isv_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.creator IS '创建人';


--
-- Name: COLUMN wechat_isv_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.create_time IS '创建时间';


--
-- Name: COLUMN wechat_isv_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.last_modifier IS '最后修改人';


--
-- Name: COLUMN wechat_isv_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wechat_isv_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.version IS '乐观锁版本号';


--
-- Name: COLUMN wechat_isv_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_isv_key_config.deleted IS '逻辑删除标志';


--
-- Name: wechat_transfer_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wechat_transfer_config (
    id bigint NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    transfer_scene character varying(64),
    transfer_app_ref_id bigint,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE wechat_transfer_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wechat_transfer_config IS '微信转账配置(一对一, 一个通道商户一条: 转账场景+发起应用)';


--
-- Name: COLUMN wechat_transfer_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.id IS '主键';


--
-- Name: COLUMN wechat_transfer_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.mch_no IS '商户号';


--
-- Name: COLUMN wechat_transfer_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN wechat_transfer_config.transfer_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.transfer_scene IS '转账场景ID(微信transfer_scene, 枚举:1000现金营销/1002行政补贴/1004保险理赔/1005佣金报酬/1009采购货款/1010二手回收/1011企业赔付/1013公益补助)';


--
-- Name: COLUMN wechat_transfer_config.transfer_app_ref_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.transfer_app_ref_id IS '转账发起应用引用(指向wx_mch_app主键, 须为公众号类型, 决定appid与openid来源)';


--
-- Name: COLUMN wechat_transfer_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.creator IS '创建者ID';


--
-- Name: COLUMN wechat_transfer_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.create_time IS '创建时间';


--
-- Name: COLUMN wechat_transfer_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN wechat_transfer_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wechat_transfer_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.version IS '乐观锁版本号';


--
-- Name: COLUMN wechat_transfer_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wechat_transfer_config.deleted IS '逻辑删除标志';


--
-- Name: wx_channel_app_capability; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wx_channel_app_capability (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    channel_mch_no character varying(64) NOT NULL,
    capability character varying(64) NOT NULL,
    app_scope character varying(16) NOT NULL,
    wx_app_ref_id bigint NOT NULL
);


--
-- Name: TABLE wx_channel_app_capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wx_channel_app_capability IS '通道商户微信应用能力绑定（同能力可按档位双绑 platform+merchant）';


--
-- Name: COLUMN wx_channel_app_capability.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.id IS '主键';


--
-- Name: COLUMN wx_channel_app_capability.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.creator IS '创建者ID';


--
-- Name: COLUMN wx_channel_app_capability.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.create_time IS '创建时间';


--
-- Name: COLUMN wx_channel_app_capability.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.last_modifier IS '最后修改ID';


--
-- Name: COLUMN wx_channel_app_capability.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wx_channel_app_capability.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.version IS '版本号';


--
-- Name: COLUMN wx_channel_app_capability.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.deleted IS '删除标志';


--
-- Name: COLUMN wx_channel_app_capability.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.mch_no IS '商户号';


--
-- Name: COLUMN wx_channel_app_capability.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.channel_mch_no IS '通道商户号';


--
-- Name: COLUMN wx_channel_app_capability.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.capability IS '支付能力编码';


--
-- Name: COLUMN wx_channel_app_capability.app_scope; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.app_scope IS '应用档位：platform/merchant';


--
-- Name: COLUMN wx_channel_app_capability.wx_app_ref_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_channel_app_capability.wx_app_ref_id IS '微信应用主数据主键（由 app_scope 决定指向平台或商户表）';


--
-- Name: wx_mch_app; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wx_mch_app (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    mch_no character varying(32) NOT NULL,
    app_name character varying(64) NOT NULL,
    app_type character varying(32) NOT NULL,
    wx_app_id character varying(64) NOT NULL,
    app_secret character varying(512)
);


--
-- Name: TABLE wx_mch_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wx_mch_app IS '商户微信应用（商户域开放平台身份，跨通道可引用）';


--
-- Name: COLUMN wx_mch_app.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.id IS '主键';


--
-- Name: COLUMN wx_mch_app.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.creator IS '创建者ID';


--
-- Name: COLUMN wx_mch_app.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.create_time IS '创建时间';


--
-- Name: COLUMN wx_mch_app.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.last_modifier IS '最后修改ID';


--
-- Name: COLUMN wx_mch_app.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wx_mch_app.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.version IS '版本号';


--
-- Name: COLUMN wx_mch_app.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.deleted IS '删除标志';


--
-- Name: COLUMN wx_mch_app.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.mch_no IS '商户号';


--
-- Name: COLUMN wx_mch_app.app_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.app_name IS '应用名称';


--
-- Name: COLUMN wx_mch_app.app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.app_type IS '应用类型：official_account/mini_program/mobile_app';


--
-- Name: COLUMN wx_mch_app.wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.wx_app_id IS '微信应用AppId';


--
-- Name: COLUMN wx_mch_app.app_secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_mch_app.app_secret IS '应用密钥(加密存储)';


--
-- Name: wx_platform_app; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wx_platform_app (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    app_name character varying(64) NOT NULL,
    app_type character varying(32) NOT NULL,
    wx_app_id character varying(64) NOT NULL,
    app_secret character varying(512)
);


--
-- Name: TABLE wx_platform_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wx_platform_app IS '平台微信应用（开放平台身份，跨通道可引用）';


--
-- Name: COLUMN wx_platform_app.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.id IS '主键';


--
-- Name: COLUMN wx_platform_app.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.creator IS '创建者ID';


--
-- Name: COLUMN wx_platform_app.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.create_time IS '创建时间';


--
-- Name: COLUMN wx_platform_app.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.last_modifier IS '最后修改ID';


--
-- Name: COLUMN wx_platform_app.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wx_platform_app.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.version IS '版本号';


--
-- Name: COLUMN wx_platform_app.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.deleted IS '删除标志';


--
-- Name: COLUMN wx_platform_app.app_name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.app_name IS '应用名称';


--
-- Name: COLUMN wx_platform_app.app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.app_type IS '应用类型：official_account/mini_program/mobile_app';


--
-- Name: COLUMN wx_platform_app.wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.wx_app_id IS '微信应用AppId';


--
-- Name: COLUMN wx_platform_app.app_secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app.app_secret IS '应用密钥(加密存储)';


--
-- Name: wx_platform_app_capability; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.wx_platform_app_capability (
    id bigint NOT NULL,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    capability character varying(64) NOT NULL,
    wx_platform_app_id bigint NOT NULL,
    product character varying(64) NOT NULL
);


--
-- Name: TABLE wx_platform_app_capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.wx_platform_app_capability IS '平台微信应用默认能力绑定（全局一能力一应用）';


--
-- Name: COLUMN wx_platform_app_capability.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.id IS '主键';


--
-- Name: COLUMN wx_platform_app_capability.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.creator IS '创建者ID';


--
-- Name: COLUMN wx_platform_app_capability.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.create_time IS '创建时间';


--
-- Name: COLUMN wx_platform_app_capability.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.last_modifier IS '最后修改ID';


--
-- Name: COLUMN wx_platform_app_capability.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN wx_platform_app_capability.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.version IS '版本号';


--
-- Name: COLUMN wx_platform_app_capability.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.deleted IS '删除标志';


--
-- Name: COLUMN wx_platform_app_capability.capability; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.capability IS '支付能力编码';


--
-- Name: COLUMN wx_platform_app_capability.wx_platform_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.wx_platform_app_id IS '平台微信应用ID';


--
-- Name: COLUMN wx_platform_app_capability.product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.wx_platform_app_capability.product IS '支付产品编码';


--
-- Name: yeepay_direct_key_config; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.yeepay_direct_key_config (
    id bigint NOT NULL,
    mch_no character varying(32),
    channel_mch_no character varying(64),
    merchant_no character varying(64),
    yop_isv_no character varying(64),
    app_key text,
    private_key text,
    yop_public_key text,
    wx_app_id character varying(64),
    wx_app_secret text,
    sandbox boolean DEFAULT false,
    creator bigint,
    create_time timestamp(6) with time zone,
    last_modifier bigint,
    last_modified_time timestamp(6) with time zone,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL
);


--
-- Name: TABLE yeepay_direct_key_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.yeepay_direct_key_config IS '易宝直连配置';


--
-- Name: COLUMN yeepay_direct_key_config.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.id IS '主键';


--
-- Name: COLUMN yeepay_direct_key_config.mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.mch_no IS '商户号';


--
-- Name: COLUMN yeepay_direct_key_config.channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.channel_mch_no IS '通道商户号(创建时录入不可修改)';


--
-- Name: COLUMN yeepay_direct_key_config.merchant_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.merchant_no IS '易宝商户号(merchantNo, 创建时录入不可修改)';


--
-- Name: COLUMN yeepay_direct_key_config.yop_isv_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.yop_isv_no IS '易宝服务商商编(parentMerchantNo / yopIsvNo, 创建时录入不可修改)';


--
-- Name: COLUMN yeepay_direct_key_config.app_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.app_key IS '通道应用 AppKey(YOP 应用标识, 加密存储)';


--
-- Name: COLUMN yeepay_direct_key_config.private_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.private_key IS '商户 RSA 私钥(PEM PKCS#8, SDK 签名用, 加密存储)';


--
-- Name: COLUMN yeepay_direct_key_config.yop_public_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.yop_public_key IS '易宝平台 RSA 公钥(PEM, SDK 验签用, 加密存储)';


--
-- Name: COLUMN yeepay_direct_key_config.wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.wx_app_id IS '微信 AppId(微信 H5/JSAPI 场景用, 可空)';


--
-- Name: COLUMN yeepay_direct_key_config.wx_app_secret; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.wx_app_secret IS '微信 AppSecret(微信场景用, 可空, 加密存储)';


--
-- Name: COLUMN yeepay_direct_key_config.sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.sandbox IS '是否沙箱环境';


--
-- Name: COLUMN yeepay_direct_key_config.creator; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.creator IS '创建者ID';


--
-- Name: COLUMN yeepay_direct_key_config.create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.create_time IS '创建时间';


--
-- Name: COLUMN yeepay_direct_key_config.last_modifier; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.last_modifier IS '最后修改者ID';


--
-- Name: COLUMN yeepay_direct_key_config.last_modified_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.last_modified_time IS '最后修改时间';


--
-- Name: COLUMN yeepay_direct_key_config.version; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.version IS '版本号(乐观锁)';


--
-- Name: COLUMN yeepay_direct_key_config.deleted; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.yeepay_direct_key_config.deleted IS '删除标志';


--
-- Name: adapay_direct_key_config id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.adapay_direct_key_config ALTER COLUMN id SET DEFAULT nextval('public.adapay_direct_key_config_id_seq'::regclass);


--
-- Name: alipay_direct_app_capability id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_direct_app_capability ALTER COLUMN id SET DEFAULT nextval('public.alipay_direct_app_capability_id_seq'::regclass);


--
-- Name: base_city_adjacent id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_city_adjacent ALTER COLUMN id SET DEFAULT nextval('public.base_city_adjacent_id_seq'::regclass);


--
-- Name: hmpay_isv_channel_merchant id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hmpay_isv_channel_merchant ALTER COLUMN id SET DEFAULT nextval('public.hmpay_isv_channel_merchant_id_seq'::regclass);


--
-- Name: hmpay_isv_key_config id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hmpay_isv_key_config ALTER COLUMN id SET DEFAULT nextval('public.hmpay_isv_key_config_id_seq'::regclass);


--
-- Name: mch_app_notify_config id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_app_notify_config ALTER COLUMN id SET DEFAULT nextval('public.mch_app_notify_config_id_seq'::regclass);


--
-- Name: mch_user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_user ALTER COLUMN id SET DEFAULT nextval('public.mch_user_id_seq'::regclass);


--
-- Name: pay_close_record id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_close_record ALTER COLUMN id SET DEFAULT nextval('public.pay_close_record_id_seq'::regclass);


--
-- Name: pay_sync_record id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_sync_record ALTER COLUMN id SET DEFAULT nextval('public.pay_sync_record_id_seq'::regclass);


--
-- Name: adapay_direct_key_config adapay_direct_key_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.adapay_direct_key_config
    ADD CONSTRAINT adapay_direct_key_config_pkey PRIMARY KEY (id);


--
-- Name: alipay_direct_app_capability alipay_direct_app_capability_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_direct_app_capability
    ADD CONSTRAINT alipay_direct_app_capability_pkey PRIMARY KEY (id);


--
-- Name: base_area base_area_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_area
    ADD CONSTRAINT base_area_pkey PRIMARY KEY (code);


--
-- Name: base_city_adjacent base_city_adjacent_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_city_adjacent
    ADD CONSTRAINT base_city_adjacent_pkey PRIMARY KEY (id);


--
-- Name: base_city base_city_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_city
    ADD CONSTRAINT base_city_pkey PRIMARY KEY (code);


--
-- Name: base_province base_province_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_province
    ADD CONSTRAINT base_province_pkey PRIMARY KEY (code);


--
-- Name: base_street base_street_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_street
    ADD CONSTRAINT base_street_pkey PRIMARY KEY (code);


--
-- Name: base_user_protocol base_user_protocol_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_user_protocol
    ADD CONSTRAINT base_user_protocol_pkey PRIMARY KEY (id);


--
-- Name: base_user_protocol_version base_user_protocol_version_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.base_user_protocol_version
    ADD CONSTRAINT base_user_protocol_version_pkey PRIMARY KEY (id);


--
-- Name: device_qr_code device_qr_code_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.device_qr_code
    ADD CONSTRAINT device_qr_code_pkey PRIMARY KEY (id);


--
-- Name: dy_channel_app_capability dy_channel_app_capability_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dy_channel_app_capability
    ADD CONSTRAINT dy_channel_app_capability_pkey PRIMARY KEY (id);


--
-- Name: dy_mch_app dy_mch_app_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dy_mch_app
    ADD CONSTRAINT dy_mch_app_pkey PRIMARY KEY (id);


--
-- Name: dy_platform_app_capability dy_platform_app_capability_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dy_platform_app_capability
    ADD CONSTRAINT dy_platform_app_capability_pkey PRIMARY KEY (id);


--
-- Name: dy_platform_app dy_platform_app_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.dy_platform_app
    ADD CONSTRAINT dy_platform_app_pkey PRIMARY KEY (id);


--
-- Name: hmpay_isv_channel_merchant hmpay_isv_channel_merchant_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hmpay_isv_channel_merchant
    ADD CONSTRAINT hmpay_isv_channel_merchant_pkey PRIMARY KEY (id);


--
-- Name: hmpay_isv_key_config hmpay_isv_key_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hmpay_isv_key_config
    ADD CONSTRAINT hmpay_isv_key_config_pkey PRIMARY KEY (id);


--
-- Name: iam_perm_code iam_perm_code_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_perm_code
    ADD CONSTRAINT iam_perm_code_pkey PRIMARY KEY (id);


--
-- Name: iam_perm_menu iam_perm_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_perm_menu
    ADD CONSTRAINT iam_perm_menu_pkey PRIMARY KEY (id);


--
-- Name: iam_role_code iam_role_code_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_role_code
    ADD CONSTRAINT iam_role_code_pkey PRIMARY KEY (id);


--
-- Name: iam_role_menu iam_role_menu_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_role_menu
    ADD CONSTRAINT iam_role_menu_pkey PRIMARY KEY (id);


--
-- Name: iam_role iam_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_role
    ADD CONSTRAINT iam_role_pkey PRIMARY KEY (id);


--
-- Name: iam_social_login_config iam_social_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_social_login_config
    ADD CONSTRAINT iam_social_config_pkey PRIMARY KEY (id);


--
-- Name: iam_user_dashboard_preference iam_user_dashboard_preference_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_dashboard_preference
    ADD CONSTRAINT iam_user_dashboard_preference_pkey PRIMARY KEY (id);


--
-- Name: iam_user_expand_info iam_user_expand_info_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_expand_info
    ADD CONSTRAINT iam_user_expand_info_pkey PRIMARY KEY (id);


--
-- Name: iam_user_info iam_user_info_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_info
    ADD CONSTRAINT iam_user_info_pkey PRIMARY KEY (id);


--
-- Name: iam_user_passkey iam_user_passkey_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_passkey
    ADD CONSTRAINT iam_user_passkey_pkey PRIMARY KEY (id);


--
-- Name: iam_user_password_history iam_user_password_history_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_password_history
    ADD CONSTRAINT iam_user_password_history_pkey PRIMARY KEY (id);


--
-- Name: iam_user_password_security iam_user_password_security_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_password_security
    ADD CONSTRAINT iam_user_password_security_pkey PRIMARY KEY (id);


--
-- Name: iam_user_role iam_user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_role
    ADD CONSTRAINT iam_user_role_pkey PRIMARY KEY (id);


--
-- Name: iam_user_two_factor iam_user_two_factor_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_two_factor
    ADD CONSTRAINT iam_user_two_factor_pkey PRIMARY KEY (id);


--
-- Name: lakala_isv_channel_merchant lakala_isv_channel_merchant_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lakala_isv_channel_merchant
    ADD CONSTRAINT lakala_isv_channel_merchant_pkey PRIMARY KEY (id);


--
-- Name: lakala_isv_key_config lakala_isv_key_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.lakala_isv_key_config
    ADD CONSTRAINT lakala_isv_key_config_pkey PRIMARY KEY (id);


--
-- Name: mch_app_info mch_app_info_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_app_info
    ADD CONSTRAINT mch_app_info_pkey PRIMARY KEY (id);


--
-- Name: mch_credential mch_credential_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_credential
    ADD CONSTRAINT mch_credential_pkey PRIMARY KEY (id);


--
-- Name: mch_info mch_info_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_info
    ADD CONSTRAINT mch_info_pkey PRIMARY KEY (id);


--
-- Name: mch_notice_record mch_notice_record_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_notice_record
    ADD CONSTRAINT mch_notice_record_pkey PRIMARY KEY (id);


--
-- Name: mch_notice_task mch_notice_task_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_notice_task
    ADD CONSTRAINT mch_notice_task_pkey PRIMARY KEY (id);


--
-- Name: mch_risk_config mch_risk_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_risk_config
    ADD CONSTRAINT mch_risk_config_pkey PRIMARY KEY (id);


--
-- Name: mch_store_info mch_store_info_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_store_info
    ADD CONSTRAINT mch_store_info_pkey PRIMARY KEY (id);


--
-- Name: mch_user mch_user_mch_no_user_id_key; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_user
    ADD CONSTRAINT mch_user_mch_no_user_id_key UNIQUE (mch_no, user_id);


--
-- Name: mch_user mch_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_user
    ADD CONSTRAINT mch_user_pkey PRIMARY KEY (id);


--
-- Name: notify_message notify_message_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notify_message
    ADD CONSTRAINT notify_message_pkey PRIMARY KEY (id);


--
-- Name: notify_notice notify_notice_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notify_notice
    ADD CONSTRAINT notify_notice_pkey PRIMARY KEY (id);


--
-- Name: notify_notice_read notify_notice_read_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notify_notice_read
    ADD CONSTRAINT notify_notice_read_pkey PRIMARY KEY (id);


--
-- Name: pay_blacklist pay_blacklist_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_blacklist
    ADD CONSTRAINT pay_blacklist_pkey PRIMARY KEY (id);


--
-- Name: pay_callback_record pay_callback_record_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_callback_record
    ADD CONSTRAINT pay_callback_record_pkey PRIMARY KEY (id);


--
-- Name: pay_channel_terminal pay_channel_terminal_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_channel_terminal
    ADD CONSTRAINT pay_channel_terminal_pkey PRIMARY KEY (id);


--
-- Name: pay_close_record pay_close_record_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_close_record
    ADD CONSTRAINT pay_close_record_pkey PRIMARY KEY (id);


--
-- Name: pay_easy_pay_refund_order pay_easy_pay_refund_order_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_easy_pay_refund_order
    ADD CONSTRAINT pay_easy_pay_refund_order_pkey PRIMARY KEY (id);


--
-- Name: pay_gateway_pay_client_env pay_gateway_pay_client_env_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_gateway_pay_client_env
    ADD CONSTRAINT pay_gateway_pay_client_env_pkey PRIMARY KEY (id);


--
-- Name: pay_gateway_pay_config pay_gateway_pay_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_gateway_pay_config
    ADD CONSTRAINT pay_gateway_pay_config_pkey PRIMARY KEY (id);


--
-- Name: pay_md_capability pay_md_capability_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_capability
    ADD CONSTRAINT pay_md_capability_pkey PRIMARY KEY (id);


--
-- Name: pay_md_channel pay_md_channel_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_channel
    ADD CONSTRAINT pay_md_channel_pkey PRIMARY KEY (id);


--
-- Name: pay_md_method pay_md_method_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_method
    ADD CONSTRAINT pay_md_method_pkey PRIMARY KEY (id);


--
-- Name: pay_md_product_capability pay_md_product_capability_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_product_capability
    ADD CONSTRAINT pay_md_product_capability_pkey PRIMARY KEY (id);


--
-- Name: pay_md_product pay_md_product_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_product
    ADD CONSTRAINT pay_md_product_pkey PRIMARY KEY (id);


--
-- Name: pay_md_provider_method pay_md_provider_method_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_provider_method
    ADD CONSTRAINT pay_md_provider_method_pkey PRIMARY KEY (id);


--
-- Name: pay_md_provider pay_md_provider_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_provider
    ADD CONSTRAINT pay_md_provider_pkey PRIMARY KEY (id);


--
-- Name: pay_platform_mobile_app pay_platform_mobile_app_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_platform_mobile_app
    ADD CONSTRAINT pay_platform_mobile_app_pkey PRIMARY KEY (id);


--
-- Name: pay_risk_hit pay_risk_hit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_risk_hit
    ADD CONSTRAINT pay_risk_hit_pkey PRIMARY KEY (id);


--
-- Name: pay_route_basic_config pay_route_basic_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_route_basic_config
    ADD CONSTRAINT pay_route_basic_config_pkey PRIMARY KEY (id);


--
-- Name: pay_route_scene_config pay_route_scene_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_route_scene_config
    ADD CONSTRAINT pay_route_scene_config_pkey PRIMARY KEY (id);


--
-- Name: pay_sync_record pay_sync_record_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_sync_record
    ADD CONSTRAINT pay_sync_record_pkey PRIMARY KEY (id);


--
-- Name: pay_terminal_channel_bind pay_terminal_channel_bind_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_terminal_channel_bind
    ADD CONSTRAINT pay_terminal_channel_bind_pkey PRIMARY KEY (id);


--
-- Name: pay_terminal_device pay_terminal_device_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_terminal_device
    ADD CONSTRAINT pay_terminal_device_pkey PRIMARY KEY (id);


--
-- Name: alipay_direct_alloc_receiver pk_alipay_direct_alloc_receiver; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_direct_alloc_receiver
    ADD CONSTRAINT pk_alipay_direct_alloc_receiver PRIMARY KEY (id);


--
-- Name: alipay_direct_app pk_alipay_direct_app; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_direct_app
    ADD CONSTRAINT pk_alipay_direct_app PRIMARY KEY (id);


--
-- Name: alipay_direct_app_auth_config pk_alipay_direct_app_auth_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_direct_app_auth_config
    ADD CONSTRAINT pk_alipay_direct_app_auth_config PRIMARY KEY (id);


--
-- Name: alipay_direct_app_key_config pk_alipay_direct_app_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_direct_app_key_config
    ADD CONSTRAINT pk_alipay_direct_app_key_config PRIMARY KEY (id);


--
-- Name: alipay_direct_channel_merchant pk_alipay_direct_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_direct_channel_merchant
    ADD CONSTRAINT pk_alipay_direct_channel_merchant PRIMARY KEY (id);


--
-- Name: alipay_isv_alloc_receiver pk_alipay_isv_alloc_receiver; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_isv_alloc_receiver
    ADD CONSTRAINT pk_alipay_isv_alloc_receiver PRIMARY KEY (id);


--
-- Name: alipay_isv_app pk_alipay_isv_app; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_isv_app
    ADD CONSTRAINT pk_alipay_isv_app PRIMARY KEY (id);


--
-- Name: alipay_isv_app_auth_config pk_alipay_isv_app_auth_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_isv_app_auth_config
    ADD CONSTRAINT pk_alipay_isv_app_auth_config PRIMARY KEY (id);


--
-- Name: alipay_isv_app_key_config pk_alipay_isv_app_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_isv_app_key_config
    ADD CONSTRAINT pk_alipay_isv_app_key_config PRIMARY KEY (id);


--
-- Name: alipay_isv_channel_merchant pk_alipay_isv_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_isv_channel_merchant
    ADD CONSTRAINT pk_alipay_isv_channel_merchant PRIMARY KEY (id);


--
-- Name: alipay_transfer_config pk_alipay_transfer_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_transfer_config
    ADD CONSTRAINT pk_alipay_transfer_config PRIMARY KEY (id);


--
-- Name: alipay_transfer_scene_config pk_alipay_transfer_scene_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alipay_transfer_scene_config
    ADD CONSTRAINT pk_alipay_transfer_scene_config PRIMARY KEY (id);


--
-- Name: douyin_direct_alloc_receiver pk_douyin_direct_alloc_receiver; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.douyin_direct_alloc_receiver
    ADD CONSTRAINT pk_douyin_direct_alloc_receiver PRIMARY KEY (id);


--
-- Name: douyin_direct_channel_merchant pk_douyin_direct_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.douyin_direct_channel_merchant
    ADD CONSTRAINT pk_douyin_direct_channel_merchant PRIMARY KEY (id);


--
-- Name: douyin_direct_key_config pk_douyin_direct_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.douyin_direct_key_config
    ADD CONSTRAINT pk_douyin_direct_key_config PRIMARY KEY (id);


--
-- Name: douyin_transfer_config pk_douyin_transfer_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.douyin_transfer_config
    ADD CONSTRAINT pk_douyin_transfer_config PRIMARY KEY (id);


--
-- Name: fuyou_isv_channel_merchant pk_fuyou_isv_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fuyou_isv_channel_merchant
    ADD CONSTRAINT pk_fuyou_isv_channel_merchant PRIMARY KEY (id);


--
-- Name: fuyou_isv_key_config pk_fuyou_isv_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.fuyou_isv_key_config
    ADD CONSTRAINT pk_fuyou_isv_key_config PRIMARY KEY (id);


--
-- Name: iam_user_social pk_iam_user_social; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_user_social
    ADD CONSTRAINT pk_iam_user_social PRIMARY KEY (id);


--
-- Name: leshua_isv_channel_merchant pk_leshua_isv_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leshua_isv_channel_merchant
    ADD CONSTRAINT pk_leshua_isv_channel_merchant PRIMARY KEY (id);


--
-- Name: leshua_isv_key_config pk_leshua_isv_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.leshua_isv_key_config
    ADD CONSTRAINT pk_leshua_isv_key_config PRIMARY KEY (id);


--
-- Name: mch_app_notify_config pk_mch_app_notify_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_app_notify_config
    ADD CONSTRAINT pk_mch_app_notify_config PRIMARY KEY (id);


--
-- Name: mch_channel_merchant pk_mch_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_channel_merchant
    ADD CONSTRAINT pk_mch_channel_merchant PRIMARY KEY (id);


--
-- Name: wechat_direct_channel_merchant pk_mch_wechat_direct_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wechat_direct_channel_merchant
    ADD CONSTRAINT pk_mch_wechat_direct_channel_merchant PRIMARY KEY (id);


--
-- Name: wechat_isv_channel_merchant pk_mch_wechat_isv_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wechat_isv_channel_merchant
    ADD CONSTRAINT pk_mch_wechat_isv_channel_merchant PRIMARY KEY (id);


--
-- Name: mch_wx_domain_verify pk_mch_wx_domain_verify; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.mch_wx_domain_verify
    ADD CONSTRAINT pk_mch_wx_domain_verify PRIMARY KEY (id);


--
-- Name: pay_abnormal_order pk_pay_abnormal_order; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_abnormal_order
    ADD CONSTRAINT pk_pay_abnormal_order PRIMARY KEY (id);


--
-- Name: pay_alloc_detail pk_pay_alloc_detail; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_alloc_detail
    ADD CONSTRAINT pk_pay_alloc_detail PRIMARY KEY (id);


--
-- Name: pay_alloc_order pk_pay_alloc_order; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_alloc_order
    ADD CONSTRAINT pk_pay_alloc_order PRIMARY KEY (id);


--
-- Name: pay_easy_pay_config pk_pay_easy_pay_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_easy_pay_config
    ADD CONSTRAINT pk_pay_easy_pay_config PRIMARY KEY (id);


--
-- Name: pay_easy_pay_credential pk_pay_easy_pay_credential; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_easy_pay_credential
    ADD CONSTRAINT pk_pay_easy_pay_credential PRIMARY KEY (id);


--
-- Name: pay_easy_pay_order pk_pay_easy_pay_order; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_easy_pay_order
    ADD CONSTRAINT pk_pay_easy_pay_order PRIMARY KEY (id);


--
-- Name: pay_fund_flow pk_pay_fund_flow; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_fund_flow
    ADD CONSTRAINT pk_pay_fund_flow PRIMARY KEY (id);


--
-- Name: pay_gateway_cashier_item pk_pay_gateway_cashier_item; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_gateway_cashier_item
    ADD CONSTRAINT pk_pay_gateway_cashier_item PRIMARY KEY (id);


--
-- Name: pay_gateway_order pk_pay_gateway_order; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_gateway_order
    ADD CONSTRAINT pk_pay_gateway_order PRIMARY KEY (id);


--
-- Name: pay_md_product_config pk_pay_md_product_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_md_product_config
    ADD CONSTRAINT pk_pay_md_product_config PRIMARY KEY (id);


--
-- Name: pay_normal_order pk_pay_normal_order; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_normal_order
    ADD CONSTRAINT pk_pay_normal_order PRIMARY KEY (id);


--
-- Name: pay_refund_order pk_pay_refund_order; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_refund_order
    ADD CONSTRAINT pk_pay_refund_order PRIMARY KEY (id);


--
-- Name: pay_route_strategy pk_pay_route_strategy; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_route_strategy
    ADD CONSTRAINT pk_pay_route_strategy PRIMARY KEY (id);


--
-- Name: pay_trade pk_pay_trade; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_trade
    ADD CONSTRAINT pk_pay_trade PRIMARY KEY (id);


--
-- Name: pay_transfer_order_alipay pk_pay_transfer_order_alipay; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_transfer_order_alipay
    ADD CONSTRAINT pk_pay_transfer_order_alipay PRIMARY KEY (id);


--
-- Name: pay_transfer_order_douyin pk_pay_transfer_order_douyin; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_transfer_order_douyin
    ADD CONSTRAINT pk_pay_transfer_order_douyin PRIMARY KEY (id);


--
-- Name: pay_transfer_order_wechat pk_pay_transfer_order_wechat; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_transfer_order_wechat
    ADD CONSTRAINT pk_pay_transfer_order_wechat PRIMARY KEY (id);


--
-- Name: pay_transfer_trade pk_pay_transfer_trade; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.pay_transfer_trade
    ADD CONSTRAINT pk_pay_transfer_trade PRIMARY KEY (id);


--
-- Name: stripe_channel_merchant pk_stripe_direct_channel_merchant; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stripe_channel_merchant
    ADD CONSTRAINT pk_stripe_direct_channel_merchant PRIMARY KEY (id);


--
-- Name: stripe_key_config pk_stripe_direct_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.stripe_key_config
    ADD CONSTRAINT pk_stripe_direct_key_config PRIMARY KEY (id);


--
-- Name: wechat_direct_alloc_receiver pk_wechat_direct_alloc_receiver; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wechat_direct_alloc_receiver
    ADD CONSTRAINT pk_wechat_direct_alloc_receiver PRIMARY KEY (id);


--
-- Name: wechat_direct_key_config pk_wechat_direct_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wechat_direct_key_config
    ADD CONSTRAINT pk_wechat_direct_key_config PRIMARY KEY (id);


--
-- Name: wechat_isv_alloc_receiver pk_wechat_isv_alloc_receiver; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wechat_isv_alloc_receiver
    ADD CONSTRAINT pk_wechat_isv_alloc_receiver PRIMARY KEY (id);


--
-- Name: wechat_isv_key_config pk_wechat_isv_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wechat_isv_key_config
    ADD CONSTRAINT pk_wechat_isv_key_config PRIMARY KEY (id);


--
-- Name: wechat_transfer_config pk_wechat_transfer_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wechat_transfer_config
    ADD CONSTRAINT pk_wechat_transfer_config PRIMARY KEY (id);


--
-- Name: yeepay_direct_key_config pk_yeepay_direct_key_config; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.yeepay_direct_key_config
    ADD CONSTRAINT pk_yeepay_direct_key_config PRIMARY KEY (id);


--
-- Name: starter_audit_unipay_log starter_audit_unipay_log_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.starter_audit_unipay_log
    ADD CONSTRAINT starter_audit_unipay_log_pkey PRIMARY KEY (id);


--
-- Name: starter_platform_file_record starter_platform_file_record_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.starter_platform_file_record
    ADD CONSTRAINT starter_platform_file_record_pkey PRIMARY KEY (id);


--
-- Name: system_sensitive_word_hit system_sensitive_word_hit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_sensitive_word_hit
    ADD CONSTRAINT system_sensitive_word_hit_pkey PRIMARY KEY (id);


--
-- Name: system_sensitive_word system_sensitive_word_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.system_sensitive_word
    ADD CONSTRAINT system_sensitive_word_pkey PRIMARY KEY (id);


--
-- Name: iam_social_login_config uk_iam_social_config_source; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iam_social_login_config
    ADD CONSTRAINT uk_iam_social_config_source UNIQUE (source);


--
-- Name: notify_notice_read uk_notify_notice_read; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notify_notice_read
    ADD CONSTRAINT uk_notify_notice_read UNIQUE (user_id, notice_id);


--
-- Name: ums_direct_key_config ums_direct_key_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.ums_direct_key_config
    ADD CONSTRAINT ums_direct_key_config_pkey PRIMARY KEY (id);


--
-- Name: union_key_config union_key_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.union_key_config
    ADD CONSTRAINT union_key_config_pkey PRIMARY KEY (id);


--
-- Name: vbill_isv_channel_merchant vbill_isv_channel_merchant_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vbill_isv_channel_merchant
    ADD CONSTRAINT vbill_isv_channel_merchant_pkey PRIMARY KEY (id);


--
-- Name: vbill_isv_key_config vbill_isv_key_config_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vbill_isv_key_config
    ADD CONSTRAINT vbill_isv_key_config_pkey PRIMARY KEY (id);


--
-- Name: wx_channel_app_capability wx_channel_app_capability_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wx_channel_app_capability
    ADD CONSTRAINT wx_channel_app_capability_pkey PRIMARY KEY (id);


--
-- Name: wx_mch_app wx_mch_app_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wx_mch_app
    ADD CONSTRAINT wx_mch_app_pkey PRIMARY KEY (id);


--
-- Name: wx_platform_app_capability wx_platform_app_capability_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wx_platform_app_capability
    ADD CONSTRAINT wx_platform_app_capability_pkey PRIMARY KEY (id);


--
-- Name: wx_platform_app wx_platform_app_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.wx_platform_app
    ADD CONSTRAINT wx_platform_app_pkey PRIMARY KEY (id);


--
-- Name: idx_alipay_transfer_config_channel; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_alipay_transfer_config_channel ON public.alipay_transfer_config USING btree (channel_mch_no, deleted);


--
-- Name: INDEX idx_alipay_transfer_config_channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_alipay_transfer_config_channel IS '按通道商户号查询转账配置';


--
-- Name: idx_alipay_transfer_scene_config_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_alipay_transfer_scene_config_mch ON public.alipay_transfer_scene_config USING btree (channel_mch_no, deleted);


--
-- Name: INDEX idx_alipay_transfer_scene_config_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_alipay_transfer_scene_config_mch IS '按通道商户号查询转账场景列表';


--
-- Name: idx_base_street_area_code; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_base_street_area_code ON public.base_street USING btree (area_code);


--
-- Name: INDEX idx_base_street_area_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_base_street_area_code IS '按区县编码查询街道';


--
-- Name: idx_base_user_protocol_type_client; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_base_user_protocol_type_client ON public.base_user_protocol USING btree (type, client_type);


--
-- Name: INDEX idx_base_user_protocol_type_client; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_base_user_protocol_type_client IS '按协议类型与终端类型查询';


--
-- Name: idx_device_qr_code_store_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_device_qr_code_store_no ON public.device_qr_code USING btree (store_no);


--
-- Name: INDEX idx_device_qr_code_store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_device_qr_code_store_no IS '按门店号查询二维码设备';


--
-- Name: idx_douyin_transfer_config_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_douyin_transfer_config_mch ON public.douyin_transfer_config USING btree (channel_mch_no, deleted);


--
-- Name: INDEX idx_douyin_transfer_config_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_douyin_transfer_config_mch IS '按通道商户号查询转账配置';


--
-- Name: idx_dy_channel_app_cap_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_dy_channel_app_cap_mch ON public.dy_channel_app_capability USING btree (mch_no);


--
-- Name: INDEX idx_dy_channel_app_cap_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_dy_channel_app_cap_mch IS '商户号关联查询';


--
-- Name: idx_dy_channel_app_cap_ref; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_dy_channel_app_cap_ref ON public.dy_channel_app_capability USING btree (app_scope, dy_app_ref_id);


--
-- Name: INDEX idx_dy_channel_app_cap_ref; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_dy_channel_app_cap_ref IS '按应用范围+应用引用查询能力';


--
-- Name: idx_dy_mch_app_app_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_dy_mch_app_app_type ON public.dy_mch_app USING btree (mch_no, app_type);


--
-- Name: INDEX idx_dy_mch_app_app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_dy_mch_app_app_type IS '按商户号+应用类型查询';


--
-- Name: idx_dy_mch_app_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_dy_mch_app_mch_no ON public.dy_mch_app USING btree (mch_no);


--
-- Name: INDEX idx_dy_mch_app_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_dy_mch_app_mch_no IS '商户号关联查询';


--
-- Name: idx_dy_platform_app_app_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_dy_platform_app_app_type ON public.dy_platform_app USING btree (app_type);


--
-- Name: INDEX idx_dy_platform_app_app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_dy_platform_app_app_type IS '按应用类型筛选';


--
-- Name: idx_easy_pay_order_order_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_easy_pay_order_order_id ON public.pay_easy_pay_order USING btree (order_id);


--
-- Name: INDEX idx_easy_pay_order_order_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_easy_pay_order_order_id IS '按支付订单 ID 查询';


--
-- Name: idx_easy_pay_order_pid_out; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_easy_pay_order_pid_out ON public.pay_easy_pay_order USING btree (pid, out_trade_no);


--
-- Name: INDEX idx_easy_pay_order_pid_out; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_easy_pay_order_pid_out IS '按易支付商户号+商户订单号查询';


--
-- Name: idx_easy_pay_order_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_easy_pay_order_trade_no ON public.pay_easy_pay_order USING btree (trade_no);


--
-- Name: INDEX idx_easy_pay_order_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_easy_pay_order_trade_no IS '按平台交易号查询';


--
-- Name: idx_gateway_cashier_item_bucket; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_gateway_cashier_item_bucket ON public.pay_gateway_cashier_item USING btree (app_id, cashier_type, client_env);


--
-- Name: INDEX idx_gateway_cashier_item_bucket; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_gateway_cashier_item_bucket IS '按应用+收银类型+客户端环境查询收银台项';


--
-- Name: idx_gateway_cashier_item_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_gateway_cashier_item_mch ON public.pay_gateway_cashier_item USING btree (mch_no);


--
-- Name: INDEX idx_gateway_cashier_item_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_gateway_cashier_item_mch IS '商户号关联查询';


--
-- Name: idx_gateway_order_app_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_gateway_order_app_biz ON public.pay_gateway_order USING btree (app_id, biz_order_no);


--
-- Name: INDEX idx_gateway_order_app_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_gateway_order_app_biz IS '按应用+业务订单号查询';


--
-- Name: idx_gateway_order_mch_store; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_gateway_order_mch_store ON public.pay_gateway_order USING btree (mch_no, store_no);


--
-- Name: INDEX idx_gateway_order_mch_store; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_gateway_order_mch_store IS '按商户+门店维度查询';


--
-- Name: idx_iam_role_code_role_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_iam_role_code_role_id ON public.iam_role_code USING btree (role_id);


--
-- Name: INDEX idx_iam_role_code_role_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_iam_role_code_role_id IS '角色权限码关联表角色ID索引';


--
-- Name: idx_iam_user_info_client_account; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_iam_user_info_client_account ON public.iam_user_info USING btree (client_code, account) WHERE (deleted = false);


--
-- Name: INDEX idx_iam_user_info_client_account; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_iam_user_info_client_account IS '用户信息表终端账号索引';


--
-- Name: idx_iam_user_info_client_email; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_iam_user_info_client_email ON public.iam_user_info USING btree (client_code, email) WHERE ((deleted = false) AND (email IS NOT NULL) AND ((email)::text <> ''::text));


--
-- Name: INDEX idx_iam_user_info_client_email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_iam_user_info_client_email IS '用户信息表终端邮箱索引';


--
-- Name: idx_iam_user_info_client_phone; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_iam_user_info_client_phone ON public.iam_user_info USING btree (client_code, phone) WHERE ((deleted = false) AND (phone IS NOT NULL) AND ((phone)::text <> ''::text));


--
-- Name: INDEX idx_iam_user_info_client_phone; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_iam_user_info_client_phone IS '用户信息表终端手机号索引';


--
-- Name: idx_iam_user_passkey_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_iam_user_passkey_user_id ON public.iam_user_passkey USING btree (user_id);


--
-- Name: INDEX idx_iam_user_passkey_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_iam_user_passkey_user_id IS '按用户 ID 查询已绑定通行密钥';


--
-- Name: idx_iam_user_role_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_iam_user_role_user_id ON public.iam_user_role USING btree (user_id);


--
-- Name: INDEX idx_iam_user_role_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_iam_user_role_user_id IS '用户角色关联表用户ID索引';


--
-- Name: idx_iam_user_social_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_iam_user_social_user_id ON public.iam_user_social USING btree (user_id);


--
-- Name: INDEX idx_iam_user_social_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_iam_user_social_user_id IS '按用户 ID 查询第三方账号绑定';


--
-- Name: idx_mch_app_info_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_app_info_mch_no ON public.mch_app_info USING btree (mch_no);


--
-- Name: INDEX idx_mch_app_info_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_app_info_mch_no IS '商户号索引';


--
-- Name: idx_mch_credential_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_credential_mch_no ON public.mch_credential USING btree (mch_no);


--
-- Name: INDEX idx_mch_credential_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_credential_mch_no IS '商户号索引';


--
-- Name: idx_mch_info_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_info_mch_no ON public.mch_info USING btree (mch_no);


--
-- Name: INDEX idx_mch_info_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_info_mch_no IS '商户号关联查询';


--
-- Name: idx_mch_info_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_info_status ON public.mch_info USING btree (status);


--
-- Name: INDEX idx_mch_info_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_info_status IS '状态筛选';


--
-- Name: idx_mch_notice_record_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_notice_record_create_time ON public.mch_notice_record USING btree (create_time DESC);


--
-- Name: INDEX idx_mch_notice_record_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_notice_record_create_time IS '按时间倒序分页/范围扫描（通知投递记录高频）';


--
-- Name: idx_mch_notice_record_task_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_notice_record_task_id ON public.mch_notice_record USING btree (task_id);


--
-- Name: INDEX idx_mch_notice_record_task_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_notice_record_task_id IS '按通知任务 ID 查询投递记录';


--
-- Name: idx_mch_notice_task_biz_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_notice_task_biz_no ON public.mch_notice_task USING btree (biz_no);


--
-- Name: INDEX idx_mch_notice_task_biz_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_notice_task_biz_no IS '按业务单号查询通知任务';


--
-- Name: idx_mch_notice_task_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_notice_task_create_time ON public.mch_notice_task USING btree (create_time DESC);


--
-- Name: INDEX idx_mch_notice_task_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_notice_task_create_time IS '按时间倒序分页/范围扫描（通知任务高频）';


--
-- Name: idx_mch_notice_task_next_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_notice_task_next_time ON public.mch_notice_task USING btree (next_time) WHERE ((deleted = false) AND (success = false));


--
-- Name: INDEX idx_mch_notice_task_next_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_notice_task_next_time IS '兜底扫描: 未成功且 next_time 已到的任务';


--
-- Name: idx_mch_notice_task_success; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_notice_task_success ON public.mch_notice_task USING btree (success);


--
-- Name: INDEX idx_mch_notice_task_success; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_notice_task_success IS '按投递结果筛选（成功/失败统计）';


--
-- Name: idx_mch_store_info_mch_default; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_store_info_mch_default ON public.mch_store_info USING btree (mch_no, default_store);


--
-- Name: INDEX idx_mch_store_info_mch_default; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_store_info_mch_default IS '按商户号查默认门店';


--
-- Name: idx_mch_store_info_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_store_info_mch_no ON public.mch_store_info USING btree (mch_no, deleted);


--
-- Name: INDEX idx_mch_store_info_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_store_info_mch_no IS '按商户号查询门店列表';


--
-- Name: idx_mch_wx_domain_verify_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_wx_domain_verify_mch_no ON public.mch_wx_domain_verify USING btree (mch_no);


--
-- Name: INDEX idx_mch_wx_domain_verify_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_wx_domain_verify_mch_no IS '商户号关联查询';


--
-- Name: idx_mch_wx_domain_verify_platform; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_mch_wx_domain_verify_platform ON public.mch_wx_domain_verify USING btree (platform);


--
-- Name: INDEX idx_mch_wx_domain_verify_platform; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_mch_wx_domain_verify_platform IS '按平台筛选';


--
-- Name: idx_normal_order_app_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_normal_order_app_biz ON public.pay_normal_order USING btree (app_id, biz_order_no);


--
-- Name: INDEX idx_normal_order_app_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_normal_order_app_biz IS '按应用+业务订单号查询';


--
-- Name: idx_normal_order_mch_store; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_normal_order_mch_store ON public.pay_normal_order USING btree (mch_no, store_no);


--
-- Name: INDEX idx_normal_order_mch_store; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_normal_order_mch_store IS '按商户+门店维度查询';


--
-- Name: idx_notify_mail_record_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notify_mail_record_create_time ON public.notify_mail_record USING btree (create_time);


--
-- Name: INDEX idx_notify_mail_record_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_notify_mail_record_create_time IS '发送记录创建时间索引(分页排序)';


--
-- Name: idx_notify_mail_record_receiver_email; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notify_mail_record_receiver_email ON public.notify_mail_record USING btree (receiver_email);


--
-- Name: INDEX idx_notify_mail_record_receiver_email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_notify_mail_record_receiver_email IS '收件邮箱索引(按收件人查询)';


--
-- Name: idx_notify_mail_record_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notify_mail_record_status ON public.notify_mail_record USING btree (status);


--
-- Name: INDEX idx_notify_mail_record_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_notify_mail_record_status IS '发送状态索引(失败记录定位)';


--
-- Name: idx_notify_message_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notify_message_user ON public.notify_message USING btree (user_id, deleted, is_read);


--
-- Name: INDEX idx_notify_message_user; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_notify_message_user IS '按用户查询站内信（含已读/未读筛选）';


--
-- Name: idx_notify_notice_read_user; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notify_notice_read_user ON public.notify_notice_read USING btree (user_id);


--
-- Name: INDEX idx_notify_notice_read_user; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_notify_notice_read_user IS '按用户查询公告阅读记录';


--
-- Name: idx_notify_notice_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_notify_notice_status ON public.notify_notice USING btree (status, deleted, effective_time, expire_time);


--
-- Name: INDEX idx_notify_notice_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_notify_notice_status IS '按状态+生效/失效时间筛选有效公告';


--
-- Name: idx_password_history_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_password_history_user_id ON public.iam_user_password_history USING btree (user_id);


--
-- Name: INDEX idx_password_history_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_password_history_user_id IS '用户ID索引';


--
-- Name: idx_pay_abnormal_order_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_abnormal_order_create_time ON public.pay_abnormal_order USING btree (create_time);


--
-- Name: INDEX idx_pay_abnormal_order_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_abnormal_order_create_time IS '按发现时间查询';


--
-- Name: idx_pay_alloc_detail_alloc_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_alloc_detail_alloc_no ON public.pay_alloc_detail USING btree (alloc_no);


--
-- Name: INDEX idx_pay_alloc_detail_alloc_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_alloc_detail_alloc_no IS '分账单号反查明细';


--
-- Name: idx_pay_alloc_order_status_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_alloc_order_status_time ON public.pay_alloc_order USING btree (status, create_time);


--
-- Name: INDEX idx_pay_alloc_order_status_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_alloc_order_status_time IS '定时同步扫描(状态+创建时间)';


--
-- Name: idx_pay_alloc_order_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_alloc_order_trade_no ON public.pay_alloc_order USING btree (trade_no);


--
-- Name: INDEX idx_pay_alloc_order_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_alloc_order_trade_no IS '原支付交易号反查分账单';


--
-- Name: idx_pay_blacklist_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_blacklist_status ON public.pay_blacklist USING btree (status);


--
-- Name: INDEX idx_pay_blacklist_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_blacklist_status IS '状态筛选（启用/停用）';


--
-- Name: idx_pay_blacklist_type_value; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_blacklist_type_value ON public.pay_blacklist USING btree (type, value);


--
-- Name: INDEX idx_pay_blacklist_type_value; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_blacklist_type_value IS '按类型+值查询黑名单';


--
-- Name: idx_pay_callback_record_channel_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_callback_record_channel_mch_no ON public.pay_callback_record USING btree (channel_mch_no);


--
-- Name: INDEX idx_pay_callback_record_channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_callback_record_channel_mch_no IS '按通道商户号查询回调';


--
-- Name: idx_pay_callback_record_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_callback_record_create_time ON public.pay_callback_record USING btree (create_time DESC);


--
-- Name: INDEX idx_pay_callback_record_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_callback_record_create_time IS '按时间倒序分页/范围扫描（回调流水高频）';


--
-- Name: idx_pay_callback_record_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_callback_record_mch_no ON public.pay_callback_record USING btree (mch_no);


--
-- Name: INDEX idx_pay_callback_record_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_callback_record_mch_no IS '商户号关联查询';


--
-- Name: idx_pay_callback_record_out_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_callback_record_out_trade_no ON public.pay_callback_record USING btree (out_trade_no);


--
-- Name: INDEX idx_pay_callback_record_out_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_callback_record_out_trade_no IS '按商户订单号查询回调';


--
-- Name: idx_pay_callback_record_product; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_callback_record_product ON public.pay_callback_record USING btree (product);


--
-- Name: INDEX idx_pay_callback_record_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_callback_record_product IS '按支付产品筛选';


--
-- Name: idx_pay_callback_record_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_callback_record_trade_no ON public.pay_callback_record USING btree (trade_no);


--
-- Name: INDEX idx_pay_callback_record_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_callback_record_trade_no IS '按平台交易号查询回调';


--
-- Name: idx_pay_channel_terminal_channel_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_channel_terminal_channel_mch_no ON public.pay_channel_terminal USING btree (channel_mch_no);


--
-- Name: INDEX idx_pay_channel_terminal_channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_channel_terminal_channel_mch_no IS '按通道商户号查询终端';


--
-- Name: idx_pay_channel_terminal_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_channel_terminal_mch_no ON public.pay_channel_terminal USING btree (mch_no);


--
-- Name: INDEX idx_pay_channel_terminal_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_channel_terminal_mch_no IS '商户号关联查询';


--
-- Name: idx_pay_channel_terminal_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_channel_terminal_status ON public.pay_channel_terminal USING btree (status);


--
-- Name: INDEX idx_pay_channel_terminal_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_channel_terminal_status IS '状态筛选';


--
-- Name: idx_pay_easy_pay_refund_order_out_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_easy_pay_refund_order_out_trade_no ON public.pay_easy_pay_refund_order USING btree (out_trade_no);


--
-- Name: INDEX idx_pay_easy_pay_refund_order_out_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_easy_pay_refund_order_out_trade_no IS '按商户订单号查询退款';


--
-- Name: idx_pay_easy_pay_refund_order_refund_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_easy_pay_refund_order_refund_id ON public.pay_easy_pay_refund_order USING btree (refund_id);


--
-- Name: INDEX idx_pay_easy_pay_refund_order_refund_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_easy_pay_refund_order_refund_id IS '按平台退款单 ID 查询';


--
-- Name: idx_pay_easy_pay_refund_order_refund_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_easy_pay_refund_order_refund_no ON public.pay_easy_pay_refund_order USING btree (refund_no);


--
-- Name: INDEX idx_pay_easy_pay_refund_order_refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_easy_pay_refund_order_refund_no IS '按商户退款号查询';


--
-- Name: idx_pay_fund_flow_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_fund_flow_create_time ON public.pay_fund_flow USING btree (create_time);


--
-- Name: INDEX idx_pay_fund_flow_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_fund_flow_create_time IS '按流水时间范围查询';


--
-- Name: idx_pay_fund_flow_mch_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_fund_flow_mch_time ON public.pay_fund_flow USING btree (mch_no, create_time);


--
-- Name: INDEX idx_pay_fund_flow_mch_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_fund_flow_mch_time IS '商户维度时间段流水查询';


--
-- Name: idx_pay_gateway_pay_env_config_id; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_gateway_pay_env_config_id ON public.pay_gateway_pay_client_env USING btree (config_id);


--
-- Name: INDEX idx_pay_gateway_pay_env_config_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_gateway_pay_env_config_id IS '网关支付环境外键查询索引(关联 pay_gateway_pay_config 主键)';


--
-- Name: idx_pay_md_product_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX idx_pay_md_product_code ON public.pay_md_product USING btree (code) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_md_product_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_md_product_code IS '产品编码唯一索引';


--
-- Name: idx_pay_risk_hit_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_risk_hit_create_time ON public.pay_risk_hit USING btree (create_time DESC);


--
-- Name: INDEX idx_pay_risk_hit_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_risk_hit_create_time IS '按时间倒序分页/范围扫描（风控命中高频）';


--
-- Name: idx_pay_risk_hit_hit_type_value; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_risk_hit_hit_type_value ON public.pay_risk_hit USING btree (hit_type, hit_value);


--
-- Name: INDEX idx_pay_risk_hit_hit_type_value; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_risk_hit_hit_type_value IS '按命中类型+值查询';


--
-- Name: idx_pay_risk_hit_phase; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_risk_hit_phase ON public.pay_risk_hit USING btree (phase);


--
-- Name: INDEX idx_pay_risk_hit_phase; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_risk_hit_phase IS '按风控阶段筛选';


--
-- Name: idx_pay_risk_hit_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_risk_hit_trade_no ON public.pay_risk_hit USING btree (trade_no);


--
-- Name: INDEX idx_pay_risk_hit_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_risk_hit_trade_no IS '按交易号查询风控命中';


--
-- Name: idx_pay_terminal_channel_bind_channel; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_terminal_channel_bind_channel ON public.pay_terminal_channel_bind USING btree (channel_terminal_id);


--
-- Name: INDEX idx_pay_terminal_channel_bind_channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_terminal_channel_bind_channel IS '按通道终端 ID 查询绑定';


--
-- Name: idx_pay_terminal_channel_bind_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_terminal_channel_bind_mch ON public.pay_terminal_channel_bind USING btree (mch_no);


--
-- Name: INDEX idx_pay_terminal_channel_bind_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_terminal_channel_bind_mch IS '商户号关联查询';


--
-- Name: idx_pay_terminal_channel_bind_system; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_terminal_channel_bind_system ON public.pay_terminal_channel_bind USING btree (system_terminal_no);


--
-- Name: INDEX idx_pay_terminal_channel_bind_system; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_terminal_channel_bind_system IS '按系统终端号查询绑定';


--
-- Name: idx_pay_terminal_device_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_terminal_device_mch_no ON public.pay_terminal_device USING btree (mch_no);


--
-- Name: INDEX idx_pay_terminal_device_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_terminal_device_mch_no IS '商户号关联查询';


--
-- Name: idx_pay_terminal_device_store_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_terminal_device_store_no ON public.pay_terminal_device USING btree (store_no);


--
-- Name: INDEX idx_pay_terminal_device_store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_terminal_device_store_no IS '按门店号查询设备';


--
-- Name: idx_pay_trade_container; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_trade_container ON public.pay_trade USING btree (container_id, trade_type);


--
-- Name: INDEX idx_pay_trade_container; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_trade_container IS '按业务容器+交易类型查询';


--
-- Name: idx_pay_trade_mch_channel; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_trade_mch_channel ON public.pay_trade USING btree (mch_no, channel_mch_no);


--
-- Name: INDEX idx_pay_trade_mch_channel; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_trade_mch_channel IS '按商户+通道商户维度查询';


--
-- Name: idx_pay_trade_mch_store; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_trade_mch_store ON public.pay_trade USING btree (mch_no, store_no);


--
-- Name: INDEX idx_pay_trade_mch_store; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_trade_mch_store IS '按商户+门店维度查询';


--
-- Name: idx_pay_trade_out_order_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_trade_out_order_no ON public.pay_trade USING btree (out_order_no);


--
-- Name: INDEX idx_pay_trade_out_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_trade_out_order_no IS '按商户订单号查询交易';


--
-- Name: idx_pay_trade_relation_order_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_trade_relation_order_no ON public.pay_trade USING btree (relation_order_no);


--
-- Name: INDEX idx_pay_trade_relation_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_trade_relation_order_no IS '按关联订单号查询交易';


--
-- Name: idx_pay_trade_status_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_trade_status_create_time ON public.pay_trade USING btree (status, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_trade_status_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_trade_status_create_time IS '按资金状态+创建时间窗口扫描(定时同步)';


--
-- Name: idx_pay_transfer_order_alipay_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_order_alipay_mch ON public.pay_transfer_order_alipay USING btree (mch_no, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_order_alipay_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_order_alipay_mch IS '按商户号+创建时间查询';


--
-- Name: idx_pay_transfer_order_alipay_status_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_order_alipay_status_time ON public.pay_transfer_order_alipay USING btree (status, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_order_alipay_status_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_order_alipay_status_time IS '按状态+创建时间窗口扫描(定时同步)';


--
-- Name: idx_pay_transfer_order_douyin_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_order_douyin_mch ON public.pay_transfer_order_douyin USING btree (mch_no, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_order_douyin_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_order_douyin_mch IS '按商户号+创建时间查询';


--
-- Name: idx_pay_transfer_order_douyin_status_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_order_douyin_status_time ON public.pay_transfer_order_douyin USING btree (status, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_order_douyin_status_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_order_douyin_status_time IS '按状态+创建时间窗口扫描(定时同步)';


--
-- Name: idx_pay_transfer_order_wechat_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_order_wechat_mch ON public.pay_transfer_order_wechat USING btree (mch_no, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_order_wechat_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_order_wechat_mch IS '按商户号+创建时间查询';


--
-- Name: idx_pay_transfer_order_wechat_status_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_order_wechat_status_time ON public.pay_transfer_order_wechat USING btree (status, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_order_wechat_status_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_order_wechat_status_time IS '按状态+创建时间窗口扫描(定时同步)';


--
-- Name: idx_pay_transfer_trade_container; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_trade_container ON public.pay_transfer_trade USING btree (container_id, container_channel);


--
-- Name: INDEX idx_pay_transfer_trade_container; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_trade_container IS '按所属转账单+通道查询';


--
-- Name: idx_pay_transfer_trade_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_trade_mch ON public.pay_transfer_trade USING btree (mch_no, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_trade_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_trade_mch IS '按商户号+创建时间查询';


--
-- Name: idx_pay_transfer_trade_status_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_pay_transfer_trade_status_time ON public.pay_transfer_trade USING btree (status, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_pay_transfer_trade_status_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_pay_transfer_trade_status_time IS '按状态+创建时间窗口扫描(定时同步)';


--
-- Name: idx_refund_order_app_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refund_order_app_biz ON public.pay_refund_order USING btree (app_id, biz_refund_no);


--
-- Name: INDEX idx_refund_order_app_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_refund_order_app_biz IS '按应用+业务退款号查询';


--
-- Name: idx_refund_order_mch_store; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refund_order_mch_store ON public.pay_refund_order USING btree (mch_no, store_no);


--
-- Name: INDEX idx_refund_order_mch_store; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_refund_order_mch_store IS '按商户+门店维度查询';


--
-- Name: idx_refund_order_order_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refund_order_order_no ON public.pay_refund_order USING btree (trade_no);


--
-- Name: INDEX idx_refund_order_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_refund_order_order_no IS '按支付订单号查询退款';


--
-- Name: idx_refund_order_status_create_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_refund_order_status_create_time ON public.pay_refund_order USING btree (status, create_time) WHERE (deleted = false);


--
-- Name: INDEX idx_refund_order_status_create_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_refund_order_status_create_time IS '按退款状态+创建时间窗口扫描(定时同步)';


--
-- Name: idx_role_menu_role_client; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_role_menu_role_client ON public.iam_role_menu USING btree (role_id, client_code);


--
-- Name: INDEX idx_role_menu_role_client; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_role_menu_role_client IS '角色菜单表按角色ID和终端编码的普通索引';


--
-- Name: idx_starter_audit_unipay_log_mch_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_starter_audit_unipay_log_mch_time ON public.starter_audit_unipay_log USING btree (mch_no, operate_time DESC);


--
-- Name: INDEX idx_starter_audit_unipay_log_mch_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_starter_audit_unipay_log_mch_time IS '按商户号+时间倒序查询审计日志';


--
-- Name: idx_starter_audit_unipay_log_success_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_starter_audit_unipay_log_success_time ON public.starter_audit_unipay_log USING btree (success, operate_time DESC);


--
-- Name: INDEX idx_starter_audit_unipay_log_success_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_starter_audit_unipay_log_success_time IS '按成功标志+时间筛选审计日志';


--
-- Name: idx_starter_audit_unipay_log_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_starter_audit_unipay_log_time ON public.starter_audit_unipay_log USING btree (operate_time DESC);


--
-- Name: INDEX idx_starter_audit_unipay_log_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_starter_audit_unipay_log_time IS '按时间倒序分页/范围扫描（审计日志高频）';


--
-- Name: idx_starter_audit_unipay_log_trace; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_starter_audit_unipay_log_trace ON public.starter_audit_unipay_log USING btree (trace_id);


--
-- Name: INDEX idx_starter_audit_unipay_log_trace; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_starter_audit_unipay_log_trace IS '按链路追踪 ID 查询';


--
-- Name: idx_system_sensitive_word_hit_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_system_sensitive_word_hit_mch ON public.system_sensitive_word_hit USING btree (mch_no, create_time);


--
-- Name: INDEX idx_system_sensitive_word_hit_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_system_sensitive_word_hit_mch IS '按商户号+时间查询命中记录';


--
-- Name: idx_system_sensitive_word_hit_scene; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_system_sensitive_word_hit_scene ON public.system_sensitive_word_hit USING btree (scene);


--
-- Name: INDEX idx_system_sensitive_word_hit_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_system_sensitive_word_hit_scene IS '按场景筛选命中';


--
-- Name: idx_system_sensitive_word_hit_time; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_system_sensitive_word_hit_time ON public.system_sensitive_word_hit USING btree (create_time);


--
-- Name: INDEX idx_system_sensitive_word_hit_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_system_sensitive_word_hit_time IS '按时间分页/范围扫描';


--
-- Name: idx_system_sensitive_word_hit_word; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_system_sensitive_word_hit_word ON public.system_sensitive_word_hit USING btree (hit_word);


--
-- Name: INDEX idx_system_sensitive_word_hit_word; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_system_sensitive_word_hit_word IS '按命中词查询';


--
-- Name: idx_system_sensitive_word_status; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_system_sensitive_word_status ON public.system_sensitive_word USING btree (status);


--
-- Name: INDEX idx_system_sensitive_word_status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_system_sensitive_word_status IS '状态筛选（启用/停用）';


--
-- Name: idx_user_protocol_version_protocol; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_user_protocol_version_protocol ON public.base_user_protocol_version USING btree (protocol_id);


--
-- Name: INDEX idx_user_protocol_version_protocol; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_user_protocol_version_protocol IS '按协议 ID 查询版本列表';


--
-- Name: idx_wechat_transfer_config_app; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_wechat_transfer_config_app ON public.wechat_transfer_config USING btree (transfer_app_ref_id, deleted);


--
-- Name: INDEX idx_wechat_transfer_config_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_wechat_transfer_config_app IS '按发起应用引用查询(应用换绑/清理用)';


--
-- Name: idx_wx_channel_app_cap_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_wx_channel_app_cap_mch ON public.wx_channel_app_capability USING btree (mch_no);


--
-- Name: INDEX idx_wx_channel_app_cap_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_wx_channel_app_cap_mch IS '商户号关联查询';


--
-- Name: idx_wx_channel_app_cap_ref; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_wx_channel_app_cap_ref ON public.wx_channel_app_capability USING btree (app_scope, wx_app_ref_id);


--
-- Name: INDEX idx_wx_channel_app_cap_ref; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_wx_channel_app_cap_ref IS '按应用范围+应用引用查询能力';


--
-- Name: idx_wx_mch_app_app_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_wx_mch_app_app_type ON public.wx_mch_app USING btree (mch_no, app_type);


--
-- Name: INDEX idx_wx_mch_app_app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_wx_mch_app_app_type IS '按商户号+应用类型查询';


--
-- Name: idx_wx_mch_app_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_wx_mch_app_mch_no ON public.wx_mch_app USING btree (mch_no);


--
-- Name: INDEX idx_wx_mch_app_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_wx_mch_app_mch_no IS '商户号关联查询';


--
-- Name: idx_wx_platform_app_app_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_wx_platform_app_app_type ON public.wx_platform_app USING btree (app_type);


--
-- Name: INDEX idx_wx_platform_app_app_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.idx_wx_platform_app_app_type IS '按应用类型筛选';


--
-- Name: INDEX mch_user_mch_no_user_id_key; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.mch_user_mch_no_user_id_key IS '同一商户同一用户唯一';


--
-- Name: uk_adapay_direct_key_config_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_adapay_direct_key_config_mch ON public.adapay_direct_key_config USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_adapay_direct_key_config_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_adapay_direct_key_config_mch IS '同一通道商户密钥唯一';


--
-- Name: uk_alipay_direct_alloc_receiver; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_direct_alloc_receiver ON public.alipay_direct_alloc_receiver USING btree (channel_mch_no, receiver_type, account_hash) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_direct_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_direct_alloc_receiver IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';


--
-- Name: uk_alipay_direct_app_cap; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_direct_app_cap ON public.alipay_direct_app_capability USING btree (channel_mch_no, capability) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_direct_app_cap; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_direct_app_cap IS '同一通道商户下支付能力唯一，防重复开通';


--
-- Name: uk_alipay_direct_app_key_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_direct_app_key_sandbox ON public.alipay_direct_app_key_config USING btree (alipay_direct_app_id, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_direct_app_key_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_direct_app_key_sandbox IS '同一应用同一环境密钥唯一';


--
-- Name: uk_alipay_direct_app_mch_channel_appid; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_direct_app_mch_channel_appid ON public.alipay_direct_app USING btree (mch_no, channel_mch_no, ali_app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_direct_app_mch_channel_appid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_direct_app_mch_channel_appid IS '同一商户同一通道商户下支付宝应用ID唯一(与业务层查重作用域对齐, 对齐微信/抖音商户应用唯一约束)';


--
-- Name: uk_alipay_isv_alloc_receiver; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_isv_alloc_receiver ON public.alipay_isv_alloc_receiver USING btree (channel_mch_no, receiver_type, account_hash) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_isv_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_isv_alloc_receiver IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';


--
-- Name: uk_alipay_isv_app_appid; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_isv_app_appid ON public.alipay_isv_app USING btree (ali_app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_isv_app_appid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_isv_app_appid IS '支付宝服务商应用ID全局唯一(与业务层查重作用域对齐, 对齐微信/抖音平台应用唯一约束)';


--
-- Name: uk_alipay_transfer_config_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_transfer_config_mch ON public.alipay_transfer_config USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_transfer_config_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_transfer_config_mch IS '同一通道商户仅一条转账配置(部分唯一索引)';


--
-- Name: uk_alipay_transfer_scene_config_default; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_transfer_scene_config_default ON public.alipay_transfer_scene_config USING btree (channel_mch_no) WHERE ((is_default = true) AND (enabled = true) AND (deleted = false));


--
-- Name: INDEX uk_alipay_transfer_scene_config_default; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_transfer_scene_config_default IS '同一通道商户最多一个默认场景且必须处于启用状态(部分唯一索引)';


--
-- Name: uk_alipay_transfer_scene_config_scene; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_alipay_transfer_scene_config_scene ON public.alipay_transfer_scene_config USING btree (channel_mch_no, scene_name) WHERE (deleted = false);


--
-- Name: INDEX uk_alipay_transfer_scene_config_scene; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_alipay_transfer_scene_config_scene IS '同一通道商户同一转账场景不可重复(部分唯一索引)';


--
-- Name: uk_base_city_adjacent; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_base_city_adjacent ON public.base_city_adjacent USING btree (city_code, adjacent_city_code);


--
-- Name: INDEX uk_base_city_adjacent; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_base_city_adjacent IS '同一城市与其相邻城市关系唯一（防重复灌入）';


--
-- Name: uk_device_qr_code_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_device_qr_code_code ON public.device_qr_code USING btree (code);


--
-- Name: INDEX uk_device_qr_code_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_device_qr_code_code IS '二维码设备编码唯一';


--
-- Name: uk_douyin_direct_alloc_receiver; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_douyin_direct_alloc_receiver ON public.douyin_direct_alloc_receiver USING btree (channel_mch_no, receiver_type, account_hash) WHERE (deleted = false);


--
-- Name: INDEX uk_douyin_direct_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_douyin_direct_alloc_receiver IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';


--
-- Name: uk_douyin_direct_key_cmchno; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_douyin_direct_key_cmchno ON public.douyin_direct_key_config USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_douyin_direct_key_cmchno; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_douyin_direct_key_cmchno IS '同一通道商户密钥配置唯一';


--
-- Name: uk_douyin_direct_mch_dyid; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_douyin_direct_mch_dyid ON public.douyin_direct_channel_merchant USING btree (mch_no, dy_mch_id) WHERE (deleted = false);


--
-- Name: INDEX uk_douyin_direct_mch_dyid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_douyin_direct_mch_dyid IS '同一商户下抖音商户号唯一';


--
-- Name: uk_douyin_transfer_config_mch; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_douyin_transfer_config_mch ON public.douyin_transfer_config USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_douyin_transfer_config_mch; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_douyin_transfer_config_mch IS '同一通道商户仅一条转账配置(部分唯一索引)';


--
-- Name: uk_dy_channel_app_cap; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_dy_channel_app_cap ON public.dy_channel_app_capability USING btree (channel_mch_no, capability, app_scope) WHERE (deleted = false);


--
-- Name: INDEX uk_dy_channel_app_cap; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_dy_channel_app_cap IS '通道商户+能力+应用范围唯一';


--
-- Name: uk_dy_mch_app_mch_douyin; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_dy_mch_app_mch_douyin ON public.dy_mch_app USING btree (mch_no, douyin_app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_dy_mch_app_mch_douyin; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_dy_mch_app_mch_douyin IS '同一商户下抖音 AppID 唯一';


--
-- Name: uk_dy_platform_app_cap_product; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_dy_platform_app_cap_product ON public.dy_platform_app_capability USING btree (product, capability) WHERE (deleted = false);


--
-- Name: INDEX uk_dy_platform_app_cap_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_dy_platform_app_cap_product IS '产品+能力唯一';


--
-- Name: uk_dy_platform_app_dy_app_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_dy_platform_app_dy_app_id ON public.dy_platform_app USING btree (douyin_app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_dy_platform_app_dy_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_dy_platform_app_dy_app_id IS '抖音平台 AppID 唯一';


--
-- Name: uk_easy_pay_config_app_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_easy_pay_config_app_id ON public.pay_easy_pay_config USING btree (app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_easy_pay_config_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_easy_pay_config_app_id IS '同一应用配置唯一';


--
-- Name: uk_easy_pay_config_pid; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_easy_pay_config_pid ON public.pay_easy_pay_config USING btree (pid) WHERE (deleted = false);


--
-- Name: INDEX uk_easy_pay_config_pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_easy_pay_config_pid IS '同一易支付商户号配置唯一';


--
-- Name: uk_easy_pay_credential_app_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_easy_pay_credential_app_id ON public.pay_easy_pay_credential USING btree (app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_easy_pay_credential_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_easy_pay_credential_app_id IS '同一应用凭证唯一';


--
-- Name: uk_easy_pay_credential_pid; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_easy_pay_credential_pid ON public.pay_easy_pay_credential USING btree (pid) WHERE (deleted = false);


--
-- Name: INDEX uk_easy_pay_credential_pid; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_easy_pay_credential_pid IS '同一易支付商户号凭证唯一';


--
-- Name: uk_easy_pay_order_app_out; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_easy_pay_order_app_out ON public.pay_easy_pay_order USING btree (app_id, out_trade_no) WHERE (deleted = false);


--
-- Name: INDEX uk_easy_pay_order_app_out; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_easy_pay_order_app_out IS '同一应用下商户订单号唯一';


--
-- Name: uk_fuyou_isv_channel_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_fuyou_isv_channel_mch_no ON public.fuyou_isv_channel_merchant USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_fuyou_isv_channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_fuyou_isv_channel_mch_no IS '通道商户号唯一';


--
-- Name: uk_fuyou_isv_key_prod_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_fuyou_isv_key_prod_sandbox ON public.fuyou_isv_key_config USING btree (product, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_fuyou_isv_key_prod_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_fuyou_isv_key_prod_sandbox IS '同一产品同一环境密钥唯一';


--
-- Name: uk_fuyou_isv_mch_fuyou_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_fuyou_isv_mch_fuyou_no ON public.fuyou_isv_channel_merchant USING btree (mch_no, fuyou_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_fuyou_isv_mch_fuyou_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_fuyou_isv_mch_fuyou_no IS '同一商户下富友商户号唯一';


--
-- Name: uk_gateway_order_mch_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_gateway_order_mch_biz ON public.pay_gateway_order USING btree (mch_no, biz_order_no) WHERE (deleted = false);


--
-- Name: INDEX uk_gateway_order_mch_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_gateway_order_mch_biz IS '网关支付商户业务单号唯一约束: 同商户同 biz_order_no 仅允许一单, 防重复建单(与 pay_normal_order.uk_normal_order_mch_biz 维度一致)';


--
-- Name: uk_gateway_order_order_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_gateway_order_order_no ON public.pay_gateway_order USING btree (order_no) WHERE (deleted = false);


--
-- Name: INDEX uk_gateway_order_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_gateway_order_order_no IS '网关订单号唯一';


--
-- Name: uk_hkrt_isv_key_prod_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_hkrt_isv_key_prod_sandbox ON public.hkrt_isv_key_config USING btree (product, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_hkrt_isv_key_prod_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_hkrt_isv_key_prod_sandbox IS '同一产品同一环境密钥唯一';


--
-- Name: uk_hmpay_isv_key_prod_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_hmpay_isv_key_prod_sandbox ON public.hmpay_isv_key_config USING btree (product, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_hmpay_isv_key_prod_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_hmpay_isv_key_prod_sandbox IS '同一产品同一环境密钥唯一';


--
-- Name: uk_iam_perm_code_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_iam_perm_code_code ON public.iam_perm_code USING btree (code) WHERE (deleted = false);


--
-- Name: INDEX uk_iam_perm_code_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_iam_perm_code_code IS '权限码表编码唯一索引';


--
-- Name: INDEX uk_iam_social_config_source; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_iam_social_config_source IS '同一社交登录来源唯一';


--
-- Name: uk_iam_user_passkey_credential_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_iam_user_passkey_credential_id ON public.iam_user_passkey USING btree (credential_id) WHERE (deleted = false);


--
-- Name: INDEX uk_iam_user_passkey_credential_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_iam_user_passkey_credential_id IS '同一 WebAuthn 凭据全局唯一(只能绑定一个用户)';


--
-- Name: uk_iam_user_social_source_open_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_iam_user_social_source_open_id ON public.iam_user_social USING btree (source, open_id) WHERE (deleted = false);


--
-- Name: INDEX uk_iam_user_social_source_open_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_iam_user_social_source_open_id IS '同一第三方来源的 OpenID 唯一绑定';


--
-- Name: uk_iam_user_two_factor_user_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_iam_user_two_factor_user_id ON public.iam_user_two_factor USING btree (user_id) WHERE (deleted = false);


--
-- Name: INDEX uk_iam_user_two_factor_user_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_iam_user_two_factor_user_id IS '同一用户两步验证配置唯一';


--
-- Name: uk_lakala_isv_channel_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_lakala_isv_channel_mch_no ON public.lakala_isv_channel_merchant USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_lakala_isv_channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_lakala_isv_channel_mch_no IS '通道商户号唯一';


--
-- Name: uk_lakala_isv_key_prod_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_lakala_isv_key_prod_sandbox ON public.lakala_isv_key_config USING btree (product, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_lakala_isv_key_prod_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_lakala_isv_key_prod_sandbox IS '同一产品同一环境密钥唯一';


--
-- Name: uk_lakala_isv_key_product; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_lakala_isv_key_product ON public.lakala_isv_key_config USING btree (product) WHERE (deleted = false);


--
-- Name: INDEX uk_lakala_isv_key_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_lakala_isv_key_product IS '同一产品密钥唯一（疑似冗余，见 §6）';


--
-- Name: uk_lakala_isv_mch_lakala_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_lakala_isv_mch_lakala_no ON public.lakala_isv_channel_merchant USING btree (mch_no, lakala_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_lakala_isv_mch_lakala_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_lakala_isv_mch_lakala_no IS '同一商户下拉卡拉商户号唯一';


--
-- Name: uk_leshua_isv_channel_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_leshua_isv_channel_mch_no ON public.leshua_isv_channel_merchant USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_leshua_isv_channel_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_leshua_isv_channel_mch_no IS '通道商户号唯一';


--
-- Name: uk_leshua_isv_key_prod_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_leshua_isv_key_prod_sandbox ON public.leshua_isv_key_config USING btree (product, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_leshua_isv_key_prod_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_leshua_isv_key_prod_sandbox IS '同一产品同一环境密钥唯一';


--
-- Name: uk_leshua_isv_mch_ls_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_leshua_isv_mch_ls_no ON public.leshua_isv_channel_merchant USING btree (mch_no, ls_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_leshua_isv_mch_ls_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_leshua_isv_mch_ls_no IS '同一商户下乐刷商户号唯一';


--
-- Name: uk_mch_app_info_app_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_app_info_app_id ON public.mch_app_info USING btree (app_id);


--
-- Name: INDEX uk_mch_app_info_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_app_info_app_id IS '应用号唯一索引';


--
-- Name: uk_mch_app_info_default; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_app_info_default ON public.mch_app_info USING btree (mch_no) WHERE ((default_app = true) AND (deleted = false));


--
-- Name: INDEX uk_mch_app_info_default; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_app_info_default IS '每商户仅一个默认应用（默认应用）';


--
-- Name: uk_mch_app_notify_config; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_app_notify_config ON public.mch_app_notify_config USING btree (app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_mch_app_notify_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_app_notify_config IS '同一应用仅一条通知配置';


--
-- Name: uk_mch_notice_task_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_notice_task_biz ON public.mch_notice_task USING btree (mch_no, app_id, event, biz_no, transport, format, source) WHERE (deleted = false);


--
-- Name: INDEX uk_mch_notice_task_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_notice_task_biz IS '商户+应用+事件+业务号+传输通道+报文格式+来源维度的幂等唯一约束';


--
-- Name: uk_mch_risk_config_mch_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_risk_config_mch_no ON public.mch_risk_config USING btree (mch_no);


--
-- Name: INDEX uk_mch_risk_config_mch_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_risk_config_mch_no IS '同一商户风控配置唯一（1:1 商户）';


--
-- Name: uk_mch_store_info_default; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_store_info_default ON public.mch_store_info USING btree (mch_no) WHERE ((default_store = true) AND (deleted = false));


--
-- Name: INDEX uk_mch_store_info_default; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_store_info_default IS '每商户仅一个默认门店（默认门店）';


--
-- Name: uk_mch_store_info_store_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_store_info_store_no ON public.mch_store_info USING btree (store_no);


--
-- Name: INDEX uk_mch_store_info_store_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_store_info_store_no IS '门店号唯一';


--
-- Name: uk_mch_wx_domain_verify_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_mch_wx_domain_verify_code ON public.mch_wx_domain_verify USING btree (verify_code) WHERE (deleted = false);


--
-- Name: INDEX uk_mch_wx_domain_verify_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_mch_wx_domain_verify_code IS '域名校验码唯一';


--
-- Name: uk_normal_order_mch_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_normal_order_mch_biz ON public.pay_normal_order USING btree (mch_no, biz_order_no) WHERE (deleted = false);


--
-- Name: INDEX uk_normal_order_mch_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_normal_order_mch_biz IS '支付商户业务单号唯一约束: 同商户同 biz_order_no 仅允许一单, 防重复建单';


--
-- Name: uk_normal_order_order_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_normal_order_order_no ON public.pay_normal_order USING btree (order_no) WHERE (deleted = false);


--
-- Name: INDEX uk_normal_order_order_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_normal_order_order_no IS '普通订单号唯一';


--
-- Name: INDEX uk_notify_notice_read; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_notify_notice_read IS '同一用户同一通知已读记录唯一';


--
-- Name: uk_pay_abnormal_order_trade_pending; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_abnormal_order_trade_pending ON public.pay_abnormal_order USING btree (trade_no) WHERE ((deleted = false) AND ((handle_status)::text = 'pending'::text));


--
-- Name: INDEX uk_pay_abnormal_order_trade_pending; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_abnormal_order_trade_pending IS '同一交易仅一条待处理异常单';


--
-- Name: uk_pay_alloc_order_alloc_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_alloc_order_alloc_no ON public.pay_alloc_order USING btree (alloc_no);


--
-- Name: INDEX uk_pay_alloc_order_alloc_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_alloc_order_alloc_no IS '平台分账单号唯一约束';


--
-- Name: uk_pay_alloc_order_mch_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_alloc_order_mch_biz ON public.pay_alloc_order USING btree (mch_no, biz_alloc_no) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_alloc_order_mch_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_alloc_order_mch_biz IS '分账单商户分账单号唯一约束: 同商户同 biz_alloc_no 仅允许一单, 商户幂等键(升级原普通索引 idx_pay_alloc_order_mch_biz)';


--
-- Name: uk_pay_blacklist_type_value_app; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_blacklist_type_value_app ON public.pay_blacklist USING btree (type, value, COALESCE(wx_app_id, ''::character varying));


--
-- Name: INDEX uk_pay_blacklist_type_value_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_blacklist_type_value_app IS '类型+值+微信应用维度唯一约束（微信应用为空按空串参与唯一性）';


--
-- Name: uk_pay_fund_flow_pay_trade; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_fund_flow_pay_trade ON public.pay_fund_flow USING btree (trade_no) WHERE ((deleted = false) AND ((flow_type)::text = 'pay'::text));


--
-- Name: INDEX uk_pay_fund_flow_pay_trade; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_fund_flow_pay_trade IS '一笔支付交易仅一条收款流水';


--
-- Name: uk_pay_fund_flow_refund; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_fund_flow_refund ON public.pay_fund_flow USING btree (refund_no) WHERE ((deleted = false) AND ((flow_type)::text = 'refund'::text));


--
-- Name: INDEX uk_pay_fund_flow_refund; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_fund_flow_refund IS '一张退款单仅一条退款流水';


--
-- Name: uk_pay_gateway_pay_config_app; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_gateway_pay_config_app ON public.pay_gateway_pay_config USING btree (app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_gateway_pay_config_app; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_gateway_pay_config_app IS '同一支付网关应用唯一(部分唯一索引)';


--
-- Name: uk_pay_gateway_pay_env; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_gateway_pay_env ON public.pay_gateway_pay_client_env USING btree (config_id, client_env, pay_form) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_gateway_pay_env; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_gateway_pay_env IS '同一支付配置同客户端环境同支付形态唯一(部分唯一索引)';


--
-- Name: uk_pay_md_capability_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_md_capability_code ON public.pay_md_capability USING btree (code) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_md_capability_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_md_capability_code IS '支付能力编码唯一（未删除）';


--
-- Name: uk_pay_md_method_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_md_method_code ON public.pay_md_method USING btree (code) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_md_method_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_md_method_code IS '支付方式编码唯一（未删除）';


--
-- Name: uk_pay_md_product_capability_pair; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_md_product_capability_pair ON public.pay_md_product_capability USING btree (product_code, capability_code) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_md_product_capability_pair; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_md_product_capability_pair IS '产品+能力唯一（未删除）';


--
-- Name: uk_pay_md_product_config_product; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_md_product_config_product ON public.pay_md_product_config USING btree (product) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_md_product_config_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_md_product_config_product IS '同一产品配置唯一';


--
-- Name: uk_pay_md_provider_code; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_md_provider_code ON public.pay_md_provider USING btree (code) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_md_provider_code; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_md_provider_code IS '支付渠道编码唯一（未删除）';


--
-- Name: uk_pay_md_provider_method_pair; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_md_provider_method_pair ON public.pay_md_provider_method USING btree (provider, method) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_md_provider_method_pair; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_md_provider_method_pair IS '支付渠道+支付方式唯一（未删除）';


--
-- Name: uk_pay_platform_mobile_app_type_platform; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_platform_mobile_app_type_platform ON public.pay_platform_mobile_app USING btree (app_type, platform) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_platform_mobile_app_type_platform; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_platform_mobile_app_type_platform IS '同一应用类型+平台唯一';


--
-- Name: uk_pay_route_basic_config_provider; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_route_basic_config_provider ON public.pay_route_basic_config USING btree (strategy_id, provider) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_route_basic_config_provider; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_route_basic_config_provider IS '同一策略下渠道唯一';


--
-- Name: uk_pay_route_scene_config_method; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_route_scene_config_method ON public.pay_route_scene_config USING btree (strategy_id, method) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_route_scene_config_method; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_route_scene_config_method IS '同一策略下支付方式唯一';


--
-- Name: uk_pay_terminal_channel_bind; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_terminal_channel_bind ON public.pay_terminal_channel_bind USING btree (system_terminal_no, channel_terminal_id) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_terminal_channel_bind; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_terminal_channel_bind IS '系统终端+通道终端绑定唯一';


--
-- Name: uk_pay_terminal_device_terminal_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_terminal_device_terminal_no ON public.pay_terminal_device USING btree (terminal_no) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_terminal_device_terminal_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_terminal_device_terminal_no IS '终端号唯一';


--
-- Name: uk_pay_trade_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_trade_trade_no ON public.pay_trade USING btree (trade_no) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_trade_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_trade_trade_no IS '平台交易号唯一';


--
-- Name: uk_pay_transfer_order_alipay_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_transfer_order_alipay_no ON public.pay_transfer_order_alipay USING btree (transfer_no) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_transfer_order_alipay_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_transfer_order_alipay_no IS '平台转账单号唯一';


--
-- Name: uk_pay_transfer_order_douyin_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_transfer_order_douyin_no ON public.pay_transfer_order_douyin USING btree (transfer_no) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_transfer_order_douyin_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_transfer_order_douyin_no IS '平台转账单号唯一';


--
-- Name: uk_pay_transfer_order_wechat_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_transfer_order_wechat_no ON public.pay_transfer_order_wechat USING btree (transfer_no) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_transfer_order_wechat_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_transfer_order_wechat_no IS '平台转账单号唯一';


--
-- Name: uk_pay_transfer_trade_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_pay_transfer_trade_no ON public.pay_transfer_trade USING btree (trade_no) WHERE (deleted = false);


--
-- Name: INDEX uk_pay_transfer_trade_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_pay_transfer_trade_no IS '平台转账交易号唯一';


--
-- Name: uk_refund_order_mch_biz; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_refund_order_mch_biz ON public.pay_refund_order USING btree (mch_no, biz_refund_no) WHERE ((biz_refund_no IS NOT NULL) AND (deleted = false));


--
-- Name: INDEX uk_refund_order_mch_biz; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_refund_order_mch_biz IS '退款商户业务单号唯一约束: 同商户同 biz_refund_no 仅允许一单, 防重复退款双扣';


--
-- Name: uk_refund_order_refund_no; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_refund_order_refund_no ON public.pay_refund_order USING btree (refund_no) WHERE (deleted = false);


--
-- Name: INDEX uk_refund_order_refund_no; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_refund_order_refund_no IS '退款号唯一';


--
-- Name: uk_system_sensitive_word_word; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_system_sensitive_word_word ON public.system_sensitive_word USING btree (word) WHERE (deleted = false);


--
-- Name: INDEX uk_system_sensitive_word_word; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_system_sensitive_word_word IS '敏感词唯一';


--
-- Name: uk_ums_direct_key_cmchno; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_ums_direct_key_cmchno ON public.ums_direct_key_config USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_ums_direct_key_cmchno; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_ums_direct_key_cmchno IS '通道商户号唯一';


--
-- Name: uk_ums_direct_key_mch_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_ums_direct_key_mch_sandbox ON public.ums_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_ums_direct_key_mch_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_ums_direct_key_mch_sandbox IS '同一通道商户同一环境密钥唯一';


--
-- Name: uk_union_key_mch_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_union_key_mch_sandbox ON public.union_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_union_key_mch_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_union_key_mch_sandbox IS '同一通道商户同一环境密钥唯一';


--
-- Name: uk_user_das_pref_user_client; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_user_das_pref_user_client ON public.iam_user_dashboard_preference USING btree (user_id, client_code, terminal);


--
-- Name: INDEX uk_user_das_pref_user_client; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_user_das_pref_user_client IS '同一用户同一身份域终端同一壳仪表盘偏好唯一';


--
-- Name: uk_user_protocol_version_published; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_user_protocol_version_published ON public.base_user_protocol_version USING btree (protocol_id, language) WHERE (((status)::text = 'PUBLISHED'::text) AND (deleted = false));


--
-- Name: INDEX uk_user_protocol_version_published; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_user_protocol_version_published IS '同一协议同一语言仅一个已发布版本（已发布）';


--
-- Name: uk_vbill_isv_key_prod_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_vbill_isv_key_prod_sandbox ON public.vbill_isv_key_config USING btree (product, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_vbill_isv_key_prod_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_vbill_isv_key_prod_sandbox IS '同一产品同一环境密钥唯一';


--
-- Name: uk_wechat_direct_alloc_receiver; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_wechat_direct_alloc_receiver ON public.wechat_direct_alloc_receiver USING btree (channel_mch_no, receiver_type, account_hash) WHERE (deleted = false);


--
-- Name: INDEX uk_wechat_direct_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_wechat_direct_alloc_receiver IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';


--
-- Name: uk_wechat_isv_alloc_receiver; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_wechat_isv_alloc_receiver ON public.wechat_isv_alloc_receiver USING btree (channel_mch_no, receiver_type, account_hash) WHERE (deleted = false);


--
-- Name: INDEX uk_wechat_isv_alloc_receiver; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_wechat_isv_alloc_receiver IS '同一通道商户下同类型同账号接收方唯一(部分唯一索引)';


--
-- Name: uk_wechat_transfer_config; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_wechat_transfer_config ON public.wechat_transfer_config USING btree (channel_mch_no) WHERE (deleted = false);


--
-- Name: INDEX uk_wechat_transfer_config; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_wechat_transfer_config IS '一个通道商户仅一条转账配置(部分唯一索引)';


--
-- Name: uk_wx_channel_app_cap; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_wx_channel_app_cap ON public.wx_channel_app_capability USING btree (channel_mch_no, capability, app_scope) WHERE (deleted = false);


--
-- Name: INDEX uk_wx_channel_app_cap; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_wx_channel_app_cap IS '通道商户+能力+应用范围唯一';


--
-- Name: uk_wx_mch_app_mch_wx; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_wx_mch_app_mch_wx ON public.wx_mch_app USING btree (mch_no, wx_app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_wx_mch_app_mch_wx; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_wx_mch_app_mch_wx IS '同一商户下微信 AppID 唯一';


--
-- Name: uk_wx_platform_app_cap_product; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_wx_platform_app_cap_product ON public.wx_platform_app_capability USING btree (product, capability) WHERE (deleted = false);


--
-- Name: INDEX uk_wx_platform_app_cap_product; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_wx_platform_app_cap_product IS '产品+能力唯一';


--
-- Name: uk_wx_platform_app_wx_app_id; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_wx_platform_app_wx_app_id ON public.wx_platform_app USING btree (wx_app_id) WHERE (deleted = false);


--
-- Name: INDEX uk_wx_platform_app_wx_app_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_wx_platform_app_wx_app_id IS '微信平台 AppID 唯一';


--
-- Name: uk_yeepay_direct_key_cmchno_sandbox; Type: INDEX; Schema: public; Owner: -
--

CREATE UNIQUE INDEX uk_yeepay_direct_key_cmchno_sandbox ON public.yeepay_direct_key_config USING btree (channel_mch_no, sandbox) WHERE (deleted = false);


--
-- Name: INDEX uk_yeepay_direct_key_cmchno_sandbox; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON INDEX public.uk_yeepay_direct_key_cmchno_sandbox IS '同一通道商户同一环境密钥唯一';


--
-- PostgreSQL database dump complete
--


