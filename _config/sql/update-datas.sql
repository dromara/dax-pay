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
