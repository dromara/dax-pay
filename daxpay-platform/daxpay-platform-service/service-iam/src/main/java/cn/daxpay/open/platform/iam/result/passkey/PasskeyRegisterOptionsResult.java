package cn.daxpay.open.platform.iam.result.passkey;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通行密钥注册选项结果
///
/// options 结构与前端 @simplewebauthn/browser 的 PasskeyCreationOptions 对齐,
/// 前端直接透传给 startRegistration 发起系统注册弹窗。
///
@Data
@Accessors(chain = true)
@Schema(title = "通行密钥注册选项结果")
public class PasskeyRegisterOptionsResult {

    @Schema(description = "会话ID(注册确认时回传)")
    private String challengeId;

    @Schema(description = "注册选项(simplewebauthn 格式)")
    private CreationOptions options;

    /// 注册选项(对应 WebAuthn PublicKeyCredentialCreationOptionsJSON)
    @Data
    @Accessors(chain = true)
    @Schema(title = "通行密钥注册选项")
    public static class CreationOptions {

        @Schema(description = "站点信息")
        private Rp rp;

        @Schema(description = "用户信息")
        private User user;

        @Schema(description = "challenge 随机值(base64url)")
        private String challenge;

        @Schema(description = "公钥算法参数列表")
        private List<PubKeyCredParam> pubKeyCredParams;

        @Schema(description = "超时时间(毫秒)")
        private Long timeout;

        @Schema(description = "排除的已有凭据(防同一认证器重复注册)")
        private List<CredentialDescriptor> excludeCredentials;

        @Schema(description = "认证器选择条件")
        private AuthenticatorSelection authenticatorSelection;

        @Schema(description = "证明收集偏好(none 不收集, W3C 标准字段名)")
        private String attestation;
    }

    /// 站点信息(对应 WebAuthn 协议 rp 字段)
    @Data
    @Accessors(chain = true)
    @Schema(title = "站点信息")
    public static class Rp {

        @Schema(description = "站点域名")
        private String id;

        @Schema(description = "站点显示名称")
        private String name;
    }

    /// 用户信息
    @Data
    @Accessors(chain = true)
    @Schema(title = "用户信息")
    public static class User {

        @Schema(description = "用户句柄(userId, base64url)")
        private String id;

        @Schema(description = "账号")
        private String name;

        @Schema(description = "显示名称")
        private String displayName;
    }

    /// 公钥算法参数
    @Data
    @Accessors(chain = true)
    @Schema(title = "公钥算法参数")
    public static class PubKeyCredParam {

        @Schema(description = "凭证类型(固定 public-key)")
        private String type;

        @Schema(description = "COSE 算法标识(ES256=-7, RS256=-257)")
        private Integer alg;
    }

    /// 凭据描述
    @Data
    @Accessors(chain = true)
    @Schema(title = "凭据描述")
    public static class CredentialDescriptor {

        @Schema(description = "凭据ID(base64url)")
        private String id;

        @Schema(description = "凭证类型(固定 public-key)")
        private String type;
    }

    /// 认证器选择条件
    @Data
    @Accessors(chain = true)
    @Schema(title = "认证器选择条件")
    public static class AuthenticatorSelection {

        @Schema(description = "客户端可发现凭据要求(required=passkey 免输账号)")
        private String residentKey;

        @Schema(description = "用户验证要求(required=生物识别/PIN)")
        private String userVerification;
    }
}
