package cn.daxpay.open.platform.iam.result.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证挑战结果
///
/// 密码校验通过但用户已启用双因素认证时返回, 携带一次性预认证令牌(preAuthToken),
/// 前端据此切换到二次验证界面, 凭 preAuthToken + 动态码完成登录。
///
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证挑战")
public class TwoFactorChallengeResult {

    @Schema(description = "一次性预认证令牌(用于二次验证接口, 5分钟内有效)")
    private String preAuthToken;

    public TwoFactorChallengeResult() {
    }

    public TwoFactorChallengeResult(String preAuthToken) {
        this.preAuthToken = preAuthToken;
    }
}
