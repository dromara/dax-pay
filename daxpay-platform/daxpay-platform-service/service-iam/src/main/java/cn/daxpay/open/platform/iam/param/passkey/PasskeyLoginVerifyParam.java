package cn.daxpay.open.platform.iam.param.passkey;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥登录验证参数
///
/// credentialJson 为前端 WebAuthn API 认证响应的标准 JSON 序列化(base64url 编码),
/// 后端直接交由 Yubico 库解析验证。
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥登录验证参数")
public class PasskeyLoginVerifyParam {

    @Schema(description = "身份域编码(admin/merchant)")
    @NotBlank(message = "{validation.field.client.notBlank}")
    private String client;

    @Schema(description = "会话ID")
    @NotBlank(message = "{validation.field.challengeId.notBlank}")
    private String challengeId;

    @Schema(description = "认证器认证响应JSON")
    @NotBlank(message = "{validation.field.credential.notBlank}")
    private String credentialJson;
}
