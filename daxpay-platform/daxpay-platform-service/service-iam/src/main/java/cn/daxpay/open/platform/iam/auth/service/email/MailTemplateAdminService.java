package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.iam.dao.mail.IamMailTemplateManager;
import cn.daxpay.open.platform.iam.entity.mail.IamMailTemplate;
import cn.daxpay.open.platform.iam.param.mail.MailTemplatePreviewParam;
import cn.daxpay.open.platform.iam.param.mail.MailTemplateSendTestParam;
import cn.daxpay.open.platform.iam.param.mail.MailTemplateUpsertParam;
import cn.daxpay.open.platform.iam.result.mail.MailPreviewResult;
import cn.daxpay.open.platform.iam.result.mail.MailTemplateResult;
import cn.daxpay.open.platform.notify.param.mail.MailTestSendParam;
import cn.daxpay.open.platform.notify.service.mail.MailSendService;
import cn.daxpay.open.platform.system.service.config.infra.PlatformWebsiteConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/// # 邮件模板管理服务(运营端)
///
/// 邮件模板按场景枚举固定六个, 每场景中英两语言各一份五槽位覆盖, 本服务只做覆盖的增删查:
/// 列表按语言分别合并出厂默认与库覆盖展示当前生效值, 保存前校验占位符,
/// 预览/测试发送用示例变量值渲染, 其中测试发送复用 [MailSendService#testSend] 同步链路
@Service
@RequiredArgsConstructor
public class MailTemplateAdminService {

    private final IamMailTemplateManager mailTemplateManager;

    private final EmailTemplateService emailTemplateService;

    private final MailSendService mailSendService;

    private final PlatformWebsiteConfigService websiteConfigService;

    /// 模板列表(场景枚举序, 中英两语言各自合并库覆盖为当前生效值)
    public List<MailTemplateResult> list() {
        Map<String, IamMailTemplate> overrides = mailTemplateManager.lambdaQuery()
                .list()
                .stream()
                .collect(Collectors.toMap(item -> item.getTemplateCode() + ":" + item.getLanguage(), Function.identity()));
        return Arrays.stream(EmailTemplateEnum.values())
                .map(template -> new MailTemplateResult()
                        .setTemplateCode(template.getTemplateName())
                        .setName(template.getName())
                        .setZh(this.buildSlotBlock(template, MailLanguageEnum.zh, overrides))
                        .setEn(this.buildSlotBlock(template, MailLanguageEnum.en, overrides))
                        .setVariables(Arrays.stream(template.getVariables())
                                .map(v -> new MailTemplateResult.VariableResult().setName(v.getName()).setDesc(v.getDesc()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    /// 保存槽位覆盖(指定场景与语言, 存在即更新, 不存在即新增)
    @Transactional(rollbackFor = Exception.class)
    public void update(MailTemplateUpsertParam param) {
        EmailTemplateEnum template = this.requireTemplate(param.getTemplateCode());
        MailLanguageEnum language = this.requireLanguage(param.getLanguage());
        this.checkVariables(template, param.getSubject(), param.getContent(), param.getTip());
        IamMailTemplate entity = mailTemplateManager.findByCodeAndLanguage(template.getTemplateName(), language.getCode())
                .orElseGet(() -> new IamMailTemplate().setTemplateCode(template.getTemplateName()).setLanguage(language.getCode()));
        entity.setSubject(param.getSubject())
                .setTitle(param.getTitle())
                .setContent(param.getContent())
                // 空白提示/页脚规范为 null, 渲染时不输出该行
                .setTip(StrUtil.isBlank(param.getTip()) ? null : param.getTip())
                .setFooter(StrUtil.isBlank(param.getFooter()) ? null : param.getFooter());
        mailTemplateManager.saveOrUpdate(entity);
    }

    /// 恢复出厂默认(删除指定场景与语言的槽位覆盖)
    @Transactional(rollbackFor = Exception.class)
    public void reset(String templateCode, String language) {
        EmailTemplateEnum template = this.requireTemplate(templateCode);
        MailLanguageEnum lang = this.requireLanguage(language);
        mailTemplateManager.deleteByCodeAndLanguage(template.getTemplateName(), lang.getCode());
    }

    /// 预览渲染(指定语言, 空槽位以当前生效值补齐, 示例变量值渲染)
    public MailPreviewResult preview(MailTemplatePreviewParam param) {
        EmailTemplateEnum template = this.requireTemplate(param.getTemplateCode());
        MailLanguageEnum language = this.requireLanguage(param.getLanguage());
        MailSlots slots = this.mergeSlots(template, language, param);
        this.checkVariables(template, slots.subject(), slots.content(), slots.tip());
        Map<String, Object> sampleParams = template.sampleParams(EmailTemplateService.CODE_EXPIRE_MINUTES);
        // 品牌条与主题前缀读平台系统名称, 与实际发送链路同源
        String systemName = websiteConfigService.getWebsiteConfig().getSystemName();
        String brandName = StrUtil.blankToDefault(systemName, MailSkeletonRenderer.DEFAULT_BRAND);
        String subject = MailSkeletonRenderer.renderSubject(slots.subject(), sampleParams, systemName);
        String html = MailSkeletonRenderer.render(template.getSkeleton(), slots, sampleParams, brandName, language);
        return new MailPreviewResult().setSubject(subject).setHtml(html);
    }

    /// 发送预览邮件到指定邮箱(同步发送, 复用测试发送链路并落发送记录)
    public void sendTest(MailTemplateSendTestParam param) {
        MailPreviewResult rendered = this.preview(param);
        mailSendService.testSend(new MailTestSendParam()
                .setReceiverEmail(param.getReceiverEmail())
                .setSubject(rendered.getSubject())
                .setContent(rendered.getHtml()));
    }

    /// 组装单语言槽位块(覆盖行存在则以覆盖值为生效值, 否则出厂默认)
    private MailTemplateResult.SlotBlock buildSlotBlock(EmailTemplateEnum template, MailLanguageEnum language,
            Map<String, IamMailTemplate> overrides) {
        IamMailTemplate override = overrides.get(template.getTemplateName() + ":" + language.getCode());
        MailSlots defaults = template.defaultSlots(language);
        MailSlots effective = override != null
                ? new MailSlots(override.getSubject(), override.getTitle(), override.getContent(), override.getTip(), override.getFooter())
                : defaults;
        return new MailTemplateResult.SlotBlock()
                .setLanguage(language.getCode())
                .setCustomized(override != null)
                .setSubject(effective.subject())
                .setTitle(effective.title())
                .setContent(effective.content())
                .setTip(effective.tip())
                .setFooter(effective.footer())
                .setDefaultSubject(defaults.subject())
                .setDefaultTitle(defaults.title())
                .setDefaultContent(defaults.content())
                .setDefaultTip(defaults.tip())
                .setDefaultFooter(defaults.footer());
    }

    /// 合并预览参数与当前生效槽位(未传的字段用生效值, 传空串视为明确清空)
    private MailSlots mergeSlots(EmailTemplateEnum template, MailLanguageEnum language, MailTemplatePreviewParam param) {
        MailSlots current = emailTemplateService.loadSlots(template, language);
        return new MailSlots(
                param.getSubject() != null ? param.getSubject() : current.subject(),
                param.getTitle() != null ? param.getTitle() : current.title(),
                param.getContent() != null ? param.getContent() : current.content(),
                param.getTip() != null ? param.getTip() : current.tip(),
                param.getFooter() != null ? param.getFooter() : current.footer());
    }

    /// 校验槽位文案占位符: 出现场景未声明的变量直接拒绝
    ///
    /// 未声明变量若放行, [MailSkeletonRenderer] 渲染时会以 {变量名} 原样出现在邮件中
    private void checkVariables(EmailTemplateEnum template, String subject, String content, String tip) {
        Set<String> declared = Arrays.stream(template.getVariables())
                .map(MailTemplateVariable::getName)
                .collect(Collectors.toSet());
        for (String text : List.of(subject, content, tip)) {
            for (String name : MailSkeletonRenderer.extractVariables(text)) {
                if (!declared.contains(name)) {
                    // 邮箱: 文案存在未知变量, 该场景可用变量见提示
                    throw new BizInfoException(CommonCode.FAIL_CODE, "error.iam.email.unknownVariable", name,
                            String.join(", ", declared));
                }
            }
        }
    }

    /// 按编码取场景(不存在即报错)
    private EmailTemplateEnum requireTemplate(String templateCode) {
        EmailTemplateEnum template = EmailTemplateEnum.findByTemplateName(templateCode);
        if (template == null) {
            // 邮箱: 邮件模板不存在
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.iam.email.templateNotFound", templateCode);
        }
        return template;
    }

    /// 按编码取语言(不合法即报错)
    private MailLanguageEnum requireLanguage(String language) {
        MailLanguageEnum lang = MailLanguageEnum.findByCode(language);
        if (lang == null) {
            // 邮箱: 邮件语言不合法
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.iam.email.languageInvalid", language);
        }
        return lang;
    }
}
