package cn.daxpay.open.platform.iam.param.mail;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// 邮件模板测试发送参数
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Schema(title = "邮件模板测试发送参数")
public class MailTemplateSendTestParam extends MailTemplatePreviewParam {

    /// 测试收件邮箱
    @NotBlank(message = "{validation.field.receiverEmail.notBlank}")
    @Email(message = "{validation.field.receiverEmail.notValid}")
    @Schema(description = "测试收件邮箱")
    private String receiverEmail;
}
