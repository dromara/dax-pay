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
@Schema(title = "支付退款通道明细数据")
public class RefundOrderChannelResult {

    @Schema(description = "通道")
    private String channel;

    /** 关联支付网关退款请求号 */
    @Schema(description = "异步方式关联退款请求号(部分退款情况)")
    private String refundRequestNo;

    @Schema(description = "异步支付方式")
    private boolean async;

    @Schema(description = "退款金额")
    private Integer amount;
}
