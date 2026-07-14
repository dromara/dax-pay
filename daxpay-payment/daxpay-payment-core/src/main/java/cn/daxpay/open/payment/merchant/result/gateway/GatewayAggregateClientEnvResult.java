package cn.daxpay.open.payment.merchant.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 网关聚合扫码客户端环境配置结果(子表)
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合扫码客户端环境配置结果")
public class GatewayAggregateClientEnvResult {

    /// @see cn.daxpay.open.payment.merchant.enums.ClientEnvEnum
    @Schema(description = "客户端环境编码: wechat_pay/alipay/union_pay/douyin")
    private String clientEnv;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum
    @Schema(description = "支付方式(METHOD 模式)")
    private String method;

    @Schema(description = "通道商户号(DIRECT 模式)")
    private String channelMchNo;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @Schema(description = "支付能力(DIRECT 模式)")
    private String capability;
}
