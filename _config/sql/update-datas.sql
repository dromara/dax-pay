-- ----------------------------
-- 数据升级：菜单展示与 IA 调整 + i18n 改造数据回填
-- 执行顺序：update-tables.sql → update-datas.sql
-- ----------------------------

-- ----------------------------
-- 管理端菜单 IA：支付管理扁平 + 微信域名验证迁系统配置
-- ----------------------------

-- 支付产品配置 → 通道产品配置，升为支付管理二级
UPDATE "public"."iam_perm_menu"
SET "pid" = 4,
    "sort_no" = 2,
    "last_modified_time" = NOW()
WHERE "id" = 40105;

-- 微信域名验证：支付配置 → 系统配置
UPDATE "public"."iam_perm_menu"
SET "pid" = 304,
    "sort_no" = 7,
    "last_modified_time" = NOW()
WHERE "id" = 40507;

-- 支付配置 catalog 已无可见子菜单，隐藏
UPDATE "public"."iam_perm_menu"
SET "hidden" = true,
    "last_modified_time" = NOW()
WHERE "id" = 405;

-- 一级菜单顺序
UPDATE "public"."iam_perm_menu"
SET "sort_no" = 3.5,
    "last_modified_time" = NOW()
WHERE "id" = 404;

UPDATE "public"."iam_perm_menu"
SET "sort_no" = 4,
    "last_modified_time" = NOW()
WHERE "id" = 6;

-- 系统配置排序
UPDATE "public"."iam_perm_menu"
SET "sort_no" = 1,
    "last_modified_time" = NOW()
WHERE "id" = 30402;

UPDATE "public"."iam_perm_menu"
SET "sort_no" = 2,
    "last_modified_time" = NOW()
WHERE "id" = 30401;

-- ----------------------------
-- 商户管理：全局「通道商户」侧栏菜单（占位）
-- ----------------------------
INSERT INTO "public"."iam_perm_menu" (
    "id", "pid", "menu_code", "client_code", "name",
    "i18n_key", "icon",
    "hidden", "hide_children_menu", "component", "path", "redirect",
    "sort_no", "root", "keep_alive", "affix_tab",
    "creator", "last_modifier", "version", "deleted",
    "menu_type", "active_icon", "badge", "badge_type", "badge_variants",
    "iframe_src", "link", "create_time", "last_modified_time"
)
SELECT
    40402,
    404,
    'channel:merchant',
    'admin',
    'ChannelMerchantGlobal',
    'menu.payment.merchant.channelMerchant.global',
    'lucide:repeat',
    false,
    false,
    '/_core/fallback/coming-soon',
    '/payment/merchant/channel-merchants',
    NULL,
    2,
    false,
    true,
    false,
    1,
    1,
    0,
    false,
    'menu',
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NOW(),
    NOW()
WHERE NOT EXISTS (
    SELECT 1 FROM "public"."iam_perm_menu" WHERE "id" = 40402
);

-- ============================================================
-- i18n 改造：批量回填 i18n_key
-- 规则：角色 role.{code}，字典项 dict.{dictCode}.{itemCode}
-- 语言包翻译在前端 locales/langs/{locale}/dict.json 中维护
-- ============================================================

-- 角色表：按 code 生成 i18n_key
UPDATE iam_role
SET i18n_key = 'role.' || code,
    last_modified_time = NOW()
WHERE i18n_key IS NULL
  AND deleted = false;

-- 字典项表：按 dictCode.code 生成 i18n_key
UPDATE system_dict_item
SET i18n_key = 'dict.' || dict_code || '.' || code,
    last_modified_time = NOW()
WHERE i18n_key IS NULL
  AND deleted = false;
