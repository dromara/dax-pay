package cn.bootx.platform.daxpay.param.pay;

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
public class RefundSyncParam extends PayCommonParam {

    /**
     * 部分退款时 refundId 和 refundNo  必传一个
     * 如果与 businessNo同时传输，以本参数为准
     */
    @Schema(description = "退款订单ID")
    private Long refundId;

    /**
     * 退款订单号，部分退款时 refundId 和 refundNo  必传一个，
     * 如果与 paymentId 和 businessNo 同时传输，以本参数为准
     */
    @Schema(description = "退款订单号")
    private String refundNo;

    /**
     * 支付单ID， 通常为商户的业务订单号，全部退款时，
     * paymentId 和 businessNo可传其中一个，同时传输以前者为准，部分退款可以不进行传输
     */
    @Schema(description = "支付单ID")
    private Long paymentId;

    /**
     * 支付订单的业务号， 通常为商户的业务订单号，全部退款时，
     * paymentId 和 businessNo可传其中一个，同时传输以前者为准，部分退款可以不进行传输
     */
    @Schema(description = "业务号")
    private String businessNo;

}
