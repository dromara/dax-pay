-- 2026-08-29 商户端菜单: 异常订单 + 资金流水 (随运营端同功能批次补商户端镜像)
-- 菜单行幂等: 已存在(含 id 冲突)时跳过, 支持在已手工执行过部分语句的库上安全重放
-- 2026-08-30 修正: 资金流水挂到「交易管理」目录(pid=91100, sort=4)对齐运营端, 异常订单 sort=3→5, 见文件末尾修正段
INSERT INTO "public"."iam_perm_menu" VALUES (91128, 91100, 'trade:abnormal-order', 'merchant', 'AbnormalOrderList', 'menu.trade.abnormalOrder', 'lucide:triangle-alert', 'f', 'f', '/payment/order/AbnormalOrderList', '/trade/abnormal-order', NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-29 16:00:00+00', '2026-08-30 22:30:00+00')
ON CONFLICT (id) DO NOTHING;
INSERT INTO "public"."iam_perm_menu" VALUES (91203, 91100, 'trade:fund-flow', 'merchant', 'FundFlowList', 'menu.trade.fundFlow', 'lucide:coins', 'f', 'f', '/payment/record/FundFlowList', '/trade/record/fund-flow', NULL, 4, 'f', 'f', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', '2026-08-29 16:00:00+00', '2026-08-30 22:30:00+00')
ON CONFLICT (id) DO NOTHING;

-- 商户管理员角色(role 2)菜单授权(按 role_id+menu_id 语义防重, 可安全重放)
INSERT INTO "public"."iam_role_menu" (id, role_id, client_code, menu_id)
SELECT 1000000091128, 2, NULL, 91128
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_role_menu" rm WHERE rm.role_id = 2 AND rm.menu_id = 91128);
INSERT INTO "public"."iam_role_menu" (id, role_id, client_code, menu_id)
SELECT 1000000091203, 2, NULL, 91203
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_role_menu" rm WHERE rm.role_id = 2 AND rm.menu_id = 91203);

-- 商户管理员角色(role 2)权限码授权: 按code字符串解析code_id
-- (iam_perm_code 由 @PermCode 扫描同步生成——运维通过 POST /perm/code/scan 手动触发, 非启动自动同步; 升级库中ID为运行时雪花, 不可硬编码引用)
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2079866296000000311, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:abnormal-order:view'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2079866296000000312, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:fund-flow:view'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);

-- 2026-08-30 商户端菜单: 码牌管理(商户自助查/改/启停/绑应用门店/认领空白码)
INSERT INTO "public"."iam_perm_menu" VALUES (91306, 91300, 'device:qrcode', 'merchant', 'DeviceQrCodeList', 'menu.payment.merchant.qrcode', 'lucide:qr-code', 'f', 'f', '/payment/device/qrcode/DeviceQrCodeList', '/mch/qrcode', NULL, 7, 'f', 'f', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-30 21:30:00+00', '2026-08-30 21:30:00+00')
ON CONFLICT (id) DO NOTHING;

-- 商户管理员角色(role 2)菜单授权(按 role_id+menu_id 语义防重, 可安全重放)
INSERT INTO "public"."iam_role_menu" (id, role_id, client_code, menu_id)
SELECT 1000000091306, 2, NULL, 91306
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_role_menu" rm WHERE rm.role_id = 2 AND rm.menu_id = 91306);

-- 商户管理员角色(role 2)权限码授权: 按code字符串解析code_id
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2079866296000000321, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'device:qrcode:view'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2079866296000000322, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'device:qrcode:manage'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);

-- 2026-08-30 商户端菜单位置修正: 对齐运营端结构
-- 资金流水(91203)从「回调通知」目录(91200)移到「交易管理」目录(91100)下, 排序: 支付订单(3)→资金流水(4)→异常订单(5)
-- 异常订单(91128) sort 3→5, 让出资金流水位置(原 sort=3 与支付订单目录 91110 冲突)
-- UPDATE 天然幂等, 已执行过 2026-08-29 旧批次 INSERT 的库重放本段即可修正
UPDATE "public"."iam_perm_menu" SET pid = 91100, sort_no = 4, last_modifier = 1, last_modified_time = '2026-08-30 22:30:00+00'
WHERE id = 91203 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET sort_no = 5, last_modifier = 1, last_modified_time = '2026-08-30 22:30:00+00'
WHERE id = 91128 AND client_code = 'merchant';

-- 2026-09-01 商户端「支付配置」目录重组: 三组收敛为两组(应用与对接/收款通道)
-- 微信(91412)/抖音(91420)应用从「支付应用」组(91418)挪入 91417, 菜单文案同步简化为「微信应用/抖音应用」(前端 menu-titles 维护, 表内 i18n_key 不变)
-- 收款通道组内排序: 通道商户(1)/微信应用(2)/抖音应用(3)/系统终端(4)/微信域名验证(5), 系统终端(91304) sort 2→4, 微信域名验证(91305) sort 3→5
-- 「支付应用」组(91418)清空后删除, 并清理其角色授权悬挂行
-- UPDATE/DELETE 天然幂等, 可安全重放
UPDATE "public"."iam_perm_menu" SET pid = 91417, sort_no = 2, last_modifier = 1, last_modified_time = '2026-09-01 00:00:00+00'
WHERE id = 91412 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91417, sort_no = 3, last_modifier = 1, last_modified_time = '2026-09-01 00:00:00+00'
WHERE id = 91420 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET sort_no = 4, last_modifier = 1, last_modified_time = '2026-09-01 00:00:00+00'
WHERE id = 91304 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET sort_no = 5, last_modifier = 1, last_modified_time = '2026-09-01 00:00:00+00'
WHERE id = 91305 AND client_code = 'merchant';
-- 清理已删组的角色授权悬挂行, 再删除空组
DELETE FROM "public"."iam_role_menu" WHERE menu_id = 91418;
DELETE FROM "public"."iam_perm_menu" WHERE id = 91418 AND client_code = 'merchant';

-- 2026-09-01(二) 商户端「商户中心」域三组重组(参考运营端商户管理总览页分组): 顶级「支付配置」91400 并入「商户中心」91300, 下设三组
-- 基础管理 91416(原「接口对接」组改造): 商户资料/商户用户/门店管理/码牌管理/系统终端(终端自收款通道挪入)
-- 支付配置 91417(原「收款通道」组改造): 通道商户/应用管理(自基础组前身份挪入)/微信应用/抖音应用
-- 业务配置 91422(新增): 对接配置/微信域名验证(两者皆对接配套域, 参照运营端总览页业务配置组定义)
-- 组 i18n_key 换为 menu.mch.group.{basic|pay|biz}(文案与运营端商户工作台三组一致), 顶级 91400 删除并清理其角色授权
-- 全部语句幂等可重放
UPDATE "public"."iam_perm_menu" SET pid = 91300, name = 'BasicManageGroup', i18n_key = 'menu.mch.group.basic', icon = 'lucide:boxes', path = '/mch-center/basic', redirect = '/mch/info', sort_no = 1, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00'
WHERE id = 91416 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91300, name = 'PaymentConfigGroup', i18n_key = 'menu.mch.group.pay', icon = 'lucide:credit-card', path = '/mch-center/pay-config', redirect = '/mch/channel-merchant', sort_no = 2, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00'
WHERE id = 91417 AND client_code = 'merchant';
INSERT INTO "public"."iam_perm_menu" VALUES (91422, 91300, NULL, 'merchant', 'BusinessConfigGroup', 'menu.mch.group.biz', 'lucide:plug', 'f', 'f', NULL, '/mch-center/biz-config', '/mch/credential', 3, 'f', 't', 'f', 0, NULL, 0, 'f', 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-09-01 12:00:00+00', '2026-09-01 12:00:00+00')
ON CONFLICT (id) DO NOTHING;
UPDATE "public"."iam_perm_menu" SET pid = 91416, sort_no = 1, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91301 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91416, sort_no = 2, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91302 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91416, sort_no = 3, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91303 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91416, sort_no = 4, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91306 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91416, sort_no = 5, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91304 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91417, sort_no = 2, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91401 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET sort_no = 3, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91412 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET sort_no = 4, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91420 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91422, sort_no = 1, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91501 AND client_code = 'merchant';
UPDATE "public"."iam_perm_menu" SET pid = 91422, sort_no = 2, last_modifier = 1, last_modified_time = '2026-09-01 12:00:00+00' WHERE id = 91305 AND client_code = 'merchant';
DELETE FROM "public"."iam_role_menu" WHERE menu_id = 91400;
DELETE FROM "public"."iam_perm_menu" WHERE id = 91400 AND client_code = 'merchant';

-- ======================================================-- 2026-09-10 移除小程序快捷登录功能(weChatApplet/alipayApplet/douyinApplet)
-- 背景: 凭据为 source 维度单份, 无法支撑多小程序(管理端/商户端各自独立 appid)并存, 功能整体下线;
--       各端小程序登录回归账号密码; 收银台链路(pay_platform_mobile_app)不受影响。
-- 本段清理存量库中的 applet 配置行与用户绑定行, 幂等可重放。
-- =============================================================

-- 清理小程序快捷登录的平台配置行(未配置过的库无行, 0 行删除为正常)
DELETE FROM "public"."iam_social_login_config" WHERE source IN ('weChatApplet', 'alipayApplet', 'douyinApplet');

-- 清理用户的小程序快捷登录绑定关系(绑定过小程序的用户需改用账号密码登录)
DELETE FROM "public"."iam_user_social" WHERE source IN ('weChatApplet', 'alipayApplet', 'douyinApplet');

-- =============================================================
-- 2026-09-13 支付产品启停入口迁移至支付产品配置, 权限码随之迁移
-- 背景: switch-enabled 端点从 /admin/product 迁至 /admin/product-config(APP 侧 /app-admin 同口径),
--       原码 payment:platform:product:manage(ID 2070862265027072001)删除;
--       启停操作改用既有码 payment:config:product-config:manage(ID 2070862265018683392);
--       存量角色对旧码的授权(iam_role_code 按 code_id 关联)改指既有码, 授权关系不断, 防重复先删再迁。
-- =============================================================

-- 先删与目标码重复的授权行, 再把旧码授权迁指目标码, 最后删旧权限码种子行(菜单管理页扫描不再生成该码)
DELETE FROM public.iam_role_code
WHERE code_id = 2070862265027072001
  AND role_id IN (SELECT role_id FROM public.iam_role_code WHERE code_id = 2070862265018683392);
UPDATE public.iam_role_code SET code_id = 2070862265018683392 WHERE code_id = 2070862265027072001;
DELETE FROM public.iam_perm_code WHERE id = 2070862265027072001 AND code = 'payment:platform:product:manage';
-- =============================================================
-- 2026-09-17晚 盛付通拆分「商户/服务商」双产品
-- 背景: 服务商模式(代子商户发起)拆为独立产品 sheng_isv, 服务商密钥产品级全局一份(表结构见 update-tables.sql 同日块);
--       原 sheng_pay 更名「盛付通(商户)」; 能力清单与服务商模式一致(union_h5 种子保留、策略不映射的既定拍板不变)。
-- =============================================================

-- 商户模式产品名变更(盛付通 → 盛付通(商户))
UPDATE public.pay_md_product SET name = '盛付通(商户)', last_modified_time = '2026-09-17 00:00:00+00' WHERE code = 'sheng_pay';

-- 服务商模式产品种子 + 能力清单(16 项, 与商户模式一致)
INSERT INTO public.pay_md_product VALUES (10025, 'sheng_isv', '盛付通(服务商)', 'sheng_pay', 151, 1, '2026-09-17 00:00:00+00', 1, '2026-09-17 00:00:00+00', 0, false, false, true);
INSERT INTO public.pay_md_product_capability VALUES (21173, 'sheng_isv', 'wechat_qr', 0, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21174, 'sheng_isv', 'wechat_jsapi', 1, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21175, 'sheng_isv', 'wechat_app', 2, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21176, 'sheng_isv', 'wechat_h5', 3, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21177, 'sheng_isv', 'wechat_mini', 4, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21178, 'sheng_isv', 'wechat_barcode', 5, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21179, 'sheng_isv', 'alipay_qr', 6, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21180, 'sheng_isv', 'alipay_jsapi', 7, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21181, 'sheng_isv', 'alipay_app', 8, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21182, 'sheng_isv', 'alipay_h5', 9, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21183, 'sheng_isv', 'alipay_pc', 10, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21184, 'sheng_isv', 'alipay_barcode', 11, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21185, 'sheng_isv', 'union_qr', 12, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21186, 'sheng_isv', 'union_jsapi', 13, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21187, 'sheng_isv', 'union_h5', 14, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (21188, 'sheng_isv', 'union_barcode', 15, true, NULL, false, 1, '2026-09-17 00:00:00+00', 0, 1, '2026-09-17 00:00:00+00');

-- =============================================================
-- 2026-09-15 交易域 5 个 P0 模块导出权限码授权
-- 新增 iam_perm_code: trade:fund:export / trade:order:export / trade:gateway-order:export / trade:refund:export / trade:fund-flow:export
-- 运营管理员(role 1) + 商户管理员(role 2) 均授予导出权限
-- iam_perm_code 行由 data.sql 基线或 POST /perm/code/scan 写入; 已有库可能未包含这 5 行, 本段先补写(ON CONFLICT 幂等)
-- 再补 iam_role_code 角色授权, 按 code 字符串子查询解析 code_id, NOT EXISTS 防重, 幂等可安全重放
-- =============================================================

-- 补写 iam_perm_code(已有库可能缺失; 新建库 data.sql 已含; 按 code 防重, 幂等可安全重放)
INSERT INTO "public"."iam_perm_code" (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2083000000000000003, 'trade:fund:export', 'trade:fund', true, NULL, 1, 1, 0, false, '2026-09-15 00:00:00+00', '2026-09-15 00:00:00+00', 'perm.trade:fund:export'
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_perm_code" c WHERE c.code = 'trade:fund:export');
INSERT INTO "public"."iam_perm_code" (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2083000000000000004, 'trade:order:export', 'trade:order', true, NULL, 1, 1, 0, false, '2026-09-15 00:00:00+00', '2026-09-15 00:00:00+00', 'perm.trade:order:export'
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_perm_code" c WHERE c.code = 'trade:order:export');
INSERT INTO "public"."iam_perm_code" (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2083000000000000005, 'trade:gateway-order:export', 'trade:gateway-order', true, NULL, 1, 1, 0, false, '2026-09-15 00:00:00+00', '2026-09-15 00:00:00+00', 'perm.trade:gateway-order:export'
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_perm_code" c WHERE c.code = 'trade:gateway-order:export');
INSERT INTO "public"."iam_perm_code" (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2083000000000000006, 'trade:refund:export', 'trade:refund', true, NULL, 1, 1, 0, false, '2026-09-15 00:00:00+00', '2026-09-15 00:00:00+00', 'perm.trade:refund:export'
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_perm_code" c WHERE c.code = 'trade:refund:export');
INSERT INTO "public"."iam_perm_code" (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2083000000000000007, 'trade:fund-flow:export', 'trade:fund-flow', true, NULL, 1, 1, 0, false, '2026-09-15 00:00:00+00', '2026-09-15 00:00:00+00', 'perm.trade:fund-flow:export'
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_perm_code" c WHERE c.code = 'trade:fund-flow:export');

-- 运营管理员(role 1) 导出权限
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083100000000000001, 1, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:fund:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 1 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083100000000000002, 1, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:order:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 1 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083100000000000003, 1, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:gateway-order:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 1 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083100000000000004, 1, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:refund:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 1 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083100000000000005, 1, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:fund-flow:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 1 AND rc.code_id = c.id);

-- 商户管理员(role 2) 导出权限
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083200000000000001, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:fund:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083200000000000002, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:order:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083200000000000003, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:gateway-order:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083200000000000004, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:refund:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);
INSERT INTO "public"."iam_role_code" (id, role_id, code_id)
SELECT 2083200000000000005, 2, c.id
FROM "public"."iam_perm_code" c
WHERE c.code = 'trade:fund-flow:export'
  AND NOT EXISTS (SELECT 1 FROM "public"."iam_role_code" rc WHERE rc.role_id = 2 AND rc.code_id = c.id);


-- 升级数据脚本: 权限码墓碑清理(2026-09-18)
-- 清理历次重构遗留的软删权限码墓碑(通道统一/设备域收敛/通知下架等, 后端 @PermCode 已不存在, 全量种子 data.sql 同步移除);
-- 脚本幂等, 可重复执行; 墓碑码无角色绑定, 防御性联动删除角色关联。
DELETE FROM "public"."iam_role_code"
WHERE code_id IN (SELECT id FROM "public"."iam_perm_code" WHERE code IN (
    'channel:alipay:app:manage','channel:alipay:app:view','channel:douyin:app:manage','channel:douyin:app:view','channel:wechat:app:manage','channel:wechat:app:view','develop:trade:pay','device:printer:manage','device:printer:view','device:speaker:manage','device:speaker:view','device:terminal:system:manage','device:terminal:system:view','device:vendor_config:manage','device:vendor_config:view','iam:social:config:manage','iam:social:config:view','merchant:gateway-aggregate:manage','merchant:gateway-aggregate:view','merchant:gateway-code:manage','merchant:gateway-code:view','payment:alipay:isv:manage','payment:alipay:isv:view','payment:config:mobile_app:manage','payment:config:mobile_app:view','payment:dougong:isv:manage','payment:dougong:isv:view','payment:fuyou:isv:manage','payment:fuyou:isv:view','payment:hkrt:isv:manage','payment:hkrt:isv:view','payment:hmpay:isv:manage','payment:hmpay:isv:view','payment:lakala:isv:manage','payment:lakala:isv:view','payment:leshua:isv:manage','payment:leshua:isv:view','payment:risk:hit:manage','payment:vbill:isv:manage','payment:vbill:isv:view','payment:wechat:isv:manage','payment:wechat:isv:view','system:config:mobile-app:manage','system:config:mobile-app:view','system:notify:manage','system:notify:publish','system:notify:view'
));
DELETE FROM "public"."iam_perm_code" WHERE code IN (
    'channel:alipay:app:manage','channel:alipay:app:view','channel:douyin:app:manage','channel:douyin:app:view','channel:wechat:app:manage','channel:wechat:app:view','develop:trade:pay','device:printer:manage','device:printer:view','device:speaker:manage','device:speaker:view','device:terminal:system:manage','device:terminal:system:view','device:vendor_config:manage','device:vendor_config:view','iam:social:config:manage','iam:social:config:view','merchant:gateway-aggregate:manage','merchant:gateway-aggregate:view','merchant:gateway-code:manage','merchant:gateway-code:view','payment:alipay:isv:manage','payment:alipay:isv:view','payment:config:mobile_app:manage','payment:config:mobile_app:view','payment:dougong:isv:manage','payment:dougong:isv:view','payment:fuyou:isv:manage','payment:fuyou:isv:view','payment:hkrt:isv:manage','payment:hkrt:isv:view','payment:hmpay:isv:manage','payment:hmpay:isv:view','payment:lakala:isv:manage','payment:lakala:isv:view','payment:leshua:isv:manage','payment:leshua:isv:view','payment:risk:hit:manage','payment:vbill:isv:manage','payment:vbill:isv:view','payment:wechat:isv:manage','payment:wechat:isv:view','system:config:mobile-app:manage','system:config:mobile-app:view','system:notify:manage','system:notify:publish','system:notify:view'
);

-- =============================================================
-- 2026-09-18 易支付通道(easy_pay)接入
-- 背景: 自 3.0 商业版移植易支付三方聚合通道, 一通道一产品, 一期仅扫码两类(wechat_qr/alipay_qr);
--       密钥表结构见 update-tables.sql 同日块; 产品种子与能力清单按 NOT EXISTS 幂等写入。
-- =============================================================

-- 易支付产品种子
INSERT INTO public.pay_md_product (id, code, name, channel, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted, sandbox, enabled)
SELECT 10026, 'easy_pay', '易支付', 'easy_pay', 190, 1, '2026-09-18 00:00:00+00', 1, '2026-09-18 00:00:00+00', 0, false, false, true
WHERE NOT EXISTS (SELECT 1 FROM public.pay_md_product WHERE code = 'easy_pay');

-- 易支付能力种子(扫码两类)
INSERT INTO public.pay_md_product_capability (id, product_code, capability_code, sort_no, enabled, remark, deleted, creator, create_time, version, last_modifier, last_modified_time)
SELECT 21189, 'easy_pay', 'wechat_qr', 0, true, NULL, false, 1, '2026-09-18 00:00:00+00', 0, 1, '2026-09-18 00:00:00+00'
WHERE NOT EXISTS (SELECT 1 FROM public.pay_md_product_capability WHERE product_code = 'easy_pay' AND capability_code = 'wechat_qr');
INSERT INTO public.pay_md_product_capability (id, product_code, capability_code, sort_no, enabled, remark, deleted, creator, create_time, version, last_modifier, last_modified_time)
SELECT 21190, 'easy_pay', 'alipay_qr', 1, true, NULL, false, 1, '2026-09-18 00:00:00+00', 0, 1, '2026-09-18 00:00:00+00'
WHERE NOT EXISTS (SELECT 1 FROM public.pay_md_product_capability WHERE product_code = 'easy_pay' AND capability_code = 'alipay_qr');
