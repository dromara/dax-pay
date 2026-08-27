package cn.daxpay.open.platform.notify.service.mail;

import cn.daxpay.open.platform.notify.enums.mail.MailSecurityTypeEnum;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMailConfig;
import cn.daxpay.open.platform.system.service.config.infra.PlatformMailConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

/// # 邮件发送器工厂
///
/// 按平台邮件发件箱配置动态构建 [JavaMailSender] 并缓存;
/// 以配置内容指纹判变更, 配置更新后下次取用时自动重建, 无需跨模块通知失效
@Component
@RequiredArgsConstructor
public class MailSenderFactory {

    private final PlatformMailConfigService mailConfigService;

    /// 缓存的发送器与其对应配置指纹(双重检查锁保护写入)
    private volatile JavaMailSenderImpl cachedSender;
    private volatile String cachedFingerprint;

    /// 获取当前生效的邮件发件箱配置
    public PlatformMailConfig getMailConfig() {
        return mailConfigService.getMailConfig();
    }

    /// 获取邮件发送器(配置指纹变化时自动重建)
    public JavaMailSender getSender() {
        PlatformMailConfig config = this.getMailConfig();
        String fingerprint = this.fingerprint(config);
        if (cachedSender == null || !fingerprint.equals(cachedFingerprint)) {
            synchronized (this) {
                if (cachedSender == null || !fingerprint.equals(cachedFingerprint)) {
                    cachedSender = this.build(config);
                    cachedFingerprint = fingerprint;
                }
            }
        }
        return cachedSender;
    }

    /// 构建 SMTP 连接配置指纹(参与连接语义的字段, enabled 不参与)
    private String fingerprint(PlatformMailConfig config) {
        return StrUtil.join("|",
                config.getHost(), config.getPort(), config.getUsername(), config.getPassword(),
                config.getFrom(), config.getSecurityType(), config.getTimeout());
    }

    private JavaMailSenderImpl build(PlatformMailConfig config) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(config.getHost());
        sender.setPort(config.getPort());
        sender.setUsername(config.getUsername());
        sender.setPassword(config.getPassword());
        sender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // 连接/读取/写超时, 统一秒转毫秒
        int timeoutMs = config.getTimeout() * 1000;
        Properties props = sender.getJavaMailProperties();
        props.put("mail.smtp.connectiontimeout", timeoutMs);
        props.put("mail.smtp.timeout", timeoutMs);
        props.put("mail.smtp.writetimeout", timeoutMs);
        props.put("mail.smtp.auth", "true");
        String securityType = config.getSecurityType();
        if (MailSecurityTypeEnum.starttls.getCode().equals(securityType)) {
            props.put("mail.smtp.starttls.enable", "true");
        } else if (MailSecurityTypeEnum.ssl.getCode().equals(securityType)) {
            props.put("mail.smtp.ssl.enable", "true");
        }
        return sender;
    }
}
