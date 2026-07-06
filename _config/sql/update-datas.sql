-- 支付能力 alipay_order_qr 统一改为 alipay_qr（消除第三方通道"订单码"歧义，对应支付宝 precreate 预下单能力）
UPDATE "public"."pay_md_capability" SET code = 'alipay_qr' WHERE code = 'alipay_order_qr';
UPDATE "public"."pay_md_product_capability" SET capability_code = 'alipay_qr' WHERE capability_code = 'alipay_order_qr';

-- 支付方式维度清理（部分环境可能未处理，幂等兜底）
DELETE FROM "public"."pay_md_method" WHERE code = 'alipay_order_qr';
UPDATE "public"."pay_md_provider_method" SET method = 'alipay_qr' WHERE provider = 'alipay' AND method = 'alipay_order_qr';
