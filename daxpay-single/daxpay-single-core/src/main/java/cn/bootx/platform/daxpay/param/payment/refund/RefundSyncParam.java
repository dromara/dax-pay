package cn.bootx.platform.daxpay.param.payment.refund;

import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 退款状态同步参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "退款状态同步参数")
public class RefundSyncParam extends PaymentCommonParam {

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "商户退款号")
    private String outRefundNo;

}
