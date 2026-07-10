package cn.daxpay.open.channel.alipay.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用授权认证配置保存参数
///
/// 保存/更新支付宝直连商户应用授权认证配置时接收的请求参数，含商户号、通道商户号、用户标识类型。
///
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用授权认证配置保存参数")
public class AlipayDirectAppAuthConfigParam {

    @NotNull(message = "{validation.field.alipayDirectAppId.notNull}")
    @Schema(description = "关联应用ID")
    private Long alipayDirectAppId;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.userIdType.notBlank}")
    @Schema(description = "用户标识类型")
    private String userIdType;
}
