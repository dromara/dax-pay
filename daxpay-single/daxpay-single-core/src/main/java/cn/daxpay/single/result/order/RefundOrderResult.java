package cn.daxpay.single.result.order;

import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import cn.daxpay.single.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款订单数据
 * @author xxm
 * @since 2024/1/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款订单数据")
public class RefundOrderResult extends PaymentCommonResult {

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "商户退款号")
    private String bizRefundNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "退款金额")
    private BigDecimal amount;

    @Schema(description = "退款完成时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime finishTime;

    /**
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;
}
