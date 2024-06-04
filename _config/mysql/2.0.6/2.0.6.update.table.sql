SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `iam_perm_menu`
    CHANGE COLUMN `admin` `internal` bit(1) NOT NULL COMMENT '系统菜单' AFTER `hidden_header_content`;

ALTER TABLE `pay_allocation_group` ADD COLUMN `group_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账组编码' AFTER `id`;

ALTER TABLE `pay_allocation_group` MODIFY COLUMN `default_group` bit(1) NULL DEFAULT NULL COMMENT '默认分账组' AFTER `channel`;

ALTER TABLE `pay_allocation_order` MODIFY COLUMN `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付订单标题' AFTER `out_order_no`;

ALTER TABLE `pay_allocation_order` MODIFY COLUMN `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果' AFTER `status`;

ALTER TABLE `pay_allocation_order_detail` ADD COLUMN `receiver_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账接收方编号' AFTER `receiver_id`;

CREATE TABLE `pay_allocation_order_extra`  (
                                                         `id` bigint(20) NOT NULL COMMENT '主键',
                                                         `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异步通知地址',
                                                         `attach` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户扩展参数',
                                                         `req_time` datetime NULL DEFAULT NULL COMMENT '请求时间，传输时间戳',
                                                         `client_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付终端ip',
                                                         `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                         `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                         `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                                         `last_modified_time` datetime NULL DEFAULT NULL COMMENT '最后修改时间',
                                                         `version` int(11) NOT NULL COMMENT '乐观锁',
                                                         `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账订单扩展' ROW_FORMAT = Dynamic;

ALTER TABLE `pay_allocation_receiver` ADD COLUMN `receiver_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账接收方编号' AFTER `id`;

ALTER TABLE `pay_allocation_receiver` DROP COLUMN `name`;

ALTER TABLE `pay_allocation_receiver` DROP COLUMN `sync`;

ALTER TABLE `pay_allocation_receiver` DROP COLUMN `remark`;

ALTER TABLE `pay_api_config` DROP COLUMN `res_sign`;

ALTER TABLE `pay_api_config` DROP COLUMN `notice_sign`;

ALTER TABLE `pay_api_config` DROP COLUMN `record`;

CREATE TABLE `pay_reconcile_out_trade`  (
                                                      `id` bigint(20) NOT NULL COMMENT '主键',
                                                      `reconcile_id` bigint(20) NULL DEFAULT NULL COMMENT '关联对账订单ID',
                                                      `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品名称',
                                                      `amount` int(11) NULL DEFAULT NULL COMMENT '交易金额',
                                                      `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易类型',
                                                      `trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地交易号',
                                                      `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道交易号',
                                                      `trade_time` datetime NULL DEFAULT NULL COMMENT '交易时间',
                                                      `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                      `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账-通道交易明细' ROW_FORMAT = Dynamic;

ALTER TABLE `pay_refund_order` MODIFY COLUMN `out_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道支付订单号' AFTER `biz_order_no`;

ALTER TABLE `pay_refund_order` MODIFY COLUMN `out_refund_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道退款交易号' AFTER `biz_refund_no`;

CREATE TABLE `pay_trade_flow_record`  (
                                                    `id` bigint(20) NOT NULL COMMENT '主键',
                                                    `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
                                                    `amount` int(11) NULL DEFAULT NULL COMMENT '金额',
                                                    `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
                                                    `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付通道',
                                                    `trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地交易号',
                                                    `biz_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户交易号',
                                                    `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道交易号',
                                                    `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '资金流水记录' ROW_FORMAT = Dynamic;

ALTER TABLE `pay_union_pay_config` MODIFY COLUMN `single_limit` int(11) NULL DEFAULT NULL COMMENT '支付限额' AFTER `seller`;

ALTER TABLE `pay_wechat_pay_config` MODIFY COLUMN `allocation` bit(1) NULL DEFAULT NULL COMMENT '是否支付分账' AFTER `sandbox`;

ALTER TABLE `pay_wechat_pay_config` MODIFY COLUMN `single_limit` int(11) NULL DEFAULT NULL COMMENT '支付限额' AFTER `pay_ways`;

DROP TABLE IF EXISTS `pay_reconcile_trade_detail`;

SET FOREIGN_KEY_CHECKS=1;
