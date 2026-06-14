package org.dromara.daxpay.channel.alipay.result.config;

import org.dromara.daxpay.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝通道商户配置结果")
public class AlipayChannelMerchantResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "所属支付产品")
    private String product;

    @Schema(description = "支付宝服务商应用ID（aliAppId）")
    private String isvAppId;

    @Schema(description = "支付宝商家唯一识别码(2088开头的16位数字)")
    private String alipayUserId;

    @Schema(description = "应用授权令牌")
    private String appAuthToken;
}
