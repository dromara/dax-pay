package cn.daxpay.single.service.param.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付订单退款发起参数
 * @author xxm
 * @since 2024/1/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付订单退款发起参数")
public class PayOrderRefundParam {

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    private String orderNo;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private Integer amount;

    /** 原因 */
    @Schema(description = "原因")
    private String reason;
}
