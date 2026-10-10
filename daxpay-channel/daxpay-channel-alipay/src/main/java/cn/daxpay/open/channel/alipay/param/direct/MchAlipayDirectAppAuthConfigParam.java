package cn.daxpay.open.channel.alipay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用授权认证配置保存参数(商户端)
///
/// 商户端专用参数, 不含商户号(防越权: 商户号与通道商户号由控制器按服务端应用记录组装,
/// 对照运营端参数 [AlipayDirectAppAuthConfigParam])。
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用授权认证配置保存参数(商户端)")
public class MchAlipayDirectAppAuthConfigParam {

    @NotNull(message = "{validation.field.alipayDirectAppId.notNull}")
    @Schema(description = "关联应用ID")
    private Long alipayDirectAppId;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.userIdType.notBlank}")
    @Schema(description = "用户标识类型")
    private String userIdType;
}
