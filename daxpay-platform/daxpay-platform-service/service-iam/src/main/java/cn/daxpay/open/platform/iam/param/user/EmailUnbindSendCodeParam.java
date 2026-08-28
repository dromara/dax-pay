package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 邮箱解绑验证码发送参数
///
@Data
@Accessors(chain = true)
@Schema(title = "邮箱解绑验证码发送参数")
public class EmailUnbindSendCodeParam {

    @Schema(description = "登录密码(RSA加密传输)")
    @NotBlank(message = "{validation.field.password.notBlank}")
    private String password;
}
