package cn.daxpay.open.payment.trade.abnormal.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 异常订单结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "异常订单")
public class AbnormalOrderResult extends MchBaseResult {

    @Schema(description = "应用号")
    private String appId;

    @Schema(description = "平台交易号")
    private String tradeNo;

    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// @see cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum
    @Schema(description = "交易形态")
    private String tradeType;

    @Schema(description = "订单标题")
    private String title;

    @Schema(description = "交易金额(分)")
    private Long amount;

    @Schema(description = "币种")
    private String currency;

    /// @see cn.daxpay.open.payment.trade.enums.PayFundStatusEnum
    @Schema(description = "发现异常时的资金状态")
    private String tradeStatus;

    /// @see cn.daxpay.open.payment.trade.abnormal.enums.AbnormalOrderTypeEnum
    @Schema(description = "异常类型")
    private String abnormalType;

    /// @see cn.daxpay.open.payment.trade.abnormal.enums.AbnormalSourceEnum
    @Schema(description = "发现来源")
    private String source;

    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "支付渠道")
    private String provider;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "通道交易号")
    private String outOrderNo;

    @Schema(description = "通道侧订单状态")
    private String channelStatus;

    @Schema(description = "通道回调报文快照(JSON)")
    private String callbackNotifyInfo;

    /// @see cn.daxpay.open.payment.trade.abnormal.enums.AbnormalHandleStatusEnum
    @Schema(description = "处理状态")
    private String handleStatus;

    @Schema(description = "处置动作")
    private String handleAction;

    @Schema(description = "处理人账号")
    private String handler;

    @Schema(description = "处置时间")
    private OffsetDateTime handleTime;

    @Schema(description = "处置备注")
    private String handleRemark;
}
