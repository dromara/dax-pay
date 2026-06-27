-- ============================================================
-- 权限码体系重构迁移（下划线命名 + 粗粒度 + menuCode 对齐菜单）
-- ============================================================
-- 执行时机：部署本批代码改动后、重启服务前执行
-- 执行后：  重启服务，触发权限码扫描同步（PermCodeScanService）按新 @PermCode 重建 iam_perm_code
-- 前提：    iam_role_code 当前无实际角色-权限码分配数据，可直接重建

-- ---------- 1. 菜单 menu_code 改名（仅改编码，不增删菜单记录） ----------

-- 安全配置：system:securityConfig → system:security_config
UPDATE iam_perm_menu SET menu_code = 'system:security_config', last_modified_time = now() WHERE id = 30401;

-- 平台配置：system:platformConfig → system:platform_config
UPDATE iam_perm_menu SET menu_code = 'system:platform_config', last_modified_time = now() WHERE id = 30402;

-- OSS 配置：system:ossConfig → system:oss_config（同时修正原先与平台配置的撞码）
UPDATE iam_perm_menu SET menu_code = 'system:oss_config', last_modified_time = now() WHERE id = 30404;

-- 支付通道：payment:platform:payChannel → payment:platform:pay_channel
UPDATE iam_perm_menu SET menu_code = 'payment:platform:pay_channel', last_modified_time = now() WHERE id = 40103;

-- 支付产品配置：payment:config:productConfig → payment:config:product_config
UPDATE iam_perm_menu SET menu_code = 'payment:config:product_config', last_modified_time = now() WHERE id = 40105;

-- 支付渠道管理页：payment:platform:provider:manage → payment:platform:provider_manage
-- 该页复用 payment:platform:provider 权限码（来自 40102），改名为下划线连接以避免与权限码末段 manage 混淆
UPDATE iam_perm_menu SET menu_code = 'payment:platform:provider_manage', last_modified_time = now() WHERE id = 4040116;

-- 设备厂商配置：device:vendorConfig → device:vendor_config
UPDATE iam_perm_menu SET menu_code = 'device:vendor_config', last_modified_time = now() WHERE id = 90201;

-- 支付产品详情（子页面）：payment:config:productDetail → payment:config:product_detail
UPDATE iam_perm_menu SET menu_code = 'payment:config:product_detail', last_modified_time = now() WHERE id = 40501;

-- ---------- 2. 清空权限码主数据（重启后由扫描同步按新代码重建） ----------
-- iam_role_code 无分配数据；先清关联表避免外键冲突，再清权限码主数据
DELETE FROM iam_role_code;
DELETE FROM iam_perm_code;
