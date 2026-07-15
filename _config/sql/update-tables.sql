-- 付款码列：bar_code → auth_code（与 unipay 入参 authCode 对齐）
ALTER TABLE pay_normal_order RENAME COLUMN bar_code TO auth_code;
COMMENT ON COLUMN pay_normal_order.auth_code IS '付款码（被扫支付，审计保留）';
