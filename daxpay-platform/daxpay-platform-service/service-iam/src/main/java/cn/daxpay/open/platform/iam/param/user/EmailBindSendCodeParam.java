package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 邮箱绑定验证码发送参数
///
@Data
@Accessors(chain = true)
@Schema(title = "邮箱绑定验证码发送参数")
public class EmailBindSendCodeParam {

    @Schema(description = "登录密码(RSA加密传输)")
    @NotBlank(message = "{validation.field.password.notBlank}")
    private String password;

    @Schema(description = "新邮箱")
    @NotBlank(message = "{validation.field.email.notBlank}")
    @Email(message = "{validation.field.email.format}")
    private String email;
}
