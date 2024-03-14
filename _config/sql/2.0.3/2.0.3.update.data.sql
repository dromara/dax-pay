SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1766713545981153280, 'UnionPaySignType', '云闪付签名类型', b'1', '支付', '', 1399985191002447872, '2024-03-10 14:31:48', 1399985191002447872, '2024-03-10 14:31:48', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768206399071809536, 'UnionPayRecordType', '云闪付流水类型', b'1', '支付', '', 1399985191002447872, '2024-03-14 17:23:52', 1399985191002447872, '2024-03-14 17:23:52', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1766713657021157376, 1766713545981153280, 'UnionPaySignType', 'RSA2', 'RSA2', b'1', 0.00, '', 1399985191002447872, '2024-03-10 14:32:14', 1399985191002447872, '2024-03-10 14:32:14', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768206502721449984, 1768206399071809536, 'UnionPayRecordType', 'pay', '支付', b'1', 1.00, '', 1399985191002447872, '2024-03-14 17:24:17', 1399985191002447872, '2024-03-14 17:24:17', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768206547285929984, 1768206399071809536, 'UnionPayRecordType', 'refund', '退款', b'1', 0.00, '', 1399985191002447872, '2024-03-14 17:24:27', 1399985191002447872, '2024-03-14 17:24:27', 0, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1768203432981655552, 'dax-pay', 1758860876272861184, '云闪付流水', 'UnionPayRecordList', NULL, b'0', '', b'0', b'0', 'payment/channel/union/record/UnionPayRecordList', NULL, '/pay/channel/union', '', -1, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-03-14 17:12:05', 1399985191002447872, '2024-03-14 17:12:05', 0, 0);

UPDATE `iam_perm_menu` SET `client_code` = 'dax-pay', `parent_id` = 1758860876272861184, `title` = '钱包管理', `name` = 'WalletList', `perm_code` = NULL, `effect` = b'0', `icon` = '', `hidden` = b'0', `hide_children_in_menu` = b'0', `component` = 'payment/channel/wallet/manager/WalletList', `component_name` = NULL, `path` = '/pay/channel/wallet', `redirect` = '', `sort_no` = -1, `menu_type` = 1, `leaf` = NULL, `keep_alive` = b'1', `target_outside` = b'0', `hidden_header_content` = b'0', `admin` = b'0', `remark` = NULL, `creator` = 1399985191002447872, `create_time` = '2024-02-17 22:29:06', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-05 22:09:12', `version` = 2, `deleted` = 0 WHERE `id` = 1758861129311027200;

UPDATE `iam_perm_menu` SET `client_code` = 'dax-pay', `parent_id` = 1758860876272861184, `title` = '储值卡管理', `name` = 'VoucherList', `perm_code` = NULL, `effect` = b'0', `icon` = '', `hidden` = b'0', `hide_children_in_menu` = b'0', `component` = 'payment/channel/voucher/manager/VoucherList', `component_name` = NULL, `path` = '/pay/channel/voucher', `redirect` = '', `sort_no` = -1, `menu_type` = 1, `leaf` = NULL, `keep_alive` = b'1', `target_outside` = b'0', `hidden_header_content` = b'0', `admin` = b'0', `remark` = NULL, `creator` = 1399985191002447872, `create_time` = '2024-02-18 20:24:49', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-05 22:09:30', `version` = 2, `deleted` = 0 WHERE `id` = 1759192238594949120;

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994971140096, 'RefundOrderController#resetRefund', '重新发起退款', 'POST', '/order/refund/resetRefund', '支付退款控制器', b'1', b'1', '支付退款控制器 重新发起退款', 1399985191002447872, '2024-03-14 18:17:53.396000', 1399985191002447872, '2024-03-14 18:17:53.397000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528704, 'UnionPayConfigController#getConfig', '获取配置', 'GET', '/union/pay/config/getConfig', '云闪付配置', b'1', b'1', '云闪付配置 获取配置', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528705, 'UnionPayController#findById', '查询记录详情', 'GET', '/union/pay/record/findById', '云闪付控制器', b'1', b'1', '云闪付控制器 查询记录详情', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528706, 'UnionPayController#recordPage', '记录分页', 'GET', '/union/pay/record/page', '云闪付控制器', b'1', b'1', '云闪付控制器 记录分页', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528707, 'UnionPayConfigController#update', '更新', 'POST', '/union/pay/config/update', '云闪付配置', b'1', b'1', '云闪付配置 更新', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528708, 'PayReturnController#union', '云闪付同步通知', 'POST', '/return/pay/union', '支付同步通知', b'1', b'1', '支付同步通知 云闪付同步通知', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528709, 'PayCallbackController#unionPayNotify', '云闪付支付信息回调', 'POST', '/callback/pay/union', '支付通道信息回调', b'1', b'1', '支付通道信息回调 云闪付支付信息回调', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528710, 'UnionPayConfigController#toBase64', '读取证书文件内容', 'POST', '/union/pay/config/toBase64', '云闪付配置', b'1', b'1', '云闪付配置 读取证书文件内容', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1768219994979528711, 'UnionPayConfigController#findPayWays', '支持的支付方式', 'GET', '/union/pay/config/findPayWays', '云闪付配置', b'1', b'1', '云闪付配置 支持的支付方式', 1399985191002447872, '2024-03-14 18:17:53.398000', 1399985191002447872, '2024-03-14 18:17:53.398000', b'0', 0);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1768220067952029696, 1757297023118462976, 'dax-pay', 1768203432981655552);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1768220068631506944, 1757298887092326400, 'dax-pay', 1768203432981655552);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1768220144112201728, 1757297023118462976, 1768219994971140096);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1768220144112201729, 1757297023118462976, 1768219994979528705);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1768220144112201730, 1757297023118462976, 1768219994979528706);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1768220144611323904, 1757298887092326400, 1768219994971140096);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1768220144611323905, 1757298887092326400, 1768219994979528705);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1768220144611323906, 1757298887092326400, 1768219994979528706);

UPDATE `pay_channel_config` SET `code` = 'union_pay', `name` = '云闪付', `icon_id` = NULL, `bg_color` = NULL, `enable` = b'1', `remark` = NULL, `creator` = 0, `create_time` = '2024-01-08 16:47:07', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-10 15:04:36', `version` = 2, `deleted` = b'0' WHERE `id` = 3;

INSERT INTO `pay_union_pay_config`(`id`, `mach_id`, `enable`, `server_url`, `notify_url`, `pay_ways`, `remark`, `seller`, `sign_type`, `cert_sign`, `key_private_cert`, `key_private_cert_pwd`, `acp_middle_cert`, `acp_root_cert`, `sandbox`, `return_url`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (0, '123456', b'1', 'https://qra.95516.com/pay/gateway', 'ServerUrl/callback/pay/union', 'wap,app,web,qrcode,barcode,jsapi,b2b', NULL, NULL, 'RSA2', b'0', NULL, NULL, NULL, NULL, b'1', 'ServerUrl/return/pay/union', 0, '2024-03-06 22:56:22', 1399985191002447872, '2024-03-12 23:30:18', 11, b'0');

INSERT INTO `starter_quartz_job`(`id`, `name`, `job_class_name`, `cron`, `parameter`, `state`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1767553847839141888, '退款订单定时同步', 'cn.bootx.platform.daxpay.service.task.RefundSyncTask', '0 * * * * ? *', '', 1, '', 1399985191002447872, '2024-03-12 22:10:52', 1399985191002447872, '2024-03-12 22:10:59', 1, 0);

UPDATE `starter_quartz_job` SET `name` = '客户系统通知重发任务', `job_class_name` = 'cn.bootx.platform.daxpay.service.task.ClientNoticeSendTask', `cron` = '0/1 * * * * ? *', `parameter` = '', `state` = 1, `remark` = '每秒调用一下当前需要进行通知的任务', `creator` = 1399985191002447872, `create_time` = '2024-02-24 23:55:07', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-05 23:05:09', `version` = 2, `deleted` = 0 WHERE `id` = 1761419490908958720;

UPDATE `starter_quartz_job` SET `name` = '支付宝定时对账', `job_class_name` = 'cn.bootx.platform.daxpay.service.task.ReconcileTask', `cron` = '0 0 11 * * ? *', `parameter` = '{\"channel\":\"ali_pay\",\"n\":1}', `state` = 1, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-03-04 22:49:50', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-10 11:19:17', `version` = 3, `deleted` = 0 WHERE `id` = 1764664552203743232;

UPDATE `starter_quartz_job` SET `name` = '微信支付定时对账', `job_class_name` = 'cn.bootx.platform.daxpay.service.task.ReconcileTask', `cron` = '0 0 11 * * ? *', `parameter` = '{\"channel\":\"wechat_pay\",\"n\":1}', `state` = 1, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-03-04 23:01:06', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-10 11:19:14', `version` = 3, `deleted` = 0 WHERE `id` = 1764667388106887168;

SET FOREIGN_KEY_CHECKS = 1;
