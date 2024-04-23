package cn.bootx.platform.daxpay.service.core.payment.notice.result;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import cn.bootx.platform.daxpay.serializer.LocalDateTimeToTimestampSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *
 * @author xxm
 * @since 2024/2/22
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款通知消息")
public class RefundNoticeResult {

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /**
     * @see PayChannelEnum
     */
    @Schema(description = "异步通道")
    private String channel;

    @Schema(description = "退款金额")
    private Integer amount;

    /**
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "退款成功时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime refundTime;

    @Schema(description = "退款创建时间")
    @JsonSerialize(using = LocalDateTimeToTimestampSerializer.class)
    private LocalDateTime createTime;

    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    @Schema(description = "签名")
    private String sign;
}
