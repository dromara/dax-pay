SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `base_dict` (`id`, `code`, `name`, `enable`, `group_tag`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797179034383134720, 'TradeFlowRecordType', '交易流水记录类型', b'1', '支付', '', 1399985191002447872, '2024-06-02 16:10:46', 1399985191002447872, '2024-06-02 16:10:46', 0, 0);

INSERT INTO `base_dict_item` (`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1795722875761156096, 1780163691808391168, 'AllocDetailResult', 'ignore', '忽略分账', b'1', 3.00, '', 1399985191002447872, '2024-05-29 15:44:31', 1399985191002447872, '2024-05-29 15:44:31', 0, 0);

INSERT INTO `base_dict_item` (`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1795722956774137856, 1777697358802530304, 'AllocOrderStatus', 'ignore', '忽略分账', b'1', 7.00, '', 1399985191002447872, '2024-05-29 15:44:50', 1399985191002447872, '2024-05-29 15:44:50', 0, 0);

INSERT INTO `base_dict_item` (`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1795730634367918080, 1751603996496453632, 'PaymentType', 'transfer', '转账', b'1', 3.00, '', 1399985191002447872, '2024-05-29 16:15:21', 1399985191002447872, '2024-05-29 16:15:21', 0, 0);

INSERT INTO `base_dict_item` (`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1795730676084465664, 1751603996496453632, 'PaymentType', 'allocation', '分账', b'1', 4.00, '', 1399985191002447872, '2024-05-29 16:15:31', 1399985191002447872, '2024-05-29 16:15:31', 0, 0);

INSERT INTO `base_dict_item` (`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797179153430065152, 1797179034383134720, 'TradeFlowRecordType', 'pay', '支付', b'1', 0.00, '', 1399985191002447872, '2024-06-02 16:11:15', 1399985191002447872, '2024-06-02 16:11:15', 0, 0);

INSERT INTO `base_dict_item` (`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797179194613936128, 1797179034383134720, 'TradeFlowRecordType', 'refund', '退款', b'1', 1.00, '', 1399985191002447872, '2024-06-02 16:11:25', 1399985191002447872, '2024-06-02 16:11:25', 0, 0);

INSERT INTO `base_dict_item` (`id`, `dict_id`, `dict_code`, `code`, `name`, `enable`, `sort_no`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797524426741882880, 1761434095349624832, 'ClientNoticeType', 'allocation', '分账', b'1', 3.00, '', 1399985191002447872, '2024-06-03 15:03:14', 1399985191002447872, '2024-06-03 15:03:14', 0, 0);

UPDATE `base_dict_item` SET `dict_id` = 1777697358802530304, `dict_code` = 'AllocOrderStatus', `code` = 'waiting', `name` = '待分账', `enable` = b'1', `sort_no` = 1.00, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-04-09 22:10:53', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-05-29 15:45:15', `deleted` = 1, `version` = 0 WHERE `id` = 1777700713809522688;

UPDATE `base_dict_item` SET `dict_id` = 1777697358802530304, `dict_code` = 'AllocOrderStatus', `code` = 'finish', `name` = '完结', `enable` = b'1', `sort_no` = 5.00, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-04-09 22:11:30', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-05-29 15:46:08', `deleted` = 0, `version` = 2 WHERE `id` = 1777700870613577728;

UPDATE `base_dict_item` SET `dict_id` = 1777697358802530304, `dict_code` = 'AllocOrderStatus', `code` = 'finish_failed', `name` = '完结失败', `enable` = b'1', `sort_no` = 6.00, `remark` = '', `creator` = 1399985191002447872, `create_time` = '2024-04-09 22:11:53', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-05-29 15:45:51', `deleted` = 0, `version` = 2 WHERE `id` = 1777700964087836672;

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1582633196587261952, 'dax-pay', 1582276341792985088, '代码生成', 'CodeGenList', NULL, b'0', '', b'0', b'0', '/modules/develop/codegen/CodeGenList.vue', NULL, '/develop/codegen', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-19 15:22:13', 1414143554414059520, '2022-10-19 15:23:17', 1, 0);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1582633307786649600, 'dax-pay', 1582276341792985088, '动态表单', 'DynamicFormList', NULL, b'0', '', b'0', b'0', '/modules/develop/dynamicform/DynamicFormList.vue', NULL, '/develop/form', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-19 15:22:39', 1414143554414059520, '2022-10-19 15:22:39', 0, 0);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1582633620321017856, 'dax-pay', 1582276341792985088, '动态数据源', 'DynamicDataSourceList', NULL, b'0', '', b'0', b'0', '/modules/develop/dynamicsource/DynamicDataSourceList.vue', NULL, '/develop/source', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2022-10-19 15:23:54', 1414143554414059520, '2022-10-19 15:23:54', 0, 0);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1629039360928075776, 'dax-pay', 1582276341792985088, '可视化大屏', 'ProjectInfoList', NULL, b'0', '', b'0', b'0', '/modules/develop/report/ProjectInfoList', NULL, '/develop/report', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-02-24 16:43:44', 1399985191002447872, '2024-05-09 19:26:57', 1, 1);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1635274568758435840, 'dax-pay', 1582276341792985088, '数据集管理', 'DataResultSqlList', NULL, b'0', '', b'0', b'0', '/modules/develop/dataresult/DataResultSqlList', NULL, '/develop/dataresult', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-03-13 21:40:14', 1399985191002447872, '2024-05-09 19:27:24', 3, 1);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1689181991598997504, 'dax-pay', 1582253152903843840, '敏感词管理', 'ChinaWord', NULL, b'0', '', b'0', b'0', '/modules/baseapi/chianword/ChinaWordList.vue', NULL, '/system/config/chinaword', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-08-09 15:49:05', 1414143554414059520, '2023-08-09 15:49:05', 0, 0);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1690324070514782208, 'dax-pay', 1582276341792985088, '通用模板', 'GeneralTemplateList', NULL, b'0', '', b'0', b'0', '/modules/develop/template/GeneralTemplateList', NULL, '/develop/template', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-08-12 19:27:18', 1414143554414059520, '2023-08-12 19:31:26', 3, 0);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1703665090038800384, 'dax-pay', 1582276341792985088, '在线SQL', 'SqlQueryInfo', NULL, b'0', '', b'0', b'0', '/modules/develop/query/SqlQueryInfo', NULL, '/develop/sqlquery', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1414143554414059520, '2023-09-18 14:59:45', 1399985191002447872, '2024-05-09 19:27:29', 0, 1);

INSERT INTO `iam_perm_menu` (`id`, `client_code`, `parent_id`, `title`, `name`, `perm_code`, `effect`, `icon`, `hidden`, `hide_children_in_menu`, `component`, `component_name`, `path`, `redirect`, `sort_no`, `menu_type`, `leaf`, `keep_alive`, `target_outside`, `hidden_header_content`, `internal`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1797178029448867840, 'dax-pay', 1744643265142165504, '交易流水', 'TradeFlowRecordList', NULL, b'0', '', b'0', b'0', 'payment/record/flow/TradeFlowRecordList', NULL, '/pay/record/flow', '', 0, 1, NULL, b'1', b'0', b'0', b'0', NULL, 1399985191002447872, '2024-06-02 16:06:47', 1399985191002447872, '2024-06-02 16:06:47', 0, 0);

UPDATE `iam_perm_menu` SET `client_code` = 'dax-pay', `parent_id` = 1775089099078553600, `title` = '分账订单', `name` = 'AllocationOrderList', `perm_code` = NULL, `effect` = b'0', `icon` = '', `hidden` = b'0', `hide_children_in_menu` = b'0', `component` = 'payment/allocation/order/AllocationOrderList', `component_name` = NULL, `path` = '/pay/allocation/order', `redirect` = '', `sort_no` = -1, `menu_type` = 1, `leaf` = NULL, `keep_alive` = b'1', `target_outside` = b'0', `hidden_header_content` = b'0', `internal` = b'0', `remark` = NULL, `creator` = 1399985191002447872, `create_time` = '2024-04-09 21:21:53', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-05-31 11:16:55', `version` = 5, `deleted` = 0 WHERE `id` = 1777688382748700672;

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066176, 'PayNoticeReceiverController#wechatPayNotice', '微信消息通知', 'POST', '/callback/notice/wechat', '执法通道网关消息通知', b'1', b'1', '执法通道网关消息通知 微信消息通知', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066177, 'AllocationReceiverController#existsByReceiverNo', '编码是否存在', 'GET', '/allocation/receiver/existsByReceiverNo', '分账接收方控制器', b'1', b'1', '分账接收方控制器 编码是否存在', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066178, 'UniAllocationController#start', '发起分账接口', 'POST', '/unipay/allocation/start', '分账控制器', b'1', b'1', '分账控制器 发起分账接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066179, 'UniPayController#transfer', '统一转账接口', 'POST', '/unipay/transfer', '统一支付接口', b'1', b'1', '统一支付接口 统一转账接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066180, 'UniQueryController#transferOrder', '转账订单查询接口', 'POST', '/unipay/query/transferOrder', '统一查询接口', b'1', b'1', '统一查询接口 转账订单查询接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066181, 'AllocationGroupController#existsByGroupNo', '编码是否存在', 'GET', '/allocation/group/existsByGroupNo', '分账组', b'1', b'1', '分账组 编码是否存在', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066182, 'UniQueryController#queryPayOrder', '支付订单查询接口', 'POST', '/unipay/query/payOrder', '统一查询接口', b'1', b'1', '统一查询接口 支付订单查询接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066183, 'UniQueryController#queryAllocationOrder', '分账订单查询接口', 'POST', '/unipay/query/allocationOrder', '统一查询接口', b'1', b'1', '统一查询接口 分账订单查询接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066184, 'AllocationOrderController#findByAllocNo', '查询扩展信息', 'GET', '/order/allocation/findByAllocNo', '分账订单控制器', b'1', b'1', '分账订单控制器 查询扩展信息', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066185, 'UniAllocationController#receiverAdd', '分账接收方添加接口', 'POST', '/unipay/allocation/receiver/add', '分账控制器', b'1', b'1', '分账控制器 分账接收方添加接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066186, 'UniPaySyncController#allocation', '分账同步接口', 'POST', '/unipay/sync/allocation', '统一同步接口', b'1', b'1', '统一同步接口 分账同步接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066187, 'UniReconcileController#down', '下载指定日期的资金流水', 'POST', '/unipay/reconcile/pay', '对账接口处理器', b'1', b'1', '对账接口处理器 下载指定日期的资金流水', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066188, 'RefundOrderController#findByRefundNo', '查询退款订单详情', 'GET', '/order/refund/findByRefundNo', '支付退款控制器', b'1', b'1', '支付退款控制器 查询退款订单详情', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066189, 'TradeFlowRecordController#findById', '查询单条', 'GET', '/record/flow/findById', '交易流水记录控制器', b'1', b'1', '交易流水记录控制器 查询单条', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066190, 'ClientNoticeReceiveController#allocation', '分账消息(对象)', 'POST', '/demo/callback/allocationObject', '回调测试', b'1', b'1', '回调测试 分账消息(对象)', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066191, 'UniPaySyncController#pay', '支付同步接口', 'POST', '/unipay/sync/pay', '统一同步接口', b'1', b'1', '统一同步接口 支付同步接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066192, 'UniPaySyncController#refund', '退款同步接口', 'POST', '/unipay/sync/refund', '统一同步接口', b'1', b'1', '统一同步接口 退款同步接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066193, 'UniPaySyncController#transfer', '转账同步接口', 'POST', '/unipay/sync/transfer', '统一同步接口', b'1', b'1', '统一同步接口 转账同步接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066194, 'ClientNoticeReceiveController#allocation', '分账消息', 'POST', '/demo/callback/allocation', '回调测试', b'1', b'1', '回调测试 分账消息', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066195, 'UniQueryController#queryAllocReceive', '分账接收方查询接口', 'POST', '/unipay/query/allocationReceiver', '统一查询接口', b'1', b'1', '统一查询接口 分账接收方查询接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066196, 'UniAllocationController#finish', '分账完结接口', 'POST', '/unipay/allocation/finish', '分账控制器', b'1', b'1', '分账控制器 分账完结接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066197, 'PayNoticeReceiverController#aliPayNotice', '支付宝消息通知', 'POST', '/callback/notice/alipay', '执法通道网关消息通知', b'1', b'1', '执法通道网关消息通知 支付宝消息通知', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066198, 'UniQueryController#queryRefundOrder', '退款订单查询接口', 'POST', '/unipay/query/refundOrder', '统一查询接口', b'1', b'1', '统一查询接口 退款订单查询接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066199, 'TradeFlowRecordController#page', '分页查询', 'GET', '/record/flow/page', '交易流水记录控制器', b'1', b'1', '交易流水记录控制器 分页查询', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

INSERT INTO `iam_perm_path` (`id`, `code`, `name`, `request_type`, `path`, `group_name`, `enable`, `generate`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `deleted`, `version`) VALUES (1797521957446066200, 'UniAllocationController#receiverRemove', '分账接收方删除接口', 'POST', '/unipay/allocation/receiver/remove', '分账控制器', b'1', b'1', '分账控制器 分账接收方删除接口', 1399985191002447872, '2024-06-03 14:53:25.655000', 1399985191002447872, '2024-06-03 14:53:25.655000', b'0', 0);

UPDATE `iam_perm_path` SET `code` = 'AllocationReceiverController#removeByGateway', `name` = '从三方支付系统中删除', `request_type` = 'POST', `path` = '/allocation/receiver/removeByGateway', `group_name` = '分账接收方控制器', `enable` = b'1', `generate` = b'1', `remark` = '分账接收方控制器 从三方支付系统中删除', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.069000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117562556420;

UPDATE `iam_perm_path` SET `code` = 'UniQueryController#queryRefundOrder', `name` = '退款订单查询接口', `request_type` = 'POST', `path` = '/uni/query/refundOrder', `group_name` = '统一查询接口', `enable` = b'1', `generate` = b'1', `remark` = '统一查询接口 退款订单查询接口', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.069000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117562556426;

UPDATE `iam_perm_path` SET `code` = 'UniPayController#syncPay', `name` = '支付同步接口', `request_type` = 'POST', `path` = '/unipay/syncPay', `group_name` = '统一支付接口', `enable` = b'1', `generate` = b'1', `remark` = '统一支付接口 支付同步接口', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.070000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117566750722;

UPDATE `iam_perm_path` SET `code` = 'PayGatewayNoticeController#wechatPayNotice', `name` = '微信消息通知', `request_type` = 'POST', `path` = '/gateway/notice/wechat', `group_name` = '三方支付网关消息通知', `enable` = b'1', `generate` = b'1', `remark` = '三方支付网关消息通知 微信消息通知', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.070000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117566750736;

UPDATE `iam_perm_path` SET `code` = 'AllocationReceiverController#registerByGateway', `name` = '同步到三方支付系统中', `request_type` = 'POST', `path` = '/allocation/receiver/registerByGateway', `group_name` = '分账接收方控制器', `enable` = b'1', `generate` = b'1', `remark` = '分账接收方控制器 同步到三方支付系统中', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.070000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117566750740;

UPDATE `iam_perm_path` SET `code` = 'RefundOrderController#findByRefundNo', `name` = '查询退款订单详情', `request_type` = 'GET', `path` = '/order/refund/findByOrderNo', `group_name` = '支付退款控制器', `enable` = b'1', `generate` = b'1', `remark` = '支付退款控制器 查询退款订单详情', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.070000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117566750763;

UPDATE `iam_perm_path` SET `code` = 'UniQueryController#queryPayOrder', `name` = '支付订单查询接口', `request_type` = 'POST', `path` = '/uni/query/payOrder', `group_name` = '统一查询接口', `enable` = b'1', `generate` = b'1', `remark` = '统一查询接口 支付订单查询接口', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.070000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117566750771;

UPDATE `iam_perm_path` SET `code` = 'UniPayController#syncRefund', `name` = '退款同步接口', `request_type` = 'POST', `path` = '/unipay/syncRefund', `group_name` = '统一支付接口', `enable` = b'1', `generate` = b'1', `remark` = '统一支付接口 退款同步接口', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.070000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.723000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117566750798;

UPDATE `iam_perm_path` SET `code` = 'UniPayController#allocation', `name` = '开启分账接口', `request_type` = 'POST', `path` = '/unipay/allocation', `group_name` = '统一支付接口', `enable` = b'1', `generate` = b'1', `remark` = '统一支付接口 开启分账接口', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.070000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.722000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117566750833;

UPDATE `iam_perm_path` SET `code` = 'PayGatewayNoticeController#aliPayNotice', `name` = '支付宝消息通知', `request_type` = 'POST', `path` = '/gateway/notice/alipay', `group_name` = '三方支付网关消息通知', `enable` = b'1', `generate` = b'1', `remark` = '三方支付网关消息通知 支付宝消息通知', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.071000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.722000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117570945107;

UPDATE `iam_perm_path` SET `code` = 'AllocationReceiverController#update', `name` = '修改', `request_type` = 'POST', `path` = '/allocation/receiver/update', `group_name` = '分账接收方控制器', `enable` = b'1', `generate` = b'1', `remark` = '分账接收方控制器 修改', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.071000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.722000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117570945181;

UPDATE `iam_perm_path` SET `code` = 'UniPayController#allocationFinish', `name` = '分账完结接口', `request_type` = 'POST', `path` = '/unipay/allocationFinish', `group_name` = '统一支付接口', `enable` = b'1', `generate` = b'1', `remark` = '统一支付接口 分账完结接口', `creator` = 1399985191002447872, `create_time` = '2024-05-13 19:16:54.071000', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 14:53:25.716000', `deleted` = b'1', `version` = 0 WHERE `id` = 1789978117570945220;

INSERT INTO `iam_role_menu` (`id`, `role_id`, `client_code`, `permission_id`) VALUES (1797522085254897664, 1757297023118462976, 'dax-pay', 1797178029448867840);

INSERT INTO `iam_role_menu` (`id`, `role_id`, `client_code`, `permission_id`) VALUES (1797522085414281216, 1757298887092326400, 'dax-pay', 1797178029448867840);

DELETE FROM `iam_role_path` WHERE `id` = 1789979321814343710;

DELETE FROM `iam_role_path` WHERE `id` = 1789979321814343757;

DELETE FROM `iam_role_path` WHERE `id` = 1789979321814343761;

DELETE FROM `iam_role_path` WHERE `id` = 1789979321814343763;

DELETE FROM `iam_role_path` WHERE `id` = 1789979321814343784;

DELETE FROM `iam_role_path` WHERE `id` = 1789979321814343785;

DELETE FROM `iam_role_path` WHERE `id` = 1789979322187636766;

DELETE FROM `iam_role_path` WHERE `id` = 1789979322187636813;

DELETE FROM `iam_role_path` WHERE `id` = 1789979322187636817;

DELETE FROM `iam_role_path` WHERE `id` = 1789979322187636819;

DELETE FROM `iam_role_path` WHERE `id` = 1789979322187636840;

DELETE FROM `iam_role_path` WHERE `id` = 1789979322187636841;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320001957888;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320001957889;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320001957890;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320001957891;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320001957892;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320102621184;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320102621185;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320102621186;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320102621187;

DELETE FROM `iam_role_path` WHERE `id` = 1790355320102621188;

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522413752786944, 1757297023118462976, 1797521957446066199);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522413752786945, 1757297023118462976, 1797521957446066189);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522413752786946, 1757297023118462976, 1797521957446066197);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522413752786947, 1757297023118462976, 1797521957446066176);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522413752786948, 1757297023118462976, 1797521957446066187);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522723091095552, 1757298887092326400, 1797521957446066199);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522723091095553, 1757298887092326400, 1797521957446066189);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522723091095554, 1757298887092326400, 1797521957446066197);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522723091095555, 1757298887092326400, 1797521957446066176);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797522723091095556, 1757298887092326400, 1797521957446066187);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523038569865216, 1757297023118462976, 1797521957446066184);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523038569865217, 1757297023118462976, 1797521957446066181);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523038733443072, 1757298887092326400, 1797521957446066184);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523038733443073, 1757298887092326400, 1797521957446066181);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523166064123904, 1757297023118462976, 1797521957446066177);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523166223507456, 1757298887092326400, 1797521957446066177);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523263875293184, 1757297023118462976, 1789978117570945272);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523263875293185, 1757297023118462976, 1789978117570945194);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523263875293186, 1757297023118462976, 1789978117570945175);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523263875293187, 1757297023118462976, 1789978117570945164);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523263875293188, 1757297023118462976, 1789978117566750801);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523264022093824, 1757298887092326400, 1789978117570945272);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523264022093825, 1757298887092326400, 1789978117570945194);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523264022093826, 1757298887092326400, 1789978117570945175);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523264022093827, 1757298887092326400, 1789978117570945164);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523264022093828, 1757298887092326400, 1789978117566750801);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523482968956928, 1757297023118462976, 1797521957446066188);

INSERT INTO `iam_role_path` (`id`, `role_id`, `permission_id`) VALUES (1797523483111563264, 1757298887092326400, 1797521957446066188);

DELETE FROM `pay_api_config` WHERE `id` = 1;

DELETE FROM `pay_api_config` WHERE `id` = 3;

DELETE FROM `pay_api_config` WHERE `id` = 4;

DELETE FROM `pay_api_config` WHERE `id` = 6;

DELETE FROM `pay_api_config` WHERE `id` = 7;

DELETE FROM `pay_api_config` WHERE `id` = 8;

DELETE FROM `pay_api_config` WHERE `id` = 9;

DELETE FROM `pay_api_config` WHERE `id` = 10;

DELETE FROM `pay_api_config` WHERE `id` = 11;

DELETE FROM `pay_api_config` WHERE `id` = 12;

DELETE FROM `pay_api_config` WHERE `id` = 13;

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (100, 'pay', '/unipay/pay', '统一支付接口	', b'1', b'1', b'1', 'http://pay1.bootx.cn/server/demo/callback/payObject', b'1', NULL, 0, '2024-01-03 14:25:48', 1399985191002447872, '2024-03-11 17:31:41', 11, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (200, 'close', '/unipay/close', '支付关闭接口', b'0', b'1', b'1', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (300, 'refund', '/unipay/refund', '统一退款接口', b'1', b'1', b'1', 'http://pay1.bootx.cn/server/demo/callback/refundObject', b'1', NULL, 0, '2024-01-03 14:25:48', 1399985191002447872, '2024-05-16 21:59:19', 7, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (400, 'allocation', '/unipay/allocation/start', '统一分账接口', b'1', b'1', b'1', 'http://pay1.bootx.cn/server/demo/callback/allocationObject', b'1', NULL, 0, '2024-01-03 14:25:48', 1399985191002447872, '2024-05-30 19:36:35', 1, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (450, 'allocationFinish', '/unipay/allocation/finish', '分账完结接口', b'0', b'1', b'1', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (451, 'allocationReceiverAdd', '/unipay/allocation/receiver/add', '分账接收方添加接口', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (452, 'allocationReceiverRemove', '/unipay/allocation/receiver/remove', '分账接收方删除接口', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (500, 'transfer', '/unipay/transfer', '统一转账接口', b'1', b'1', b'1', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (600, 'syncPay', '/unipay/sync/pay', '支付同步接口', b'0', b'1', b'1', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (700, 'syncRefund', '/unipay/sync/refund', '退款同步接口', b'0', b'1', b'1', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (801, 'syncTransfer', '/unipay/sync/transfer', '转账同步接口', b'0', b'1', b'1', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (805, 'syncAllocation', '/unipay/sync/allocation', '分账同步接口', b'0', b'1', b'1', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1000, 'queryPayOrder', '/unipay/query/payOrder', '支付订单查询接口', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1100, 'queryRefundOrder', '/unipay/query/refundOrder', '退款订单查询接口', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1150, 'queryAllocationOrder', '/unipay/query/allocationOrder', '分账订单查询接口', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1175, 'queryAllocationReceiver', '/unipay/query/allocationReceiver', '分账接收方查询接口', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1176, 'queryTransferOrder', '/unipay/query/transferOrder', '转账订单查询接口', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1200, 'getWxAuthUrl', '/unipay/assist/getWxAuthUrl', '获取微信OAuth2授权链接', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `pay_api_config` (`id`, `code`, `api`, `name`, `notice_support`, `enable`, `notice`, `notice_url`, `req_sign`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1300, 'getWxAccessToken', '/unipay/assist/getWxAccessToken', '获取微信AccessToken', b'0', b'1', b'0', NULL, b'1', NULL, 0, '2024-01-03 14:25:48', 0, '2024-01-03 14:25:53', 0, b'0');

INSERT INTO `starter_quartz_job` (`id`, `name`, `job_class_name`, `cron`, `parameter`, `state`, `remark`, `creator`, `create_time`, `last_modifier`, `last_modified_time`, `version`, `deleted`) VALUES (1797588916837670912, '支付单超时检测(数据库)', 'cn.daxpay.single.service.task.PayExpiredByDbTimeTask', '0/20 * * * * ? *', '', 0, '检测超时的支付单, 超时后调用同步事件状态修复', 1399985191002447872, '2024-06-03 19:19:30', 1399985191002447872, '2024-06-03 19:22:28', 5, 0);

UPDATE `starter_quartz_job` SET `name` = '支付单超时检测(Redis)', `job_class_name` = 'cn.daxpay.single.service.task.PayExpiredTimeTask', `cron` = '0/5 * * * * ? *', `parameter` = NULL, `state` = 0, `remark` = '检测超时的支付单, 超时后调用同步事件状态修复', `creator` = 1399985191002447872, `create_time` = '2022-07-12 22:00:39', `last_modifier` = 1399985191002447872, `last_modified_time` = '2024-06-03 19:18:14', `version` = 9, `deleted` = 0 WHERE `id` = 1546857070483939328;

SET FOREIGN_KEY_CHECKS = 1;
