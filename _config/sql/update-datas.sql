-- 菜单管理收为内部功能（仅超管可用）2026-09-21
-- 背景: 菜单管理(iam:menu 域, 菜单 id=30102)属于内部运维功能, 运营管理员等普通角色不应可见/可用。
-- 处理: 收回所有角色的「菜单管理」菜单授权与 iam:menu 权限码授权; 超管不受影响(超管菜单与权限码均不走角色授权表)。

-- 收回所有角色对「菜单管理」菜单(id=30102)的授权
DELETE FROM iam_role_menu WHERE menu_id = 30102;

-- 收回所有角色对 iam:menu:view / iam:menu:manage 权限码的授权
DELETE FROM iam_role_code rc
  USING iam_perm_code pc
 WHERE rc.code_id = pc.id
   AND pc.code IN ('iam:menu:view', 'iam:menu:manage');
