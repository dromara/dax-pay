SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `pay_client_notice_task` MODIFY COLUMN `order_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单状态' AFTER `notice_type`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `mach_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户号' AFTER `id`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `enable` bit(1) NULL DEFAULT NULL COMMENT '是否启用' AFTER `mach_id`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `server_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付网关地址' AFTER `enable`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异步通知路径' AFTER `server_url`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `pay_ways` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '可用支付方式' AFTER `notify_url`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `pay_ways`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `seller` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户收款账号' AFTER `remark`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `sign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名类型' AFTER `seller`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `cert_sign` bit(1) NULL DEFAULT NULL COMMENT '是否为证书签名' AFTER `sign_type`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `key_private_cert` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '应用私钥证书' AFTER `cert_sign`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `key_private_cert_pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '私钥证书对应的密码' AFTER `key_private_cert`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `acp_middle_cert` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '中级证书' AFTER `key_private_cert_pwd`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `acp_root_cert` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '根证书' AFTER `acp_middle_cert`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `sandbox` bit(1) NULL DEFAULT NULL COMMENT '是否沙箱环境' AFTER `acp_root_cert`;

ALTER TABLE `pay_union_pay_config` ADD COLUMN `return_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '同步通知页面路径' AFTER `sandbox`;

ALTER TABLE `pay_union_pay_record` DROP INDEX `payment_id`;

ALTER TABLE `pay_union_pay_record` DROP INDEX `business_no`;

ALTER TABLE `pay_union_pay_record` ADD COLUMN `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题' AFTER `amount`;

ALTER TABLE `pay_union_pay_record` ADD COLUMN `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型' AFTER `title`;

ALTER TABLE `pay_union_pay_record` ADD COLUMN `order_id` bigint(20) NULL DEFAULT NULL COMMENT '本地订单号' AFTER `type`;

ALTER TABLE `pay_union_pay_record` ADD COLUMN `gateway_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网关订单号' AFTER `order_id`;

ALTER TABLE `pay_union_pay_record` ADD COLUMN `gateway_time` datetime(0) NULL DEFAULT NULL COMMENT '网关完成时间' AFTER `gateway_order_no`;

ALTER TABLE `pay_union_pay_record` ADD COLUMN `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID' AFTER `gateway_time`;

ALTER TABLE `pay_union_pay_record` ADD COLUMN `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间' AFTER `creator`;

ALTER TABLE `pay_union_pay_record` MODIFY COLUMN `amount` int(11) NULL DEFAULT NULL COMMENT '金额' AFTER `id`;

ALTER TABLE `pay_union_pay_record` DROP COLUMN `payment_id`;

ALTER TABLE `pay_union_pay_record` DROP COLUMN `business_no`;

ALTER TABLE `pay_union_pay_record` DROP COLUMN `refundable_balance`;

ALTER TABLE `pay_union_pay_record` DROP COLUMN `status`;

ALTER TABLE `pay_union_pay_record` DROP COLUMN `pay_time`;

SET FOREIGN_KEY_CHECKS=1;
