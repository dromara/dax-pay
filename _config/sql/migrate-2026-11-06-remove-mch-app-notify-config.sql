-- ============================================================================
-- 异步通知配置功能下线迁移脚本
-- 生成时间: 2026-11-06
-- 适用库: daxpay-dev / 各环境既有库（全新装库请直接用重建后的 table.sql + data.sql）
--
-- 影响范围:
--   1. 删除表 public.mch_app_notify_config（商户应用事件通知配置）
--   2. 删除通知任务中应用级(MQ/HTTP 回调)存量: mch_notice_task.source='app' 或 transport='mq'
--   3. 删除 IAM 三处关联: iam_role_menu 授权 -> iam_perm_code 权限码 -> iam_perm_menu 菜单
--
-- 执行方式（psql，单事务、遇错即停）:
--   psql -v ON_ERROR_STOP=1 --single-transaction -f migrate-2026-11-06-remove-mch-app-notify-config.sql
--
-- 数据归档:
--   下方 [可选] 归档段默认注释。如需保留删除前快照，取消注释后再执行；
--   归档表 archive_* 不参与业务，确认无误后可手工 DROP。
--
-- 幂等性:
--   各语句均带存在性判断/条件删除，重复执行不会报错（但归档段重复执行会因表已存在而跳过）。
-- ============================================================================

BEGIN;

-- ---------------------------------------------------------------------------
-- [可选] 删除前快照归档，默认关闭；需要留档时取消注释
-- ---------------------------------------------------------------------------
-- CREATE TABLE IF NOT EXISTS public.archive_mch_app_notify_config AS
--     TABLE public.mch_app_notify_config;
-- CREATE TABLE IF NOT EXISTS public.archive_mch_notice_task_app AS
--     SELECT * FROM public.mch_notice_task
--     WHERE source = 'app' OR transport = 'mq';

-- ---------------------------------------------------------------------------
-- 1. 清理应用级通知任务存量（功能下线后不再产生此类任务）
-- ---------------------------------------------------------------------------
DELETE FROM public.mch_notice_task
WHERE source = 'app'
   OR transport = 'mq';

-- ---------------------------------------------------------------------------
-- 2. 清理角色-菜单授权（必须先于菜单删除，避免留下孤儿授权）
-- ---------------------------------------------------------------------------
DELETE FROM public.iam_role_menu
WHERE menu_id IN (
    SELECT id FROM public.iam_perm_menu WHERE menu_code = 'merchant:notify-config'
);

-- ---------------------------------------------------------------------------
-- 3. 清理权限码（merchant:notify-config:view / merchant:notify-config:manage）
-- ---------------------------------------------------------------------------
DELETE FROM public.iam_perm_code
WHERE menu_code = 'merchant:notify-config';

-- ---------------------------------------------------------------------------
-- 4. 清理菜单（merchant 端 91414 / admin 端 4040126，按 menu_code 匹配以适配各环境）
-- ---------------------------------------------------------------------------
DELETE FROM public.iam_perm_menu
WHERE menu_code = 'merchant:notify-config';

-- ---------------------------------------------------------------------------
-- 5. 删除通知配置表
-- ---------------------------------------------------------------------------
DROP TABLE IF EXISTS public.mch_app_notify_config;

COMMIT;

-- ============================================================================
-- 执行后核验（应全部返回 0 行 / 0 条）:
--   SELECT count(*) FROM information_schema.tables
--    WHERE table_schema='public' AND table_name='mch_app_notify_config';
--   SELECT count(*) FROM public.iam_perm_menu WHERE menu_code='merchant:notify-config';
--   SELECT count(*) FROM public.iam_perm_code WHERE menu_code='merchant:notify-config';
--   SELECT count(*) FROM public.mch_notice_task WHERE source='app' OR transport='mq';
--
-- 后续动作:
--   本仓 table.sql / data.sql / data-demo.sql 为构建产物，已随本次下线同步手工更新；
--   待 dev 库执行完本脚本后，重跑 node build/export-sql.mjs 可让产物回到"以 dev 库为真相源"的生成路径。
-- ============================================================================
