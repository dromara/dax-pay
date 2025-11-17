package org.dromara.daxpay.payment.merchant.param.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 小程序快捷支付配置
 * @author xxm
 * @since 2025/10/10
 */
@Data
@Accessors(chain = true)
@Schema(title = "小程序快捷支付配置")
public class MiniQuicklyConfigParam {

    /** 限制小程序支付方式 */
    @Schema(description = "限制小程序支付方式")
    @Size(max = 128, message = "限制小程序支付方式不能超过128位")
    private String limitPay;

    /** 小程序付款终端号 */
    @Schema(description = "小程序付款终端号")
    @Size(max = 32, message = "小程序付款终端号不能超过32位")
    private String terminalNo;

    /** 应用号 */
    @NotBlank(message = "应用号不可为空")
    @Size(max = 32, message = "应用号不可超过32位")
    @Schema(description = "应用号")
    private String appId;
}
