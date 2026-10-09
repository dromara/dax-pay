-- =============================================================
-- 2026-10-05: 邮件模板管理(通知中心新菜单 313, 邮件发送记录 310 之上)
-- 六个固定场景 × 中英两语言 × 五槽位(主题/标题/正文/提示/页脚)的覆盖,
-- 无覆盖行时用出厂默认(iam_mail_template 建表见 update-tables.sql);
-- 权限码与 @PermCode 注解一致(view/manage/test), 固定ID种子。可安全重放。
-- =============================================================
INSERT INTO public.iam_perm_menu (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu, component, path, redirect, sort_no, root, keep_alive, affix_tab, creator, last_modifier, version, deleted, menu_type, active_icon, badge, badge_type, badge_variants, iframe_src, link, create_time, last_modified_time)
SELECT 313, 308, 'system:notify:mail-template', 'admin', 'SystemMailTemplate', 'menu.system.notify.mailTemplate', 'lucide:mail-plus', false, false, '/system/notify/mail/MailTemplateList', '/system/notify/mail-template', NULL, 25, false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-10-05 00:00:00+00', '2026-10-05 00:00:00+00'
WHERE NOT EXISTS (SELECT 1 FROM public.iam_perm_menu WHERE id = 313);

INSERT INTO public.iam_perm_code (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2079866296000000557, 'system:notify:mail-template:view', 'system:notify:mail-template', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-10-05 00:00:00+00', '2026-10-05 00:00:00+00', 'perm.system:notify:mail-template:view'
WHERE NOT EXISTS (SELECT 1 FROM public.iam_perm_code WHERE code = 'system:notify:mail-template:view');

INSERT INTO public.iam_perm_code (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2079866296000000558, 'system:notify:mail-template:manage', 'system:notify:mail-template', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-10-05 00:00:00+00', '2026-10-05 00:00:00+00', 'perm.system:notify:mail-template:manage'
WHERE NOT EXISTS (SELECT 1 FROM public.iam_perm_code WHERE code = 'system:notify:mail-template:manage');

INSERT INTO public.iam_perm_code (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 2079866296000000559, 'system:notify:mail-template:test', 'system:notify:mail-template', true, '由 @PermCode 扫描同步生成', 1, 1, 0, false, '2026-10-05 00:00:00+00', '2026-10-05 00:00:00+00', 'perm.system:notify:mail-template:test'
WHERE NOT EXISTS (SELECT 1 FROM public.iam_perm_code WHERE code = 'system:notify:mail-template:test');

-- =============================================================
-- 2026-10-09: 内置商户普通用户角色(merchant_user, id=3) + 出厂菜单授权
-- 商户子用户创建时默认绑定本角色(创建即有角色, 防止登录后菜单为空404);
-- 授权面=仪表板+交易订单+回调通知(与 merchant_admin 授权的交集子集, 不含用户管理等敏感菜单);
-- 维护口径: 后续新增商户端菜单时, 须评估是否同步补授本角色(按 menu_id NOT EXISTS 幂等可重放)。
-- =============================================================
INSERT INTO public.iam_role (id, code, client_code, data_scope, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT 3, 'merchant_user', 'merchant', NULL, true, '系统内置商户普通用户角色', 1, 1, 0, false, '2026-10-09 00:00:00+00', '2026-10-09 00:00:00+00', 'role.merchant_user'
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role WHERE id = 3 OR code = 'merchant_user');

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091001, 3, NULL, 91001
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91001);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091002, 3, NULL, 91002
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91002);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091003, 3, NULL, 91003
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91003);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091100, 3, NULL, 91100
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91100);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091101, 3, NULL, 91101
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91101);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091103, 3, NULL, 91103
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91103);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091110, 3, NULL, 91110
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91110);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091111, 3, NULL, 91111
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91111);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091112, 3, NULL, 91112
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91112);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091127, 3, NULL, 91127
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91127);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091128, 3, NULL, 91128
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91128);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091129, 3, NULL, 91129
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91129);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091130, 3, NULL, 91130
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91130);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091131, 3, NULL, 91131
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91131);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091150, 3, NULL, 91150
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91150);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091200, 3, NULL, 91200
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91200);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091201, 3, NULL, 91201
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91201);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091202, 3, NULL, 91202
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91202);

INSERT INTO public.iam_role_menu (id, role_id, client_code, menu_id)
SELECT 3000091203, 3, NULL, 91203
WHERE NOT EXISTS (SELECT 1 FROM public.iam_role_menu WHERE role_id = 3 AND menu_id = 91203);
