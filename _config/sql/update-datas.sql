-- ----------------------------
-- 数据升级：菜单展示与 IA 调整
-- 执行顺序：update-tables.sql → update-datas.sql
-- 说明：仅更新展示文案 / 父级 / 排序 / 隐藏 / 新增占位菜单；不改 menu_code / path / 权限标识（除新菜单）
-- ----------------------------

-- 通道路由：英文 Channel Routing → Channel Binding
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '通道路由',
    "title_en" = 'Channel Binding',
    "last_modified_time" = NOW()
WHERE "id" = 4040111;

-- 渠道应用 → 通道应用配置
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '通道应用配置',
    "title_en" = 'Channel App Config',
    "last_modified_time" = NOW()
WHERE "id" = 4040132;

-- 渠道服务商配置 → 通道服务商配置
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '通道服务商配置',
    "title_en" = 'Channel ISV Config',
    "last_modified_time" = NOW()
WHERE "id" = 40508;

-- 对接配置英文 Credential Config → API Credentials
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '对接配置',
    "title_en" = 'API Credentials',
    "last_modified_time" = NOW()
WHERE "id" = 4040102;

-- 码牌和聚合支付 → 聚合收款配置
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '聚合收款配置',
    "title_en" = 'Aggregate Pay Config',
    "last_modified_time" = NOW()
WHERE "id" = 4040121;

-- ----------------------------
-- 管理端菜单 IA：支付管理扁平 + 微信域名验证迁系统配置
-- ----------------------------

-- 平台管理 → 支付管理
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '支付管理',
    "title_en" = 'Payment Management',
    "last_modified_time" = NOW()
WHERE "id" = 4;

-- 支付产品配置 → 通道产品配置，升为支付管理二级
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '通道产品配置',
    "title_en" = 'Channel Product Config',
    "pid" = 4,
    "sort_no" = 2,
    "last_modified_time" = NOW()
WHERE "id" = 40105;

-- 微信域名验证：支付配置 → 系统配置（与平台配置同级区）
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

-- 一级菜单顺序：支付管理(3) → 商户管理(3.5) → 交易管理(4) → 设备管理(4.2)
UPDATE "public"."iam_perm_menu"
SET "sort_no" = 3.5,
    "last_modified_time" = NOW()
WHERE "id" = 404;

UPDATE "public"."iam_perm_menu"
SET "sort_no" = 4,
    "last_modified_time" = NOW()
WHERE "id" = 6;

-- 系统配置：平台配置在前，安全配置在后
UPDATE "public"."iam_perm_menu"
SET "sort_no" = 1,
    "last_modified_time" = NOW()
WHERE "id" = 30402;

UPDATE "public"."iam_perm_menu"
SET "sort_no" = 2,
    "last_modified_time" = NOW()
WHERE "id" = 30401;

-- ----------------------------
-- 商户管理：全局「通道商户」侧栏菜单（占位，页面后续实现）
-- id=40402；复用 menu_code=channel:merchant（与现有通道商户权限一致）
-- component 暂用 coming-soon；path 预留全局列表路由
-- ----------------------------
INSERT INTO "public"."iam_perm_menu" (
    "id", "pid", "menu_code", "client_code", "name",
    "title_cn", "title_en", "i18n_key", "icon",
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
    '通道商户',
    'Channel Merchant',
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
