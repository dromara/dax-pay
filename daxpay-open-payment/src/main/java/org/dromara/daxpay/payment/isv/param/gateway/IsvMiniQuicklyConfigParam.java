package org.dromara.daxpay.payment.isv.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

/**
 * 小程序快捷支付配置参数
 * @author xxm
 * @since 2025/10/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷支付配置参数")
public class IsvMiniQuicklyConfigParam  {

    @Schema(description = "限制用户支付类型")
    @Size(max = 50, message = "限制用户支付类型长度不能超过50")
    private String limitPay;

    @Schema(description = "服务商号")
    @NotBlank(message = "服务商号不能为空")
    @Size(max = 50, message = "服务商号长度不能超过50")
    private String isvNo;
}
