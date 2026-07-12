package cn.daxpay.open.payment.core.trade.runtime.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/// # 退款发起参数(管理端)
///
/// 从已支付的订单发起退款, 支持部分退款。
/// orderNo 与 bizOrderNo 至少传一个, 优先使用 orderNo。
@Data
@Schema(title = "退款发起参数")
public class PayRefundParam {

    /// 原支付订单号(平台 tradeNo)
    @Schema(description = "原支付订单号")
    private String orderNo;

    /// 商户业务订单号
    @Schema(description = "商户业务订单号")
    private String bizOrderNo;

    /// 退款金额(单位: 分)
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    @Schema(description = "退款金额(分)")
    private Long amount;

    /// 退款原因
    @Schema(description = "退款原因")
    private String reason;

    /// 商户退款号(可选, 不传则由系统生成)
    @Schema(description = "商户退款号")
    private String bizRefundNo;
}
