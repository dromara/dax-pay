package cn.daxpay.open.platform.iam.param.social;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 扫码授权码兑换参数(绑定/登录共用)
///
/// 调用方是 H5 回调页: H5 的 http 封装会把 POST 的 params 并入 JSON body(不挂在 query 上),
/// 因此本组接口以 body 接参, 与 Web 端调用、走 query 参数绑定的 exchange-login/exchange-bind 不同.
@Data
@Accessors(chain = true)
@Schema(title = "扫码授权码兑换参数")
public class SocialScanExchangeParam {

    @Schema(description = "微信授权码")
    @NotBlank(message = "{validation.field.oauthCode.notBlank}")
    private String code;

    @Schema(description = "授权state(一次性映射, 换取绑定/登录票据)")
    @NotBlank(message = "{validation.field.oauthState.notBlank}")
    private String state;
}
