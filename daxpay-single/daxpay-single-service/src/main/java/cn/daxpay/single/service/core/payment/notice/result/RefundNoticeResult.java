package cn.daxpay.single.service.core.payment.notice.result;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.result.PaymentCommonResult;
import cn.daxpay.single.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *
 * @author xxm
 * @since 2024/2/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "退款通知消息")
public class RefundNoticeResult extends PaymentCommonResult {

    /** 退款号 */
    @Schema(description = "退款号")
    private String refundNo;

    /** 商户退款号 */
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /**
     * 支付通道
     * @see PayChannelEnum
     */
    @Schema(description = "支付通道")
    private String channel;

    /** 退款金额 */
    @Schema(description = "退款金额")
    private Integer amount;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    /** 退款成功时间 */
    @Schema(description = "退款成功时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime finishTime;

    /** 退款创建时间 */
    @Schema(description = "退款创建时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime createTime;

    /** 商户扩展参数,回调时会原样返回 */
    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;
}
