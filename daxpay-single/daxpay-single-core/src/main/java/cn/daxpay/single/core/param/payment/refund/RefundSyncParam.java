package cn.daxpay.single.core.param.payment.refund;

import cn.daxpay.single.core.param.PaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Size;

/**
 * 退款状态同步参数
 * @author xxm
 * @since 2023/12/17
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "退款状态同步参数")
public class RefundSyncParam extends PaymentCommonParam {

    /** 退款号 */
    @Schema(description = "退款号")
    @Size(max = 100, message = "退款号不可超过100位")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    @Size(max = 100, message = "商户退款号不可超过100位")
    private String bizRefundNo;

}
