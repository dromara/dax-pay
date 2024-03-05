SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1763588034467713024, 'ReconcileDiffType', '对账差异类型', b'1', '支付', '', 1399985191002447872, '2024-03-01 23:32:08', 1399985191002447872, '2024-03-01 23:32:08', 0, 0);

UPDATE `base_dict` SET `code` = 'ReconcileTrade', `name` = '支付对账交易类型', `enable` = b'1', `group_tag` = '支付', `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-01-23 09:59:00', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-01 23:31:32', `deleted` = 0, `version` = 1 WHERE `id` = 1749612665392541696;

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1763588081838182400, 1763588034467713024, 'ReconcileDiffType', 'local_not_exists', '本地订单不存在', b'1', 0.00, '', 1399985191002447872, '2024-03-01 23:32:19', 1399985191002447872, '2024-03-01 23:32:19', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1763588123143688192, 1763588034467713024, 'ReconcileDiffType', 'remote_not_exists', '远程订单不存在', b'1', 1.00, '', 1399985191002447872, '2024-03-01 23:32:29', 1399985191002447872, '2024-03-01 23:32:29', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1763588174695878656, 1763588034467713024, 'ReconcileDiffType', 'not_match', '订单信息不一致', b'1', 2.00, '', 1399985191002447872, '2024-03-01 23:32:41', 1399985191002447872, '2024-03-01 23:32:48', 0, 1);

UPDATE `base_dict_item` SET `dict_id` = 1749612665392541696, `dict_code` = 'ReconcileTrade', `code` = 'pay', `name` = '支付', `enable` = b'1', `sort_no` = 1.00, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-01-23 09:59:11', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-01-23 09:59:11', `deleted` = 0, `version` = 0 WHERE `id` = 1749612708363186176;

UPDATE `base_dict_item` SET `dict_id` = 1749612665392541696, `dict_code` = 'ReconcileTrade', `code` = 'refund', `name` = '退款', `enable` = b'1', `sort_no` = 2.00, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-01-23 09:59:23', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-01-23 09:59:23', `deleted` = 0, `version` = 0 WHERE `id` = 1749612758531256320;

UPDATE `base_dict_item` SET `dict_id` = 1749612665392541696, `dict_code` = 'ReconcileTrade', `code` = 'revoked', `name` = '撤销', `enable` = b'1', `sort_no` = 3.00, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-01-23 09:59:32', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-01-23 09:59:32', `deleted` = 0, `version` = 0 WHERE `id` = 1749612797680889856;

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1764638353289027584, 'dax-pay', 1749262518385082368, '对账单', 'ReconcileOrderList', NULL, b'0', '', b'0', b'0', 'payment/order/reconcile/order/ReconcileOrderList.vue', NULL, '/pay/order/reconcile/list', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-03-04 21:05:43', 1399985191002447872, '2024-03-04 21:07:14', 1, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1764638678821543936, 'dax-pay', 1749262518385082368, '差异单', 'ReconcileDiffList', NULL, b'0', '', b'1', b'0', 'payment/order/reconcile/diff/ReconcileDiffList.vue', NULL, '/pay/order/reconcile/diff', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-03-04 21:07:01', 1399985191002447872, '2024-03-05 12:49:19', 2, 0);

UPDATE `iam_perm_menu` SET `client_code` = 'dax-pay', `parent_id` = 1744642856348520448, `title` = '对账订单', `name` = 'ReconcileOrder', `perm_code` = NULL, `effect` = b'0', `icon` = '', `hidden` = b'0', `hide_children_in_menu` = b'0', `component` = 'Layout', `component_name` = NULL, `path` = '/pay/order/reconcile', `redirect` = '', `sort_no` = 0, `menu_type` = 1, `leaf` = NULL, `keep_alive` = b'1', `target_outside` = b'0', `hidden_header_content` = b'0', `admin` = b'0', `remark` = NULL, `creator` = 1399985191002447872, `create_time` = '2024-01-22 10:47:39', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-03-04 21:06:18', `version` = 1, `deleted` = 0 WHERE `id` = 1749262518385082368;

DELETE FROM `iam_perm_path` WHERE `id` = 1757297527147974657;

DELETE FROM `iam_perm_path` WHERE `id` = 1757297527147974712;

DELETE FROM `iam_perm_path` WHERE `id` = 1757297527152169074;

DELETE FROM `iam_perm_path` WHERE `id` = 1757297527152169137;

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1764663810424303616, 'ReconcileOrderController#pageDiff', '对账差异分页', 'GET', '/order/reconcile/diff/page', '对账控制器', b'1', b'1', '对账控制器 对账差异分页', 1399985191002447872, '2024-03-04 22:46:52.930000', 1399985191002447872, '2024-03-04 22:46:52.930000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1764663810424303617, 'ReconcileOrderController#findDiffById', '对账差异详情', 'GET', '/order/reconcile/diff/findById', '对账控制器', b'1', b'1', '对账控制器 对账差异详情', 1399985191002447872, '2024-03-04 22:46:52.930000', 1399985191002447872, '2024-03-04 22:46:52.930000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1764663810424303618, 'ReconcileOrderController#findDetailById', '对账明细详情', 'GET', '/order/reconcile/detail/findById', '对账控制器', b'1', b'1', '对账控制器 对账明细详情', 1399985191002447872, '2024-03-04 22:46:52.930000', 1399985191002447872, '2024-03-04 22:46:52.930000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1764663810424303619, 'ReconcileOrderController#compare', '手动触发对账单比对', 'POST', '/order/reconcile/compare', '对账控制器', b'1', b'1', '对账控制器 手动触发对账单比对', 1399985191002447872, '2024-03-04 22:46:52.930000', 1399985191002447872, '2024-03-04 22:46:52.930000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1764663810424303620, 'ReconcileOrderController#pageDetail', '对账明细分页', 'GET', '/order/reconcile/detail/page', '对账控制器', b'1', b'1', '对账控制器 对账明细分页', 1399985191002447872, '2024-03-04 22:46:52.930000', 1399985191002447872, '2024-03-04 22:46:52.930000', b'0', 0);

UPDATE `iam_perm_path` SET `code` = 'PayReconcileOrderController#findById', `name` = '订单详情', `request_type` = 'GET', `path` = '/order/reconcile/findById', `group_name` = '对账控制器', `enable` = b'1', `generate` = b'1', `remark` = '对账控制器 订单详情', `creator` = 1399985191002447872, `create_time` = '2024-02-13 14:55:54.139000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-13 14:55:54.139000', `deleted` = b'0', `version` = 0 WHERE `id` = 1757297527152169016;

UPDATE `iam_perm_path` SET `code` = 'PayReconcileOrderController#downAndSave', `name` = '手动触发对账文件下载', `request_type` = 'POST', `path` = '/order/reconcile/downAndSave', `group_name` = '对账控制器', `enable` = b'1', `generate` = b'1', `remark` = '对账控制器 手动触发对账文件下载', `creator` = 1399985191002447872, `create_time` = '2024-02-13 14:55:54.139000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-13 14:55:54.139000', `deleted` = b'0', `version` = 0 WHERE `id` = 1757297527152169041;

UPDATE `iam_perm_path` SET `code` = 'PayReconcileOrderController#create', `name` = '手动创建对账订单', `request_type` = 'POST', `path` = '/order/reconcile/create', `group_name` = '对账控制器', `enable` = b'1', `generate` = b'1', `remark` = '对账控制器 手动创建支付订单', `creator` = 1399985191002447872, `create_time` = '2024-02-13 14:55:54.139000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-13 14:55:54.139000', `deleted` = b'0', `version` = 0 WHERE `id` = 1757297527152169054;

UPDATE `iam_perm_path` SET `code` = 'PayReconcileOrderController#page', `name` = '订单分页', `request_type` = 'GET', `path` = '/order/reconcile/page', `group_name` = '对账控制器', `enable` = b'1', `generate` = b'1', `remark` = '对账控制器 订单分页', `creator` = 1399985191002447872, `create_time` = '2024-02-13 14:55:54.139000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-13 14:55:54.139000', `deleted` = b'0', `version` = 0 WHERE `id` = 1757297527152169098;

DELETE FROM `iam_role_path` WHERE `id` = 1757299898544541715;

DELETE FROM `iam_role_path` WHERE `id` = 1757299898544541716;

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1764931648924622848, 1757297023118462976, 1764663810424303620);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1764931648924622849, 1757297023118462976, 1764663810424303619);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1764931648924622850, 1757297023118462976, 1764663810424303618);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1764931648924622851, 1757297023118462976, 1764663810424303617);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1764931648924622852, 1757297023118462976, 1764663810424303616);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1764945575888781312, 1757297023118462976, 'dax-pay', 1764638353289027584);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1764945575888781313, 1757297023118462976, 'dax-pay', 1764638678821543936);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1764945576350154752, 1757298887092326400, 'dax-pay', 1764638353289027584);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1764945576350154753, 1757298887092326400, 'dax-pay', 1764638678821543936);

INSERT INTO `starter_quartz_job`(`id`, `name`, `job_class_name`, `cron`, `parameter`, `state`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1764664552203743232, '支付宝定时对账', 'cn.bootx.platform.daxpay.service.task.ReconcileTask', '* * 11 * * ? *', '{\"channel\":\"ali_pay\",\"n\":1}', 0, '', 1399985191002447872, '2024-03-04 22:49:50', 1399985191002447872, '2024-03-04 22:49:50', 0, 0);

INSERT INTO `starter_quartz_job`(`id`, `name`, `job_class_name`, `cron`, `parameter`, `state`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1764667388106887168, '微信支付定时对账', 'cn.bootx.platform.daxpay.service.task.ReconcileTask', '* * 11 * * ? *', '{\"channel\":\"wechat_pay\",\"n\":1}', 0, '', 1399985191002447872, '2024-03-04 23:01:06', 1399985191002447872, '2024-03-04 23:01:06', 0, 0);

SET FOREIGN_KEY_CHECKS = 1;
