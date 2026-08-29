-- ----------------------------------------------------------------
-- 数据库升级数据 (update-tables.sql 的配套数据脚本, 每次发版归档后清空本文件)
-- ----------------------------------------------------------------

-- 2026-08-28 锁定用户监控页: 系统监控下新增「锁定用户」菜单(iam:lock, sort 1.5, 位于在线用户与存储文件之间)
INSERT INTO "public"."iam_perm_menu" VALUES (30702, 307, 'iam:lock', 'admin', 'LoginLock', 'menu.system.monitor.lock', 'lucide:lock', 'f', 'f', '/system/monitor/lock/LoginLockList', '/system/monitor/lock', NULL, 1.5, 'f', 't', 'f', 1, 1, 0, 'f', 'menu', NULL, NULL, NULL, NULL, NULL, NULL, '2026-08-28 16:00:00+00', '2026-08-28 16:00:00+00');
