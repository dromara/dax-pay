-- ============================================================
-- 支付管理(id=4) 菜单结构优化 (admin 端)
--
-- 目标结构:
--   ├─ 支付主数据          (catalog)  服务商/通道/方式/产品/能力
--   ├─ 支付产品配置        (menu, 提为1级·前移)
--   ├─ 支付应用            (catalog 新建)  微信支付应用 / 抖音支付应用
--   ├─ 码牌管理            (menu, 顶层叶子)
--   ├─ 接入配置            (catalog, 原"支付配置"恢复)  移动端应用管理 / 平台微信域名校验
--   └─ 支付风控            (catalog)  黑名单 / 风险命中记录
--
-- 说明:
--   1. 仅改 admin 端(pid=4 子树), 不影响 merchant 端
--   2. 菜单显示文案改名(接入配置/微信支付应用等)在 locale json 维护, 见文件末尾清单
--   3. 顺带清理死节点 id=9(设备管理, 已软删除且零子节点)
--   4. 排序整数化, 替换原 0.5/2.5/2.6/2.7 小数补位
-- ============================================================

-- 1. 新建「支付应用」目录 (id=409, 收拢微信/抖音)
INSERT INTO iam_perm_menu
  (id, pid, menu_code, client_code, name, i18n_key, icon,
   hidden, hide_children_menu, component, path, redirect,
   sort_no, root, keep_alive, affix_tab, creator, last_modifier, version, deleted, menu_type,
   create_time, last_modified_time)
VALUES
  (409, 4, 'payment:app', 'admin', 'PaymentApp', 'menu.payment.app', 'lucide:layout-grid',
   false, false, NULL, '/payment/app', NULL,
   30, false, true, false, 1, 1, 0, false, 'catalog',
   now(), now());

-- 2. 归组: 微信/抖音 -> 支付应用(409)
UPDATE iam_perm_menu SET pid = 409, sort_no = 10, last_modified_time = now()
 WHERE id = 40106 AND client_code = 'admin';
UPDATE iam_perm_menu SET pid = 409, sort_no = 20, last_modified_time = now()
 WHERE id = 40108 AND client_code = 'admin';

-- 3. 归组: 移动端应用管理 -> 接入配置(405)
UPDATE iam_perm_menu SET pid = 405, sort_no = 10, last_modified_time = now()
 WHERE id = 40504 AND client_code = 'admin';

-- 4. 「接入配置」(原"支付配置", id=405) 恢复显示
--    该目录此前 deleted='t' 且 hidden='t', 导致子项"平台微信域名校验"(40507) 成孤儿
UPDATE iam_perm_menu
   SET deleted = false, hidden = false, sort_no = 50, last_modified_time = now()
 WHERE id = 405 AND client_code = 'admin';
UPDATE iam_perm_menu SET sort_no = 20, last_modified_time = now()
 WHERE id = 40507 AND client_code = 'admin';

-- 5. 「支付产品配置」提为1级并前移; 其余目录排序整数化
UPDATE iam_perm_menu SET sort_no = 10, last_modified_time = now()
 WHERE id = 401 AND client_code = 'admin';
UPDATE iam_perm_menu SET sort_no = 20, last_modified_time = now()
 WHERE id = 40105 AND client_code = 'admin';
UPDATE iam_perm_menu SET sort_no = 40, last_modified_time = now()
 WHERE id = 901 AND client_code = 'admin';
UPDATE iam_perm_menu SET sort_no = 60, last_modified_time = now()
 WHERE id = 406 AND client_code = 'admin';

-- 6. 清理设备管理死节点 (已软删除且零子节点, 终端已归商户)
DELETE FROM iam_perm_menu WHERE id = 9;


-- ============================================================
-- 可选: 码牌 menu_code/path 前缀归一 (device: -> payment:)
-- !! 执行前需核对 dax-pay-open controller 映射与前端 router.push 字面量 !!
-- ============================================================
-- UPDATE iam_perm_menu
--    SET menu_code = 'payment:qrcode',
--        path      = '/payment/qrcode',
--        last_modified_time = now()
--  WHERE id = 901 AND client_code = 'admin';


-- ============================================================
-- i18n 文案改动 (不在本 SQL, 在 dax-pay-ui/apps/daxpay-admin/src/locales/menu-titles/{locale}.json, 至少 6 语种)
--   新增 key: menu.payment.app        = 支付应用
--   改文案(key 不变):
--     menu.payment.config            支付配置       -> 接入配置
--     menu.payment.wx.app            支付应用(微信) -> 微信支付应用
--     menu.payment.douyin.app        支付应用(抖音) -> 抖音支付应用
--   去重(隐藏子页, 避免与上面重名):
--     menu.payment.wx.mchApp         -> 微信支付应用(商户)
--     menu.payment.douyin.mchApp     -> 抖音支付应用(商户)
--   删失效 key(10 语种 locale 同步删):
--     menu.payment.merchant.channelMerchant.douyinApp
--     menu.payment.merchant.aggregateScan
--     menu.payment.merchant.codePayConfig
--     menu.business.config
--     menu.payment.merchant.notifyConfig
-- ============================================================


-- ============================================================
-- 附: 本次未处理的存量软删除 (merchant 端历史残留, 与支付管理无关, 待单独确认)
--   91104/91102/91105  trade:pay-order 旧目录及其子(已被 91110/91111/91112 取代)
--   91106/91107/91108  trade:record 旧目录及其子(已被 91200/91201/91202 取代)
-- ============================================================
