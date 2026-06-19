package cn.daxpay.open.payment.merchant.param.miniapp.quickly;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/// # 小程序快捷被扫支付参数
///
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷付款码支付参数")
public class QuicklyBarPayParam {

    @NotNull(message = "{validation.field.amount.notNull}")
    @DecimalMin(value = "0.01", message = "{validation.field.amount.decimalMin}")
    @Digits(integer = 8, fraction = 2, message = "{validation.field.amount.digits}")
    @Schema(description = "支付金额")
    private BigDecimal amount;

    /// 支付描述
    @Schema(description = "支付描述")
    @Size(max = 50, message = "{validation.field.description.size}")
    private String description;

    @NotBlank(message = "{validation.field.authCode.notBlank}")
    @Size(max = 128, message = "{validation.field.authCode.size}")
    @Schema(description = "付款码")
    private String authCode;

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用号")
    private String appId;

}
