SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `pay_alipay_reconcile_bill_detail` MODIFY COLUMN `create_time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建时间' AFTER `subject`;

ALTER TABLE `pay_alipay_record` ADD COLUMN `gateway_time` datetime(0) NULL DEFAULT NULL COMMENT '网关完成时间' AFTER `create_time`;

ALTER TABLE `pay_client_notice_record` ADD COLUMN `send_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送类型' AFTER `create_time`;

ALTER TABLE `pay_client_notice_record` DROP COLUMN `type`;

ALTER TABLE `pay_client_notice_task` ADD COLUMN `notice_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型' AFTER `deleted`;

ALTER TABLE `pay_client_notice_task` ADD COLUMN `order_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类型' AFTER `notice_type`;

ALTER TABLE `pay_client_notice_task` DROP COLUMN `type`;

ALTER TABLE `pay_order` MODIFY COLUMN `close_time` datetime(0) NULL DEFAULT NULL COMMENT '关闭时间' AFTER `pay_time`;

ALTER TABLE `pay_platform_config` MODIFY COLUMN `order_timeout` int(11) NULL DEFAULT NULL COMMENT '订单默认超时时间(分钟)' AFTER `return_url`;

ALTER TABLE `pay_reconcile_detail` ADD COLUMN `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '本地订单ID' AFTER `type`;

ALTER TABLE `pay_reconcile_detail` ADD COLUMN `order_time` datetime(0) NULL DEFAULT NULL COMMENT '订单时间' AFTER `gateway_order_no`;

ALTER TABLE `pay_reconcile_detail` DROP COLUMN `payment_id`;

ALTER TABLE `pay_reconcile_detail` DROP COLUMN `refund_id`;

CREATE TABLE `pay_reconcile_diff_record`  (
                                                          `id` bigint(20) NOT NULL COMMENT '主键',
                                                          `record_id` bigint(20) NULL DEFAULT NULL COMMENT '对账单ID',
                                                          `detail_id` bigint(20) NULL DEFAULT NULL COMMENT '对账单明细ID',
                                                          `order_id` bigint(20) NULL DEFAULT NULL COMMENT '本地订单id',
                                                          `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单标题',
                                                          `order_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类型',
                                                          `diff_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '差异类型',
                                                          `diffs` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '差异内容',
                                                          `gateway_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网关订单号',
                                                          `amount` int(11) NULL DEFAULT NULL COMMENT '交易金额',
                                                          `order_time` datetime(0) NULL DEFAULT NULL COMMENT '订单时间',
                                                          `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                          `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                          `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                                          `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                                          `version` int(11) NOT NULL COMMENT '乐观锁',
                                                          `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '对账差异单' ROW_FORMAT = Dynamic;

ALTER TABLE `pay_wechat_pay_config` MODIFY COLUMN `api_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口版本' AFTER `return_url`;

ALTER TABLE `pay_wechat_pay_record` ADD COLUMN `gateway_time` datetime(0) NULL DEFAULT NULL COMMENT '网关完成时间' AFTER `create_time`;

SET FOREIGN_KEY_CHECKS=1;
