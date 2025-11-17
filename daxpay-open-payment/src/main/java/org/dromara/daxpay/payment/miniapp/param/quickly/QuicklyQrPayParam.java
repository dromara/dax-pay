package org.dromara.daxpay.payment.miniapp.param.quickly;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 小程序快捷二维码支付参数
 * @author xxm
 * @since 2025/4/21
 */
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷二维码支付参数")
public class QuicklyQrPayParam {

    @NotNull(message = "支付金额不可为空")
    @DecimalMin(value = "0.01", message = "支付金额不可小于0.01元")
    @Digits(integer = 8, fraction = 2, message = "支付金额精度到分, 且要小于一亿元")
    @Schema(description = "支付金额")
    private BigDecimal amount;

    /** 支付描述 */
    @Schema(description = "支付描述")
    @Size(max = 50, message = "支付描述不可超过50位")
    private String description;


    @NotBlank(message = "商户AppId不可为空")
    @Schema(description = "应用号")
    private String appId;

}
