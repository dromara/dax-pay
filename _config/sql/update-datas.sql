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


-- ============================================================
-- 云闪付(union_pay) 支付主数据种子
--
-- 目标: 补全 pay_md_* 主数据表中的 union 相关行, 使云闪付在
--      支付渠道Tab/支付方式目录/支付能力页/支付产品页正常展示
--
-- 说明:
--   1. union 复用通用 channel:merchant 菜单族, 无需新增菜单/权限种子
--   2. 展示名走枚举 i18n (common-i18n enum/*.json 已全 10 语种就绪),
--      DB 行 description/name 仅作维护参考, 不直接驱动前端文案
--   3. 幂等: 有部分唯一索引(WHERE deleted=false)的表用 ON CONFLICT ... DO NOTHING,
--      pay_md_channel 无唯一索引用 WHERE NOT EXISTS 兜底
--   4. ID 段位 9xxxx 自成一段, 避开既有雪花ID与商业版数据
--   5. pay_md_product_config 不补, active_env 由 PayProductConfigService 启动时兜底
-- ============================================================

-- 1. 支付通道 (pay_md_channel) — 无唯一索引, 用 WHERE NOT EXISTS
INSERT INTO public.pay_md_channel
  (id, code, sort_no, description, icon, creator, create_time, last_modifier, last_modified_time, version, deleted)
SELECT 91001, 'union_pay', 3, '云闪付', NULL, 1, now(), 1, now(), 0, false
WHERE NOT EXISTS (
  SELECT 1 FROM public.pay_md_channel WHERE code = 'union_pay' AND deleted = false
);

-- 2. 支付渠道 (pay_md_provider)
INSERT INTO public.pay_md_provider
  (id, code, icon, sort_no, deleted, last_modifier, last_modified_time, version, creator, create_time, enabled, description)
VALUES
  (92001, 'union_pay', NULL, 3, false, 1, now(), 0, 1, now(), true, '银联')
ON CONFLICT (code) WHERE deleted = false DO NOTHING;

-- 3. 支付方式 (pay_md_method) — 4 种方式
INSERT INTO public.pay_md_method
  (id, code, sort_no, description, deleted, last_modifier, last_modified_time, version, creator, create_time)
VALUES
  (93001, 'union_qr',      30, '银联扫码',   false, 1, now(), 0, 1, now()),
  (93002, 'union_jsapi',   31, '银联JSAPI',  false, 1, now(), 0, 1, now()),
  (93003, 'union_h5',      32, '银联H5',     false, 1, now(), 0, 1, now()),
  (93004, 'union_barcode', 33, '银联付款码', false, 1, now(), 0, 1, now())
ON CONFLICT (code) WHERE deleted = false DO NOTHING;

-- 4. 支付能力 (pay_md_capability) — 与支付方式同码
INSERT INTO public.pay_md_capability
  (id, code, sort_no, enabled, description, deleted, last_modifier, last_modified_time, version, creator, create_time)
VALUES
  (94001, 'union_qr',      30, true, '银联扫码',   false, 1, now(), 0, 1, now()),
  (94002, 'union_jsapi',   31, true, '银联JSAPI',  false, 1, now(), 0, 1, now()),
  (94003, 'union_h5',      32, true, '银联H5',     false, 1, now(), 0, 1, now()),
  (94004, 'union_barcode', 33, true, '银联付款码', false, 1, now(), 0, 1, now())
ON CONFLICT (code) WHERE deleted = false DO NOTHING;

-- 5. 渠道-方式关联 (pay_md_provider_method) — union_pay 下 4 种方式
INSERT INTO public.pay_md_provider_method
  (id, provider, method, sort_no, deleted, last_modifier, last_modified_time, version, creator, create_time, description)
VALUES
  (95001, 'union_pay', 'union_qr',      1, false, 1, now(), 0, 1, now(), NULL),
  (95002, 'union_pay', 'union_jsapi',   2, false, 1, now(), 0, 1, now(), NULL),
  (95003, 'union_pay', 'union_h5',      3, false, 1, now(), 0, 1, now(), NULL),
  (95004, 'union_pay', 'union_barcode', 4, false, 1, now(), 0, 1, now(), NULL)
ON CONFLICT (provider, method) WHERE deleted = false DO NOTHING;

-- 6. 支付产品 (pay_md_product) — 3 个产品, 均支持沙箱(sandbox=true)
INSERT INTO public.pay_md_product
  (id, code, name, channel, description, sort_no, creator, create_time, last_modifier, last_modified_time, version, deleted, sandbox, enabled)
VALUES
  (96001, 'union_qrcode',  '云闪付(C扫B)', 'union_pay', '云闪付主扫(二维码)',  1, 1, now(), 1, now(), 0, false, true, true),
  (96002, 'union_h5',      '云闪付(H5)',   'union_pay', '云闪付H5',            2, 1, now(), 1, now(), 0, false, true, true),
  (96003, 'union_barcode', '云闪付(B扫C)', 'union_pay', '云闪付被扫(付款码)', 3, 1, now(), 1, now(), 0, false, true, true)
ON CONFLICT (code) WHERE deleted = false DO NOTHING;

-- 7. 产品-能力关联 (pay_md_product_capability) — 对齐各 ProductStrategy 的 methodCapabilityMapping
INSERT INTO public.pay_md_product_capability
  (id, product_code, capability_code, sort_no, enabled, remark, deleted, last_modifier, last_modified_time, version, creator, create_time)
VALUES
  (97001, 'union_qrcode',  'union_qr',      1, true, NULL, false, 1, now(), 0, 1, now()),
  (97002, 'union_h5',      'union_h5',      1, true, NULL, false, 1, now(), 0, 1, now()),
  (97003, 'union_barcode', 'union_barcode', 1, true, NULL, false, 1, now(), 0, 1, now())
ON CONFLICT (product_code, capability_code) WHERE deleted = false DO NOTHING;
