package cn.daxpay.open.payment.merchant.result.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 网关聚合扫码场景配置结果(子表)
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合扫码场景配置结果")
public class GatewayAggregateSceneResult {

    /// @see cn.daxpay.open.payment.common.enums.CashierSceneEnum
    @Schema(description = "场景编码: wechat_pay/alipay/union_pay/douyin")
    private String scene;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum
    @Schema(description = "支付方式(METHOD 模式)")
    private String method;

    @Schema(description = "通道商户号(DIRECT 模式)")
    private String channelMchNo;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @Schema(description = "支付能力(DIRECT 模式)")
    private String capability;
}
