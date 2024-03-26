SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `pay_alipay_config` ADD COLUMN `single_limit` int(11) NULL DEFAULT NULL COMMENT '支付限额' AFTER `deleted`;

ALTER TABLE `pay_alipay_config` ADD COLUMN `alipay_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合作者身份ID' AFTER `single_limit`;

ALTER TABLE `pay_channel_order` MODIFY COLUMN `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付完成时间' AFTER `status`;

ALTER TABLE `pay_platform_config` ADD COLUMN `limit_amount` int(11) NULL DEFAULT NULL COMMENT '支付限额' AFTER `deleted`;

ALTER TABLE `pay_platform_config` MODIFY COLUMN `return_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '同步支付跳转地址' AFTER `notify_url`;

CREATE TABLE `pay_transfer_order`  (
                                                   `id` bigint(20) NOT NULL COMMENT '主键',
                                                   `out_trade_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                                   `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                                   `amount` int(11) NULL DEFAULT NULL,
                                                   `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                                   `payer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                                   `payee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                                   `success_time` datetime(0) NULL DEFAULT NULL,
                                                   `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                   `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                                   `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                                   `version` int(11) NOT NULL COMMENT '乐观锁',
                                                   `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                                   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '转账订单' ROW_FORMAT = Dynamic;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `single_limit` int(11) NULL DEFAULT NULL COMMENT '支付限额' AFTER `deleted`;

CREATE TABLE `pay_union_reconcile_bill_detail`  (
                                                                `record_order_id` bigint(20) NULL DEFAULT NULL COMMENT '关联对账订单ID',
                                                                `trade_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易代码',
                                                                `txn_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易传输时间',
                                                                `txn_amt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易金额',
                                                                `query_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询流水号',
                                                                `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户订单号'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '云闪付业务明细对账单' ROW_FORMAT = Dynamic;

ALTER TABLE `pay_wechat_pay_config` ADD COLUMN `single_limit` int(11) NULL DEFAULT NULL COMMENT '支付限额' AFTER `deleted`;

SET FOREIGN_KEY_CHECKS=1;
