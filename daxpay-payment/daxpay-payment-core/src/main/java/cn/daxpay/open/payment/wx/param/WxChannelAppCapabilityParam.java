package cn.daxpay.open.payment.wx.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道商户微信应用能力绑定单项
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户微信应用能力绑定单项")
public class WxChannelAppCapabilityParam {

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.capability.notBlank}")
    @Schema(description = "支付能力编码")
    private String capability;

    @NotBlank(message = "{validation.field.appScope.notBlank}")
    @Schema(description = "应用档位：platform/merchant")
    private String appScope;

    @NotNull(message = "{validation.field.wxAppRefId.notNull}")
    @Schema(description = "微信应用主数据主键")
    private Long wxAppRefId;
}
