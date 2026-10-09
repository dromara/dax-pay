package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.iam.dao.mail.IamMailTemplateManager;
import cn.daxpay.open.platform.notify.service.mail.MailSendService;
import cn.daxpay.open.platform.notify.service.mail.MailSenderFactory;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMailConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformWebsiteConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/// # 邮箱验证邮件模板服务
///
/// 邮箱绑定/找回密码场景的验证码与通知邮件渲染和发送:
/// 按请求语言选定中英槽位(中文语种发中文, 其余一律英文),
/// 槽位文案取运营端覆盖(iam_mail_template 表), 无覆盖时用 [EmailTemplateEnum] 出厂默认,
/// 交给 [MailSkeletonRenderer] 以定死样式的骨架渲染;
/// 发送走 [MailSendService#asyncSend] 异步落库链路,
/// 调用方须先经 [checkMailReady] 同步预检通道配置, 未配置/未启用时明确报错,
/// 区别于 asyncSend 自身的静默跳过语义, 避免用户误以为验证码已发出
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final MailSenderFactory mailSenderFactory;

    private final MailSendService mailSendService;

    private final IamMailTemplateManager mailTemplateManager;

    private final PlatformWebsiteConfigService websiteConfigService;

    /// 验证码有效期(分钟, 与默认模板提示文案保持一致)
    public static final int CODE_EXPIRE_MINUTES = 5;

    /// 渲染并发送邮件(按请求语言选定中英槽位, 覆盖优先无则出厂默认, 异步落库发送)
    ///
    /// 品牌条读平台系统名称(未配置回落 DaxPay), 主题前缀仅在系统名称已配置时添加
    public void send(String receiverEmail, Long receiverUserId, EmailTemplateEnum template, Map<String, Object> params) {
        MailLanguageEnum language = this.isChineseLocale() ? MailLanguageEnum.zh : MailLanguageEnum.en;
        MailSlots slots = this.loadSlots(template, language);
        String systemName = websiteConfigService.getWebsiteConfig().getSystemName();
        String brandName = StrUtil.blankToDefault(systemName, MailSkeletonRenderer.DEFAULT_BRAND);
        String subject = MailSkeletonRenderer.renderSubject(slots.subject(), params, systemName);
        String content = MailSkeletonRenderer.render(template.getSkeleton(), slots, params, brandName, language);
        mailSendService.asyncSend(receiverEmail, receiverUserId, subject, content, template.getBusinessType());
    }

    /// 加载指定语言的槽位取值(运营端覆盖优先, 无覆盖用出厂默认)
    ///
    /// tip/footer 无兜底: 清空即不输出该行, 是运营端明确的编辑意图;
    /// subject/title/content 有必填校验, 空值兜底回默认仅作防御
    public MailSlots loadSlots(EmailTemplateEnum template, MailLanguageEnum language) {
        MailSlots defaults = template.defaultSlots(language);
        return mailTemplateManager.findByCodeAndLanguage(template.getTemplateName(), language.getCode())
                .map(override -> new MailSlots(
                        StrUtil.blankToDefault(override.getSubject(), defaults.subject()),
                        StrUtil.blankToDefault(override.getTitle(), defaults.title()),
                        StrUtil.blankToDefault(override.getContent(), defaults.content()),
                        override.getTip(),
                        override.getFooter()))
                .orElse(defaults);
    }

    /// 邮件通道是否就绪(已启用且 host/username/password 配置完整)
    ///
    /// 只读判断不抛异常, 供找回密码可用通道探测等状态查询场景使用;
    /// 发码等需要明确报错的场景改用 [#checkMailReady]
    public boolean isMailReady() {
        PlatformMailConfig config = mailSenderFactory.getMailConfig();
        return config.getEnabled() && !StrUtil.hasBlank(config.getHost(), config.getUsername(), config.getPassword());
    }

    /// 同步预检邮件通道是否可用(未启用或配置不完整抛业务异常)
    public void checkMailReady() {
        if (!this.isMailReady()) {
            // 邮箱: 邮件服务未配置或未启用
            throw new BizInfoException("error.iam.email.mailNotReady");
        }
    }

    /// 判定当前请求是否中文语境(简繁统一中文模板)
    private boolean isChineseLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        return Objects.nonNull(locale) && locale.getLanguage().equalsIgnoreCase("zh");
    }
}
