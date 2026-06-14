package org.dromara.daxpay.payment.merchant.param.route.strategy;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 支付通道路由策略参数
///
@Data
@Accessors(chain = true)
@Schema(title = "支付通道路由策略参数")
public class PayRouteStrategyParam {

    @Schema(description = "主键,更新时传入")
    private Long id;

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 32)
    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "路由模式：basic/scene")
    private String mode;

    @Schema(description = "支付渠道")
    private String provider;

    @Schema(description = "是否启用")
    private Boolean enable;

    @Schema(description = "策略名称")
    private String name;
}
