package org.dromara.daxpay.channel.wechat.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户创建参数
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户创建参数")
public class WechatIsvChannelMerchantCreateParam {

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

    /// 微信特约商户号/二级商户号
    @Schema(description = "微信特约商户号/二级商户号")
    @NotBlank(message = "{validation.field.subMchId.notBlank}")
    private String subMchId;
}
