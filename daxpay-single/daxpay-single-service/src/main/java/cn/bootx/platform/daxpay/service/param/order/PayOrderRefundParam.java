package cn.bootx.platform.daxpay.service.param.order;

import cn.bootx.platform.daxpay.param.pay.RefundChannelParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 支付订单退款发起参数
 * @author xxm
 * @since 2024/1/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付订单退款发起参数")
public class PayOrderRefundParam {

    /** 支付id */
    @Schema(description = "支付id")
    private Long paymentId;

    /** 原因 */
    @Schema(description = "原因")
    private String reason;

    /** 退款明细 */
    @Schema(description = "退款明细")
    private List<RefundChannelParam> refundChannels;
}
