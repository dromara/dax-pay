package cn.daxpay.open.platform.iam.param.passkey;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥删除参数
///
/// 删除凭据属敏感操作, 须以登录密码确认身份, 防止借用会话移除用户凭据。
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥删除参数")
public class PasskeyDeleteParam {

    @Schema(description = "凭据记录ID")
    @NotNull(message = "{validation.field.id.notNull}")
    private Long id;

    @Schema(description = "登录密码(RSA加密传输)")
    @NotBlank(message = "{validation.field.password.notBlank}")
    private String password;
}
