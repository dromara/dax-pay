package org.dromara.daxpay.payment.merchant.result.route.strategy;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付通道路由策略结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付通道路由策略")
public class PayRouteStrategyResult extends BaseResult {

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "路由模式")
    private String mode;

    @Schema(description = "支付渠道")
    private String provider;

    @Schema(description = "是否启用")
    private boolean enable;

    @Schema(description = "策略名称")
    private String name;
}
