-- ====================================================================
-- 增量数据脚本（PostgreSQL）
-- 升级顺序：先 update-tables.sql，再本文件
-- ====================================================================

-- 商户端「支付应用(微信)」菜单（支付配置下，与通道商户/应用管理同级）
INSERT INTO "public"."iam_perm_menu" VALUES
  (91412, 91400, 'payment:wx:mch-app', 'merchant', 'MchWxAppList', 'menu.payment.wx.mchApp',
   'lucide:message-circle', 'f', 'f', '/payment/wx/mch/MchWxAppList', '/mch/wx-app', NULL, 3,
   'f', 'f', 'f', 0, 1, 1, 'f', 'menu', NULL, NULL, NULL, NULL, '', '', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT ("id") DO NOTHING;

-- 应用配置目录让位（原 sort=3 → 4；已是 4 则跳过）
UPDATE "public"."iam_perm_menu"
SET "sort_no" = 4
WHERE "id" = 91403 AND "sort_no" = 3;

-- 挂到所有商户端角色（已授权则跳过）
INSERT INTO "public"."iam_role_menu" ("id", "role_id", "menu_id")
SELECT
  920000000000 + row_number() OVER (ORDER BY r."id"),
  r."id",
  91412
FROM "public"."iam_role" r
WHERE r."client_code" = 'merchant'
  AND NOT EXISTS (
    SELECT 1
    FROM "public"."iam_role_menu" rm
    WHERE rm."role_id" = r."id" AND rm."menu_id" = 91412
  );
