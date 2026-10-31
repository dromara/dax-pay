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

-- ========== 2026-10-31 mock 模拟通道(P0): 通道/产品/能力主数据 + 运营端菜单 ==========
-- 通道 mock(id 92005, 排序 24); 产品 mock_pay/mock_isv(id 96005/96006, 一通道双产品);
-- 能力挂载 42 行(id 97004-97045, 两产品各 21 项: 微信7/支付宝6/银联4/抖音4),
-- 能力码全部取自既有 PayCapabilityEnum, 无新增 pay_md_capability 行、无新增能力码。
INSERT INTO public.pay_md_channel VALUES (92005, 'mock', 24, NULL, 1, '2026-10-31 00:00:00+00', 1, '2026-10-31 00:00:00+00', 0, false);
INSERT INTO public.pay_md_product VALUES (96005, 'mock_pay', '模拟支付', 'mock', 210, 1, '2026-10-31 00:00:00+00', 1, '2026-10-31 00:00:00+00', 0, false, true, true);
INSERT INTO public.pay_md_product VALUES (96006, 'mock_isv', '模拟支付(服务商)', 'mock', 211, 1, '2026-10-31 00:00:00+00', 1, '2026-10-31 00:00:00+00', 0, false, true, true);
INSERT INTO public.pay_md_product_capability VALUES (97004, 'mock_pay', 'wechat_cashier', 0, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97005, 'mock_pay', 'wechat_qr', 1, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97006, 'mock_pay', 'wechat_jsapi', 2, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97007, 'mock_pay', 'wechat_mini', 3, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97008, 'mock_pay', 'wechat_h5', 4, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97009, 'mock_pay', 'wechat_app', 5, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97010, 'mock_pay', 'wechat_barcode', 6, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97011, 'mock_pay', 'alipay_qr', 7, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97012, 'mock_pay', 'alipay_jsapi', 8, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97013, 'mock_pay', 'alipay_pc', 9, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97014, 'mock_pay', 'alipay_h5', 10, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97015, 'mock_pay', 'alipay_app', 11, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97016, 'mock_pay', 'alipay_barcode', 12, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97017, 'mock_pay', 'union_qr', 13, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97018, 'mock_pay', 'union_jsapi', 14, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97019, 'mock_pay', 'union_h5', 15, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97020, 'mock_pay', 'union_barcode', 16, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97021, 'mock_pay', 'douyin_qr', 17, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97022, 'mock_pay', 'douyin_jsapi', 18, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97023, 'mock_pay', 'douyin_h5', 19, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97024, 'mock_pay', 'douyin_app', 20, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97025, 'mock_isv', 'wechat_cashier', 0, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97026, 'mock_isv', 'wechat_qr', 1, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97027, 'mock_isv', 'wechat_jsapi', 2, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97028, 'mock_isv', 'wechat_mini', 3, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97029, 'mock_isv', 'wechat_h5', 4, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97030, 'mock_isv', 'wechat_app', 5, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97031, 'mock_isv', 'wechat_barcode', 6, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97032, 'mock_isv', 'alipay_qr', 7, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97033, 'mock_isv', 'alipay_jsapi', 8, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97034, 'mock_isv', 'alipay_pc', 9, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97035, 'mock_isv', 'alipay_h5', 10, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97036, 'mock_isv', 'alipay_app', 11, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97037, 'mock_isv', 'alipay_barcode', 12, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97038, 'mock_isv', 'union_qr', 13, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97039, 'mock_isv', 'union_jsapi', 14, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97040, 'mock_isv', 'union_h5', 15, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97041, 'mock_isv', 'union_barcode', 16, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97042, 'mock_isv', 'douyin_qr', 17, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97043, 'mock_isv', 'douyin_jsapi', 18, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97044, 'mock_isv', 'douyin_h5', 19, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
INSERT INTO public.pay_md_product_capability VALUES (97045, 'mock_isv', 'douyin_app', 20, true, NULL, false, 1, '2026-10-31 00:00:00+00', 0, 1, '2026-10-31 00:00:00+00');
-- 运营端菜单「模拟服务商管理」+「模拟服务商工作台」(subpage, 挂 40508 渠道服务商配置组下;
-- 复用 menu_code=channel:merchant 零新权限码; i18n_key=menu.payment.mock.isv / menu.payment.mock.isvWorkbench,
-- 菜单 id 顺延 4040133/4040134; component 路径与 views/payment/channel/mock/ 下文件一致;
-- 菜单授权无超管全量特判, 与既有 40502/40506 子页一致显式挂运营管理员角色 1)
INSERT INTO public.iam_perm_menu VALUES (4040133, 40508, 'channel:merchant', 'admin', 'MockIsvManage', 'menu.payment.mock.isv', NULL, true, false, '/payment/channel/mock/MockIsvManage', '/payment/mock/isv', NULL, 6, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-10-31 00:00:00+00', '2026-10-31 00:00:00+00', false);
INSERT INTO public.iam_role_menu VALUES (1000004040133, 1, NULL, 4040133);
INSERT INTO public.iam_perm_menu VALUES (4040134, 40508, 'channel:merchant', 'admin', 'MockIsvWorkbench', 'menu.payment.mock.isvWorkbench', NULL, true, false, '/payment/channel/mock/MockIsvWorkbench', '/payment/mock/isv-workbench', NULL, 7, false, true, false, 1, 1, 0, false, 'subpage', NULL, NULL, NULL, NULL, NULL, NULL, '2026-10-31 00:00:00+00', '2026-10-31 00:00:00+00', false);
INSERT INTO public.iam_role_menu VALUES (1000004040134, 1, NULL, 4040134);
-- 修正 4040133 组件路径(初版误写为 /payment/mock/MockIsvManage, 与 views/payment/channel/mock/ 下实际文件不一致, 已应用库执行本句即可)
UPDATE public.iam_perm_menu SET component = '/payment/channel/mock/MockIsvManage' WHERE id = 4040133;

-- ========== 2026-10-31 主体注销治理批C: 存量已删商户/用户数据批量脱敏(幂等, 仅命中 deleted=true 行, 零行时无操作) ==========
-- 口径对齐后端注销服务(移植自 plus 52efeb86): 名称类改占位符, 强 PII 置 NULL, 密钥/密码置失效占位, 骨架字段(编号/时间戳)保留;
-- password 置为格式合法但明文不存在的 BCrypt 占位串, 防离线字典还原(历史已删用户的密码哈希不再可信)。
UPDATE public.mch_info SET mch_name = '已删除-' || mch_no, mch_short_name = NULL WHERE deleted;
UPDATE public.mch_app_info SET app_name = '已删除应用' WHERE deleted;
UPDATE public.mch_store_info SET store_name = '已删除门店', contact_phone = NULL, address = NULL, logo_url = NULL, facade_url = NULL, interior_url = NULL, remark = NULL WHERE deleted;
UPDATE public.mch_credential SET public_key = NULL, secret_key = NULL WHERE deleted;
UPDATE public.iam_user_info SET name = '已删除用户-' || id, account = 'deleted-' || id, password = '$2a$10$SwQ7Km2RcXv9Bn4Ld8GfUopA1zE5yT9wQ3iO7pL2kJ6hG4fD0sA5', phone = NULL, email = NULL WHERE deleted;
UPDATE public.iam_user_expand_info SET sex = NULL, birthday = NULL, avatar = NULL, last_login_ip = NULL WHERE deleted;

