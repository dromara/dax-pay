package cn.daxpay.open.channel.stripe.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Stripe 通道商户创建参数
///
@Schema(title = "Stripe 通道商户创建参数")
@Data
@Accessors(chain = true)
public class StripeChannelMerchantCreateParam {

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

    /// Stripe 账户 ID(acct_xxx)
    @Schema(description = "Stripe 账户 ID(acct_xxx)")
    @NotBlank(message = "{validation.field.accountId.notBlank}")
    private String accountId;
}
