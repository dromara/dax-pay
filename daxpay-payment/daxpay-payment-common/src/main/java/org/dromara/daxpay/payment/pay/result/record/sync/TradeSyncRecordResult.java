package org.dromara.daxpay.payment.pay.result.record.sync;

import org.dromara.daxpay.platform.core.enums.pay.channel.ChannelEnum;
import org.dromara.daxpay.payment.common.result.MchTradeBaseResult;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付同步记录
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付同步订单")
public class TradeSyncRecordResult extends MchTradeBaseResult {

    /// 平台交易号
    @Schema(description = "本地订单ID")
    private String tradeNo;

    /// 商户交易号
    @Schema(description = "商户交易号")
    private String bizTradeNo;

    /// 通道交易号
    @Schema(description = "通道交易号")
    private String outTradeNo;

    /// 三方支付返回状态
    @Schema(description = "网关返回状态")
    private String outTradeStatus;

    /// 交易类型
    /// @see TradeTypeEnum
    @Schema(description = "交易类型")
    private String tradeType;

    /// 同步通道
    /// @see ChannelEnum#getCode()
    @Schema(description = "支付产品")
    private String product;

    @Schema(description = "同步通道")
    private String channel;

    /// 网关返回的同步消息
    @Schema(description = "同步消息")
    private String syncInfo;

    /// 支付单如果状态不一致, 是否进行调整
    @Schema(description = "是否进行调整")
    private boolean adjust;

    /// 调整单号
    @Schema(description = "调整单号")
    private String adjustNo;

    /// 错误码
    @Schema(description = "错误码")
    private String errorCode;

    /// 错误消息
    @Schema(description = "错误消息")
    private String errorMsg;

    /// 客户端IP
    @Schema(description = "客户端IP")
    private String clientIp;
}

