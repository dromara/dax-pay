package cn.daxpay.open.channel.stripe.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # Stripe 密钥配置保存参数
///
/// 保存/更新 Stripe 密钥时接收的请求参数，含通道商户号和密钥信息。
///
@Data
@Accessors(chain = true)
@Schema(title = "Stripe 密钥配置保存参数")
public class StripeKeyConfigParam {

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "Stripe Secret Key(sk_test_xxx 沙箱 / sk_live_xxx 生产)")
    private String secretKey;

    @Schema(description = "Stripe Publishable Key(pk_test_xxx 沙箱 / pk_live_xxx 生产)")
    private String publishableKey;

    @Schema(description = "Webhook 签名密钥(whsec_xxx)")
    private String webhookSecret;
}
