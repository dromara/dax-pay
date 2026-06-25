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

-- ========== 存储管理菜单结构调整 ==========
-- 「平台文件」(30601) 改名「存储文件」，从「存储管理」(306) 迁移到「系统监控」(307) 下
-- 删除原「存储管理」目录菜单 (306)
-- 字段顺序同 iam_perm_menu: id, pid, menu_code, client_code, name, title_cn, title_en,
--   i18n_key, icon, hidden, hide_children_menu, component, path, redirect, sort_no,
--   root, keep_alive, affix_tab, creator, last_modifier, version, deleted, menu_type,
--   active_icon, badge, badge_type, badge_variants, iframe_src, link,
--   create_time, last_modified_time

-- 1. 更新平台文件菜单为「存储文件」并挂到系统监控下（menu_code 保持不变，后端 @PermCode 无需改动）
UPDATE "iam_perm_menu" SET
    pid                = 307,
    name               = 'StorageFile',
    title_cn           = '存储文件',
    title_en           = 'Storage File',
    i18n_key           = 'menu.system.monitor.file',
    icon               = 'lucide:files',
    component          = '/system/monitor/file/PlatformFileList',
    path               = '/system/monitor/file',
    sort_no            = 2,
    last_modified_time = now()
WHERE id = 30601;

-- 2. 删除原「存储管理」目录菜单
DELETE FROM "iam_perm_menu" WHERE id = 306;
