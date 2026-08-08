-- =====================================================================
-- 转账收款人敏感字段加密迁移检查与说明
-- =====================================================================
-- 背景: pay_transfer_order_{alipay,douyin,wechat} 三表的收款人账号/姓名字段
-- 已接入 DataEncryptTypeHandler (AES-256-GCM), 密文格式 v{version}:{base64}。
--
-- SecureAesGcmEncryptor.decrypt 已做容错: 非密文格式(历史明文)原样透传返回,
-- 不会变 null, 故上线零风险, 历史数据可正常读取。迁移仅用于"把明文也加密落库",
-- 属合规优化, 非上线阻塞项。
--
-- 迁移方式: AES-GCM 加密需随机 IV + 密钥版本前缀, 纯 SQL 无法完成,
-- 必须通过应用层(MyBatis updateById)触发 TypeHandler 加密写入。
-- 迁移完成后, 下方检查语句查询结果应均为 0。
-- =====================================================================

-- 1. 检查历史明文数量(未加密 = 不以 v 开头且非空)

-- 支付宝转账单: 收款人账号 + 收款人姓名
SELECT 'pay_transfer_order_alipay.payee_account' AS field,
       count(*) AS plaintext_count
FROM pay_transfer_order_alipay
WHERE payee_account IS NOT NULL AND payee_account NOT LIKE 'v%'
UNION ALL
SELECT 'pay_transfer_order_alipay.payee_name',
       count(*)
FROM pay_transfer_order_alipay
WHERE payee_name IS NOT NULL AND payee_name NOT LIKE 'v%';

-- 抖音转账单: 收款人账号 + 收款人姓名
SELECT 'pay_transfer_order_douyin.payee_account' AS field,
       count(*) AS plaintext_count
FROM pay_transfer_order_douyin
WHERE payee_account IS NOT NULL AND payee_account NOT LIKE 'v%'
UNION ALL
SELECT 'pay_transfer_order_douyin.payee_name',
       count(*)
FROM pay_transfer_order_douyin
WHERE payee_name IS NOT NULL AND payee_name NOT LIKE 'v%';

-- 微信转账单: 收款人 openid + 收款人姓名(userName)
SELECT 'pay_transfer_order_wechat.payee_openid' AS field,
       count(*) AS plaintext_count
FROM pay_transfer_order_wechat
WHERE payee_openid IS NOT NULL AND payee_openid NOT LIKE 'v%'
UNION ALL
SELECT 'pay_transfer_order_wechat.user_name',
       count(*)
FROM pay_transfer_order_wechat
WHERE user_name IS NOT NULL AND user_name NOT LIKE 'v%';

-- 2. 迁移方法(应用层一次性执行)
--
--    启用加密(daxpay.platform.config.encrypt.enable=true + 配置 keys)后,
--    遍历历史记录执行 updateById 即可触发 TypeHandler 加密写入:
--
--    // 伪代码示例(三表同理)
--    alipayTransferOrderManager.list().forEach(e -> {
--        e.setPayeeAccount(e.getPayeeAccount());   // 自身赋值触发 dirty
--        e.setPayeeName(e.getPayeeName());
--        alipayTransferOrderManager.updateById(e); // TypeHandler 写入密文
--    });
--
--    可封装为 CommandLineRunner / 一次性 admin 接口, 随 profile 触发, 执行后下线。
--    迁移完成后重新执行上方检查语句, plaintext_count 应全部为 0。
