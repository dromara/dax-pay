
-- 收银台支付项图标值对齐 PayProviderEnum 编码（union → union_pay, aggregate → aggregate_pay）
UPDATE pay_gateway_cashier_item SET icon = 'union_pay' WHERE icon = 'union';
UPDATE pay_gateway_cashier_item SET icon = 'aggregate_pay' WHERE icon = 'aggregate';

-- 菜单 component 路径统一：去掉 views/ 前缀（与全库 /{path} 风格一致）
UPDATE iam_perm_menu
SET component = '/iam/user/UserList',
    last_modified_time = NOW()
WHERE id = 30501
  AND component = 'views/iam/user/UserList';

UPDATE iam_perm_menu
SET component = '/system/config/third-platform/ThirdPlatform',
    last_modified_time = NOW()
WHERE id = 900001
  AND component = 'views/system/config/third-platform/ThirdPlatform';
