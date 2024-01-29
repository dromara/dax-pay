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
     * 部分退款时 refundId 和 refundNo  必传一个, 同时传输时，以 refundId 为准
     */
    @Schema(description = "退款订单ID")
    private Long refundId;

    /**
     * 退款订单号，部分退款时 refundId 和 refundNo  必传一个，同时传输时，以 refundId 为准
     */
    @Schema(description = "退款订单号")
    private String refundNo;

}
