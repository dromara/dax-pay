package cn.daxpay.open.payment.douyin.param.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道商户抖音应用能力绑定单项
///
@Data
@Accessors(chain = true)
@Schema(title = "通道商户抖音应用能力绑定单项")
public class DyChannelAppCapabilityParam {

    // mchNo/channelMchNo 由外层 DyChannelAppCapabilityBatchParam 统一携带并校验，单项不重复声明（对齐微信 WxChannelAppCapabilityParam 范式）
    @NotBlank(message = "{validation.field.capability.notBlank}")
    @Schema(description = "支付能力编码")
    private String capability;

    @NotBlank(message = "{validation.field.appScope.notBlank}")
    @Schema(description = "应用档位：platform/merchant")
    private String appScope;

    @NotNull(message = "{validation.field.dyAppRefId.notNull}")
    @Schema(description = "抖音应用主数据主键")
    private Long dyAppRefId;
}
