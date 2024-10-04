package org.dromara.daxpay.core.param.trade.pay;

import org.dromara.daxpay.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 支付单查询参数
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付单查询参数")
public class QueryPayParam extends PaymentCommonParam {

    @Schema(description = "订单号")
    @Size(max = 32, message = "支付订单号不可超过32位")
    private String orderNo;

    @Schema(description = "商户订单号")
    @Size(max = 100, message = "商户支付订单号不可超过100位")
    private String bizOrderNoeNo;
}
