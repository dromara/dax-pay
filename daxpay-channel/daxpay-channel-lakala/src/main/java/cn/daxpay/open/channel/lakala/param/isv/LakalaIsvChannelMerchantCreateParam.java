package cn.daxpay.open.channel.lakala.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 拉卡拉通道商户创建参数
///
@Data
@Accessors(chain = true)
@Schema(title = "拉卡拉通道商户创建参数")
public class LakalaIsvChannelMerchantCreateParam {

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

    /// 拉卡拉商户编号
    @Schema(description = "拉卡拉商户编号")
    @NotBlank(message = "{validation.field.lakalaMchNo.notBlank}")
    private String lakalaMchNo;
}
