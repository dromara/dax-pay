package org.dromara.daxpay.service.pay.result.gateway;

import org.dromara.daxpay.service.merchant.result.gateway.AggregatePayConfigResult;
import org.dromara.daxpay.service.merchant.result.gateway.GatewayPayConfigResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 网关聚合支付订单和配置信息
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "网关聚合支付订单和配置信息")
public class AggregateOrderAndConfigResult {

    /** 订单信息 */
    @Schema(description = "订单信息")
    private GatewayOrderResult order;

    /** 网关支付配置信息 */
    @Schema(description = "网关支付配置信息")
    private GatewayPayConfigResult config;

    /** 聚合支付配置信息 */
    @Schema(description = "聚合支付配置信息")
    private AggregatePayConfigResult aggregateConfig;

}
