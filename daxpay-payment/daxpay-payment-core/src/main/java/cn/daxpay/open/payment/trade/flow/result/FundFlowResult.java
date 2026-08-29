package cn.daxpay.open.payment.trade.flow.result;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 资金流水结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "资金流水")
public class FundFlowResult extends MchBaseResult {

    @Schema(description = "应用号")
    private String appId;

    /// @see cn.daxpay.open.payment.trade.flow.enums.FundFlowTypeEnum
    @Schema(description = "流水类型(pay-收款/refund-退款)")
    private String flowType;

    @Schema(description = "原支付交易号")
    private String tradeNo;

    @Schema(description = "退款单号(仅退款流水)")
    private String refundNo;

    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    @Schema(description = "订单标题")
    private String title;

    @Schema(description = "流水金额(分)")
    private Long amount;

    @Schema(description = "币种")
    private String currency;

    @Schema(description = "支付通道")
    private String channel;

    @Schema(description = "支付渠道")
    private String provider;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "通道交易号")
    private String outOrderNo;

    @Schema(description = "资金完成时间")
    private OffsetDateTime finishTime;
}
