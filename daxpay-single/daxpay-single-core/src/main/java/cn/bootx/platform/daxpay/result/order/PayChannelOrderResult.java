package cn.bootx.platform.daxpay.result.order;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付订单通道响应参数
 * @author xxm
 * @since 2024/1/16
 */
@Data
@Schema(title = "支付订单通道响应参数")
public class PayChannelOrderResult {


    @Schema(description = "异步支付方式")
    private boolean async;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "支付方式")
    private String payWay;

    /**
     * 第三方支付网关生成的订单号, 用与将记录关联起来
     */
    @Schema(description = "关联网关支付号")
    private String gatewayOrderNo;

    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

    @Schema(description = "金额")
    private Integer amount;

    @Schema(description = "可退款金额")
    private Integer refundableBalance;
}
