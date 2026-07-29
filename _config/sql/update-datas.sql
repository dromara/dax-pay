-- 微信通道旧应用体系下线: 删除已废弃的直连/服务商应用管理菜单
-- 4040113=WechatMchAppManage(直连商户应用)、4040119=WechatIsvMchAppManage(ISV子商户应用)、40503=WechatIsvAppManage(ISV服务商应用)
-- 入口已由 WxAppHub(id=40106, menu.payment.wx.app)统一接管

DELETE FROM "public"."iam_perm_menu" WHERE "id" IN (4040113, 4040119, 40503);

-- 「移动端应用管理」菜单归属迁移: 系统管理>系统配置 → 支付管理(顶级)
-- 40504=MobileAppConfig(移动端应用管理)、40505=MobileAppDetail(移动端应用详情)
-- 原因: 该配置的 cashier(收银台小程序)凭证被支付核心 CashierAuthService 直接依赖, 属支付链路前置配置;
--       表 pay_platform_mobile_app 前缀亦属支付域。pid 由 304(系统配置) 改为 4(支付管理)。
-- 注: 405(支付配置 catalog) 在运营端已软删(deleted=t), 故直接挂顶级 4 下, 与"支付产品配置(40105)"平级。

-- 40504 主菜单: pid 304→4, menu_code/i18n_key/component/path 支付域化, sort_no 调整到支付产品配置之后
UPDATE "public"."iam_perm_menu" SET
  "pid" = 4,
  "menu_code" = 'payment:config:mobile-app',
  "i18n_key" = 'menu.payment.config.mobileApp',
  "component" = '/payment/config/mobile-app/MobileAppConfig',
  "path" = '/payment/config/mobile-app',
  "sort_no" = 2.7
WHERE "id" = 40504;

-- 40505 详情子页: menu_code/i18n_key/component/path 支付域化 (pid 仍挂 40504)
UPDATE "public"."iam_perm_menu" SET
  "menu_code" = 'payment:config:mobile-app-detail',
  "i18n_key" = 'menu.payment.config.mobileAppDetail',
  "component" = '/payment/config/mobile-app/detail/MobileAppDetail',
  "path" = '/payment/config/mobile-app/detail/:appType'
WHERE "id" = 40505;
