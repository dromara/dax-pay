package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 社交登录授权码兑换结果
///
/// 前端回调页收到第三方平台的 code+state 后, 调用 exchange API,
/// 后端完成 code 换 token 并返回此结果, 前端据此决定跳转逻辑.
///
@Data
@Accessors(chain = true)
@Schema(title = "社交登录兑换结果")
public class SocialExchangeResult {

    /// 登录 token(LOGIN 场景且成功时返回)
    @Schema(description = "登录token")
    private String token;

    /// 操作结果(BIND 场景成功时为 bind_success)
    @Schema(description = "操作结果")
    private String result;

    /// 错误码(unbind=未绑定, state_invalid=state过期, oauth_failed=授权失败)
    @Schema(description = "错误码")
    private String error;
}
