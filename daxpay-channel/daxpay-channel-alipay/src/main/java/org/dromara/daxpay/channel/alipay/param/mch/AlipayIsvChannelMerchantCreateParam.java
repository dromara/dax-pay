package org.dromara.daxpay.channel.alipay.param.mch;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Schema(title = "支付宝服务商通道商户创建参数")
public class AlipayIsvChannelMerchantCreateParam {

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

    /// 服务商应用ID(系统主键, 指向 alipay_isv_app.id)
    @Schema(description = "服务商应用ID")
    @NotNull(message = "{validation.field.appId.notNull}")
    private Long appId;

    /// 子商户支付宝用户ID(2088开头)
    @Schema(description = "子商户支付宝用户ID(2088开头)")
    @NotBlank(message = "{validation.field.alipayUserId.notBlank}")
    private String alipayUserId;

    /// 应用授权令牌，服务商代子商户调用接口的凭据
    @Schema(description = "应用授权令牌")
    @NotBlank(message = "{validation.field.appAuthToken.notBlank}")
    private String appAuthToken;
}
