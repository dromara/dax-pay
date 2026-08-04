package cn.daxpay.open.channel.stripe.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # Stripe 密钥配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "Stripe 密钥配置结果")
public class StripeKeyConfigResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @SensitiveInfo(front = 6, end = 6)
    @Schema(description = "Stripe Secret Key(已脱敏)")
    private String secretKey;

    @SensitiveInfo(front = 6, end = 6)
    @Schema(description = "Stripe Publishable Key(已脱敏)")
    private String publishableKey;

    @SensitiveInfo(front = 6, end = 6)
    @Schema(description = "Webhook 签名密钥(已脱敏)")
    private String webhookSecret;

    @Schema(description = "Secret Key 是否已配置")
    private boolean secretKeyConfigured;

    @Schema(description = "Publishable Key 是否已配置")
    private boolean publishableKeyConfigured;

    @Schema(description = "Webhook 签名密钥是否已配置")
    private boolean webhookSecretConfigured;
}
