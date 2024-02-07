package cn.bootx.platform.daxpay.result.order;

import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 退款订单数据
 * @author xxm
 * @since 2024/1/16
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款订单数据")
public class RefundOrderResult {

    @Schema(description = "退款ID")
    private Long refundId;

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "关联支付ID")
    private Long paymentId;

    @Schema(description = "关联支付业务号")
    private String businessNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "退款金额")
    private BigDecimal amount;

    @Schema(description = "剩余可退")
    private BigDecimal refundableBalance;

    /**
     * 异步支付通道发给网关的退款号, 用与将记录关联起来
     */
    @Schema(description = "支付网关订单号")
    private String gatewayOrderNo;

    @Schema(description = "退款完成时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime refundTime;

    /**
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "通道退款订单")
    private List<RefundChannelOrderResult> channels;
}
