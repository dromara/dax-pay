package org.dromara.daxpay.payment.unipay.param.gateway;

import org.dromara.daxpay.payment.unipay.enums.CheckoutCounterTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取收银台认证结果参数
 * @author xxm
 * @since 2024/9/28
 */
@Data
@Accessors(chain = true)
@Schema(title = "获取收银台认证结果参数")
public class CheckoutCounterAuthCodeParam {

    /** 支付订单号 */
    @NotBlank(message = "支付订单号不可为空")
    @Schema(description = "支付订单号")
    private String orderNo;

    /**
     * 支付场景
     * @see CheckoutCounterTypeEnum
     */
    @Schema(description = "支付场景")
    private String scene;

    /** 认证Code */
    @Schema(description = "认证Code")
    private String authCode;
}
