package org.dromara.daxpay.core.param.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银台支付参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台支付参数")
public class CheckoutPayParam {

    @Schema(description = "要支付的订单号")
    private String orderNo;

    @Schema(description = "支付配置项ID")
    private Long itemId;

    @Schema(description = "唯一标识")
    private String openId;

    @Schema(description = "付款码")
    private String barCode;
}
