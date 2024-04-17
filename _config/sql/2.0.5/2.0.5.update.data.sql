SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `dax-pay`.`base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775112798259302400, 'AllocationReceiverType', '分账接收方类型', b'1', '支付', '', 1399985191002447872, '2024-04-02 18:47:26', 1399985191002447872, '2024-04-02 18:47:26', 0, 0);

INSERT INTO `dax-pay`.`base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122632706805760, 'AllocationRelationType', '分账关系类型', b'1', '支付', '', 1399985191002447872, '2024-04-02 19:26:30', 1399985191002447872, '2024-04-02 19:26:30', 0, 0);

INSERT INTO `dax-pay`.`base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777697358802530304, 'AllocationOrderStatus', '分账状态', b'1', '支付', '', 1399985191002447872, '2024-04-09 21:57:33', 1399985191002447872, '2024-04-16 19:09:18', 0, 1);

INSERT INTO `dax-pay`.`base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780163691808391168, 'AllocationDetailResult', '支付分账明细处理结果', b'1', '支付', '', 1399985191002447872, '2024-04-16 17:17:53', 1399985191002447872, '2024-04-16 17:23:37', 0, 2);

INSERT INTO `dax-pay`.`base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780165499633106944, 'AllocationOrderResult', '支付分账订单处理结果', b'1', '支付', '', 1399985191002447872, '2024-04-16 17:25:04', 1399985191002447872, '2024-04-16 17:25:04', 0, 0);

INSERT INTO `dax-pay`.`base_dict`(`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780165929528295424, 'PayOrderAllocationStatus', '支付订单分账状态', b'1', '支付', '', 1399985191002447872, '2024-04-16 17:26:46', 1399985191002447872, '2024-04-16 17:26:46', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122126567559168, 1775112798259302400, 'AllocationReceiverType', 'wx_personal', '个人', b'1', 1.00, '', 1399985191002447872, '2024-04-02 19:24:30', 1399985191002447872, '2024-04-02 19:24:30', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122171861848064, 1775112798259302400, 'AllocationReceiverType', 'wx_merchant', '商户', b'1', 2.00, '', 1399985191002447872, '2024-04-02 19:24:41', 1399985191002447872, '2024-04-02 19:24:41', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122227956469760, 1775112798259302400, 'AllocationReceiverType', 'ali_user_id', '用户ID', b'1', 3.00, '', 1399985191002447872, '2024-04-02 19:24:54', 1399985191002447872, '2024-04-02 19:25:53', 0, 1);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122278170677248, 1775112798259302400, 'AllocationReceiverType', 'ali_open_id', '登录号', b'1', 4.00, '', 1399985191002447872, '2024-04-02 19:25:06', 1399985191002447872, '2024-04-02 19:25:06', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122427802472448, 1775112798259302400, 'AllocationReceiverType', 'ali_login_name', '账号', b'1', 5.00, '', 1399985191002447872, '2024-04-02 19:25:42', 1399985191002447872, '2024-04-02 19:25:42', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122672623996928, 1775122632706805760, 'AllocationRelationType', 'SERVICE_PROVIDER', '服务商', b'1', 1.00, '', 1399985191002447872, '2024-04-02 19:26:40', 1399985191002447872, '2024-04-02 19:26:40', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122710884438016, 1775122632706805760, 'AllocationRelationType', 'STORE', '门店', b'1', 2.00, '', 1399985191002447872, '2024-04-02 19:26:49', 1399985191002447872, '2024-04-02 19:26:49', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122750612885504, 1775122632706805760, 'AllocationRelationType', 'STAFF', '员工', b'1', 3.00, '', 1399985191002447872, '2024-04-02 19:26:59', 1399985191002447872, '2024-04-02 19:26:59', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122847035740160, 1775122632706805760, 'AllocationRelationType', 'STORE_OWNER', '店主', b'1', 4.00, '', 1399985191002447872, '2024-04-02 19:27:22', 1399985191002447872, '2024-04-02 19:27:22', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122892464246784, 1775122632706805760, 'AllocationRelationType', 'PARTNER', '合作伙伴', b'1', 5.00, '', 1399985191002447872, '2024-04-02 19:27:32', 1399985191002447872, '2024-04-02 19:27:32', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775122934138851328, 1775122632706805760, 'AllocationRelationType', 'HEADQUARTER', '总部', b'1', 6.00, '', 1399985191002447872, '2024-04-02 19:27:42', 1399985191002447872, '2024-04-02 19:27:42', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775123560512016384, 1775122632706805760, 'AllocationRelationType', 'DISTRIBUTOR', '分销商', b'1', 7.00, '', 1399985191002447872, '2024-04-02 19:30:12', 1399985191002447872, '2024-04-02 19:30:12', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775123607781822464, 1775122632706805760, 'AllocationRelationType', 'USER', '用户', b'1', 8.00, '', 1399985191002447872, '2024-04-02 19:30:23', 1399985191002447872, '2024-04-02 19:30:23', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775123654527340544, 1775122632706805760, 'AllocationRelationType', 'SUPPLIER', '供应商', b'1', 9.00, '', 1399985191002447872, '2024-04-02 19:30:34', 1399985191002447872, '2024-04-02 19:30:34', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1775123705886593024, 1775122632706805760, 'AllocationRelationType', 'CUSTOM', '自定义', b'1', 10.00, '', 1399985191002447872, '2024-04-02 19:30:46', 1399985191002447872, '2024-04-02 19:30:46', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777700713809522688, 1777697358802530304, 'AllocationOrderStatus', 'waiting', '待分账', b'1', 1.00, '', 1399985191002447872, '2024-04-09 22:10:53', 1399985191002447872, '2024-04-09 22:10:53', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777700750744563712, 1777697358802530304, 'AllocationOrderStatus', 'allocation_processing', '分账处理中', b'1', 2.00, '', 1399985191002447872, '2024-04-09 22:11:02', 1399985191002447872, '2024-04-16 17:14:53', 0, 1);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777700787453112320, 1777697358802530304, 'AllocationOrderStatus', 'allocation_end', '分账完成', b'1', 3.00, '', 1399985191002447872, '2024-04-09 22:11:10', 1399985191002447872, '2024-04-16 17:36:25', 0, 2);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777700835826020352, 1777697358802530304, 'AllocationOrderStatus', 'allocation_failed', '分账失败', b'1', 4.00, '', 1399985191002447872, '2024-04-09 22:11:22', 1399985191002447872, '2024-04-16 17:16:34', 0, 2);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777700870613577728, 1777697358802530304, 'AllocationOrderStatus', 'finish', '分账完结', b'1', 5.00, '', 1399985191002447872, '2024-04-09 22:11:30', 1399985191002447872, '2024-04-16 17:17:06', 0, 1);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777700915450687488, 1777697358802530304, 'AllocationStatus', 'partial_failed', '部分分账失败', b'1', 6.00, '', 1399985191002447872, '2024-04-09 22:11:41', 1399985191002447872, '2024-04-16 17:16:49', 1, 1);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777700964087836672, 1777697358802530304, 'AllocationOrderStatus', 'finish_failed', '分账完结失败', b'1', 6.00, '', 1399985191002447872, '2024-04-09 22:11:53', 1399985191002447872, '2024-04-16 17:17:25', 0, 1);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777701030081015808, 1777697358802530304, 'AllocationStatus', 'closed', '分账关闭', b'1', 8.00, '', 1399985191002447872, '2024-04-09 22:12:08', 1399985191002447872, '2024-04-16 17:16:19', 1, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1777701090676125696, 1777697358802530304, 'AllocationStatus', 'unknown', '分账状态未知', b'1', 9.00, '', 1399985191002447872, '2024-04-09 22:12:23', 1399985191002447872, '2024-04-16 17:16:16', 1, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780164864510623744, 1780163691808391168, 'AllocationDetailResult', 'pending', '待分账', b'1', 1.00, '', 1399985191002447872, '2024-04-16 17:22:32', 1399985191002447872, '2024-04-16 17:22:32', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780164903886749696, 1780163691808391168, 'AllocationDetailResult', 'success', '分账成功', b'1', 2.00, '', 1399985191002447872, '2024-04-16 17:22:42', 1399985191002447872, '2024-04-16 17:22:42', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780164940712738816, 1780163691808391168, 'AllocationDetailResult', 'fail', '分账失败', b'1', 0.00, '', 1399985191002447872, '2024-04-16 17:22:50', 1399985191002447872, '2024-04-16 17:22:50', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780165545665593344, 1780165499633106944, 'AllocationOrderResult', 'all_pending', '全部成功', b'1', 0.00, '', 1399985191002447872, '2024-04-16 17:25:15', 1399985191002447872, '2024-04-16 17:25:15', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780165581623361536, 1780165499633106944, 'AllocationOrderResult', 'all_success', '全部成功', b'1', 1.00, '', 1399985191002447872, '2024-04-16 17:25:23', 1399985191002447872, '2024-04-16 17:25:23', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780165617413357568, 1780165499633106944, 'AllocationOrderResult', 'part_success', '部分成功', b'1', 2.00, '', 1399985191002447872, '2024-04-16 17:25:32', 1399985191002447872, '2024-04-16 17:25:32', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780165653350154240, 1780165499633106944, 'AllocationOrderResult', 'all_failed', '全部失败', b'1', 3.00, '', 1399985191002447872, '2024-04-16 17:25:40', 1399985191002447872, '2024-04-16 17:25:40', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780165986231091200, 1780165929528295424, 'PayOrderAllocationStatus', 'waiting', '待分账', b'1', 1.00, '', 1399985191002447872, '2024-04-16 17:27:00', 1399985191002447872, '2024-04-16 17:27:00', 0, 0);

INSERT INTO `dax-pay`.`base_dict_item`(`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780166037149941760, 1780165929528295424, 'PayOrderAllocationStatus', 'allocation', '已分账', b'1', 2.00, '', 1399985191002447872, '2024-04-16 17:27:12', 1399985191002447872, '2024-04-16 17:27:12', 0, 0);

INSERT INTO `dax-pay`.`iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1775089099078553600, 'dax-pay', NULL, '分账管理', 'Allocation', NULL, b'0', 'ant-design:sliders-twotone', b'0', b'0', 'Layout', NULL, '/pay/allocation', '', 0, 0, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-04-02 17:13:15', 1399985191002447872, '2024-04-02 17:13:15', 0, 0);

INSERT INTO `dax-pay`.`iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1775089820368818176, 'dax-pay', 1775089099078553600, '分账接收者', 'AllocationReceiverList', NULL, b'0', '', b'0', b'0', 'payment/allocation/receiver/AllocationReceiverList', NULL, '/pay/allocation/receiver', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-04-02 17:16:07', 1399985191002447872, '2024-04-02 17:23:13', 2, 0);

INSERT INTO `dax-pay`.`iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1775091561835450368, 'dax-pay', 1775089099078553600, '分账组', 'AllocationGroupList', NULL, b'0', '', b'0', b'0', 'payment/allocation/group/AllocationGroupList', NULL, '/pay/allocation/group', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-04-02 17:23:03', 1399985191002447872, '2024-04-02 17:23:27', 1, 0);

INSERT INTO `dax-pay`.`iam_perm_menu`(`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `admin`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1777688382748700672, 'dax-pay', 1744642856348520448, '分账订单', 'AllocationOrderList', NULL, b'0', '', b'0', b'0', 'payment/order/allocation/AllocationOrderList', NULL, '/pay/order/allocation', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-04-09 21:21:53', 1399985191002447872, '2024-04-09 21:21:53', 0, 0);

UPDATE `dax-pay`.`iam_perm_menu` SET `client_code` = 'dax-pay', `parent_id` = 1744642856348520448, `title` = '对账订单', `name` = 'ReconcileOrderList', `perm_code` = NULL, `effect` = b'0', `icon` = '', `hidden` = b'0', `hide_children_in_menu` = b'0', `component` = 'payment/order/reconcile/order/ReconcileOrderList.vue', `component_name` = NULL, `path` = '/pay/order/reconcile/list', `redirect` = '', `sort_no` = 0, `menu_type` = 1, `leaf` = NULL, `keep_alive` = b'1', `target_outside` = b'0', `hidden_header_content` = b'0', `admin` = b'0', `remark` = NULL, `creator` = 1399985191002447872, `create_time` = '2024-01-22 10:47:39', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-04-16 19:11:56', `version` = 5, `deleted` = 0 WHERE `id` = 1749262518385082368;

UPDATE `dax-pay`.`iam_perm_menu` SET `client_code` = 'dax-pay', `parent_id` = 1749262518385082368, `title` = '对账单', `name` = 'ReconcileOrderList', `perm_code` = NULL, `effect` = b'0', `icon` = '', `hidden` = b'0', `hide_children_in_menu` = b'0', `component` = 'payment/order/reconcile/order/ReconcileOrderList.vue', `component_name` = NULL, `path` = '/pay/order/reconcile/list', `redirect` = '', `sort_no` = 0, `menu_type` = 1, `leaf` = NULL, `keep_alive` = b'1', `target_outside` = b'0', `hidden_header_content` = b'0', `admin` = b'0', `remark` = NULL, `creator` = 1399985191002447872, `create_time` = '2024-03-04 21:05:43', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-04-16 19:12:02', `version` = 1, `deleted` = 1 WHERE `id` = 1764638353289027584;

UPDATE `dax-pay`.`iam_perm_menu` SET `client_code` = 'dax-pay', `parent_id` = 1749262518385082368, `title` = '差异单', `name` = 'ReconcileDiffList', `perm_code` = NULL, `effect` = b'0', `icon` = '', `hidden` = b'1', `hide_children_in_menu` = b'0', `component` = 'payment/order/reconcile/diff/ReconcileDiffList.vue', `component_name` = NULL, `path` = '/pay/order/reconcile/diff', `redirect` = '', `sort_no` = 0, `menu_type` = 1, `leaf` = NULL, `keep_alive` = b'1', `target_outside` = b'0', `hidden_header_content` = b'0', `admin` = b'0', `remark` = NULL, `creator` = 1399985191002447872, `create_time` = '2024-03-04 21:07:01', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-04-16 19:12:06', `version` = 2, `deleted` = 1 WHERE `id` = 1764638678821543936;

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138432, 'AllocationOrderController#sync', '同步分账结果', 'POST', '/order/allocation/sync', '对账订单控制器', b'1', b'1', '对账订单控制器 同步分账结果', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.850000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138433, 'AllocationGroupController#findReceiversByGroups', '查询分账接收方信息', 'GET', '/allocation/group/findReceiversByGroups', '分账组', b'1', b'1', '分账组 查询分账接收方信息', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138434, 'AllocationGroupController#unbindReceiver', '取消绑定接收者', 'POST', '/allocation/group/unbindReceiver', '分账组', b'1', b'1', '分账组 取消绑定接收者', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138435, 'PayOrderController#allocation', '发起分账', 'POST', '/order/pay/allocation', '支付订单控制器', b'1', b'1', '支付订单控制器 发起分账', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138436, 'AllocationReceiverController#registerByGateway', '同步到三方支付系统中', 'POST', '/allocation/receiver/registerByGateway', '对账接收方控制器', b'1', b'1', '对账接收方控制器 同步到三方支付系统中', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.850000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138437, 'AllocationGroupController#unbindReceivers', '批量取消绑定接收者', 'POST', '/allocation/group/unbindReceivers', '分账组', b'1', b'1', '分账组 批量取消绑定接收者', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138438, 'AllocationOrderController#findDetailById', '查询明细详情', 'GET', '/order/allocation/detail/findById', '对账订单控制器', b'1', b'1', '对账订单控制器 查询明细详情', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.849000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138439, 'AllocationOrderController#findChannels', '获取可以分账的通道', 'GET', '/order/allocation/findChannels', '对账订单控制器', b'1', b'1', '对账订单控制器 获取可以分账的通道', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.849000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138440, 'SystemMonitorController#getSystemInfo', '获取系统消息', 'GET', '/monitor/system/getSystemInfo', '系统信息监控', b'1', b'1', '系统信息监控 获取系统消息', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138441, 'AllocationReceiverController#page', '分页', 'GET', '/allocation/receiver/page', '对账接收方控制器', b'1', b'1', '对账接收方控制器 分页', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.849000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138442, 'AllocationGroupController#clearDefault', '清除默认分账组', 'POST', '/allocation/group/clearDefault', '分账组', b'1', b'1', '分账组 清除默认分账组', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138443, 'AllocationGroupController#create', '创建', 'POST', '/allocation/group/create', '分账组', b'1', b'1', '分账组 创建', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138444, 'AllocationOrderController#finish', '分账完结', 'POST', '/order/allocation/finish', '对账订单控制器', b'1', b'1', '对账订单控制器 分账完结', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.848000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138445, 'UniPayController#allocationFinish', '分账完结接口', 'POST', '/unipay/allocationFinish', '统一支付接口', b'1', b'1', '统一支付接口 分账完结接口', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138446, 'AllocationOrderController#findById', '查询详情', 'GET', '/order/allocation/findById', '对账订单控制器', b'1', b'1', '对账订单控制器 查询详情', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.848000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138447, 'SystemMonitorController#getRedisInfo', '获取Redis信息', 'GET', '/monitor/system/getRedisInfo', '系统信息监控', b'1', b'1', '系统信息监控 获取Redis信息', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138448, 'AllocationReceiverController#removeByGateway', '从三方支付系统中删除', 'POST', '/allocation/receiver/removeByGateway', '对账接收方控制器', b'1', b'1', '对账接收方控制器 从三方支付系统中删除', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.848000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138449, 'AllocationReceiverController#findReceiverTypeByChannel', '根据通道获取分账接收方类型', 'GET', '/allocation/receiver/findReceiverTypeByChannel', '对账接收方控制器', b'1', b'1', '对账接收方控制器 根据通道获取分账接收方类型', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.847000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138450, 'AllocationGroupController#delete', '删除', 'POST', '/allocation/group/delete', '分账组', b'1', b'1', '分账组 删除', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138451, 'AllocationGroupController#update', '修改', 'POST', '/allocation/group/update', '分账组', b'1', b'1', '分账组 修改', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138452, 'AllocationReceiverController#add', '新增', 'POST', '/allocation/receiver/add', '对账接收方控制器', b'1', b'1', '对账接收方控制器 新增', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.847000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138453, 'AllocationGroupController#setDefault', '设置默认分账组', 'POST', '/allocation/group/setDefault', '分账组', b'1', b'1', '分账组 设置默认分账组', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138454, 'AllocationGroupController#findById', '查询详情', 'GET', '/allocation/group/findById', '分账组', b'1', b'1', '分账组 查询详情', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138455, 'AllocationReceiverController#update', '修改', 'POST', '/allocation/receiver/update', '对账接收方控制器', b'1', b'1', '对账接收方控制器 修改', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.847000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138456, 'AllocationReceiverController#delete', '删除', 'POST', '/allocation/receiver/delete', '对账接收方控制器', b'1', b'1', '对账接收方控制器 删除', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.846000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138457, 'AllocationReceiverController#findChannels', '获取可以分账的通道', 'GET', '/allocation/receiver/findChannels', '对账接收方控制器', b'1', b'1', '对账接收方控制器 获取可以分账的通道', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.846000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138458, 'AllocationReceiverController#findById', '查询详情', 'GET', '/allocation/receiver/findById', '对账接收方控制器', b'1', b'1', '对账接收方控制器 查询详情', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.846000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138459, 'AllocationGroupController#page', '分页', 'GET', '/allocation/group/page', '分账组', b'1', b'1', '分账组 分页', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138460, 'AllocationGroupController#bindReceivers', '批量绑定接收者', 'POST', '/allocation/group/bindReceivers', '分账组', b'1', b'1', '分账组 批量绑定接收者', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138461, 'AllocationGroupController#updateRate', '修改分账比例', 'POST', '/allocation/group/updateRate', '分账组', b'1', b'1', '分账组 修改分账比例', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138462, 'UniPayController#allocation', '开启分账接口', 'POST', '/unipay/allocation', '统一支付接口', b'1', b'1', '统一支付接口 开启分账接口', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-16 20:47:44.424000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138463, 'AllocationOrderController#findDetailsByOrderId', '分账明细列表', 'GET', '/order/allocation/detail/findAll', '对账订单控制器', b'1', b'1', '对账订单控制器 分账明细列表', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.846000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138464, 'AllocationOrderController#retryAllocation', '分账重试', 'POST', '/order/allocation/retry', '对账订单控制器', b'1', b'1', '对账订单控制器 分账重试', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.845000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780216505637138465, 'AllocationOrderController#page', '分页', 'GET', '/order/allocation/page', '对账订单控制器', b'1', b'1', '对账订单控制器 分页', 1399985191002447872, '2024-04-16 20:47:44.424000', 1399985191002447872, '2024-04-17 10:09:40.839000', b'1', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780417658844966912, 'PayGatewayNoticeController#wechatPayNotice', '微信消息通知', 'POST', '/gateway/notice/wechat', '三方支付网关消息通知', b'1', b'1', '三方支付网关消息通知 微信消息通知', 1399985191002447872, '2024-04-17 10:07:03.086000', 1399985191002447872, '2024-04-17 10:07:03.086000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780417658844966913, 'PayGatewayNoticeController#aliPayNotice', '支付宝消息通知', 'POST', '/gateway/notice/alipay', '三方支付网关消息通知', b'1', b'1', '三方支付网关消息通知 支付宝消息通知', 1399985191002447872, '2024-04-17 10:07:03.086000', 1399985191002447872, '2024-04-17 10:07:03.086000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063232, 'AllocationReceiverController#page', '分页', 'GET', '/allocation/receiver/page', '分账接收方控制器', b'1', b'1', '分账接收方控制器 分页', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063233, 'AllocationOrderController#finish', '分账完结', 'POST', '/order/allocation/finish', '分账订单控制器', b'1', b'1', '分账订单控制器 分账完结', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063234, 'AllocationReceiverController#delete', '删除', 'POST', '/allocation/receiver/delete', '分账接收方控制器', b'1', b'1', '分账接收方控制器 删除', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063235, 'AllocationReceiverController#add', '新增', 'POST', '/allocation/receiver/add', '分账接收方控制器', b'1', b'1', '分账接收方控制器 新增', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063236, 'AllocationReceiverController#findById', '查询详情', 'GET', '/allocation/receiver/findById', '分账接收方控制器', b'1', b'1', '分账接收方控制器 查询详情', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063237, 'AllocationOrderController#sync', '同步分账结果', 'POST', '/order/allocation/sync', '分账订单控制器', b'1', b'1', '分账订单控制器 同步分账结果', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063238, 'AllocationReceiverController#findReceiverTypeByChannel', '根据通道获取分账接收方类型', 'GET', '/allocation/receiver/findReceiverTypeByChannel', '分账接收方控制器', b'1', b'1', '分账接收方控制器 根据通道获取分账接收方类型', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063239, 'AllocationReceiverController#removeByGateway', '从三方支付系统中删除', 'POST', '/allocation/receiver/removeByGateway', '分账接收方控制器', b'1', b'1', '分账接收方控制器 从三方支付系统中删除', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063240, 'AllocationOrderController#page', '分页', 'GET', '/order/allocation/page', '分账订单控制器', b'1', b'1', '分账订单控制器 分页', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063241, 'AllocationOrderController#findChannels', '获取可以分账的通道', 'GET', '/order/allocation/findChannels', '分账订单控制器', b'1', b'1', '分账订单控制器 获取可以分账的通道', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063242, 'AllocationOrderController#findDetailsByOrderId', '分账明细列表', 'GET', '/order/allocation/detail/findAll', '分账订单控制器', b'1', b'1', '分账订单控制器 分账明细列表', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063243, 'AllocationReceiverController#registerByGateway', '同步到三方支付系统中', 'POST', '/allocation/receiver/registerByGateway', '分账接收方控制器', b'1', b'1', '分账接收方控制器 同步到三方支付系统中', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063244, 'AllocationReceiverController#findChannels', '获取可以分账的通道', 'GET', '/allocation/receiver/findChannels', '分账接收方控制器', b'1', b'1', '分账接收方控制器 获取可以分账的通道', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063245, 'AllocationOrderController#findById', '查询详情', 'GET', '/order/allocation/findById', '分账订单控制器', b'1', b'1', '分账订单控制器 查询详情', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063246, 'AllocationOrderController#findDetailById', '查询明细详情', 'GET', '/order/allocation/detail/findById', '分账订单控制器', b'1', b'1', '分账订单控制器 查询明细详情', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063247, 'AllocationReceiverController#update', '修改', 'POST', '/allocation/receiver/update', '分账接收方控制器', b'1', b'1', '分账接收方控制器 修改', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

INSERT INTO `dax-pay`.`iam_perm_path`(`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1780418342852063248, 'AllocationOrderController#retryAllocation', '分账重试', 'POST', '/order/allocation/retry', '分账订单控制器', b'1', b'1', '分账订单控制器 分账重试', 1399985191002447872, '2024-04-17 10:09:46.166000', 1399985191002447872, '2024-04-17 10:09:46.166000', b'0', 0);

UPDATE `dax-pay`.`iam_perm_path` SET `code` = 'TestController#wxcs', `name` = '微信回调测试', `request_type` = 'GET', `path` = '/test/wxcs/', `group_name` = '测试', `enable` = b'1', `generate` = b'1', `remark` = '测试 微信回调测试', `creator` = 1399985191002447872, `create_time` = '2024-02-13 14:55:54.138000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-04-16 20:47:44.531000', `deleted` = b'1', `version` = 0 WHERE `id` = 1757297527147974671;

UPDATE `dax-pay`.`iam_perm_path` SET `code` = 'TestController#wxcs', `name` = '微信回调测试', `request_type` = 'GET', `path` = '/test/wxcs', `group_name` = '测试', `enable` = b'1', `generate` = b'1', `remark` = '测试 微信回调测试', `creator` = 1399985191002447872, `create_time` = '2024-02-13 14:55:54.138000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-04-16 20:47:44.526000', `deleted` = b'1', `version` = 0 WHERE `id` = 1757297527147974672;

DELETE FROM `dax-pay`.`iam_role_menu` WHERE `id` = 1764945575888781312;

DELETE FROM `dax-pay`.`iam_role_menu` WHERE `id` = 1764945575888781313;

DELETE FROM `dax-pay`.`iam_role_menu` WHERE `id` = 1764945576350154752;

DELETE FROM `dax-pay`.`iam_role_menu` WHERE `id` = 1764945576350154753;

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732268605440, 1757297023118462976, 'dax-pay', 1775089099078553600);

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732268605441, 1757297023118462976, 'dax-pay', 1775089820368818176);

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732268605442, 1757297023118462976, 'dax-pay', 1775091561835450368);

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732268605443, 1757297023118462976, 'dax-pay', 1777688382748700672);

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732574789632, 1757298887092326400, 'dax-pay', 1775089099078553600);

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732574789633, 1757298887092326400, 'dax-pay', 1775089820368818176);

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732574789634, 1757298887092326400, 'dax-pay', 1775091561835450368);

INSERT INTO `dax-pay`.`iam_role_menu`(`id`, `role_id`, `client_code`, `permission_id`) VALUES (1780416732574789635, 1757298887092326400, 'dax-pay', 1777688382748700672);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855808, 1757297023118462976, 1780216505637138435);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855809, 1757297023118462976, 1780418342852063247);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855810, 1757297023118462976, 1780418342852063244);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855811, 1757297023118462976, 1780418342852063238);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855812, 1757297023118462976, 1780418342852063236);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855813, 1757297023118462976, 1780418342852063235);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855814, 1757297023118462976, 1780418342852063234);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855815, 1757297023118462976, 1780418342852063232);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855816, 1757297023118462976, 1780418342852063239);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855817, 1757297023118462976, 1780418342852063243);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855818, 1757297023118462976, 1780418342852063248);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855819, 1757297023118462976, 1780418342852063246);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855820, 1757297023118462976, 1780418342852063245);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855821, 1757297023118462976, 1780418342852063242);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855822, 1757297023118462976, 1780418342852063241);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855823, 1757297023118462976, 1780418342852063240);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855824, 1757297023118462976, 1780418342852063237);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836388855825, 1757297023118462976, 1780418342852063233);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177344, 1757298887092326400, 1780216505637138435);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177346, 1757298887092326400, 1780418342852063244);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177347, 1757298887092326400, 1780418342852063238);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177348, 1757298887092326400, 1780418342852063236);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177349, 1757298887092326400, 1780418342852063235);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177351, 1757298887092326400, 1780418342852063232);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177354, 1757298887092326400, 1780418342852063248);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177355, 1757298887092326400, 1780418342852063246);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177356, 1757298887092326400, 1780418342852063245);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177357, 1757298887092326400, 1780418342852063242);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177358, 1757298887092326400, 1780418342852063241);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177359, 1757298887092326400, 1780418342852063240);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177360, 1757298887092326400, 1780418342852063237);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780464836741177361, 1757298887092326400, 1780418342852063233);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395904, 1757297023118462976, 1780216505637138461);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395905, 1757297023118462976, 1780216505637138460);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395906, 1757297023118462976, 1780216505637138459);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395907, 1757297023118462976, 1780216505637138454);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395908, 1757297023118462976, 1780216505637138453);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395909, 1757297023118462976, 1780216505637138451);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395910, 1757297023118462976, 1780216505637138450);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395911, 1757297023118462976, 1780216505637138443);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395912, 1757297023118462976, 1780216505637138442);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395913, 1757297023118462976, 1780216505637138437);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395914, 1757297023118462976, 1780216505637138434);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020646395915, 1757297023118462976, 1780216505637138433);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020956774402, 1757298887092326400, 1780216505637138459);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020956774403, 1757298887092326400, 1780216505637138454);

INSERT INTO `dax-pay`.`iam_role_path`(`id`, `role_id`, `permission_id`) VALUES (1780466020956774411, 1757298887092326400, 1780216505637138433);

SET FOREIGN_KEY_CHECKS = 1;
