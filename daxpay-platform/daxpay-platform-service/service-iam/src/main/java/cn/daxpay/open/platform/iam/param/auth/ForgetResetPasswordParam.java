package cn.daxpay.open.platform.iam.param.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 找回密码重置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "找回密码重置参数")
public class ForgetResetPasswordParam {

    @Schema(description = "找回流程ID(发送验证码时返回)")
    @NotBlank(message = "{validation.field.flowId.notBlank}")
    private String flowId;

    @Schema(description = "邮箱验证码")
    @NotBlank(message = "{validation.field.emailCode.notBlank}")
    private String code;

    @Schema(description = "新密码(RSA加密传输)")
    @NotBlank(message = "{validation.field.newPassword.notBlank}")
    private String password;
}
