/*
 表结构更新脚本
*/
SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE `cash_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `amount` int(11) NULL DEFAULT NULL COMMENT '金额',
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端ip',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '现金记录' ROW_FORMAT = DYNAMIC;

ALTER TABLE `pay_alipay_config` MODIFY COLUMN `server_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付网关地址' AFTER `return_url`;

ALTER TABLE `pay_alipay_config` MODIFY COLUMN `sign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名类型 RSA2' AFTER `auth_type`;

CREATE TABLE `pay_alipay_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `amount` int(11) NULL DEFAULT NULL COMMENT '金额',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '本地订单号',
  `gateway_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网关订单号',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '支付宝流水记录' ROW_FORMAT = DYNAMIC;

ALTER TABLE `pay_api_config` MODIFY COLUMN `notice_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认通知地址' AFTER `notice`;

ALTER TABLE `pay_api_config` MODIFY COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注' AFTER `deleted`;

ALTER TABLE `pay_api_config` DROP COLUMN `only_async_notice`;

CREATE TABLE `pay_cash_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `enable` bit(1) NULL DEFAULT NULL COMMENT '是否启用',
  `single_limit` int(11) NULL DEFAULT NULL COMMENT '单次支付最多多少金额 ',
  `pay_ways` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可用支付方式',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '乐观锁',
  `deleted` bit(1) NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '现金支付配置' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_client_notice_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `task_id` bigint(20) NULL DEFAULT NULL COMMENT '任务ID',
  `req_count` int(11) NULL DEFAULT NULL COMMENT '请求次数',
  `success` bit(1) NULL DEFAULT NULL COMMENT '发送是否成功',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送类型',
  `error_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息通知任务记录' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_client_notice_task`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '本地订单ID',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息类型',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息内容',
  `success` bit(1) NULL DEFAULT NULL COMMENT '是否发送成功',
  `send_count` int(11) NULL DEFAULT NULL COMMENT '发送次数',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送地址',
  `latest_time` datetime(0) NULL DEFAULT NULL COMMENT '最后发送时间',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '乐观锁',
  `deleted` bit(1) NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息通知任务' ROW_FORMAT = DYNAMIC;

ALTER TABLE `pay_order` ADD COLUMN `close_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间' AFTER `pay_time`;

ALTER TABLE `pay_order_extra` ADD COLUMN `req_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求链路ID' AFTER `return_url`;

ALTER TABLE `pay_order_extra` ADD COLUMN `notice_sign` bit(1) NULL DEFAULT NULL COMMENT '回调通知时是否需要进行签名' AFTER `req_id`;

ALTER TABLE `pay_order_extra` ADD COLUMN `req_sign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名类型' AFTER `attach`;

ALTER TABLE `pay_order_extra` ADD COLUMN `req_sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名' AFTER `req_sign_type`;

ALTER TABLE `pay_order_extra` MODIFY COLUMN `return_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '同步跳转地址' AFTER `description`;

ALTER TABLE `pay_order_extra` MODIFY COLUMN `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异步通知地址，以最后一次为准' AFTER `notice_sign`;

ALTER TABLE `pay_order_extra` MODIFY COLUMN `client_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付终端ip' AFTER `req_time`;

ALTER TABLE `pay_order_extra` DROP COLUMN `not_notify`;

ALTER TABLE `pay_order_extra` DROP COLUMN `sign_type`;

ALTER TABLE `pay_order_extra` DROP COLUMN `sign`;

ALTER TABLE `pay_order_extra` DROP COLUMN `api_version`;

ALTER TABLE `pay_platform_config` MODIFY COLUMN `order_timeout` int(11) NULL DEFAULT NULL COMMENT '订单默认超时时间(分钟)' AFTER `website_url`;

ALTER TABLE `pay_refund_order` MODIFY COLUMN `payment_id` bigint(20) NULL DEFAULT NULL COMMENT '原支付id' AFTER `id`;

ALTER TABLE `pay_refund_order` MODIFY COLUMN `business_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原支付业务号' AFTER `payment_id`;

ALTER TABLE `pay_refund_order` MODIFY COLUMN `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原支付标题' AFTER `business_no`;

ALTER TABLE `pay_refund_order` DROP COLUMN `req_id`;

ALTER TABLE `pay_refund_order` DROP COLUMN `client_ip`;

CREATE TABLE `pay_refund_order_extra`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `notice_sign` bit(1) NULL DEFAULT NULL COMMENT '回调通知时是否需要进行签名',
  `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '异步通知地址',
  `attach` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户扩展参数',
  `req_sign_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求签名值',
  `req_sign` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求签名值',
  `req_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求链路ID',
  `req_time` datetime(0) NULL DEFAULT NULL COMMENT '请求时间，传输时间戳',
  `client_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付终端ip',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '乐观锁',
  `deleted` bit(1) NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '退款订单扩展信息' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_union_pay_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `payment_id` bigint(20) NULL DEFAULT NULL COMMENT '交易记录ID',
  `business_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联的业务号',
  `amount` int(11) NULL DEFAULT NULL COMMENT '交易金额',
  `refundable_balance` int(11) NULL DEFAULT NULL COMMENT '可退款金额',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付状态',
  `pay_time` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `payment_id`(`payment_id`) USING BTREE COMMENT '交易记录ID',
  INDEX `business_no`(`business_no`) USING BTREE COMMENT '业务号索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '云闪付流水记录' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `pay_voucher`;
CREATE TABLE `pay_voucher`  (
                                `id` bigint(20) NOT NULL COMMENT '主键',
                                `card_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '卡号',
                                `face_value` int(11) NULL DEFAULT NULL COMMENT '面值',
                                `balance` int(11) NULL DEFAULT NULL COMMENT '余额',
                                `enduring` bit(1) NULL DEFAULT NULL COMMENT '是否长期有效',
                                `start_time` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
                                `end_time` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
                                `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
                                `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                `version` int(11) NOT NULL COMMENT '乐观锁',
                                `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `card_no`(`card_no`) USING BTREE COMMENT '卡号索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '储值卡' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_voucher_config`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `enable` bit(1) NULL DEFAULT NULL COMMENT '是否启用',
  `single_limit` int(11) NULL DEFAULT NULL COMMENT '单次支付最多多少金额 ',
  `pay_ways` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可用支付方式',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
  `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
  `version` int(11) NOT NULL COMMENT '乐观锁',
  `deleted` bit(1) NOT NULL COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '储值卡配置' ROW_FORMAT = DYNAMIC;

CREATE TABLE `pay_voucher_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `voucher_id` bigint(20) NULL DEFAULT NULL COMMENT '储值卡id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `amount` int(11) NULL DEFAULT NULL COMMENT '金额',
  `old_amount` int(11) NULL DEFAULT NULL COMMENT '变动之前的金额',
  `new_amount` int(11) NULL DEFAULT NULL COMMENT '变动之后的金额',
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端ip',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `voucher_id`(`voucher_id`) USING BTREE COMMENT '储值卡ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '储值卡记录' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `pay_wallet`;
CREATE TABLE `pay_wallet`  (
                               `id` bigint(20) NOT NULL COMMENT '主键',
                               `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联用户id',
                               `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '钱包名称',
                               `balance` int(11) NULL DEFAULT NULL COMMENT '余额',
                               `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
                               `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                               `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                               `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                               `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                               `version` int(11) NOT NULL COMMENT '乐观锁',
                               `deleted` bit(1) NOT NULL COMMENT '删除标志',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `用户ID索引`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `pay_wallet_config`;

CREATE TABLE `pay_wallet_config`  (
                                      `id` bigint(20) NOT NULL COMMENT '主键',
                                      `enable` bit(1) NULL DEFAULT NULL COMMENT '是否启用',
                                      `single_limit` int(11) NULL DEFAULT NULL COMMENT '单次支付最多多少金额 ',
                                      `pay_ways` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可用支付方式',
                                      `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                      `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
                                      `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                                      `last_modifier` bigint(20) NULL DEFAULT NULL COMMENT '最后修者ID',
                                      `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后修改时间',
                                      `version` int(11) NOT NULL COMMENT '乐观锁',
                                      `deleted` bit(1) NOT NULL COMMENT '删除标志',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包配置' ROW_FORMAT = DYNAMIC;


CREATE TABLE `pay_wallet_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `wallet_id` bigint(20) NULL DEFAULT NULL COMMENT '钱包id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `amount` int(11) NULL DEFAULT NULL COMMENT '金额',
  `old_amount` int(11) NULL DEFAULT NULL COMMENT '变动之前的金额',
  `new_amount` int(11) NULL DEFAULT NULL COMMENT '变动之后的金额',
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易订单号',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终端ip',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `wallet_id`(`wallet_id`) USING BTREE COMMENT '钱包ID'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '钱包记录' ROW_FORMAT = DYNAMIC;

ALTER TABLE `pay_wechat_pay_config` MODIFY COLUMN `api_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接口版本' AFTER `deleted`;

CREATE TABLE `pay_wechat_pay_record`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `amount` int(11) NULL DEFAULT NULL COMMENT '金额',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '本地订单号',
  `gateway_order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '网关订单号',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信支付记录' ROW_FORMAT = DYNAMIC;

CREATE TABLE `starter_file_data`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `base64` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT 'base64方式存储',
  `data` longblob NULL COMMENT '数据方式存储',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '上传文件数据' ROW_FORMAT = DYNAMIC;

DROP TABLE `pay_cash_pay_order`;

DROP TABLE `pay_union_pay_order`;

DROP TABLE `pay_voucher_log`;

DROP TABLE `pay_voucher_pay_order`;

DROP TABLE `pay_wallet_log`;

DROP TABLE `pay_wallet_pay_order`;

SET FOREIGN_KEY_CHECKS=1;
