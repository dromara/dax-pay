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
