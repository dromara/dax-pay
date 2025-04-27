package org.dromara.daxpay.service.param.order.refund;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 退款创建参数
 * @author xxm
 * @since 2024/8/16
 */
@Data
@Accessors(chain = true)
@Schema(title = "退款创建参数")
public class RefundCreateParam {

    /** 支付订单号 */
    @Schema(description = "支付订单号")
    @NotBlank(message = "支付订单号不可为空")
    private String orderNo;

    /** 退款金额 */
    @Schema(description = "退款金额")
    @NotNull(message = "退款金额不可为空")
    @DecimalMin(value = "0.01", message = "退款金额不可小于0.01元")
    private BigDecimal amount;

    /** 原因 */
    @Schema(description = "原因")
    private String reason;
}
