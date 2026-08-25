package cn.daxpay.open.platform.iam.param.passkey;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥注册确认参数
///
/// credentialJson 为前端 WebAuthn API 注册响应的标准 JSON 序列化(base64url 编码),
/// 后端直接交由 Yubico 库解析验证; transports 由前端认证器能力回传, 用于展示。
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥注册确认参数")
public class PasskeyRegisterParam {

    @Schema(description = "会话ID")
    @NotBlank(message = "{validation.field.challengeId.notBlank}")
    private String challengeId;

    @Schema(description = "设备可辨识名")
    @NotBlank(message = "{validation.field.deviceName.notBlank}")
    private String deviceName;

    @Schema(description = "认证器注册响应JSON")
    @NotBlank(message = "{validation.field.credential.notBlank}")
    private String credentialJson;

    @Schema(description = "凭据传输方式列表(internal/hybrid/usb/nfc/ble)")
    private List<String> transports;
}
