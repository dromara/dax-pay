package cn.bootx.platform.daxpay.service.core.payment.notice.result;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author xxm
 * @since 2024/2/22
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款通知消息")
public class RefundNoticeResult {

    @Schema(description = "退款ID")
    private Long refundId;

    @Schema(description = "退款号")
    private String refundNo;

    @Schema(description = "是否含有异步通道")
    private boolean asyncPay;

    /**
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @Schema(description = "异步通道")
    private String asyncChannel;

    @Schema(description = "退款金额")
    private Integer amount;

    @Schema(description = "退款通道信息")
    private List<RefundChannelResult> refundChannels;

    /**
     * @see RefundStatusEnum
     */
    @Schema(description = "退款状态")
    private String status;

    @Schema(description = "退款成功时间")
    private LocalDateTime refundTime;

    @Schema(description = "退款创建时间")
    private LocalDateTime createTime;

    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    @Schema(description = "签名")
    private String sign;
}
