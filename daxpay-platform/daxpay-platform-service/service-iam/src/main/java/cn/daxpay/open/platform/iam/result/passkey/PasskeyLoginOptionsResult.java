package cn.daxpay.open.platform.iam.result.passkey;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥登录选项结果
///
/// options 结构与前端 @simplewebauthn/browser 的 PasskeyRequestOptions 对齐,
/// 前端直接透传给 startAuthentication 发起系统凭据选择弹窗(discoverable 免输账号)。
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥登录选项结果")
public class PasskeyLoginOptionsResult {

    @Schema(description = "会话ID(登录验证时回传)")
    private String challengeId;

    @Schema(description = "认证选项(simplewebauthn 格式)")
    private RequestOptions options;

    /// 认证选项(对应 WebAuthn PublicKeyCredentialRequestOptionsJSON)
    @Data
    @Accessors(chain = true)
    @Schema(title = "通行密钥认证选项")
    public static class RequestOptions {

        @Schema(description = "challenge 随机值(base64url)")
        private String challenge;

        @Schema(description = "超时时间(毫秒)")
        private Long timeout;

        @Schema(description = "站点域名")
        private String rpId;

        @Schema(description = "允许的凭据列表(空=discoverable 免输账号)")
        private List<PasskeyRegisterOptionsResult.CredentialDescriptor> allowCredentials;

        @Schema(description = "用户验证要求(required)")
        private String userVerification;
    }
}
