SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `pay_alipay_config` ADD COLUMN `allocation` bit(1) NULL DEFAULT NULL COMMENT '是否支付分账' AFTER `single_limit`;

ALTER TABLE `pay_alipay_config` MODIFY COLUMN `alipay_user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '合作者身份ID' AFTER `pay_ways`;

ALTER TABLE `pay_alipay_config` MODIFY COLUMN `single_limit` int(11) NULL DEFAULT NULL COMMENT '支付限额' AFTER `alipay_user_id`;

CREATE TABLE `pay_allocation_group`  (
                                                     `id` bigint(20) NOT NULL COMMENT '主键',
                                                     `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                                                     `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '通道',
                                                     `total_rate` int(11) NULL DEFAULT NULL COMMENT '总分账比例(万分之多少)',
                                                     `default_group` bit(1) NULL DEFAULT NULL COMMENT '默认分账组',
                                                     `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                     `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                     `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                                     `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                                     `version` int(11) NOT NULL COMMENT '乐观锁',
                                                     `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                                     PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账组' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_allocation_group_receiver`  (
                                                              `id` bigint(20) NOT NULL COMMENT '主键',
                                                              `group_id` bigint(20) NULL DEFAULT NULL COMMENT '分账组ID',
                                                              `receiver_id` bigint(20) NULL DEFAULT NULL COMMENT '接收者ID',
                                                              `rate` int(11) NULL DEFAULT NULL COMMENT '分账比例(万分之多少)',
                                                              `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                              `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账接收组关系' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_allocation_order`  (
                                                     `id` bigint(20) NOT NULL COMMENT '主键',
                                                     `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账订单号',
                                                     `allocation_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账单号',
                                                     `payment_id` bigint(20) NULL DEFAULT NULL COMMENT '支付订单ID',
                                                     `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                                     `gateway_pay_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网关支付订单号',
                                                     `gateway_allocation_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网关分账单号',
                                                     `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属通道',
                                                     `amount` int(11) NULL DEFAULT NULL COMMENT '总分账金额',
                                                     `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账描述',
                                                     `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
                                                     `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账处理结果',
                                                     `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误原因',
                                                     `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                     `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                                     `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                                     `version` int(11) NOT NULL COMMENT '乐观锁',
                                                     `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                                     PRIMARY KEY (`id`) USING BTREE,
                                                     UNIQUE INDEX `allocation_no`(`allocation_no`) USING BTREE COMMENT '分账单号索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账订单' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_allocation_order_detail`  (
                                                            `id` bigint(20) NOT NULL COMMENT '主键',
                                                            `allocation_id` bigint(20) NULL DEFAULT NULL COMMENT '分账订单ID',
                                                            `receiver_id` bigint(20) NULL DEFAULT NULL COMMENT '接收者ID',
                                                            `rate` int(11) NULL DEFAULT NULL COMMENT '分账比例(万分之多少)',
                                                            `amount` int(11) NULL DEFAULT NULL COMMENT '分账金额',
                                                            `receiver_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账接收方类型',
                                                            `receiver_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收方账号',
                                                            `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收方姓名',
                                                            `result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账结果',
                                                            `error_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误代码',
                                                            `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误原因',
                                                            `finish_time` datetime(0) NULL DEFAULT NULL COMMENT '分账完成时间',
                                                            `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                            `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                            `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                                            `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                                            `version` int(11) NOT NULL COMMENT '乐观锁',
                                                            `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账订单明细' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_allocation_receiver`  (
                                                        `id` bigint(20) NOT NULL COMMENT '主键',
                                                        `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号别名',
                                                        `channel` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属通道',
                                                        `receiver_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账接收方类型',
                                                        `receiver_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收方账号',
                                                        `receiver_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接收方姓名',
                                                        `relation_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账关系类型',
                                                        `relation_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关系名称',
                                                        `sync` bit(1) NULL DEFAULT NULL COMMENT '是否已经同步到网关',
                                                        `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                        `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                                        `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                                        `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                                        `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                                        `version` int(11) NOT NULL COMMENT '乐观锁',
                                                        `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                                        PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分账接收方' ROW_FORMAT = DYNAMIC;

ALTER TABLE `pay_order` ADD COLUMN `allocation` bit(1) NULL DEFAULT NULL COMMENT '是否需要分账' AFTER `deleted`;

ALTER TABLE `pay_order` ADD COLUMN `allocation_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分账状态' AFTER `allocation`;

ALTER TABLE `pay_wechat_pay_config` ADD COLUMN `allocation` bit(1) NULL DEFAULT NULL COMMENT '是否支付分账' AFTER `single_limit`;

SET FOREIGN_KEY_CHECKS=1;
