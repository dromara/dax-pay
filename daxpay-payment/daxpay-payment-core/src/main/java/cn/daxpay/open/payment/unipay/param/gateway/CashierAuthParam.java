package cn.daxpay.open.payment.unipay.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 收银台小程序认证参数
///
/// 收银台小程序(微信/支付宝/抖音)前端通过 uni.login / my.getAuthCode / tt.login
/// 获取授权码后, 调用此参数换 openId/userId。无商户签名, 走 [IgnoreAuth] 端点。
@Data
@Schema(title = "收银台认证参数")
public class CashierAuthParam {

    /// 支付通道: wechat / alipay / douyin
    @Schema(description = "支付通道")
    @NotBlank(message = "{validation.field.channel.notBlank}")
    @Size(max = 32, message = "{validation.field.channel.size}")
    private String channel;

    /// 授权码(uni.login / my.getAuthCode / tt.login 返回)
    @Schema(description = "授权码")
    @NotBlank(message = "{validation.field.oauthCode.notBlank}")
    @Size(max = 128, message = "{validation.field.oauthCode.size}")
    private String authCode;
}
