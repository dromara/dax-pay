package cn.daxpay.open.platform.iam.param.passkey;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥注册选项参数
///
/// 注册新凭据属敏感操作, 须先以登录密码确认身份, 防止借用会话植入凭据。
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥注册选项参数")
public class PasskeyRegisterOptionsParam {

    @Schema(description = "登录密码(RSA加密传输)")
    @NotBlank(message = "{validation.field.password.notBlank}")
    private String password;
}
