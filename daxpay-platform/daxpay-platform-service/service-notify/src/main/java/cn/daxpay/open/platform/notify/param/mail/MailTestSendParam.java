package cn.daxpay.open.platform.notify.param.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// 邮件测试发送参数
@Data
@Accessors(chain = true)
@Schema(title = "邮件测试发送参数")
public class MailTestSendParam {

    /// 测试收件邮箱
    @NotBlank(message = "{validation.field.receiverEmail.notBlank}")
    @Email(message = "{validation.field.receiverEmail.notValid}")
    @Schema(description = "测试收件邮箱")
    private String receiverEmail;

    /// 邮件主题(为空时使用默认测试主题)
    @Schema(description = "邮件主题")
    private String subject;

    /// 邮件正文(为空时使用默认测试正文)
    @Schema(description = "邮件正文")
    private String content;
}
