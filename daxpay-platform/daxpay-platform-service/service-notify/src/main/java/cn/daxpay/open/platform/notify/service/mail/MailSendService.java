package cn.daxpay.open.platform.notify.service.mail;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.notify.dao.mail.NotifyMailRecordManager;
import cn.daxpay.open.platform.notify.entity.mail.NotifyMailRecord;
import cn.daxpay.open.platform.notify.enums.mail.MailBusinessTypeEnum;
import cn.daxpay.open.platform.notify.enums.mail.MailSendStatusEnum;
import cn.daxpay.open.platform.notify.param.mail.MailTestSendParam;
import cn.daxpay.open.platform.system.entity.config.platform.infra.PlatformMailConfig;
import cn.hutool.core.util.StrUtil;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

/// # 邮件发送服务
///
/// 平台邮件通知外发统一入口: 测试发送(同步) / 业务发送(异步) / 失败重发,
/// 所有发送行为均落 [NotifyMailRecord] 记录
@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendService {

    private final MailSenderFactory mailSenderFactory;

    private final NotifyMailRecordManager recordManager;

    /// 错误信息落库截断长度(预留 errorMsg 列宽余量)
    private static final int ERROR_MSG_MAX_LENGTH = 1900;

    /// 测试发送(同步执行, 供配置页即时验证; 不受通道开关限制)
    public void testSend(MailTestSendParam param) {
        PlatformMailConfig config = this.checkConfig(mailSenderFactory.getMailConfig());
        // 主题与正文未填时使用默认测试内容
        String subject = StrUtil.blankToDefault(param.getSubject(), I18nUtil.get("notify.mail.testSubject"));
        String content = StrUtil.blankToDefault(param.getContent(), this.defaultTestContent());
        NotifyMailRecord record = new NotifyMailRecord()
                .setReceiverEmail(param.getReceiverEmail())
                .setSubject(subject)
                .setContent(content)
                .setBusinessType(MailBusinessTypeEnum.test.getCode())
                .setStatus(MailSendStatusEnum.sending.getCode())
                .setRetryCount(0)
                .setSendTime(OffsetDateTime.now());
        recordManager.save(record);
        this.doSend(record, config);
    }

    /// 业务异步发送入口(预留, 二期挂接找回密码等业务场景)
    ///
    /// 通道开关关闭时静默跳过, 不落发送记录
    @Async
    public void asyncSend(String receiverEmail, Long receiverUserId, String subject, String content, MailBusinessTypeEnum businessType) {
        PlatformMailConfig config = mailSenderFactory.getMailConfig();
        if (!config.getEnabled()) {
            log.info("邮件通道未启用, 跳过业务邮件发送: receiver={}, businessType={}", receiverEmail, businessType.getCode());
            return;
        }
        try {
            this.checkConfig(config);
        } catch (BizInfoException e) {
            log.warn("邮件发件箱配置不完整, 跳过业务邮件发送: {}", e.getMessage());
            return;
        }
        NotifyMailRecord record = new NotifyMailRecord()
                .setReceiverEmail(receiverEmail)
                .setReceiverUserId(receiverUserId)
                .setSubject(subject)
                .setContent(content)
                .setBusinessType(businessType.getCode())
                .setStatus(MailSendStatusEnum.sending.getCode())
                .setRetryCount(0)
                .setSendTime(OffsetDateTime.now());
        recordManager.save(record);
        this.doSend(record, config);
    }

    /// 失败重发(同步执行, 供记录页手动触发)
    public void resend(Long id) {
        NotifyMailRecord record = recordManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.notify.mail.notExist"));
        if (!MailSendStatusEnum.fail.getCode().equals(record.getStatus())) {
            // 邮件: 仅失败状态可重发
            throw new BizInfoException("error.notify.mail.notFail");
        }
        PlatformMailConfig config = this.checkConfig(mailSenderFactory.getMailConfig());
        record.setRetryCount(record.getRetryCount() + 1)
                .setStatus(MailSendStatusEnum.sending.getCode())
                .setSendTime(OffsetDateTime.now());
        recordManager.updateById(record);
        this.doSend(record, config);
    }

    /// 执行 SMTP 发送并回写记录状态
    ///
    /// 测试发送失败时抛出业务异常(前端需即时看到失败原因), 其余场景仅落记录
    private void doSend(NotifyMailRecord record, PlatformMailConfig config) {
        try {
            JavaMailSender sender = mailSenderFactory.getSender();
            var message = sender.createMimeMessage();
            var helper = new MimeMessageHelper(message, false, StandardCharsets.UTF_8.name());
            // 发件地址缺省取发件账号, 配置了昵称时以 "昵称 <地址>" 形式展示
            String from = StrUtil.blankToDefault(config.getFrom(), config.getUsername());
            if (StrUtil.isNotBlank(config.getNickname())) {
                helper.setFrom(new InternetAddress(from, config.getNickname(), StandardCharsets.UTF_8.name()));
            } else {
                helper.setFrom(from);
            }
            helper.setTo(record.getReceiverEmail());
            helper.setSubject(record.getSubject());
            // 正文按 HTML 发送
            helper.setText(record.getContent(), true);
            sender.send(message);
            record.setStatus(MailSendStatusEnum.success.getCode())
                    // 置空串而非 null, updateById 忽略 null 字段, 否则历史失败原因残留
                    .setErrorMsg("");
            recordManager.updateById(record);
        } catch (Exception e) {
            log.warn("邮件发送失败: receiver={}, subject={}, msg={}", record.getReceiverEmail(), record.getSubject(), e.getMessage());
            record.setStatus(MailSendStatusEnum.fail.getCode())
                    .setErrorMsg(StrUtil.maxLength(e.getMessage(), ERROR_MSG_MAX_LENGTH));
            recordManager.updateById(record);
            if (MailBusinessTypeEnum.test.getCode().equals(record.getBusinessType())) {
                // BizInfoException 无 (String, Object...) 构造, 带参数需显式传默认错误码
                throw new BizInfoException(CommonCode.FAIL_CODE, "error.notify.mail.sendFailed", e.getMessage());
            }
        }
    }

    /// 校验发件箱配置完整性, 返回校验通过的配置
    private PlatformMailConfig checkConfig(PlatformMailConfig config) {
        if (StrUtil.hasBlank(config.getHost(), config.getUsername(), config.getPassword())) {
            throw new BizInfoException("error.notify.mail.configMissing");
        }
        return config;
    }

    /// 默认测试邮件正文(简单 HTML)
    private String defaultTestContent() {
        return "<p>" + I18nUtil.get("notify.mail.testBody") + "</p>";
    }
}
