package org.dromara.daxpay.core.param.checkout;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 收银台聚合条码支付参数
 * @author xxm
 * @since 2024/11/26
 */
@Data
@Accessors(chain = true)
@Schema(title = "收银台聚合支付参数")
public class CheckoutAggregateBarPayParam {
    /** 支付订单号 */
    @NotBlank(message = "支付订单号不可为空")
    @Size(max = 32, message = "支付订单号不可超过32位")
    @Schema(description = "支付订单号")
    private String orderNo;


    /** 付款码 */
    @NotBlank(message = "付款码不可为空")
    @Size(max = 256, message = "付款码不可超过256位")
    @Schema(description = "付款码")
    private String barCode;

}
