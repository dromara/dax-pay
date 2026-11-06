package cn.daxpay.open.payment.merchant.result.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 商户收银支付/订单状态结果
///
/// status 直接使用 [cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum] 的 code
/// (wait/progress/success/close/cancel/fail/timeout), 供收银端统一轮询展示。
@Data
@Accessors(chain = true)
@Schema(title = "商户收银支付/订单状态结果")
public class MchCashierPayResult {

    /// 订单ID
    @Schema(description = "订单ID")
    private Long orderId;

    /// 平台业务单号
    @Schema(description = "平台业务单号")
    private String orderNo;

    /// 商户业务单号
    @Schema(description = "商户业务单号")
    private String bizOrderNo;

    /// 资金交易号
    @Schema(description = "资金交易号")
    private String tradeNo;

    /// 支付状态
    /// @see cn.daxpay.open.platform.core.enums.pay.pay.PayStatusEnum
    @Schema(description = "支付状态")
    private String status;

    /// 支付金额(分)
    @Schema(description = "支付金额(分)")
    private Long amount;

    /// 支付通道
    @Schema(description = "支付通道")
    private String channel;

    /// 支付渠道
    @Schema(description = "支付渠道")
    private String provider;

    /// 支付成功时间
    @Schema(description = "支付成功时间")
    private OffsetDateTime payTime;

    /// 业务单过期时间
    @Schema(description = "业务单过期时间")
    private OffsetDateTime expiredTime;

    /// 错误信息(通道结果未知/失败时保留)
    @Schema(description = "错误信息")
    private String errorMsg;

}
