-- 商户应用事件通知配置: 菜单(权限码 merchant:notify_config)
INSERT INTO "public"."iam_perm_menu" VALUES (4040118, 40401, 'merchant:notify_config', 'admin', 'MchAppNotifyConfig', '通知配置', 'Notify Config', 'menu.payment.merchant.app.notifyConfig', NULL, 't', 'f', '/payment/merchant/app/MchAppNotifyConfig', '/payment/merchant/app/notify-config', NULL, 11, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-02 16:00:00+00', '2026-07-02 16:00:00+00');

-- 退款订单: 菜单(权限码 payment:refund)
INSERT INTO "public"."iam_perm_menu" VALUES (603, 6, 'payment:refund', 'admin', 'RefundOrderList', '退款订单', 'Refund Orders', 'menu.trade.refundOrder', 'lucide:rotate-ccw', 'f', 'f', '/payment/order/RefundOrderList', '/trade/refund-order', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-03 16:00:00+00', '2026-07-03 16:00:00+00');

-- 微信服务商通道商户应用(子商户应用): 菜单(权限码 channel:wechat:app)
INSERT INTO "public"."iam_perm_menu" VALUES (4040119, 40401, 'channel:wechat:app', 'admin', 'WechatIsvMchAppManage', '微信服务商通道商户应用', 'WeChat ISV Channel Merchant App', 'menu.payment.merchant.channelMerchant.wechatIsvMchApp', NULL, 't', 'f', '/payment/channel/wechat/manage/mch/isv-app/WechatIsvMchAppManage', '/payment/merchant/channel-merchant/wechat-isv-mch-app-manage', NULL, 14, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-05 00:00:00+00');

-- ============================================================
-- 菜单标题与 i18n_key 统一重命名
-- ============================================================

-- 支付产品详情 → 产品配置详情
UPDATE "public"."iam_perm_menu" SET title_cn='产品配置详情', title_en='Product Config Detail',
  i18n_key='menu.payment.config.detail' WHERE id=40501;
-- 支付宝服务商应用: i18n_key 归入 config.*
UPDATE "public"."iam_perm_menu" SET i18n_key='menu.payment.config.alipayApp' WHERE id=40502;
-- 微信服务商应用: i18n_key 归入 config.*
UPDATE "public"."iam_perm_menu" SET i18n_key='menu.payment.config.wechatApp' WHERE id=40503;

-- 商户信息(列表入口) → 商户列表
UPDATE "public"."iam_perm_menu" SET title_cn='商户列表', title_en='Merchant List',
  i18n_key='menu.payment.merchant.list' WHERE id=40401;
-- 商户管理(工作台) → 商户工作台
UPDATE "public"."iam_perm_menu" SET title_cn='商户工作台', title_en='Merchant Workbench',
  i18n_key='menu.payment.merchant.workbench' WHERE id=4040101;
-- 对接配置: i18n_key 去掉中间 manage 层
UPDATE "public"."iam_perm_menu" SET i18n_key='menu.payment.merchant.credential' WHERE id=4040102;
-- 商户信息(详情) → 商户资料
UPDATE "public"."iam_perm_menu" SET title_cn='商户资料', title_en='Merchant Profile',
  i18n_key='menu.payment.merchant.profile' WHERE id=4040103;
-- 通道路由: i18n_key 从 app.* 移出(实为商户级配置)
UPDATE "public"."iam_perm_menu" SET i18n_key='menu.payment.merchant.payRoute' WHERE id=4040111;
-- 通道商户详情 → 通道商户管理(静态通用名)
UPDATE "public"."iam_perm_menu" SET title_cn='通道商户管理', title_en='Channel Merchant Management'
  WHERE id=4040112;
-- 微信通道商户应用 → 微信应用
UPDATE "public"."iam_perm_menu" SET title_cn='微信应用', title_en='WeChat App' WHERE id=4040113;
-- 支付宝通道商户应用 → 支付宝应用
UPDATE "public"."iam_perm_menu" SET title_cn='支付宝应用', title_en='Alipay App' WHERE id=4040114;
-- 抖音通道商户应用 → 抖音应用
UPDATE "public"."iam_perm_menu" SET title_cn='抖音应用', title_en='Douyin App' WHERE id=4040115;
-- 微信服务商通道商户应用 → 微信服务商子商户应用
UPDATE "public"."iam_perm_menu" SET title_cn='微信服务商子商户应用', title_en='WeChat ISV Sub-merchant App',
  i18n_key='menu.payment.merchant.channelMerchant.wechatIsvApp' WHERE id=4040119;

-- 移动端应用管理(平台级, 商户端/管理端/收银台应用配置)
INSERT INTO "public"."iam_perm_menu" VALUES (40504, 405, 'payment:config:mobile_app', 'admin', 'MobileAppConfig', '移动端应用管理', 'Mobile App Management', 'menu.payment.config.mobileApp', 'lucide:smartphone', 'f', 'f', '/payment/config/mobileApp/MobileAppConfig', '/payment/config/mobile-app', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-05 00:00:00+00');
-- 移动端应用详情(按端类型配置各平台, 隐藏子页)
INSERT INTO "public"."iam_perm_menu" VALUES (40505, 40504, 'payment:config:mobile_app_detail', 'admin', 'MobileAppDetail', '移动端应用详情', 'Mobile App Detail', 'menu.payment.config.mobileAppDetail', NULL, 't', 'f', '/payment/config/mobileApp/detail/MobileAppDetail', '/payment/config/mobile-app/detail/:appType', NULL, 1, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-05 00:00:00+00', '2026-07-05 00:00:00+00');
