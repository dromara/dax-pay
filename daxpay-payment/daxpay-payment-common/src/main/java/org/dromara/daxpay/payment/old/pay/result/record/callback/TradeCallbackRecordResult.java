package org.dromara.daxpay.payment.old.pay.result.record.callback;

import org.dromara.daxpay.platform.core.enums.pay.notice.CallbackStatusEnum;
import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import org.dromara.daxpay.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 回调记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "回调记录")
public class TradeCallbackRecordResult extends MchTradeBaseResult {

    @Schema(description = "交易号")
    private String tradeNo;

    @Schema(description = "通道交易号")
    private String outTradeNo;
    /// 支付通道
    /// @see ChannelEnum#getCode()
    @Schema(description = "支付产品")
    private String product;

    @Schema(description = "支付通道")
    private String channel;

    /// 回调类型
    /// @see TradeTypeEnum
    @Schema(description = "回调类型")
    private String callbackType;

    /// 通知消息
    @Schema(description = "通知消息")
    private String notifyInfo;

    /// 回调处理状态
    /// @see CallbackStatusEnum
    @Schema(description = "回调处理状态")
    private String status;

    @Schema(description = "调整号")
    private String adjustNo;

    /// 提示信息
    @Schema(description = "提示信息")
    private String errorMsg;

}


