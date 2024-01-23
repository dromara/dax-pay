package cn.bootx.platform.daxpay.result.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 支付退款通道明细数据
 * @author xxm
 * @since 2024/1/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "通道退款订单")
public class RefundChannelOrderResult {

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "通道支付单id")
    private Long payChannelId;

    @Schema(description = "支付网关订单号")
    private String gatewayOrderNo;

    @Schema(description = "异步支付方式")
    private boolean async;

    @Schema(description = "订单金额")
    private Integer totalAmount;

    @Schema(description = "退款金额")
    private Integer amount;
}
