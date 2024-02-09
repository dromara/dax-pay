package cn.bootx.platform.daxpay.service.dto.order.pay;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付订单关联通道信息
 * @author xxm
 * @since 2024/1/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付订单关联通道信息")
public class PayChannelOrderDto extends BaseDto {

    @Schema(description = "支付id")
    private Long paymentId;

    @Schema(description = "异步支付方式")
    private boolean async;

    @Schema(description = "通道")
    private String channel;

    @Schema(description = "支付方式")
    private String payWay;

    /**
     * 异步支付通道发给网关的退款号, 用与将记录关联起来
     */
    @Schema(description = "关联网关支付号")
    private String gatewayOrderNo;

    @Schema(description = "金额")
    private Integer amount;

    @Schema(description = "可退款金额")
    private Integer refundableBalance;
    /**
     * 支付状态
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;
}
