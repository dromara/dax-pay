-- 商户端应用工作台菜单改造：配置项从独立菜单收敛为应用工作台隐藏子页面
-- 1. AppConfigDir(91403) 从可见目录改为透明分组容器(subpage_group)，挂到应用配置(91401)下
-- 2. 新增应用工作台(91413)隐藏子页面，作为配置入口聚合页
-- 3. 通道路由/聚合/码牌/收银台/易支付 改为隐藏子页面(subpage)
-- 4. 通知配置(91408)删除独立路由，改由工作台抽屉承载

-- AppConfigDir 改为透明分组容器，挂到应用配置下
UPDATE "public"."iam_perm_menu"
SET pid = 91401, icon = NULL, hidden = true, menu_type = 'subpage_group'
WHERE id = 91403;

-- 新增应用工作台隐藏子页面（配置入口聚合页）
INSERT INTO "public"."iam_perm_menu"
  (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu,
   component, path, redirect, sort_no, root, keep_alive, affix_tab,
   creator, last_modifier, version, deleted, menu_type,
   active_icon, badge, badge_type, badge_variants, iframe_src, link,
   create_time, last_modified_time)
VALUES (91413, 91403, 'merchant:app:workbench', 'merchant', 'MchAppWorkbench',
        'menu.payment.merchant.appWorkbench', NULL, true, false,
        '/payment/merchant/app/MchAppWorkbench', '/mch/app/manage', NULL, 0, false, false, false,
        0, 1, 1, false, 'subpage',
        NULL, NULL, NULL, NULL, NULL, NULL,
        now(), now())
ON CONFLICT (id) DO UPDATE SET
  pid = EXCLUDED.pid,
  menu_code = EXCLUDED.menu_code,
  name = EXCLUDED.name,
  i18n_key = EXCLUDED.i18n_key,
  icon = EXCLUDED.icon,
  hidden = EXCLUDED.hidden,
  component = EXCLUDED.component,
  path = EXCLUDED.path,
  sort_no = EXCLUDED.sort_no,
  menu_type = EXCLUDED.menu_type,
  last_modified_time = now();

-- 通道路由/聚合/码牌/收银台/易支付 改为隐藏子页面
UPDATE "public"."iam_perm_menu"
SET hidden = true, menu_type = 'subpage'
WHERE id IN (91404, 91405, 91406, 91407, 91409);

-- 通知配置删除独立路由（改由应用工作台抽屉承载）
DELETE FROM "public"."iam_perm_menu" WHERE id = 91408;

-- ===== 商户端「业务配置」分组(BusinessConfig 91500)子菜单 =====
-- 91500 catalog 已在全量 iam_perm_menu.sql 中，此处仅补子菜单
-- 1. 对接配置(91501)：商户 API 密钥配置，页面已迁移，补菜单注册让 /mch/credential 路由生效

-- 对接配置（商户级，menu 类型，可见）
INSERT INTO "public"."iam_perm_menu"
  (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu,
   component, path, redirect, sort_no, root, keep_alive, affix_tab,
   creator, last_modifier, version, deleted, menu_type,
   active_icon, badge, badge_type, badge_variants, iframe_src, link,
   create_time, last_modified_time)
VALUES (91501, 91500, 'merchant:credential', 'merchant', 'MerchantCredentialConfig',
        'menu.payment.merchant.credential', 'lucide:key', false, false,
        '/payment/merchant/credential/MerchantCredentialConfig', '/mch/credential', NULL, 1, false, false, false,
        0, 1, 1, false, 'menu',
        NULL, NULL, NULL, NULL, NULL, NULL,
        now(), now())
ON CONFLICT (id) DO UPDATE SET
  pid = EXCLUDED.pid,
  menu_code = EXCLUDED.menu_code,
  name = EXCLUDED.name,
  i18n_key = EXCLUDED.i18n_key,
  icon = EXCLUDED.icon,
  hidden = EXCLUDED.hidden,
  component = EXCLUDED.component,
  path = EXCLUDED.path,
  sort_no = EXCLUDED.sort_no,
  menu_type = EXCLUDED.menu_type,
  last_modified_time = now();

-- 通知配置（应用级汇总页：列出商户所有应用，逐个配置异步通知）
INSERT INTO "public"."iam_perm_menu"
  (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu,
   component, path, redirect, sort_no, root, keep_alive, affix_tab,
   creator, last_modifier, version, deleted, menu_type,
   active_icon, badge, badge_type, badge_variants, iframe_src, link,
   create_time, last_modified_time)
VALUES (91502, 91500, 'merchant:notify-config', 'merchant', 'MchNotifyConfigOverview',
        'menu.payment.merchant.notifyConfig', 'lucide:bell', false, false,
        '/payment/merchant/notify-config/MchNotifyConfigOverview', '/mch/notify-config', NULL, 2, false, false, false,
        0, 1, 1, false, 'menu',
        NULL, NULL, NULL, NULL, NULL, NULL,
        now(), now())
ON CONFLICT (id) DO UPDATE SET
  pid = EXCLUDED.pid,
  menu_code = EXCLUDED.menu_code,
  name = EXCLUDED.name,
  i18n_key = EXCLUDED.i18n_key,
  icon = EXCLUDED.icon,
  hidden = EXCLUDED.hidden,
  component = EXCLUDED.component,
  path = EXCLUDED.path,
  sort_no = EXCLUDED.sort_no,
  menu_type = EXCLUDED.menu_type,
  last_modified_time = now();

-- 支付宝服务商代运营授权（商户级独立页：列出商户名下支付宝通道商户，扫码完成代运营授权）
INSERT INTO "public"."iam_perm_menu"
  (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu,
   component, path, redirect, sort_no, root, keep_alive, affix_tab,
   creator, last_modifier, version, deleted, menu_type,
   active_icon, badge, badge_type, badge_variants, iframe_src, link,
   create_time, last_modified_time)
VALUES (91503, 91500, 'merchant:alipay-isv-auth', 'merchant', 'AlipayIsvAuth',
        'menu.business.alipayIsvAuth', 'lucide:shield-check', false, false,
        '/payment/merchant/alipay-isv-auth/AlipayIsvAuth', '/mch/alipay-isv-auth', NULL, 3, false, false, false,
        0, 1, 1, false, 'menu',
        NULL, NULL, NULL, NULL, NULL, NULL,
        now(), now())
ON CONFLICT (id) DO UPDATE SET
  pid = EXCLUDED.pid,
  menu_code = EXCLUDED.menu_code,
  name = EXCLUDED.name,
  i18n_key = EXCLUDED.i18n_key,
  icon = EXCLUDED.icon,
  hidden = EXCLUDED.hidden,
  component = EXCLUDED.component,
  path = EXCLUDED.path,
  sort_no = EXCLUDED.sort_no,
  menu_type = EXCLUDED.menu_type,
  last_modified_time = now();

-- ===== 商户端菜单结构重构：业务配置目录拆分（数据归属对齐） =====
-- 背景：「业务配置」(91500) 混淆了商户级与应用级配置，按数据模型唯一键拆分：
--   对接配置(91501, mchNo 唯一) → 商户中心(91300) 下
--   通知配置(91502, appId 唯一) → 删除独立总览页，已由应用工作台抽屉(MchAppNotifyConfig)承载
--   支付宝授权(91503, channelMchNo 唯一) → 删除独立菜单，改由通道商户详情页抽屉(AlipayIsvAuthDrawer)承载
--   微信域名验证(91305, mchNo 唯一) → 新增到商户中心下（原为孤儿页，页面已存在未挂菜单）
--   业务配置目录(91500) → 子菜单全部迁出后删除

-- 对接配置移至商户中心下（商户级密钥，与具体应用无关）
UPDATE "public"."iam_perm_menu"
SET pid = 91300, sort_no = 5
WHERE id = 91501;

-- 新增微信域名验证菜单（商户中心下，商户级）
INSERT INTO "public"."iam_perm_menu"
  (id, pid, menu_code, client_code, name, i18n_key, icon, hidden, hide_children_menu,
   component, path, redirect, sort_no, root, keep_alive, affix_tab,
   creator, last_modifier, version, deleted, menu_type,
   active_icon, badge, badge_type, badge_variants, iframe_src, link,
   create_time, last_modified_time)
VALUES (91305, 91300, 'merchant:wx-verify', 'merchant', 'MchWxDomainVerifyList',
        'menu.payment.merchant.wxVerify', 'lucide:shield-check', false, false,
        '/payment/merchant/wx-verify/MchWxDomainVerifyList', '/mch/wx-verify', NULL, 6, false, false, false,
        0, 1, 1, false, 'menu',
        NULL, NULL, NULL, NULL, NULL, NULL,
        now(), now())
ON CONFLICT (id) DO UPDATE SET
  pid = EXCLUDED.pid,
  menu_code = EXCLUDED.menu_code,
  name = EXCLUDED.name,
  i18n_key = EXCLUDED.i18n_key,
  icon = EXCLUDED.icon,
  hidden = EXCLUDED.hidden,
  component = EXCLUDED.component,
  path = EXCLUDED.path,
  sort_no = EXCLUDED.sort_no,
  menu_type = EXCLUDED.menu_type,
  last_modified_time = now();

-- 删除通知配置独立总览页（应用级配置，已由应用工作台抽屉承载）
DELETE FROM "public"."iam_perm_menu" WHERE id = 91502;

-- 删除支付宝授权独立菜单（通道商户级配置，改由通道商户详情抽屉承载）
DELETE FROM "public"."iam_perm_menu" WHERE id = 91503;

-- 删除业务配置目录（子菜单已全部迁出）
DELETE FROM "public"."iam_perm_menu" WHERE id = 91500;
