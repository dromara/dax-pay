package org.dromara.daxpay.core.result.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银台聚合支付配置和订单信息
 * @author xxm
 * @since 2024/11/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台聚合支付配置")
public class CheckoutAggregateOrderAndConfigResult {

    /** 订单信息 */
    @Schema(description = "订单信息")
    private CheckoutOrderResult order;

    /** 收银台配置信息 */
    @Schema(description = "收银台配置信息")
    private CheckoutConfigResult config;

    /** 收银台聚合配置信息 */
    @Schema(description = "收银台聚合配置信息")
    private CheckoutAggregateConfigResult aggregateConfig;

}
