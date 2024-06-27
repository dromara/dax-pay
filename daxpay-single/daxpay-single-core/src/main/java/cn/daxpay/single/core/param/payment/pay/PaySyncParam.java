package cn.daxpay.single.core.param.payment.pay;

import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

/**
 * 支付状态同步参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付状态同步参数")
public class PaySyncParam extends PaymentCommonParam {

    /** 订单号 */
    @Schema(description = "订单号")
    @Size(max = 32, message = "支付订单号不可超过32位")
    private String orderNo;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "商户支付订单号不可超过100位")
    private String bizOrderNo;

}
