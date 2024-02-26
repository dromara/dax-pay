/*
 数据更新脚本, 执行前需要先执行表结构更新脚本
*/
SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1430063572491411456, 'loginType', '字典类型', b'1', '基础属性', '字典类型', 1399985191002447872, '2021-08-24 15:05:00', 1399985191002447872, '2021-08-24 15:05:00', 1, 2);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1758881354618220544, 'WalletStatus', '钱包状态', b'1', '支付', '', 1399985191002447872, '2024-02-17 23:49:28', 1399985191002447872, '2024-02-17 23:49:28', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759189874194481152, 'VoucherStatus', '储值卡状态', b'1', '支付', '', 1399985191002447872, '2024-02-18 20:15:25', 1399985191002447872, '2024-02-18 20:15:25', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190066511708160, 'WalletRecordType', '钱包记录类型', b'1', '支付', '', 1399985191002447872, '2024-02-18 20:16:11', 1399985191002447872, '2024-02-18 20:16:11', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190427897135104, 'VoucherRecordType', '储值卡记录类型', b'1', '支付', '', 1399985191002447872, '2024-02-18 20:17:37', 1399985191002447872, '2024-02-18 20:17:37', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190780252225536, 'CashRecordType', '现金记录类型', b'1', '支付', '', 1399985191002447872, '2024-02-18 20:19:01', 1399985191002447872, '2024-02-19 22:07:09', 0, 1);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761434095349624832, 'ClientNoticeType', '客户消息通知类型', b'1', '支付', '', 1399985191002447872, '2024-02-25 00:53:09', 1399985191002447872, '2024-02-25 00:53:09', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761434264858226688, 'ClientNoticeSendType', '客户消息通知发送类型', b'1', '支付', '', 1399985191002447872, '2024-02-25 00:53:49', 1399985191002447872, '2024-02-25 00:53:49', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761581634023583744, 'AlipayRecordType', '支付宝流水记录类型', b'1', '支付', '', 1399985191002447872, '2024-02-25 10:39:25', 1399985191002447872, '2024-02-25 10:39:25', 0, 0);

INSERT INTO `base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761588314480300032, 'WechatPayRecordType', '微信支付流水记录类型', b'1', '支付', '', 1399985191002447872, '2024-02-25 11:05:58', 1399985191002447872, '2024-02-25 11:05:58', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1745134703416152064, 1745134438772346880, 'PayRepairWay', 'refund', '退款', b'1', 5.00, '', 1399985191002447872, '2024-01-11 01:25:11', 1399985191002447872, '2024-01-28 21:55:55', 1, 1);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1758881424411439104, 1758881354618220544, 'WalletStatus', 'normal', '正常', b'1', 1.00, '', 1399985191002447872, '2024-02-17 23:49:45', 1399985191002447872, '2024-02-17 23:49:45', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1758881470334873600, 1758881354618220544, 'WalletStatus', 'forbidden', '禁用', b'1', 2.00, '', 1399985191002447872, '2024-02-17 23:49:56', 1399985191002447872, '2024-02-17 23:49:56', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759189919170002944, 1759189874194481152, 'VoucherStatus', 'normal', '正常', b'1', 1.00, '', 1399985191002447872, '2024-02-18 20:15:36', 1399985191002447872, '2024-02-18 20:15:36', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759189962564272128, 1759189874194481152, 'VoucherStatus', 'forbidden', '禁用', b'1', 2.00, '', 1399985191002447872, '2024-02-18 20:15:46', 1399985191002447872, '2024-02-18 20:15:46', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190150464897024, 1759190066511708160, 'WalletRecordType', 'create', '创建', b'1', 1.00, '', 1399985191002447872, '2024-02-18 20:16:31', 1399985191002447872, '2024-02-18 20:16:31', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190193783668736, 1759190066511708160, 'WalletRecordType', 'pay', '支付', b'1', 2.00, '', 1399985191002447872, '2024-02-18 20:16:41', 1399985191002447872, '2024-02-18 20:16:41', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190227510067200, 1759190066511708160, 'WalletRecordType', 'refund', '退款', b'1', 3.00, '', 1399985191002447872, '2024-02-18 20:16:49', 1399985191002447872, '2024-02-18 20:16:56', 0, 1);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190291980713984, 1759190066511708160, 'WalletRecordType', 'close_pay', '支付关闭', b'1', 4.00, '', 1399985191002447872, '2024-02-18 20:17:04', 1399985191002447872, '2024-02-18 20:17:04', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190340517199872, 1759190066511708160, 'WalletRecordType', 'close_refund', '退款关闭', b'1', 5.00, '', 1399985191002447872, '2024-02-18 20:17:16', 1399985191002447872, '2024-02-19 17:31:26', 1, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190472654553088, 1759190427897135104, 'VoucherRecordType', 'import', '导入', b'1', 1.00, '', 1399985191002447872, '2024-02-18 20:17:48', 1399985191002447872, '2024-02-18 20:17:48', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190506087350272, 1759190427897135104, 'VoucherRecordType', 'pay', '支付', b'1', 2.00, '', 1399985191002447872, '2024-02-18 20:17:55', 1399985191002447872, '2024-02-18 20:18:16', 0, 1);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190574865547264, 1759190427897135104, 'VoucherRecordType', 'refund', '退款', b'1', 3.00, '', 1399985191002447872, '2024-02-18 20:18:12', 1399985191002447872, '2024-02-18 20:18:12', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190633921347584, 1759190427897135104, 'VoucherRecordType', 'close_pay', '支付关闭', b'1', 4.00, '', 1399985191002447872, '2024-02-18 20:18:26', 1399985191002447872, '2024-02-18 20:18:26', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190717757095936, 1759190427897135104, 'VoucherRecordType', 'close_refund', '退款关闭', b'1', 5.00, '', 1399985191002447872, '2024-02-18 20:18:46', 1399985191002447872, '2024-02-19 17:31:20', 1, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190820500766720, 1759190780252225536, 'CashRecordType', 'pay', '支付', b'1', 1.00, '', 1399985191002447872, '2024-02-18 20:19:10', 1399985191002447872, '2024-02-18 20:19:39', 0, 1);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190850754281472, 1759190780252225536, 'CashRecordType', 'refund', '退款', b'1', 2.00, '', 1399985191002447872, '2024-02-18 20:19:18', 1399985191002447872, '2024-02-18 20:19:42', 0, 1);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190884061249536, 1759190780252225536, 'CashRecordType', 'close_pay', '支付关闭', b'1', 3.00, '', 1399985191002447872, '2024-02-18 20:19:26', 1399985191002447872, '2024-02-18 20:19:46', 0, 1);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759190913261993984, 1759190780252225536, 'CashrRecordType', 'close_refund', '退款关闭', b'1', 4.00, '', 1399985191002447872, '2024-02-18 20:19:33', 1399985191002447872, '2024-02-19 17:31:15', 1, 1);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759514962507554816, 1759190066511708160, 'WalletRecordType', 'recharge', '充值', b'1', 11.00, '', 1399985191002447872, '2024-02-19 17:47:12', 1399985191002447872, '2024-02-19 17:47:12', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1759515000520531968, 1759190066511708160, 'WalletRecordType', 'deduct', '扣减', b'1', 12.00, '', 1399985191002447872, '2024-02-19 17:47:21', 1399985191002447872, '2024-02-19 17:47:21', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761434142275497984, 1761434095349624832, 'ClientNoticeType', 'pay', '支付通知', b'1', 1.00, '', 1399985191002447872, '2024-02-25 00:53:20', 1399985191002447872, '2024-02-25 00:53:20', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761434179445420032, 1761434095349624832, 'ClientNoticeType', 'refund', '退款通知', b'1', 2.00, '', 1399985191002447872, '2024-02-25 00:53:29', 1399985191002447872, '2024-02-25 00:53:29', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761434308537708544, 1761434264858226688, 'ClientNoticeSendType', 'auto', '自动发送', b'1', 0.00, '', 1399985191002447872, '2024-02-25 00:54:00', 1399985191002447872, '2024-02-25 00:54:00', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761434346206752768, 1761434264858226688, 'ClientNoticeSendType', 'manual', '手动发送', b'1', 1.00, '', 1399985191002447872, '2024-02-25 00:54:09', 1399985191002447872, '2024-02-25 00:54:09', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761581686381080576, 1761581634023583744, 'AlipayRecordType', 'pay', '支付', b'1', 0.00, '', 1399985191002447872, '2024-02-25 10:39:37', 1399985191002447872, '2024-02-25 10:39:37', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761581731029446656, 1761581634023583744, 'AlipayRecordType', 'refund', '退款', b'1', 1.00, '', 1399985191002447872, '2024-02-25 10:39:48', 1399985191002447872, '2024-02-25 10:39:48', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761588368863645696, 1761588314480300032, 'WechatPayRecordType', 'pay', '支付', b'1', 0.00, '', 1399985191002447872, '2024-02-25 11:06:11', 1399985191002447872, '2024-02-25 11:06:11', 0, 0);

INSERT INTO `base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1761588397825314816, 1761588314480300032, 'WechatPayRecordType', 'refund', '退款', b'1', 0.00, '', 1399985191002447872, '2024-02-25 11:06:17', 1399985191002447872, '2024-02-25 11:06:17', 0, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1758860876272861184, 'dax-pay', NULL, '支付通道', 'PayChannel', NULL, b'0', 'ant-design:align-left-outlined', b'0', b'0', 'Layout', NULL, '/pay/channel', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-17 22:28:06', 1399985191002447872, '2024-02-17 22:28:06', 0, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1758861129311027200, 'dax-pay', 1758860876272861184, '钱包管理', 'WalletList', NULL, b'0', '', b'0', b'0', 'payment/channel/wallet/manager/WalletList', NULL, '/pay/channel/wallet', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-17 22:29:06', 1399985191002447872, '2024-02-18 20:26:07', 1, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1759192238594949120, 'dax-pay', 1758860876272861184, '储值卡管理', 'VoucherList', NULL, b'0', '', b'0', b'0', 'payment/channel/voucher/manager/VoucherList', NULL, '/pay/channel/voucher', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-18 20:24:49', 1399985191002447872, '2024-02-18 20:26:14', 1, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1759192520611561472, 'dax-pay', 1758860876272861184, '现金流水', 'CashRecordList', NULL, b'0', '', b'0', b'0', 'payment/channel/cash/record/CashRecordList', NULL, '/pay/channel/cash', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-18 20:25:56', 1399985191002447872, '2024-02-18 23:35:22', 1, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1759768820429352960, 'dax-pay', NULL, '演示模块', '', NULL, b'0', 'ant-design:crown-outlined', b'0', b'0', 'Layout', NULL, '/pay/demo', '', 9, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-20 10:35:56', 1399985191002447872, '2024-02-20 10:35:56', 0, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1759769092698402816, 'dax-pay', 1759768820429352960, '收银台演示', '', NULL, b'0', '', b'0', b'0', '', NULL, 'outside:///cashier', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-20 10:37:01', 1399985191002447872, '2024-02-20 10:37:01', 0, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1759861648606097408, 'dax-pay', 1758860876272861184, '支付宝流水', 'AlipayRecordList', NULL, b'0', '', b'0', b'0', 'payment/channel/alipay/record/AlipayRecordList', NULL, '/pay/channel/alipay', '', -1, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-20 16:44:48', 1399985191002447872, '2024-02-20 16:44:48', 0, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1759865163772485632, 'dax-pay', 1758860876272861184, '微信流水', 'WechatPayRecordList', NULL, b'0', '', b'0', b'0', 'payment/channel/wechat/record/WechatPayRecordList', NULL, '/pay/channel/wecha', '', -1, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-20 16:58:46', 1399985191002447872, '2024-02-20 17:00:08', 1, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1761429304959528960, 'dax-pay', NULL, '任务记录', 'PayTask', NULL, b'0', 'ant-design:clock-circle-outlined', b'0', b'0', 'Layout', NULL, '/pay/task', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-25 00:34:07', 1399985191002447872, '2024-02-25 00:34:30', 1, 0);

INSERT INTO `iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1761429682618855424, 'dax-pay', 1761429304959528960, '消息通知', 'ClientNoticeTaskList', NULL, b'0', '', b'0', b'0', 'payment/task/notice/ClientNoticeTaskList', NULL, '/pay/task/notice', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-02-25 00:35:37', 1399985191002447872, '2024-02-25 00:35:37', 0, 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206464, 'VoucherController#findById', '查询储值卡详情', 'GET', '/voucher/findById', '储值卡管理', b'1', b'1', '储值卡管理 查询储值卡详情', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206465, 'WalletConfigController#update', '更新', 'POST', '/wallet/config/update', '钱包配置', b'1', b'1', '钱包配置 更新', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206466, 'ClientNoticeTaskController#recordPage', '分页查询', 'GET', '/task/notice/record/page', '客户系统通知任务', b'1', b'1', '客户系统通知任务 分页查询', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206467, 'WalletConfigController#findPayWays', '支付宝支持支付方式', 'GET', '/wallet/config/findPayWays', '钱包配置', b'1', b'1', '钱包配置 支付宝支持支付方式', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206468, 'VoucherController#voucherImport', '导入储值卡', 'POST', '/voucher/import', '储值卡管理', b'1', b'1', '储值卡管理 导入储值卡', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206469, 'VoucherController#recordFindById', '查询记录详情', 'GET', '/voucher/record/findById', '储值卡管理', b'1', b'1', '储值卡管理 查询记录详情', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206470, 'CashController#recordPage', '记录分页', 'GET', '/cash/record/page', '现金控制器', b'1', b'1', '现金控制器 记录分页', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206471, 'AlipayController#recordPage', '记录分页', 'GET', '/alipay/record/page', '支付宝控制器', b'1', b'1', '支付宝控制器 记录分页', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206472, 'CashConfigController#getConfig', '获取配置', 'GET', '/cash/config/getConfig', '现金支付配置', b'1', b'1', '现金支付配置 获取配置', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206473, 'ClientNoticeReceiveController#pay', '支付消息(对象接收)', 'POST', '/demo/callback/payObject', '回调测试', b'1', b'1', '回调测试 支付消息(对象接收)', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206474, 'VoucherConfigController#findPayWays', '支付宝支持支付方式', 'GET', '/voucher/config/findPayWays', '储值卡支付配置', b'1', b'1', '储值卡支付配置 支付宝支持支付方式', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206475, 'ClientNoticeReceiveController#refund', '退款消息(对象)', 'POST', '/demo/callback/refundObject', '回调测试', b'1', b'1', '回调测试 退款消息(对象)', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351862206476, 'WalletController#recharge', '充值', 'POST', '/wallet/recharge', '钱包管理', b'1', b'1', '钱包管理 充值', 1399985191002447872, '2024-02-26 21:48:17.805000', 1399985191002447872, '2024-02-26 21:48:17.805000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400768, 'VoucherController#page', '储值卡分页', 'GET', '/voucher/page', '储值卡管理', b'1', b'1', '储值卡管理 储值卡分页', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400769, 'PayReturnController#wechat', '微信同步通知', 'GET', '/return/pay/wechat', '支付同步通知', b'1', b'1', '支付同步通知 微信同步通知', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400770, 'WalletController#findRecordById', '查询记录详情', 'GET', '/wallet/record/findById', '钱包管理', b'1', b'1', '钱包管理 查询记录详情', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400771, 'WalletConfigController#getConfig', '获取配置', 'GET', '/wallet/config/getConfig', '钱包配置', b'1', b'1', '钱包配置 获取配置', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400772, 'WalletController#recordPage', '记录分页', 'GET', '/wallet/record/page', '钱包管理', b'1', b'1', '钱包管理 记录分页', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400773, 'VoucherController#existsByCardNo', '判断卡号是否存在', 'GET', '/voucher/existsByCardNo', '储值卡管理', b'1', b'1', '储值卡管理 判断卡号是否存在', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400774, 'WalletController#page', '钱包分页', 'GET', '/wallet/page', '钱包管理', b'1', b'1', '钱包管理 钱包分页', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400775, 'ClientNoticeTaskController#findById', '查询单条', 'GET', '/task/notice/findById', '客户系统通知任务', b'1', b'1', '客户系统通知任务 查询单条', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400776, 'CashController#findById', '查询记录详情', 'GET', '/cash/record/findById', '现金控制器', b'1', b'1', '现金控制器 查询记录详情', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400777, 'WeChatPayController#findById', '查询记录详情', 'GET', '/wechat/pay/record/findById', '微信支付控制器', b'1', b'1', '微信支付控制器 查询记录详情', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400778, 'ClientNoticeReceiveController#pay', '支付消息(map接收)', 'POST', '/demo/callback/pay', '回调测试', b'1', b'1', '回调测试 支付消息(map接收)', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400779, 'VoucherConfigController#getConfig', '获取配置', 'GET', '/voucher/config/getConfig', '储值卡支付配置', b'1', b'1', '储值卡支付配置 获取配置', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400780, 'WalletController#create', '创建钱包', 'POST', '/wallet/create', '钱包管理', b'1', b'1', '钱包管理 创建钱包', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400781, 'ClientNoticeTaskController#findRecordById', '查询单条', 'GET', '/task/notice/record/findById', '客户系统通知任务', b'1', b'1', '客户系统通知任务 查询单条', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400782, 'CashConfigController#update', '更新', 'POST', '/cash/config/update', '现金支付配置', b'1', b'1', '现金支付配置 更新', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400783, 'ClientNoticeReceiveController#refund', '退款消息', 'POST', '/demo/callback/refund', '回调测试', b'1', b'1', '回调测试 退款消息', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400784, 'WeChatPayController#recordPage', '记录分页', 'GET', '/wechat/pay/record/page', '微信支付控制器', b'1', b'1', '微信支付控制器 记录分页', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400785, 'PayReturnController#alipay', '支付宝同步跳转连接', 'GET', '/return/pay/alipay', '支付同步通知', b'1', b'1', '支付同步通知 支付宝同步跳转连接', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400786, 'WalletController#findById', '查询钱包详情', 'GET', '/wallet/findById', '钱包管理', b'1', b'1', '钱包管理 查询钱包详情', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400787, 'CashConfigController#findPayWays', '支付宝支持支付方式', 'GET', '/cash/config/findPayWays', '现金支付配置', b'1', b'1', '现金支付配置 支付宝支持支付方式', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400788, 'AlipayController#findById', '查询记录详情', 'GET', '/alipay/record/findById', '支付宝控制器', b'1', b'1', '支付宝控制器 查询记录详情', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400789, 'VoucherConfigController#update', '更新', 'POST', '/voucher/config/update', '储值卡支付配置', b'1', b'1', '储值卡支付配置 更新', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400790, 'ClientNoticeTaskController#page', '分页查询', 'GET', '/task/notice/page', '客户系统通知任务', b'1', b'1', '客户系统通知任务 分页查询', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400791, 'VoucherController#recordPage', '记录分页', 'GET', '/voucher/record/page', '储值卡管理', b'1', b'1', '储值卡管理 记录分页', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400792, 'ClientNoticeTaskController#resetSend', '重新发送消息通知', 'POST', '/task/notice/resetSend', '客户系统通知任务', b'1', b'1', '客户系统通知任务 重新发送消息通知', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400793, 'WalletController#deduct', '扣减', 'POST', '/wallet/deduct', '钱包管理', b'1', b'1', '钱包管理 扣减', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1762112351866400794, 'WalletController#existsByUserId', '判断用户是否开通了钱包', 'GET', '/wallet/existsByUserId', '钱包管理', b'1', b'1', '钱包管理 判断用户是否开通了钱包', 1399985191002447872, '2024-02-26 21:48:17.806000', 1399985191002447872, '2024-02-26 21:48:17.806000', b'0', 0);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073344, 1757297023118462976, 'dax-pay', 1758860876272861184);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073345, 1757297023118462976, 'dax-pay', 1759861648606097408);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073346, 1757297023118462976, 'dax-pay', 1759865163772485632);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073347, 1757297023118462976, 'dax-pay', 1758861129311027200);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073348, 1757297023118462976, 'dax-pay', 1759192238594949120);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073349, 1757297023118462976, 'dax-pay', 1759192520611561472);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073350, 1757297023118462976, 'dax-pay', 1761429304959528960);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073351, 1757297023118462976, 'dax-pay', 1761429682618855424);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073352, 1757297023118462976, 'dax-pay', 1759768820429352960);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282006073353, 1757297023118462976, 'dax-pay', 1759769092698402816);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565760, 1757298887092326400, 'dax-pay', 1758860876272861184);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565761, 1757298887092326400, 'dax-pay', 1759861648606097408);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565762, 1757298887092326400, 'dax-pay', 1759865163772485632);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565763, 1757298887092326400, 'dax-pay', 1758861129311027200);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565764, 1757298887092326400, 'dax-pay', 1759192238594949120);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565765, 1757298887092326400, 'dax-pay', 1759192520611561472);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565766, 1757298887092326400, 'dax-pay', 1761429304959528960);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565767, 1757298887092326400, 'dax-pay', 1761429682618855424);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565768, 1757298887092326400, 'dax-pay', 1759768820429352960);

INSERT INTO `iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1762112282232565769, 1757298887092326400, 'dax-pay', 1759769092698402816);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020608, 1757297023118462976, 1762112351866400794);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020609, 1757297023118462976, 1762112351866400793);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020610, 1757297023118462976, 1762112351866400786);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020611, 1757297023118462976, 1762112351866400780);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020612, 1757297023118462976, 1762112351866400774);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020613, 1757297023118462976, 1762112351866400772);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020614, 1757297023118462976, 1762112351866400770);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020615, 1757297023118462976, 1762112351862206476);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020616, 1757297023118462976, 1762112351866400787);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020617, 1757297023118462976, 1762112351866400782);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020618, 1757297023118462976, 1762112351862206472);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020619, 1757297023118462976, 1762112351866400788);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020620, 1757297023118462976, 1762112351862206471);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020621, 1757297023118462976, 1762112351866400789);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020622, 1757297023118462976, 1762112351866400779);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020623, 1757297023118462976, 1762112351862206474);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020624, 1757297023118462976, 1762112351866400791);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020625, 1757297023118462976, 1762112351866400773);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020626, 1757297023118462976, 1762112351866400768);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020627, 1757297023118462976, 1762112351862206469);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020628, 1757297023118462976, 1762112351862206468);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020629, 1757297023118462976, 1762112351862206464);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020630, 1757297023118462976, 1762112351866400792);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020631, 1757297023118462976, 1762112351866400790);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020632, 1757297023118462976, 1762112351866400781);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020633, 1757297023118462976, 1762112351866400775);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020634, 1757297023118462976, 1762112351862206466);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020635, 1757297023118462976, 1762112351866400776);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020636, 1757297023118462976, 1762112351862206470);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020637, 1757297023118462976, 1762112351866400784);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020638, 1757297023118462976, 1762112351866400777);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020639, 1757297023118462976, 1762112351866400771);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020640, 1757297023118462976, 1762112351862206467);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867598020641, 1757297023118462976, 1762112351862206465);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095936, 1757298887092326400, 1762112351866400794);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095937, 1757298887092326400, 1762112351866400793);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095938, 1757298887092326400, 1762112351866400786);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095939, 1757298887092326400, 1762112351866400780);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095940, 1757298887092326400, 1762112351866400774);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095941, 1757298887092326400, 1762112351866400772);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095942, 1757298887092326400, 1762112351866400770);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095943, 1757298887092326400, 1762112351862206476);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095944, 1757298887092326400, 1762112351866400787);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095945, 1757298887092326400, 1762112351866400782);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095946, 1757298887092326400, 1762112351862206472);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095947, 1757298887092326400, 1762112351866400788);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095948, 1757298887092326400, 1762112351862206471);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095949, 1757298887092326400, 1762112351866400789);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095950, 1757298887092326400, 1762112351866400779);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095951, 1757298887092326400, 1762112351862206474);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095952, 1757298887092326400, 1762112351866400791);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095953, 1757298887092326400, 1762112351866400773);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095954, 1757298887092326400, 1762112351866400768);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095955, 1757298887092326400, 1762112351862206469);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095956, 1757298887092326400, 1762112351862206468);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095957, 1757298887092326400, 1762112351862206464);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095958, 1757298887092326400, 1762112351866400792);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095959, 1757298887092326400, 1762112351866400790);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095960, 1757298887092326400, 1762112351866400781);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095961, 1757298887092326400, 1762112351866400775);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095962, 1757298887092326400, 1762112351862206466);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095963, 1757298887092326400, 1762112351866400776);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095964, 1757298887092326400, 1762112351862206470);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095965, 1757298887092326400, 1762112351866400784);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095966, 1757298887092326400, 1762112351866400777);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095967, 1757298887092326400, 1762112351866400771);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095968, 1757298887092326400, 1762112351862206467);

INSERT INTO `iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1762112867837095969, 1757298887092326400, 1762112351862206465);

UPDATE `pay_api_config` SET `code` = 'pay', `api` = '/uniPay/pay', `name` = '统一支付接口	', `notice_support` = b'1', `enable` = b'1', `notice` = b'1', `notice_url` = 'http://127.0.0.1:9000/demo/callback/payObject', `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'1', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-25 15:20:41', `version` = 7, `deleted` = b'0', `remark` = NULL WHERE `id` = 1;

UPDATE `pay_api_config` SET `code` = 'simplePay', `api` = '/uniPay/simplePay', `name` = '简单支付接口', `notice_support` = b'1', `enable` = b'1', `notice` = b'1', `notice_url` = 'http://127.0.0.1:9000/demo/callback/payObject', `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'1', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-25 15:20:44', `version` = 11, `deleted` = b'0', `remark` = NULL WHERE `id` = 2;

UPDATE `pay_api_config` SET `code` = 'close', `api` = '/uniPay/close', `name` = '支付关闭接口', `notice_support` = b'0', `enable` = b'1', `notice` = b'1', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 3;

UPDATE `pay_api_config` SET `code` = 'refund', `api` = '/uniPay/refund', `name` = '统一退款接口', `notice_support` = b'1', `enable` = b'1', `notice` = b'1', `notice_url` = 'http://127.0.0.1:9000/demo/callback/refundObject', `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'1', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-25 15:20:33', `version` = 4, `deleted` = b'0', `remark` = NULL WHERE `id` = 4;

UPDATE `pay_api_config` SET `code` = 'simpleRefund', `api` = '/uniPay/simpleRefund', `name` = '简单退款接口', `notice_support` = b'1', `enable` = b'1', `notice` = b'1', `notice_url` = 'http://127.0.0.1:9000/demo/callback/refundObject', `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'1', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-25 15:20:38', `version` = 4, `deleted` = b'0', `remark` = NULL WHERE `id` = 5;

UPDATE `pay_api_config` SET `code` = 'syncPay', `api` = '/uniPay/syncPay', `name` = '支付同步接口', `notice_support` = b'0', `enable` = b'1', `notice` = b'1', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 6;

UPDATE `pay_api_config` SET `code` = 'syncRefund', `api` = '/uniPay/syncRefund', `name` = '退款同步接口', `notice_support` = b'0', `enable` = b'1', `notice` = b'1', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 7;

UPDATE `pay_api_config` SET `code` = 'transfer', `api` = '/uniPay/transfer', `name` = '统一转账接口', `notice_support` = b'1', `enable` = b'1', `notice` = b'1', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 8;

UPDATE `pay_api_config` SET `code` = 'allocation', `api` = '/uniPay/allocation', `name` = '统一分账接口', `notice_support` = b'1', `enable` = b'1', `notice` = b'1', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 9;

UPDATE `pay_api_config` SET `code` = 'queryPayOrder', `api` = '/uniPay/queryPayOrder', `name` = '支付订单查询接口', `notice_support` = b'0', `enable` = b'1', `notice` = b'0', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 10;

UPDATE `pay_api_config` SET `code` = 'queryRefundOrder', `api` = '/uniPay/queryRefundOrder', `name` = '退款订单查询接口', `notice_support` = b'0', `enable` = b'1', `notice` = b'0', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 11;

UPDATE `pay_api_config` SET `code` = 'getWxAuthUrl', `api` = '/unipay/assist/getWxAuthUrl', `name` = '获取微信OAuth2授权链接', `notice_support` = b'0', `enable` = b'1', `notice` = b'0', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 12;

UPDATE `pay_api_config` SET `code` = 'getWxAccessToken', `api` = '/unipay/assist/getWxAccessToken', `name` = '获取微信AccessToken', `notice_support` = b'0', `enable` = b'1', `notice` = b'0', `notice_url` = NULL, `req_sign` = b'1', `res_sign` = b'0', `notice_sign` = b'0', `record` = b'0', `creator` = 0, `create_time` = '2024-01-03 14:25:48', `last_modifier` = 0, `last_modified_time` = '2024-01-03 14:25:53', `version` = 0, `deleted` = b'0', `remark` = NULL WHERE `id` = 13;

UPDATE `pay_channel_config` SET `code` = 'cash_pay', `name` = '现金支付', `icon_id` = NULL, `bg_color` = NULL, `enable` = b'1', `remark` = NULL, `creator` = 0, `create_time` = '2024-01-08 16:47:07', `last_modifier` = 0, `last_modified_time` = '2024-01-08 16:47:11', `version` = 0, `deleted` = b'0' WHERE `id` = 4;

UPDATE `pay_channel_config` SET `code` = 'wallet_pay', `name` = '钱包支付', `icon_id` = NULL, `bg_color` = NULL, `enable` = b'1', `remark` = NULL, `creator` = 0, `create_time` = '2024-01-08 16:47:07', `last_modifier` = 0, `last_modified_time` = '2024-01-08 16:47:11', `version` = 0, `deleted` = b'0' WHERE `id` = 5;

UPDATE `pay_channel_config` SET `code` = 'voucher_pay', `name` = '储值卡支付', `icon_id` = NULL, `bg_color` = NULL, `enable` = b'1', `remark` = NULL, `creator` = 0, `create_time` = '2024-01-08 16:47:07', `last_modifier` = 0, `last_modified_time` = '2024-01-08 16:47:11', `version` = 0, `deleted` = b'0' WHERE `id` = 6;

INSERT INTO `pay_wallet_config`(`id`, `enable`, `single_limit`, `pay_ways`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (0, b'1', 2000, 'normal', NULL, 0, '2024-02-17 14:36:28', 1399985191002447872, '2024-02-17 14:40:45', 4, b'0');

INSERT INTO `pay_voucher_config`(`id`, `enable`, `single_limit`, `pay_ways`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (0, b'1', 2000, 'normal', NULL, 0, '2024-02-17 14:36:28', 1399985191002447872, '2024-02-17 17:01:25', 7, b'0');

INSERT INTO `pay_cash_config`(`id`, `enable`, `single_limit`, `pay_ways`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (0, b'1', 2000, 'normal', NULL, 0, '2024-02-17 14:36:28', 1399985191002447872, '2024-02-17 14:40:45', 4, b'0');

UPDATE `pay_wechat_pay_config` SET `wx_mch_id` = '123', `wx_app_id` = '123', `enable` = b'1', `notify_url` = 'ServerUrl/callback/pay/wechat', `return_url` = 'ServerUrl/pay/wechat', `api_key_v2` = NULL, `api_key_v3` = 'E0jIzPNngkpkZYL19H3vFQ==', `app_secret` = 'E0jIzPNngkpkZYL19H3vFQ==', `p12` = 'E0jIzPNngkpkZYL19H3vFQ==', `sandbox` = NULL, `pay_ways` = '0', `remark` = 'wap,app,jsapi,qrcode,barcode', `creator` = 0, `create_time` = '0000-00-00 00:00:00', `last_modifier` = 2024, `last_modified_time` = '0000-00-00 00:00:00', `version` = 2024, `deleted` = b'1', `api_version` = '0' WHERE `id` = 0;

INSERT INTO `qrtz_cron_triggers`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `CRON_EXPRESSION`, `TIME_ZONE_ID`) VALUES ('quartzScheduler', '1546857070483939328', 'DEFAULT', '0/5 * * * * ? *', 'Asia/Shanghai');

INSERT INTO `qrtz_job_details`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`, `DESCRIPTION`, `JOB_CLASS_NAME`, `IS_DURABLE`, `IS_NONCONCURRENT`, `IS_UPDATE_DATA`, `REQUESTS_RECOVERY`, `JOB_DATA`) VALUES ('quartzScheduler', '1546857070483939328', 'DEFAULT', NULL, 'cn.bootx.platform.daxpay.service.task.PayExpiredTimeTask', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C77080000001000000001740009706172616D65746572707800);

INSERT INTO `qrtz_triggers`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `JOB_NAME`, `JOB_GROUP`, `DESCRIPTION`, `NEXT_FIRE_TIME`, `PREV_FIRE_TIME`, `PRIORITY`, `TRIGGER_STATE`, `TRIGGER_TYPE`, `START_TIME`, `END_TIME`, `CALENDAR_NAME`, `MISFIRE_INSTR`, `JOB_DATA`) VALUES ('quartzScheduler', '1546857070483939328', 'DEFAULT', '1546857070483939328', 'DEFAULT', NULL, 1708956430000, 1708956425000, 5, 'WAITING', 'CRON', 1708844216000, 0, NULL, 0, '');

INSERT INTO `starter_quartz_job`(`id`, `name`, `job_class_name`, `cron`, `parameter`, `state`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1546857070483939328, '支付单超时检测', 'cn.bootx.platform.daxpay.service.task.PayExpiredTimeTask', '0/5 * * * * ? *', NULL, 1, '检测超时的支付单, 超时后调用同步事件状态修复', 1399985191002447872, '2022-07-12 22:00:39', 1399985191002447872, '2024-02-25 14:56:56', 4, 0);

INSERT INTO `starter_quartz_job`(`id`, `name`, `job_class_name`, `cron`, `parameter`, `state`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1761419490908958720, '客户系统通知重发任务', 'cn.bootx.platform.daxpay.service.task.ClientNoticeSendTask', '0/1 * * * * ? *', '', 0, '每秒调用一下当前需要进行通知的任务', 1399985191002447872, '2024-02-24 23:55:07', 1399985191002447872, '2024-02-25 15:03:28', 1, 0);

UPDATE `starter_quartz_job` SET `name` = '测试任务', `job_class_name` = 'cn.bootx.platform.starter.quartz.task.TestTask', `cron` = '0/3 0 * * * ? *', `parameter` = '{\"aaa\":\"5255\"}', `state` = 0, `remark` = '测试任务', `creator` = 1399985191002447872, `create_time` = '2021-11-05 19:09:43', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-02-24 23:57:57', `version` = 29, `deleted` = 0 WHERE `id` = 1456579473573867520;

SET FOREIGN_KEY_CHECKS = 1;
