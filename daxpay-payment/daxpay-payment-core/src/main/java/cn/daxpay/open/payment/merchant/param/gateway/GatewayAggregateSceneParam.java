package cn.daxpay.open.payment.merchant.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/// # 网关聚合扫码场景配置参数(子表)
@Data
@Schema(title = "网关聚合扫码场景配置参数")
public class GatewayAggregateSceneParam {

    /// @see cn.daxpay.open.payment.merchant.enums.CashierSceneEnum
    @Schema(description = "场景编码: wechat_pay/alipay/union_pay/douyin")
    @Size(max = 32, message = "{validation.field.scene.size}")
    private String scene;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum
    @Schema(description = "支付方式(METHOD 模式填)")
    @Size(max = 32, message = "{validation.field.method.size}")
    private String method;

    @Schema(description = "通道商户号(DIRECT 模式填)")
    @Size(max = 64, message = "{validation.field.channelMchNo.size}")
    private String channelMchNo;

    /// @see cn.daxpay.open.platform.core.enums.pay.channel.PayCapabilityEnum
    @Schema(description = "支付能力(DIRECT 模式填)")
    @Size(max = 64, message = "{validation.field.capability.size}")
    private String capability;
}
