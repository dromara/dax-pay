package cn.bootx.platform.daxpay.result.order;

import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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

    @Schema(description = "异步支付方式")
    private boolean async;

    @Schema(description = "订单金额")
    private Integer orderAmount;

    @Schema(description = "退款金额")
    private Integer amount;

    @Schema(description = "剩余可退余额")
    private Integer refundableAmount;

    /**
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "退款完成时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime refundTime;

}
