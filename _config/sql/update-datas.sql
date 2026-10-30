-- =============================================================
-- 增量数据脚本(上一版本 -> 当前版本)
-- 清空重置基线。
-- 发版时本文件无需改动, 保持空白即可; 下一版本周期内的种子数据变化再追加于此,
-- =============================================================

-- ========== 2026-10-24 虚拟设备简版: 运营端/商户端菜单 + 权限码 + 内置角色授权 ==========
-- 运营端: 目录「虚拟设备」(/device/virtual, 挂支付系统下, 与码牌同域) + 页「设备管理」(/device/virtual/list)
INSERT INTO public.iam_perm_menu VALUES (917, 4, NULL, 'admin', 'VirtualDeviceGroup', 'menu.payment.device.virtual', 'lucide:smartphone', false, false, NULL, '/device/virtual', '/device/virtual/list', 41, false, false, false, 0, NULL, 0, false, 'catalog', NULL, NULL, NULL, NULL, NULL, NULL, '2026-10-24 00:00:00+00', '2026-10-24 00:00:00+00', false);
INSERT INTO public.iam_perm_menu VALUES (918, 917, 'device:virtual', 'admin', 'VirtualDeviceList', 'menu.payment.device.virtual.list', 'lucide:monitor-speaker', false, false, '/payment/device/virtual/VirtualDeviceList', '/device/virtual/list', NULL, 1, false, false, false, 0, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-10-24 00:00:00+00', '2026-10-24 00:00:00+00', false);
-- 商户端: 页「收款设备」(/mch/device/audio, 挂商户中心基础管理组, 与码牌/终端同组;
-- component/path 取 plus 原生形态(C2 波按此路径建页), i18n_key 与前端 menu-titles 词条 key 一致)
INSERT INTO public.iam_perm_menu VALUES (91308, 91416, 'device:audio', 'merchant', 'MchAudioDeviceList', 'menu.payment.merchant.audioDevice', 'lucide:speaker', false, false, '/payment/device/audio/MchAudioDeviceList', '/mch/device/audio', NULL, 6, false, false, false, 0, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-10-24 00:00:00+00', '2026-10-24 00:00:00+00', false);
-- 权限码 4 条(运营端 device:virtual:view/manage + 商户端 device:audio:view/manage; B 波 Controller 的 @PermCode 与之对齐)
INSERT INTO public.iam_perm_code VALUES (2079866296000002001, 'device:virtual:view', 'device:virtual', true, '虚拟设备查看权限码(与 @PermCode 注解一致, 固定ID种子)', 1, 1, 0, false, '2026-10-24 00:00:00+00', '2026-10-24 00:00:00+00', 'perm.device:virtual:view');
INSERT INTO public.iam_perm_code VALUES (2079866296000002002, 'device:virtual:manage', 'device:virtual', true, '虚拟设备管理权限码(与 @PermCode 注解一致, 固定ID种子)', 1, 1, 0, false, '2026-10-24 00:00:00+00', '2026-10-24 00:00:00+00', 'perm.device:virtual:manage');
INSERT INTO public.iam_perm_code VALUES (2079866296000002003, 'device:audio:view', 'device:audio', true, '商户端收款设备查看权限码(与 @PermCode 注解一致, 固定ID种子)', 1, 1, 0, false, '2026-10-24 00:00:00+00', '2026-10-24 00:00:00+00', 'perm.device:audio:view');
INSERT INTO public.iam_perm_code VALUES (2079866296000002004, 'device:audio:manage', 'device:audio', true, '商户端收款设备管理权限码(与 @PermCode 注解一致, 固定ID种子)', 1, 1, 0, false, '2026-10-24 00:00:00+00', '2026-10-24 00:00:00+00', 'perm.device:audio:manage');
-- 内置角色授权(菜单授权无超管全量特判, 须显式挂角色: role 1=运营管理员/role 2=商户管理员)
INSERT INTO public.iam_role_menu VALUES (1000000000917, 1, NULL, 917);
INSERT INTO public.iam_role_menu VALUES (1000000000918, 1, NULL, 918);
INSERT INTO public.iam_role_menu VALUES (1000000091308, 2, NULL, 91308);
INSERT INTO public.iam_role_code VALUES (2079866296000003001, 1, 2079866296000002001);
INSERT INTO public.iam_role_code VALUES (2079866296000003002, 1, 2079866296000002002);
INSERT INTO public.iam_role_code VALUES (2079866296000003003, 2, 2079866296000002003);
INSERT INTO public.iam_role_code VALUES (2079866296000003004, 2, 2079866296000002004);

