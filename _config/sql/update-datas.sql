-- 通知模块菜单数据(增量, 幂等)
-- 执行顺序: update-tables.sql -> update-datas.sql
-- 字段顺序同 iam_perm_menu: id, pid, menu_code, client_code, name, title_cn, title_en,
--   i18n_key, icon, hidden, hide_children_menu, component, path, redirect, sort_no,
--   root, keep_alive, affix_tab, creator, last_modifier, version, deleted, menu_type,
--   active_icon, badge, badge_type, badge_variants, iframe_src, link, create_time, last_modified_time

-- 公告通知菜单(挂在系统管理 id=3 下, 与配置/日志/权限同级)
INSERT INTO "iam_perm_menu" VALUES (
    309, 3, 'system:notify', 'admin', 'SystemNotify', '公告通知', 'Notification',
    'menu.system.notify', 'lucide:bell', false, false,
    '/system/notify/notice/NoticeList', '/system/notify', NULL, 20,
    false, true, false, 1, 1, 0, false, 'menu', NULL, NULL, NULL, NULL, NULL,
    '2026-06-24 16:00:00+00', '2026-06-24 16:00:00+00'
) ON CONFLICT (id) DO NOTHING;
