package cn.daxpay.open.payment.merchant.param.route.strategy;

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

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Size(max = 32)
    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "路由模式：basic/scene")
    private String mode;
}
