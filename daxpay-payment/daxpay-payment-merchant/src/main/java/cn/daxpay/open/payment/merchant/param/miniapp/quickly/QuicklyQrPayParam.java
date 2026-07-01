package cn.daxpay.open.payment.merchant.param.miniapp.quickly;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 小程序快捷二维码支付参数
///
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷二维码支付参数")
public class QuicklyQrPayParam {

    @NotNull(message = "{validation.field.amount.notNull}")
    @Min(value = 1, message = "{validation.field.amount.min}")
    @Max(value = 9999999999L, message = "{validation.field.amount.max}")
    @Schema(description = "支付金额(分)")
    private Long amount;

    /// 支付描述
    @Schema(description = "支付描述")
    @Size(max = 50, message = "{validation.field.description.size}")
    private String description;

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "应用号")
    private String appId;

}
