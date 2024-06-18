package cn.daxpay.single.core.param.payment.pay;

import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付订单撤销
 * 并不是所有的订单都可以被撤销，只有部分类型的支持撤销，主要是当面付
 * @author xxm
 * @since 2024/4/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付订单撤销")
public class PayCancelParam extends PaymentCommonParam {

    /** 订单号 */
    @Schema(description = "订单号")
    private String orderNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String bizOrderNo;
}
