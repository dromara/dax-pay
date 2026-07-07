package cn.daxpay.open.channel.hkrt.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 海科融通通道商户创建参数
///
@Data
@Accessors(chain = true)
@Schema(title = "海科融通通道商户创建参数")
public class HkrtIsvChannelMerchantCreateParam {

    /// 商户号
    @Schema(description = "商户号")
    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    private String mchNo;

    /// 通道商户名称
    @Schema(description = "通道商户名称")
    @NotBlank(message = "{validation.field.channelMerchantName.notBlank}")
    private String channelMerchantName;

    /// 所属支付产品
    @Schema(description = "所属支付产品")
    @NotBlank(message = "{validation.field.product.notBlank}")
    private String product;

    /// 海科商户号
    @Schema(description = "海科商户号")
    @NotBlank(message = "{validation.field.merchNo.notBlank}")
    private String merchNo;
}
