package cn.daxpay.open.payment.unipay.result.trade.refund;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 退款订单(统一查询)
///
/// 对外查询结果(精简), 不复用管理端 [cn.daxpay.open.payment.trade.order.result.RefundOrderResult]
/// (后者含 mchName 翻译、channelMchNo 等内部字段, 不宜对商户暴露)。
@Data
@Accessors(chain = true)
@Schema(title = "退款订单")
public class RefundOrderResult {

    /// 平台退款号
    @Schema(description = "平台退款号")
    private String refundNo;

    /// 商户退款号
    @Schema(description = "商户退款号")
    private String bizRefundNo;

    /// 原支付资金交易号
    @Schema(description = "原支付资金交易号")
    private String tradeNo;

    /// 原支付商户业务订单号
    @Schema(description = "原支付商户业务订单号")
    private String bizOrderNo;

    /// 通道退款流水号
    @Schema(description = "通道退款流水号")
    private String outRefundNo;

    /// 退款金额(分)
    @Schema(description = "退款金额(分)")
    private Long amount;

    /// 订单总金额(分)
    @Schema(description = "订单总金额(分)")
    private Long orderAmount;

    /// 退款状态
    /// @see RefundOrderStatusEnum
    @Schema(description = "退款状态")
    private String status;

    /// 退款原因
    @Schema(description = "退款原因")
    private String reason;

    /// 退款完成时间(UTC)
    @Schema(description = "退款完成时间(UTC)")
    private OffsetDateTime finishTime;

    /// 错误信息
    @Schema(description = "错误信息")
    private String errorMsg;

}
