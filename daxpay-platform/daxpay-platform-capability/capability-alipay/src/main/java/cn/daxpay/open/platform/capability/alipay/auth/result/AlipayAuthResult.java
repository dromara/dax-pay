package cn.daxpay.open.platform.capability.alipay.auth.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝授权结果
///
/// auth_code 换取后得到的用户标识。支付宝同时存在 user_id(传统) 与 open_id(新标准),
/// 认证场景统一以 user_id 作为唯一标识(与 iam_user_social.open_id 列对应)。
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝授权结果")
public class AlipayAuthResult {

    /// 支付宝用户唯一标识(传统 user_id)
    @Schema(description = "支付宝用户ID")
    private String userId;

    /// 支付宝新标准 open_id(部分应用返回)
    @Schema(description = "支付宝 open_id")
    private String openId;

    /// 访问令牌(可用于后续调用 alipay.user.info.share 获取用户信息)
    @Schema(description = "访问令牌")
    private String accessToken;
}
