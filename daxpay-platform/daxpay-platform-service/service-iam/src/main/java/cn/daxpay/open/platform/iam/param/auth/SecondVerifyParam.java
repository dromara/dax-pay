package cn.daxpay.open.platform.iam.param.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证二次验证参数
///
/// 登录密码通过后, 凭预认证令牌 + 动态码(或备用码)完成二次验证。
///
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证二次验证参数")
public class SecondVerifyParam {

    @Schema(description = "预认证令牌(密码通过后返回)")
    @NotBlank(message = "{validation.field.preAuthToken.notBlank}")
    private String preAuthToken;

    @Schema(description = "动态码或备用验证码")
    @NotBlank(message = "{validation.field.twoFactorCode.notBlank}")
    private String code;

    @Schema(description = "验证码类型: TOTP(动态码, 默认) | BACKUP(备用码)")
    private String codeType;
}
