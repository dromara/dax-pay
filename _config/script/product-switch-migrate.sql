-- 2026-09-13 支付产品启停权限码迁移(存量库一次性执行留档): 语句以三个分号组成的行分隔
-- 目标库: daxpay-dev(开源版 dev)

DELETE FROM public.iam_role_code
WHERE code_id = 2070862265027072001
  AND role_id IN (SELECT role_id FROM public.iam_role_code WHERE code_id = 2070862265018683392);
;;;
UPDATE public.iam_role_code SET code_id = 2070862265018683392 WHERE code_id = 2070862265027072001;
;;;
DELETE FROM public.iam_perm_code WHERE id = 2070862265027072001 AND code = 'payment:platform:product:manage';
;;;
SELECT id, code, menu_code FROM public.iam_perm_code WHERE id IN (2070862265027072001, 2070862265018683392) ORDER BY id;
