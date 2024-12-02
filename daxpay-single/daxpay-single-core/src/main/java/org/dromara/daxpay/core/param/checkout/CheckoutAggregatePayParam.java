package org.dromara.daxpay.core.param.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CheckoutAggregateEnum;

/**
 * 收银台聚合支付参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台聚合支付参数")
public class CheckoutAggregatePayParam {

    @Schema(description = "要支付的订单号")
    private String orderNo;

    /**
     * 聚合支付类型
     * @see CheckoutAggregateEnum
     */
    @Schema(description = "聚合支付类型")
    private String aggregateType;

    @Schema(description = "唯一标识")
    private String openId;

}
