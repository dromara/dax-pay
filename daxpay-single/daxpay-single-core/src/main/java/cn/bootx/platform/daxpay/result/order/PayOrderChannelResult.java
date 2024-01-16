package cn.bootx.platform.daxpay.result.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付订单通道响应参数
 * @author xxm
 * @since 2024/1/16
 */
@Data
@Schema(title = "支付订单通道响应参数")
public class PayOrderChannelResult {

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "支付方式")
    private String payWay;

    @Schema(description = "异步支付方式")
    private boolean async;

    @Schema(description = "金额")
    private Integer amount;
}
