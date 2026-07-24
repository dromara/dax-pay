package cn.daxpay.open.platform.iam.result.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 登录页上下⽂信息
///
@Data
@Accessors(chain = true)
@Schema(title =  "登录页上下⽂信息")
public class LoginContentResult {

    /// 支持登录方式
    @Schema(description = "支持登录方式")
    private List<String> loginTypes;

    @Schema(description = "是否启用验证码")
    private boolean enableCaptcha;

    @Schema(description = "密码是否加密传输")
    private boolean passwordEncrypted;

    /// 本端应用内社交自动登录策略(已按 client 裁剪且交叉校验社交平台启用态)
    @Schema(description = "应用内社交自动登录")
    private AutoSocialLogin autoSocialLogin;

    /// 单端自动登录下发片段
    @Data
    @Accessors(chain = true)
    @Schema(title = "应用内社交自动登录")
    public static class AutoSocialLogin {

        @Schema(description = "是否启用")
        private boolean enabled;

        @Schema(description = "可自动跳转的社交平台编码列表(前端按 UA 匹配其一)")
        private List<String> sources;
    }
}
