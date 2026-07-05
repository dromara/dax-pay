-- ============================================================
-- 拉卡拉服务商配置菜单 + 权限挂载点
-- ============================================================

-- 拉卡拉服务商配置入口(通过产品配置列表点击"拉卡拉"跳转 ProductDetailDispatch 分发到 LakalaManage)
-- 同时作为 @PermCode(menuCode="payment:lakala:isv") 的权限挂载点
-- parent_id=40105(支付产品管理), 与微信服务商(40503)/支付宝服务商同级
INSERT INTO "public"."iam_perm_menu" VALUES (
    40506, 40105,
    'payment:lakala:isv', 'admin',
    'LakalaManage',
    '拉卡拉服务商配置', 'Lakala ISV Configuration',
    'menu.payment.lakala.config',
    NULL, 't', 'f',
    '/payment/channel/lakala/manage/LakalaManage', '/payment/config/product/lakala-manage',
    NULL, 5, 'f', 't', 'f', 1, 1, 0, 'f', 'subpage',
    NULL, NULL, NULL, NULL, NULL, NULL,
    '2026-07-05 00:00:00+00', '2026-07-05 00:00:00+00'
) ON CONFLICT (id) DO NOTHING;
