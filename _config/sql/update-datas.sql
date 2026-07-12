
-- 收银台支付项图标值对齐 PayProviderEnum 编码（union → union_pay, aggregate → aggregate_pay）
UPDATE pay_gateway_cashier_item SET icon = 'union_pay' WHERE icon = 'union';
UPDATE pay_gateway_cashier_item SET icon = 'aggregate_pay' WHERE icon = 'aggregate';
