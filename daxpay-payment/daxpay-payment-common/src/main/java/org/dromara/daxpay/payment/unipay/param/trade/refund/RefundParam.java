package org.dromara.daxpay.payment.unipay.param.trade.refund;

import org.dromara.daxpay.platform.core.enums.pay.trade.TradeSourceEnum;
import org.dromara.daxpay.payment.unipay.param.MerchantPaymentCommonParam;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/// # 退款参数，适用于组合支付的订单退款操作中，
///
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "退款参数")
public class RefundParam extends MerchantPaymentCommonParam {

    /// 支付订单号/商户订单号至少要传输一个，支付订单号 > 商户订单号
    @Schema(description = "商户退款号")
    @NotBlank(message = "{validation.field.bizRefundNo.notBlank}")
    @Size(max = 100, message = "{validation.field.bizRefundNo.size}")
    private String bizRefundNo;

    /// 支付订单号/商户订单号至少要传输一个，支付订单号 > 商户订单号
    @Schema(description = "订单号")
    @Size(max = 100, message = "{validation.field.orderNo.size}")
    private String orderNo;

    /// 支付订单号/商户订单号至少要传输一个，支付订单号 > 商户订单号
    @Schema(description = "商户订单号")
    @Size(max = 100, message = "{validation.field.bizOrderNo.size}")
    private String bizOrderNo;

    /// 退款金额
    @Schema(description = "退款金额")
    @NotNull(message = "{validation.field.amount.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.amount.decimalMin}")
    @Digits(integer = 8, fraction = 2, message = "{validation.field.amount.digits}")
    private BigDecimal amount;

    /// 预留的退款扩展参数
    @Schema(description = "退款扩展参数")
    @Size(max = 2048, message = "{validation.field.extraParam.size}")
    private String extraParam;

    /// 退款原因
    @Schema(description = "退款原因")
    @Size(max = 50, message = "{validation.field.reason.size}")
    private String reason;

    /// 商户扩展参数,回调时会原样返回
    @Schema(description = "商户扩展参数")
    @Size(max = 500, message = "{validation.field.attach.size}")
    private String attach;

    /// 订单来源
    /// @see TradeSourceEnum
    @Schema(description = "订单来源", hidden = true)
    @Null(message = "{validation.field.orderSource.mustBeNull}")
    private String source;

    /// 异步通知地址
    @Schema(description = "异步通知地址")
    @Size(max = 200, message = "{validation.field.notifyUrl.size}")
    private String notifyUrl;

}

