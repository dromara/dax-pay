package cn.bootx.platform.daxpay.service.param.order;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
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
    private Long orderNo;

    /**
     * 支付通道
     * @see PayChannelEnum#getCode()
     */
    private String channel;

    /**
     * 退款金额
     */
    private Integer amount;

    /** 原因 */
    @Schema(description = "原因")
    private String reason;
}
