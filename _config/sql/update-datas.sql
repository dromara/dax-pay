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
-- (iam_perm_code 由 @PermCode 启动扫描同步生成, 升级库中ID为运行时雪花, 不可硬编码引用)
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
