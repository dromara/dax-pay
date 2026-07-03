-- 商户应用事件通知配置: 菜单(权限码 merchant:notify_config)
INSERT INTO "public"."iam_perm_menu" VALUES (4040118, 40401, 'merchant:notify_config', 'admin', 'MchAppNotifyConfig', '通知配置', 'Notify Config', 'menu.payment.merchant.app.notifyConfig', NULL, 't', 'f', '/payment/merchant/app/MchAppNotifyConfig', '/payment/merchant/app/notify-config', NULL, 11, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-02 16:00:00+00', '2026-07-02 16:00:00+00');

-- 退款订单: 菜单(权限码 payment:refund)
INSERT INTO "public"."iam_perm_menu" VALUES (603, 6, 'payment:refund', 'admin', 'RefundOrderList', '退款订单', 'Refund Orders', 'menu.trade.refundOrder', 'lucide:rotate-ccw', 'f', 'f', '/payment/order/RefundOrderList', '/trade/refund-order', NULL, 3, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-07-03 16:00:00+00', '2026-07-03 16:00:00+00');
