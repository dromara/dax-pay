package cn.bootx.platform.daxpay.service.core.payment.notice.result;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付异步通知类
 * @author xxm
 * @since 2024/1/7
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付异步通知类")
public class PayNoticeResult {

    @Schema(description = "支付ID")
    private Long paymentId;

    @Schema(description = "业务号")
    private String businessNo;

    @Schema(description = "是否是异步支付")
    private boolean asyncPay;

    /**
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @Schema(description = "异步支付通道")
    private String asyncChannel;

    @Schema(description = "支付金额")
    private Integer amount;

    @Schema(description = "支付通道信息")
    private List<PayChannelResult> payChannels;

    /**
     * @see PayStatusEnum
     */
    @Schema(description = "支付状态")
    private String status;

    @Schema(description = "支付成功时间")
    private LocalDateTime payTime;

    @Schema(description = "支付创建时间")
    private LocalDateTime createTime;

    @Schema(description = "商户扩展参数,回调时会原样返回")
    private String attach;

    @Schema(description = "签名")
    private String sign;

}
