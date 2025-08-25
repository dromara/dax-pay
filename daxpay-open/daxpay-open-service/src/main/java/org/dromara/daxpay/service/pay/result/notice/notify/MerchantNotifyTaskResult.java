package org.dromara.daxpay.service.pay.result.notice.notify;

import org.dromara.daxpay.core.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.core.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.core.result.trade.transfer.TransferOrderResult;
import org.dromara.daxpay.service.merchant.result.info.MchResult;
import org.dromara.daxpay.service.pay.enums.NotifyContentTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 商户订阅通知任务
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户订阅通知任务")
public class MerchantNotifyTaskResult extends MchResult {


    /** 本地交易ID */
    @Schema(description = "本地交易ID")
    private Long tradeId;

    /** 平台交易号 */
    @Schema(description = "平台交易号")
    private String tradeNo;

    /**
     * 通知类型
     * @see NotifyContentTypeEnum
     */
    @Schema(description = "通知类型")
    private String notifyType;

    /**
     * 消息内容
     * @see PayOrderResult
     * @see RefundOrderResult
     * @see TransferOrderResult
     */
    @Schema(description = "消息内容")
    private String content;

    /** 是否发送成功 */
    @Schema(description = "是否发送成功")
    private boolean success;

    /** 发送次数 */
    @Schema(description = "发送次数")
    private Integer sendCount;

    /** 延迟次数 */
    @Schema(description = "延迟次数")
    private Integer delayCount;

    /** 下次发送时间 */
    @Schema(description = "下次发送时间")
    private LocalDateTime nextTime;

    /** 最后发送时间 */
    @Schema(description = "最后发送时间")
    private LocalDateTime latestTime;
}
