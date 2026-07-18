-- ============================================================
-- 门店/应用默认项唯一性约束（升级脚本）
-- ============================================================
-- 背景:
--   商户应用(MchAppInfo)和门店(MchStoreInfo)均引入「默认项」机制,
--   要求同一商户下至多一个默认应用 / 至多一个默认门店。
--   原有约束仅由 Service 层 setDefault(先 clearDefault 再 set)保证,
--   并发或绕过 Service 直接改库时存在隐患。
-- 策略:
--   数据库层加 PostgreSQL 部分唯一索引(Partial Unique Index)兜底,
--   与 Service 层逻辑形成双保险。
-- 影响范围:
--   - 升级前请先执行 update-datas.sql 中的诊断查询,
--     如发现「同商户多个默认应用/门店」需先人工修复, 否则建索引会失败。
-- ============================================================

-- 商户默认应用唯一索引 (同一商户至多一个 default_app=true 且未删除的记录)
CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_app_info_default
    ON mch_app_info (mch_no)
    WHERE default_app = TRUE AND deleted = FALSE;

-- 商户默认门店唯一索引 (同一商户至多一个 default_store=true 且未删除的记录)
CREATE UNIQUE INDEX IF NOT EXISTS uk_mch_store_info_default
    ON mch_store_info (mch_no)
    WHERE default_store = TRUE AND deleted = FALSE;
