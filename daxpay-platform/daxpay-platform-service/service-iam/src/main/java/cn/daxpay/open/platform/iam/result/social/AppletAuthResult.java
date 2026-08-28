package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 小程序快捷登录授权结果
///
/// 前端直传平台登录 code 后, 由 [cn.daxpay.open.platform.iam.service.social.SocialAppletAuthService]
/// 分发到 capability 能力层换取的归一化用户标识.
///
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷登录授权结果")
public class AppletAuthResult {

    /// 平台用户唯一标识(微信/抖音 openId, 支付宝优先 userId)
    @Schema(description = "平台用户唯一标识")
    private String openId;
}
