
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


-- ========== 权限码重构：menu_code 全量对齐（直改，无兼容） ==========
-- 执行后请管理端 POST /perm/code/scan 重建内置权限码，并重授角色

-- kebab / 语义 / 域缩短 / 交易域
UPDATE iam_perm_menu SET menu_code = 'system:security-config' WHERE menu_code = 'system:security_config';
UPDATE iam_perm_menu SET menu_code = 'system:platform-config' WHERE menu_code = 'system:platform_config';
UPDATE iam_perm_menu SET menu_code = 'system:oss-config' WHERE menu_code = 'system:oss_config';
UPDATE iam_perm_menu SET menu_code = 'merchant:wx-verify' WHERE menu_code = 'merchant:wx_verify';
UPDATE iam_perm_menu SET menu_code = 'merchant:notify-config' WHERE menu_code = 'merchant:notify_config';
UPDATE iam_perm_menu SET menu_code = 'merchant:profile' WHERE menu_code = 'merchant:mchInfo';
UPDATE iam_perm_menu SET menu_code = 'payment:config:product-config' WHERE menu_code = 'payment:config:product_config';
UPDATE iam_perm_menu SET menu_code = 'payment:config:product-detail' WHERE menu_code = 'payment:config:product_detail';
UPDATE iam_perm_menu SET menu_code = 'payment:config:wx-verify' WHERE menu_code = 'payment:config:wx_verify';
UPDATE iam_perm_menu SET menu_code = 'payment:platform:pay-channel' WHERE menu_code = 'payment:platform:pay_channel';
UPDATE iam_perm_menu SET menu_code = 'system:config:mobile-app' WHERE menu_code = 'system:config:mobile_app';
UPDATE iam_perm_menu SET menu_code = 'system:config:mobile-app-detail' WHERE menu_code = 'system:config:mobile_app_detail';
UPDATE iam_perm_menu SET menu_code = 'system:file' WHERE menu_code = 'system:file:platform';
UPDATE iam_perm_menu SET menu_code = 'iam:user' WHERE menu_code = 'iam:user:manager';
UPDATE iam_perm_menu SET menu_code = 'iam:online' WHERE menu_code = 'iam:online:user';
UPDATE iam_perm_menu SET menu_code = 'iam:menu' WHERE menu_code = 'iam:perm:menu';
UPDATE iam_perm_menu SET menu_code = 'iam:social' WHERE menu_code = 'iam:social:login-config';

-- provider 语义（顺序：先 manage 再 方式→method）
UPDATE iam_perm_menu SET menu_code = 'payment:platform:provider' WHERE menu_code = 'payment:platform:provider_manage';
UPDATE iam_perm_menu SET menu_code = 'payment:platform:pay-method' WHERE menu_code = 'payment:platform:provider' AND component LIKE '%PayMethodList%';
-- 若上条已把 方式 改掉，渠道行若仍是 provider_manage 已处理；若渠道行曾被误写成 pay-method 用 id 修正：
UPDATE iam_perm_menu SET menu_code = 'payment:platform:provider' WHERE id = 4040116;

-- 交易域
UPDATE iam_perm_menu SET menu_code = 'trade:order' WHERE menu_code = 'payment:order';
UPDATE iam_perm_menu SET menu_code = 'trade:refund' WHERE menu_code = 'payment:refund';
UPDATE iam_perm_menu SET menu_code = 'trade:fund' WHERE menu_code = 'payment:trade';
UPDATE iam_perm_menu SET menu_code = 'trade:gateway-order' WHERE menu_code = 'payment:gateway-order';

-- 消歧
UPDATE iam_perm_menu SET menu_code = 'channel:app' WHERE id = 4040119;
UPDATE iam_perm_menu SET menu_code = 'merchant:app:workbench' WHERE id = 4040123;

-- 权限码主数据动作后缀（若表存完整 code 字符串）
UPDATE iam_perm_code SET code = replace(code, ':credential_config_update', ':manage') WHERE code LIKE '%:credential_config_update';
UPDATE iam_perm_code SET code = replace(code, ':notify_config_update', ':manage') WHERE code LIKE '%:notify_config_update';
UPDATE iam_perm_code SET code = replace(code, ':reset_password', ':reset-password') WHERE code LIKE '%:reset_password';
UPDATE iam_perm_code SET code = replace(code, ':assign_role', ':assign-role') WHERE code LIKE '%:assign_role';
UPDATE iam_perm_code SET code = replace(code, ':update', ':manage') WHERE code LIKE '%:update' AND code NOT LIKE '%:reset-password%' AND code NOT LIKE '%:assign-role%';

-- menu_code 字段同步（权限码表）
UPDATE iam_perm_code SET menu_code = 'system:security-config' WHERE menu_code = 'system:security_config';
UPDATE iam_perm_code SET menu_code = 'system:platform-config' WHERE menu_code = 'system:platform_config';
UPDATE iam_perm_code SET menu_code = 'system:oss-config' WHERE menu_code = 'system:oss_config';
UPDATE iam_perm_code SET menu_code = 'merchant:wx-verify' WHERE menu_code = 'merchant:wx_verify';
UPDATE iam_perm_code SET menu_code = 'merchant:notify-config' WHERE menu_code = 'merchant:notify_config';
UPDATE iam_perm_code SET menu_code = 'payment:config:product-config' WHERE menu_code = 'payment:config:product_config';
UPDATE iam_perm_code SET menu_code = 'payment:config:wx-verify' WHERE menu_code = 'payment:config:wx_verify';
UPDATE iam_perm_code SET menu_code = 'payment:platform:pay-channel' WHERE menu_code = 'payment:platform:pay_channel';
UPDATE iam_perm_code SET menu_code = 'system:config:mobile-app' WHERE menu_code = 'system:config:mobile_app';
UPDATE iam_perm_code SET menu_code = 'system:file' WHERE menu_code = 'system:file:platform';
UPDATE iam_perm_code SET menu_code = 'iam:user' WHERE menu_code = 'iam:user:manager';
UPDATE iam_perm_code SET menu_code = 'iam:online' WHERE menu_code = 'iam:online:user';
UPDATE iam_perm_code SET menu_code = 'iam:menu' WHERE menu_code = 'iam:perm:menu';
UPDATE iam_perm_code SET menu_code = 'iam:social' WHERE menu_code = 'iam:social:login-config';
UPDATE iam_perm_code SET menu_code = 'payment:platform:pay-method' WHERE menu_code = 'payment:platform:provider' AND code LIKE 'payment:platform:pay-method%';
UPDATE iam_perm_code SET menu_code = 'trade:order' WHERE menu_code = 'payment:order';
UPDATE iam_perm_code SET menu_code = 'trade:refund' WHERE menu_code = 'payment:refund';
UPDATE iam_perm_code SET menu_code = 'trade:fund' WHERE menu_code = 'payment:trade';
UPDATE iam_perm_code SET menu_code = 'trade:gateway-order' WHERE menu_code = 'payment:gateway-order';

-- 完整码前缀交易域
UPDATE iam_perm_code SET code = replace(code, 'payment:order:', 'trade:order:') WHERE code LIKE 'payment:order:%';
UPDATE iam_perm_code SET code = replace(code, 'payment:refund:', 'trade:refund:') WHERE code LIKE 'payment:refund:%';
UPDATE iam_perm_code SET code = replace(code, 'payment:trade:', 'trade:fund:') WHERE code LIKE 'payment:trade:%';
UPDATE iam_perm_code SET code = replace(code, 'payment:gateway-order:', 'trade:gateway-order:') WHERE code LIKE 'payment:gateway-order:%';
UPDATE iam_perm_code SET code = replace(code, 'iam:user:manager:', 'iam:user:') WHERE code LIKE 'iam:user:manager:%';
UPDATE iam_perm_code SET code = replace(code, 'iam:online:user:', 'iam:online:') WHERE code LIKE 'iam:online:user:%';
UPDATE iam_perm_code SET code = replace(code, 'iam:perm:menu:', 'iam:menu:') WHERE code LIKE 'iam:perm:menu:%';
UPDATE iam_perm_code SET code = replace(code, 'iam:social:login-config:', 'iam:social:') WHERE code LIKE 'iam:social:login-config:%';
UPDATE iam_perm_code SET code = replace(code, 'system:file:platform:', 'system:file:') WHERE code LIKE 'system:file:platform:%';
UPDATE iam_perm_code SET code = replace(code, 'system:security_config:', 'system:security-config:') WHERE code LIKE 'system:security_config:%';
UPDATE iam_perm_code SET code = replace(code, 'system:platform_config:', 'system:platform-config:') WHERE code LIKE 'system:platform_config:%';
UPDATE iam_perm_code SET code = replace(code, 'system:oss_config:', 'system:oss-config:') WHERE code LIKE 'system:oss_config:%';
UPDATE iam_perm_code SET code = replace(code, 'merchant:notify_config:', 'merchant:notify-config:') WHERE code LIKE 'merchant:notify_config:%';
UPDATE iam_perm_code SET code = replace(code, 'merchant:wx_verify:', 'merchant:wx-verify:') WHERE code LIKE 'merchant:wx_verify:%';
UPDATE iam_perm_code SET code = replace(code, 'payment:config:product_config:', 'payment:config:product-config:') WHERE code LIKE 'payment:config:product_config:%';
UPDATE iam_perm_code SET code = replace(code, 'payment:config:wx_verify:', 'payment:config:wx-verify:') WHERE code LIKE 'payment:config:wx_verify:%';
UPDATE iam_perm_code SET code = replace(code, 'payment:platform:pay_channel:', 'payment:platform:pay-channel:') WHERE code LIKE 'payment:platform:pay_channel:%';
UPDATE iam_perm_code SET code = replace(code, 'system:config:mobile_app:', 'system:config:mobile-app:') WHERE code LIKE 'system:config:mobile_app:%';

-- 建议：迁移后执行权限码扫描，删除代码中已不存在的旧码

-- 通道应用 / 服务商 ISV 权限码统一（不按通道拆）
UPDATE iam_perm_menu SET menu_code = 'channel:app' WHERE menu_code IN (
  'channel:alipay:app','channel:wechat:app','channel:douyin:app','channel:wechat:isv-app','channel:app');
UPDATE iam_perm_menu SET menu_code = 'payment:isv' WHERE menu_code LIKE 'payment:%:isv' OR menu_code = 'payment:isv';
UPDATE iam_perm_code SET menu_code = 'channel:app' WHERE menu_code IN (
  'channel:alipay:app','channel:wechat:app','channel:douyin:app','channel:wechat:isv-app');
UPDATE iam_perm_code SET menu_code = 'payment:isv' WHERE menu_code LIKE 'payment:%:isv' AND menu_code <> 'payment:isv';
UPDATE iam_perm_code SET code = replace(code, menu_code || ':', 'channel:app:') WHERE menu_code = 'channel:app' AND code NOT LIKE 'channel:app:%';
-- 更稳妥：按旧前缀改完整码
UPDATE iam_perm_code SET code = 'channel:app:view' WHERE code LIKE 'channel:%:app:view' OR code = 'channel:wechat:isv-app:view';
UPDATE iam_perm_code SET code = 'channel:app:manage' WHERE code LIKE 'channel:%:app:manage' OR code = 'channel:wechat:isv-app:manage';
UPDATE iam_perm_code SET code = 'payment:isv:view' WHERE code LIKE 'payment:%:isv:view' AND code <> 'payment:isv:view';
UPDATE iam_perm_code SET code = 'payment:isv:manage' WHERE code LIKE 'payment:%:isv:manage' AND code <> 'payment:isv:manage';
