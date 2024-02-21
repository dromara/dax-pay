package cn.bootx.platform.daxpay.service.dto.order.refund;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录
 *
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款记录")
public class RefundOrderDto extends BaseDto {

    @Schema(description = "支付号")
    private Long paymentId;

    @Schema(description = "关联的业务id")
    private String businessNo;

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "是否含有异步通道")
    private boolean asyncPay;

    /**
     * 异步通道
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @Schema(description = "异步通道")
    private String asyncChannel;

    @Schema(description = "支付网关订单号")
    private String gatewayOrderNo;

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

    /**
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "错误码")
    private String errorCode;

    @Schema(description = "错误信息")
    private String errorMsg;

}
