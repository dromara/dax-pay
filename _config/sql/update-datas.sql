-- ============================================================
-- 升级数据脚本（增量，按版本追加）
-- 适用于已有存量数据的线上环境；全新安装走 tables.sql → datas.sql
-- ============================================================

-- ------------------------------------------------------------
-- 省市两级地区黑名单: 省级名单存储值 省名 → 省行政区划编码
-- 说明:
--   - 前端 province 类型名单存的是 base_province.name 全称(如"广东省"),
--     改存 code 后按 base_province 全称 JOIN 迁移为 2 位省编码
--   - JOIN 不到的行(历史脏数据/非全称)保留原值, 需人工核查
-- ------------------------------------------------------------
UPDATE pay_blacklist b
SET value = p.code
FROM base_province p
WHERE b.type = 'province' AND b.value = p.name;

-- ------------------------------------------------------------
-- 分账(Allocation)功能 - 分账订单菜单
-- 说明:
--   - admin(608)/merchant(91127)两端各新增「分账订单」菜单, 挂交易订单目录,
--     排序 2.5(退款订单之后、支付订单目录之前), 与 iam_perm_menu.sql 种子一致
--   - 注意: admin 端菜单 id 用 608 而非 605 —— 605 已被「商户出站通知」
--     (trade:mch-notice)占用, 早前种子文件曾重复使用 605 导致插入被主键冲突跳过
--   - 超管角色(role_id=1)补角色-菜单关联; 其余角色由管理员在
--     「系统管理 → 角色管理 → 权限分配」中自行勾选
--   - 幂等: 菜单按主键冲突跳过, 脚本可重复执行
-- ------------------------------------------------------------
INSERT INTO iam_perm_menu (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu, component, path, redirect, sort_no, root, keep_alive, affix_tab, creator, last_modifier, version, deleted, menu_type)
VALUES (608, 6, 'trade:alloc', 'admin', 'AllocOrderList', 'menu.trade.allocOrder', 'lucide:split', false, false, '/payment/order/AllocOrderList', '/trade/alloc-order', NULL, 2.5, false, true, false, 1, 1, 0, false, 'menu')
ON CONFLICT (id) DO NOTHING;

INSERT INTO iam_perm_menu (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu, component, path, redirect, sort_no, root, keep_alive, affix_tab, creator, last_modifier, version, deleted, menu_type)
VALUES (91127, 91100, 'trade:alloc', 'merchant', 'AllocOrderList', 'menu.trade.allocOrder', 'lucide:split', false, false, '/payment/order/AllocOrderList', '/trade/alloc-order', NULL, 2.5, false, true, false, 1, 1, 0, false, 'menu')
ON CONFLICT (id) DO NOTHING;

-- 超管角色-菜单关联(id 沿用 data.sql 种子段位: 1000000000000 + 菜单ID)
INSERT INTO iam_role_menu (id, role_id, client_code, menu_id) VALUES
(1000000000608, 1, NULL, 608),
(1000000091127, 1, NULL, 91127)
ON CONFLICT (id) DO NOTHING;
