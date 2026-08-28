package cn.daxpay.open.platform.iam.auth.service.email;

import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.notify.service.mail.MailSendService;
import cn.daxpay.open.platform.notify.service.mail.MailSenderFactory;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMailConfig;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/// # 邮箱验证邮件模板服务
///
/// 邮箱绑定/找回密码场景的验证码与通知邮件渲染和发送:
/// 中英双模板按请求语言二选一(中文语种发中文模板, 其余一律英文模板);
/// 发送走 [MailSendService#asyncSend] 异步落库链路,
/// 调用方须先经 [checkMailReady] 同步预检通道配置, 未配置/未启用时明确报错,
/// 区别于 asyncSend 自身的静默跳过语义, 避免用户误以为验证码已发出
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final MailSenderFactory mailSenderFactory;

    private final MailSendService mailSendService;

    /// classpath 邮件模板根路径
    private static final String TEMPLATE_PATH = "mail/";

    /// 验证码有效期(分钟, 与模板提示文案保持一致)
    public static final int CODE_EXPIRE_MINUTES = 5;

    /// 渲染并发送邮件(按请求语言选中英模板, 异步落库发送)
    public void send(String receiverEmail, Long receiverUserId, EmailTemplateEnum template, Map<String, Object> params) {
        boolean chinese = this.isChineseLocale();
        String subject = chinese ? template.getSubjectZh() : template.getSubjectEn();
        String content = StrUtil.format(this.loadTemplate(template.getTemplateName(), chinese), params);
        mailSendService.asyncSend(receiverEmail, receiverUserId, subject, content, template.getBusinessType());
    }

    /// 同步预检邮件通道是否可用(未启用或配置不完整抛业务异常)
    public void checkMailReady() {
        PlatformMailConfig config = mailSenderFactory.getMailConfig();
        if (!config.getEnabled() || StrUtil.hasBlank(config.getHost(), config.getUsername(), config.getPassword())) {
            // 邮箱: 邮件服务未配置或未启用
            throw new BizInfoException("error.iam.email.mailNotReady");
        }
    }

    /// 判定当前请求是否中文语境(简繁统一中文模板)
    private boolean isChineseLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        return locale != null && locale.getLanguage().equalsIgnoreCase("zh");
    }

    /// 加载 classpath 邮件模板并做命名占位符渲染前的原文读取
    private String loadTemplate(String templateName, boolean chinese) {
        String fileName = templateName + (chinese ? ".zh" : ".en") + ".html";
        try {
            ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH + fileName);
            return resource.getContentAsString(StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            log.error("邮件模板加载失败: {}", fileName, e);
            // 邮箱: 邮件模板加载失败(服务端配置问题)
            throw new BizInfoException("error.iam.email.templateError");
        }
    }
}
