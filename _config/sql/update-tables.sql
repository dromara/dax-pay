-- ----------------------------
-- 2026-10-05: 邮件模板自定义(运营端按场景按语言覆盖出厂默认槽位, 场景清单在 EmailTemplateEnum)
-- ----------------------------
CREATE TABLE public.iam_mail_template (
    id bigint NOT NULL,
    template_code varchar(50) NOT NULL,
    language varchar(10) NOT NULL,
    subject varchar(200) NOT NULL,
    title varchar(100) NOT NULL,
    content text NOT NULL,
    tip text,
    footer varchar(500),
    creator bigint,
    last_modifier bigint,
    version integer DEFAULT 0 NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    create_time timestamp(6) with time zone,
    last_modified_time timestamp(6) with time zone,
    CONSTRAINT iam_mail_template_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uk_iam_mail_template_code_lang ON public.iam_mail_template (template_code, language) WHERE deleted = false;

COMMENT ON TABLE public.iam_mail_template IS '邮件模板自定义(运营端按场景按语言覆盖出厂默认槽位, 场景清单在 EmailTemplateEnum)';
COMMENT ON COLUMN public.iam_mail_template.id IS '主键';
COMMENT ON COLUMN public.iam_mail_template.template_code IS '模板编码(关联 EmailTemplateEnum.templateName)';
COMMENT ON COLUMN public.iam_mail_template.language IS '语言(zh/en, 关联 MailLanguageEnum.code)';
COMMENT ON COLUMN public.iam_mail_template.subject IS '邮件主题';
COMMENT ON COLUMN public.iam_mail_template.title IS '邮件标题';
COMMENT ON COLUMN public.iam_mail_template.content IS '正文文案(纯文本, 支持 {变量} 占位符)';
COMMENT ON COLUMN public.iam_mail_template.tip IS '提示行文案(空则不输出该行)';
COMMENT ON COLUMN public.iam_mail_template.footer IS '页脚文案(空则不输出该行)';
COMMENT ON COLUMN public.iam_mail_template.creator IS '创建人';
COMMENT ON COLUMN public.iam_mail_template.last_modifier IS '最后修改人';
COMMENT ON COLUMN public.iam_mail_template.version IS '版本号, 使用乐观锁';
COMMENT ON COLUMN public.iam_mail_template.deleted IS '删除标识';
COMMENT ON COLUMN public.iam_mail_template.create_time IS '创建时间';
COMMENT ON COLUMN public.iam_mail_template.last_modified_time IS '最后修改时间';
COMMENT ON INDEX public.uk_iam_mail_template_code_lang IS '同一场景同一语言仅一份覆盖(软删后可重建)';
