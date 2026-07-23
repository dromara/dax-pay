-- 商户端侧栏菜单种子（client_code=merchant）
-- 对齐「工作台能力菜单化」：仪表板 / 交易管理 / 交易记录 / 商户中心 / 支付配置 / 业务配置
-- VALUES 列序与 iam_perm_menu 表一致（含 DB 列 name/root/active_icon）

-- ========== 仪表板 91001-91003 ==========
INSERT INTO "public"."iam_perm_menu" VALUES
  (91001, NULL, NULL, 'merchant', 'Dashboard', 'menu.dashboard', 'lucide:layout-dashboard',
   'f','f', NULL, '/dashboard', '/workspace', -1, 'f','f','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91002, 91001, 'dashboard:workspace', 'merchant', 'Workspace', 'menu.dashboard.workspace',
   'lucide:panels-top-left', 'f','f', '/dashboard/workspace/index', '/workspace', NULL, 1,
   'f','f','t', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91003, 91001, 'dashboard:analytics', 'merchant', 'Analytics', 'menu.dashboard.analytics',
   'lucide:area-chart', 'f','f', '/dashboard/analytics/index', '/analytics', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- ========== 交易管理 91100-91112 ==========
INSERT INTO "public"."iam_perm_menu" VALUES
  (91100, NULL, 'trade', 'merchant', 'TradeManagement', 'menu.trade', 'lucide:arrow-left-right',
   'f','f', NULL, '/trade', '/trade/pay-trade', 2, 'f','t','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91101, 91100, 'trade:fund', 'merchant', 'PayTradeList', 'menu.trade.payTrade',
   'lucide:circle-dollar-sign', 'f','f', '/payment/order/PayTradeList', '/trade/pay-trade', NULL, 1,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91102, 91100, 'trade:refund', 'merchant', 'RefundOrderList', 'menu.trade.refundOrder',
   'lucide:undo-2', 'f','f', '/payment/order/RefundOrderList', '/trade/refund-order', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91110, 91100, 'trade:pay-order', 'merchant', 'PayOrderCatalog', 'menu.trade.payOrder',
   'lucide:receipt', 'f','f', NULL, '/trade/pay-order', '/trade/pay-order/normal', 3,
   'f','f','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91111, 91110, 'trade:order', 'merchant', 'NormalOrderList', 'menu.trade.normalPay',
   'lucide:file-text', 'f','f', '/payment/order/NormalOrderList', '/trade/pay-order/normal', NULL, 1,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91112, 91110, 'trade:gateway-order', 'merchant', 'GatewayOrderList', 'menu.trade.gatewayOrder',
   'lucide:globe', 'f','f', '/payment/order/GatewayOrderList', '/trade/pay-order/gateway', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- ========== 交易记录（一级 catalog）91200-91202 ==========
INSERT INTO "public"."iam_perm_menu" VALUES
  (91200, NULL, 'trade:record', 'merchant', 'TradeRecordCatalog', 'menu.trade.record',
   'lucide:scroll-text', 'f','f', NULL, '/trade/record', '/trade/record/mch-notice', 3,
   'f','t','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91201, 91200, 'trade:mch-notice', 'merchant', 'MchNoticeTaskList', 'menu.trade.mchNotice',
   'lucide:bell-ring', 'f','f', '/payment/notice/MchNoticeTaskList', '/trade/record/mch-notice', NULL, 1,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91202, 91200, 'trade:callback-record', 'merchant', 'PayCallbackRecordList', 'menu.trade.callbackRecord',
   'lucide:webhook', 'f','f', '/payment/record/PayCallbackRecordList', '/trade/record/callback-record', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- ========== 商户中心 91300-91304 ==========
INSERT INTO "public"."iam_perm_menu" VALUES
  (91300, NULL, NULL, 'merchant', 'MchCenter', 'menu.mch.center', 'lucide:building-2',
   'f','f', NULL, '/mch-center', '/mch/info', 4, 'f','t','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91301, 91300, 'merchant:profile', 'merchant', 'MchInfoManage', 'menu.payment.merchant.profile',
   'lucide:badge-info', 'f','f', '/payment/merchant/info/MchInfoManage', '/mch/info', NULL, 1,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91302, 91300, 'merchant:user', 'merchant', 'MerchantUserList', 'menu.payment.merchant.user',
   'lucide:users', 'f','f', '/payment/merchant/user/MerchantUserList', '/mch/user', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91303, 91300, 'merchant:store', 'merchant', 'MchStoreInfoList', 'menu.payment.merchant.store',
   'lucide:store', 'f','f', '/payment/merchant/store/MchStoreInfoList', '/mch/store', NULL, 3,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91304, 91300, 'merchant:terminal', 'merchant', 'SystemTerminalList', 'menu.payment.merchant.terminal',
   'lucide:monitor', 'f','f', '/payment/device/terminal/system/SystemTerminalList', '/mch/terminal', NULL, 4,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- ========== 支付配置 91400-91411 ==========
INSERT INTO "public"."iam_perm_menu" VALUES
  (91400, NULL, NULL, 'merchant', 'PaymentConfig', 'menu.payment.config', 'lucide:settings-2',
   'f','f', NULL, '/mch-payment', '/mch/app', 5, 'f','t','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91401, 91400, 'merchant:app', 'merchant', 'MchAppInfoList', 'menu.payment.merchant.app',
   'lucide:app-window', 'f','f', '/payment/merchant/app/MchAppInfoList', '/mch/app', NULL, 1,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91402, 91400, 'channel:merchant', 'merchant', 'ChannelMerchantList', 'menu.payment.merchant.channelMerchant',
   'lucide:store', 'f','f', '/payment/channel-merchant/ChannelMerchantList', '/mch/channel-merchant', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91410, 91402, 'channel:merchant:create', 'merchant', 'ChannelMerchantCreate', 'menu.payment.merchant.channelMerchant.create',
   NULL, 't','f', '/payment/channel-merchant/ChannelMerchantCreate', '/mch/channel-merchant/create', NULL, 1,
   'f','f','f', 0,1,1,'f','subpage', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91411, 91402, 'channel:merchant:detail', 'merchant', 'ChannelMerchantDetail', 'menu.payment.merchant.channelMerchant.detail',
   NULL, 't','f', '/payment/channel-merchant/detail/ChannelMerchantDetailDispatch', '/mch/channel-merchant/detail', NULL, 2,
   'f','f','f', 0,1,1,'f','subpage', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- 应用配置 directory（无独立页面）
INSERT INTO "public"."iam_perm_menu" VALUES
  (91403, 91400, NULL, 'merchant', 'AppConfigDir', 'menu.payment.merchant.appWorkbench',
   'lucide:sliders-horizontal', 'f','f', NULL, '/mch/app-config', NULL, 3,
   'f','f','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91404, 91403, 'merchant:app:route', 'merchant', 'PayRouteConfig', 'menu.payment.merchant.payRoute',
   'lucide:git-branch', 'f','f', '/payment/route/PayRouteConfig', '/mch/route', NULL, 1,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91405, 91403, 'merchant:gateway-aggregate', 'merchant', 'AggregateScanConfig', 'menu.payment.merchant.aggregateScan',
   'lucide:qr-code', 'f','f', '/payment/merchant/aggregate/AggregateScanConfig', '/mch/aggregate', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91406, 91403, 'merchant:gateway-code', 'merchant', 'CodePayConfig', 'menu.payment.merchant.codePayConfig',
   'lucide:smartphone', 'f','f', '/payment/merchant/code-config/CodePayConfig', '/mch/code-config', NULL, 3,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91407, 91403, 'merchant:gateway-cashier', 'merchant', 'CashierConfig', 'menu.payment.merchant.cashierConfig',
   'lucide:monitor-smartphone', 'f','f', '/payment/merchant/cashier/CashierConfig', '/mch/cashier', NULL, 4,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91408, 91403, 'merchant:notify-config', 'merchant', 'MchAppNotifyConfig', 'menu.payment.merchant.notifyConfig',
   'lucide:bell', 'f','f', '/payment/merchant/notify/MchAppNotifyConfigPage', '/mch/notify-config', NULL, 5,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91409, 91403, 'merchant:easypay', 'merchant', 'EasyPayConfig', 'menu.payment.merchant.easypay',
   'lucide:plug', 'f','f', '/payment/merchant/easypay/EasyPayConfig', '/mch/easypay', NULL, 6,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- ========== 业务配置 91500-91502 ==========
INSERT INTO "public"."iam_perm_menu" VALUES
  (91500, NULL, NULL, 'merchant', 'BusinessConfig', 'menu.business.config', 'lucide:key-round',
   'f','f', NULL, '/mch-business', '/mch/credential', 6, 'f','t','f', 0, NULL, 0, 'f', 'catalog',
   NULL,NULL,NULL,NULL, NULL,NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91501, 91500, 'merchant:credential', 'merchant', 'MerchantCredentialConfig', 'menu.payment.merchant.credential',
   'lucide:key', 'f','f', '/payment/merchant/credential/MerchantCredentialConfig', '/mch/credential', NULL, 1,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
  (91502, 91500, 'merchant:wx-verify', 'merchant', 'MchWxDomainVerifyList', 'menu.payment.merchant.wxVerify',
   'lucide:shield-check', 'f','f', '/payment/merchant/wx-verify/MchWxDomainVerifyList', '/mch/wx-verify', NULL, 2,
   'f','f','f', 0,1,1,'f','menu', NULL,NULL,NULL,NULL, '','', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- 挂到所有商户端角色（避免重复：仅插入尚不存在的 role_id+menu_id）
INSERT INTO "public"."iam_role_menu" ("id", "role_id", "menu_id")
SELECT
  920000000000 + row_number() OVER (ORDER BY r."id", m."id"),
  r."id",
  m."id"
FROM "public"."iam_role" r
CROSS JOIN (
  VALUES
    (91001),(91002),(91003),
    (91100),(91101),(91102),(91110),(91111),(91112),
    (91200),(91201),(91202),
    (91300),(91301),(91302),(91303),(91304),
    (91400),(91401),(91402),(91403),(91404),(91405),(91406),(91407),(91408),(91409),(91410),(91411),
    (91500),(91501),(91502)
) AS m("id")
WHERE r."client_code" = 'merchant'
  AND NOT EXISTS (
    SELECT 1
    FROM "public"."iam_role_menu" rm
    WHERE rm."role_id" = r."id"
      AND rm."menu_id" = m."id"
  );
