package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 邮箱解绑参数
///
@Data
@Accessors(chain = true)
@Schema(title = "邮箱解绑参数")
public class EmailUnbindParam {

    @Schema(description = "登录密码(RSA加密传输)")
    @NotBlank(message = "{validation.field.password.notBlank}")
    private String password;

    @Schema(description = "邮箱验证码(发至当前绑定邮箱)")
    @NotBlank(message = "{validation.field.emailCode.notBlank}")
    private String code;
}
