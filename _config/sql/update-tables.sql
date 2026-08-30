-- 表结构更新(2026-08-30): notify_mail_record 两列注释精简为紧凑枚举值风格
COMMENT ON COLUMN public.notify_mail_record.business_type IS '业务场景(test/manual等)';
COMMENT ON COLUMN public.notify_mail_record.status IS '发送状态(sending/success/fail)';
