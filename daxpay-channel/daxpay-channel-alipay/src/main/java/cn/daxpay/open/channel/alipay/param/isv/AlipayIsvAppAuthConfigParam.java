package cn.daxpay.open.channel.alipay.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝服务商应用授权认证配置保存参数
///
/// 保存/更新支付宝服务商应用授权认证配置时接收的请求参数，含应用ID、用户标识类型和授权回调地址。
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商应用授权认证配置保存参数")
public class AlipayIsvAppAuthConfigParam {

    @NotNull(message = "{validation.field.alipayIsvAppId.notNull}")
    @Schema(description = "支付宝服务商应用ID")
    private Long alipayIsvAppId;

    @NotBlank(message = "{validation.field.userIdType.notBlank}")
    @Schema(description = "用户标识类型")
    private String userIdType;

    @NotBlank(message = "{validation.field.authCallbackUrl.notBlank}")
    @Size(max = 512, message = "{validation.field.authCallbackUrl.size}")
    @Schema(description = "授权回调地址")
    private String authCallbackUrl;
}
