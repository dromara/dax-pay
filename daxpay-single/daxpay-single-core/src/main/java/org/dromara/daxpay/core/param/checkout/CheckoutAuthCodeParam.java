package org.dromara.daxpay.core.param.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CheckoutAggregateEnum;

/**
 * 获取收银台认证结果参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "获取收银台认证结果参数")
public class CheckoutAuthCodeParam {

    /** 要支付的订单号 */
    @Schema(description = "要支付的订单号")
    private String orderNo;

    /**
     * 聚合支付类型
     * @see CheckoutAggregateEnum
     */
    @Schema(description = "聚合支付类型")
    private String aggregateType;

    /** 认证Code */
    @Schema(description = "认证Code")
    private String authCode;
}
