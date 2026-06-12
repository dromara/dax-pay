package org.dromara.daxpay.payment.merchant.result.route.resolve;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 通道路由解析结果
///
@Data
@Accessors(chain = true)
@Schema(title = "通道路由解析结果")
public class PayRouteResolveResult {

    @Schema(description = "通道编码")
    private String channel;

    @Schema(description = "支付方式编码")
    private String method;

    @Schema(description = "产品编码")
    private String product;

    @Schema(description = "命中规则ID（精细模式预留，当前恒为空）")
    private String hitRuleId;

    @Schema(description = "命中简化配置ID")
    private String hitConfigId;

    @Schema(description = "路由模式")
    private String mode;
}
