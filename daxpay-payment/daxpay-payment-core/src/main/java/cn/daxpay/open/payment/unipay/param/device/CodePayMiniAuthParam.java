package cn.daxpay.open.payment.unipay.param.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 码牌小程序认证参数
///
/// 码牌小程序(微信/支付宝/抖音)前端通过 uni.login / my.getAuthCode / tt.login
/// 获取授权码后, 调用此参数同步换 openId/userId。无商户签名, 走 [cn.daxpay.open.platform.core.annotation.IgnoreAuth] 端点。
///
/// 与 H5 码牌授权([CodePayAuthUrlParam] → generate-auth-url) 的区别:
/// - H5: OAuth 跳转 → 异步 session/queryCode 轮询
/// - 小程序: 前端直拿 code → 同步直返 openId, 无需 session 机制
///
/// 抖音: `authCode`(login code) 与 `anonymousCode` 对应官方分字段 `code` / `anonymous_code`,
/// 至少传一个; 非匿名场景可同时传两者。微信/支付宝仅使用 `authCode`。
@Data
@Schema(title = "码牌小程序认证参数")
public class CodePayMiniAuthParam {

    /// 码牌编码(用于校验码牌启用且已分配商户)
    @Schema(description = "码牌编码")
    @NotBlank(message = "{validation.field.code.notBlank}")
    @Size(max = 64, message = "{validation.field.code.size}")
    private String code;

    /// 支付通道: wechat / alipay / douyin
    @Schema(description = "支付通道")
    @NotBlank(message = "{validation.field.channel.notBlank}")
    @Size(max = 32, message = "{validation.field.channel.size}")
    private String channel;

    /// 授权码(微信 uni.login / 支付宝 my.getAuthCode / 抖音 tt.login 的 code)
    /// 抖音匿名登录时可空, 此时须传 [anonymousCode]
    @Schema(description = "授权码")
    @Size(max = 128, message = "{validation.field.oauthCode.size}")
    private String authCode;

    /// 抖音匿名登录凭证(tt.login 的 anonymous_code), 仅抖音使用; 与 authCode 至少传一个
    @Schema(description = "抖音匿名授权码")
    @Size(max = 128, message = "{validation.field.oauthCode.size}")
    private String anonymousCode;
}
