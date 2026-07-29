package cn.daxpay.open.payment.unipay.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 收银台小程序认证参数
///
/// 收银台小程序(微信/支付宝/抖音)前端通过 uni.login / my.getAuthCode / tt.login
/// 获取授权码后, 调用此参数换 openId/userId。无商户签名, 走 [IgnoreAuth] 端点。
///
/// 抖音: `authCode`(login code) 与 `anonymousCode` 对应官方分字段 `code` / `anonymous_code`,
/// 至少传一个; 非匿名场景可同时传两者。微信/支付宝仅使用 `authCode`。
@Data
@Schema(title = "收银台认证参数")
public class CashierAuthParam {

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
