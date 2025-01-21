package org.dromara.daxpay.service.result.notice.callback;

import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.core.result.MchAppResult;
import org.dromara.daxpay.core.result.trade.pay.PayOrderResult;
import org.dromara.daxpay.core.result.trade.refund.RefundOrderResult;
import org.dromara.daxpay.core.result.trade.transfer.TransferOrderResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 商户回调消息发送记录
 * @author xxm
 * @since 2024/8/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户回调消息发送记录")
public class MerchantCallbackTaskResult extends MchAppResult {

    /** 本地交易ID */
    @Schema(description = "本地交易ID")
    private Long tradeId;

    /** 平台交易号 */
    @Schema(description = "平台交易号")
    private String tradeNo;

    /**
     * 交易类型
     * @see TradeTypeEnum
     */
    @Schema(description = "交易类型")
    private String tradeType;

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

    /** 下次发送时间 */
    @Schema(description = "下次发送时间")
    private LocalDateTime nextTime;

    /** 发送次数 */
    @Schema(description = "发送次数")
    private Integer sendCount;

    /** 延迟重试次数 */
    @Schema(description = "延迟重试次数")
    private Integer delayCount;

    /** 发送地址 */
    @Schema(description = "发送地址")
    private String url;

    /** 最后发送时间 */
    @Schema(description = "最后发送时间")
    private LocalDateTime latestTime;
}
