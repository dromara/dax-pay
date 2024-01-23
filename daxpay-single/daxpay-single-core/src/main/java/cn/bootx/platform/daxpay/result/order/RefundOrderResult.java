package cn.bootx.platform.daxpay.result.order;

import cn.bootx.platform.daxpay.code.PayRefundStatusEnum;
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
    @Schema(description = "支付号")
    private Long paymentId;

    @Schema(description = "关联的业务id")
    private String businessNo;

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "退款金额")
    private BigDecimal amount;

    @Schema(description = "剩余可退")
    private BigDecimal refundableBalance;

    @Schema(description = "退款终端ip")
    private String clientIp;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "通道退款订单")
    private List<RefundChannelOrderResult> channels;

    /**
     * @see PayRefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;
}
