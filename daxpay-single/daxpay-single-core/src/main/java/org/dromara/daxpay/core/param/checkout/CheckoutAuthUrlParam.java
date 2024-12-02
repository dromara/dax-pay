package org.dromara.daxpay.core.param.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.daxpay.core.enums.CheckoutAggregateEnum;

/**
 * 获取通道收银认证url参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道收银认证参数")
public class CheckoutAuthUrlParam {

    /**
     * 聚合支付类型
     * @see CheckoutAggregateEnum
     */
    @Schema(description = "聚合支付类型")
    private String aggregateType;

    @Schema(description = "收银台类型")
    private String cashierType;
}
