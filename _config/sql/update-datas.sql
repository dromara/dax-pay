-- 升级数据脚本: 存量用户密码安全记录补齐
-- 为缺少 iam_user_password_security 记录的存量用户补建默认记录 (initial_password=false, 密码不过期),
-- 避免存量正常用户首次登录被误判为初始密码而强制改密;
-- 新建用户与管理员重置密码仍由应用层写入 initial_password=true, 不受本脚本影响。
-- 脚本幂等, 可重复执行。
INSERT INTO "public"."iam_user_password_security"
    (id, password_error_count, initial_password, version, deleted)
SELECT u.id, 0, false, 0, false
FROM "public"."iam_user_info" u
WHERE u.deleted = false
  AND NOT EXISTS (
      SELECT 1 FROM "public"."iam_user_password_security" s WHERE s.id = u.id
  );

-- 升级数据脚本: 下架微信消息通知功能清理存量数据
-- 开源版移除微信公众号模板通知(配置页/发送记录/编排链路), 商业版另议;
-- 清理菜单/权限码/角色关联孤儿数据与 wechat_notify 平台配置残留。
-- 脚本幂等, 可重复执行。
DELETE FROM "public"."iam_role_menu"
WHERE menu_id IN (
    SELECT id FROM "public"."iam_perm_menu" WHERE menu_code = 'system:notify:wechat-config'
);
DELETE FROM "public"."iam_perm_menu" WHERE menu_code = 'system:notify:wechat-config';
DELETE FROM "public"."iam_role_code"
WHERE code_id IN (
    SELECT id FROM "public"."iam_perm_code" WHERE code LIKE 'system:notify:wechat-config:%'
);
DELETE FROM "public"."iam_perm_code" WHERE code LIKE 'system:notify:wechat-config:%';
DELETE FROM "public"."system_platform_config" WHERE config_type = 'wechat_notify';
DROP TABLE IF EXISTS "public"."pay_platform_wechat_message_record";

-- 升级数据脚本: 邮件通知功能菜单与权限码
-- 新增 通知中心→邮件发送记录 菜单(310)与其权限码(view/manage/resend + platform-config:test),
-- 并绑定默认管理员角色(role_id=1);
-- 权限码亦可由 @PermCode 扫描服务手动同步生成, 本脚本保证存量环境开箱可用。
-- 脚本幂等, 可重复执行。
INSERT INTO "public"."iam_perm_menu"
    (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu,
     component, path, redirect, sort_no, root, keep_alive, affix_tab,
     creator, last_modifier, version, deleted, menu_type, create_time, last_modified_time)
SELECT 310::int8, 308::int8, 'system:notify:mail-record'::varchar, 'admin'::varchar,
       'SystemMailRecord'::varchar, 'menu.system.notify.mailRecord'::varchar, 'lucide:mail'::varchar, false, false,
       '/system/notify/mail/MailRecordList'::varchar, '/system/notify/mail-record'::varchar, NULL, 30::float8, false, true, false,
       1::int8, 1::int8, 0, false, 'menu'::varchar,
       '2026-08-27 16:00:00+00'::timestamptz, '2026-08-27 16:00:00+00'::timestamptz
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_perm_menu" WHERE id = 310);

INSERT INTO "public"."iam_perm_code"
    (id, code, menu_code, internal, remark, creator, last_modifier, version, deleted, create_time, last_modified_time, i18n_key)
SELECT v.id, v.code, v.menu_code, true, '由 @PermCode 扫描同步生成', 1, 1, 0, false,
       '2026-08-27 16:00:00+00', '2026-08-27 16:00:00+00', v.i18n_key
FROM (VALUES
    (2079866296000000001::int8, 'system:notify:mail-record:view'::varchar, 'system:notify:mail-record'::varchar, 'perm.system:notify:mail-record:view'::varchar),
    (2079866296000000002::int8, 'system:notify:mail-record:manage'::varchar, 'system:notify:mail-record'::varchar, 'perm.system:notify:mail-record:manage'::varchar),
    (2079866296000000003::int8, 'system:notify:mail-record:resend'::varchar, 'system:notify:mail-record'::varchar, 'perm.system:notify:mail-record:resend'::varchar),
    (2079866296000000004::int8, 'system:platform-config:test'::varchar, 'system:platform-config'::varchar, 'perm.system:platform-config:test'::varchar)
) AS v(id, code, menu_code, i18n_key)
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_perm_code" WHERE id = v.id);

INSERT INTO "public"."iam_role_menu" (id, role_id, client_code, menu_id)
SELECT 1000000000310::int8, 1::int8, NULL, 310::int8
WHERE NOT EXISTS (SELECT 1 FROM "public"."iam_role_menu" WHERE id = 1000000000310);
