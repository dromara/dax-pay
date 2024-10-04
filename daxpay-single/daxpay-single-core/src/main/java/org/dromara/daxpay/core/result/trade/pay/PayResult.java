package org.dromara.daxpay.core.result.trade.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一支付响应参数
 * @author xxm
 * @since 2024/6/27
 */
@Data
@Accessors(chain = true)
@Schema(title = "统一支付响应参数")
public class PayResult {

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 支付状态 */
    @Schema(description = "支付状态")
    private String status;

    /** 支付参数体(通常用于发起支付的参数) */
    @Schema(description = "支付参数体")
    private String payBody;
}
