package cn.daxpay.open.platform.iam.param.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 找回密码验证码发送参数
///
@Data
@Accessors(chain = true)
@Schema(title = "找回密码验证码发送参数")
public class ForgetSendCodeParam {

    @Schema(description = "终端Code")
    @NotBlank(message = "{validation.field.clientId.notBlank}")
    private String clientId;

    @Schema(description = "用户账户(登录账号)")
    @NotBlank(message = "{validation.field.account.notBlank}")
    private String account;

    @Schema(description = "已绑定的邮箱")
    @NotBlank(message = "{validation.field.email.notBlank}")
    @Email(message = "{validation.field.email.format}")
    private String email;

    @Schema(description = "图形验证码key")
    @NotBlank(message = "{validation.field.captchaKey.notBlank}")
    private String captchaKey;

    @Schema(description = "图形验证码")
    @NotBlank(message = "{validation.field.captchaCode.notBlank}")
    private String captchaCode;
}
