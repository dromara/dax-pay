package cn.bootx.platform.notice.core.mail.service;

import cn.bootx.platform.notice.exception.MailConfigNotExistException;
import cn.bootx.platform.notice.service.EmailNoticeSender;
import cn.bootx.platform.notice.code.MailCode;
import cn.bootx.platform.notice.dto.mail.MailConfigDto;
import cn.bootx.platform.notice.dto.mail.MailFileParam;
import cn.bootx.platform.notice.dto.mail.SendMailParam;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 邮件发送服务
 *
 * @author xxm
 * @since 2020/5/2 16:06
 */
@Slf4j
@Service
@AllArgsConstructor
public class EmailNoticeSenderImpl implements EmailNoticeSender {

    /**
     * 默认 MIME Type
     */
    private static final String MIME_TYPE_DEFAULT = "text/html; charset=utf-8";

    private final MailConfigService mailConfigService;

    /**
     * 简单邮件发送
     * @param email 邮件地址
     * @param subject 邮件标题
     * @param msg 邮件消息
     */
    @Override
    public void sentSimpleMail(String email, String subject, String msg) {
        SendMailParam mailParam = new SendMailParam();
        mailParam.setTo(Collections.singletonList(email));
        mailParam.setSubject(subject);
        mailParam.setMessage(msg);
        SpringUtil.getBean(getClass()).sendMail(mailParam);
    }

    /**
     * 标准邮件发送
     */
    @SneakyThrows
    @Override
    public void sendMail(SendMailParam mailParam) {
        log.info("开始发送邮件");
        MailConfigDto mailConfig = this.getMailConfig(mailParam.getConfigCode());
        JavaMailSenderImpl mailSender = this.getMailSender(mailConfig);
        List<MimeMessage> mimeMessageList = new ArrayList<>();

        if (CollUtil.isEmpty(mailParam.getTo())) {
            return;
        }
        // 获取to cc bcc 中所有允许发送的receiver
        HashSet<String> allReceivers = Sets.newHashSet(mailParam.getTo());
        // 密送
        if (CollUtil.isNotEmpty(mailParam.getBccList())) {
            allReceivers.addAll(mailParam.getBccList());
        }
        // 抄送
        if (CollUtil.isNotEmpty(mailParam.getCcList())) {
            allReceivers.addAll(mailParam.getCcList());
        }
        // 设置接收人
        Set<String> receivers = Sets.intersection(allReceivers, new HashSet<>(mailParam.getTo()));
        if (CollUtil.isEmpty(receivers)) {
            return;
        }
        // 是否单条发送(拆分收件人)
        if (mailParam.getSingleSend()) {
            for (String to : receivers) {
                this.buildMailParam(mailParam, mailConfig, mailSender, mimeMessageList, allReceivers, to);
            }
        }
        else {
            this.buildMailParam(mailParam, mailConfig, mailSender, mimeMessageList, allReceivers,
                    ArrayUtil.toArray(receivers, String.class));
        }

        // 调用发送
        mailSender.send(ArrayUtil.toArray(mimeMessageList, MimeMessage.class));
        log.info("SendMail结束");
    }

    /**
     * 构建邮件参数
     * @param mailParam 发邮件的参数
     * @param mailConfig 邮箱配置
     * @param mailSender 邮件发送器
     * @param mimeMessageList 多媒体信息
     * @param allReceivers 接收人
     * @param to 发送人
     * @throws MessagingException 消息异常
     * @throws UnsupportedEncodingException 不支持的编码异常
     */
    private void buildMailParam(SendMailParam mailParam, MailConfigDto mailConfig, JavaMailSenderImpl mailSender,
            List<MimeMessage> mimeMessageList, Set<String> allReceivers, String... to)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setFrom(new InternetAddress(mailConfig.getFrom(), mailConfig.getSender()));
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(mailParam.getSubject());

        // 处理抄送
        List<String> ccList = mailParam.getCcList();
        if (CollUtil.isNotEmpty(ccList)) {
            Set<String> ccReceivers = Sets.intersection(allReceivers, Sets.newHashSet(ccList));
            if (!CollectionUtil.isEmpty(ccReceivers)) {
                String[] ccReceiverArray = new String[ccReceivers.size()];
                ccReceivers.toArray(ccReceiverArray);
                mimeMessageHelper.setCc(ccReceiverArray);
            }
        }

        // 处理密送
        List<String> bccList = mailParam.getBccList();
        if (CollUtil.isNotEmpty(bccList)) {
            Set<String> bccReceivers = Sets.intersection(allReceivers, Sets.newHashSet(mailParam.getBccList()));
            if (!CollectionUtil.isEmpty(bccReceivers)) {
                String[] bccReceiverArray = new String[bccReceivers.size()];
                bccReceivers.toArray(bccReceiverArray);
                mimeMessageHelper.setBcc(ArrayUtil.toArray(bccList, String.class));
            }
        }

        // 创建一个消息部分来代表正文
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mailParam.getMessage(), MIME_TYPE_DEFAULT);
        // 使用MimeMultipart，因为我们需要处理文件附件
        Multipart multipart = new MimeMultipart();

        // 判断是否包含附件
        if (mailParam.isSendAttachment()) {
            this.buildAttachmentParam(mailParam, multipart);
        }

        // 添加正文
        multipart.addBodyPart(messageBodyPart);
        // 将所有消息部分放入消息中
        mimeMessage.setContent(multipart);
        mimeMessageList.add(mimeMessage);

    }

    /**
     * 构建附件参数
     */
    private void buildAttachmentParam(SendMailParam mailParam, Multipart multipart) throws MessagingException {
        // 获得附件集合 进行发送附件
        List<MailFileParam> files = mailParam.getFileList();
        if (!CollectionUtil.isEmpty(files)) {
            for (MailFileParam f : files) {

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                // 附件数据源:根据文件字节放入数据源
                DataSource dss = new ByteArrayDataSource(f.getFileInputStream(), "application/png");
                // 数据处理器
                DataHandler dh = new DataHandler(dss);
                mimeBodyPart.setFileName(f.getFileName());
                mimeBodyPart.setDataHandler(dh);
                multipart.addBodyPart(mimeBodyPart);
            }
        }
    }

    /**
     * 获取邮件配置
     */
    private MailConfigDto getMailConfig(String configCode) {
        MailConfigDto mailConfig;

        // 无code获取默认
        if (StrUtil.isEmpty(configCode)) {
            mailConfig = mailConfigService.getDefaultConfig();
        }
        else {
            mailConfig = mailConfigService.getByCode(configCode);
        }
        if (mailConfig == null) {
            throw new MailConfigNotExistException();
        }
        return mailConfig;
    }

    /**
     * 获取邮件发件器
     */
    private JavaMailSenderImpl getMailSender(MailConfigDto mailConfig) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());

        Properties props = new Properties();
        // 判断是否是TLS
        if (Objects.equals(Optional.ofNullable(mailConfig.getSecurityType()).orElse(0), MailCode.SECURITY_TYPE_TLS)) {
            props.setProperty("mail.smtp.starttls.enable", "true");
        }
        else if (Objects.equals(Optional.ofNullable(mailConfig.getSecurityType()).orElse(0),
                MailCode.SECURITY_TYPE_SSL)) {
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.socketFactory.port", mailConfig.getPort() + "");
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
        }
        mailSender.setJavaMailProperties(props);

        return mailSender;
    }

}
