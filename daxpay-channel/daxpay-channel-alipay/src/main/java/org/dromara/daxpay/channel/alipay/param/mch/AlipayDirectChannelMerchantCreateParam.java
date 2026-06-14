package org.dromara.daxpay.channel.alipay.param.mch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连通道商户创建参数")
public class AlipayDirectChannelMerchantCreateParam {

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

    /// 支付宝商家用户ID(2088开头)
    @Schema(description = "支付宝商家用户ID(2088开头)")
    @NotBlank(message = "{validation.field.alipayUserId.notBlank}")
    private String alipayUserId;
}
