package org.dromara.daxpay.core.param.trade.pay;

import org.dromara.daxpay.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 支付关闭参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付关闭参数")
public class PayCloseParam extends PaymentCommonParam {

    /** 订单号 */
    @Schema(description = "订单号")
    @Size(max = 32, message = "支付订单号不可超过32位")
    private String orderNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "商户支付订单号不可超过100位")
    private String bizOrderNo;

    /**
     * 是否使用撤销方式进行订单关闭, 只有部分支付通道的支付方式才可以使用,
     * 如果支付订单不支持撤销, 这个参数将不会生效
     */
    @Schema(description = "是否使用撤销方式进行订单关闭")
    private boolean useCancel;
}
