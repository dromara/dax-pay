-- 敏感词菜单（系统管理 · 方案 A）：catalog + 词库 + 命中
-- 策略开关在「平台配置」Tab，不新增菜单
-- 不预置敏感词词条
-- 列序对齐 iam_perm_menu 全量导出

INSERT INTO "public"."iam_perm_menu" VALUES
(311, 3, 'system:sensitive', 'admin', 'SensitiveWord', 'menu.system.sensitive', 'lucide:shield-ban',
 'f', 'f', NULL, '/system/sensitive', NULL, 15,
 'f', 't', 'f', 1, 1, 0, 'f', 'catalog',
 NULL, NULL, NULL, NULL, NULL, NULL,
 NOW(), NOW())
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
(31101, 311, 'system:sensitive-word', 'admin', 'SensitiveWordList', 'menu.system.sensitive.word', 'lucide:book-x',
 'f', 'f', '/system/sensitive-word/SensitiveWordList', '/system/sensitive/word', NULL, 1,
 'f', 't', 'f', 1, 1, 0, 'f', 'menu',
 NULL, NULL, NULL, NULL, NULL, NULL,
 NOW(), NOW())
ON CONFLICT ("id") DO NOTHING;

INSERT INTO "public"."iam_perm_menu" VALUES
(31102, 311, 'system:sensitive-word-hit', 'admin', 'SensitiveWordHitList', 'menu.system.sensitive.hit', 'lucide:scan-search',
 'f', 'f', '/system/sensitive-word/SensitiveWordHitList', '/system/sensitive/hit', NULL, 2,
 'f', 't', 'f', 1, 1, 0, 'f', 'menu',
 NULL, NULL, NULL, NULL, NULL, NULL,
 NOW(), NOW())
ON CONFLICT ("id") DO NOTHING;
