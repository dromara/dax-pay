package org.dromara.daxpay.payment.old.pay.param.order.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 退款创建参数
///
@Data
@Accessors(chain = true)
@Schema(title = "退款创建参数")
public class RefundCreateParam {

    /// 支付订单号
    @Schema(description = "支付订单号")
    @NotBlank(message = "{validation.field.orderNo.notBlank}")
    private String orderNo;

    /// 退款金额
    @Schema(description = "退款金额")
    @NotNull(message = "{validation.field.amount.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.amount.decimalMin}")
    private BigDecimal amount;

    /// 原因
    @Schema(description = "原因")
    private String reason;
}
