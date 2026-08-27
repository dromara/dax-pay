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
