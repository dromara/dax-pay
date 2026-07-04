package cn.daxpay.open.channel.alipay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 应用授权令牌更新参数
///
/// 用于手动设置或更新支付宝服务商通道商户的应用授权令牌(app_auth_token)。
/// 适用于创建时未填写令牌后续补充, 或令牌过期/变更后重新绑定的场景。
///
@Data
@Accessors(chain = true)
@Schema(title = "应用授权令牌更新参数")
public class AlipayIsvAppAuthTokenUpdateParam {

    /// 通道商户号
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    private String channelMchNo;

    /// 应用授权令牌(服务商代子商户调用接口的凭据)
    @Schema(description = "应用授权令牌")
    @NotBlank(message = "{validation.field.appAuthToken.notBlank}")
    private String appAuthToken;
}
