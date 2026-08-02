package cn.daxpay.open.payment.unipay.param.trade.refund;

import cn.daxpay.open.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/// # 统一退款发起参数
///
/// 从已支付订单发起退款, 支持部分退款。
/// 原支付单定位: tradeNo(资金交易号) 与 bizOrderNo(商户业务单号) 至少传一个, 优先使用 tradeNo。
/// 与内部编排参数 [cn.daxpay.open.payment.trade.runtime.param.RefundParam] 同名但职责不同:
/// 本类是对外签名 DTO(含 mchNo/appId/sign), Controller 层负责转换。
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "统一退款参数")
public class RefundParam extends MerchantPaymentCommonParam {

    /// 原支付资金交易号(平台 tradeNo)
    @Schema(description = "原支付资金交易号")
    @Size(max = 100, message = "{validation.field.tradeNo.size}")
    private String tradeNo;

    /// 原支付商户业务订单号
    @Schema(description = "原支付商户业务订单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    /// 退款金额(单位: 分, 最小货币单位)
    @Schema(description = "退款金额(分)")
    @NotNull(message = "{validation.field.amount.notNull}")
    @Positive(message = "{validation.field.amount.positive}")
    @Min(value = 1, message = "{validation.field.amount.min}")
    private Long amount;

    /// 退款原因
    @Schema(description = "退款原因")
    @Size(max = 50, message = "{validation.field.reason.size}")
    private String reason;

    /// 商户退款号(可选, 不传则由系统生成)
    @Schema(description = "商户退款号")
    @Size(max = 100, message = "{validation.field.bizRefundNo.size}")
    private String bizRefundNo;

}
