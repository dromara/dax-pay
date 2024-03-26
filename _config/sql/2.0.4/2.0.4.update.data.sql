SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1772446740356460544, 'CockpitReportController#getRefundChannelInfo', '显示通道退款订单金额和订单数', 'GET', '/report/cockpit/getRefundChannelInfo', '驾驶舱接口', b'1', b'1', '驾驶舱接口 显示通道退款订单金额和订单数', 1399985191002447872, '2024-03-26 10:13:28.025000', 1399985191002447872, '2024-03-26 10:13:28.025000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1772446740356460545, 'CockpitReportController#getRefundAmount', '退款金额(分)', 'GET', '/report/cockpit/getRefundAmount', '驾驶舱接口', b'1', b'1', '驾驶舱接口 退款金额(分)', 1399985191002447872, '2024-03-26 10:13:28.025000', 1399985191002447872, '2024-03-26 10:13:28.025000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1772446740356460546, 'CockpitReportController#getRefundOrderCount', '退款订单数量', 'GET', '/report/cockpit/getRefundOrderCount', '驾驶舱接口', b'1', b'1', '驾驶舱接口 退款订单数量', 1399985191002447872, '2024-03-26 10:13:28.025000', 1399985191002447872, '2024-03-26 10:13:28.025000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1772446740356460547, 'CockpitReportController#getPayChannelInfo', '显示通道支付订单金额和订单数', 'GET', '/report/cockpit/getPayChannelInfo', '驾驶舱接口', b'1', b'1', '驾驶舱接口 显示通道支付订单金额和订单数', 1399985191002447872, '2024-03-26 10:13:28.025000', 1399985191002447872, '2024-03-26 10:13:28.025000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1772446740356460548, 'ReconcileOrderController#upload', '手动上传对账单文件', 'POST', '/order/reconcile/upload', '对账控制器', b'1', b'1', '对账控制器 手动上传对账单文件', 1399985191002447872, '2024-03-26 10:13:28.025000', 1399985191002447872, '2024-03-26 10:13:28.025000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1772446740356460549, 'CockpitReportController#getPayAmount', '支付金额(分)', 'GET', '/report/cockpit/getPayAmount', '驾驶舱接口', b'1', b'1', '驾驶舱接口 支付金额(分)', 1399985191002447872, '2024-03-26 10:13:28.025000', 1399985191002447872, '2024-03-26 10:13:28.025000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1772446740356460550, 'CockpitReportController#getPayOrderCount', '支付订单数量', 'GET', '/report/cockpit/getPayOrderCount', '驾驶舱接口', b'1', b'1', '驾驶舱接口 支付订单数量', 1399985191002447872, '2024-03-26 10:13:28.025000', 1399985191002447872, '2024-03-26 10:13:28.025000', b'0', 0);

DELETE FROM `iam_role_path` WHERE `id` = 1757299898825560083;

DELETE FROM `iam_role_path` WHERE `id` = 1757299898825560084;

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571904, 1757297023118462976, 1772446740356460550);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571905, 1757297023118462976, 1772446740356460549);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571906, 1757297023118462976, 1772446740356460547);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571907, 1757297023118462976, 1772446740356460546);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571908, 1757297023118462976, 1772446740356460545);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571909, 1757297023118462976, 1772446740356460544);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571910, 1757297023118462976, 1772446740356460548);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571911, 1757297023118462976, 1768219994979528711);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571912, 1757297023118462976, 1768219994979528710);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571913, 1757297023118462976, 1768219994979528704);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446949400571914, 1757297023118462976, 1768219994979528707);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234688, 1757298887092326400, 1772446740356460550);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234689, 1757298887092326400, 1772446740356460549);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234690, 1757298887092326400, 1772446740356460547);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234691, 1757298887092326400, 1772446740356460546);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234692, 1757298887092326400, 1772446740356460545);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234693, 1757298887092326400, 1772446740356460544);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234694, 1757298887092326400, 1772446740356460548);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234695, 1757298887092326400, 1768219994979528711);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234696, 1757298887092326400, 1768219994979528710);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772446952005234697, 1757298887092326400, 1768219994979528704);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772447125519396864, 1757298887092326400, 1764663810424303620);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772447125519396865, 1757298887092326400, 1764663810424303619);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772447125519396866, 1757298887092326400, 1764663810424303618);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772447125519396867, 1757298887092326400, 1764663810424303617);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1772447125519396868, 1757298887092326400, 1764663810424303616);

UPDATE `pay_alipay_config` SET `app_id` = '123456', `enable` = b'1', `notify_url` = 'ServerUrl/callback/pay/alipay', `return_url` = 'ServerUrl/return/pay/alipay', `server_url` = 'https://openapi.alipay.com/gateway.do', `auth_type` = 'key', `sign_type` = 'RSA2', `alipay_public_key` = 'ImfODRp7hnJ3DUk9fCes0Q==', `private_key` = 'ImfODRp7hnJ3DUk9fCes0Q==', `app_cert` = NULL, `alipay_cert` = NULL, `alipay_root_cert` = NULL, `sandbox` = b'0', `pay_ways` = 'wap,app,web,qrcode,barcode', `remark` = '支付宝支付', `creator` = 0, `create_time` = '2024-01-02 21:17:58', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-13 15:43:51', `version` = 23, `deleted` = b'0', `single_limit` = NULL, `alipay_user_id` = NULL WHERE `id` = 0;

UPDATE `pay_platform_config` SET `website_url` = 'http://127.0.0.1', `sign_type` = 'HMAC_SHA256', `sign_secret` = '132456', `notify_url` = 'http://127.0.0.1/h5/#/result/success', `return_url` = 'http://127.0.0.1/h5/#/result/success', `order_timeout` = 30, `creator` = 0, `create_time` = '2024-01-02 20:23:19', `last_modifier` = 1757299137932677120, `last_modified_time` = '2024-02-13 15:08:51', `version` = 3, `deleted` = b'0', `limit_amount` = NULL WHERE `id` = 0;

UPDATE `pay_union_pay_config` SET `mach_id` = '123456', `enable` = b'1', `server_url` = 'https://qra.95516.com/pay/gateway', `notify_url` = 'ServerUrl/callback/pay/union', `pay_ways` = 'wap,app,web,qrcode,barcode,jsapi,b2b', `remark` = NULL, `seller` = NULL, `sign_type` = 'RSA2', `cert_sign` = b'0', `key_private_cert` = NULL, `key_private_cert_pwd` = NULL, `acp_middle_cert` = NULL, `acp_root_cert` = NULL, `sandbox` = b'1', `return_url` = 'ServerUrl/return/pay/union', `creator` = 0, `create_time` = '2024-03-06 22:56:22', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-12 23:30:18', `version` = 11, `deleted` = b'0', `single_limit` = NULL WHERE `id` = 0;

UPDATE `pay_wechat_pay_config` SET `wx_mch_id` = '123', `wx_app_id` = '123', `enable` = b'1', `notify_url` = 'ServerUrl/callback/pay/wechat', `return_url` = 'ServerUrl/pay/wechat', `api_version` = '0', `api_key_v2` = NULL, `api_key_v3` = 'E0jIzPNngkpkZYL19H3vFQ==', `app_secret` = 'E0jIzPNngkpkZYL19H3vFQ==', `p12` = 'E0jIzPNngkpkZYL19H3vFQ==', `sandbox` = NULL, `pay_ways` = '0', `remark` = 'wap,app,jsapi,qrcode,barcode', `creator` = 0, `create_time` = '0000-00-00 00:00:00', `last_modifier` = 2024, `last_modified_time` = '0000-00-00 00:00:00', `version` = 2024, `deleted` = b'1', `single_limit` = NULL WHERE `id` = 0;

SET FOREIGN_KEY_CHECKS = 1;
