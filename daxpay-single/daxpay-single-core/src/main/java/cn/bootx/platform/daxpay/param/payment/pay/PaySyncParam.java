package cn.bootx.platform.daxpay.param.payment.pay;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付状态同步参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付状态同步参数")
public class PaySyncParam extends PaymentCommonParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "商户业务号")
    private String bizOrderNo;

}
