-- ----------------------------
-- 数据升级：对外业务命名统一（菜单 title）
-- 执行顺序：update-tables.sql → update-datas.sql
-- 说明：仅更新展示文案，不改 menu code / path / 权限标识
-- ----------------------------

-- 通道路由：英文 Channel Routing → Channel Binding
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '通道路由',
    "title_en" = 'Channel Binding',
    "last_modified_time" = NOW()
WHERE "id" = 4040111;

-- 支付产品管理 → 支付产品配置
UPDATE "public"."iam_perm_menu"
SET "title_cn" = '支付产品配置',
    "title_en" = 'Payment Product Config',
    "last_modified_time" = NOW()
WHERE "id" = 40105;

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
