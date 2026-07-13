package cn.daxpay.open.platform.iam.result.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 二次验证结果
///
/// 密码通过但还需双因素时返回, 携带临时凭证 preAuthToken。
/// 前端切到二次验证页, 凭凭证 + 动态码完成登录。
///
@Data
@Accessors(chain = true)
@Schema(title = "二次验证结果")
public class TwoFactorRequiredResult {

    @Schema(description = "临时凭证(二次验证接口使用, 5分钟内有效)")
    private String preAuthToken;

    public TwoFactorRequiredResult() {
    }

    public TwoFactorRequiredResult(String preAuthToken) {
        this.preAuthToken = preAuthToken;
    }
}
